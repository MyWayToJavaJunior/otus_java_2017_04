	

	function createWebSocket() {
		var ws;
		
		ws = new WebSocket("ws://localhost:8090/chat");
		
		ws.onopen = function (event) {

		}
		
		ws.onmessage = function (event) {
			var $textarea = document.getElementById("messages");
			$textarea.value = $textarea.value + event.data + "\n";
		}
		
		ws.onclose = function (event) {

		}
		
		return ws;
	};



	function sendMessage(ws) {
		var messageField = document.getElementById("message");
		var userNameField = document.getElementById("username");
		var message = userNameField.value + ":" + messageField.value;
		ws.send(message);
		messageField.value = '';
	}
	
	var socket = createWebSocket(); 