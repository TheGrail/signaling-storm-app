import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;


public class MyTopology {
	
	private static final String m_strTopologyName = "SignalingTopology";

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// �ж��Ǳ���ģʽ���Ǽ�Ⱥģʽ
		boolean bClusterMode = false;
		String strNimbusHost = null;
		if (args.length != 0) {
			strNimbusHost = args[0];
			bClusterMode = true;
		}
		// �������˽ṹ
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("FTPSpout", new FTPSpout());
		builder.setBolt("FTPBolt", new FTPBolt(), 5).fieldsGrouping("FTPSpout", new Fields("mdn"));
		builder.setBolt("RedisBolt", new RedisBolt(), 5).shuffleGrouping("FTPBolt", "redis");
		builder.setBolt("HDFSBolt", new HDFSBolt()).shuffleGrouping("FTPSpout");
		
		// ����
		Config conf = new Config();
		conf.setDebug(false);
		// �ύ��������
		if (bClusterMode) {
			conf.put(Config.NIMBUS_HOST, strNimbusHost);
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(m_strTopologyName, conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(m_strTopologyName, conf, builder.createTopology());
		}
	}

}
