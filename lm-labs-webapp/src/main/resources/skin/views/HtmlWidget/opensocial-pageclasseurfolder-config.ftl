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
        <input type="text" class="disabled span3" disabled='' name="pageClasseurFolderShortPath" value='' />
        <btn id="selectFolderBtn" class="btn btn-primary" ><i class="icon-folder-open" ></i></btn>
        <#-- POUR TESTER l'iframe
        <a id="selectFolderIframeBtn" class="btn btn-primary iframe" href="${Context.baseURL}${contextPath}/site/labssites/${Common.siteDoc(Document).site.URL}/@views/selectPageClasseurFolderView?modeSelect=true" ><i class="icon-folder-open" ></i>(iframe)</a>
        -->
    </div>
</div>
<div class="control-group" >
    <div class="controls" >
	    <label class="checkbox">
		<input type="checkbox" name="relativePath" value=""/>Chemin relatif</label>
    </div>
</div>
<div class="control-group">
    <div class="controls" >
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
if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (str){
    return this.slice(0, str.length) == str;
  };
}
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
        jQuery('#divConfigGadget form input[name=relativePath]').attr('checked', false);
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
		if (!nxfolderPath.startsWith('/')) {
			jQuery('#divConfigGadget form input[name=relativePath]').attr('checked', 'checked');
		} else {
			jQuery('#divConfigGadget form input[name=relativePath]').removeAttr('checked');
		}
	}
}

jQuery(document).ready(function() {
	updatePathInputs();
    jQuery('input[name=relativePath]').on('change', function() {
    	var startPath = '${Common.getPathAsString(Session.getParentDocument(Document.ref))}';
        var endPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderPath]').val();
	    var operation = 'LabsSite.MakeAbsolutePath';
    	if (jQuery(this).is(':checked')) {
    		operation = 'LabsSite.MakeRelativePath';
    	}
	    var d = {"params":{"startPath":startPath, "endPath":endPath},"context":{}};
		jQuery.ajax({
			type: 'POST',
			url: '${Context.serverURL}/nuxeo/site/automation/' + operation,
	        async: false,
	        contentType: 'application/json+nxrequest',
	        data: JSON.stringify(d),
	        success: function(data, textStatus, jqXHR) {
		        var inputPageClasseurFolderPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderPath]');
		        var inputPageClasseurFolderShortPath = jQuery('#divConfigGadget form input[name=pageClasseurFolderShortPath]');
		        var inputPageClasseurFolderId = jQuery('#divConfigGadget form input[name=pageClasseurFolderId]');
		        var inputPageClasseurFolderTitle = jQuery('#divConfigGadget form input[name=pageClasseurFolderTitle]');
		        var inputPageClasseurTitle = jQuery('#divConfigGadget form input[name=pageClasseurTitle]');
				jQuery(inputPageClasseurFolderPath).val(data['value']);
				if (operation === 'LabsSite.MakeAbsolutePath') {
					jQuery(inputPageClasseurFolderShortPath).val(getShortFolderPath(data['value']));
				} else {
					jQuery(inputPageClasseurFolderShortPath).val(data['value']);
				}
		        if (jQuery(inputPageClasseurFolderPath).val() !== "") {
		            jQuery('#divConfigGadget form input[name=NX_FOLDER]').val(buildGadgetIdProp(jQuery(inputPageClasseurFolderPath).val(), jQuery(inputPageClasseurFolderId).val(), jQuery(inputPageClasseurFolderTitle).val(), jQuery(inputPageClasseurTitle).val()));
		        }
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert( textStatus + ':' + errorThrown );
			}
			/*
	        ,complete: function(jqXHR, textStatus) {alert('complete');}
			 */
		});
    });
	jQuery('#selectFolderBtn').click(function() {
		jQuery(this).hide();
		var checkbox = jQuery('#divConfigGadget form input[name=relativePath]');
		if (jQuery(checkbox).is(':checked')) {
			jQuery(checkbox).click();
		}
		jQuery('#divConfigGadget form input[name=relativePath]').attr('checked', false);
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
