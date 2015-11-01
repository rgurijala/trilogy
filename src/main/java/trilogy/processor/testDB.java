package trilogy.processor;

import trilogy.util.DBUtil;

public class testDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tag="<mon>11</mon>";
		System.out.println("select  * from voice_audit_table where MASTER_ORDER_NUMBER = '" + tag + "'");
		String test = DBUtil.fetchFromDB("select  * from voice_audit_table where MASTER_ORDER_NUMBER = '" + tag + "'");
		System.out.println(test);

	}

}
