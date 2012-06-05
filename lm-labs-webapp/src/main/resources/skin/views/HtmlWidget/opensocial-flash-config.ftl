<#include "views/HtmlWidget/${widgetType}-config.ftl" />
<#--
<div class="control-group">
    <label class="control-label" for="title" >Titre</label>
    <div class="controls" >
        <input type="text" class="input" id="video" name="video-title-field" value="" />
    </div>
</div>
-->
<div class="control-group">
    <label class="control-label" for="file" >Animation</label>
    <div class="controls" >
<#if 0 < Document.file.content.length >
        <p>${Document.file.filename} (${Document.file.content.length} octets)</p>
</#if>
        <input type="file" name="file" id="file" class="input" size="20" />
    </div>
</div>
<script type="text/javascript">
<#-- TODO before form submit -->
function control(){
    var filename  =jQuery.trim(jQuery("#file").val())  ;
    if(filename == "")
        return false;

    if(filename.match(".swf$" == ".swf")) {
        alert("Fichier non pris en charge (pas .swf)");
        return false;
    }
    return true;
};
jQuery(document).ready(function() {
    jQuery('#divConfigGadget form').attr('action', '/nuxeo/site/gadgetDocumentAPI/${Document.id}');
    jQuery('#divConfigGadget form').attr('enctype', 'multipart/form-data');
})
</script>
