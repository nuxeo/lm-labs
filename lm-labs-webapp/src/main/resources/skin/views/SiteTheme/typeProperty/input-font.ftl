<select name="valueProperty${cptProperties}" id="valueProperty${cptProperties}">
	<option value="" <#if ("" == property.value) >selected</#if>>Par défaut</option>
	<#assign fontFamilies = Common.getFontFamilies() />
	<#list fontFamilies as fontFamily>
		<option value="${fontFamily.cssName}" style="font-family: ${fontFamily.cssName};" <#if (fontFamily.cssName == property.value) >selected</#if>>${fontFamily.displayName}</option>
	</#list>
</select>