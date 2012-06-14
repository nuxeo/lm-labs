<div>
<#if page.isDisplayable(This.DC_DESCRIPTION)>
	<div class="columns viewblock page-description">${Document.dublincore.description}</div>
</#if>

	<div class="row-ckeditor editblock toc-noreplace" id="pageDescription">
		<div id="description" class="ckeditorBorder" style="cursor: pointer">${Document.dublincore.description}</div>
		<script type="text/javascript">
function reloadPageData(response, ckeObj, ckeip_html) {
    reloadPageForTocIfNeeded(response, ckeObj, ckeip_html);
    refreshPageTitle(ckeip_html);
}
function reloadPageForTocIfNeeded(response, ckeObj, ckeip_html) {
<#if This.type.name == "PageClasseur" || This.type.name == "HtmlPage" >
    if (ckeip_html.indexOf('[[TOC]]') != -1) {
        jQuery('#waitingPopup').dialog2('open');
<#if This.type.name == "HtmlPage" >
        scrollToRowAfterCkeip(response, ckeObj, ckeip_html);
</#if>
        window.location.reload();
    }
</#if>
    jQuery(ckeObj).closest('div.row-ckeditor').siblings('.viewblock').html(ckeip_html);
<#if This.type.name == "HtmlPage" >
    scrollToRowAfterCkeip(response, ckeObj, ckeip_html);
</#if>
    return false;
}
		</script>
		<script type="text/javascript">
		  <#-- detect error when loading picture -->
		  $('#description').find('img').each(function() {
		  	$(this).error( function() {
		  		$(this).attr('style', '');
		  		$(this).attr('src', '/nuxeo/icons/image_100.png');
		  	});
		  });
		   <#-- load CKEIP plugin -->
	      $('#description').ckeip({
	      	<#-- @put (the nuxeo way to update description) seems to not work with ckeditor
	        e_url: '${This.path}/@put', -->
	        e_url: '${This.path}/updateDescriptionCKEIP',
	        ckeditor_config: ckeditorconfig,
	        view_style: "cke_hidden",
	        emptyedit_message: "<div style='font-weight: bold;font-size: 18px;padding: 5px;text-decoration: underline;cursor: pointer'>${Context.getMessage('label.ckeditor.double_click_to_edit_content')}</div>"<#if (!This.page.isDisplayable(This.DC_DESCRIPTION))>,
	        display_ckeipTex: false</#if>
	        }, reloadPageData);
	    </script>
	</div>
</div>