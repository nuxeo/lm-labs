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

  setTimeout("hideAlerts()", 5000);

});

function hideAlerts() {
  $(".alert-message").each(function()
  {
    if (!$(this).hasClass("no-fade")) {
      $(this).fadeOut();
    }

  });
}
