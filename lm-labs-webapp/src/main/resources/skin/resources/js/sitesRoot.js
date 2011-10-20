


function sendForm(path, msgError){
  $("#form-labssite").validate();
    var options = {
    beforeSubmit: function(){
      return $("#form-labssite").valid();
    },
        success: function(responseText, statusText) {
          if (statusText == "notmodified"){
            $("#labsSiteURL").val($("#oldURL").val());
            alert(msgError);
          }else{
            document.location.href=path + '?homepage=display';
          }
        },
        error: function(){
          alert("ERROR");
        }
    };
    $('#form-labssite').ajaxForm(options);
}

jQuery(document).ready(function(){

  $(".dialog2").each(function() {
    $(this).dialog2({
      showCloseHandle : true,
      removeOnClose : false,
      autoOpen : false
    });
  });

  $(".open-dialog").click(function(event) {
    event.preventDefault();
    dialogId = $(this).attr("dialog");
    $(dialogId).dialog2("open");
  });
});