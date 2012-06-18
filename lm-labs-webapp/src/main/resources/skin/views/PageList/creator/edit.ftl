<#if line != null && line.getEntryByIdHead(header.idHeader) != null>
    <#assign prop = line.getEntryByIdHead(header.idHeader).text />
    <#if 0 < prop?length >
        <#-- on devrait TOUJOURS rentrer ici -->
        <#attempt>
	        <#assign propValue = Document[prop] />
	        <#assign propValueStr = userFullName(propValue) />
        <#recover>
        	<#assign propValueStr = "N.D." />
        </#attempt>
    </#if>
</#if>
<input class="input disabled" type="text" disabled="" value="${propValueStr?html}" title="${Context.getMessage('tooltip.pageList.edit.line.readonly.field')}" />
