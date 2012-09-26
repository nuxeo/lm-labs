$(document).ready(function() {
    jQuery(".sections-edition").dialog2({
        autoOpen : false,
        closeOnOverlayClick : false
    });
  prettyPrint();

  jQuery("#div-modifyCSSLine").dialog2({
    autoOpen : false,
	closeOnOverlayClick : true,
	removeOnClose : false,
	showCloseHandle : true
  });
  
	jQuery('#tabbed_divSections').find('a[data-toggle="tab"]').on('shown', function (e) {
        var pattern=/#.+/gi //use regex to get anchor(==selector)
        var contentID = e.target.toString().match(pattern)[0];        
		initOpensocialGadgets(jQuery(contentID));
		
		// resize widget 'Dernieres actualites du site'
		jQuery(contentID).find('.rss-feed-list.bloc .itemList').ready(function() {
			jQuery('.rss-feed-list.bloc').each(function(index, obj) {
				jQuery(obj).parent().animate({
			        height:jQuery(obj).height() + 20
				});
			});
		});
	});
});

function openModifiyCSSLine(url, cssName){
	jQuery("#div-modifyCSSLine").dialog2('open');
	jQuery('#form-modifyCSSLine').attr('action', url + '/@modifyCSS');
	jQuery("#cssName").val(cssName);
}

function displayCssClass(section){
    jQuery("#displayCssClass_" + section).show();
    jQuery("#herfDisplayCssClass_" + section).hide();
}