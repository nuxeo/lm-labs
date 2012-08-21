<script type="text/javascript">
    var usernameTmp = "";
    jQuery(document).ready(function() {
	    bindFocusPassword(jQuery("#password"));
	
	    usernameTmp = $('#username').val();
	
	    jQuery(".listener").keypress(function(e){
	      if (e.keyCode == 13 || e.which == 13){
	       valid();
	      }
	    });
	
	    jQuery("#login").click(valid);
	    jQuery("#logoutLnk").click(doLogout);
	    
	    $('input[placeholder], textarea[placeholder]').placeholder();
	  });



  function valid(){
    jQuery('#login').button('loading');
    var user = jQuery("#username").val();
    var pwd = jQuery("#password").val();

    if(jQuery.trim(user) == "" || jQuery.trim(pwd) == "" || jQuery.trim(user) == usernameTmp) {
      jQuery("#FKerrorLogin").show();
      jQuery('#login').button('reset');
    } else {
      doLogin(user,pwd);
    }
   }


  function bindFocusPassword(elem){
    jQuery(elem).focus(function() {
        jQuery("#FKerrorLogin").hide();
        var value = jQuery("#password").val();
        if (this.value == value) this.value = "";
    });
  };

  function doLogout() {
    clearUserData();
    document.location.href = "${Context.basePath}/labssites";
  }

  function clearUserData() {
    jQuery.cookie("JSESSIONID", null, {path: "/"});
    jQuery.cookie("LAST_ACTION", "LOGOUT", {path: "/"});
    jQuery.post("${Context.loginPath}", {caller: "login", nuxeo_login : "true"});
  }

  function doLogin(username, password) {
    jQuery("#username").hide();
    jQuery("#password").hide();
    var req = jQuery.ajax({
      type: "POST",
      async: false,
      url: "${Context.loginPath}",
      data: {caller: "login", nuxeo_login : "true", username : username, password : password},
      success: function(html, status) {
        jQuery.cookie("LAST_ACTION", "LOGIN", {path: "/"});
        document.location.reload();
      },
      error: function(html, status) {
       jQuery("#FKerrorLogin").show();
       jQuery("#username").show();
       jQuery("#password").show();
       jQuery('#login').button('reset');
       return false;
      }
    });
  }
</script>

<#if Context.principal.isAnonymous() == true>
    <form class="navbar-form form-horizontal pull-right" action="" onsubmit="alert('toto');return false;">
    <input type="text" id="username" placeholder="${Context.getMessage('label.Username')}" class="input-small listener" size="13"/>
    <input type="password" id="password" class="input-small listener" size="13"/>
    <a href="#" id="login" title="${Context.getMessage('tooltip.login')}" class="btn listener" onclick="valid();return false;" data-loading-text="${Context.getMessage('command.login.ongoing')}">${Context.getMessage('command.login')}</a>
    </form> 
</#if>