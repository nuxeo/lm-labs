<@extends src="/views/labs-manage-base.ftl"> 

	<@block name="title">
	       Identification requise
	</@block>
	
	<@block name="scripts">
		<@superBlock/>
		<script type="text/javascript">
		    $(document).ready(function() {
				jQuery("#divLogin").dialog2({
					autoOpen : true,
					closeOnOverlayClick : false,
					removeOnClose : false,
					buttons :  {
						'${Context.getMessage('label.login')}': {
							click: function() { valid();},
							primary: true,
							type: "btn-login"
						}
					},
					showCloseHandle : true
				});
				
				jQuery(".listener").keypress(function(e){
			      if (e.keyCode == 13 || e.which == 13){
			       valid();
			      }
			    });
			    jQuery(".btn-login").data('loading-text', "${Context.getMessage('command.login.ongoing')}");
			    jQuery(".btn-login").unbind('click').click(function(e){
			    		valid();
			    	}
			    );
				<#if Context.principal.isAnonymous() == false>
					showError();
				</#if>
			});
			
			function showError() {
		       $(".control-group").addClass("error");
		       $(".control-group").children(".help-inline").show();
		       jQuery('.btn-login').button('reset');
			}
			
		
		  function valid() {
		  	jQuery('.btn-login').button('loading');
 		    username = $('#username').val();
 		    password = $('#password').val();
		  	if(username=='' || password == '') {
		  		showError();
		  	} else {
			    var req = jQuery.ajax({
			      type: "POST",
			      async: false,
			      url: "${Context.loginPath}",
			      data: {caller: "login", nuxeo_login : "true", username : username, password : password},
			      success: function(html, status) {
			        jQuery.cookie("LAST_ACTION", "LOGIN", {path: "/"});
			        window.location.reload();
			      },
			      error: function(html, status) {
			      	showError();
			      }
			    });
		  	}
		  }
  		</script>
	</@block>
	
	<@block name="topbar" />
	
	<@block name="content">
		<div id="divLogin"  class="fixed-container dialog2" style="display: none;">
			<h1>${Context.getMessage('label.unauthorized.popup.title')}</h1>
			<form id="form-loginPopup" class="form-horizontal" onsubmit="return false;" action="">
				<fieldset>
					<div class="control-group">
						<label class="control-label" style="font-weight: bold;">${Context.getMessage('label.Username')}</label>
						<div class="controls">
							<input type="text" id="username" placeholder="${Context.getMessage('label.Username')}" class="required text input listener"/>
						</div>
                    </div>
                    <div class="control-group">
						<label class="control-label" style="font-weight: bold;margin-top:15px">${Context.getMessage('label.Password')}</label>
						<div class="controls" style="margin-top:15px">
							<input type="password" id="password" placeholder="${Context.getMessage('label.Password')}" class="required text input listener"/>
						</div>
						<span class="help-inline" style="display: none;">
							<br /><br />
							<strong style="margin-left: 151px;">
								<#if Context.principal.isAnonymous() == true>
									${Context.getMessage('label.login.failed')}
								<#else>
									${Context.getMessage('label.unauthorized.user.invalid', Context.principal.name)}
								</#if>
							</strong>
						</span>
					</div>
				</fieldset>
			</form>
		</div>
	
	  <div class="alert alert-block alert-error no-fade">
	  <p class="alert-heading"><strong>${Context.getMessage('label.unauthorized.error.msg')}</strong></p>
	  <ul>
	  <li>${Context.getMessage('label.unauthorized.choice.1')}
	  <div class="alert-actions">
	  	<a class="btn btn-small" href="#" onclick="$('#divLogin').dialog2('open');return false;">M'identifier</a>
	  </div>
	  </li>
	  <li>${Context.getMessage('label.unauthorized.choice.2')}
	  <div class="alert-actions">
	    <a class="btn btn-small" href="${Context.modulePath}">Liste des sites</a>
	  </div>
	  </li>
	  </ul>
	</div>
	</@block>
	
</@extends>


