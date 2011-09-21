  <script type="text/javascript">
    var usernameTmp = "";
    jQuery(document).ready(function() {
    bindFocus(jQuery("#username"), jQuery("#username").val());
    bindFocus(jQuery("#password"), jQuery("#password").val());

    usernameTmp = $('#username').val();

    jQuery(".listener").keypress(function(e){
      if (!e)
        e = event;
      if (e.keyCode == 13 || e.which == 13){
       valid();
      }
    });

    jQuery("#login").click(valid);
    jQuery("#logout").click(doLogout);
  });

  function valid(){
    var user = jQuery("#username").val();
    var pwd = jQuery("#password").val();

    if(jQuery.trim(user) == "" || jQuery.trim(pwd) == "" || jQuery.trim(user) == usernameTmp) {
      jQuery("#FKerrorLogin").show().html("${Context.getMessage('label.login.failed')}");
    } else {
      doLogin(user,pwd);
    }
   }


  function bindFocus(elem, value){
    jQuery(elem).focus(function() {
        jQuery("#FKerrorLogin").hide();
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
    var req = jQuery.ajax({
      type: "POST",
      async: false,
      url: "${Context.loginPath}",
      data: {caller: "login", nuxeo_login : "true", username : username, password : password},
      success: function(html, status) {
        succesRedirect();
        jQuery.cookie("LAST_ACTION", "LOGIN", {path: "/"});
        return true;
      },
      error: function(html, status) {
       jQuery("#FKerrorLogin").show().html("${Context.getMessage('label.login.failed')}");
       return false;
      }
    });
  }

  function succesRedirect(){
    jQuery.ajax({
      url: document.location.href,
      method: "GET",
      success: function(data) {
        document.location.reload();
      },
      error: function(){
        var url = escape("${Context.loginPath}");
      document.location.href = url.split("@")[0];
      }
    });
  }

  </script>


<#if Context.principal.isAnonymous() == true>
    <form action="">
    <input type="text" id="username" placeholder="${Context.getMessage('label.Username')}" class="small listener" size="13"/>
    <input type="password" id="password" placeholder="Mot de passe" class="small listener" size="13"/>
    <button id="login" title="Login" class="btn listener">OK</button>
    </form> 
</#if>