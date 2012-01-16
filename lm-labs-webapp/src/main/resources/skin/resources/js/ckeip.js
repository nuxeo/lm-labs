/*
# CKEDITOR Edit-In Place jQuery Plugin.
# Created By Dave Earley.
# www.Dave-Earley.com
*/

$.fn.ckeip = function (options, callback) {

    var original_html = $(this);
    var defaults = {
        e_height: '10',
        data: {}, e_url: '',
        e_hover_color: '#eeeeee',
        ckeditor_config: '',
        e_width: '50',
        emptyedit_message: 'Double click to edit content',
        view_style: '',
        display_ckeipTex: true
    };
    var settings = $.extend({}, defaults, options);

    return this.each(function () {
        var eip_html = $(this).html();

        if(eip_html == "") {
          $(this).html(settings.emptyedit_message);
        }
        var u_id = Math.floor(Math.random() * 99999999);
        
        if (settings.display_ckeipTex){
        	$(this).parent().before("<div id='ckeipText_" + u_id + "' class='" + settings.view_style + "viewblock' style='display:none;' >" + eip_html + "</div>");
    	}

        $(this).before("<div id='ckeip_" + u_id + "' style='display:none;'><textarea id ='ckeip_e_" + u_id + "' cols='" + settings.e_width + "' rows='" + settings.e_height + "'  >" + eip_html + "</textarea>  <br /><a class='btn primary' href='#' id='save_ckeip_" + u_id + "'>Enregistrer</a> <a href='#' class='btn'  id='cancel_ckeip_" + u_id + "'>Annuler</a></div>");

        $('#ckeip_e_' + u_id + '').ckeditor(settings.ckeditor_config);

        $(this).bind("dblclick", function () {

            $(this).hide();
            $('#ckeip_' + u_id + '').show();

        });

        $(this).hover(function () {
            $(this).css({
                backgroundColor: settings.e_hover_color
            });
        }, function () {
            $(this).css({
                backgroundColor: ''
            });
        });


        $("#cancel_ckeip_" + u_id + "").click(function () {
            $('#ckeip_' + u_id + '').hide();
            $(original_html).fadeIn();
            return false;
        });

        $("#save_ckeip_" + u_id + "").click(function () {
            var ckeip_html = $('#ckeip_e_' + u_id + '').val();
            if (settings.display_ckeipTex){
            	$('#ckeipText_' + u_id + '').html(ckeip_html);
            }
            $.post(settings.e_url, {
                content: ckeip_html,
                data: settings.data
            }, function (response) {
                if (typeof callback == "function") callback(response);

	            if(ckeip_html=='') {
	            	$(original_html).html(settings.emptyedit_message);
	            } else {
	                $(original_html).html(ckeip_html);
	            }
	            	            
                $('#ckeip_' + u_id + '').hide();
                $(original_html).fadeIn();

            })
            .error(function(msg) { 
            	alert(msg.responseText);
            	$('#ckeip_e_' + u_id + '').val($(original_html).html());
	            	            
                $('#ckeip_' + u_id + '').hide();
                $(original_html).fadeIn();
            });
            return false;

        });

    });
};
