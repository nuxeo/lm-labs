<textarea cols="120" rows="10" name="message" id="editor">
</textarea>
<div class="commentButton">
	<button class="edit">Modifier le commentaire</button>
	<button class="save">Enregistrer</button>
	<button class="cancel">Annuler</button>
</div>

<script type="text/javascript">
var editor;
init();
function init() {
	jQuery("button[class=edit]").click(function() {
		editMode();
	});
	jQuery("button[class=save]").click(function() {
		readonlyMode();
	});
	jQuery("button[class=cancel]").click(function() {
		readonlyMode();
	});
	
	readonlyMode();
}
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
function readonlyMode() {
	jQuery("button[class=edit]").show();
	jQuery("button[class=save]").hide();
	jQuery("button[class=cancel]").hide();
	if(editor!=null) {
		CKEDITOR.remove(editor);
	}
	jQuery("#cke_editor").remove();
	jQuery("#editor").show();
	jQuery("#editor").attr("style", null);
}
</script>