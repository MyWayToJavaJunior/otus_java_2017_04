	

	function createWebSocket() {
		var ws;
		
		ws = new WebSocket("ws://localhost:8090/admin_page_data");
		
		ws.onopen = function (event) {

		}
		
		ws.onmessage = function (event) {
			clearOutput();
			
			var obj = JSON.parse(event.data);
			
			var tmp;
			tmp = document.getElementById("cache-status-header");
			tmp.innerHTML = tmp.innerHTML.replace("..empty", "");
			
			tmp = document.getElementById("cachedObjectsCount");
			tmp.innerHTML = obj.cachedObjectsCount;
			
			tmp = document.getElementById("numberOfHits");
			tmp.innerHTML = obj.numberOfHits;
			
			tmp = document.getElementById("numberOfMisses");
			tmp.innerHTML = obj.numberOfMisses;

			//-----------------------------------------------------------------------------
			
			tmp = document.getElementById("cache-parameters-header");
			tmp.innerHTML = tmp.innerHTML.replace("..empty", "");
			
			tmp = document.getElementById("maximalLifeTime");
			tmp.value = obj.maximalLifeTime;

			tmp = document.getElementById("maximalIdleTime");
			tmp.value = obj.maximalIdleTime;

			tmp = document.getElementById("maximalSize");
			tmp.value = obj.maximalSize;
			
		}
		
		ws.onclose = function (event) {
			
		}
		
		return ws;
	};
	
	function clearOutput() {
			var tmp;
			tmp = document.getElementById("cache-status-header");
			tmp.innerHTML = tmp.innerHTML = "Cache status..empty";
			
			tmp = document.getElementById("cachedObjectsCount");
			tmp.innerHTML = "0";
			
			tmp = document.getElementById("numberOfHits");
			tmp.innerHTML = "0";
			
			tmp = document.getElementById("numberOfMisses");
			tmp.innerHTML = "0";

			//-----------------------------------------------------------------------------
			
			tmp = document.getElementById("cache-parameters-header");
			tmp.innerHTML = "Cache parameters..empty";
			
			tmp = document.getElementById("maximalLifeTime");
			tmp.value = "0";

			tmp = document.getElementById("maximalIdleTime");
			tmp.value = "0";

			tmp = document.getElementById("maximalSize");
			tmp.value = "0";
	}

    function changeCacheParametersValues(socket) {
		clearOutput();
		
        var query = '{ "command": "changeCacheParametersValues"' +
		            ', "maximalLifeTime": "' + document.getElementById("maximalLifeTime").value + '"' +
                    ', "maximalIdleTime": "' + document.getElementById("maximalIdleTime").value + '"' +
                    ', "maximalSize": "' + document.getElementById("maximalSize").value + '"}';
        socket.send(query);
    }
	
	function onChangeDBServiceAddress(socket) {
		clearOutput();
		
		tmp = document.getElementById("dBServiceAddress");
		var query = '{"command": "subscribe", "newDBServiceAddressID": "' + tmp.value + '"}'
		socket.send(query);
	}

	var socket = createWebSocket(); 