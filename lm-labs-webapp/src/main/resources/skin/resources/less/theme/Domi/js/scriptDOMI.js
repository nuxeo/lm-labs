$(document).ready(function() {
    $(".nav-tabs li").each(function() {
        if($.trim($(this).text()) == $.trim($(".page-title h1").text())) {
            $(".page-title h1").css("color", $(this).css("border-bottom-color"));
            $(".page-description p").css("border-top-color", $(this).css("border-bottom-color"));
        }
        console.log( $(".page-description p").text());
    });
});