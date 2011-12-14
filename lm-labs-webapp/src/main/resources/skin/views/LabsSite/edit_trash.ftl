<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">

<@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
		  jQuery("table[class*='zebra-striped']").tablesorter({
		      sortList: [[4,0]],
		      textExtraction: function(node) {
		            // extract data from markup and return it
		        var sortValues = jQuery(node).find('span[class=sortValue]');
		        if (sortValues.length > 0) {
		          return sortValues[0].innerHTML;
		        }
		            return node.innerHTML;
		        }
		  });
		});
	</script>
  </@block>
  
<@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
 </@block>

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
  </@block>


  <@block name="content">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="trash"/>
	<div class="container">
      <section>
        <#assign deletedDocs = This.getDeletedDocs() />
        <div class="page-header">
          <h3>
          	${Context.getMessage('label.lifeCycle.trash.title')} <#if (deletedDocs?size > 0)>(${deletedDocs?size})</#if>
          	<div style="text-align: right;margin-top: -37px;">
				<button onClick="beEmptyTrash();" title="${Context.getMessage('label.lifeCycle.page.emptyTrash')}" class="btn" style="margin-left:20px;" >${Context.getMessage('label.lifeCycle.page.emptyTrash')}</button>
			</div>
          </h3>
        </div>
        <#if (deletedDocs?size > 0)>
	        <table class="zebra-striped bs">
	            <thead>
	            <tr>
	              <th>${Context.getMessage('label.lifeCycle.trash.page')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.createdBy')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.lastModifiedBy')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
	              <th>&nbsp;</th>
	            </tr>
	          </thead>
	          <tbody>
	          <#list deletedDocs as doc>
	            <tr>
	              <td>
	              	<a href="#" rel="popover" data-content='${doc.dublincore.description}' data-original-title="${Context.getMessage('label.description')}">${doc.dublincore.title} (${doc.type})</a>
	              </td>
	              <td>${userFullName(doc.dublincore.creator)}</td>
	              <td>
	                ${doc.dublincore.created?string.medium}
	                <span class="sortValue">${doc.dublincore.created?string("yyyyMMddHHmmss")}</span>
	              </td>
	              <#assign modified=doc.dublincore.modified/>
	              <td>${userFullName(doc.dublincore.lastContributor)}</td>
	              <td>
	                ${modified?string.medium}
	                <span class="sortValue">${modified?string("yyyyMMddHHmmss")}</span>
	              </td>
	              <td>
	              	<a  href="#" class="btn" onclick="javascript:undeletePage('${doc.ref}');">${Context.getMessage('command.docactions.undelete')}</a>
	              </td>
	            </tr>
	          </#list>
	          </tbody>
	        </table>
	      </#if>
      </section>
    </div>
    <script type="text/javascript">
		function undeletePage(ref){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouUndelete')}")){
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: '${This.path}/@labspublish/undelete/' + ref,
				    success: function(data) {
				    	if (data == 'undelete') {
				          alert("${Context.getMessage('label.lifeCycle.page.hasUndeleted')}");
				          document.location.href = '${This.path}/@views/edit_trash';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotUndeleted')}");
				        }
				    }
				});
			}
		}
		function beEmptyTrash(){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouBeEmptyTrash')}")){
    			jQuery.ajax({
					type: 'DELETE',
				    async: false,
				    url: '${This.path}/@labspublish/emptyTrash',
				    success: function(data) {
				    	if (data == 'beEmpty') {
				          alert("${Context.getMessage('label.lifeCycle.page.beEmpty')}");
				          document.location.href = '${This.path}/@views/edit_trash';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.notBeEmpty')}");
				        }
				    }
				});
			}
		}
		
		jQuery(function () {
				jQuery("a[rel=popover]")
					.popover({offset: 10, html:true})
					.click(
						function(e) {e.preventDefault()}
					)
			}
		) 
	</script>
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
