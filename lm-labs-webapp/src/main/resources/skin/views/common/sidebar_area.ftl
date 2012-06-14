    <#-- NOTIFICATION AREA --> 
    <@block name="displayLogo">
		<#assign theme=Common.siteDoc(Document).getSite().themeManager.getTheme(Context.coreSession) />
		<#if theme.logo != null>
			<div style="height:${theme.logoAreaHeight}px;">&nbsp;</div>
		</#if>
    </@block>
    
    <#-- NOTIFICATION AREA --> 
    <@block name="notification">
    	<#include "views/common/notification_area.ftl" />
    </@block>
    <div style="margin-bottom:10px;"></div><#-- TEMPORAIRE : attente refonte complète graphique -->
    <#-- SITEMAP AREA --> 
    <@block name="siteMap">
    	<#include "views/common/sitemap_area.ftl" />
    	<div style="height: 10px;">&nbsp;</div>
    </@block>
    <#-- TOP NAVIGATION --> 
    <@block name="topPage">
    	<#include "views/common/toppages_area.ftl" />
    </@block>
    <#-- SUB PAGE --> 
    <@block name="subPages">
    	<#include "views/common/children_area.ftl" />
    </@block>
    <#-- LAST MESSAGE AREA --> 
    <@block name="lastActivities">
    	<#include "views/common/last_message_area.ftl" />
    </@block>
    <#-- EXTERNAL URL AREA --> 
    <@block name="externalURL">
    	<#include "views/common/external_url_area.ftl" />
    </@block>
    <#-- LATEST UPLOADS AREA --> 
    <@block name="lastUploads">
    	<#include "views/common/latestuploads_area.ftl" />
    </@block>
    