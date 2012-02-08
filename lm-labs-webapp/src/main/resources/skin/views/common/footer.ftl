<div id="FKfooter_logo_lm">&nbsp;</div>
<@block name="footer">
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