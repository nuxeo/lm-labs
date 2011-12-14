<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="title">${site.title}-${This.document.title}</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageList.css"/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
	  	
		<script type="text/javascript" src="${skinPath}/js/Collection.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageListHeaders.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageList.js"></script>
		
		<script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
		
	</@block>
	
	<@block name="docactions">
		<@superBlock/>
		<#include "views/common/page_actions.ftl" />
	</@block>
	
	<@block name="content">	
		<div id="content" class="container">
  <div class="page-header">
    <h1><span title="${Document.dublincore.description}" >${Document.dublincore.title}</span></h1>
  </div>
			<#if isAuthorized>
				<div id="divActionManageList">
					<a href="#" class="btn open-dialog" rel="divEditLine" onClick="javascript:addLine();">${Context.getMessage('label.pageList.addLine')}</a>
					<#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')>
						<a href="#" class="editblock open-dialog" rel="divManageList" onClick="javascript:manageList();">${Context.getMessage('label.pageList.manageList')}</a>
					</#if>
				</div>
			</#if>
			<div class="container">
			<#assign bean = This.getFreemarkerBean() />
			<#if isAuthorized>
				<div id="divManageList" class="dialog2" style="display: none;">
					<#include "/views/PageList/editHeaders.ftl" />
				</div>
				<div id="divEditLine" class="dialog2" style="display: none;">
					<h1>${Context.getMessage('label.pageList.edit.line.title')}</h1>
					<#include "/views/PageList/editLine.ftl" />
					<div id="divActionsLine"  class="actions">
						<button id="saveLine" class="btn primary" onClick="javascript:saveLine('${This.path}');" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
						<button id="cancelLine" class="btn" onClick="javascript:closeEditLine('${This.path}');" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
						<!--<button id="deleteLine" class="btn" onClick="javascript:if(confirm('${Context.getMessage('label.pageList.line_deleted.confirm')?js_string}')){deleteLine('${This.path}');}{return false;}" title="${Context.getMessage('label.pageList.edit.manage.delete')}">${Context.getMessage('label.pageList.edit.manage.delete')}</button>
						<button id="StructureJsLine" class="btn info" onClick="javascript:alert(linesCollection.toString());" >line structure</button>-->
					</div>
				</div>
			</#if>
			<div id="divDisplayArray" class="container">
				<#include "/views/PageList/displayArray.ftl" />
				<#if 0 < bean.entriesLines?size>
					<div class="container" style="text-align: right;">
						<a href="${This.path}/exportExcel/export.xls"><img title="export excel" alt="export excel" src="/nuxeo/icons/xls.png" /></a>
					</div>
				</#if>
			</div>
			<script type="text/javascript">
				var msg_title = "${Context.getMessage('label.pageList.edit.manage.title')}";
				var title_add_line = "${Context.getMessage('label.pageList.edit.line.title.add')}";
				var title_modify_line = "${Context.getMessage('label.pageList.edit.line.title.modify')}";
				var label_newsHeader = "${Context.getMessage('label.pageList.edit.manage.newsHeader')}";
				var label_options = "${Context.getMessage('label.pageList.edit.editHeader.options')}";
				var label_delete_error = "${Context.getMessage('label.pageList.edit.manage.delete.error')}";
				var is_new = true;
				var allContributors = false;
				<#if This.allContributors >
				 	allContributors = true;
				</#if>
				var commentableLines = false;
				<#if This.commentableLines >
				 	commentableLines = true;
				</#if>
				
				
				var headersMapBase = '(${bean.headersMapJS?js_string})';
				var headersNameBase = '${bean.headersNameJS?js_string}';
				
				<#if 2 < bean.headersMapJS?length >
					headersCollection.setCollection(eval( headersMapBase ), eval( '[' + headersNameBase  + ']'));
					is_new = false;
				<#else>
					headersCollection.setCollection({}, []);
					addOneHeader();
				</#if>
			</script>
		</div>
		<hr />
	</@block>
</@extends>		