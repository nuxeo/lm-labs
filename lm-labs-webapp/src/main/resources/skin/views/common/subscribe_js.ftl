<script type="text/javascript">
    function subscribePage(subscribe, targetType) {
        if (targetType == "Site") {
            var confirmMsg = '';
            if (subscribe) {
                confirmMsg = "${Context.getMessage('command.contextmenu.Site.subscribe.confirm')}";
            } else {
                confirmMsg = "${Context.getMessage('command.contextmenu.Site.unsubscribe.confirm')}";
            }
            if (!confirm(confirmMsg)) {
                return;
            }
        }
        var subsUrl = "";
        if (targetType != "Site") {
            subsUrl = "${This.path}";
        } else {
            subsUrl = "${Context.modulePath}/${Common.siteDoc(Document).getSite(Context.coreSession).URL}";
        }
        jQuery.ajax({
            type: 'GET',
            async: false,
            url: subsUrl + "/@" + (subscribe ? 'subscribe' : 'unsubscribe'),
            success: function(data) {
                if (subscribe) {
                    jQuery('.subscribeBt' + targetType).hide();
                    jQuery('.unsubscribeBt' + targetType).show();
                }
                else {
                    jQuery('.subscribeBt' + targetType).show();
                    jQuery('.unsubscribeBt' + targetType).hide();
                }
            },
            error: function() {
                <#-- TODO alert
                console.log('subscribe failed.');
                -->
            }
        });
        return false;
    }
</script>
