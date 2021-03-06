<#assign mySite=Common.siteDoc(Document).getSite() />
<#if mySite?? && !Context.principal.anonymous>
<#include "macros/notification_button.ftl">
<#assign subscribedPages = mySite.subscribedPages />
<@extends src="/views/labs-admin-base.ftl">

  <@block name="title">${mySite.title}-${This.document.title} - ${Context.getMessage('title.LabsSite.subscriptions')}</@block>

<@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip.js"></script>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
    <script type="text/javascript">
jQuery(document).ready(function() {
  jQuery('.btn').attr('disabled', false);
<#if This.isSubscribed() || 0 < subscribedPages?size >
  jQuery("table[class*='table-striped']").tablesorter({
    headers: { 5: { sorter: false}},
      sortList: [[0,0]],
      textExtraction: function(node) {
            // extract data from markup and return it
        var sortValues = jQuery(node).find('span[class=sortValue]');
        if (sortValues.length > 0) {
          return sortValues[0].innerHTML;
        }
            return node.innerHTML;
        }
  });
</#if>
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

  <@block name="content">
  <div class="container">
    <section>
      <div class="page-header">
        <h1>${Context.getMessage('title.LabsSite.subscriptions')}</h1>
      </div>

  <div class="row">
  <div class="span12 columns">

      <div class="subscriptions">
        <table class="table table-striped table-bordered bs labstable">
          <thead>
            <tr>
              <th class="header">${Context.getMessage('label.lifeCycle.trash.page')}</th>
              <th class="header">${Context.getMessage('label.lifeCycle.trash.createdBy')}</th>
              <th class="header">${Context.getMessage('label.lifeCycle.trash.the')}</th>
              <th class="header">${Context.getMessage('label.lifeCycle.trash.lastModifiedBy')}</th>
              <th class="header">${Context.getMessage('label.lifeCycle.trash.the')}</th>
              <th class="header">&nbsp;</th>
            </tr>
          </thead>
          <tbody>
          <#if This.isSubscribed() >
            <tr>
              <td>Site</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </#if> 
          <#list subscribedPages as page >
            <#assign doc = page.document />
          <tr>
            <td>
              <a href="${Root.getLink(doc)}" rel="popover" data-trigger="hover" data-content="&lt;strong&gt;${Context.getMessage('label.labspath')?html}: &lt;/strong&gt;&lt;br&gt;${Root.getTruncatedLink(doc)}&lt;br&gt;&lt;strong&gt;${Context.getMessage('label.doctype')}: &lt;/strong&gt;&lt;br&gt;${doc.type}&lt;br&gt;&lt;strong&gt;${Context.getMessage('label.description')}: &lt;/strong&gt;&lt;br&gt;${doc['dc:description']?html}&lt;br&gt;"
              data-original-title="${Context.getMessage('label.properties')}">${doc['dc:title']}</a>
            </td>
            <td>${userFullName(doc['dc:creator'])}</td>
            <td>
              ${doc['dc:created']?string.medium}
              <span class="sortValue">${doc['dc:created']?string("yyyyMMddHHmmss")}</span>
            </td>
            <#assign modified = doc['dc:modified'] />
            <td>${userFullName(doc['dc:lastContributor'])}</td>
            <td>
              ${modified?string.medium}
              <span class="sortValue">${modified?string("yyyyMMddHHmmss")}</span>
            </td>
            <td>
              <@notificationButton notifType="Page" doc=doc />
              <#assign notified = page.lastNotified />
              <#if notified?? >
            <#-- TODO
	              <#assign motifiedStr = notified?datetime?string("yyyy-MM-dd HH:mm:ss zzzz") />
				???${motifiedStr}
              <span class="sortValue">${notified?datetime?string("yyyyMMddHHmmss")}</span>
            -->  
              </#if>
            </td>
          </tr>
          </#list>
          </tbody>
        </table>
        </div>
        </div>

      </div>
    </div>
    </section>
    </div>
  </@block>
</@extends>
</#if>