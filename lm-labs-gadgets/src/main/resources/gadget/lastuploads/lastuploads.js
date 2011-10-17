function htmlDetailsUrl(url, nxParams) {
	var html = "";
	var fullUrl = NXGadgetContext.clientSideBaseUrl + nxParams.labsSiteModulePath + "/" + url + "/@views/latestuploads?page=0";
    if (nxParams.detailsUrlTitle) {
    	html += "<a title=\"";
    	html += nxParams.detailsUrlTitle;
    	html += "\" href=\"";
    	html += "#";
    	html += "\" ";
    	html += "onclick=\"containerNavigateTo('";
    	html += fullUrl;
    	html += "');\" ";
    	html += "/>";
    	html += nxParams.detailsUrlTitle;
    	html += "</a>";
    }
	return html;
}

function displayDetailsUrl(response, nxParams) {
	labsSiteUrl = response.data['string'];
	_gel("nxBottomZone").innerHTML = htmlDetailsUrl(response.data['string'], nxParams);
}

function containerNavigateTo(url) {
	gadgets.rpc.call("", "navigateTo", null, url);
}