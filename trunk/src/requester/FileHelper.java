package requester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class FileHelper {

    private FileHelper() {

    }

    public static String readFileXml(File file) {

        final StringBuffer stringFile = new StringBuffer();
        FileReader fr = null;

        try {
            fr = new FileReader(file);
            char[] buff = new char[(int) file.length()];
            while (fr.read(buff, 0, (int) file.length()) != -1) {
                stringFile.append(buff);
            }
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

        return stringFile.toString();
    }

    public static RIF readFileRif(File file) {

        final StringBuffer stringFile = new StringBuffer();
        BufferedReader fr = null;

        try {
            fr = new BufferedReader(new FileReader(file));
            final String url = fr.readLine().trim();
            String line = "";
            while ((line = fr.readLine()) != null) {
                stringFile.append(line.trim());
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

        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(url);
            fr.write("\n\r");
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
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
