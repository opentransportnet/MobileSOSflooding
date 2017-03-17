var compositions = [];

function Composition() {
    this.layers = new Array();
    this.legends = new Array();
    
    this.addLayer = function (layer) {
        this.layers.push(layer);
    };
    
    this.addLegend = function (legend) {
        this.legends.push(legend);
    };
}

function addWmsLayerForComposition(compId, url, layerName) {
    console.log("adding WMS for composition with ID " + compId);
    console.log(url);
    console.log(layerName);

    var layer = new L.NonTiledLayer.WMS(url, {
        layers: layerName,
        format: 'image/png',
        transparent: true,
        maxZoom: maxZoom
    });

    map.addLayer(layer);
    
    var language = "eng";
    var uri2 = url + "&service=WMS&VERSION=1.3.0&SLD_VERSION=1.1.0&request=GetLegendGraphic&format=image/png"
            + "&layer=" + layerName + "&language=" + language;
    var wmsLegendControl = L.wmsLegend(uri2);
    map.addControl(wmsLegendControl);

    if (compositions[compId] === null || typeof compositions[compId] === "undefined") {
        compositions[compId] = new Composition();
    }

    compositions[compId].addLayer(layer);
    compositions[compId].addLegend(wmsLegendControl);
}

function removeWmsLayersFromComposition(compId) {
    console.log("removing WMS layers for composition with ID " + compId);
    var legends = compositions[compId].legends;

    while (legends.length > 0) {
        map.removeControl(legends.pop());
        console.log("legend removed");
    }
    
    var wmsLayers = compositions[compId].layers;

    while (wmsLayers.length > 0) {
        map.removeLayer(wmsLayers.pop());
        console.log("layer removed");
    }
}