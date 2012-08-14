<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
<#assign bsMinified = ".min" />
<#assign popoverPlacement = "" />
    <head>
        <@block name="meta">
          <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        </@block>

        <title>
            <@block name="title">Labs</@block>
        </title>

        <@block name="css">
          <link rel="icon" type="image/x-icon" href="/nuxeo/img/logo.jpeg" />
          <link rel="shortcut icon"  type="image/x-icon" href="/nuxeo/img/logo.jpeg"/>
          <link rel="stylesheet" type="text/css" media="all" href="/nuxeo/wro/labs.common.css"/>
        </@block>



    <@block name="scripts">
      <script type="text/javascript" src="/nuxeo/wro/labs.common.js"></script>
        </@block>
    </head>
    <body>
    <#-- timeout -->
      <input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />

    <@block name="topbar">
      <#include "views/common/topbar.ftl" />
    </@block>

    <div id="FKtopContent" style="position: relative;">
      <@block name="FKtopContent" />
    </div><!-- /FKtopContent -->

    <div style="display:none;" class="nav_up" id="nav_up"></div>
    <div style="display:none;" class="nav_down" id="nav_down"></div>

    <@block name="FKfooter" />

    <@block name="bottom-page-js" >
      <#include "views/common/topbar_js.ftl" />
      <#if (mySite?? && mySite.isContributor(Context.principal.name) ) >
            <script type="text/javascript" src="${skinPath}/js/page_parameters.js"></script>
          </#if>
          <#if mySite?? && mySite.isContributor(Context.principal.name) && This.page?? && !(mySite.getHomePageRef() == This.page.document.id) >
            <script type="text/javascript" src="${skinPath}/js/setHomePage.js"></script>
          </#if>
    </@block>
    </body>
</html>