<#if This.type.name != "sitesRoot" && !mySite?? >
	<#assign mySite=Common.siteDoc(Document).getSite() />
</#if>
<#assign siteContributor = mySite?? && mySite.isContributor(Context.principal.name) />
<#assign siteAdministrator = mySite?? && mySite.isAdministrator(Context.principal.name) />
	<script type="text/javascript">
<#if siteContributor || (This.page?? && This.page != null && This.page.isContributor(Context.principal.name)) >
var IS_MODE_EDITION = true;
var pathCookie = '${Context.modulePath}/${mySite.URL}';
var dragDrop = null;
var urlHomepage = "";
function editKeyPressed() {
      if (IS_MODE_EDITION){
          IS_MODE_EDITION = false;
          jQuery.cookie('labseditmode', 'false', { path: pathCookie });
          displayViewMode();
          if(jQuery("#page_edit")){
            jQuery("#page_edit").html('<i class="icon-pencil"></i>Editer');
          }
      }
      else{
          IS_MODE_EDITION = true;
          jQuery.cookie('labseditmode', 'true', { path: pathCookie });
          displayEditMode();
          if(jQuery("#page_edit")){
                jQuery("#page_edit").html('<i class="icon-eye-open"></i>Visualiser');
              }
      }
}

		$(document).ready(function() {
			  // handling shorcut for mode previsualisation
			  $(document).bind('keyup', 'e', editKeyPressed);
			  if ($.cookie('labseditmode') != 'true'){
				  //Passage en mode visu
				  simulateKeyup69();
			  } else {
			     jQuery(".editblock").show();
			     jQuery(".viewblock").hide();
			  }

		});

		function displayEditMode(){
			<#if siteAdministrator>
				if (dragDrop != null){
					dragDrop.unlock();
				}
				$("#logoDragMsgId").show();
				var logoImgId = jQuery("#logoImgId");
				if (logoImgId != null) {
					urlHomepage = $(logoImgId).attr("onclick");
					$(logoImgId).attr("onclick", "");
					$(logoImgId).removeClass("logoImgId-notmove");
					$(logoImgId).addClass("logoImgId-move");
				}
			</#if>
			$(".viewblock").hide();
			$(".editblock").show();
			jQuery(".cke_hidden").hide();
		}

		function displayViewMode(){
			$(".editblock").hide();
			<#if siteAdministrator>
				$("#logoDragMsgId").hide();
				$("#logoImgId").removeClass("logoImgId-move");
				var logoImgId = jQuery("#logoImgId");
				if(urlHomepage != null && urlHomepage.length > 0 && logoImgId != null) {
					$("#logoImgId").attr("onclick", urlHomepage);
				}
				if (logoImgId != null) {
					$("#logoImgId").addClass("logoImgId-notmove");
				}
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
		<#if siteContributor || (This.page?? && This.page != null && This.page.isContributor(Context.principal.name)) >
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
		<#else>
			return;
		</#if>
		}
	</script>