package requester.logic.model;

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

    public Rif(String url, String data) {

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
