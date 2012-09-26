var openSocialOptions = openSocialOptions || {
/*
//    baseURL: '${Context.baseURL}${contextPath}' + '/',
*/
	baseURL: '',
	language: ''
};

var openSocialGadgetOptions = openSocialGadgetOptions || {
    displayButtons: false,
    displaySettingsButton: false,
    displayToggleButton: false,
	displayTitleBar: false,
/* TODO
//            <#if isContributor >
//            permission: '[SpaceContributeur]',
//            </#if>
*/
	width: '100%'
};

function setOpensocialOptions(baseUrl, lang) {
	openSocialOptions.baseURL = baseUrl;
	openSocialOptions.language = lang;
}

function initOpensocialGadgets() {
	initOpensocialGadgets(jQuery('body'));
}

function initOpensocialGadgets(parentObj) {
	jQuery(parentObj).find('div.opensocialGadgets').each(function(index, value) {
		var userPrefs = {};
		//eval("var userPrefs = userPrefs + index");
//		if (jQuery(this).attr("id"))
		var userPrefsStr = jQuery(this).data('gadget-user-preferences');
		if (userPrefsStr.length > 0) {
//			alert(userPrefsStr);
//			var encoded = jQuery.toJSON(userPrefsStr);
//			userPrefs = jQuery.parseJSON(encoded);
//			alert(encoded);
//			userPrefs = $.evalJSON( encoded );
			//alert(userPrefs);
			userPrefs = eval("(" + jQuery(this).data('gadget-user-preferences') + ")");
			//alert(userPrefs);
//			userPrefsStr
		}
		jQuery(this).openSocialGadget({
			baseURL: openSocialOptions.baseURL,
			language: openSocialOptions.language,
			gadgetDefs: [
				{
/*
//				specUrl: '${Context.baseURL}${contextPath}/site/gadgets/${widget.name}/${widget.name}.xml',
 */
				specUrl: jQuery(this).data('gadget-specurl'),
				userPrefs: userPrefs,
				displayTitleBar: openSocialGadgetOptions.displayTitleBar,
				width: openSocialGadgetOptions.width,
				displayButtons: openSocialGadgetOptions.displayButtons,
				displaySettingsButton: openSocialGadgetOptions.displaySettingsButton,
				displayToggleButton: openSocialGadgetOptions.displayToggleButton,
				title: jQuery(this).data('gadget-title') }
			]
		});
	});
}