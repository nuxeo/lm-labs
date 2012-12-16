<#include "macros/labsSiteRssFeedList.ftl" />
<#assign nbrNews = content.html />
<#attempt>
    <#assign nbrNews = content.html?trim?number />
<#recover>
	<#assign nbrNews = 5 />
</#attempt>

<#assign myDivId = "${widgetMode}_rss_feed_list_s" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<@labsSiteRssFeedList feed="lastNews" nbrItems=nbrNews?string divId=myDivId />