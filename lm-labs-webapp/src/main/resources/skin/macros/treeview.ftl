<#macro labsTreeview view="treeview">
<div class="row-fluid" id="treeviewControl">
	<a class="btn btn-mini" href="#" id="reduceLink"><i class="icon-minus"></i>Tout réduire</a>
	<a class="btn btn-mini" href="#" id="expandLink"><i class="icon-plus"></i>Tout étendre</a>
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