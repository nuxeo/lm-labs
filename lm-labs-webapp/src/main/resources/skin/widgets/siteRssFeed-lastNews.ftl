<#include "macros/labsSiteRssFeedList.ftl" />
<#assign myDivId = "${widgetMode}_rss_feed_list_s" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<@labsSiteRssFeedList feed="lastNews" nbrItems="5" divId=myDivId />