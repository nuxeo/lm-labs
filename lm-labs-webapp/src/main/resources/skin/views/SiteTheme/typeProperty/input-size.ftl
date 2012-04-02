<select name="valueProperty${cptProperties}" id="valueProperty${cptProperties}">
	<option value="" <#if ("" == property.value) >selected</#if>>Par défaut</option>
    <#assign fontSizes = Common.getFontSizes() />
    <#list fontSizes as fontSize>
		<option value="${fontSize.size}" <#if (fontSize.size == property.value) >selected</#if>>${fontSize.displayName}</option>
	</#list>
</select>