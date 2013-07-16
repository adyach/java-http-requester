package requester.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import requester.logic.model.RequestModel;
import requester.logic.model.ResponseModel;

public class PostRequester implements Runnable {

    private static final String METHOD = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_XML = "application/xml;charset=UTF-8";

    private final ResponseModel responseModel = ResponseModel.getInstance();
    private final RequestModel requestModel = RequestModel.getInstance();

    @Override
    public void run() {

        try {
            sendPost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPost() throws IOException {

        final long startTime = System.currentTimeMillis();

        URL url = new URL(requestModel.getUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(METHOD);
        conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_XML);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write(requestModel.getRequest());
        writer.flush();

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        writer.close();
        reader.close();

        final int responseCode = conn.getResponseCode();
        final long responseTime = System.currentTimeMillis() - startTime;

        responseModel.setResponseText(sb.toString());
        responseModel.setResponseCode(responseCode);
        responseModel.setResponseSize(sb.length());
        responseModel.setResponseTime(responseTime);
    }
}
