package requester.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

import requester.logic.model.FileModel;
import requester.logic.model.HttpHeadersModel;
import requester.logic.model.RequestModel;
import requester.logic.model.Rif;

/**
 * 
 * Helps to save and load files for the requester.
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class FileHelper {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String RIF_EXT = "rif";
    private static final String XML_EXT = "xml";

    private static final FileModel fileModel = FileModel.getInstance();
    private static final RequestModel requestModel = RequestModel.getInstance();

    public void saveFileRIF() {

        File file = fileModel.getFile();
        if (!file.getName().endsWith(".rif")) {
            file = new File(file.getAbsolutePath() + ".rif");
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
            bw.write(requestModel.getUrl());
            bw.newLine();
            bw.write(requestModel.getRequest());
            bw.newLine();
            bw.newLine();
            Map<String, String> headers = HttpHeadersModel.getInstance().getHttpHeaders();
            for (String key : headers.keySet()) {
                bw.write(key);
                bw.write("=");
                bw.write(headers.get(key));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void readFile() {

        final File file = fileModel.getFile();

        if (file.getName().endsWith(XML_EXT)) {
            final String fileStr = readFileXml(file);
            requestModel.setRequest(fileStr);
        } else if (file.getName().endsWith(RIF_EXT)) {
            final Rif rif = readFileRif(file);
            requestModel.setRequest(rif.getData());
            requestModel.setUrl(rif.getUrl());
            HttpHeadersModel.getInstance().setHeaders(rif.getHeaders());
        }

    }

    private String readFileXml(File file) {

        final StringBuffer stringFile = new StringBuffer();
        FileInputStream fis = null;

        try {

            fis = new FileInputStream(file);
            byte[] buff = new byte[10];
            while (fis.read(buff) != -1) {
                stringFile.append(new String(buff, Charset.forName("UTF-8")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringFile.toString();
    }

    private Rif readFileRif(File file) {

        final StringBuffer stringFile = new StringBuffer();
        BufferedReader fr = null;

        try {
            fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
            final String url = fr.readLine().trim();
            String line = "";
            while ((line = fr.readLine()) != null) {
                stringFile.append(line);
                stringFile.append(NEW_LINE);
            }

            return new Rif(url, stringFile.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new Rif("Error", "Error");
    }
}
