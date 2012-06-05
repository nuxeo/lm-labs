<#--
<div class="control-group">
    <label class="control-label" for="title" >Titre</label>
    <div class="controls" >
        <input type="text" class="input" id="video" name="video-title-field" value="" />
    </div>
</div>
-->
<#assign files = Document.files.files />
<#assign htmlContent = "" />
<#if files?size != 0 >
    <#list files as file>
        <#if (file.filename == "htmlContent" && 0 < file.file.length) >
            <#assign htmlContent = file.file.data />
            <#break />
        </#if>
    </#list>
</#if>
<div class="control-group">
    <label class="control-label" for="wcopensocial:files/htmlContent" >Balise Video</label>
    <div class="controls" >
        <textarea id="baliseVideo" name="wcopensocial:files/htmlContent" >${htmlContent}</textarea>
    </div>
</div>
<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery('#divConfigGadget form').attr('action', '/nuxeo/site/gadgetDocumentAPI/${Document.id}');
})
</script>
