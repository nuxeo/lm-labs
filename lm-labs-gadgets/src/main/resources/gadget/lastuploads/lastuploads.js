function getSiteName(gadgetId) {
	return "";
//	console.log("gadgetId:" + gadgetId);
	// does not work yet
	var NXRequestParams = {
		operationId : 'Chain.LabsSite.GetSiteDocument',
		operationParams : {
			value : gadgetId
		},
		operationContext : {},
		operationCallback : setLabsSiteName
	};
	// execute automation request onload
	gadgets.util.registerOnLoadHandler(function() {
		doAutomationRequest(NXRequestParams);
	});
}

function setLabsSiteName(response, nxParams) {
	console.log('LabsSite.GetSiteDocument returns :' + response.responseText);
}
