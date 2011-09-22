<select name="${header.idHeader}" id="${header.idHeader}">
	<#list header.selectlist as option>
		<option value="${option}" <#if line != null && line.getEntryByIdHead(header.idHeader) != null && line.getEntryByIdHead(header.idHeader).text == option >selected</#if>>${option}</option>
	</#list>
</select>