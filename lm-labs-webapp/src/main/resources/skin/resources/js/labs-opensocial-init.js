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
	jQuery('div.opensocialGadgets').each(function(index, value) {
		var userPrefs = {};
		var userPrefsStr = jQuery(this).data('gadget-user-preferences');
		if (userPrefsStr.length > 0) {
			userPrefs = $.toJSON("(" + jQuery(this).data('gadget-user-preferences') + ")");
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