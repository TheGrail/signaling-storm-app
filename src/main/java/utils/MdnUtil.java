package utils;

public class MdnUtil {

	public static String reviseMdn(String mdn){
		if(mdn.charAt(0) == '0'){
			return null;
		}else if(mdn.substring(0, 2).equals("86")){
			mdn = mdn.substring(2);
		}else if(!isNumeric(mdn)){
			return null;
		}
		return mdn;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
