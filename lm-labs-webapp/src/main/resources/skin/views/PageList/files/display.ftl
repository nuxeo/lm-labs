<#assign nbrAttachedFiles = entriesLine.getNbrFiles() />
<input type="hidden" value="${entriesLine.docLine.name}" />
<a href="#" class="btn btn-mini openLineFiles open-dialog<#if nbrAttachedFiles == 0> noFileAttached</#if>" rel="lineFiles" style="padding-right:3px;"
	data-original-title="${Context.getMessage('label.PageList.line.nbAttachedFiles', nbrAttachedFiles)}" >
    <i class="icon-file" ></i>
</a>
