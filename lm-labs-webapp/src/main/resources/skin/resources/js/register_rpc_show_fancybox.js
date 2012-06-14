function registerRPCshow_fancybox() {
	if(console) {
		console.log('registering RPC service show_fancybox');
	}
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
        } else if(console) {
        	console.error("Add FancyBox plugin");
        }
	});
	if(console) {
		console.log('registered.');
	}
}
jQuery(document).ready(registerRPCshow_fancybox);