<div>
<#if Session.hasPermission(Document.ref, 'AddChildren') >
<button class="btn" data-toggle="collapse" data-target="#list-line-input"><i class="icon-plus"></i>${Context.getMessage('command.PageList.line.files.form.add.open')}</button>
<div id="list-line-input" class="collapse" >
    <form class="ajax form-horizontal" method="post" action="${This.path}" enctype="multipart/form-data" accept-charset="utf-8" >
    <input type="hidden" name="doctype" value="File" />
    <input type="hidden" name="redirect" value="false" />
    <input name="file:content" type="file" class="input-file required" id="list-line-input-file" name="list-line-input-file" />
    <div class="actions" >
        <button style="float: right;" class="btn" id="list-line-add-file-btn"><i class="icon-ok"></i>${Context.getMessage('command.PageList.line.files.form.add')}</button>
    </div>
    </form>
</div>
</#if>
<table class="table table-striped bs table-bordered labstable attached-files-table">
  <thead>
      <tr>
          <th class="header">${Context.getMessage('label.PageList.line.files.form.table.title.filename')}</th>
          <th class="header">${Context.getMessage('label.PageList.line.files.form.table.title.filesize')}</th>
          <th>&nbsp;</th>
      </tr>
  </thead>
  <tbody>
  <#list This.files as file >
      <tr>
          <#assign blobLength = 0 >
          <#assign blob = This.getBlobHolder(file).blob >
          <#assign blobLength = blob.length >
          <td>${file.dublincore.title}</td>
          <td>${bytesFormat(blobLength, "K", "fr_FR")}<span class="sortValue">${blobLength?string.computer}</span></td>
          <td>
            <input type="hidden" value="${file.name}" />
            <#if Session.hasPermission(Document.ref, 'RemoveChildren') >
            <button class="btn btn-mini btn-danger delete" title="${Context.getMessage('tooltip.PageList.line.files.form.table.delete')}" ><i class="icon-remove" style="padding-right:0px;"></i></button>
            </#if>
            <a rel="nofollow" class="btn btn-mini download" href="${Root.getLink(file)}/@blob/" title="${Context.getMessage('tooltip.PageList.line.files.form.table.download')}" ><i class="icon-download" style="padding-right:0px;"></i></a>
          </td>
      </tr>
  </#list>
  </tbody>
</table>
</div>

<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery('#list-line-input > form').ajaxForm(function() { 
        jQuery("#lineFiles").load('${This.path}/@views/files');
    }); 
    jQuery('table.attached-files-table').on('click', 'button.delete', function() {
        var filename = jQuery(this).siblings('input[type=hidden]').val();
        if (confirm('${Context.getMessage('label.PageList.line.files.form.table.delete.confirm')}')) {
            jQuery(this).closest('td').html('<div class="progress progress-info progress-striped active" style="margin-bottom: 0px;"><div class="bar" style="width: 50%" ></div></div>');
            jQuery.ajax({
                type: 'DELETE',
                url: '${This.path}/' + filename,
                success: function(msg) {
                    jQuery("#lineFiles").load('${This.path}/@views/files');
                },
                error: function(msg) {
                    alert( msg.responseText );
                    jQuery('#waitingPopup').dialog2('close');
                }
            });
        }
    });
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
