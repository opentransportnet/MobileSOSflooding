/*
 * L.Control.WMSLegend is used to add a WMS Legend to the map
 */

L.Control.WMSLegend = L.Control.extend({
    options: {
        position: 'topright',
        uri: ''
    },
    onAdd: function () {
        var controlClassName = 'leaflet-control-wms-legend',
                legendClassName = 'wms-legend',
                stop = L.DomEvent.stopPropagation;
        this.container = L.DomUtil.create('div', controlClassName);
        this.img = L.DomUtil.create('img', legendClassName, this.container);
        // Store container in image object
        this.img.container = this.container;
        this.img.isLegendAvailable = true;
        this.img.onload = function () {
            checkLegend(this);
        };
        this.img.onerror = function () {
            legendNotAvailable(this);
        };
        this.img.src = this.options.uri;
        this.img.alt = 'Legend';
        this.img.style.display = 'none';

        this.img2 = L.DomUtil.create('img', legendClassName, this.container);
        this.img2.src = "img/layerLegendaIcon.png";
        this.img2.style.width = "26px";
        this.img2.style.height = "26px";
        this.img2.style.margin = "6px";
        this.img2.style.display = 'block';

        L.DomEvent
                .on(this.img, 'click', this._click, this)
                .on(this.container, 'click', this._click, this)
                .on(this.img, 'mousedown', stop)
                .on(this.img, 'dblclick', stop)
                .on(this.img, 'click', L.DomEvent.preventDefault)
                .on(this.img, 'click', stop);
        this.height = null;
        this.width = null;

        return this.container;
    },
    _click: function (e) {
        L.DomEvent.stopPropagation(e);
        L.DomEvent.preventDefault(e);
        // toggle legend visibility
        var style = window.getComputedStyle(this.img);
        
        if(this.img.isLegendAvailable === false){
            // Leave error message as it is
            return;
        }
        
        if(this.img.height < 10){
            // Leave error message as it is
            return;
        }

        if (style.display === 'none') {
            this.container.style.overflowY = "scroll";
            this.container.style.width = this.width + 'px';
            this.container.style.height = '200px';
            this.img.style.display = 'block';
            this.img2.style.display = 'none';
        } else {
            if (this.width === null && this.height === null) {
                // Only do inside the above check to prevent the container
                // growing on successive uses
                this.height = this.container.offsetHeight;
                this.width = this.container.offsetWidth;
            }
            this.displayStyle = this.img.style.display;
            this.img.style.display = 'none';
            this.img2.style.display = 'block';
            this.container.style.height = '40px';
            this.container.style.width = '40px';
        }
    },
});

L.wmsLegend = function (uri) {
    var wmsLegendControl = new L.Control.WMSLegend;
    wmsLegendControl.options.uri = uri;
    map.addControl(wmsLegendControl);
    return wmsLegendControl;
};

function legendNotAvailable(img) {
    var errorMessage = MapActivity.getLegendNotAvailableMessage();
    img.container.innerHTML = errorMessage;
    img.isLegendAvailable = false;
}

function checkLegend(img) {
    // There is invalid legend with height 5px
    if(img.height < 6){
        legendNotAvailable(img);
    }
}