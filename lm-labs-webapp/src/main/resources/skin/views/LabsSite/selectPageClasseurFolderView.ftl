<@extends src="/views/labs-common-base.ftl">
  <#assign mySite=Common.siteDoc(Document).getSite() />

  <@block name="title">${mySite.title}-${This.document.title}-Sélection Répertoire de Page Classeur</@block>

  <@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/wro/labs.common.css" />
	<link rel="stylesheet" type="text/css" href="${Context.modulePath}/${mySite.URL}/@currenttheme/rendercss" />
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
  </@block>

  <@block name="scripts">
    <link rel="stylesheet" type="text/css" href="${contextPath}/wro/labs.common.css" />
	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
   	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
   	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
   	<script type="text/javascript" src="${contextPath}/opensocial/gadgets/js/lmselectvalue.js?c=0&debug=1"></script>
    <@superBlock/>

    
  </@block>

  <@block name="topbar" />

  <div id="FKtopContent">
  <@block name="FKtopContent">
    <div class="container-fluid">
      <div class="sidebar">
      </div>
      <div class="content">
        <div class="row">
          <div class="span11 columns well"
    <#include "views/LabsSite/selectPageClasseur.ftl" />
        </div>
      </div>
    </div>
  </@block>
  </div>

  <@block name="FKfooter" />

  <@block name="bottom-page-js" />

</@extends>
