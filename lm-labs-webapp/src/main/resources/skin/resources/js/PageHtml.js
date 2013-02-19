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
  
  var executedDoEllipsisTabbed_divSections = new Array();
	jQuery('#tabbed_divSections').find('a[data-toggle="tab"]').on('shown', function (e) {
        var pattern=/#.+/gi //use regex to get anchor(==selector)
        var contentID = e.target.toString().match(pattern)[0];   
        initWidgets(jQuery(contentID));
        //Refresh ellipsis
        if (!executedDoEllipsisTabbed_divSections[contentID]){
        	doEllipsisTextFind(jQuery(contentID));
        	executedDoEllipsisTabbed_divSections[contentID] = true;
        }
	});
	
	var executedDoEllipsisCarousel_divSections = new Array();
	jQuery('#carousel_divSections').bind('slid', function() {
		var carouselActiveItem = jQuery(this).find('div.item.active');
		initOpensocialGadgets(carouselActiveItem);
		initHtmlWidgets(carouselActiveItem);
        //Refresh ellipsis 
		var contentID = jQuery(carouselActiveItem).attr("id"); 
        if (!executedDoEllipsisCarousel_divSections[contentID]){
        	doEllipsisTextFind(jQuery("#"+contentID));
        	executedDoEllipsisCarousel_divSections[contentID] = true;
        }
	});
	
});

function initHtmlWidgets(parentObj) {
	// resize widget 'Dernieres actualites du site'
	jQuery(parentObj).find('.rss-feed-list.bloc .itemList').ready(function() {
		jQuery('.rss-feed-list.bloc').each(function(index, obj) {
			jQuery(obj).parent().animate({
				height:jQuery(obj).height() + 25
			});
		});
	});
}

function initWidgets(parentObj) {
	initOpensocialGadgets(jQuery(parentObj));
	initHtmlWidgets(parentObj);
}

function openModifiyCSSLine(url, cssName, userClassInput){
	jQuery("#div-modifyCSSLine").dialog2('open');
	jQuery('#form-modifyCSSLine').attr('action', url + '/@modifyCSS');
	jQuery("#cssName").val(cssName);
	$("#userClassSelect").val(userClassInput).select2({
		placeholder: "Ajouter du style",
		formatNoMatches: function(trem){
				return '';
			}
		}
	);
	$("#userClassSelect").on("change", function(event) {
		$("#userClass").val($("#userClassSelect").select2("val"));
	});
}

function displayCssClass(section){
    jQuery("#displayCssClass_" + section).show();
    jQuery("#herfDisplayCssClass_" + section).hide();
}