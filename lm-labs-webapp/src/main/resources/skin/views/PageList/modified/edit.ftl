<#assign propValueStr = "N.D." />
<#assign tooltip = "" />
<#if line != null && line.getEntryByIdHead(header.idHeader) != null>
    <#assign prop = line.getEntryByIdHead(header.idHeader).text />
    <#if 0 < prop?length >
        <#-- on devrait TOUJOURS rentrer ici -->
        <#assign propValue = Document[prop] />
		<#if propValue != null>
	 	   <#assign propValueStr = propValue?string(header.formatDate) />
	 	   <#assign tooltip = propValue?string('yyyy-MM-dd hh:mm:ss') />
		</#if>
    </#if>
</#if>
<input class="input disabled" type="text" disabled="" value="${propValueStr?html}" title="${Context.getMessage('tooltip.pageList.edit.line.readonly.field')} (${tooltip})" />
