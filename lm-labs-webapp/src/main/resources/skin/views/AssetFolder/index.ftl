<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
<#assign bsMinified = ".min" />
<#assign popoverPlacement = "" />
<#assign mySite=Common.siteDoc(Document).site />
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      	<title>Interface des médias</title>
      	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.2.min.js"></script>
      	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
      	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
      	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
      	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
        <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.placeholder.min.js"></script> 
        <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip${bsMinified}.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover${bsMinified}.js"></script>

	  <link rel="stylesheet/less" href="${Context.modulePath}/${mySite.URL}/generated.less">
      <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.3.0.min.js"></script>

      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
      <link rel="icon" type="image/x-icon" href="/nuxeo/img/logo.jpeg" />
      <link rel="shortcut icon"  type="image/x-icon" href="/nuxeo/img/logo.jpeg"/>
    </head>
    <body id="body">
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
            <a href="#" rel="addFileDialog" class="open-dialog btn"><i class="icon-file"></i>Ajouter un fichier</a>
            <a href="#" rel="addFolderDialog" class="open-dialog btn"><i class="icon-folder-close"></i>Ajouter un répertoire</a>
          </div>

          <div id="addFolderDialog" style="display:none;">
            <h1>Ajouter un répertoire</h1>
            <form class="form-horizontal" id="addFolderForm" action="${This.path}" onSubmit="this.action=currentPath" method="post">
              <input type="hidden" name="doctype" value="Folder"/>
              <fieldset>
                <div class="control-group">
                    <label class="control-label" for="title">Nom du répertoire</label>
                      <div class="controls">
                        <input name="dublincore:title" class="required input"/>
                      </div>
                    </div>
              </fieldset>
              <div class="actions">
                <button type="submit" class="btn btn-primary required-fields" form-id="addFolderForm">Ajouter</button>
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
       url: "${Context.modulePath}/${mySite.URL}/@assets/json",
       persist: "cookie",
       control: "#navtreecontrol",
       collapsed: true,
       cookieId: "${mySite.document.id}-assets-navtree"
      }
    );    
  });

    function sendToCallFunction(href) {
    	window.opener.${This.getCallFunction()}('${This.getCalledRef()}', href);
        window.close();
    }
    </script>
  </div>
  </body>
</html>