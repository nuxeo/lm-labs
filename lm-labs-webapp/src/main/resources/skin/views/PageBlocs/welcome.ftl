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
	</@block>
	
	
  <@block name="content">
      <#-- SIDEBAR AREA --> 
      <#include "views/common/sidebar_area.ftl" />
    <div id="content" class="pageBlocs welcome">
      
      <#-- COMMENT AREA --> 
      <#include "views/common/comment_area.ftl" />
      
      <div id="blocs" >
      <#list This.children as root>
        <#if root.name != "welcome">
        <div id="bloc${root_index}" class="bloc">
          <div class="blocTitle welcome">
          <a href="${This.previous.path}/${root.name}">${root.title}</a>
          </div>
          <div class="blocContent">
            <ul>
          <#list Session.getChildren(root.ref) as child>
              <li><a href="${This.previous.path}/${root.name}/${child.name}">${child.title}</a></li>
          </#list>
            </ul>
          </div>
        </div>
        </#if>
      </#list>
      <#-- FIXME -->
        <div id="bloc_test" class="bloc">
          <div class="blocTitle welcome">
        MANUTEO
          </div>
          <div class="blocContent">
          Catalogue en ligne des engins et supports de manutention
          </div>
      </div>
    </div>
  </@block>
</@extends>	