<style>
label.error { float: none; color: red; font-size:12px; padding-left: .5em;  }
</style>
<div class="titleEdit">
	${Context.getMessage('label.labssite.edit.site')}
</div>
<form method="post" id="form-labssite" action="${This.pathForEdit}/persistLabsSite">
	<fieldset>	
		<ul class="fieldEdit">
			<li>
				<label for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label><br />
				<input type="text" class="required" name="labsSiteTitle" id="labsSiteTitle" size="60" value="<#if This.labsSite != null >${This.labsSite.title}</#if>" />
			</li>
			<li>
				<label for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label><br />
    			${This.URL}/<input type="text" class="required" name="labsSiteURL" size="32" id="labsSiteURL" value="<#if This.labsSite != null >${This.labsSite.URL}</#if>" />
			</li>
			<li>
				<label for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label><br />		
    			<textarea name="labsSiteDescription" id="labsSiteDescription" class="labsSiteDescription"><#if This.labsSite != null >${This.labsSite.description}</#if></textarea>
			</li>
		</ul>
	</fieldset>
	<div class="labssiteOK">
		<button class="inputSubmit" id="submit_button"  name="submit_button" onClick="javascript:sendForm('${This.path}');" >${Context.getMessage('label.labssites.edit.valid')}</button>
	</div>
	<input type="hidden" name="labssiteId" id="labssiteId" value="<#if This.labsSite == null >-1<#else>${This.labsSite.documentModel.id}</#if>" />
	<input type="hidden" name="oldURL" id="oldURL" value="<#if This.labsSite != null >${This.labsSite.URL}</#if>" />
</form>
<script type="text/javascript">
	$(document).ready(function() {
	    $.validator.messages.required = "${Context.getMessage('label.labssites.edit.required')}";
	});
	
	function sendForm(path){
		
		$("#form-labssite").validate();
	    var options = {  
			beforeSubmit: function(){
				return $("#form-labssite").valid();
			},
	        success: function(responseText, statusText) {
	        	if (statusText == "notmodified"){
	        		$("#labsSiteURL").val($("#oldURL").val());
					alert("${Context.getMessage('label.labssites.edit.error')}");
	        	}else{
	        		document.location.href=path;
	        	}
	        },
	        error: function(){
	          alert("ERROR");
	        }
	    };
	    $('#form-labssite').ajaxForm(options);
	   }
</script>
