package trilogy.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import trilogy.util.DBUtil;

public class FileProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		final Message inMessage = exchange.getIn();
		final String header = inMessage.getHeader("testcaseSequenceNumber",
				String.class);
		String Filename = header + ".xml";
		String res = readFile(Filename, exchange);
		inMessage.setBody(res);
	}

	public String readFile(String inputPath, Exchange exchange)
			throws Exception {
		StringBuilder stb = new StringBuilder("");
		final Message inMessage = exchange.getIn();
		// try (BufferedReader br = new BufferedReader(new
		// FileReader(inputPath))) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(this
				.getClass().getClassLoader().getResourceAsStream(inputPath)))) {
			String mon = "";
			String sCurrentLine;
			String orderNum = "";
			while ((sCurrentLine = br.readLine()) != null) {
				stb.append(sCurrentLine).append("\n");
				if (sCurrentLine.indexOf("<mon>") > -1) {
					mon = getMon(sCurrentLine);
					inMessage.setHeader("mon", mon);
				}
				if (sCurrentLine.indexOf("<btn>") > -1) {
					orderNum = insertVoiceDB(sCurrentLine, mon);
					stb.append("<voiceordernum>").append(orderNum)
							.append("</voiceordernum>").append("\n");
					stb.append("<voiceduedate>").append(giveDate(1))
							.append("</voiceduedate>").append("\n");
				}
				if (sCurrentLine.indexOf("<circuitid>") > -1) {
					orderNum = insertDataDB(sCurrentLine, mon);
					stb.append("<dataordernum>").append(orderNum)
							.append("</dataordernum>").append("\n");
					stb.append("<dataduedate>").append(giveDate(3))
							.append("</dataduedate>").append("\n");
				}
				if (sCurrentLine.indexOf("<videoid>") > -1) {
					orderNum = insertVideoDB(sCurrentLine, mon);
					stb.append("<videoordernum>").append(orderNum)
							.append("</videoordernum>").append("\n");
					stb.append("<videoduedate>").append(giveDate(5))
							.append("</videoduedate>").append("\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return stb.toString();
	}

	public String insertVoiceDB(String tag, String Mon) {
		String ret = new Random().nextInt(99999) + ""
				+ new Random().nextInt(99999);
		DBUtil.insertIntoDB("insert into  voice_audit_table values ('Voiceapp','"
				+ Mon.trim()
				+ "','"
				+ tag.trim()
				+ "' , '"
				+ ret.trim()
				+ "', 'Y')");
		return ret;
	}

	public String insertDataDB(String tag, String Mon) {
		String ret = new Random().nextInt(99999) + ""
				+ new Random().nextInt(99999);
		DBUtil.insertIntoDB("insert into  data_audit_table values ('Dataapp','"
				+ Mon.trim() + "','" + tag.trim() + "' , '" + ret.trim()
				+ "', 'Y')");
		return ret;
	}

	public String insertVideoDB(String tag, String Mon) {
		String ret = new Random().nextInt(99999) + ""
				+ new Random().nextInt(99999);
		DBUtil.insertIntoDB("insert into  Video_audit_table values ('Videoapp','"
				+ Mon.trim()
				+ "','"
				+ tag.trim()
				+ "' , '"
				+ ret.trim()
				+ "', 'Y')");
		return ret;
	}

	public String giveDate(int days) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		dt = new Date((dt.getTime()) + (days * 24 * 60 * 60 * 1000));
		return dateFormat.format(dt.getTime());
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
