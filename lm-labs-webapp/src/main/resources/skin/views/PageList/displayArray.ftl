<#assign entriesLines = bean.entriesLines />
<table id="sortArray" class="table labstable table-striped table-bordered bs arrayPageList">
  <thead>
    <tr>
      <#list bean.headersSet as header>
        <th <#if entriesLines?size == 0 || header.type?lower_case == 'files' >class="header noSort" </#if>style="${This.getLineStyle(header)}" >${header.name}</th>
      </#list>
      <#if This.commentableLines>
	    <th class="header noSort" style="width: 15px;">&nbsp;</th>
	  </#if>
    </tr>
  </thead>
  <tbody>
    <#list entriesLines as entriesLine>
      <#assign lineOnclick = This.getLineOnclick(entriesLine) />
      <tr>
        <#list bean.headersSet as header>
          <td style="${This.getLineStyle(header, entriesLine)}<#if header.type?lower_case == 'files'><#include "/views/PageList/" + header.type?lower_case + "/td-style.ftl" /></#if>"
            <#if header.type?lower_case != 'files'>${lineOnclick} </#if>
            <#if header.type?lower_case == 'files'><#include "/views/PageList/" + header.type?lower_case + "/td-attributes.ftl" /></#if>>
            <#assign entry = entriesLine.getEntryByIdHead(header.idHeader) />
            <#if entry != null>
              <#assign formatDate = header.formatDate />
              <#include "/views/PageList/" + header.type?lower_case + "/display.ftl" />
            </#if>
          </td>
        </#list>
      	<#if This.commentableLines >
     		<#--  --------------------COMMENTS OF LINE --------------->
     		<#assign nbComments = entriesLine.nbComments/>
     		<#assign commentLabel = Context.getMessage('label.comments.nbComments', nbComments) />
     		<#if nbComments == 0 >
                <#assign commentLabel = Context.getMessage('label.comments.null.nbComments') />
     		</#if>
  			<td style="vertical-align: middle;width: 15px;" rel="tooltip" data-original-title="${commentLabel}" alt="${Context.getMessage('label.comments.title')}">
  				<a href="#" class="btn btn-mini <#if nbComments == 0 >labscomments noComments</#if> open-dialog" rel="divCommentable" 
  				  onClick="javascript:openComments('${This.path}/${entriesLine.docLine.name}/@labscomments/');">
  				  <i class="icon-comments" ></i>
			  </a>
  			</td>
      	</#if>
      </tr>
    </#list>
  </tbody>
</table>
<#if This.allContributors >
<div id="lineFiles">
    <img src="${skinPath}/images/loading.gif" />
</div>
</#if>
<#if 0 < entriesLines?size>
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
<script type="text/javascript">
jQuery(function () {
	jQuery("td[rel=tooltip]").tooltip({live: true})
	}
)
jQuery(document).ready(function () {
<#if This.allContributors >
    jQuery("#lineFiles").dialog2({
        title: '${Context.getMessage('label.PageList.line.files.form.title')}',
        closeOnOverlayClick: false,
        buttons: {
            '${Context.getMessage('command.PageList.line.files.form.close')}': { 
                primary: false, 
                click: function() {
                    <#-- TODO
                    var lineName = jQuery('#linename').val();
                    var nbrFiles = jQuery('#lineNbrFiles').val();
                    -->
                    jQuery(this).dialog2("close");
                    jQuery(this).html('');
                    <#-- TODO
                    //jQuery('#sortArray tr.' + lineName + ' > td > a[rel=lineFiles]').closest('td').attr('data-original-title', nbrFiles);
                    -->
                }
            }
        },
        autoOpen: false
    });
    jQuery('table.arrayPageList').on('click', 'a.openLineFiles', function() {
        var lineName = jQuery(this).siblings('input[type=hidden]').val();
        jQuery("#lineFiles").load('${This.path}/' + lineName + '/@views/files?linename=' + lineName);
    });
</#if>
});
</script>
<#--  --------------------COMMENTS OF LINE --------------->
<#include "/views/LabsComments/displayCommentsPopup.ftl" />
