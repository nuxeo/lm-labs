<#assign lineFiles = This.files />
<input type="hidden" id="lineNbrFiles" value="${lineFiles?size}" />
<table class="table table-striped bs table-bordered labstable attached-files-table" style="margin-top:3px;">
  <thead>
      <tr>
          <th <#if lineFiles?size == 0 >class="header noSort" </#if>>${Context.getMessage('label.PageList.line.files.form.table.title.filename')}</th>
          <th <#if lineFiles?size == 0 >class="header noSort" </#if>>${Context.getMessage('label.PageList.line.files.form.table.title.filesize')}</th>
          <th class="header noSort">&nbsp;</th>
      </tr>
  </thead>
  <tbody>
  <#list lineFiles as file >
      <tr>
          <#assign blobLength = 0 >
          <#assign blob = This.getBlobHolder(file).blob >
          <#assign blobLength = blob.length >
          <td>${file.dublincore.title}</td>
          <td>${bytesFormat(blobLength, "K", "fr_FR")}<span class="sortValue">${blobLength?string.computer}</span></td>
          <td>
            <input type="hidden" value="${file.name}" />
            <#if Session.hasPermission(file.ref, 'ReadWrite')>
            <button class="btn btn-mini btn-danger delete" title="${Context.getMessage('tooltip.PageList.line.files.form.table.delete')}" ><i class="icon-remove" style="padding-right:0px;"></i></button>
            </#if>
            <a rel="nofollow" class="btn btn-mini download" href="${Root.getLink(file)}/@blob/" title="${Context.getMessage('tooltip.PageList.line.files.form.table.download')}" ><i class="icon-download" style="padding-right:0px;"></i></a>
          </td>
      </tr>
  </#list>
  </tbody>
</table>

<#if lineFiles?size &gt; 0 >
<script type="text/javascript">
jQuery("table.attached-files-table").ready(function() {
    jQuery("table.attached-files-table").tablesorter({
        headers: { 2: { sorter: false}},
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
});
</script>
</#if>
