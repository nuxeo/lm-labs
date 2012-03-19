var headersCollection = new Collection();

// Override toString method Of Collection class
headersCollection.toString = function(){
	var output = [];
	var myOrder = headersCollection.getOrder();
	var myCollection = headersCollection.getCollection();
	for ( var ii = 0; ii < myOrder.length; ++ii) {
		if (myOrder[ii] != null) {
			output.push(myOrder[ii] + '\n');
			output.push('Name:' + myCollection[myOrder[ii]].name + '\n');
			output.push('Type:' + myCollection[myOrder[ii]].type + '\n');
			output.push('width:' + myCollection[myOrder[ii]].width + '\n');
			output.push('fontName:' + myCollection[myOrder[ii]].fontName + '\n');
			output.push('fontSize:' + myCollection[myOrder[ii]].fontSize + '\n');
			output.push('idHeader:' + myCollection[myOrder[ii]].idHeader + '\n');
			output.push('orderPosition:' + myCollection[myOrder[ii]].orderPosition+ '\n');
			output.push('formatDate:' + myCollection[myOrder[ii]].formatDate + '\n');
			output.push('selectlist:\n');
			var selectlist = myCollection[myOrder[ii]].selectlist;
			for ( var j = 0; j< selectlist.length; j++) {
				if (selectlist[j] != null){
					output.push('\t' + 'option[' + j + ']:' + selectlist[j]+ '\n');
				}
			}
			output.push('\n');
		}
	}
	return output;
}

//add method for organize orderPosition
headersCollection.organizeOrderPosition = function(){
	var myOrder = headersCollection.getOrder();
	var myCollection = headersCollection.getCollection();
	for ( var ii = 0; ii < myOrder.length; ++ii) {
		if (myOrder[ii] != null) {		
			myCollection[myOrder[ii]].orderPosition = ii;
		}
	}
}

jQuery(document).ready(function() {
	jQuery("#divManageList").dialog2({
		height : '385px',
		overflowy : 'auto',
		overflowx : 'hidden',
		autoOpen : false, 
		closeOnOverlayClick : false, 
		removeOnClose : false, 
		showCloseHandle : false
	});
});

function manageList() {
	jQuery("#divManageList").dialog2('open');
	initFields();
}

function closeManageList() {
	jQuery("#divManageList").dialog2('close');
	headersCollection.setCollection(eval( headersMapBase ), eval( '[' + headersNameBase  + ']'));
}

function initFields() {
	if (is_new) {
		initNewHeader();
	} else {
		headersCollection.setCollection(eval( headersMapBase ), eval( '[' + headersNameBase  + ']'));
		loadHeaders();
		changeHeader(headersCollection.getSelectedName());
		setCheckbox(allContributors, 'allContributors');
		setCheckbox(commentableLines, 'commentableLines');
	}
}

function setCheckbox(isChecked, varName){
	if (isChecked){
		$('#' + varName).attr("checked", true);
	}
	else{
		$('#' + varName).attr("checked", false);
	}
}

function initNewHeader() {
	$("#form-manageList").clearForm();
	$("#headerName").val(label_newsHeader);
	$("#headerName").select();
	$("#headerName").focus();
	$('#headerType option[value=TEXT]').attr("selected", "selected");
	$('#headerFormatDate option:nth(0)').attr("selected", "selected");
	$('#headerFontName option:nth(0)').attr("selected", "selected");
	$('#headerFontSize option:nth(0)').attr("selected", "selected");
	$('#headerWidth option:nth(0)').attr("selected", "selected");
	clearDivEditOptions();
	hideEditSelect();
	hideEditFormatDate();
}

function manageEditType() {
	if (jQuery("#headerType").val() == "SELECT") {
		initDivEditOptions();
		displayEditSelect();
	}else {
		clearDivEditOptions();
		hideEditSelect();
	}
	if (jQuery("#headerType").val() == "DATE") {
		displayEditFormatDate();
	}
	else{
		hideEditFormatDate();
	}
	headersCollection.getSelectedItem().type = $("#headerType").val();
}

function displayEditSelect() {
	$("#div_edit_select").show();
}

function displayEditFormatDate() {
	$("#div_edit_format_date").show();
}

function hideEditSelect() {
	$("#div_edit_select").hide();
}

function hideEditFormatDate() {
	$("#div_edit_format_date").hide();
}

function initDivEditOptions() {
	addOneOption();
}

function clearDivEditOptions() {
	$("#div_edit_options").html("");
}

function deleteOption(index) {
	$("#div_edit_one_option_" + index).remove();
	headersCollection.getSelectedItem().selectlist[index] = null;
}

function changeValueOption(index) {
	headersCollection.getSelectedItem().selectlist[index] = $('#headerOption' + index).val();
}

function addOneOption() {
	var name = headersCollection.getSelectedItem().selectlist.length;
	var str = '<div id="div_edit_one_option_' + name + '" class="div_edit_one_option">';
	str = str + '	<label for="headerOption">' + label_options + '</label>';
	str = str + '	<input type="text" class="input_headerOption" name="headerOption" id="headerOption' + name + '" onblur="javascript:changeValueOption(' + name + ')" />';
	str = str + '	<span class="lineOptionsDelete" onClick="deleteOption(' + name + ');"></span>';
	str = str + '</div>';
	$("#div_edit_options").append(str);
}

function loadEditOptions() {
	$("#div_edit_options").html("");
	var selectlist = headersCollection.getSelectedItem().selectlist;
	var hasOneOption = false;
	for (var ii = 0; ii < selectlist.length; ii++){
		if (selectlist[ii] != null){
			loadOneOption(selectlist[ii], ii);
			hasOneOption = true;
		}
	}
	if (hasOneOption){
		displayEditSelect();
	}
}

function loadOneOption(optionName, index) {
	var str = '<div id="div_edit_one_option_' + index + '" class="div_edit_one_option">';
	str = str + '	<label for="headerOption">' + label_options + '</label>';
	str = str + '	<input type="text" class="input_headerOption" name="headerOption" id="headerOption' + index + '" onblur="javascript:changeValueOption(' + index + ')" value="' + optionName.replace(/"/g, '&#34;') + '" />';
	str = str + '	<span class="lineOptionsDelete" onClick="deleteOption(' + index + ');"></span>';
	str = str + '</div>';
	$("#div_edit_options").append(str);
}

function clearUlHeaders() {
	$("#ul_action_on_header").html("");
}

function addOneHeader() {
	var name = "order_" + headersCollection.totalSize();
	var str = '<li class="actionOnHeader" id="actionOnHeader' + name + '">';
	str = str + '	<span class="lineHeaderName" id="label_header_' + name + '"  onClick="javascript:changeHeader(\'' + name + '\');">' + label_newsHeader + '</span>';
	str = str + '	<span class="lineHeaderMoveUp" onClick="javascript:moveUpHeader(\'' + name + '\');"></span>';
	str = str + '	<span class="lineHeaderMoveDown" onClick="javascript:moveDownHeader(\'' + name + '\');"></span>';
	str = str + '	<span class="lineHeaderMoveDelete" onClick="javascript:deleteHeader(\'' + name + '\');"></span>';
	str = str + '</li>';
	initNewHeader();
	headersCollection.add(name, createNewHeader());
	headersCollection.setSelected(name);
	$("#ul_action_on_header").append(str);
	addClassLineheaderSelected();
}

function changeHeaderName() {
	$("#label_header_" + headersCollection.getSelectedName()).html($("#headerName").val());
}

function changeAllHeaderName() {
	headersCollection.getSelectedItem().name = $("#headerName").val();
}

function changeAllHeaderWidth() {
	headersCollection.getSelectedItem().width = $("#headerWidth").val();
}

function changeAllHeaderFormatDate() {
	headersCollection.getSelectedItem().formatDate = $("#headerFormatDate").val();
}

function changeAllHeaderFontName() {
	headersCollection.getSelectedItem().fontName = $("#headerFontName").val();
}

function changeAllHeaderFontSize() {
	headersCollection.getSelectedItem().fontSize = $("#headerFontSize").val();
}

function changeHeader(selected) {
	headersCollection.setSelected(selected) ;
	var selectedItem = headersCollection.getSelectedItem();
	$("#headerName").val(selectedItem.name);
	$("#headerName").focus();
	$('#headerType option[value=' + selectedItem.type + ']').attr( "selected", "selected");
	$('#headerFontName option[value="' + selectedItem.fontName + '"]').attr( "selected", "selected");
	$('#headerFontSize option[value=' + selectedItem.fontSize + ']').attr( "selected", "selected");
	$('#headerWidth option[value=' + selectedItem.width + ']').attr( "selected", "selected");
	$('#headerFormatDate option[value="' + selectedItem.formatDate + '"]').attr( "selected", "selected");
	addClassLineheaderSelected();
	manageEditType();
	loadEditOptions();
}

function addClassLineheaderSelected(){
	initLineHeaderSelected();
	var selectedName = headersCollection.getSelectedName();
	var newClass = $("#actionOnHeader" + selectedName).attr( "class") + " lineHeaderSelected";
	$("#actionOnHeader" + selectedName).attr( "class", newClass);
}

function initLineHeaderSelected(){
	var actionOnHeader = "actionOnHeader";
	var tabName = headersCollection.getOrder();
	for (ii = 0; ii < tabName.length; ii++){
		$("#actionOnHeader" + tabName[ii]).attr( "class", actionOnHeader);
	}
}

function loadHeaders() {
	clearUlHeaders();
	var obj = null;
	var index;
	var first = true;
	headersCollection.reset();
	while (headersCollection.hasNext()){
		index = headersCollection.nextKey();
		if (first){
			headersCollection.setSelected(index);
			first = false;
		}
		obj = headersCollection.nextItem();
		addMyHeader(obj, index);
	}
	loadEditOptions();
}

function addMyHeader(order, index) {
	var str = '<li class="actionOnHeader" id="actionOnHeader' + index + '">';
	str = str + '	<span class="lineHeaderName" id="label_header_' + index + '" onClick="javascript:changeHeader(\'' + index + '\');">' + order.name + '</span>';
	str = str + '	<span class="lineHeaderMoveUp" onClick="javascript:moveUpHeader(\'' + index + '\');"></span>';
	str = str + '	<span class="lineHeaderMoveDown" onClick="javascript:moveDownHeader(\'' + index + '\');"></span>';
	str = str + '	<span class="lineHeaderMoveDelete" onClick="javascript:deleteHeader(\'' + index + '\');"></span>';
	str = str + '</li>';
	$("#ul_action_on_header").append(str);
}

function moveUpHeader(current_header_move) {
	headersCollection.moveUp(current_header_move);
	headersCollection.organizeOrderPosition();
	loadHeaders();
	changeHeader(current_header_move);
}

function moveDownHeader(current_header_move) {
	headersCollection.moveDown(current_header_move);
	headersCollection.organizeOrderPosition();
	loadHeaders();
	changeHeader(current_header_move);
}

function deleteHeader(index) {
	if (headersCollection.size() == 1){
		alert(label_delete_error);
	}else{
		headersCollection.remove(index);
		loadHeaders();
		changeHeader(headersCollection.getSelectedName());
	}
}

function createNewHeader() {
	var obj = {};
	obj.name = "";
	obj.type = "TEXT";
	obj.width = $('#headerWidth').val();
	obj.fontName = $('#headerFontName').val();
	obj.fontSize = $('#headerFontSize').val();
	obj.idHeader = headersCollection.getOrder().length;
	obj.orderPosition = obj.idHeader;
	obj.selectlist = [];
	obj.formatDate = $('#headerFormatDate').val();
	return obj;
}

function saveHeaderList(url) {
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type : "POST",
		url : url + '/saveheaders',
		data : 'headerList=' + JSON.stringify(headersCollection.getCollection()) + "&allContributors=" + $('#allContributors:checked').val() + "&commentableLines=" + $('#commentableLines:checked').val(),
		success : function(msg) {
			document.location.href = url + msg;
		},
		error : function(msg) {
			alert('ERROR' + msg.responseText);
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}