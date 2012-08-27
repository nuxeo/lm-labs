<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />
<#if !mySite?? >
  <#assign mySite=Common.siteDoc(Document).site />
</#if>
<@extends src="/views/labs-common-base.ftl">

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" href="${Context.modulePath}/${mySite.URL}/@currenttheme/rendercss-${mySite.themeManager.getTheme(Session).document.dublincore.modified?string("yyyyMMddHHmmss")}" />
        <link href="${Context.modulePath}/${mySite.URL}/@labsrss/lastNews" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.lastNews.title')}" />
        <link href="${Context.modulePath}/${mySite.URL}/@labsrss/lastUpload" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.lastUpload.title')}" />
        <link href="${Context.modulePath}/${mySite.URL}/@labsrss/all" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.all.title')}" />
  </@block>

  <@block name="scripts">
      <#if canWrite>
    <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
      </#if>



        <#--  ckeditor_confi -->
    <script type="text/javascript">
      <#include "views/common/ckeditor_config.ftl" />
    </script>

    <#include "views/common/subscribe_js.ftl" />
  </@block>

  <@block name="topbar">
    <@superBlock/>
    <#if Context.principal.isAdministrator() == true>
      <a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
    </#if>
  </@block>

  <@block name="FKfooter">
      <div id="FKfooter">
          <#include "views/common/footer.ftl">
          <#include "views/common/labsPiwik.ftl">
          <#include "views/common/loading.ftl">

            <#include "/views/common/audioReader.ftl" />

      </div><#-- /FKfooter -->
      <div>&nbsp;</div>
  </@block>

</@extends>