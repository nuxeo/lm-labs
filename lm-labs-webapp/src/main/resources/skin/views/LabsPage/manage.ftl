<#assign mySite=Common.siteDoc(Document).getSite() />
<h1>Ajouter du contenu</h1>
<style media="all" type="text/css">
	label.control-label {font-weight:bold; }
	.assistant {
		display: block;
		position: relative;
		top: 5%;
		left: 5%;
	}
	.dropdown-menu a {
		white-space: normal;
	}
	#divAssistantPagesPreview {
		margin-top: 30px;
		margin-left: 25px;
	}
</style>
<form onsubmit="addDoc(null);return false;" id="add_doc_form" action="${This.path}" method="post">
  <input name="overwritePage" id="overwritePage" type="hidden" value="false" />
  <input name="idPageTemplate" id="idPageTemplate" type="hidden" value="" />
  <fieldset>
  
  <div class="row-fluid">
  <div class="span6">
  
    <div class="control-group">
      <label class="control-label" for="name">${Context.getMessage('label.title')}</label>
      <div class="controls">
        <input name="dc:title" id="name" class="focused required input-xlarge" />
      </div>
    </div>
    <div class="control-group">
      <label class="control-label" for="description">${Context.getMessage('label.description')}</label>
      <div class="controls">
        <textarea class="input-xlarge" id="description" name="dc:description" style="height: 150px;"/>
        </textarea>
      </div>
    </div>
    
  </div><#-- /span6 -->
  <div class="span6">
    
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
        <br><br><br>
      </div>
    </div>
    
    <div class="control-group">
    	<label class="control-label" for="assistant">Mode de création</label>
    	<div class="controls">
		    <label class="radio inline">
			  <input type="radio" name="assistant" id="assistant" value="assistant" onChange="javascript:changeAssistant('assistant');" checked />
			  Assistant
			</label>
			<label class="radio">
			  <input type="radio" name="assistant" id="assistant" onChange="javascript:changeAssistant('blankPage');" value="blankPage" />
			  Page vierge
			</label>
		</div>
        <br>
	</div>

    <div id="divAssistantTypePage" class="control-group" style="display: none;">
      <#assign page = Common.sitePage(Document) />
      <label class="control-label" for="doctype">${Context.getMessage('label.doctype')}</label>
      	<div class="controls">
	        <select name="doctype">
		        <#list page.getAllowedSubtypes() as type>
		          <div class="controls">
		            <option value="${type}" <#if type == "HtmlPage">selected="selected"</#if> > ${Context.getMessage('label.doctype.'+type)}</option>
		          </div>
		        </#list>
		    </select>
		</div>
    </div>

  </div><#-- /span6 -->
  </div><#-- /row -->
  
  <div class="row-fluid" id="divAssistantContent">
  	<div class="span4">
  		<h3 style="text-align: center;">Catégories</h3>
		<ul class="dropdown-menu assistant" role="menu" aria-labelledby="dropdownMenu">
  			<#list categories as child >
			  <li><a tabindex="-1" onClick="javascript:loadPagesTemplate('${This.path}/@getPagesTemplate?id=${child.id}', '${skinPath}', this);" href="#"><i class="icon-arrow-right"></i>${child.title}</a></li>
  			</#list>
			  <li class="divider"></li>
			  <li><a tabindex="-1" onClick="javascript:loadPagesTemplate('${This.path}/@getPagesTemplateOfSite', '${skinPath}', this);" href="#"><i class="icon-arrow-right"></i>${Context.getMessage('label.list.elements.site.template')}</a></li>
		</ul>
  	</div><#-- /span4 -->
  	<div class="span4">
  		<h3 style="text-align: center;">Modèles de pages</h3>
  		<div id="divAssistantPages"></div>
  	</div><#-- /span4 -->
  	<div class="span4" id="divAssistantPagesPreview">
  		
  	</div><#-- /span4 -->
  </div><#-- /row -->
  
  
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
	//error if with assistant and no selected page
	if ( $("#assistant:checked").val() == 'assistant' && $("#idPageTemplate").val()  == ''){
		alert('${Context.getMessage('label.page.creation.assistant.noselected.page')?js_string}');
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