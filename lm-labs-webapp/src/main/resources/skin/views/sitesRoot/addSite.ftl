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

<form method="post" enctype="multipart/form-data" id="form-labssite" action="${This.path}">
  <fieldset>
    <div class="clearfix">
      <label for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
      <div class="input">
        <input class="required" name="dc:title" id="labsSiteTitle" required-error-text="${Context.getMessage('label.labssites.edit.required.title')}"/>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="input">
        ${This.URL}/<input class="required" name="webc:url" id="labsSiteURL" required-error-text="${Context.getMessage('label.labssites.edit.required.url')}"/>
        <span class="help-block">C'est par ce lien que le site sera accessible</span>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
      <div class="input">
        <textarea name="dc:description" id="labsSiteDescription" ></textarea>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="piwik:piwikId">${Context.getMessage('label.labssite.edit.piwikId')}</label>
      <div class="input">
        <input name="piwik:piwikId" id="piwik:piwikId" />
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <div class="input">
        <input id="siteTemplate" type="checkbox" name="labssite:siteTemplate" />
        <label for="siteTemplate">&nbsp;${Context.getMessage('label.labssite.edit.siteTemplate')}</label>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix" id="siteTemplateRadioDiv">
      <label for="siteTemplate">&nbsp;${Context.getMessage('label.labssite.edit.siteTemplateName')}</label>
      <div class="input">
        <span class="radioSiteTemplate">
          <input type="radio" name="siteTemplateId" value="" onclick="cleanPreview();" checked="checked" ></input>
          <span onclick="clickParentInput(this);" >${Context.getMessage('label.labssite.edit.siteTemplateName.none')}</span>
        </span>
        <#list Common.getTemplateSites(Session) as templateName >
        <span class="radioSiteTemplate">
          <input type="radio" name="siteTemplateId" value="${templateName.document.id}" 
        	<#if templateName.hasSiteTemplatePreview()>
        	onclick="onSelectRadio(this, '${Root.getLink(templateName.document)}/@blob');"
        	<#else>
        	onclick="cleanPreview();"
        	</#if>
        	>
          </input>
          <span onclick="clickParentInput(this);" rel="popover" data-content="${templateName.document.dublincore.description?html}" data-original-title="${Context.getMessage('label.description')}">${templateName.document.dublincore.title}</span>
        </span>
        </#list>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <div id="templatePreview" ></div>
    </div><!-- /clearfix -->

	<div class="clearfix" id="siteTemplatePreviewDiv" style="display:none;" >
      <label for="siteTemplatePreview">${Context.getMessage('label.labssite.edit.siteTemplatePreview')}</label>
      <div class="input">
        <input name="labssite:siteTemplatePreview" type="file" size="30" id="siteTemplatePreview" />
      </div>
    </div><!-- /clearfix -->
  </fieldset>
  <div class="actions">
    <button class="btn primary required-fields" form-id="form-labssite">${Context.getMessage('label.labssites.edit.valid')}</button>
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
