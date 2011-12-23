<#if entry.date != null>
	${entry.date.time?string(formatDate)}<span class="sortValue">${entry.date.time?string('yyyyMMddhhmmss')}</span>
</#if>