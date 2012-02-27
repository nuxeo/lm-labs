<table id="sortArray" class="table table-striped table-bordered bs arrayPageList">
  <thead>
    <tr>
      <#list bean.headersSet as header>
        <th class="header headerSortDown" ${This.getLineStyle(header)} >${header.name}</th>
      </#list>
      <#if This.isCommantableLines()>
	      <th class="header" style="width: 15px;">&nbsp;</th>
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
              <#assign formatDate = header.formatDate />
              <#include "/views/PageList/" + header.type?lower_case + "/display.ftl" />
            </#if>
          </td>
        </#list>
      	<#if This.isCommantableLines()>
     		<!--  --------------------COMMENTS OF LINE --------------->
     		<#assign nbComments = entriesLine.nbComments/>
     		<#if 0 < nbComments>
      			<td style="vertical-align: middle;width: 15px;" rel="tooltip" data-original-title="${Context.getMessage('label.comments.nbComments', nbComments)}"  alt="${Context.getMessage('label.comments.title')}">
      				<a href="#" class="open-dialog" rel="divCommentable" onClick="javascript:openComments('${This.path}/${entriesLine.docLine.name}/@labscomments/');"><img src="${skinPath}/images/comments.png" /></a>
      			</td>
      		<#else>
      			<td style="vertical-align: middle;width: 15px;" rel="tooltip" data-original-title="${Context.getMessage('label.comments.null.nbComments')}"  alt="${Context.getMessage('label.comments.title')}">
      				<a href="#" class="labscomments noComments open-dialog" rel="divCommentable" onClick="javascript:openComments('${This.path}/${entriesLine.docLine.name}/@labscomments/');"><img src="${skinPath}/images/comments.png" /></a>
      			</td>
      		</#if>
      	</#if>
      </tr>
    </#list>
  </tbody>
</table>
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
		$("td[rel=tooltip]").tooltip({live: true})
		}
	) 
</script>
</#if>
<!--  --------------------COMMENTS OF LINE --------------->
<#include "/views/LabsComments/displayCommentsPopup.ftl" />
