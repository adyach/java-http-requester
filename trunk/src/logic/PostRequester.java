package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class PostRequester implements Callable<Response> {

    private Response response;
    private final String requestingUrl;
    private final String request;

    public PostRequester(String requestingUrl, String request) {

        this.requestingUrl = requestingUrl.trim();
        this.request = request.trim();
    }

    @Override
    public Response call() throws Exception {

        try {
            sendPost();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendPost() throws IOException {

        final long startTime = System.currentTimeMillis();

        URL url = new URL(requestingUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write(request);
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

        response = new Response(sb.toString(), responseCode, sb.length(), responseTime);
    }
}
