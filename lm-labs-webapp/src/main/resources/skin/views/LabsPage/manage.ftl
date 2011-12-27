<h1>Ajouter du contenu</h1>

<form id="add_doc_form" action="${This.path}" method="post">
  <input name="overwritePage" id="overwritePage" type="hidden" value="false" />
  <fieldset>
    <div class="clearfix">
      <label for="name">${Context.getMessage('label.title')}</label>
      <div class="input">
        <input name="name" id="name" class="required" />
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
    </div><!-- /clearfix -->

  </fieldset>

  <div class="actions">
    <button class="btn primary required-fields btnCreatePage" form-id="add_doc_form">Créer</button>
  </div>
</form>

<script type="text/javascript">
		
	
	jQuery(document).ready(function(){
	  initRequiredFields();
	
	  $(".btnCreatePage").click(function(event) {
	    event.preventDefault();
	    jQuery(".btnCreatePage").attr("disabled", true);
		jQuery.ajax({
			type: "POST",
			url: jQuery("#add_doc_form").attr("action") + '/@addContent',
			data: $("#add_doc_form").serialize(),		
			success: function(msg){
				if (msg == "existedPageName"){
						<#if site?? && site.isAdministrator(Context.principal.name)>
							if (confirm("${Context.getMessage('label.page.creation.existDeletedPageName.administrator')?js_string}")){
								addDocWithDelete();
							}
						<#else>
							<#if site?? && site.isContributor(Context.principal.name)>
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
	  });
	});
	
function addDocWithDelete(){
	jQuery("#overwritePage").val("true");
	jQuery.ajax({
		type: "POST",
		url: jQuery("#add_doc_form").attr("action") + '/@addContent',
		data: $("#add_doc_form").serialize(),		
		success: function(msg){
			document.location.href = msg;
		},
		error: function(msg){
			alert(msg);
		}
	});
}

</script>