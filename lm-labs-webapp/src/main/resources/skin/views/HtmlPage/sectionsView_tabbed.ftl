<#include "macros/HtmlPageSection.ftl" />
<#assign idsPrefix = "tabbed_" />
<div id="${idsPrefix}divSections" class="viewblock tabbable" >
<#if 1 < sections?size >
	<ul class="nav nav-tabs viewblock">
	<#list sections as section>
	<li<#if section_index == 0> class="active" </#if>><a href="#${idsPrefix}div_section_${section_index}" data-toggle="tab"><#if section.title?length == 0 >...<#else>${section.title}</#if></a></li>
	</#list>
</ul>
</#if>
	<div class="tab-content">
<#list sections as section>
	<@displayHtmlPageSection page=This.page section=section section_index=section_index viewMode="tabbed" idsPrefix=idsPrefix />
</#list>
	</div>
</div>