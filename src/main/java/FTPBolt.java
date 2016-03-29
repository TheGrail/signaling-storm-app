import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import utils.CBSSAPCC;
import utils.CBSSAPMM;
import utils.CBSSAPSM;
import utils.LastLocation;
import utils.MdnUtil;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FTPBolt extends BaseRichBolt {

	private static final long serialVersionUID = 4176181489956056124L;
	private OutputCollector m_collector;
	
	private HashMap<String, LastLocation> m_mapMdn2LastLocation;

	public void execute(Tuple tuple) {
		String source = tuple.getStringByField("source");
		String mdn = tuple.getStringByField("mdn");
		String line = tuple.getStringByField("line");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		
		// mdn修正
		mdn = MdnUtil.reviseMdn(mdn);
		if(mdn != null){
			String[] cols = line.split(",", -1);
			
			long timestamp = 0;
			int lac = 0;
			int cid = 0;
			try {
				// 数据剥离
				if(source.equals("CBSSAPSM")){
					timestamp = df.parse(cols[CBSSAPSM.START_TIME]).getTime();
					lac = Integer.parseInt(cols[CBSSAPSM.START_LAC]);
					cid = Integer.parseInt(cols[CBSSAPSM.START_CI]);
				}else if(source.equals("CBSSAPCC")){
					timestamp = df.parse(cols[CBSSAPCC.START_TIME]).getTime();
					lac = Integer.parseInt(cols[CBSSAPCC.END_LAC]);
					cid = Integer.parseInt(cols[CBSSAPCC.END_CI]);
				}else if(source.equals("CBSSAPMM")){
					timestamp = df.parse(cols[CBSSAPMM.START_TIME]).getTime();
					lac = Integer.parseInt(cols[CBSSAPMM.START_LAC]);
					cid = Integer.parseInt(cols[CBSSAPMM.START_CI]);
				}
				// 数据处理
				// - 实时扇区用户数据
				if(m_mapMdn2LastLocation.containsKey(mdn)){
					LastLocation lastloc = m_mapMdn2LastLocation.get(mdn);
					if(timestamp > lastloc.getTimestamp()){
						// 移除原位置
						String key = lastloc.getLAC() + "-" + lastloc.getCID();
						String val = mdn;
						m_collector.emit("redis", new Values("del", key, val));
						// 插入新位置
						key = lac + "-" + cid;
						val = mdn;
						m_collector.emit("redis", new Values("ins", key, val));
						m_mapMdn2LastLocation.put(mdn, new LastLocation(timestamp, lac, cid));
					}
				}else{
					m_mapMdn2LastLocation.put(mdn, new LastLocation(timestamp, lac, cid));
				}
				// - 用户历史信令数据
				String date = df2.format(new Date(timestamp));
				String key = mdn + "_" + date;
				String val = String.format("%s,%s,%s", lac + "-" + cid, source, timestamp);
				m_collector.emit("redis", new Values("ins", key, val));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		m_collector.ack(tuple);
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		m_collector = collector;
		m_mapMdn2LastLocation = new HashMap<String, LastLocation>();		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("redis", new Fields("operation", "key", "val"));
	}

}
