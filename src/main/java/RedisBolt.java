import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;


public class RedisBolt extends BaseRichBolt {

	private static final long serialVersionUID = 6609746751476242782L;

	public void execute(Tuple tuple) {
		String operation = tuple.getStringByField("operation");
		String key = tuple.getStringByField("key");
		String val = tuple.getStringByField("val");
		
		System.out.println(operation + "," + key + "," + val);
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
