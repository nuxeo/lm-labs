<div class="control-group" >
    <label class="control-label" for="content" >${Context.getMessage('label.HtmlPage.widget.siteRssFeed-lastNews.nbrNews')}</label>
    <div class="controls" >
        <input id="configsiteRssFeedLastNews" type="text" class="span4" name="content" value='${content.html?html}' />
    </div>
</div>
<script type="text/javascript">
jQuery(document).ready(function() {
	var formObj = jQuery('#divConfigGadget form');
	var actionUrl = jQuery(formObj).attr('action').replace('/w/@put', '');
    jQuery('#divConfigGadget form').attr('action', actionUrl);
});
</script>
