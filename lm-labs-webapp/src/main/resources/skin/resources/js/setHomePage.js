function setAsHomePage(pagePath) {
    if (pagePath.length > 0) {
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: pagePath + '/@setHome',
            success: function(data) {
                window.location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.statusText);
                jQuery('#waitingPopup').dialog2('close');
            }
        });
        return false;
    }
}
