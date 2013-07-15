package logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class XmlFormatter {

    private XmlFormatter() {

    }

    public static String formatXml(String xml) {

        try {
            final Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            final Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")))));
            final StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);

            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray(), Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return xml;
        }
    }
}
