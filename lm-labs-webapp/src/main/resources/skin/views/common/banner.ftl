
<div id="labssite-banner">
    <img src="${This.path}/@banner" id="bannerImgId"/> 
</div>

<#assign canAdminister = false />
<#if This.type.name != "sitesRoot" >
  <#if Session.hasPermission(This.document.ref, 'Everything') >
    <#assign canAdminister = true />
  </#if>
</#if>

<#if canAdminister >
	<style type="text/css">
	  #actionBanner {
	    float: right;
	    height: 30px;
	    margin-top: -36px;
	    padding-right: 4px;
	    text-align: right;
	    z-index: 3;
	     }
	     
	  #labssite-banner {
	  	float: left;
	    height: 79px;
	    z-index: -1;
	     }
	 </style>
	<div id="actionBanner">
		<button id="bt_modifyBanner" >${Context.getMessage('label.labssites.banner.modify')}</button>
		<button id="bt_deleteBanner">${Context.getMessage('label.labssites.banner.delete')}</button>
	</div>
	<div style="clear: both; "></div>
	
	<div id="divEditBanner" class="editBanner" style="display: none;">
		<form enctype="multipart/form-data" id="form-banner" method="post">
			<p>
	        	<span><input type="file" size="33" id="bannerFileId" name="bannerfile"/></span>
	     	</p>
		</form>
	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			
			jQuery("#divEditBanner").dialog({
				dialogClass: 'modify-banner',
				open: function() {},
				buttons: {
					"Annuler": function() { jQuery(this).dialog("close");},
					"Modifier": function(evt) {
						if (jQuery("#bannerFileId").attr("value").length > 0) {
							jQuery(this).dialog("close");
							setTimeout(function() {jQuery('#waitingPopup').dialog({ modal: true });}, 100);
		
							jQuery("#form-banner").ajaxSubmit({
								type: "POST",
								url : "${This.path}/@banner/",
								success: function(data){
									if (data.indexOf("Upload file ok") == -1) {
										alert("failed: " + data);
									} else {
										jQuery("#bannerFileId").val("");
										var cal = new Date();
							            jQuery('#waitingPopup').dialog( "close" );
										jQuery("#bannerImgId").attr("src","${This.path}/@banner?date="+cal.getTime());
									}
								}
							});
							return true;
						}
					}
				},
				width: 800,
				modal: true,
				autoOpen: false
			});
		
			jQuery("#bt_modifyBanner").click(function() {
				jQuery("#divEditBanner").dialog('open');
				return false;
			});
			
			jQuery("#bt_deleteBanner").click(function() {
				jQuery.ajax({
					type: "DELETE",
					url : "${This.path}/@banner/",
					success: function(data){
						var cal = new Date();
						$("#bannerImgId").attr("src","${This.path}/@banner?date="+cal.getTime());
					},
					error: function(msg){
						alert( msg.responseText );
					}			
				});
				return false;
			});
			
		});
	</script>
</#if>