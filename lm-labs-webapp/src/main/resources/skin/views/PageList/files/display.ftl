<#assign nbrAttachedFiles = entriesLine.getNbrFiles() />
<input type="hidden" value="${entriesLine.docLine.name}" />
<a href="#" class="btn btn-mini openLineFiles open-dialog<#if nbrAttachedFiles == 0> noFileAttached</#if>" rel="lineFiles" style="padding-right:3px;" >
    <i class="icon-file" ></i>
</a>
