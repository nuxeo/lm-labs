<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/templates/labs-base.ftl">

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
        <#assign deletedPages = This.getDeletedPage() />
        <div class="page-header">
          <h3>${Context.getMessage('label.lifeCycle.trash.title')} <#if (deletedPages?? && deletedPages?size > 0)>(${deletedPages?size})</#if></h3>
        </div>
        <#if (deletedPages?? && deletedPages?size > 0)>
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
	          <#list deletedPages as deletedPage>
	            <tr>
	              <#assign doc=deletedPage.document />
	              <td>
	              	<a href="#" rel="popover" data-content="${deletedPage.description}" data-original-title="${Context.getMessage('label.description')}">${deletedPage.title} (${deletedPage.document.type})</a>
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
	              	<a  href="#" class="btn" onclick="javascript:undeletePage('${deletedPage.document.ref}');">${Context.getMessage('command.docactions.undelete')}</a>
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
				          document.location.href = '${This.path}';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotUndeleted')}");
				        }
				    }
				});
			}
		}
		
		$(function () {
				$("a[rel=popover]")
					.popover({offset: 10})
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
