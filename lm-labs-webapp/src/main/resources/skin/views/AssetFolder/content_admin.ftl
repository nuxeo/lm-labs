<#-- TODO : externalize style -->

<#assign children=Session.getChildren(Document.ref) />
<#if children?size==0>
	<div id="contentAdminPictures" class="media-grid" style="width: 664px;text-align:center">
 		<span style="font-style:italic">${Context.getMessage("label.admin.asset.noPicture")}</span>
 	</div>
<#else>
	<div id="contentAdminPictures" class="media-grid" style="width: 664px;">
		<#list children as doc>
		  <#if !doc.isFolder >
		  <a>
		    <img src="/nuxeo/nxpicsfile/default/${doc.id}/Thumbnail:content/any_value" style="float: left;"/>
			<img src="${skinPath}/images/asset/bin.png" onclick="deletePicture('${doc.id}');" style="float: left; margin-left: 5px; margin-right: 5px; cursor: pointer;">
		    <span style="display: block;">${doc.name}</span>
		  </a>
		  </#if>
		</#list>
	</div>
</#if>

