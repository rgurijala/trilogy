package trilogy.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import trilogy.util.DBUtil;

public class DBProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		final Message inMessage = exchange.getIn();
		String xmlBody = inMessage.getBody(String.class);
		String dbResult = "";
		if (xmlBody.indexOf("<DBResult>") < 0) {
			String mon = getMon(xmlBody);
			if (xmlBody.indexOf("voiceordernum") > -1) {
				dbResult += verifyVoice(mon);
			}
			if (xmlBody.indexOf("dataordernum") > -1) {
				dbResult += verifyData(mon);
			}
			if (xmlBody.indexOf("videoordernum") > -1) {
				dbResult += verifyVideo(mon);
			}
			xmlBody = xmlBody.replace("</order>", "") + "<DBResult>" + dbResult
					+ "</DBResult></order>";
			inMessage.setBody(xmlBody);
		}
	}

	public String verifyVoice(String tag) {
		if (DBUtil.fetchFromDB(
				"select * from voice_audit_table where MASTER_ORDER_NUMBER ='"
						+ tag + "'").length() > 1)
			return "<Voiceorder>Success</Voiceorder>";
		else
			return "<Voiceorder>Failure</Voiceorder>";
	}

	public String verifyData(String tag) {
		if (DBUtil.fetchFromDB(
				"select * from data_audit_table where MASTER_ORDER_NUMBER ='"
						+ tag + "'").length() > 1)
			return "<Dataorder>Success</Dataorder>";
		else
			return "<Dataorder>Failure</Dataorder>";
	}

	public String verifyVideo(String tag) {
		if (DBUtil.fetchFromDB(
				"select * from video_audit_table where MASTER_ORDER_NUMBER ='"
						+ tag + "'").length() > 1)
			return "<Videoorder>Success</Videoorder>";
		else
			return "<Videoorder>Failure</Videoorder>";
	}

	public String getMon(String str) {
		String patternString = "<mon>(.+?)</mon>";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			return matcher.group(1);
		else
			return "";
	}
}
