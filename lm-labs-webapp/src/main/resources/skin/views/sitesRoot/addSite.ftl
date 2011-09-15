
<h1>${Context.getMessage('label.labssite.add.site')}</h1>

<form method="post" id="form-labssite" action="${This.path}" method="post">
  <fieldset>
    <div class="clearfix">
      <label for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
      <div class="input">
        <input class="required" name="labsSiteTitle"/>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="input">
        ${This.URL}/<input class="required" name="labsSiteURL"/>
        <span class="help-block">C'est par ce lien que le site sera accessible</span>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <label for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
      <div class="input">
        <textarea name="labsSiteDescription" id="labsSiteDescription" ></textarea>
      </div>
    </div><!-- /clearfix -->
  </fieldset>
  <div class="actions">
    <button class="btn primary">${Context.getMessage('label.labssites.edit.valid')}</button>
  </div>
</form>

<script type="text/javascript">
  $(document).ready(function() {
      $.validator.messages.required = "${Context.getMessage('label.labssites.edit.required')}";
  });
</script>
