<#if !isSiteRoot>
	<#assign mySite=Common.siteDoc(Document).site />
</#if>
	<script type="text/javascript">
<#if mySite?? && mySite.isContributor(Context.principal.name) >
		
		var IS_MODE_EDITION = true;
		var pathCookie = '${Context.modulePath}/${mySite.URL}';
		var dragDrop = null;
		
		$(document).ready(function() {
			  // handling shorcut for mode previsualisation
			  $(document).bind('keyup', 'e', function() {
				  $(".editblock").toggle();
				  if (IS_MODE_EDITION){
				  	  IS_MODE_EDITION = false;
				  	  $.cookie('labseditmode', 'false', { path: pathCookie });
				  	  displayViewMode();
				  	  if($("#page_edit")){
				  		$("#page_edit").html('<i class="icon-pencil"></i>Editer'); 
				  	  }
				  }
				  else{
					  IS_MODE_EDITION = true;
					  $.cookie('labseditmode', 'true', { path: pathCookie });
					  displayEditMode();
				  	  if($("#page_edit")){
					  		$("#page_edit").html('<i class="icon-eye-open"></i>Visualiser'); 
					  	  }
				  }
			  });
			  if ($.cookie('labseditmode') != 'true'){
				  //Passage en mode visu
				  simulateKeyup69();
			  } else {
			     jQuery(".viewblock").hide();
			  }
			  
		});
		
		function displayEditMode(){
			if (dragDrop != null){
				dragDrop.unlock();
			}
			$("#logoDragMsgId").show();
			$("#logoImgId").removeClass("logoImgId-notmove");
			$("#logoImgId").addClass("logoImgId-move");
			$(".viewblock").hide();
			jQuery(".cke_hidden").hide();
		}
		
		function displayViewMode(){
			$("#logoDragMsgId").hide();
			$("#logoImgId").removeClass("logoImgId-move");
			$("#logoImgId").addClass("logoImgId-notmove");
			if (dragDrop != null){
				dragDrop.lock();
			}
			$(".viewblock").show();
			jQuery(".cke_hidden").hide();
		}
		
		function simulateKeyup69(){
			var press = jQuery.Event("keyup");
			  press.ctrlKey = false;
			  press.which = 69;
			  $("body").trigger(press);
		}
		
</#if>
function refreshDisplayMode(obj){
	jQuery(obj).find(".editblock").each(
		function(i){
			if(IS_MODE_EDITION){
				jQuery(this).show();
			}
			else{
				jQuery(this).hide();
			}
		}
	);
}
	</script>