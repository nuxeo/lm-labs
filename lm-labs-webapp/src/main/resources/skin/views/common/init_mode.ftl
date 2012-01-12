<#if site?? && site.isContributor(Context.principal.name) >
	<script type="text/javascript">
		
		var IS_MODE_EDITION = true;
		var pathCookie = '${Context.modulePath}/${site.URL}';
		
		$(document).ready(function() {
			  // handling shorcut for mode previsualisation
			  $(document).bind('keyup', 'e', function() {
				  $(".editblock").toggle();
				  if (IS_MODE_EDITION){
				  	  IS_MODE_EDITION = false;
				  	  $.cookie('labseditmode', 'false', { path: pathCookie });
				  	  displayViewMode();
				  	  if($("#page_edit")){
				  		$("#page_edit").html("Modifier la page"); 
				  	  }
				  }
				  else{
					  IS_MODE_EDITION = true;
					  $.cookie('labseditmode', 'true', { path: pathCookie });
					  displayEditMode();
				  	  if($("#page_edit")){
					  		$("#page_edit").html("Voir la page"); 
					  	  }
				  }
			  });
			  if ($.cookie('labseditmode') != 'true'){
				  //Passage en mode visu
				  simulateKeyup69();
			  }
		});
		
		function displayEditMode(){
			$("#logoDragMsgId").show();
			$("#logoImgId").removeClass("logoImgId-notmove");
			$("#logoImgId").addClass("logoImgId-move");
			$(".viewblock").hide();
		}
		
		function displayViewMode(){
			$("#logoDragMsgId").hide();
			$("#logoImgId").removeClass("logoImgId-move");
			$("#logoImgId").addClass("logoImgId-notmove");
			$(".viewblock").show();
		}
		
		function simulateKeyup69(){
			var press = jQuery.Event("keyup");
			  press.ctrlKey = false;
			  press.which = 69;
			  $("body").trigger(press);
		}
	</script>
</#if>