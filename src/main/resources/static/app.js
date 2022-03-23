var app = (function () {

    class Point{
        constructor(x,y){
            this.x=x;
            this.y=y;
        }        
    }

    var identificador = null;
    var stompClient = null;

    var addPointToCanvas = function (point) {        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 3, 0, 2 * Math.PI);
        ctx.stroke();
    };

    var publicPoint = function (point) {
        stompClient.send(`/app/newpoint.${identificador}`
            , {}, JSON.stringify(point));
    };

    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();
        stompClient.send("/topic/newpoint"+$('#identif').val(),{},JSON.stringify({x:evt.clientX-rect.left,y:evt.clientY-rect.top}));
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    };

    var addPolygonToCanvas = function (lista) {
        var c2 = canvas.getContext("2d");
        c2.fillStyle = "#b13de2";
        c2.beginPath();
        c2.moveTo(lista[0].x, lista[0].y);
        for (var i = 1; i < lista.length; i++) {
            c2.lineTo(lista[i].x, lista[i].y);
        }
        c2.closePath();
        c2.fill();
    };
    var connectAndSubscribe = function (numDib) {
        identificador = numDib;
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        
        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {
            //console.log('Connected: ' + frame);
            stompClient.subscribe(`/topic/newpoint.${identificador}`, function (message) {
                var theObject = JSON.parse(message.body);
                alert(theObject.x + "--" + theObject.y);
                var puntito = new Point(theObject.x, theObject.y);
                addPointToCanvas(puntito);

            });
            stompClient.subscribe(
                `/topic/newpolygon.${identificador}`,
                function (message) {
                    var listPolygons = JSON.parse(message.body);
                    console.log(listPolygons);
                    addPolygonToCanvas(listPolygons);
                }
            );
        });

    };
    
    

    return {

        init: function (numDib) {
            var can = document.getElementById("canvas");
            
            //websocket connection
            connectAndSubscribe(numDib);
        },

        publishPoint: function(px,py){
            var pt=new Point(px,py);
            console.info("publishing point at "+pt);
            addPointToCanvas(pt);

            //publicar el evento
            publicPoint(pt);
        },

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }
    };

})();