<#assign errorValue = "N.D." />
<#if LabsUtils.validDocProperty(entry.text!"", entriesLine.docLine) >
    <#-- on devrait TOUJOURS rentrer ici -->
    <#attempt>
	    <#assign propValue = entriesLine.docLine[entry.text] />
		<#if propValue != null>
	 	   <#assign propValueStr = propValue?string(formatDate) + '<span class="sortValue">' + propValue?string('yyyyMMddhhmmss') + "</span>" />
		<#else>
		    <#assign propValueStr = errorValue />
		</#if>
    <#recover>
	    <#assign propValueStr = errorValue />
    </#attempt>
<#else>
	<#assign propValueStr = errorValue />
</#if>
${propValueStr}