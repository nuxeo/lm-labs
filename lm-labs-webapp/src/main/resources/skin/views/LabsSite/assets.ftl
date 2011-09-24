<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
      <title></title>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
      <link rel="stylesheet/less" href="${skinPath}/less/labs.less">
      <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.1.4.min.js"></script>

      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/browse_tree.css"/>
    </head>
    <body>


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
          <div class="span12 columns well">
            Actions
          </div>
          <div class="span12 columns">
            Content
          </div>
        </div>
      </div>
    </div>


   <script>
            $(document).ready(function() {
              $('#treenav').treeview({
                url: "${This.path}/json",
                persist: "cookie",
                control: "#navtreecontrol",
                collapsed: true,
                cookieId: "${site.document.id}-assets-navtree"
              });
            });

    function sendToCKEditor(href) {
      window.opener.CKEDITOR.tools.callFunction('2', href);
      window.close();
    }
    </script>
  </body>
</html>