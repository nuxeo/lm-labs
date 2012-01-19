<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
      <title></title>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
        <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
        <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>

	  <link rel="stylesheet/less" href="${Context.modulePath}/${site.URL}/generated.less">
      <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.1.4.min.js"></script>

      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
    </head>
    <body>
	<#-- timeout -->
    <input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />

  <div id="FKtopContent">
    <div class="container-fluid">

      <div class="sidebar">
        <div class="bloc">
          <div class="header">
            Arborescence
          </div>




           <div class="treeroot"></div>
           <ul id="treenav" class="treeview">

           </ul>

        </div> <!-- bloc -->
      </div>


      <div class="content">
        <div class="row">
          <div id="fileContent" class="span11 columns well" style="min-height:300px;">
            <#include "views/AssetFolder/content.ftl"/>
          </div>
          <div class="span9 columns actions">
            <a href="#" rel="addFileDialog" class="open-dialog btn">Ajouter un fichier</a>
            <a href="#" rel="addFolderDialog" class="open-dialog btn">Ajouter un répertoire</a>
          </div>

          <div id="addFolderDialog" style="display:none;">
            <h1>Ajouter un répertoire</h1>
            <form id="addFolderForm" action="${This.path}" onSubmit="this.action=currentPath" method="post">
              <input type="hidden" name="doctype" value="Folder"/>
              <fieldset>
                <div class="clearfix">
                    <label for="title">Nom du répertoire</label>
                      <div class="input">
                        <input name="dublincore:title" class="required"/>
                      </div>
                    </div><!-- /clearfix -->
              </fieldset>
              <div class="actions">
                <button type="submit" class="btn primary required-fields" form-id="addFolderForm">Ajouter</button>
              </div>
            </form>
          </div>
			
		 <#include "macros/add_file_dialog.ftl" />
		 <@addFileDialog action="${This.path}" onSubmit="this.action=currentPath"/>

        </div>
      </div>
    </div>


   <script>
   var currentPath = "${This.path}";
   $(document).ready(function() {
     $('#treenav').treeview({
       url: "${Context.modulePath}/${site.URL}/@assets/json",
       persist: "cookie",
       control: "#navtreecontrol",
       collapsed: true,
       cookieId: "${site.document.id}-assets-navtree"
      }
    );
  });



    function sendToCKEditor(href) {
      window.opener.CKEDITOR.tools.callFunction(${This.getCKEditorFuncNum()}, href);
      window.close();
    }
    </script>
  </div>
  </body>
</html>