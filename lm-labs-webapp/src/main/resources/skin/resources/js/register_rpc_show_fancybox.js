function registerRPCshow_fancybox() {
        log('registering RPC service show_fancybox');
        gadgets.rpc.register('show_fancybox', function(childs, current) {
        if(jQuery.fancybox) {
            var items = [];

            jQuery.each(childs, function(index, child) {
            items.push({href:[child.path.value,"@view/Original.jpg"].join(""), title:child.description.value});
            });

            jQuery.fancybox(items, {
                    'titleShow' : true,
                    'titlePosition' : 'inside',
                    'zoomSpeedIn': 500,
                    'zoomSpeedOut': 500,
                    'overlayShow': false,
                    'forceImage': true,
                    'hideOnContentClick': false
            }, current);
        } else {
                logError("Add FancyBox plugin");
        }
        });
        log('registered.');
}
jQuery(document).ready(registerRPCshow_fancybox);

function log(str) {
        if (typeof console !== "undefined") {
                console.log(str);
        }
}

function logError(str) {
        if (typeof console !== "undefined") {
                console.error(str);
        }
}
