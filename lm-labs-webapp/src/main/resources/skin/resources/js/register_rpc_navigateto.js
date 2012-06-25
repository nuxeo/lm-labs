function registerRPCnavigateTo() {
	if(console) {
		console.log('registering RPC service navigateTo');
	}
	gadgets.rpc.register('navigateTo', function(url) {
		document.location.href = url;
	});
	if(console) {
		console.log('navigateTo registered.');
	}
}
jQuery(document).ready(registerRPCnavigateTo);