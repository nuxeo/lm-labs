<script type="text/javascript">
    var usernameTmp = "";
    jQuery(document).ready(function() {
	    bindFocus(jQuery("#username"), jQuery("#username").val());
	    bindFocus(jQuery("#password"), jQuery("#password").val());
	
	    usernameTmp = $('#username').val();
	
	    jQuery(".listener").keypress(function(e){
	      if (e.keyCode == 13 || e.which == 13){
	       valid();
	      }
	    });
	
	    jQuery("#login").click(valid);
	    jQuery("#logout").click(doLogout);
	    
	    $('input[placeholder], textarea[placeholder]').placeholder();
	  });



  function valid(){
    var user = jQuery("#username").val();
    var pwd = jQuery("#password").val();

    if(jQuery.trim(user) == "" || jQuery.trim(pwd) == "" || jQuery.trim(user) == usernameTmp) {
      jQuery("#FKerrorLogin").show();
    } else {
      doLogin(user,pwd);
    }
   }


  function bindFocus(elem, value){
    jQuery(elem).focus(function() {
        jQuery("#FKerrorLogin").hide();
        //if (this.value == value) this.value = "";
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
       return false;
      }
    });
  }
</script>

<#if Context.principal.isAnonymous() == true>
    <form class="navbar-form form-horizontal pull-right" action="">
    <input type="text" id="username" placeholder="${Context.getMessage('label.Username')}" class="input-small listener" size="13"/>
    <input type="password" id="password" placeholder="${Context.getMessage('label.Password')}" class="input-small listener" size="13"/>
    <button id="login" title="${Context.getMessage('tooltip.login')}" class="btn listener" onclick="return false;">${Context.getMessage('command.login')}</button>
    </form> 
</#if>