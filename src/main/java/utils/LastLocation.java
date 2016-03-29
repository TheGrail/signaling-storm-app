package utils;

public class LastLocation {

	private long m_lTimestamp;
	private int m_nLAC;
	private int m_nCID;
	
	public LastLocation(long timestamp, int lac, int cid){
		m_lTimestamp = timestamp;
		m_nLAC = lac;
		m_nCID = cid;
	}
	
	public long getTimestamp(){
		return m_lTimestamp;
	}
	
	public int getLAC(){
		return m_nLAC;
	}
	
	public int getCID(){
		return m_nCID;
	}
	
}
