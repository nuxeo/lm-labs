<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
    
    <@block name="scripts">
	  	<@superBlock/>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
	</@block>
    
    <@block name="css">
	  	<@superBlock/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_blocs.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
		<link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/searchbox.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/sidebar.css"/>
	    <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	</@block>
	
	
  <@block name="content">
      <#-- SIDEBAR AREA --> 
      <div id="container-fluid">
      <div class="sidebar">
      	<#include "views/common/sidebar_area.ftl" />
      </div>
      
      
      <div class="content">
      <#include "views/common/description_area.ftl">
	
	  <#include "views/common/comment_area.ftl">
      

      <div id="blocs" >
      <#------------------------------------maxNbLabsNews------------------------> 
      <#assign maxNbLabsNews = 5 />
      <#list This.children as root>
        <#if root.name != "welcome">
        <div id="bloc${root_index}" class="bloc">
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
        </div>
        </#if>
      </#list>

      </div>
      </div>
    </div>
  </@block>
</@extends>	