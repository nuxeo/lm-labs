<#macro jsTreeControls treeId>
<style>
.mini {
	font-size: 11px;
	padding: 3px 5px 3px 6px;
}
</style>
<div class="span12 columns jsTreeControls">
	<button class="btn btn-mini" onclick="jQuery('#${treeId}').jstree('open_all');return false;">
	<i class="icon-plus"></i>${Context.getMessage('command.admin.tree.expandAll')}
	</button>
	<button class="btn btn-mini" onclick="jQuery('#${treeId}').jstree('close_all');return false;">
	<i class="icon-minus"></i>${Context.getMessage('command.admin.tree.collapseAll')}
	</button>
</div>
</#macro>
