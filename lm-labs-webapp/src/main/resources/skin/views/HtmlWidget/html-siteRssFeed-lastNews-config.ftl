<div class="control-group" >
    <label class="control-label" for="configsiteRssFeedLastNews" >${Context.getMessage('label.HtmlPage.widget.siteRssFeed-lastNews.nbrNews')}</label>
    <div class="controls" >
        <input id="configsiteRssFeedLastNews" type="text" class="span4" name="configsiteRssFeedLastNews" value='${content.html?html}' />
    </div>
    <label class="control-label" for="configsiteRssFeedLastNewsSummary" >${Context.getMessage('label.HtmlPage.widget.siteRssFeed-lastNews.displaySummaryPicture')}</label>
    <div class="controls" >
        <input class="checkbox" id="configsiteRssFeedLastNewsSummary" type="checkbox"  name="configsiteRssFeedLastNewsSummary"  <#if content.html?contains(",hasSummaryPicture")>checked="true"</#if> />
    </div>
</div>
<input type="hidden" name="content" id="contentDivConfigGadget" value ="${content.html}" />
<input type="hidden" id="typeConfigGadget" value ="html" />
<script type="text/javascript">
jQuery(document).ready(function() {
	var formObj = jQuery('#divConfigGadget form');
	var actionUrl = jQuery(formObj).attr('action').replace('/w/@put', '');
    jQuery(formObj).attr('action', actionUrl);
    
    var elems = "${content.html?html}".split('-');
    jQuery("#configsiteRssFeedLastNews").val(elems[0]);
    if (elems.length > 1 && elems[1] === "on"){
    	jQuery("#configsiteRssFeedLastNewsSummary").attr("checked", "true");
    }
    else{
    	jQuery("#configsiteRssFeedLastNewsSummary").removeAttr("checked");
	}    
    
    jQuery("#configsiteRssFeedLastNews").change(function() {
	  jQuery("#contentDivConfigGadget").val(jQuery("#configsiteRssFeedLastNews").val() + "-" + jQuery("#configsiteRssFeedLastNewsSummary:checked").val());
	});
	
    jQuery("#configsiteRssFeedLastNewsSummary").change(function() {
	  jQuery("#contentDivConfigGadget").val(jQuery("#configsiteRssFeedLastNews").val() + "-" + jQuery("#configsiteRssFeedLastNewsSummary:checked").val());
	});
});
</script>
