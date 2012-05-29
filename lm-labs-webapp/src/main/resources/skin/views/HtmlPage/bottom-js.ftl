<#assign isContributor = This.page?? && This.page.isContributor(Context.principal.name, Context.coreSession) />
<#if isContributor >
<script type="text/javascript">
var rowWidgetsAtLoad = [];
jQuery(document).ready(function() {
    jQuery("#divConfigRowGadgets").dialog2({
        autoOpen : false,
        closeOnOverlayClick : false
    });
    jQuery("#divConfigGadget").dialog2({
        autoOpen : false,
        closeOnOverlayClick : false
    });
    jQuery('#config-row-gadgets-form').ajaxForm({
        data: {ajax: 'true'},
        beforeSubmit:  function() {
            jQuery('#divConfigRowGadgets div.actions a, input, button').attr('disabled', '');
            return true;
        },
        error: function() {
            jQuery('#divConfigRowGadgets').dialog2('close');
        },
        success: function() {
            jQuery('#divConfigRowGadgets div.actions a, input, button').removeAttr('disabled');
            jQuery('#waitingPopup').dialog2('open');
            <#--
            jQuery('#divConfigRowGadgets').dialog2('close');
            -->
            window.location.href = '${This.path}#row_s' + jQuery('#divConfigRowGadgets input[name=section]').val() + '_r' + jQuery('#divConfigRowGadgets input[name=row]').val();
            document.location.reload(true);
        }
    });
    jQuery('#config-gadget-form').ajaxForm({
        data: {ajax: 'true'},
        beforeSubmit:  function() {
            jQuery('#divConfigGadget').find('div.actions a, input, button').attr('disabled', '');
            return true;
        },
        error: function() {
            jQuery('#divConfigGadget').dialog2('close');
        },
        success: function() {
            jQuery('#divConfigGadget div.actions a, input, button').removeAttr('disabled');
            jQuery('#waitingPopup').dialog2('open');
            <#--
            jQuery('#divConfigGadget').dialog2('close');
            -->
            window.location.href = '${This.path}#row_s' + jQuery('#divConfigGadget input[name=section]').val() + '_r' + jQuery('#divConfigGadget input[name=row]').val();
            document.location.reload(true);
        }
    });
    jQuery('#config-row-gadgets-form-btn').click(function() {
        rowWidgetsAtSubmit = [];
        jQuery('#divConfigRowGadgets-content option:selected').each(function() {
            rowWidgetsAtSubmit.push(jQuery(this).val());
        });
        if (!(jQuery(rowWidgetsAtSubmit).not(rowWidgetsAtLoad).length == 0 && jQuery(rowWidgetsAtLoad).not(rowWidgetsAtSubmit).length == 0)) {
            <#-- lists are different -->
            if (confirm('${Context.getMessage('label.HtmlPage.row.widgets.config.save.confirm')}')) {
                jQuery('#waitingPopup').dialog2('open');
                jQuery('#' + jQuery(this).attr('form-id')).submit();
            } else {
                jQuery('#divConfigRowGadgets').dialog2('close');
                return false; <#-- prevents form submit -->
            }
        }
        jQuery('#divConfigRowGadgets').dialog2('close');
        return false; <#-- prevents form submit -->
    });
    jQuery('#config-gadget-form-btn').click(function() {
        var inputs = jQuery("#divConfigGadget form > fieldset").find("input[type=text],select,checkbox");
        if (inputs.length == 0) {
            jQuery('#divConfigGadget').dialog2('close');
            return false; <#-- prevents form submit -->
        }
        jQuery('#waitingPopup').dialog2('open');
        jQuery('#' + jQuery(this).attr('form-id')).submit();
    });
    jQuery('a[rel=divConfigRowGadgets]').click(function() {
        var sectionIndex = jQuery(this).closest('li').find('input.section-index-value').val();
        var rowIndex = jQuery(this).closest('li').find('input.row-index-value').val();
        jQuery('#divConfigRowGadgets input[name=section]').val(sectionIndex);
        jQuery('#divConfigRowGadgets input[name=row]').val(rowIndex);
        jQuery('#divConfigRowGadgets form').attr('action', '${This.path}/s/' + sectionIndex + '/r/' + rowIndex + '/@manage-widgets');
        jQuery.ajax({
            type : "GET",
            url : '${This.path}/s/' + sectionIndex + '/r/' + rowIndex + '/@views/manage-widgets',
            success : function(msg) {
                jQuery("#divConfigRowGadgets-content").html(msg);
                rowWidgetsAtLoad = [];
                jQuery('#divConfigRowGadgets-content option:selected').each(function() {
                    rowWidgetsAtLoad.push(jQuery(this).val());
                });
            },
            error : function(msg) {
                jQuery("#divConfigRowGadgets-content").html(msg.responseText);
                //alert('ERROR' + msg.responseText);
            }
        });
    });
    jQuery('a[rel=divConfigGadget]').click(function() {
        var sectionIndex = jQuery(this).closest('div.editblock').find('input.section-index-value').val();
        var rowIndex = jQuery(this).closest('div.editblock').find('input.row-index-value').val();
        var contentIndex = jQuery(this).closest('div.editblock').find('input.content-index-value').val();
        var widgetType = jQuery(this).closest('div.editblock').find('input.widget-type').val();
        var widgetName = jQuery(this).closest('div.editblock').find('input.widget-name').val();
        jQuery('#divConfigGadget input[name=section]').val(sectionIndex);
        jQuery('#divConfigGadget input[name=row]').val(rowIndex);
        jQuery('#divConfigGadget input[name=content]').val(contentIndex);
        jQuery('#divConfigGadget input[name=widget-type]').val(widgetType);
        jQuery('#divConfigGadget input[name=widget-name]').val(widgetName);
        jQuery('#divConfigGadget form').attr('action', '${This.path}/s/' + sectionIndex + '/r/' + rowIndex + '/c/' + contentIndex + '/w/@put');
        jQuery.ajax({
            type : "GET",
            url : '${This.path}/s/' + sectionIndex + '/r/' + rowIndex + '/c/' + contentIndex + '/w/@views/edit',
            success : function(msg) {
                jQuery("#divConfigGadget-content").html(msg);
            },
            error : function(msg) {
                jQuery("#divConfigGadget-content").html(msg.responseText);
                //alert('ERROR' + msg.responseText);
            }
        });
    });
})
</script>
</#if>
