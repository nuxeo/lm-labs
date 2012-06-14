<#include "views/HtmlWidget/${widgetType}-config.ftl" />
<#assign prefValue = userPreferenceValues['NXID_PictureBook'] />
<div class="control-group">
    <label class="control-label" for="picturebookTitle" >Titre de l'Album Photo</label>
    <div class="controls" >
    <input type="text" class="disabled" disabled='' name="picturebookTitle" value='' />
    </div>
</div>
<div class="control-group">
    <div class="controls" >
        <a id="selectPicturebookBtn" href="${Context.baseURL}${contextPath}/site/picturebook?modeSelect=true" class="iframe btn" >Sélectionner Album Photo</a>
        <input type="hidden" name="NXID_PictureBook" value='${prefValue}' />
    </div>
</div>
<div class="control-group" style="display:none;" >
    <label class="control-label" for="picturebookId" >ID de l'Album Photo</label>
    <div class="controls" >
        <input type="text" class="disabled" disabled='' name="picturebookId" value='' />
    </div>
</div>
<script type="text/javascript">
function setSelectFn() {
    window.gadgets = window.gadgets || {};
    window.gadgets.lmselectvalue_containerProxy = function(gadgetId, value) {
        var json = eval('(' + value + ')');
        var inputPictureBookId = jQuery('#divConfigGadget form input[name=picturebookId]');
        var inputPictureBookTitle = jQuery('#divConfigGadget form input[name=picturebookTitle]');
        jQuery(inputPictureBookId).val(json.picturebookId);
        jQuery(inputPictureBookTitle).val(json.picturebookTitle);
        if (jQuery(inputPictureBookId).val() !== "") {
            jQuery('#divConfigGadget form input[name=NXID_PictureBook]').val('{"NXID": "' + jQuery(inputPictureBookId).val() + '","NXNAME":"'+ jQuery(inputPictureBookTitle).val() +'"}');
        }
        jQuery.fancybox.close();
    }
}
jQuery(document).ready(function() {
    var userPrefNxIdPbVal = '${prefValue}';
    if (userPrefNxIdPbVal !== "") {
        var json = eval('('+ userPrefNxIdPbVal +')');
        var inputPictureBookId = jQuery('#divConfigGadget form input[name=picturebookId]');
        var inputPictureBookTitle = jQuery('#divConfigGadget form input[name=picturebookTitle]');
        jQuery(inputPictureBookId).val(json.NXID);
        jQuery(inputPictureBookTitle).val(json.NXNAME);
    }
    jQuery('#selectPicturebookBtn').fancybox({
        'onComplete' : setSelectFn,
        'width' : 980,
        'height' : 800,
        'autoDimensions' : false,
        'padding' : 10
    });
})
</script>
