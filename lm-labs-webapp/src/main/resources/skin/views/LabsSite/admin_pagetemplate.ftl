<#assign mySite=Common.siteDoc(Document).getSite() />

<@extends src="/views/labs-admin-base.ftl">

<@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip.js"></script>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
	<script type="text/javascript">
jQuery(document).ready(function() {
  jQuery('.btn').attr('disabled', false);
  jQuery("table[class*='table-striped']").tablesorter({
  	headers: { 0: { sorter: false}, 6: { sorter: false}},
      sortList: [[5,0]],
      textExtraction: function(node) {
            // extract data from markup and return it
        var sortValues = jQuery(node).find('span[class=sortValue]');
        if (sortValues.length > 0) {
          return sortValues[0].innerHTML;
        }
            return node.innerHTML;
        }
  });
  jQuery('input[name="checkoptionsHeader"]').change(function() {
    var checkboxes = jQuery(this).closest('table').find('input[name="checkoptions"]');
    if (jQuery(this).is(':checked')) {
      jQuery(checkboxes).attr('checked','checked');
    } else {
      jQuery(checkboxes).removeAttr('checked');
    }
  });
		  
});
	</script>
  </@block>
  
<@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
 </@block>

  <@block name="docactions"></@block>


  <@block name="content">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="admin_elementPageTemplate"/>
	<div class="container">
      <section>
        <#assign pagesTemplate = mySite.getAllPagesTemplate() />
        <div class="page-header">
          <h3>
          	${Context.getMessage('label.admin.pagetemplate.title')} <#if (pagesTemplate?size > 0)><span class="badge badge-info" style="vertical-align: middle;" >${pagesTemplate?size}</span></#if>
          </h3>
        </div>
        <#if (pagesTemplate?size > 0)>
            <div id="pageTemplateActions" style="margin-bottom: 3px;" >
              <button class="btn" id="noTemplateSelection" onClick="processSelection(this, 'noTemplate');"><i class="icon-share-alt"></i>${Context.getMessage('command.template.notemplate.bulk')}</button>
            </div>
	        <table class="table table-striped table-bordered bs">
	            <thead>
	            <tr>
	              <th><input type="checkbox" name="checkoptionsHeader" title="${Context.getMessage('label.trash.checkbox')}"/></th>
	              <th>${Context.getMessage('label.lifeCycle.trash.page')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.createdBy')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.lastModifiedBy')}</th>
	              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
	              <th>&nbsp;</th>
	            </tr>
	          </thead>
	          <tbody>
	          <#list pagesTemplate as page>
	            <#assign doc = page.document />
	            <#assign url = Context.modulePath + "/" + Common.siteDoc(page.document).resourcePath />
	            <tr>
	              <td>
	                <input type="checkbox" name="checkoptions" value="${doc.id}" />
	              </td>
	              <td>
	              	<a href="${url}" rel="popover" data-trigger="hover" data-content="&lt;strong&gt;${Context.getMessage('label.labspath')?html}: &lt;/strong&gt;&lt;br&gt;${Root.getTruncatedLink(doc)}&lt;br&gt;&lt;strong&gt;${Context.getMessage('label.doctype')}: &lt;/strong&gt;&lt;br&gt;${doc.type}&lt;br&gt;&lt;strong&gt;${Context.getMessage('label.description')}: &lt;/strong&gt;&lt;br&gt;${doc.dublincore.description?html}&lt;br&gt;<#if page.hasElementPreview()>&lt;br&gt;&lt;img src='${url + "/@blob"}'/&gt;</#if>"
	              	  data-original-title="${Context.getMessage('label.properties')}"
	              	  onClick="stopEventPropagation(event); window.location.href = '${url?html}'; return false;"">${doc.dublincore.title}</a>
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
	              	<#assign docUrl = Root.getLink(doc) />
	              	<a  href="${docUrl}" class="btn" style="margin-bottom: 3px;" onclick="javascript:noTemplate(this);return false;"><i class="icon-share-alt"></i>${Context.getMessage('command.template.notemplate')}</a>
	              </td>
	            </tr>
	          </#list>
	          </tbody>
	        </table>
	        
	      </#if>
      </section>
    </div>
    <script type="text/javascript">

		function noTemplate(bt){
			var url = jQuery(bt).attr('href');
			if (confirm("${Context.getMessage('label.admin.pagetemplate.noTemplate.confirm')}")){
				jQuery('#waitingPopup').dialog2('open');
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: url + '/@noTemplate',
				    success: function(data) {
				    	if (data == 'ok') {
				          alert("${Context.getMessage('label.admin.pagetemplate.noTemplate.ok')}");
				          document.location.href = '${This.path}/@views/admin_pagetemplate';
				        }
				        else {
				          alert("${Context.getMessage('label.admin.pagetemplate.noTemplate.nok')}");
				          jQuery('#waitingPopup').dialog2('close');
				        }
				    },
				    error: function(data){
				    	jQuery('#waitingPopup').dialog2('close');
				    }
				});
			}
		}

		function processSelection(bt, action) {
			var n = jQuery('input[name="checkoptions"]:checked').length;
      		if (n <= 0) {
        		return false;
      		}
			if (action != "noTemplate") {
				return false;
			}
			var confirmMsg = '${Context.getMessage('label.admin.pageTemplate.noTemplate.bulk.confirm')?js_string}';
			var successMsg = '${Context.getMessage('label.admin.pageTemplate.noTemplate.bulk.success')?js_string}';
			var failureMsg = '${Context.getMessage('label.admin.pageTemplate.noTemplate.bulk.failed')?js_string}';
			if (confirm(confirmMsg)) {
				jQuery('#waitingPopup').dialog2('open');
				var url = '${This.path}/@bulkNoTemplate/' + '?';
				jQuery('input[name="checkoptions"]:checked').each(function(i) {
					url += "id=" + this.value;
					if (i < n-1) {
						url += "&";
					}
				});
				jQuery.ajax({
					url: url,
					type: 'PUT',
					complete: function(jqXHR, textStatus) {
						jQuery('input[name="checkoptions"]').attr('checked', false);
					},
					success: function (data, textStatus, jqXHR) {
						alert(successMsg);
						window.location.reload();
					},
					error: function(jqXHR, textStatus, errorThrown) {
						<#--alert(errorThrown + ": " + "," + jqXHR.responseText);-->
						alert(failureMsg);
						jQuery('#waitingPopup').dialog2('close');
					}
				});
			}
			return false;
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

