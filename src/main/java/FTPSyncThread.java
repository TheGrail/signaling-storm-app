import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPSyncThread implements Runnable {

	private static final String m_ftpServer = "132.228.178.140";
	private static final String m_ftpUsername = "zc";
	private static final String m_ftpPassword = "zctt123";
	private static final String m_ftpRootDir = "/dsndata1/data";
	private static final long m_lCheckInterval = 1000 * 60 * 5;
	
	private Thread m_thread;
	private Lock m_lock = new ReentrantLock();
	private HashSet<String> m_setFiles = new HashSet<String>();
	private Queue<String> m_queueFiles = new ConcurrentLinkedQueue<String>();
	private boolean m_bInitialized = false;
	
	public FTPSyncThread(){		
		m_thread = new Thread(this, "MyFTPClient");
		m_thread.start();
	}
	
	public void run() {
		while(true){
			try {
				m_lock.lock();
				HashSet<String> setTempFiles = new HashSet<String>();
				FTPClient client = new FTPClient();
				client.connect(m_ftpServer);
				client.login(m_ftpUsername, m_ftpPassword);
				client.setFileType(FTP.BINARY_FILE_TYPE);
				FTPFile[] files = client.listFiles(m_ftpRootDir);
				for(FTPFile file : files){
					String filename = file.getName();
					if(filename.endsWith(".bz2")){
						setTempFiles.add(filename);
						if(!m_setFiles.contains(filename) && m_bInitialized){
							m_queueFiles.add(m_ftpRootDir + "/" + filename);
						}
					}
				}
				client.disconnect();
				m_setFiles = setTempFiles;
				m_bInitialized = true;
				m_lock.unlock();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(m_lCheckInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getNextFile(){
		return m_queueFiles.poll();
	}
	
	public static FTPClient getFTPClient() throws SocketException, IOException{
		FTPClient client = new FTPClient();
		client.connect(m_ftpServer);
		client.login(m_ftpUsername, m_ftpPassword);
		client.setFileType(FTP.BINARY_FILE_TYPE);
		return client;
	}
	
}
