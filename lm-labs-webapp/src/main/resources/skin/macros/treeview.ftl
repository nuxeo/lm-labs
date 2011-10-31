<#macro labsTreeview view="treeview">
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
    url: "${This.path}/@treeview?view=${view}",
        persist: "cookie",
        control: "#treeviewControl",
        collapsed: false,
        cookieId: "${site.document.id}-doctree-navtree"
  });

});
</script>
</#macro>