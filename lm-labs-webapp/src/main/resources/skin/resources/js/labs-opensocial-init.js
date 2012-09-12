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
		//var toto = 'ert';
		var userPrefsStr = jQuery(this).data('gadget-user-preferences');
		alert(userPrefsStr);
		if (userPrefsStr.length > 0) {
			userPrefs = $.toJSON("(" + jQuery(this).data('gadget-user-preferences') + ")");
			//userPrefs = eval("(" + jQuery(this).data('gadget-user-preferences') + ")");
		}
		//if (userPrefsStr.length > 0) {
			//alert(jQuery(this).data('gadget-user-preferences'));
			//eval('(' + jQuery(this).data('gadget-user-preferences') + ');');
			//var evaluated = '(' + jQuery(this).data('gadget-user-preferences') + ')';
			//alert(jQuery(this).data('gadget-user-preferences'));
			//userPrefs = eval('(' + userPrefsStr + ')');
		//}
			//eval('var to' + 'to="azerty";');
			//alert(toto);
			//eval( "var userPrefs = " +  didi_tttt + ";");
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