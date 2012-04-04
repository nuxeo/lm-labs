<#assign propValue = Document.dublincore.creator />
<#if line != null && line.getEntryByIdHead(header.idHeader) != null>
    <#assign prop = line.getEntryByIdHead(header.idHeader).text />
    <#if 0 < prop?length >
        <#-- on devrait TOUJOURS rentrer ici -->
        <#assign propValue = Document[prop] />
    </#if>
</#if>
<input class="input disabled" type="text" disabled="" value="${userFullName(propValue)?html}" title="${Context.getMessage('tooltip.pageList.edit.line.readonly.field')}" />
