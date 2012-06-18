<#assign errorValue = "N.D." />
<#if LabsUtils.validDocProperty(entry.text!"", entriesLine.docLine) >
    <#-- on devrait TOUJOURS rentrer ici -->
    <#attempt>
	    <#assign propValue = entriesLine.docLine[entry.text] />
    <#recover>
	    <#assign propValue = errorValue />
    </#attempt>
<#else>
	<#assign propValue = errorValue />
</#if>
${userFullName(propValue)?html}