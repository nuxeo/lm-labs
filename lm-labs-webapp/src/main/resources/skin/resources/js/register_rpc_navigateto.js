function registerRPCnavigateTo() {
        log('registering RPC service navigateTo');
        gadgets.rpc.register('navigateTo', function(url) {
                document.location.href = url;
        });
        log('navigateTo registered.');
}
jQuery(document).ready(registerRPCnavigateTo);

function log(str) {
        if (typeof console !== "undefined") {
                console.log(str);
        }
}
