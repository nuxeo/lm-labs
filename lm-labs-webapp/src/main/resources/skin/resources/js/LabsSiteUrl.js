function verifyUrlAvailability(serviceUrl, successCallback, errorCallback) {
    var url = jQuery('#labsSiteURL').val();
    var ok = false;
    jQuery.ajax({
        type : 'GET',
        async : false,
        url : serviceUrl + '/' + url,
        success : function(msg) {
            if (typeof successCallback == "function") {
                successCallback(msg);
            }
            ok = true;
        },
        error : function(xhr) {
            if (typeof errorCallback == "function") {
                errorCallback(xhr);
            }
            ok = false;
        }
    });
    return ok;
}

function setCheckUrlButton(state) {
    if (state === 'complete') {
        jQuery('#verifyUrlAvailability').button(state);
        jQuery('#verifyUrlAvailability').removeClass('btn-primary');
        jQuery('#verifyUrlAvailability').addClass('btn-success');
    } else if (state === 'failed') {
        jQuery('#verifyUrlAvailability').button(state);
        jQuery('#verifyUrlAvailability').removeClass('btn-primary');
        jQuery('#verifyUrlAvailability').addClass('btn-warning');
    }
}

jQuery(document).ready(function() {
    jQuery('#labsSiteURL').keypress(function(e) {
        jQuery('#urlAvailability').val('false');
        var btnObj = jQuery('#verifyUrlAvailability');
        jQuery(btnObj).button('reset');
        jQuery(btnObj).removeClass('btn-warning');
        jQuery(btnObj).removeClass('btn-success');
        jQuery(btnObj).addClass('btn-primary');
        if ((e.which >= 32 && e.which <= 44) || e.which === 47
                || (e.which >= 58 && e.which <= 64)) {
            return false;
        }
    });
    jQuery('#labsSiteURL').keyup(function(e) {
        if (jQuery(this).val() === '') {
            jQuery('#verifyUrlAvailability').addClass('disabled');
            jQuery('#verifyUrlAvailability').attr('disabled', true);
        } else {
            jQuery('#verifyUrlAvailability').removeClass('disabled');
            jQuery('#verifyUrlAvailability').removeAttr('disabled');
        }
    });
});
