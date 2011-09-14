<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
    
    <@block name="scripts">
	  	<@superBlock/>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
	</@block>
    
    <@block name="css">
	  	<@superBlock/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_blocs.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
		<link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	</@block>
	
	
  <@block name="content">
  
    <div id="content" class="pageBlocs">
      <#-- COMMENT AREA --> 
      <#include "views/common/comment_area.ftl" />
      
          <div class="blocTitle">
          ${Document.dublincore.description}
          </div>
          <div class="blocContent">
            <ul>
          <#list Session.getChildren(This.document.ref) as child>
              <li><a href="${This.path}/${child.name}">${child.title}</a></li>
          </#list>
            </ul>
          </div>
    </div>
  </@block>
</@extends>	