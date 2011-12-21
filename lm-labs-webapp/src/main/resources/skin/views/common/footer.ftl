<div id="FKfooter_logo_lm">&nbsp;</div>
<@block name="footer">
  <a href="/nuxeo/pdf/charte.pdf" target="_blank">${Context.getMessage('label.information')}</a> | ${Context.getMessage('label.contact')} : <a href="mailto:communicationinterne@leroymerlin.fr">communicationinterne@leroymerlin.fr</a>
  <br />
  <div id="admin_contact"></div>
  
  <script type="text/javascript">
	jQuery(document).ready(function(){
		jQuery("#admin_contact").load('${This.path}/@labspermissions/permAdmin');
	});
  </script> 
</@block>

<div style="clear:both;"></div>

<div id="FKfooter_bottom">
    <div id="FKfooter_logo_adeo"></div>
</div>