	

	function createWebSocket() {
		var ws;
		
		ws = new WebSocket("ws://localhost:8090/admin_page_data");
		
		ws.onopen = function (event) {

		}
		
		ws.onmessage = function (event) {
			var obj = JSON.parse(event.data);
			
			var tmp;
			tmp = document.getElementById("cache-status-header");
			tmp.innerHTML = tmp.innerHTML.replace("..loading", "");
			
			tmp = document.getElementById("cachedObjectsCount");
			tmp.innerHTML = obj.cachedObjectsCount;
			
			tmp = document.getElementById("numberOfHits");
			tmp.innerHTML = obj.numberOfHits;
			
			tmp = document.getElementById("numberOfMisses");
			tmp.innerHTML = obj.numberOfMisses;

			//-----------------------------------------------------------------------------
			
			tmp = document.getElementById("cache-parameters-header");
			tmp.innerHTML = tmp.innerHTML.replace("..loading", "");
			
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

	var socket = createWebSocket(); 