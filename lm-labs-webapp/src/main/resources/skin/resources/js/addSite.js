function onSelectRadio(obj, url) {
    jQuery('#templatePreview').html('<img src="' + url + '" onerror="function(){};" style="width:400px;display:block;margin:auto;"/>');
}
function cleanPreview() {
    jQuery('#templatePreview').html('');
}
jQuery(function() {
    jQuery("span[rel=popover]").popover({
        offset : 10,
        html : true
    });
})
function clickParentInput(obj) {
    jQuery(obj).siblings('input').click();
}

jQuery(document).ready(function() {
    jQuery('#siteTemplate').click(function() {
        if (jQuery(this).is(':checked')) {
            jQuery('#siteTemplateRadioDiv').hide();
            jQuery('#siteTemplatePreviewDiv').show();
        } else {
            jQuery('#siteTemplateRadioDiv').show();
            jQuery('#siteTemplatePreviewDiv').hide();
        }
    });
    jQuery('span[class*=inputInner]').click(function() {
        alert('span clicked.');
        jQuery(this).closest('input[type=radio]').click();
    });
});
