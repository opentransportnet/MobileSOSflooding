var lat = Activity.getLatitude();
var lng = Activity.getLongitude();
//var iconNames = JSON.parse(Activity.getIconNames());
var map = L.map('map', {zoomControl: false}).setView([lat, lng], 11);
var attribution = map.attributionControl;
attribution.setPrefix('');
var maxZoom = 15;
var timeFromLastTouch = 0;
var locationZoom = 11;
var markerLat = 0;
var markerLng = 0;
var centerMapView = true;
var percentage = 0;
var zooming = false;
var routePolyline;
var isRecording = false;
var drawingTrack = false;
var routeStrokeColor = "#3fb4fb";
var light_green = "00cc00";
var cyan = "66ffff";
var markerIcon = L.divIcon({
    className: 'icon-location icon-size-standart curr-loc-marker',
    iconSize: new L.Point(56, 95),
    html: '<div style="width:25px;height:25px;background-color:#ffcc00;border-radius: 50%;margin-left: 15px;margin-top: -49px;"></div'
});
var marker = L.marker([lat, lng], {icon: markerIcon});
// Stack with all created change points
var changePoints = new Array();
var makeNewRoute = false;

var tileLayer = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    maxZoom: maxZoom
}).addTo(map);

L.tileLayer.wms("http://geoportal.kraj-lbc.cz/cgi-bin/wms4otn?", {
    layers: 'stinovany_relief',
    format: 'image/png',
    transparent: true
}).addTo(map);


function detectTouch() {
    // If user holding set time how long don't center map
    if (Activity.isHoldingMap()) {
        timeFromLastTouch = 20000;
    }
    // Update time from last touch
    else if (timeFromLastTouch > 0) {
        timeFromLastTouch -= 200;
    } else {
        if (centerMapView) {
            // Pan map view
            map.setView([lat, lng], locationZoom, {
                pan: {animate: true, duration: 1}
            });
        }
    }
}
setInterval(detectTouch, 200);

function continueShowLoc() {
    var latitude = Activity.getLatitude();
    var longitude = Activity.getLongitude();

    marker.setLatLng([latitude, longitude]).update();
    lat = latitude;
    lng = longitude;

    map.setView([latitude, longitude], locationZoom, {pan: {animate: false}});
    mapViewLat = latitude;
    mapViewLng = longitude;

    timeFromLastTouch = 0;
    showMyLocation();
}

function showMyLocation() {
    centerMapView = true;
    markerLat = lat;
    markerLng = lng;
    lat = Activity.getLatitude();
    lng = Activity.getLongitude();

    if (markerLat !== lat || markerLng !== lng) {
        centerMapView = false;
        percentage = 0.1;

        function animateMarker() {
            if (percentage > 1) {
                showMyLocation();
            } else {
                if (!zooming) {
                    var pLat = markerLat + percentage * (lat - markerLat);
                    var pLng = markerLng + percentage * (lng - markerLng);
                    // Update marker location
                    marker.setLatLng([pLat, pLng]).update();

                    if (timeFromLastTouch <= 0) {
                        // Pan map view
                        map.setView([pLat, pLng], locationZoom, {
                            pan: {animate: false}
                        });
                    }
                }
                percentage = percentage + 0.1;
                setTimeout(animateMarker, 50);
            }
        }
        animateMarker();
    }
    else {
        setTimeout(showMyLocation, 1000);
    }
}

setTimeout(function () {
    marker.addTo(map);
}, 400);

map.on('zoomstart', function (e) {
    zooming = true;
});

map.on('zoomend', function (e) {
    zooming = false;
});

function loadScript(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;
    script.onreadystatechange = callback;
    script.onload = callback;
    head.appendChild(script);
}

function loadStyleSheet(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var styleSheet = document.createElement('link');
    styleSheet.rel = 'stylesheet';
    styleSheet.href = url;
    styleSheet.onreadystatechange = callback;
    styleSheet.onload = callback;
    head.appendChild(styleSheet);
}


