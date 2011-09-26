<@extends src="views/LabsSite/sitemap-base.ftl">
     <@block name="sitemap-content">
       <div id="treeviewControl">
          <a href="#" id="reduceLink">Tout réduire</a>
          <a href="#" id="expandLink">Tout étendre</a>
        </div>
        <div>
        <ul id="tree">
        </ul>
        </div>

        <script type="text/javascript">
        jQuery(document).ready( function() {
          jQuery("#tree").treeview({
            url: "${This.path}/treeview",
                persist: "cookie",
                control: "#treeviewControl",
                collapsed: false,
                cookieId: "${site.document.id}-doctree-navtree"
          });

        });
        </script>
    </@block>
</@extends>