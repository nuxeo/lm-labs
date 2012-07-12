<#if Session.hasPermission(Document.ref, 'ReadWrite') >
    <input type="hidden" id="linename" value="${Context.request.getParameter('linename')}" />
    <button class="btn" data-toggle="collapse" data-target="#list-line-input"><i class="icon-plus"></i>${Context.getMessage('command.PageList.line.files.form.add.open')}</button>
    <div id="list-line-input" class="collapse" >
        <form id="list-line-input-form" class="form-horizontal" method="post" action="${This.path}" enctype="multipart/form-data" accept-charset="utf-8" >
        <input type="hidden" name="doctype" value="File" />
        <input name="file:content" type="file" class="input-file required" id="list-line-input-file" name="list-line-input-file" />
        <div class="actions" >
            <button style="float: right;" class="btn" id="list-line-add-file-btn" ><i class="icon-ok"></i>${Context.getMessage('command.PageList.line.files.form.add')}</button>
        </div>
        </form>
    </div>
</#if>
<div id="attached-files">
<#include "/views/PageListLine/files-table.ftl" />
</div>

<script type="text/javascript">
jQuery(document).ready(function () {
    function formBeforeSubmit() {
        jQuery('#list-line-add-file-btn').attr('disabled', '');
        return true;
    }
    function formAfterSubmit()  {
        jQuery('#list-line-add-file-btn').removeAttr('disabled');
        jQuery("#attached-files").load('${This.path}/@views/files-table');
    }
    jQuery('#list-line-input-form').ajaxForm({
        data: {ajax: 'true'},
        beforeSubmit:  formBeforeSubmit,
        success:       formAfterSubmit
    });
    jQuery('#attached-files').on('click', 'table.attached-files-table button.delete', function() {
        var filename = jQuery(this).siblings('input[type=hidden]').val();
        if (confirm('${Context.getMessage('label.PageList.line.files.form.table.delete.confirm')}')) {
            jQuery(this).closest('td').html('<div class="progress progress-info progress-striped active" style="margin-bottom: 0px;min-width: 40px;"><div class="bar" style="width: 50%" ></div></div>');
            jQuery.ajax({
                type: 'DELETE',
                url: '${This.path}/@file/' + filename + '/ajax',
                complete: function(jqXHR, textStatus) {
                    jQuery("#attached-files").load('${This.path}/@views/files-table');
                },
                error: function(msg) {
                    alert( msg.responseText );
                    //jQuery('#waitingPopup').dialog2('close');
                }
            });
        }
    });
});
</script>
