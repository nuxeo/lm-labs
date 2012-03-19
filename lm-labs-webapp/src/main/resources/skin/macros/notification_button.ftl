<#macro notificationButton notifType="Page" inBtnGroup=false >
    <#assign i18nType = notifType />
    <#if notifType == "Page" && Document.type == "PageNews" >
        <#assign i18nType = Document.type />
    </#if>
    <#if Common.getNotifiableTypes()?seq_contains(Document.type) >
        <a class="<#if !inBtnGroup>btn btn-primary </#if>subscribeBt${notifType}" 
            <#if This.isSubscribed() >style="display:none;"</#if> href="#" 
            onclick="javascript:return subscribePage(true, '${notifType}');"
            title="${Context.getMessage('tooltip.contextmenu.' + i18nType + '.subscribe')}" ><i class="icon-envelope"></i>
            ${Context.getMessage('command.contextmenu.' + i18nType + '.subscribe')}
        </a>
        <a class="<#if !inBtnGroup>btn btn-primary </#if>unsubscribeBt${notifType}" 
            <#if !This.isSubscribed() >style="display:none;"</#if> href="#" 
            onclick="javascript:return subscribePage(false, '${notifType}');"
            title="${Context.getMessage('tooltip.contextmenu.' + i18nType + '.unsubscribe')}" ><i class="icon-envelope"></i>
            ${Context.getMessage('command.contextmenu.' + i18nType + '.unsubscribe')}
        </a>
    </#if>
</#macro>