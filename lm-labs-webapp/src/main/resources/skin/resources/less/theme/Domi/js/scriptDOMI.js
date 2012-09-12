$(document).ready(function() {
    $(".nav-tabs li").each(function() {
        if($.trim($(this).text()) == $.trim($(".page-title h1").text())) {
            $(".page-title h1").css("color", $(this).css("border-bottom-color"));
            $(".page-description p").css("border-top-color", $(this).css("border-bottom-color"));
        }
        //console.log( $(".page-description p").text());
    });
    
    $(".nav-tabs li.dropdown a h5 b.caret").each(function() {
    	$(this).remove();
    });
    
    $(".nav-tabs li.dropdown div.star").each(function() {
    	$(this).remove();
    });
    
    $(".nav-tabs li.dropdown ul").each(function() {
    	$(this).remove();
    });
    
    $(".nav-tabs li.dropdown a").each(function() {
    	$(this).removeClass("dropdown-toggle");
    });
    
    $(".nav-tabs li.dropdown").each(function() {
    	$(this).removeClass("dropdown");
    });
    
});