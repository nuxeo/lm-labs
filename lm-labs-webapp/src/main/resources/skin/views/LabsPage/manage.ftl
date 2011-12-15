<h1>Ajouter du contenu</h1>

<form id="add_doc_form" action="${This.path}/@addContent" method="post">
  <fieldset>
    <div class="clearfix">
      <label for="name">${Context.getMessage('label.title')}</label>
      <div class="input">
        <input name="name" class="required"/>
      </div>
    </div><!-- /clearfix -->
    <div class="clearfix">
      <label for="dc:description">${Context.getMessage('label.description')}</label>
      <div class="input">
        <textarea name="description"/>
        </textarea>
      </div>
    </div><!-- /clearfix -->

    <div class="clearfix">
      <#assign page = Common.sitePage(Document) />
      <label for="doctype">${Context.getMessage('label.doctype')}</label>
        <#list page.allowedSubtypes as type>
          <div class="input">
            <label><input type="radio" name="doctype" value="${type}" checked> ${Context.getMessage('label.doctype.'+type)}</label>
          </div>
        </#list>
    </div><!-- /clearfix -->
    
    <div class="clearfix">
      <label for="location">${Context.getMessage('label.page.creation.location')}</label>
      <div class="input">
        <select name="location">
          <#assign locations = ["top", "same", "under"] />
          <#list locations as location>
          <option value="${location}" <#if location == "under">selected="selected"</#if>>${Context.getMessage('label.page.creation.location.' + location)}</option>
          </#list>
        </select>
      </div>
    </div><!-- /clearfix -->

  </fieldset>

  <div class="actions">
    <button class="btn primary required-fields" form-id="add_doc_form">Créer</button>
  </div>
</form>

<script type="text/javascript">
	initRequiredFields();
</script>