<#--
This is a generic WYSIWYG EDITOR, to use it include this FTL and specify needed parameters
/!\ 'comment_form', 'callback_function', must be defined in the calling FTL

ALL FIELD MUST HAVE ONE OF THESE CLASSES: formInput or formWysiwyg
-->
<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />

<#if !Context.principal.isAnonymous() && canWrite>
  <div class="editButton">
    <button class="edit">${Context.getMessage('command.Page.CommentModify')}</button>
    <button class="save">${Context.getMessage('command.Page.CommentSave')}</button>
    <button class="cancel">${Context.getMessage('command.Page.CommentCancel')}</button>
  </div>

  <script type="text/javascript">
  var editorList = new Array();

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

    readonlyMode(false);
  });

  function editMode() {
    jQuery("button[class=edit]").hide();
    jQuery("button[class=save]").show();
    jQuery("button[class=cancel]").show();

    jQuery("#${form_name}").children('.formInput').each(function (i) {
      var val = jQuery(this).html();
          jQuery(this).html("<input type='text' value='"+val+"' />");
      });

      jQuery("#${form_name}").children('.formWysiwyg').each(function (i) {
           var id = jQuery(this).attr("id");
           var val = jQuery(this).html();
           jQuery(this).html("<textarea id='${form_name}_"+id+"' cols='110' rows='10'>"+val+"</textarea>");
           var editor = CKEDITOR.replace("${form_name}_"+id);
           editorList[i] = editor;
           /*editor.on("instanceReady", function() {
           CKEDITOR.instances.editor.document.on('keydown', function() {
              CKEDITOR.tools.setTimeout( function() {
                  $("#${form_name}_"+id).val(CKEDITOR.instances.editor.getData());
              }, 0);
           });
        });*/
      });
  }

  function readonlyMode(toBeSaved) {
    jQuery("button[class=edit]").show();
    jQuery("button[class=save]").hide();
    jQuery("button[class=cancel]").hide();

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