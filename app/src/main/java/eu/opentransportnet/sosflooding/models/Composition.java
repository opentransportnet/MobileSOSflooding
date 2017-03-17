package eu.opentransportnet.sosflooding.models;

import java.util.ArrayList;

/**
 * @author Kristaps Krumins
 */
public class Composition {
    private int id;
    private String title;
    private ArrayList<WmsLayer> wmsLayers;
    private boolean isSelected;
    private boolean layersLoaded;

    public Composition(int id, String title, boolean isSelected){
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;

        wmsLayers = new ArrayList<>();
        layersLoaded = false;
    }

    public void addWmsLayer(WmsLayer layer){
        wmsLayers.add(layer);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId(){
        return id;
    }

    public void setLayersLoaded(){
        layersLoaded = true;
    }

    public boolean isLayersLoaded(){
        return layersLoaded;
    }

    public ArrayList<WmsLayer> getWmsLayers(){
        return wmsLayers;
    }
}
