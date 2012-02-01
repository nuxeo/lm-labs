<@extends src="/views/labs-error-base.ftl"> 

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
					showCloseHandle : true
				});
				
				jQuery(".listener").keypress(function(e){
			      if (e.keyCode == 13 || e.which == 13){
			       doLogin();
			      }
			    });
				<#if Context.principal.isAnonymous() == false>
					showError();
				</#if>
			});
			
			function showError() {
		       $(".clearfix").addClass("error");
		       $(".clearfix").children(".help-inline").show();
			}
			
		
		  function doLogin() {
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
			        window.location.href = '${This.context.URL}';
			      },
			      error: function(html, status) {
			      	showError();
			      }
			    });
		  	}
		  }
  		</script>
	</@block>
	
	
	<@block name="content">
		<div id="divLogin"  class="fixed-container dialog2" style="display: none;">
			<h1>${Context.getMessage('label.unauthorized.popup.title')}</h1>
			<form id="form-loginPopup" class="form" onsubmit="doLogin();return false;" action="">
				<fieldset>
					<div class="clearfix">
						<span class="help-inline" style="display: none">
							<strong>
								<#if Context.principal.isAnonymous() == true>
									${Context.getMessage('label.login.failed')}
								<#else>
									${Context.getMessage('label.unauthorized.user.invalid', Context.principal.name)}
								</#if>
							</strong>
						</span>
						<label>${Context.getMessage('label.Username')}</label>
						<div class="input">
							<input type="text" id="username" placeholder="${Context.getMessage('label.Username')}" class="small listener text required" size="13"/>
						</div>
						<label>${Context.getMessage('label.Password')}</label>
						<div class="input">
							<input type="password" id="password" placeholder="Mot de passe" class="small listener text required" size="13"/>
						</div>
					</div>
				</fieldset>
				<div  class="actions">
					<button id="login" title="Login" form-id="form-loginPopup" class="btn primary">OK</button>
				</div>
			</form>
		</div>
	
	  <div class="alert-message block-message error no-fade">
	  <p><strong>${Context.getMessage('label.unauthorized.error.msg')}</p>
	  <p>- ${Context.getMessage('label.unauthorized.choice.1')}</p>
	  	<a class="btn small" href="#" onclick="$('#divLogin').dialog2('open');return false;">M'identifier</a>
	  <p>- ${Context.getMessage('label.unauthorized.choice.2')}</p>
	  <div class="alert-actions">
	    <a class="btn small" href="${Context.modulePath}">Liste des sites</a>
	  </div>
	</div>
	</@block>
	
</@extends>


