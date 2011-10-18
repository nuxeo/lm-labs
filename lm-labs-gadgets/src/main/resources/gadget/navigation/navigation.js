var contextListLoaded = false;

function setSiteUrl(response, nxParams) {
	labsSiteUrl = response.data['string'];
}

function displayParentPageChooser() {
	var ContextRequestParams = {
		operationId : 'LabsSite.GetSameSitePages',
		operationParams : {
			pageSize : 0,
			docId : idGadget
		},
		operationContext : {},
		operationDocumentProperties : "uid,dublincore",
		entityType : 'documents',
		usePagination : false,
		displayMethod : availablePagesReceived
	};

	if (contextListLoaded) {
		showParentPagePathSelector();
	} else {
		doAutomationRequest(ContextRequestParams);
	}
}

function getParentPagePath() {
    var path = gadgets.util.unescapeString(prefs.getString("parentPage"));
    if (path == null || path == '') {
    	path = "/default-domain"; //in Nuxeo pref should be set at creation time
    }
    return path;
}

function saveParentPage() {
	var parentPagePath = _gel("parentPagePathChooser").value;
	prefs.set("parentPage", gadgets.util.escapeString(parentPagePath));
	_gel("parentPageChooser").style.display = "none";
}

function availablePagesReceived(entries, nxParams) {

    var elSel = _gel("parentPagePathChooser");

    var selectedValue = getParentPagePath();

    for (var i = 0; i < entries.length; i++) {

        var elOptNew = document.createElement('option');
        elOptNew.text = entries[i].title + " (" + entries[i].path.replace("/default-domain/sites/", "").replace("/tree", "") + ")";
        elOptNew.value = entries[i].path;
        if (elOptNew.value == selectedValue) {
            elOptNew.selected = true;
        }
        try {
            elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
        }
        catch(ex) {
            elSel.add(elOptNew); // IE only
        }
    }
    contextListLoaded = true;
    showParentPagePathSelector();
}

function showParentPagePathSelector() {
	_gel("parentPageChooser").style.display = "block";
}

function initParentPagePathSettingsButton() {
	var permission = gadgets.nuxeo.isEditable();
	if (permission) {
		_gel("parentPageButton").style.display = "block";
	}
}