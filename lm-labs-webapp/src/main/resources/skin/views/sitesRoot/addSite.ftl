
<h1>${Context.getMessage('label.labssite.add.site')}</h1>

<form method="post" id="form-labssite" action="${This.path}">
  <fieldset>
    <div class="clearfix">
      <label for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
      <div class="input">
        <input class="required" name="labsSiteTitle" id="labsSiteTitle" required-error-text="${Context.getMessage('label.labssites.edit.required.title')}"/>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="input">
        ${This.URL}/<input class="required" name="labsSiteURL" id="labsSiteURL" required-error-text="${Context.getMessage('label.labssites.edit.required.url')}"/>
        <span class="help-block">C'est par ce lien que le site sera accessible</span>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
      <div class="input">
        <textarea name="labsSiteDescription" id="labsSiteDescription" ></textarea>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="piwik:piwikId">${Context.getMessage('label.labssite.edit.piwikId')}</label>
      <div class="input">
        <input name="piwik:piwikId" id="piwik:piwikId" />
      </div>
    </div><!-- /clearfix -->
  </fieldset>
  <div class="actions">
    <button class="btn primary required-fields" form-id="form-labssite">${Context.getMessage('label.labssites.edit.valid')}</button>
  </div>
</form>
