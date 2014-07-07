var SAAgent = null;
var SASocket = null;
var CHANNELID = 104;
var ProviderAppName = "GearNotification";

connect();

function createHTML(log_string)
{
	var log = document.getElementById('NotifyText');
	log.innerHTML = log_string;
}

function onerror(err) {
	console.log("err [" + err.name + "] msg[" + err.message + "]");
}

var agentCallback = {
	onrequest: function(peerAgent) 
		   {
		         SAAgent.acceptServiceConnectionRequest(peerAgent);
		   },
	onconnect : function(socket) {
		SASocket = socket;
		SASocket.setSocketStatusListener(function(reason){
			console.log("Service connection lost, Reason : [" + reason + "]");
			disconnect();
		});
		SASocket.setDataReceiveListener(onreceive);
	},
	onerror : onerror
};


function onsuccess(agents) {
	try {
		if (agents.length > 0) {
			SAAgent = agents[0];
			SAAgent.setServiceConnectionListener(agentCallback);
		} else {
			alert("Not found SAAgent!!");
		}
	} catch(err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}

function connect() {
	if (SASocket) {
        return false;
    }
	try {
		webapis.sa.requestSAAgent(onsuccess, onerror);
	} catch(err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}


function onreceive(channelId, data) {
	createHTML(data);
}


( function () {
	window.addEventListener( 'tizenhwkey', function( ev ) {
		if( ev.keyName == "back" ) {
			var page = document.getElementsByClassName( 'ui-page-active' )[0],
				pageid = page ? page.id : "";
			if( pageid === "main" ) {
				tizen.application.getCurrentApplication().exit();
			} else {
				window.history.back();
			}
		}
	} );
} () );
