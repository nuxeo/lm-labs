<#include "macros/widgets.ftl" />
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
    
    <#assign sidebar=Common.siteDoc(Document).getSite().getSidebar() />
    <#assign nbrOsGadgetsSidebar = 0 />
    <#list sidebar.section(0).rows as row>
    	<#assign isWidgetCol = false />
          <#assign isOsGadgetCol = false />
          <#assign widgets = [] />
          <#assign content = row.content(0) />
          <@determineWidgetType content=content />
          <#if isOsGadgetCol >
            <div id="sidebar_gadgetCol-s_0_r_${row_index}_c_0">
            <#assign nbrOsGadgetsSidebar = nbrOsGadgetsSidebar + 1 />
                <script type="text/javascript">
                	userPrefsTab['${widgets[0].doc.id}'] = eval ( '(${Common.getUserPrefsFormatJS(widgets[0].userPrefs)?js_string})' );
                </script>
            
            <div id="${widgets[0].doc.id}" class="opensocialGadgets gadget-${widgets[0].name} bloc"
            	data-gadget-specurl="${widgets[0].specUrl}"
				data-gadget-title="${widgets[0].name}"
            >
            </div>
            </div>
          <#elseif isWidgetCol >
            <#--<div class="span<#if maxSpanSize != content.colNumber >${content.colNumber}</#if> columns" >-->
        	<@displayContentHtmlWidget widget=widgets[0] widgetMode="view" sectionIdx=0 rowIdx=row_index columnIdx=0 content=content />
            <#--</div>-->
          <#else>
            <div class="bloc">
               <@displayContentHtml content=content />
            </div>
          </#if>
    </#list>
    
    <script type="text/javascript" src="${contextPath}/wro/labs.pagehtml.bottom.js"></script>
  	<script type="text/javascript" src="${skinPath}/js/externalLinksHelper.js"></script>
    <#if 0 < nbrOsGadgetsSidebar >
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.pack.js"></script>
	    <script type="text/javascript" src="${contextPath}/opensocial/gadgets/js/rpc.js?c=1"></script>
	    <#--<script type="text/javascript" language="javascript" src="${contextPath}/opensocial/gadgets/js/rpc:pubsub:lmselectvalue.js?c=1"></script>-->
	    <script type="text/javascript" src="${skinPath}/js/register_rpc_show_fancybox.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/register_rpc_navigateto.js"></script>
	    <script type="text/javascript" src="${contextPath}/js/?scripts=opensocial/cookies.js|opensocial/util.js|opensocial/gadgets.js|opensocial/cookiebaseduserprefstore.js|opensocial/jquery.opensocial.gadget.js"></script>
    </#if>
    
    <#-- TOP NAVIGATION 
    <@block name="topPage">
    	<#include "views/common/toppages_area.ftl" />
    </@block>--> 
    <#-- SUB PAGE 
    <@block name="subPages">
    	<#include "views/common/children_area.ftl" />
    </@block>--> 
    <#-- LAST MESSAGE AREA 
    <@block name="lastActivities">
    	<#include "views/common/last_message_area.ftl" />
    </@block>--> 
    <#-- EXTERNAL URL AREA 
    <@block name="externalURL">
    	<#include "views/common/external_url_area.ftl" />
    </@block>--> 
    <#-- LATEST UPLOADS AREA 
    <@block name="lastUploads">
    	<#include "views/common/latestuploads_area.ftl" />
    </@block>--> 
    