package trilogy.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

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
		String Filename = "D:\\Eclipse\\workspace\\trilogy\\src\\data\\"
				+ header + ".xml";
		String res = readFile(Filename);
		System.out.println(res);
		inMessage.setBody(res);
	}

	public String readFile(String inputPath) throws Exception {
		StringBuilder stb = new StringBuilder("");
		try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
			String Mon = "";
			String sCurrentLine;
			String orderNum = "";
			while ((sCurrentLine = br.readLine()) != null) {
				stb.append(sCurrentLine).append("\n");
				if (sCurrentLine.indexOf("<mon>") > -1) {
					Mon = sCurrentLine;
				}
				if (sCurrentLine.indexOf("<btn>") > -1) {
					orderNum = insertVoiceDB(sCurrentLine, Mon);
					stb.append("<voiceordernum>").append(orderNum)
							.append("</voiceordernum>").append("\n");
				}
				if (sCurrentLine.indexOf("<circuitid>") > -1) {
					orderNum = insertDataDB(sCurrentLine, Mon);
					stb.append("<dataordernum>").append(orderNum)
							.append("</dataordernum>").append("\n");
				}
				if (sCurrentLine.indexOf("<videoid>") > -1) {
					orderNum = insertVideoDB(sCurrentLine, Mon);
					stb.append("<videoordernum>").append(orderNum)
							.append("</videoordernum>").append("\n");
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
				+ Mon + "','" + tag + "' , '" + ret + "', 'Y')");
		return ret;
	}

	public String insertDataDB(String tag, String Mon) {
		String ret = new Random().nextInt(99999) + ""
				+ new Random().nextInt(99999);
		DBUtil.insertIntoDB("insert into  data_audit_table values ('Dataapp','"
				+ Mon + "','" + tag + "' , '" + ret + "', 'Y')");
		return ret;
	}

	public String insertVideoDB(String tag, String Mon) {
		String ret = new Random().nextInt(99999) + ""
				+ new Random().nextInt(99999);
		DBUtil.insertIntoDB("insert into  Video_audit_table values ('Videoapp','"
				+ Mon + "','" + tag + "' , '" + ret + "', 'Y')");
		return ret;
	}
}