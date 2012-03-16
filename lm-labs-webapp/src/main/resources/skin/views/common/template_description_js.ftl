<script type="text/javascript">
function getTemplateDescription(name) {
    var templates = {};
<#list Common.getTemplates() as template>
    templates["${template}"] = "${Context.getMessage('label.labssites.appearance.templates.' + template + '.description')}";
</#list>
    return templates[name];
}

function updateTemplateDescription(selectObj, objId) {
    var desc = getTemplateDescription(jQuery('#' + selectObj.id + ' option:selected').val());
    if (desc.indexOf('!') == 0) {
        desc = "(Pas de description)";
    }
    jQuery('#' + objId).html('<small>' + desc + '</small>');
}
</script>
