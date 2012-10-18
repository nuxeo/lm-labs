<#macro labsContentAssets ref path=This.path isCommon="false">
	<#assign endUrl = "" />
	<#if isCommon == "true">
		<#assign endUrl = "?isCommon=true" />
	</#if>
	<ul class="thumbnails">
	<#list Session.getChildren(ref) as doc>
	  <#if !doc.isFolder >
	  <li class="">
	  <div class="thumbnail" style="background-color:white;" >
	  <a onclick="sendToCallFunction('${path}/${doc.name}/@blob${endUrl}');return false;">
	    <img src="${path}/${doc.name}/@blob${endUrl}" width="120"/>
	    <p style="font-size: smaller;margin: 3px;" >${doc.title?html}</p>
	  </a>
	  </div>
	  </li>
	  </#if>
	</#list>
	</ul>
	<input type="hidden" id="pathToAction" value="${path}" />
</#macro>