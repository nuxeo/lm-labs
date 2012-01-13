<select name="valueProperty${cptProperties}" id="valueProperty${cptProperties}">
	<option value="" <#if ("" == property.value) >selected</#if>>Par défaut</option>
	<#list Common.getFontFamilies() as font>
		<option value="${font.getCssName()}" style="font-family: ${font.getCssName()};" <#if (font.getCssName() == property.value) >selected</#if>>${font.getDisplayName()}</option>
	</#list>
</select>