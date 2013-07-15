package requester;

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

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class FileHelper {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private FileHelper() {

    }

    public static String readFileXml(File file) {

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

    public static RIF readFileRif(File file) {

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

            return new RIF(url, stringFile.toString());
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

        return new RIF("Error", "Error");
    }

    public static void saveFileRIF(File file, String data, String url) {

        if (!file.getName().endsWith(".rif")) {
            file = new File(file.getAbsolutePath() + ".rif");
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
            bw.write(url);
            bw.newLine();
            bw.write(data);
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

    public static class RIF {

        private final String url;
        private final String data;

        public RIF(String url, String data) {

            this.url = url;
            this.data = data;
        }

        public String getUrl() {

            return url;
        }

        public String getData() {

            return data;
        }
    }
}
