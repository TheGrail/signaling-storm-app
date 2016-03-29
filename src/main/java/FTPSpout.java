import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.net.ftp.FTPClient;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;


public class FTPSpout extends BaseRichSpout {

	private static final long serialVersionUID = 3692028380088236881L;
	private SpoutOutputCollector m_collector;
	private FTPSyncThread m_thread;
	private static long m_lSleepInterval = 1000;
	private static HashMap<String, Integer> m_mapSourceMdnIndex;

	public void nextTuple() {
		String filename = m_thread.getNextFile();
		if(filename == null){
			try {
				Thread.sleep(m_lSleepInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			String source = filename.split("_", -1)[2];
			int nMdnIndex = m_mapSourceMdnIndex.get(source);
			try {
				FTPClient client = FTPSyncThread.getFTPClient();
				InputStream in = client.retrieveFileStream(filename);
				BufferedInputStream bis = new BufferedInputStream(in);
				BZip2CompressorInputStream bz2in = new BZip2CompressorInputStream(bis);
				TarArchiveInputStream tarin = new TarArchiveInputStream(bz2in);
				TarArchiveEntry entry = null;
				while ((entry = tarin.getNextTarEntry()) != null){
					byte[] bytes = new byte[(int) entry.getSize()];
					tarin.read(bytes);
					BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
					String line = null;
					while((line = br.readLine()) != null){
						String mdn = line.split(",", -1)[nMdnIndex];
						if(mdn.length() >= 2){
							m_collector.emit(new Values(source, mdn, line));
						}
					}
					br.close();
				}
				tarin.close();
				bz2in.close();
				bis.close();
				in.close();
				client.disconnect();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void open(Map config, TopologyContext context, SpoutOutputCollector collector) {
		m_collector = collector;
		m_mapSourceMdnIndex = new HashMap<String, Integer>();
		m_mapSourceMdnIndex.put("CBSSAPSM", 12);		// ∂Ã–≈ - ±ªΩ–∫≈¬Î
		m_mapSourceMdnIndex.put("CBSSAPCC", 11);		// ∫ÙΩ– - ±ªΩ–∫≈¬Î
		m_mapSourceMdnIndex.put("CBSSAPMM", 7);			// Œª÷√
		m_thread = new FTPSyncThread();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("source", "mdn", "line"));
	}

}
