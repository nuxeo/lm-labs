$(document).ready(function() {
    $(".nav-tabs li h5").each(function() {
        if($.trim($(this).text()) == $.trim($(".page-title h1").text())) {
            $couleur = $(this).parent().parent().css("border-bottom-color")
            $(".page-title h1").css("color", $couleur);
            $(".page-description").css("border-top-color", $couleur);
            $(".page-header h1").css("background", $couleur);
            $(".as-page-title-description h1").css("color", $couleur);
        }
    });
    
    $(".labsNewsStartPublicationDDMM").each(function(index){
        var tabDate = $(this).text().split(" ");
        $(this).parents(".labsnews").before("<div class=\"dateNews\"><span class=\"chiffreDate\">" + tabDate[0] + "</span><span class=\"moisDate\">" + tabDate[1] + "</span></div>")
    });
    
    $(".page-header h1").each(function(index){
        if(jQuery(this).text() == "")
            jQuery(this).hide();
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
    
    $(".sidebar").height($(".central").height())
    
});