<table id="sortArray" class="zebra-striped bs">
  <thead>
    <tr>
      <#list bean.headersSet as header>
        <th class="header headerSortDown" ${This.getLineStyle(header)} >${header.name}</th>
      </#list>
      <#if This.isCommantableLines()>
	      <!-- Commentaires -->
	      <th class="header">&nbsp;</th>
	     </#if>
    </tr>
  </thead>
  <tbody>
    <#list bean.entriesLines as entriesLine>
      <tr>
        <#list bean.headersSet as header>
          <td ${This.getLineStyle(header)} ${This.getLineOnclick(entriesLine)}>
            <#assign entry = entriesLine.getEntryByIdHead(header.idHeader) />
            <#if entry != null>
              <#include "/views/PageList/" + header.type?lower_case + "/display.ftl" />
            </#if>
          </td>
        </#list>
      	<#if This.isCommantableLines()>
     		<!-- Commentaires -->
      		<td class="labscomments display" rel="twipsy" data-original-title="${entriesLine.nbComments}" onClick="javascript:openComments('${This.path}/${entriesLine.docLine.name}/@labscomments/');" alt="${Context.getMessage('label.comments.title')}"></td>
      	</#if>
      </tr>
    </#list>
  </tbody>
</table>
<#include "/views/LabsComments/displayModalComments.ftl" />
<#if 0 < bean.entriesLines?size>
  <script type="text/javascript">
    $("table#sortArray").tablesorter({
        textExtraction: function(node) {
              // extract data from markup and return it
            var sortValues = jQuery(node).find('span[class=sortValue]');
            if (sortValues.length > 0) {
              return sortValues[0].innerHTML;
            }
                return node.innerHTML;
        }
      });
  </script>
</#if>
<#if This.isCommantableLines()>
<script type="text/javascript">
	$(function () {
		$("td[rel=twipsy]")
		.twipsy({live: true})
		}
	) 
</script>
</#if>
