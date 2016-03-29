import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;


public class HDFSBolt extends BaseRichBolt {

	private static final long serialVersionUID = 6796146514495389713L;

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
