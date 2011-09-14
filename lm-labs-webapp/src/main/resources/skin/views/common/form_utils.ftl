<#--
This is a generic WYSIWYG EDITOR, to use it include this FTL and specify needed parameters
/!\ 'comment_form', 'callback_function', must be defined in the calling FTL

ALL FIELD MUST HAVE ONE OF THESE CLASSES: formInput or formWysiwyg
-->
<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />

<#if !Context.principal.isAnonymous() && canWrite>
  <#--div class="editButton">
    <button id="editCommentButton" class="edit btn small">${Context.getMessage('command.Page.CommentModify')}</button>
    <button id="saveCommentButton" class="save primary btn small">${Context.getMessage('command.Page.CommentSave')}</button>
    <button id="cancelCommentButton" class="cancel btn small">${Context.getMessage('command.Page.CommentCancel')}</button>
  </div-->
  
  <script type="text/javascript">
	var ckeditorconfig = {
	filebrowserBrowseUrl : '${This.path}/displayBrowseTree',
	filebrowserImageBrowseUrl : '${This.path}/displayBrowseTree',
	filebrowserFlashBrowseUrl : '${This.path}/displayBrowseTree',
	filebrowserUploadUrl : '${This.path}/displayBrowseTree',
	filebrowserImageUploadUrl : '${This.path}/displayBrowseTree',
	filebrowserFlashUploadUrl : '${This.path}/displayBrowseTree',
	toolbar:
	[
	['Source','-','Preview','-'],
	['Cut','Copy','Paste','PasteText','PasteFromWord'],
	
	['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
	'/',
	[ 'Bold','Italic','Strike','-','SelectAll','RemoveFormat'],
	[ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar' ] ,'/',
	['Format','Font','FontSize'],
	['TextColor','BGColor'],
	['Maximize', 'ShowBlocks','-']
	]
	};
		
	$('#commentField').ckeip({
		e_url: '${This.path}/updateCommentaire',
		data: {
			commentaire : jQuery("#commentField").html()
		},	
		ckeditor_config: ckeditorconfig
	});	
		
  var editorList = new Array();

  jQuery(document).ready(function() {
      jQuery("button.edit").click(function() {
          editMode();
    });
    jQuery("button.save").click(function() {
      readonlyMode(true);
    });
    jQuery("button.cancel").click(function() {
      readonlyMode(false);
    });

    readonlyMode(false);
  });

  function editMode() {
    jQuery("button.edit").hide();
    jQuery("button.save").show();
    jQuery("button.cancel").show();

    jQuery("#${form_name}").children('.formInput').each(function (i) {
      var val = jQuery(this).html();
          jQuery(this).html("<input type='text' value='"+val+"' />");
      });

      jQuery("#${form_name}").children('.formWysiwyg').each(function (i) {
           var id = jQuery(this).attr("id");
           var val = jQuery(this).html();
           jQuery(this).html("<textarea id='${form_name}_"+id+"' cols='110' rows='10'>"+val+"</textarea>");
           
           /*var editor = CKEDITOR.replace("${form_name}_"+id,
           		{
					filebrowserBrowseUrl : '${This.path}/displayBrowseTree',
					filebrowserImageBrowseUrl : '${This.path}/displayBrowseTree',
					filebrowserFlashBrowseUrl : '${This.path}/displayBrowseTree',
					filebrowserUploadUrl : '${This.path}/displayBrowseTree',
					filebrowserImageUploadUrl : '${This.path}/displayBrowseTree',
					filebrowserFlashUploadUrl : '${This.path}/displayBrowseTree'
				}
           );
           editorList[i] = editor;*/
      });
  }

  function readonlyMode(toBeSaved) {
    jQuery("button.edit").show();
    jQuery("button.save").hide();
    jQuery("button.cancel").hide();

    jQuery("#${form_name}").children('.formInput').each(function (i) {
          var val = jQuery(this).children().val();
          jQuery(this).html(val);
      });

     jQuery("#${form_name}").children('.formWysiwyg').each(function (i) {
           var val = editorList[i].getData();
           var id = jQuery(this).attr("id");
           if(val!=null) {
             jQuery(this).html(val);
             jQuery(id).html(val);
           }
           /*editorName = "${form_name}_"+id;
           alert(editorMap.editorName);
           CKEDITOR.remove(editorMap.editorName);*/
      });

      if(toBeSaved==true && editorList.length>0) {
        ${callback_function}();
      }

      for(i=0; i<editorList.length; i++) {
          CKEDITOR.remove(editorList[i]);
      }
  }
  </script>
  </#if>