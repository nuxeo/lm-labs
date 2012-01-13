<select name="valueProperty${cptProperties}" id="valueProperty${cptProperties}">
	<option value="" <#if ("" == property.value) >selected</#if>>Par défaut</option>
	<#list Common.getFontSizes() as size>
		<option value="${size.getSize()}" <#if (size.getSize() == property.value) >selected</#if>>${size.getSize()}</option>
	</#list>
</select>