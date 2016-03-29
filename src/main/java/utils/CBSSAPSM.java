package utils;

// A接口短信记录
public class CBSSAPSM {

	public static final int START_TIME = 0;			// CM业务请求或寻呼响应时间
	public static final int CDR_ID = 1;				// CDR标识号
	public static final int OPC = 2;				// OPC
	public static final int DPC = 3;				// DPC
	public static final int S_IP = 4;				// 源IP
	public static final int D_IP = 5;				// 目的IP
	public static final int A_TYPE = 6;				// 子业务类型
	public static final int SMS_TYPE = 7;			// MO或MT标识
	public static final int SMS_MODE = 8;			// 短信业务模式
	public static final int CALLING = 9;			// 主叫号码
	public static final int CALLING_TYPE = 10;		// 主叫号码类型
	public static final int CALLING_PLAN = 11;		// 主叫号码编号计划
	public static final int CALLED = 12;			// 被叫号码
	public static final int CALLED_TYPE = 13;		// 被叫号码类型
	public static final int CALLED_PLAN = 14;		// 被叫号码编号计划
	public static final int IMSI = 15;				// IMSI
	public static final int ESN = 16;				// ESN
	public static final int MS_CLASS = 17;			// MS级别
	public static final int START_LAC = 18;			// 业务发起的LAC
	public static final int START_CI = 19;			// 业务发起的CI
	public static final int PR_FLAG = 20;			// 寻呼标志
	public static final int SMS_RESULT = 21;		// 业务详细结果
	public static final int SMS_RESEND = 22;		// 短信重发次数
	public static final int ASSG_CAUSE = 23;		// 指配失败或短信传送失败原因
	public static final int CLEAR_CAUSE = 24;		// 清除原因
	public static final int SMS_LENGTH = 25;		// 短信长度
	public static final int USPRP_TIME = 26;		// 用户寻呼响应时延
	public static final int NETPR_TIME = 27;		// 网络寻呼响应时延
	public static final int RESP_TIME = 28;			// 网络响应时延
	public static final int ASSGREQ_TIME = 29;		// 指配请求时延
	public static final int ASSGCPL_TIME = 30;		// 指配结束时延
	public static final int CLEAR_TIME = 31;		// 清除时延
	public static final int RLC_TIME = 32;			// 释放时长
	public static final int SMS_DELAY = 33;			// 短信时延
	public static final int BEAR_PROTO = 34;		// 承载类型
	public static final int FRONTNO = 35;			// 前端机
	public static final int REMOTENO = 36;			// 远端站
	public static final int OFFSET = 37;			// 偏移量
	public static final int MSC_ID = 38;			// MSC
	public static final int MGW_ID = 39;			// MGW
	public static final int BSC_ID = 40;			// BSC
	public static final int O_OPER_ID = 41;			// 起源运营商
	public static final int D_OPER_ID = 42;			// 目的运营商
	public static final int O_PROV_ID = 43;			// 源省
	public static final int O_CITY_ID = 44;			// 源城市
	public static final int D_PROV_ID = 45;			// 目的省
	public static final int D_CITY_ID = 46;			// 目的城市
	public static final int JOINER_ID = 47;			// 关联ID
}
