<#if 0 < Document.file.content.length >
<#include "views/HtmlWidget/${widgetType}-config.ftl" />
</#if>
<#--
<div class="control-group">
    <label class="control-label" for="title" >Titre</label>
    <div class="controls" >
        <input type="text" class="input" id="video" name="video-title-field" value="" />
    </div>
</div>
-->
<div class="control-group">
    <label class="control-label" for="file" >Animation</label>
    <div class="controls" >
<#if 0 < Document.file.content.length >
        <p>${Document.file.filename} (${Document.file.content.length} octets)</p>
        <p><strong>Pour changer le fichier vidéo veuillez re-créer le widget.</strong></p>
<#else>
        <input type="file" name="file" id="file" class="input" size="20" />
        <p class="help-block" style="display: none;" >Le fichier n'est pas de type 'flash' (.swf)</p>
</#if>

    </div>
</div>

<script type="text/javascript">
<#-- TODO before form submit -->
function gadgetVideoFileControl() {
    var filename = jQuery.trim(jQuery("#file").val());
    if(filename == "") {
        return false;
    }
    if(!filename.match(/\.(swf)$/)) {
        return false;
    }
    return true;
};
function flashConfigGadgetAjaxFormBeforeSubmit() {
	if (!gadgetVideoFileControl()) {
		jQuery('#file').parents('div.control-group').addClass('error');
		jQuery('#file').siblings('p.help-block').show();
		jQuery('#waitingPopup').dialog2('close');
		jQuery('#divConfigGadget').find('div.actions a, input, button').removeAttr('disabled');
		return false;
	}
	jQuery('#divConfigGadget').find('div.actions a, input, button').attr('disabled', '');
	jQuery('#file').parents('div.control-group').removeClass('error');
	jQuery('#file').siblings('p.help-block').hide();
	return true;
}
jQuery(document).ready(function() {
	jQuery('#file').click(function() {
		jQuery('#file').parents('div.control-group').removeClass('error');
		jQuery('#file').siblings('p.help-block').hide();
	});
<#if 0 < Document.file.content.length >
<#else>
    jQuery('#divConfigGadget form').attr('action', '/nuxeo/site/gadgetDocumentAPI/${Document.id}');
    jQuery('#divConfigGadget form').attr('enctype', 'multipart/form-data');
    jQuery('#config-gadget-form').ajaxForm({
        data: {ajax: 'true'},
        beforeSubmit:  flashConfigGadgetAjaxFormBeforeSubmit,
        error: defaultConfigGadgetAjaxFormError,
        success: defaultConfigGadgetAjaxFormSucess
    });
</#if>
});
</script>
