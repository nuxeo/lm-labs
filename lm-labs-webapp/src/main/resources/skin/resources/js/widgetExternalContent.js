function renderWidgetExternalContent(widgetdivId, externalContentUrl) {
	var contentObj = jQuery('#' + widgetdivId + ' > div.external-content');
	if (isValidExternalContentUrl(externalContentUrl)) {
		jQuery.ajax({
			type: 'GET',
			url: externalContentUrl,
			dataType: 'html',
			success: function(data, textStatus, xhr) {
				if (xhr.status != 200 || !isValidExternalContent(data)) {
					jQuery(contentObj).hide();
					jQuery(contentObj).siblings('div.external-content-error').show();
				} else {
					jQuery(contentObj).html(data);
				}
			},
			error: function(xhr, textStatus, errorThrown) {
				jQuery(contentObj).siblings('div.external-content-error').find('div.extra-error').html('Erreur: ' + xhr.status + " " + xhr.statusText)
				jQuery(contentObj).hide();
				jQuery(contentObj).siblings('div.external-content-error').show();
			}
		});
	} else {
		jQuery(contentObj).siblings('div.external-content-error').find('div.extra-error').html('Erreur: référence de colonne invalide')
		jQuery(contentObj).hide();
		jQuery(contentObj).siblings('div.external-content-error').show();
	}
}