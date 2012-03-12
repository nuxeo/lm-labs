<#macro notificationButton notifType="Page" inBtnGroup=false >
    <#if Common.getNotifiableTypes()?seq_contains(Document.type) >
        <a class="<#if !inBtnGroup>btn btn-primary </#if>subscribeBt${notifType}" <#if This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(true, '${notifType}');"><i class="icon-envelope"></i>
            <#if notifType == "Site">
                ${Context.getMessage('command.contextmenu.Site.subscribe')}
            <#else>
                <#if Document.type == "PageNews">
                     ${Context.getMessage('command.contextmenu.PageNews.subscribe')}
                <#else>
                    ${Context.getMessage('command.contextmenu.Page.subscribe')}
                </#if>
            </#if>
        </a>
        <a class="<#if !inBtnGroup>btn btn-primary </#if>unsubscribeBt${notifType}" <#if !This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(false, '${notifType}');"><i class="icon-envelope"></i>
            <#if notifType == "Site">
                ${Context.getMessage('command.contextmenu.Site.unsubscribe')}
            <#else>
                <#if Document.type == "PageNews">
                    ${Context.getMessage('command.contextmenu.PageNews.unsubscribe')}
                <#else>
                    ${Context.getMessage('command.contextmenu.Page.unsubscribe')}
                </#if>
            </#if>
        </a>
    </#if>
</#macro>