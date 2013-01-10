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

jQuery(document).ready(function(){
  jQuery("table[class*='table-striped']").tablesorter({
    headers: { 3: { sorter: false}},
    sortList: [[0,0]],
    textExtraction: function(node) {
      // extract data from markup and return it
      var sortValues = jQuery(node).find('span[class=sortValue]');
      if (sortValues.length > 0) {
        return sortValues[0].innerHTML;
      }
      return node.innerHTML;
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