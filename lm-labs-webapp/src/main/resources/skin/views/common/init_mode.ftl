<#if This.type.name != "sitesRoot" && !mySite?? >
	<#assign mySite=Common.siteDoc(Document).getSite() />
</#if>
<#assign siteContributor = mySite?? && mySite.isContributor(Context.principal.name) />
<#assign siteAdministrator = mySite?? && mySite.isAdministrator(Context.principal.name) />
	<script type="text/javascript">
<#if siteContributor || (This.page != null && This.page.isContributor(Context.principal.name)) >
		
		var IS_MODE_EDITION = true;
		var pathCookie = '${Context.modulePath}/${mySite.URL}';
		var dragDrop = null;
		var urlHomepage = "";
		
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
			<#if siteAdministrator>
				if (dragDrop != null){
					dragDrop.unlock();
				}
				$("#logoDragMsgId").show();
				urlHomepage = $("#logoImgId").attr("onclick");
				$("#logoImgId").attr("onclick", "");
				$("#logoImgId").removeClass("logoImgId-notmove");
				$("#logoImgId").addClass("logoImgId-move");
			</#if>
			$(".viewblock").hide();
			jQuery(".cke_hidden").hide();
		}
		
		function displayViewMode(){
			<#if siteAdministrator>
				$("#logoDragMsgId").hide();
				$("#logoImgId").removeClass("logoImgId-move");
				if(urlHomepage.length > 0){
					$("#logoImgId").attr("onclick", urlHomepage);
				}
				$("#logoImgId").addClass("logoImgId-notmove");
				if (dragDrop != null){
					dragDrop.lock();
				}
			</#if>
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