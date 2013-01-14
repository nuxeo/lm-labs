function likePage(docid, serverUrl, like, successCallback, errorCallback) {
    var operation = '';
    if (like) {
        operation = 'Services.Like';
    } else {
        operation = 'Services.CancelLike';
    }
    var d = {"params": {'document': docid}};
    jQuery.ajax({
        type: 'POST',
        url: serverUrl + '/automation/' + operation,
        async: false,
        contentType: 'application/json+nxrequest',
        data: JSON.stringify(d)
        ,error: function(jqXHR, textStatus, errorThrown) {
            if (typeof errorCallback == "function") {
                errorCallback(jqXHR, textStatus, errorThrown);
            }
        }
        /*
        ,complete: function(jqXHR, textStatus) {alert('complete');}
        ,success: function(data, textStatus, jqXHR) {alert('success');}
         */
    });
}

// not used
function getLikePageCount(docid, serverUrl, successCallback) {
    var operation = 'Services.GetLikeStatus';
    var d = {"params": {'document': docid}};
    jQuery.ajax({
        type: 'POST',
        url: serverUrl + '/automation/' + operation,
        async: false,
        contentType: 'application/json+nxrequest',
        data: JSON.stringify(d)
        ,success: function(data, textStatus, jqXHR) {
            if (typeof successCallback == "function") {
                successCallback(data.likesCount);
            }
        }
        /*
        ,complete: function(jqXHR, textStatus) {},
        ,error: function(jqXHR, textStatus, errorThrown) {alert('error');}
         */
    });
    return 0;
}

function likePageErrorCallback() {
    jQuery('#divLike').find('.count').hide();
    jQuery('#divLike').find('.error').show();
}

function updateDivLikeWaiting(obj) {
    var divCount = jQuery(obj).parent('div').siblings('div.count');
    if (divCount != null) {
        jQuery(divCount).find('span.badge').hide();
        jQuery(divCount).find('i.icon-spinner').show();
    }
}

jQuery(document).ready(function() {
    jQuery('#divLike').on('click', 'i.like-action', function() {
        updateDivLikeWaiting(jQuery('#divLike'));
        likePage(jQuery(this).data('docid'), jQuery(this).data('serverurl'), true, null, likePageErrorCallback);
        reloadDivLike(jQuery('#divLike'));
    });
    jQuery('#divLike').on('click', 'i.dislike-action', function() {
        updateDivLikeWaiting(jQuery('#divLike'));
        likePage(jQuery(this).data('docid'), jQuery(this).data('serverurl'), false, null, likePageErrorCallback);
        reloadDivLike(jQuery('#divLike'));
    });
});
