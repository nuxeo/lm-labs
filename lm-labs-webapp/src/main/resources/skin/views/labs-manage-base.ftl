<@extends src="/views/labs-common-base.ftl">

	<@block name="css">
		<@superBlock/>
		<link rel="stylesheet/less" media="all" href="${skinPath}/less/theme/labs/specific.less" />
        <style type="text/css">
		  label {
			font-weight: bold;
		  }
		</style>
	</@block>
	
	<@block name="topbar">
		<@superBlock/>
		<a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
	</@block>
	
	<@block name="FKtopContent">
		<div id="masthead">
        	<img src="${skinPath}/images/default_banner.png" />
    	</div>
    	
    	<div class="container" style="width: 960px;">
			<#--  action-message -->
			<#include "views/common/action_message.ftl" >
    		<@block name="content" />
    	</div>
	</@block>
	
	<@block name="FKfooter">
		<div id="FKfooter">
			<#include "views/footer-manage-base.ftl">
        	<#include "views/common/loading.ftl">
	    </div><#-- /FKfooter -->
	</@block>
	
</@extends>
