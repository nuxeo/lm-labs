<#assign propValue = entriesLine.docLine.dublincore.creator />
<#if 0 < entry.text?length >
    <#-- on devrait TOUJOURS rentrer ici -->
    <#assign propValue = entriesLine.docLine[entry.text] />
</#if>
${userFullName(propValue)?html}