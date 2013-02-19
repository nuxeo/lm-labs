String.prototype.replaceAt=function(index, char, nbrChar) {
	return this.substr(0, index) + char + this.substr(index+nbrChar);
}
if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (str){
    return this.slice(0, str.length) == str;
  };
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
var oddColor = jQuery.trim(prefs.getString("oddColor"));
var evenColor = jQuery.trim(prefs.getString("evenColor"));
var textColor = jQuery.trim(prefs.getString("textColor"));
if (textColor === "") {
	textColor = "#0066CC";
}
var displayTitleStr = prefs.getString("displayTitle");
if (gadgetId !== "") {
	NXID_GADGET = gadgetId;
}
var NX_FOLDER_jsonstr = gadgets.util.unescapeString(prefs.getString("NX_FOLDER"));
var NXPATH = "";
var NXFOLDERTITLE = "";
var NXCLASSEURTITLE = "";
if(NX_FOLDER_jsonstr !== "") {
	var docidjson = gadgets.json.parse(NX_FOLDER_jsonstr);
	NXPATH = unescape(docidjson.NXPATH);
	NXFOLDERTITLE = unescape(docidjson.NXTITLE);
	NXCLASSEURTITLE = unescape(docidjson.NXCLASSEURTITLE);
}

// configure Automation REST call
var NXRequestParamsConvertPath = {
	operationId : 'LabsSite.MakeAbsolutePath',
	operationParams : {
		gadgetDocId : NXID_GADGET,
		endPath : NXPATH
	},
	operationContext : {},
	operationCallback : callbackConvertPath
};
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
if (textColor !== "") {
	NXRequestParams.textColor = textColor;
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
	if (displayTitleStr === 'CLASSEURFOLDER') {
		doAutomationRequest(NXRequestParamsSiteUrl);
	} else if (displayTitleStr === 'CUSTOM') {
		displayCustomTitle();
	}
}

function callbackSiteUrl(response, nxParams) {
	var labsSiteUrl = response.data['value'];
	displayDetailsUrl(labsSiteUrl, nxParams.labsSiteModulePath);
}

function callbackConvertPath(response, nxParams) {
	NXPATH = response.data['value'];
	NXRequestParams.operationParams.queryParams = NXPATH;
	doAutomationRequest(NXRequestParams);
}

function getTitleStyle() {
	var titleStyle = "style='padding-left: 4px; font-size: larger;";
	if (textColor !== "") {
		titleStyle += "color: " + textColor + ";";
	}
	titleStyle += "'";
	return titleStyle;
}

function displayCustomTitle() {
	var customTitle = prefs.getString("customTitle");
	if (customTitle !== "") {
		var html = "";
		html += "<span ";
		html += getTitleStyle();
		html += ">";
		html += "<strong>";
		html += gadgets.util.escapeString(customTitle);
		html += "</strong>";
		html += "</span>";
		_gel("nxBottomZone").innerHTML = html;
		jQuery("#nxBottomZone").css('margin-bottom', '10px');
		gadgets.window.adjustHeight();
	}
}

function displayDetailsUrl(siteUrl, modulePath) {
	if (displayTitleStr === 'CLASSEURFOLDER') {
		// build URL
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

		// build title
		pos = title.lastIndexOf("/");
		if (pos >= 0) {
			title = title.replaceAt(pos, " - ", 1);
		}
		pos = title.lastIndexOf("/");
		if (pos >= 0) {
			title = title.substring(pos + 1);
		}

		var newTitle = "";
		if (NXCLASSEURTITLE !== "") {
			newTitle = NXCLASSEURTITLE + ' - ';
		}
		newTitle += NXFOLDERTITLE;

		html += "<a title=\"";
		html += "";
		html += "\" href=\"";
		html += "#";
		html += "\" ";
//	if (NXRequestParams.bootstrapEnabled) {
//		html += "class='btn btn-mini btn-primary' ";
//	}
		html += "class='btn btn-mini' ";
		html += getTitleStyle() + " ";
		html += "onclick=\"containerNavigateTo('";
		html += fullUrl;
		html += "');\" ";
		html += "/>";
		html += "<strong>";
		if (newTitle !== "") {
			html += newTitle;
		} else {
			html += title;
		}
		html += "</strong>";
		html += "</a>";
		_gel("nxBottomZone").innerHTML = html;
		jQuery("#nxBottomZone").css('margin-bottom', '10px');
		gadgets.window.adjustHeight();
	}
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
	if (NXPATH !== "" && !NXPATH.startsWith('/')) {
		doAutomationRequest(NXRequestParamsConvertPath);
	} else {
		doAutomationRequest(NXRequestParams);
	}
});

function log(str) {
	if (typeof console !== "undefined") {
		console.log(str);
	}
}
