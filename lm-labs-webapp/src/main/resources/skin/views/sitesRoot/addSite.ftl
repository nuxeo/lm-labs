<style>
#form-labssite .input input[type="checkbox"] {
	margin-top: 6px;
	float: left;
}
	
#form-labssite .input label {
	text-align: left;
	width: 90%;
}
.radioSiteTemplate {
	display:block;
}
</style>

<h1>${Context.getMessage('label.labssite.add.site')}</h1>

<form class="form-horizontal" method="post" enctype="multipart/form-data" id="form-labssite" action="${This.path}/"> <#-- trailing slash in form's URL is very important, DONT'T REMOVE !! -->
  <fieldset>
    <div class="control-group">
      <label class="control-label" for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
      <div class="controls">
        <input class="focused required input" name="dc:title" id="labsSiteTitle" required-error-text="${Context.getMessage('label.labssites.edit.required.title')}"/>
      </div>
    </div>

    <div class="control-group">
      <label class="control-label" for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="controls">
        <span>${This.URL}/</span><input class="input required" name="webc:url" id="labsSiteURL" required-error-text="${Context.getMessage('label.labssites.edit.required.url')}"/>
        <p class="help-block">C'est par ce lien que le site sera accessible</p>
      </div>
    </div>

    <div class="control-group">
      <label class="control-label" for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
      <div class="controls">
        <textarea class="input" name="dc:description" id="labsSiteDescription" ></textarea>
      </div>
    </div>

<#--
    <div class="control-group">
      <label class="control-label" for="piwik:piwikId">${Context.getMessage('label.labssite.edit.piwikId')}</label>
      <div class="controls">
        <input class="input" name="piwik:piwikId" id="piwik:piwikId" />
      </div>
    </div>
-->

	<#if Common.canCreateTemplateSite(Context.principal.name) >
    <div class="control-group">
      <div class="controls">
        <label class="checkbox" for="siteTemplate">
          <input id="siteTemplate" type="checkbox" name="labssite:siteTemplate" />
        &nbsp;${Context.getMessage('label.labssite.edit.siteTemplate')}</label>
      </div>
    </div>
    </#if>

    <div class="control-group" id="siteTemplateRadioDiv">
      <label class="control-label" for="siteTemplate">&nbsp;${Context.getMessage('label.labssite.edit.siteTemplateName')}</label>
      <div class="controls">
        <span class="radioSiteTemplate">
          <input class="radio" type="radio" name="siteTemplateId" value="" onclick="cleanPreview();" checked="checked" ></input>
          <span onclick="clickParentInput(this);" >${Context.getMessage('label.labssite.edit.siteTemplateName.none')}</span>
        </span>
        <#list Common.getTemplateSites() as templateName >
        <span class="radioSiteTemplate">
          <input class="radio" type="radio" name="siteTemplateId" value="${templateName.document.id}" 
        	<#if templateName.hasElementPreview()>
        	onclick="onSelectRadio(this, '${Context.modulePath}/@templatePreview/${templateName.URL}');"
        	<#else>
        	onclick="cleanPreview();"
        	</#if>
        	>
          </input>
          <span onclick="clickParentInput(this);" rel="popover" data-content="${templateName.document['dc:description']?html}" data-original-title="${Context.getMessage('label.description')}">${templateName.document['dc:title']}</span>
        </span>
        </#list>
      </div>
    </div>

    <div class="control-group">
      <div id="templatePreview" ></div>
    </div>

	<div class="control-group" id="siteTemplatePreviewDiv" style="display:none;" >
      <label class="control-label" for="siteTemplatePreview">${Context.getMessage('label.labssite.edit.siteTemplatePreview')}</label>
      <div class="controls">
        <input class="input-file" name="labssite:siteTemplatePreview" type="file" size="30" id="siteTemplatePreview" />
      </div>
    </div>
  </fieldset>
  <div class="actions">
    <button class="btn btn-primary required-fields" form-id="form-labssite">${Context.getMessage('label.labssites.edit.valid')}</button>
    <button class="btn" type="reset">Reset</button>
  </div>
</form>
<script>
function onSelectRadio(obj, url) {
	jQuery('#templatePreview').html('<img src="' + url + '" onerror="function(){};" style="width:400px;display:block;margin:auto;"/>');
}
function cleanPreview() {
	jQuery('#templatePreview').html('');
}
jQuery(function () {
		jQuery("span[rel=popover]").popover({offset: 10, html:true});
	}
) 
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
</script>
