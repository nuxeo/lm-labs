<style>
#form-labssite .input input[type="checkbox"] {
	margin-top: 6px;
	float: left;
}
	
#form-labssite .input label {
	text-align: left;
	width: 90%;
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
jQuery(document).ready(function() {
	jQuery('#siteTemplate').click(function() {
		if (jQuery(this).is(':checked')) {
			jQuery('#siteTemplatePreviewDiv').show();
		} else {
			jQuery('#siteTemplatePreviewDiv').hide();
		}
	});
});
</script>
