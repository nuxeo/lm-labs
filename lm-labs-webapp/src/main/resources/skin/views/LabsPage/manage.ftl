<h1>Ajouter du contenu</h1>

<form id="add_doc_form" action="${This.path}" method="post">
  <fieldset>
    <div class="clearfix">
      <label for="name">${Context.getMessage('label.title')}</label>
      <div class="input">
        <input name="name"/>
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
            <label><input type="radio" name="doctype" value="${type}"> ${Context.getMessage('label.doctype.'+type)}</label>
          </div>
        </#list>
    </div><!-- /clearfix -->

  </fieldset>

  <div class="actions">
    <btn type="input" class="btn primary" href="${This.path}" onclick="$('#add_doc_form').submit();">Créer</a>
  </div>

</form>