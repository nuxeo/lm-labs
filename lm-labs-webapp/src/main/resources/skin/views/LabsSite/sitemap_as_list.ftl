<@extends src="views/LabsSite/sitemap-base.ftl">
     <@block name="scripts">
	  	<@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("table[class*='zebra-striped']").tablesorter({ 
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
        <table class="zebra-striped">
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
            <tr>
              <#assign doc=page.document />
              <td class="nameCol"><a href="${This.path}${pageEndUrl(doc)}">${doc.title}</a></td>
              <td class="createdCol">${userFullName(doc.dublincore.creator)}</td><td class="createdCol">${doc.dublincore.created?string.medium}<span class="sortValue">${doc.dublincore.created?string("yyyyMMddHHmmss")}</span></td>
              <td class="updatedCol">${userFullName(doc.dublincore.lastContributor)}</td><td class="createdCol">${doc.dublincore.modified?string.medium}<span class="sortValue">${doc.dublincore.modified?string("yyyyMMddHHmmss")}</span></td>
            </tr>
          </#list>
          </tbody>
      </table>

  </@block>
</@extends>