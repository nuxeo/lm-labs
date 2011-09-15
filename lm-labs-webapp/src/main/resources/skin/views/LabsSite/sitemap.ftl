<@extends src="views/LabsSite/sitemap-base.ftl">
     <@block name="sitemap-content">
       <div id="treeviewControl">
          <a href="#" id="reduceLink">Tout réduire</a>
        </div>
        <div>
        <ul id="tree">
          <li class="hasChildren">
            <span>Arborescence du site ${This.document.title}</span>
            <ul>
              <li><span class="placeholder">&nbsp;</span></li>
            </ul>
          </li>
        </ul>
      </div>

        <script type="text/javascript">
        jQuery(document).ready( function() {
          jQuery("#tree").treeview({
            url: "${This.path}/treeview",
            ajax: {
              data: { },
              type: "post"
            },
            control: "#treeviewControl"
          });
          jQuery(".hitarea").click();
        });
        </script>
    </@block>
</@extends>