<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
    
    <@block name="scripts">
	  	<@superBlock/>
	</@block>
    
    <@block name="css">
	  	<@superBlock/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_blocs.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/searchbox.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/sidebar.css"/>
	</@block>
	
	
  <@block name="content">
      <#-- SIDEBAR AREA --> 
      <div class="container-fluid">
      <div id="sidebar" class="sidebar">
      	<#include "views/common/sidebar_area.ftl" />
      </div>
      
      
      <div class="content">
      <#include "views/common/description_area.ftl">
	
	
		<div  class="span12 columns">
	  <#include "views/common/comment_area.ftl">
		</div>

      <#------------------------------------maxNbLabsNews------------------------> 
      <#assign maxNbLabsNews = 5 />
      <#list This.children as root>
        <#if root.name != "welcome">
        <div id="bloc${root_index}" class="bloc columns">
          <div class="blocTitle welcome">
          <a href="${This.previous.path}/${root.name}">${root.title}</a>
          </div>
          <div class="blocContent">
            <ul>
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
			              <li><a href="${This.previous.path}/${root.name}/${child.name}">${child.title}</a></li>
			            </#if>
			          </#list>
			    </#if>
            </ul>
          </div>
        </div> <!-- bloc -->
        </#if>
      </#list>

      </div> <!-- content -->
    </div> <!-- container-fluid -->
  </@block>
</@extends>	