<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

  <@block name="css">
      <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>


  <@block name="content">
      <#-- SIDEBAR AREA -->
      <div class="container-fluid">

      <div id="sidebar" class="sidebar">
        <#include "views/common/sidebar_area.ftl" />
      </div>


      <div class="content">

        <div class="row">
          <div  class="span12 columns">
            <#assign Document = site.indexDocument />
            <#include "views/common/description_area.ftl">
              <#include "views/common/comment_area.ftl">

      <#assign Document = site.document />

        <#------------------------------------maxNbLabsNews------------------------>
        <#assign maxNbLabsNews = 5 />

          <#list Session.getChildren(site.tree.ref) as root>
          	<#assign page = This.getPage(root)/>
            <#if root.name != "welcome" && page?? && page.isAuthorized(Context.principal.name, Context.principal.anonymous)>
	            <div id="bloc${root_index}" class="bloc welcome span6 column">
	              <div class="header">
	                <a href="${This.path}/${root.name}">${root.title}</a>
	              </div>
	
	              <ul class="unstyled">
	                <#if root.type == 'PageNews'>
	                  <#assign nbNews = 0 />
	                  <#list This.getNews(root.ref) as child>
	                    <#if nbNews < maxNbLabsNews >
	                      <li>${child.title}</li>
	                    </#if>
	                    <#assign nbNews = nbNews + 1 />
	                  </#list>
	                <#else>
	                  <#list Session.getChildren(root.ref) as child>
	                    <#if child.type == 'Folder'>
	                      <li>${child.title}</li>
	                    <#else>
	                      	<li><a href="${This.path}/${root.name}/${child.name}">${child.title}</a></li>
	                    </#if>
	                  </#list>
	                </#if>
	              </ul>
	            </div> <!-- bloc -->
            </#if>
          </#list>
          </div>
        </div> <!-- row -->
      </div> <!-- content -->
    </div> <!-- container-fluid -->
  </@block>
</@extends>