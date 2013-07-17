package requester.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import requester.logic.model.RequestModel;
import requester.logic.model.ResponseModel;

public class PostRequester implements Runnable {

    private static final Logger log = Logger.getLogger(PostRequester.class);

    private static final String METHOD = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_XML = "application/xml;charset=UTF-8";
    private static final String ENCODING = "UTF-8";

    private final ResponseModel responseModel = ResponseModel.getInstance();
    private final RequestModel requestModel = RequestModel.getInstance();

    @Override
    public void run() {

        try {
            sendPost();
        } catch (SSLHandshakeException e) {
            log.error("Error happened while trying to connect via SSL (2): ", e);
            responseModel.setResponseText(e.getMessage());
        } catch (IOException e) {
            log.error("Error happened while requesting server: ", e);
            responseModel.setResponseText(e.getMessage());
        }
    }

    private void sendPost() throws IOException {

        final long startTime = System.currentTimeMillis();

        final URL url = new URL(requestModel.getUrl());
        final URLConnection connection = url.openConnection();

        if (requestModel.getCertificate() != null) {
            if (connection instanceof HttpsURLConnection) {

                log.info("Setting up https connection ...");
                SSLSocketFactory sslSocketFactory = null;
                try {
                    sslSocketFactory = getSSLSocketFactory();
                } catch (Exception e) {
                    final int responseCode = ((HttpsURLConnection) connection).getResponseCode();
                    final long responseTime = System.currentTimeMillis() - startTime;

                    responseModel.setResponseText("Bad Certificate");
                    responseModel.setResponseCode(responseCode);
                    responseModel.setResponseSize(0);
                    responseModel.setResponseTime(responseTime);

                    throw new IOException("Error happened while trying to connect via SSL (1):", e);
                }

                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
            } else {
                log.info("Setting up http connection ...");
            }
        }

        ((HttpURLConnection) connection).setRequestMethod(METHOD);
        ((HttpURLConnection) connection).setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_XML);
        ((HttpURLConnection) connection).setDoInput(true);
        ((HttpURLConnection) connection).setDoOutput(true);

        log.info("Creating output writter ...");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        log.info("Sending request ...");
        writer.write(requestModel.getRequest());
        writer.flush();

        log.info("Reading response ...");
        String line;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(ENCODING)));
        final StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        writer.close();
        reader.close();

        log.info("Refershing model ...");
        final int responseCode = ((HttpURLConnection) connection).getResponseCode();
        final long responseTime = System.currentTimeMillis() - startTime;

        responseModel.setResponseText(sb.toString());
        responseModel.setResponseCode(responseCode);
        responseModel.setResponseSize(sb.length());
        responseModel.setResponseTime(responseTime);
    }

    private SSLSocketFactory getSSLSocketFactory() throws Exception {

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509"); //"SunX509", "SunJSSE"
        keyManagerFactory.init(getKeyStore(), requestModel.getPassword().toCharArray());
        final SSLContext sslContext = SSLContext.getInstance("TLS"); //TLS
        sslContext.init(keyManagerFactory.getKeyManagers(), trustAllCerts, null);

        final SSLSocketFactory socketFactory = sslContext.getSocketFactory();

        return socketFactory;
    }

    private KeyStore getKeyStore() throws Exception {

        final KeyStore keyStore = KeyStore.getInstance("PKCS12");
        final File keyFile = requestModel.getCertificate();
        final FileInputStream fis = new FileInputStream(keyFile);
        keyStore.load(fis, requestModel.getPassword().toCharArray());
        fis.close();

        return keyStore;
    }

    final X509TrustManager easyTrustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {

            return null;
        }
    };

    // Create a trust manager that does not validate certificate chains
    final TrustManager[] trustAllCerts = new TrustManager[] {easyTrustManager};
}
