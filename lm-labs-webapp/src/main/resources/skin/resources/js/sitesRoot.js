function sendForm(path, msgError){
	jQuery('#waitingPopup').dialog2('open');
    $("#form-labssite").validate();
    var options = {
	    beforeSubmit: function(){
	      return $("#form-labssite").valid();
	    },
        success: function(responseText, statusText) {
          if (statusText == "notmodified"){
            $("#labsSiteURL").val($("#oldURL").val());
            alert(msgError);
            jQuery('#waitingPopup').dialog2('close');
          }else{
            document.location.href=path + '?homepage=display';
          }
        },
        error: function(){
          alert("ERROR");
          jQuery('#waitingPopup').dialog2('close');
        }
    };
    $('#form-labssite').ajaxForm(options);
}

function extractSortValue(node) {
    // extract data from markup and return it
    var sortValues = jQuery(node).find('span[class=sortValue]');
    if (sortValues.length > 0) {
        return sortValues[0].innerHTML;
    }
    return node.innerHTML;
}

jQuery(document).ready(function(){
    var sitesTables = jQuery("table[class*='table-striped']");
    jQuery.each(sitesTables, function(i, val) {
        if (jQuery(val).hasClass('hasCategoryColumn') && jQuery(val).hasClass('hasDeleteColumn')) {
            jQuery(val).tablesorter({
                headers: { 4: { sorter: false}, 5: { sorter: false}},
                sortList: [[0,0]],
                textExtraction: extractSortValue
            });
        } else if (jQuery(val).hasClass('hasCategoryColumn') && !jQuery(val).hasClass('hasDeleteColumn')) {
            jQuery(val).tablesorter({
                headers: { 4: { sorter: false}},
                sortList: [[0,0]],
                textExtraction: extractSortValue
            });
        } else if (jQuery(val).hasClass('hasCategoryColumn') || jQuery(val).hasClass('hasDeleteColumn')) {
            jQuery(val).tablesorter({
                headers: { 3: { sorter: false}, 4: { sorter: false}},
                sortList: [[0,0]],
                textExtraction: extractSortValue
            });
        } else if (jQuery(val).attr('id') == 'MyDeletedSites') {
            jQuery(val).tablesorter({
                headers: { 3: { sorter: false}, 4: { sorter: false}},
                sortList: [[0,0]],
                textExtraction: extractSortValue
            });
        } else {
            jQuery(val).tablesorter({
                headers: { 3: { sorter: false}},
                sortList: [[0,0]],
                textExtraction: extractSortValue
            });
        }
    });

  $(".dialog2").each(function() {
    $(this).dialog2({
      showCloseHandle : true,
      removeOnClose : false,
      closeOnOverlayClick : false,
      autoOpen : false
    });
  });

  $(".open-dialog").click(function(event) {
    event.preventDefault();
    dialogId = $(this).attr("dialog");
    $(dialogId).dialog2("open");
  });
});