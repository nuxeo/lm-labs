<#if Context.principal.isAnonymous() == false>
<#include "macros/notification_button.ftl">
<#assign btnGroup = false />
<#if Document.type != "Site" && Common.siteDoc(Document).site.homePageRef == Document.id >
    <#assign btnGroup = true />
</#if>
<div id="notification"<#if btnGroup > class="btn-group"</#if> >
<#if btnGroup >
    <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><i class="icon-envelope"></i>Abonnement <span class="caret"></span></a>
    <ul class="dropdown-menu" >
        <li><@notificationButton inBtnGroup=true /></li>
        <li><@notificationButton notifType="Site" inBtnGroup=true /></li>
    </ul>
<#else>
    <#if Document.type != "Site" >
        <@notificationButton />
    </#if>
    <#if Common.siteDoc(Document).site.homePageRef == Document.id || Document.type == "Site" >
        <@notificationButton notifType="Site" />
    </#if>
</#if>
</div>
</#if>
