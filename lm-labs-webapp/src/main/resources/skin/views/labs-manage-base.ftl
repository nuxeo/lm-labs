<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
        <@block name="meta">
          <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        </@block>

        <title>
            <@block name="title">Labs</@block>
        </title>

        <@block name="css">
          <link rel="icon" type="image/x-icon" href="/nuxeo/img/logo.jpeg" />
          <link rel="shortcut icon"  type="image/x-icon" href="/nuxeo/img/logo.jpeg"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.fancybox-1.3.4.css" />
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
          <link rel="stylesheet/less" href="${skinPath}/less/labs.less">
      <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.1.4.min.js"></script>
      <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
        </@block>

        <@block name="scripts">
          <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.validate.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
        </@block>
    </head>
    <body>
    <a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
    <div id="FKtopContent">
      <#include "views/common/topbar.ftl" />

      <div id="masthead">
          <img src="${skinPath}/images/banniere.jpg" />
      </div>

      <#-- <div class="container">
        <div class="row">
           <div class="span16 columns">
          <#assign messages = This.messages />
          <#if messages??>
          <#list messages?keys as key >
             <div class="alert-message ${key}">
               <a class="close" href="#">x</a>
               ${messages[key]}
             </div>
          </#list>
          </#if>
          </div>
        </div>
      </div>  -->

      <div class="container">
        <@block name="content">
        </@block>
      </div>



      <div style="clear:both;"></div>

    </div><!--FKtopContent-->

    <div id="FKfooter">
        <#include "views/common/footer.ftl">
    </div><!--FKfooter-->
<div>
&nbsp;
</div>
    </body>
</html>