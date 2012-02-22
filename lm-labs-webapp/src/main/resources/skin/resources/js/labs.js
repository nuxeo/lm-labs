$(document).ready(function() {
  // Dropdown for navbar nav
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

});

$(function () {
	$("#pageHeaderTitle[rel=popover]")
		.popover({offset: 10, html: true})
	}
)

var hasError = false;
function initRequiredFields(){
	var controlGroupClass = ".control-group";
	$(".required-fields").bind("click", function(event) {
		  var form = null;
		  /*get the form*/
		  if($(this).attr("form-id")){
			  form = $("#" + $(this).attr("form-id"));
		  }
		  else{
			  alert("Vous devez définir un attribut form-id sur l'élément .required, correspondant à l'identifiant du formulaire");
		  }
		  hasError = false;
		  $(form).children().find(".required").each(function(i, element) {
			    var elementInputClass = $(element).parents(controlGroupClass);
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
					$(element).parents(controlGroupClass).addClass("error");
					hasError = true;
				}
				else{
					$(element).parents(controlGroupClass).removeClass("error");
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
  $(".alert").each(function()
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
	jQuery('.ellipsisText').each(function(i) {
        jQuery(this).html('<span class="ellipsis_text">' + jQuery(this).html() + '</span>');
        var option = jQuery(this).attr('ellipsisTextOptions');
        if (option) {
          jQuery(this).ThreeDots(eval('(' + option + ')'));
        } else {
          jQuery(this).ThreeDots();
        }
    });
}

function popupCenter(page,largeur,hauteur,options) {
  var top=(screen.height-hauteur)/2;
  var left=(screen.width-largeur)/2;
  window.open(page,"","top="+top+",left="+left+",width="+largeur+",height="+hauteur+","+options);
}
