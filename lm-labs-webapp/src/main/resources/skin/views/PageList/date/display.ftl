<#if entry.date != null>
	${entry.date.time?string('dd MMMMM yyyy')}<span class="sortValue">${entry.date.time?string('yyyyMMdd')}</span>
</#if>