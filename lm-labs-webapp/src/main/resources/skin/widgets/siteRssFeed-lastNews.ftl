<#include "macros/labsSiteRssFeedList.ftl" />
<#assign nbrNews = content.html />
<#assign guid = "" />
<#attempt>
	<#if content.html?trim?contains("-on")>
		<#assign guid = "hasSummaryPicture" />
		<#assign nbrNews = content.html?trim?replace("-on", "")?number />
	<#elseif content.html?trim?contains("-undefined")>		
		<#assign nbrNews = content.html?trim?replace("-undefined", "")?number />
	<#else>
    	<#assign nbrNews = content.html?trim?number />
	</#if>
<#recover>
	<#assign nbrNews = 5 />
</#attempt>

<#assign myDivId = "${widgetMode}_rss_feed_list_s" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<@labsSiteRssFeedList feed="lastNews" nbrItems=nbrNews?string divId=myDivId guid=guid />