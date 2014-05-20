package requester.logic.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Requester File Format
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class Rif {

    private final String url;
    private final String data;
    private Map<String, String> headers = new HashMap<String, String>();
    
    public Rif(String url, String data) {
        this.url = url;
        String[] elements = data.split("\r\n\r\n");
        if (elements.length == 2) {
            this.data = elements[0];
            String[] headers = elements[1].split("\r\n");
            for (String kv : headers) {
            	String[] keyvalues = kv.split("=");
            	this.headers.put(keyvalues[0], keyvalues[1]);
            }
        } else {
            this.data = data;        	
        }
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
}
