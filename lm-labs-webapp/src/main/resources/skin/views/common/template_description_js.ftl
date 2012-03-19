<script type="text/javascript">
function getTemplateDescription(name) {
    var templates = {};
<#list Common.getTemplates() as template>
    templates["${template}"] = "${Context.getMessage('label.labssites.appearance.templates.' + template + '.description')}";
</#list>
    templates["none"] = "${Context.getMessage('label.labssites.appearance.templates.none.description')}";
    return templates[name];
}

function updateTemplateDescription(selectObj, objId) {
    var selectedValue = jQuery('#' + selectObj.id + ' option:selected').val();
    if (selectedValue === "") {
        selectedValue = "none";
    }
    var desc = getTemplateDescription(selectedValue);
    if (desc.indexOf('!') == 0) {
        desc = "(Pas de description)";
    }
    jQuery('#' + objId).html('<small>' + desc + '</small>');
}
</script>
