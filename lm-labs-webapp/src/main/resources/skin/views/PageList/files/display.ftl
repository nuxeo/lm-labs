<#assign nbrAttachedFiles = entriesLine.getNbrFiles(Context.coreSession) />
<input type="hidden" value="${entriesLine.docLine.name}" />
<a href="#" class="btn btn-mini<#if This.allContributors > openLineFiles open-dialog</#if><#if nbrAttachedFiles == 0> noFileAttached</#if>" rel="lineFiles" style="padding-right:3px;" >
    <i class="icon-file" ></i>
</a>
