package requester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class PostRequester {

	private StringBuilder response;

	public void sendPost(String requestingUrl, String request)
			throws IOException {
		URL url = new URL(requestingUrl);
		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream());

		writer.write(request.replaceAll(" ", "").replaceAll("\n", ""));
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		response = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		writer.close();
		reader.close();
	}

	public String getResponse() {
		return response.toString();
	}
}
// String urlParameters = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
// + "<request>" + "<header>" + "<reqid>1</reqid>"
// + "<timestamp>2069-01-01 00:00:00.228</timestamp>"
// + "<client>CB 1.0</client>" + "<os>iOS 10.1</os>"
// + "<system>CB</system>" + "<ip>99.99.99.99</ip>"
// + "<model>iPhone10,1</model>"
// + "<method>GetCompanyList</method>" + "</header>" + "<body>"
// + "<sectionid>3</sectionid>" + "</body>" + "</request>";
//
// String addComplaint = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
// + "<request>" + "<header>" + "<reqid>1</reqid>"
// + "<timestamp>2069-01-01 00:00:00.228</timestamp>"
// + "<client>CB 1.0</client>" + "<os>iOS 10.1</os>"
// + "<system>CB</system>" + "<ip>99.99.99.99</ip>"
// + "<model>iPhone10,1</model>"
// + "<method>SendComplaintInfo</method>" + "</header>" + "<body>"
// + "<complaintid>1</complaintid>"
// + "<complaintname>РАБОТАЕТ</complaintname>"
// + "<description>ПРОВЕРЯЕМ РАБОТОСПОСОБНОСТЬ</description>"
// + "<authorid>41111111</authorid>" + "<companyid>1</companyid>"
// + "<facebook>11111</facebook>"
// + "<image>gdfHFgHfHhRFdhFJfJf==</image>"
// + "<lantitude>37.567</lantitude>"
// + "<longitude>49.321</longitude>" + "</body>" + "</request>";
// String getComplaint = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
// + "<request> " + "<header> " + "<reqid>1</reqid> "
// + "<timestamp>2069-01-01 00:00:00.228</timestamp> "
// + "<client>CB 1.0</client> " + "<os>iOS 10.1</os> "
// + "<system>CB</system> " + "<ip>99.99.99.99</ip> "
// + "<model>iPhone10,1</model> "
// + "<method>GetComplaintInfo</method> " + "</header> "
// + "<body> " + "<complaintid>94</complaintid> " + "</body> "
// + "</request>";
// String request =
// "http://54.214.235.42:8080/ComplaintLogic/ComplaintFacade";