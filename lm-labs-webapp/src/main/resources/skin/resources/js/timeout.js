jQuery(document).ready(function(){
  if(jQuery('#logoutLnk').val()!=null) {
    setTimeout("jQuery('#logoutLnk').click();",parseInt(jQuery("#serverTimeoutId").val())*1000);
  }
});
