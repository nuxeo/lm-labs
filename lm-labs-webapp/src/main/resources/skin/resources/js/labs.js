$(document).ready(function() {
  // Dropdown for topbar nav
  // ===============================
  $("body").bind("click", function(e) {
    $('.dropdown-toggle, .menu').parent("li").removeClass("open");
  });

  $(".dropdown-toggle, .menu").click(function(e) {
    var $li = $(this).parent("li").toggleClass('open');
    return false;
  });

  // Alert message close button handling
  $(".alter-message, .close").click(function(e) {
    $(this).parent().fadeOut();
  });
  
  initRequiredFields();

  setTimeout("hideAlerts()", 5000);

  $(document).controls();

  // handling shorcut for mode previsualisation
  $(document).bind('keyup', 'e', function() {
	  $(".editblock").toggle();
  });
  
  
});

function initRequiredFields(){
	$(".required-fields").bind("click", function(event) {
		  var form = null;
		  /*get the form*/
		  if($(this).attr("form-id")){
			  form = $("#" + $(this).attr("form-id"));
		  }
		  else{
			  alert("Vous devez définir un attribut form-id sur l'élément .required, correspondant à l'identifiant du formulaire");
		  }
		  var hasError = false;
		  $(form).children().find(".required").each(function(i, element) {
			    var elementInputClass = $(element).parents(".clearfix");
				if($(element) && $(element).val().length < 1){
					if($(element).attr("required-error-text")){
						if (elementInputClass.children("span").html() == null){
							elementInputClass.prepend("<span class='help-inline'><strong>" + $(element).attr("required-error-text") + "</strong></span><br />");
						}
					}
					else{
						if (elementInputClass.children("span").html() == null){
							elementInputClass.prepend("<span class='help-inline'><strong>Element obligatoire !</strong></span><br />");
						}
					}
					$(element).parents(".clearfix").addClass("error");
					hasError = true;
				}
				else{
					$(element).parents(".clearfix").removeClass("error");
					if (elementInputClass.children("span").html() != null){
						elementInputClass.children("span").remove();
					}
				}
			});
		  if (hasError){
			  event.preventDefault();
		  }
	  });
}

function hideAlerts() {
  $(".alert-message").each(function()
  {
    if (!$(this).hasClass("no-fade")) {
      $(this).fadeOut();
    }

  });
}

function EllipsisText() {
  this.init = doEllipsisText;
}

function doEllipsisText() {
	jQuery('.ellipsisText').each(
      function() {
        jQuery(this).html('<span class="ellipsis_text">' + jQuery(this).html() + '</span>');
        var option = jQuery(this).attr('ellipsisTextOptions');
        if (option) {
          jQuery(this).ThreeDots(eval('(' + option + ')'));
        } else {
          jQuery(this).ThreeDots();
        }
      }
    );
}
