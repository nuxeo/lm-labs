<#assign mySite=Common.siteDoc(Document).site />
<h1>Ajouter du contenu</h1>

<form class="form-horizontal" onsubmit="addDoc(null);return false;" id="add_doc_form" action="${This.path}" method="post">
  <input name="overwritePage" id="overwritePage" type="hidden" value="false" />
  <fieldset>
    <div class="control-group">
      <label class="control-label" for="name">${Context.getMessage('label.title')}</label>
      <div class="controls">
        <input name="dc:title" id="name" class="focused required input" />
      </div>
    </div>
    <div class="control-group">
      <label class="control-label" for="description">${Context.getMessage('label.description')}</label>
      <div class="controls">
        <textarea class="input" id="description" name="dc:description"/>
        </textarea>
      </div>
    </div>

    <div class="control-group">
      <#assign page = Common.sitePage(Document) />
      <label class="control-label" for="doctype">${Context.getMessage('label.doctype')}</label>
        <#list page.getAllowedSubtypes(Context.coreSession) as type>
          <div class="controls">
            <label><input class="radio" type="radio" name="doctype" value="${type}" checked> ${Context.getMessage('label.doctype.'+type)}</label>
          </div>
        </#list>
    </div>
    
    <div class="control-group">
      <label class="control-label" for="location">${Context.getMessage('label.page.creation.location')}</label>
      <div class="controls">
        <select name="location">
          <#assign locations = ["top"] />
      	  <#assign defaultLocation = "top" />
          <#if This.isInstanceOf("LabsPage") && This.type.name != "LabsSite" >
            <#assign locations = locations + ["same", "under"] />
            <#assign defaultLocation = "under" />
          <#else>
          </#if>
          <#list locations as location>
          <option value="${location}" <#if location == defaultLocation>selected="selected"</#if>>${Context.getMessage('label.page.creation.location.' + location)}</option>
          </#list>
        </select>
      </div>
    </div>

  </fieldset>

  <div class="actions">
    <button class="btn btn-primary required-fields btnCreatePage" form-id="add_doc_form" >Créer</button>
  </div>
</form>

<script type="text/javascript">

jQuery(document).ready(function(){
	initRequiredFields();
});

function addDoc(event) {
	if(hasError) {
		return;
	}
	jQuery('#waitingPopup').dialog2('open');
	if (event != null) {
        event.preventDefault();
	}
    jQuery(".btnCreatePage").attr("disabled", true);
	jQuery.ajax({
		type: "POST",
		url: jQuery("#add_doc_form").attr("action") + '/@addContent',
		data: $("#add_doc_form").serialize(),		
		success: function(msg){
			if (msg == "existedPageName"){
					<#if mySite?? && mySite.isAdministrator(Context.principal.name)>
						if (confirm("${Context.getMessage('label.page.creation.existDeletedPageName.administrator')?js_string}")){
							addDocWithDelete();
						}
					<#else>
						<#if mySite?? && mySite.isContributor(Context.principal.name)>
							alert("${Context.getMessage('label.page.creation.existDeletedPageName.contributor')?js_string}");
						</#if>
					</#if>
				jQuery("#name").val("");
			}
			else{
				document.location.href = msg;
			}
			jQuery(".btnCreatePage").attr("disabled", false);
		},
		error: function(msg){
			alert(msg);
		}
	});
	jQuery('#waitingPopup').dialog2('close');
}
	
function addDocWithDelete(){
	jQuery("#overwritePage").val("true");
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "POST",
		url: jQuery("#add_doc_form").attr("action") + '/@addContent',
		data: $("#add_doc_form").serialize(),		
		success: function(msg){
			document.location.href = msg;
		},
		error: function(msg){
			alert(msg);
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}

</script>