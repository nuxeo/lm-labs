<ul class="nav nav-tabs">
	<li class="active" ><a href="#configGadgetRssSimpleView" data-toggle="tab" >Simple</a></li>
	<li><a href="#configGadgetRssAdvancedView" data-toggle="tab" >Avancé</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane active" id ="configGadgetRssSimpleView" >
        <#include "views/HtmlWidget/opensocial-rss-basic.ftl" />
	</div>
	<div class="tab-pane" id ="configGadgetRssAdvancedView" >
		<img src="${skinPath}/images/loading.gif" />
	</div>
</div>
<script type="text/javascript">
jQuery(document).ready(function(){
    jQuery('#divConfigGadget-content a[data-toggle="tab"]').on('shown', function (e) {
        var pattern=/#.+/gi //use regex to get anchor(==selector)
        var contentID = e.target.toString().match(pattern)[0];   
    	if (contentID == '#configGadgetRssSimpleView') {
    		var titre = jQuery('#configGadgetRssAdvancedView input[name=rssTitle1]').val();
    		var url = jQuery('#configGadgetRssAdvancedView input[name=rssUrl1]').val();
    		jQuery('#configGadgetRssAdvancedView').html('<img src="${skinPath}/images/loading.gif" />');
    		jQuery(contentID).load('${This.URL}/@views/opensocial-rss-basic', function() {
	    		jQuery(contentID).find('input[name=rssTitle1]').val(titre);
	    		jQuery(contentID).find('input[name=rssUrl1]').val(url);
    		});
    	} else {
    		var titre = jQuery('#configGadgetRssSimpleView input[name=rssTitle1]').val();
    		var url = jQuery('#configGadgetRssSimpleView input[name=rssUrl1]').val();
    		jQuery('#configGadgetRssSimpleView').html('<img src="${skinPath}/images/loading.gif" />');
    		jQuery(contentID).load('${This.URL}/@views/opensocial-config', function() {
	    		jQuery(contentID).find('input[name=rssTitle1]').val(titre);
	    		jQuery(contentID).find('input[name=rssUrl1]').val(url);
    		});
    	}
    });
});
</script>

