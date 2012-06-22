String.prototype.replaceAt=function(index, char, nbrChar) {
	return this.substr(0, index) + char + this.substr(index+nbrChar);
}
function isNumber(o) {
	return ! isNaN (o-0);
}

var prefs = new gadgets.Prefs();
var divContent, divTools;
var displayMode = prefs.getString("displayMode");
var pageSize = prefs.getString("pageSize");
if (!isNumber(pageSize)) {
	pageSize = 0;
	prefs.set("pageSize", "0");
}
var showPagination = false;
if (pageSize > 0) {
	showPagination = true;
}
var NXID_GADGET = gadgets.nuxeo.getGadgetId();
var gadgetId = prefs.getString("NXID_GADGET");
var oddColor = prefs.getString("oddColor");
var evenColor = prefs.getString("evenColor");
if (gadgetId !== "") {
	NXID_GADGET = gadgetId;
}
var NX_FOLDER_jsonstr = gadgets.util.unescapeString(prefs.getString("NX_FOLDER"));
var NXPATH = "";
if(NX_FOLDER_jsonstr !== "") {
	var docidjson = gadgets.json.parse(NX_FOLDER_jsonstr);
	NXPATH = docidjson.NXPATH;
}
// configure Automation REST call
var NXRequestParams = {
	operationId: 'Document.PageProvider',
	operationParams: {
		providerName: 'list_PageClasseur_folder_files',
		queryParams: NXPATH,
		pageSize: pageSize
	},
	operationContext : {},
	operationDocumentProperties : "uid,common,dublincore",
	entityType : 'documents',
	usePagination : showPagination,
	displayMethod : classeurDisplayDocumentList,
	displayColumns : [
    {
		type : 'builtin',
		field : 'download',
		label : ' ',
		tooltip : 'Cliquez pour télécharger'
	}, {
		type : 'builtin',
		field : 'icon',
		label : ' '
	}, {
		type : 'builtin',
		field : 'titleWithLabsDownloadLink',
		label : 'Titre',
		tooltip : 'Cliquez pour télécharger'
	}, {
		type : 'filesize',
		field : 'common:size',
		label : 'Taille'
	}, {
		type : 'date',
		field : 'dc:modified',
		label : 'Modifié'
	}, {
		type : 'text',
		field : 'dc:creator',
		label : 'Créateur'
	}],
	noEntryLabel : 'Pas de document'
};
if ('COMPACT' == displayMode) {
	NXRequestParams.displayColumns = [
    {
		type : 'builtin',
		field : 'download',
		label : ' ',
		tooltip : 'Cliquez pour télécharger'
	},
	{type: 'builtin', field: 'icon', label: ' '},
    {type: 'builtin', field: 'titleWithLabsDownloadLink', label: 'Titre', tooltip: 'Cliquez pour télécharger'},
    {
		type : 'filesize',
		field : 'common:size',
		label : 'Taille'
	}
    ];
    NXRequestParams.hideHeaders = true;
} else if ('SIMPLE' == displayMode) {
	NXRequestParams.displayColumns = [
	{
		type : 'builtin',
		field : 'download',
		label : ' ',
		tooltip : 'Cliquez pour télécharger'
	},
	{type: 'builtin', field: 'icon', label: ' '},
	{type: 'builtin', field: 'titleWithLabsDownloadLink', label: 'Titre', tooltip: 'Cliquez pour télécharger'}
	];
	NXRequestParams.hideHeaders = true;
}
if (oddColor !== "") {
	NXRequestParams.rowOddColor = oddColor;
}
if (evenColor !== "") {
	NXRequestParams.rowEvenColor = evenColor;
}
//TODO
//NXRequestParams.bootstrapEnabled = true;

var NXRequestParamsSiteUrl = {
	operationId : 'LabsSite.GetSiteUrlProp',
	operationParams : {
		docId : NXID_GADGET
	},
	operationContext : {},
    detailsUrlTitle: 'Détail',
    labsSiteModulePath: 'site/labssites',
	operationCallback : callbackSiteUrl
};

function classeurDisplayDocumentList(entries, nxParams) {
	displayDocumentList(entries, nxParams);
	doAutomationRequest(NXRequestParamsSiteUrl);
}
function callbackSiteUrl(response, nxParams) {
	var labsSiteUrl = response.data['string'];
	displayDetailsUrl(labsSiteUrl, nxParams.labsSiteModulePath);
}

function displayDetailsUrl(siteUrl, modulePath) {
	var html = "";
	var fullUrl = "";
	fullUrl = NXPATH.replace("/default-domain/sites", "").replace("/tree", "");
	var title = fullUrl;
	fullUrl = fullUrl.replace(/^\/([^\/]+)\//, "/" + siteUrl + "/");
	var pos = fullUrl.lastIndexOf("/");
	if (pos >= 0) {
		fullUrl = fullUrl.replaceAt(pos, "?folder=", 1);
	}
	fullUrl = NXGadgetContext.clientSideBaseUrl + modulePath + fullUrl;
	title = title.replace(/^\/([^\/]+)\//, "");
	pos = title.lastIndexOf("/");
	if (pos >= 0) {
		title = title.replaceAt(pos, " - ", 1);
	}
	html += "<a title=\"";
	html += "";
	html += "\" href=\"";
	html += "#";
	html += "\" ";
//	if (NXRequestParams.bootstrapEnabled) {
//		html += "class='btn btn-mini btn-primary' ";
//	}
	html += "class='btn btn-mini' ";
	html += "style='padding-left: 4px; font-size: larger' ";
	html += "onclick=\"containerNavigateTo('";
	html += fullUrl;
	html += "');\" ";
	html += "/>";
	html += "<strong>" + title + "</strong>";
	html += "</a>";
	_gel("nxBottomZone").innerHTML = html;
	jQuery("#nxBottomZone").css('margin-bottom', '10px');
	gadgets.window.adjustHeight();
}

function initGadget() {
	divContent = jQuery("#content");
	divTools = jQuery("div.tools");
	jQuery(divTools).hide();
	gadgets.window.adjustHeight();
	jQuery("#waitMessage").css('position', 'absolute');
}

// execute automation request onload
gadgets.util.registerOnLoadHandler(function() {
	initGadget();
	doAutomationRequest(NXRequestParams);
});

function log(str) {
	if (console) {
		console.log(str);
	}
}
