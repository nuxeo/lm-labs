jQuery(document).ready(function(){
  if(jQuery('#logout').val()!=null) {
    setTimeout("jQuery('#logout').click();",parseInt(jQuery("#serverTimeoutId").val())*1000);
  }
});
