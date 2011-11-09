
	<ul class="breadcrumb">
		<#macro breadcrumb resource>
		  <#if resource.previous?? && resource.document.type != "Site" >
		    <@breadcrumb resource.previous/>
		  </#if>
		  <#if resource.document??>
		  	<#assign class = ""/>
		  	<#if (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'Write')) && !resource.visible>
		  		<#-- The 'active' class is a class of bottstrap (breadcrumb) and it's used for drafted element  -->
		  		<#assign class = "class='active'"/>
		  	</#if>
		    <li ${class}><a href="${resource.path}">${resource.document.title}</a> <span class="divider">&gt;</span></li>
		  </#if>
		
		</#macro>
		
		<#if This.type.name != "sitesRoot" >
		  <@breadcrumb This/>
		</#if>
	</ul>
