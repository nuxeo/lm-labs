<#macro generateCssClass row >
	<#if row??>
		<#assign listUserClass=row.userClass>
<#if row.cssClass??> ${row.cssClass}</#if><#if (listUserClass?size > 0)><#list listUserClass as userClass> ${userClass}</#list></#if>
	</#if>
</#macro>

<#macro generateInputCssClass row >
	<#assign listUserClass=row.userClass>
<#if (listUserClass?size > 0)><#list listUserClass as userClass>"${userClass}"<#if (listUserClass?last != userClass)>,</#if></#list></#if></#macro>

<#macro generateAvailableUserClass mapUserClass >
	<#assign keys = mapUserClass?keys>
<#if (keys?size > 0)><#list keys as key>{id:'${mapUserClass[key]}',text:'${key?js_string}'}<#if (keys?last != key)>,</#if></#list></#if></#macro>

<#macro displayContentHtml content >
    <#if content.html == "" >
    &nbsp;
    <#else>
        ${content.html}
    </#if>
</#macro>

<#macro displayRawSection section >
	<#assign maxSpanSize = 12 />
	<section>
	<h1>${section.title}</h1><h2> <small>${section.description}</small></h2>
	<#list section.rows as row >
		<div class="row" >
		<#list row.contents as content >
			<div class="span<#if maxSpanSize != content.colNumber >${content.colNumber}</#if> columns">
			<#if content.type == "html" >
				<@displayContentHtml content=content />
			</#if>
			</div>
		</#list>
		</div>
	</#list>
	</section>
</#macro>