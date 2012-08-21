<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="title">${Common.siteDoc(Document).getSite().title}-${This.document.title}</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageList.css"/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
		<#include "views/common/datepicker_css.ftl">
	</@block>
	
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.ui.datepicker-fr.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.22.datepicker.slider.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/Collection.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageListHeaders.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageList.js"></script>
	</@block>
	
	<@block name="content">	
		<div id="content" class="container-fluid">
		
			<#include "views/common/page_header.ftl">
			<div id="divPageList" class="">
				<#include "views/common/paging.ftl" />
				<#assign nbrElemPerPage = This.page.elementsPerPage />
				<#assign bean = This.getFreemarkerBean() />
				<#assign pp = This.getPageListLinesPageProvider(nbrElemPerPage) />
				<#assign paramaterCurrentPage = Context.request.getParameter('page') />
      			<#assign currentPage = 0 />
				<#if paramaterCurrentPage?? && paramaterCurrentPage != null>
      				<#assign currentPage = paramaterCurrentPage?number?long />
      			</#if>
				<#assign entriesLines = This.getEntriesLines(pp.setCurrentPage(currentPage)) />
				<#if isAuthorized>
					<div id="divActionManageList">
						<#if Context.principal.isAnonymous() != true && (bean.headersSet?size > 0)>
							<a href="#" style="margin-bottom: 3px;margin-top: 3px;" class="btn open-dialog" rel="divEditLine" onClick="javascript:addLine(${pp.numberOfPages-1}, '${This.path}');"><i class="icon-plus"></i>${Context.getMessage('label.pageList.addLine')}</a>
						</#if>
						<#if This.page?? && This.page.isContributor(Context.principal.name)>
							<a href="#" id="PageList-editcolumns" class="editblock open-dialog" rel="divManageList" onClick="javascript:manageList();"><i class="icon-edit"></i>${Context.getMessage('label.pageList.manageList')}</a>
						</#if>
					</div>
				</#if>
				<#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')>
					<div id="divManageList" class="dialog2" style="display: none;">
						<#include "/views/PageList/editHeaders.ftl" />
					</div>
				</#if>
				<#if isAuthorized>
					<div id="divEditLine" class="dialog2" style="display: none;">
						<h1>${Context.getMessage('label.pageList.edit.line.title')}</h1>
						<div class="container" style="width: auto;">
							<form class="form-horizontal" method="post" name="form-editLine" id="form-editLine"  onSubmit="javascript:saveLine('${This.path}');return false;">
								<div id="contentFormLine"><#include "/views/PageList/editLine.ftl" /></div>
								<input type="hidden" id="pathFormEditLine" value="" />
								<input type="hidden" id="pathPageList" value="${This.path}" />
								<div id="divActionsLine"  class="actions">
									<button id="btnSaveLine" form-id="form-editLine" class="btn btn-primary required-fields" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
									<button id="btnCancelLine" class="btn" onClick="javascript:closeEditLine('${This.path}');return false" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
									<!--<button id="deleteLine" class="btn" onClick="javascript:if(confirm('${Context.getMessage('label.pageList.line_deleted.confirm')?js_string}')){deleteLine('${This.path}');}{return false;}" title="${Context.getMessage('label.pageList.edit.manage.delete')}">${Context.getMessage('label.pageList.edit.manage.delete')}</button>
									<button id="StructureJsLine" class="btn btn-info" onClick="javascript:alert(linesCollection.toString());" >line structure</button>-->
								</div>
							</form>
						</div>
					</div>
				</#if>
				<div id="divDisplayArray" class="">
					<#include "/views/PageList/displayArray.ftl" />
					<#if (0 < entriesLines?size)>
						<div class="container-fluid" style="text-align: right;">
							<a href="${This.path}/exportExcel/${This.document.title}.xls"><img title="export excel" alt="export excel" src="/nuxeo/icons/xls.png" /></a>
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
					
					$(document).ready(function() {
						$("div[rel=popoverEditLine]").each(function(i) {
							$(this).popover({html: false, placement: 'bottom'})
						});
					});
				</script>
				<div style="text-align : center;">
					<@paging pageProvider=pp url=This.path+"?page=" />
					<@resultsStatus pageProvider=pp />
				</div>
			</div>
		</div>
	</@block>
	<@block name="bottom-page-js" >
        <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-collapse.js"></script>
	</@block>
</@extends>		