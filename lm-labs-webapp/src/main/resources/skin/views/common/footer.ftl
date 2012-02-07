<div id="FKfooter_logo_lm">&nbsp;</div>
<@block name="footer">
  <#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')>
	  <div style="text-align: center;">
	  	<a href="${Context.modulePath}/${site.URL}/@views/edit_contacts">
	  		<button class="btn small">${Context.getMessage("label.footer.contact.goToAdminContact")}</button>
	  	</a>
	  </div>
  </#if>
	
  <div id="contact" style="margin-left: 20px;"></div>
  <div id="admin_contact" style="margin-left: 20px;"></div>
  
  <script type="text/javascript">
	jQuery(document).ready(function(){
		jQuery("#admin_contact").load('${This.path}/@labspermissions/permAdmin');
		jQuery("#contact").load('${Context.modulePath}/${site.URL}/@labscontacts/contactAdmin');
	});
  </script> 
</@block>

<div style="clear:both;"></div>

<div id="FKfooter_bottom">
    <div id="FKfooter_logo_adeo"></div>
</div>