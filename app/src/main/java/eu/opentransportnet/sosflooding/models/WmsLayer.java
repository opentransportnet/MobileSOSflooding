package eu.opentransportnet.sosflooding.models;

/**
 * @author Kristaps Krumins
 */
public class WmsLayer {
    String name = null;
    String wmsUrl = null;

    public WmsLayer(String name, String wmsUrl) {
        this.name = name;
        this.wmsUrl = wmsUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String url) {
        this.wmsUrl = url;
    }

}