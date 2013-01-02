<#macro notificationButton notifType="Page" inBtnGroup=false doc=Document >
    <#assign i18nType = notifType />
    <#if notifType == "Page" && doc.type == "PageNews" >
        <#assign i18nType = doc.type />
    </#if>
    <#if Common.getNotifiableTypes()?seq_contains(doc.type) >
    	<#if notifType == "Site" >
    		<#assign subsUrl = Context.modulePath + "/" + Common.siteDoc(doc).site.URL />
    	<#else>
    		<#assign subsUrl = Context.modulePath + "/" + Common.siteDoc(doc).resourcePath />
    	</#if>
    	<#if Common.sitePage(doc).isSubscribed(Context.principal.name) >
    		<#assign confirmMsg = Context.getMessage('command.contextmenu.Site.subscribe.confirm') />
    	<#else>
    		<#assign confirmMsg = Context.getMessage('command.contextmenu.Site.unsubscribe.confirm') />
    	</#if>
        <a class="<#if !inBtnGroup>btn btn-primary </#if>subscribeBt${notifType}" 
            <#if Common.sitePage(doc).isSubscribed(Context.principal.name) >style="display:none;"</#if> href="#" 
            onclick="if ('${notifType}' !== 'Site' || confirm('${confirmMsg}')) {subscribePageUrl(true, '${subsUrl}', '${notifType}', subscribeErrorCallback, subscribeErrorCallback);jQuery(this).hide();jQuery(this).siblings('a.unsubscribeBt${notifType}').show();} else {return false;}"
            title="${Context.getMessage('tooltip.contextmenu.' + i18nType + '.subscribe')}" ><i class="icon-envelope"></i>
            ${Context.getMessage('command.contextmenu.' + i18nType + '.subscribe')}
        </a>
        <a class="<#if !inBtnGroup>btn btn-primary </#if>unsubscribeBt${notifType}" 
            <#if !Common.sitePage(doc).isSubscribed(Context.principal.name) >style="display:none;"</#if> href="#" 
            onclick="if ('${notifType}' !== 'Site' || confirm('${confirmMsg}')) {subscribePageUrl(false, '${subsUrl}', '${notifType}', subscribeErrorCallback, subscribeErrorCallback);jQuery(this).hide();jQuery(this).siblings('a.subscribeBt${notifType}').show();} else {return false;}"
            title="${Context.getMessage('tooltip.contextmenu.' + i18nType + '.unsubscribe')}" ><i class="icon-envelope"></i>
            ${Context.getMessage('command.contextmenu.' + i18nType + '.unsubscribe')}
        </a>
    </#if>
</#macro>