<#-- 
This is a generic WYSIWYG EDITOR, to use it include this FTL and specify needed parameters
 
/!\ 'param_name', 'param_value', 'param_url' must be defined in the calling FTL  
-->
<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />

<div id="wysiwygEditor">
	<#if Context.principal.isAnonymous() || !canWrite>
		<p>${param_value}</p>
	<#else>
		<textarea cols="110" rows="10" name="message" id="editor">
		${param_value}
		</textarea>
		<div class="editButton">
			<button class="edit">Modifier</button>
			<button class="save">Enregistrer</button>
			<button class="cancel">Annuler</button>
		</div>
	
		<script type="text/javascript">
		var editor;
		
		jQuery(document).ready(function() {
			jQuery("button[class=edit]").click(function() {
				editMode();
			});
			jQuery("button[class=save]").click(function() {
				readonlyMode(true);
			});
			jQuery("button[class=cancel]").click(function() {
				readonlyMode(false);
			});
			
			readonlyMode();
		});
		
		function editMode() {
			jQuery("button[class=edit]").hide();
			jQuery("button[class=save]").show();
			jQuery("button[class=cancel]").show();
			
			editor = CKEDITOR.replace('editor');
		  	editor.on("instanceReady", function() {
		  	 	CKEDITOR.instances.editor.document.on('keydown', function() {
			  		CKEDITOR.tools.setTimeout( function() { 
			        	$("#editor").val(CKEDITOR.instances.editor.getData()); 
			    	}, 0);
				});
			});
		}
		function readonlyMode(toBeSaved) {
		
			jQuery("button[class=edit]").show();
			jQuery("button[class=save]").hide();
			jQuery("button[class=cancel]").hide();
			if(editor!=null) {
				if(toBeSaved==true) {
					var value = CKEDITOR.instances.editor.getData();
					
					jQuery.ajax({
					   type: "POST",
					   url: "${param_url}",
					   data: "${param_name}="+value,
					   success: function(msg){
					   	 jQuery("#editor").val(value);
					   },
					   error: function(msg){
					   	 alert("erreur " + msg);
					   }
					});
				}
				
				CKEDITOR.remove(editor);
			}
			jQuery("#cke_editor").remove();
			jQuery("#editor").show();
			jQuery("#editor").attr("style", null);
		}
		</script>
	</#if>
</div>