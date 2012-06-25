<#include "views/HtmlWidget/${widgetType}-config.ftl" />
<#assign userPref = This.getUserPrefByName('NX_FOLDER') />
<div class="control-group" style="display:none;">
    <label class="control-label" for="pageClasseurFolderTitle" >Répertoire de page classeur</label>
    <div class="controls" >
    <input type="text" class="disabled" disabled='' name="pageClasseurFolderTitle" value='' />
    </div>
</div>
<div class="control-group" >
    <label class="control-label" for="pageClasseurFolderShortPath" >Chemin du répertoire</label>
    <div class="controls" >
        <input type="hidden" disabled='' name="pageClasseurFolderPath" value='' />
		<input type="hidden" disabled='' name="pageClasseurTitle" value='' />
        <input type="text" class="disabled span4" disabled='' name="pageClasseurFolderShortPath" value='' />
    </div>
</div>
<div class="control-group">
    <div class="controls" >
        <btn id="selectFolderBtn" class="btn btn-primary" >Sélectionner Répertoire</btn>
        <#-- POUR TESTER l'iframe
        <a id="selectFolderIframeBtn" class="btn btn-primary iframe" href="${Context.baseURL}${contextPath}/site/labssites/${Common.siteDoc(Document).site.URL}/@views/selectPageClasseurFolderView?modeSelect=true" >Sélectionner Répertoire (iframe)</a>
        -->
        <input type="hidden" name="${userPref.name}" value='${userPref.actualValue}' />
    </div>
</div>
<div class="control-group" style="display:none;" >
    <label class="control-label" for="pageClasseurFolderId" >ID du répertoire</label>
    <div class="controls" >
        <input type="text" class="disabled" disabled='' name="pageClasseurFolderId" value='' />
    </div>
</div>
<div id="foldersSelection" >
</div>
<script type="text/javascript">
function buildGadgetIdProp(folderPath, folderId, folderTitle, classeurTitle) {
	return '{"NXPATH":"' + escape(folderPath) + '","NXID":"' + folderId + '","NXTITLE":"' + escape(folderTitle) + '","NXCLASSEURTITLE":"' + escape(classeurTitle) + '"}';
}

function setSelectFn() {
    window.gadgets = window.gadgets || {};
    window.gadgets.lmselectvalue_containerProxy = function(gadgetId, value) {
        var json = eval('(' + value + ')');
        var inputPageClasseurFolderPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderPath]');
        var inputPageClasseurFolderShortPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderShortPath]');
        var inputPageClasseurFolderId = jQuery('#divConfigGadget form input[name=pageClasseurFolderId]');
        var inputPageClasseurFolderTitle = jQuery('#divConfigGadget form input[name=pageClasseurFolderTitle]');
        var inputPageClasseurTitle = jQuery('#divConfigGadget form input[name=pageClasseurTitle]');
        jQuery(inputPageClasseurFolderPath).val(json.pageClasseurFolderPath);
        jQuery(inputPageClasseurFolderShortPath).val(getShortFolderPath(json.pageClasseurFolderPath));
        jQuery(inputPageClasseurFolderId).val(json.pageClasseurFolderId);
        jQuery(inputPageClasseurFolderTitle).val(json.pageClasseurFolderTitle);
        jQuery(inputPageClasseurTitle).val(json.pageClasseurTitle);
        if (jQuery(inputPageClasseurFolderPath).val() !== "") {
            jQuery('#divConfigGadget form input[name=NX_FOLDER]').val(buildGadgetIdProp(json.pageClasseurFolderPath, jQuery(inputPageClasseurFolderId).val(), jQuery(inputPageClasseurFolderTitle).val(), jQuery(inputPageClasseurTitle).val()));
        }
        jQuery.fancybox.close();
    }
}
function getShortFolderPath(nxfolderPath) {
	return nxfolderPath.replace("/default-domain/sites/${Common.siteDoc(Document).site.document.name}/tree", "");
}
function getShortFolderPathFromUserPrefValue(userPrefValue) {
	var json = eval('('+ userPrefValue +')');
	var nxfolderPath = json.NXPATH;
	return getShortFolderPath(nxfolderPath);
}
function updatePathInputs() {
	var nxfolderValue = jQuery('#divConfigGadget form input[name=NX_FOLDER]').val();
	if (nxfolderValue !== "") {
		var json = eval('('+ nxfolderValue +')');
		var nxfolderPath = json.NXPATH;
		jQuery('#divConfigGadget form input[name=pageClasseurFolderPath]').val(nxfolderPath);
		jQuery('#divConfigGadget form input[name=pageClasseurFolderShortPath]').val(getShortFolderPathFromUserPrefValue(nxfolderValue));
	}
}

jQuery(document).ready(function() {
	updatePathInputs();
	jQuery('#selectFolderBtn').click(function() {
		jQuery(this).hide();
		jQuery('#foldersSelection').load('${Context.modulePath}/${Common.siteDoc(Document).getSite().URL}/@views/selectPageClasseur');
		jQuery('#foldersSelection').show();
		jQuery(this).closest('div.modal-body').animate({scrollTop: $("#foldersSelection").offset().top},'slow');
	});
    var userPrefValue = '${userPref.actualValue}';
    if (userPrefValue !== "") {
        var json = eval('('+ userPrefValue +')');
        var inputPageClasseurFolderPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderPath]');
        var inputPageClasseurFolderId = jQuery('#divConfigGadget form input[name=pageClasseurFolderId]');
        var inputPageClasseurFolderTitle = jQuery('#divConfigGadget form input[name=pageClasseurFolderTitle]');
        var inputPageClasseurTitle = jQuery('#divConfigGadget form input[name=pageClasseurTitle]');
        jQuery(inputPageClasseurFolderPath).val(json.NXPATH);
        jQuery(inputPageClasseurFolderId).val(json.NXID);
        jQuery(inputPageClasseurFolderTitle).val(json.NXTITLE);
        jQuery(inputPageClasseurTitle).val(json.NXCLASSEURTITLE);
    }
    jQuery('#selectFolderIframeBtn').fancybox({
        'onComplete' : setSelectFn,
        'width' : 980,
        'height' : 800,
        'autoDimensions' : false,
        'padding' : 10
    });
});
</script>
