<@extends src="views/LabsSite/sitemap-base.ftl">
     <@block name="scripts">
      <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
  jQuery("table[class*='table-striped']").tablesorter({
      sortList: [[1,0]],
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

     <@block name="sitemap-content">
        <table class="table table-striped bs">
            <thead>
            <tr>
              <th width="30%">Elément</th>
              <th>Créé par</th>
              <th>le</th>
              <th>Dernière mise à jour par</th>
              <th>le</th>
            </tr>
          </thead>
          <tbody>
          <#list site.allPages as page>
            <#assign isAdmin = site.isAdministrator(Context.principal.name) />
            <#if isAdmin || (!site.isAdministrator(Context.principal.name) && page.visible) > 
            <tr>
              <#assign doc=page.document />
              <td class="nameCol"><a href="${Context.modulePath}/${page.path}">${doc.title}</a></td>
              <td class="createdCol">${userFullName(doc.dublincore.creator)}</td>
              <td class="createdCol">
                ${doc.dublincore.created?string.medium}
                <span class="sortValue">${doc.dublincore.created?string("yyyyMMddHHmmss")}</span>
              </td>
              <#assign modified=doc.dublincore.modified/>
              <td class="updatedCol">${userFullName(doc.dublincore.lastContributor)}</td>
              <td class="updatedCol">
                ${modified?string.medium}
                <span class="sortValue">${modified?string("yyyyMMddHHmmss")}</span>
              </td>
            </tr>
            </#if>
          </#list>
          </tbody>
      </table>

  </@block>
</@extends>