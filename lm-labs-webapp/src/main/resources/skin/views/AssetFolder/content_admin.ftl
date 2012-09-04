<#-- TODO : externalize style -->

<style>
	.emptyMsg {
		font-style:italic;
	}
	.assetVignette {
		height:140px;
		width:125px;
		border:1px groove #DDDDDD;
		border-radius:4px;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
		padding: 4px;
		float:left;
		margin:0.4em;
		word-wrap:break-word;
		cursor: pointer;
		padding-right: 10px;
	}
	.assetVignette:hover {
		background: #BEEBFF;
		border: 1px solid #99DEFD;
	}
	.imgVignette {
		float: left;
	}
</style>

<#assign children=Session.getChildren(Document.ref) />
<#if children?size==0>
	<div id="contentAdminPictures" class="thumbnails" style="width: 664px;text-align:center;margin-left: -15px;">
 		<span class="emptyMsg">${Context.getMessage("label.admin.asset.noPicture")}</span>
 	</div>
<#else>
	<div id="contentAdminPictures" class="thumbnails" style="width: 664px;margin-left: -15px;">
		<#assign hasPicture="0" />
		<#list children as doc>
		  <#if !doc.isFolder >
		  <#--We affect the same 'id' attribute to all element in this div, because JsTree needs -->
		  <div class="assetVignette jstree-draggable" title="${doc.name}" id="${doc.id}">
		    <img src="/nuxeo/nxpicsfile/default/${doc.id}/Thumbnail:content/any_value" class="imgVignette jstree-draggable" id="${doc.id}"/>
			<img src="${skinPath}/images/asset/bin.png" onclick="deletePicture('${doc.id}');" style="cursor: pointer;float:right" id="${doc.id}"/>
			<div style="clear:both" id="${doc.id}"></div>
		    <div class="ellipsisText" rel="adminAsset" ellipsisTextOptions="{max_rows:2, alt_text_e:true, alt_text_t:true}" style="width:125px;margin-top:10px" id="${doc.id}">${doc.title?html}</div>
		  </div>
		  <#assign hasPicture="1" />
		  </#if>
		</#list>
		<#if hasPicture=="0">
			<div style="text-align:center">
				<span class="emptyMsg">${Context.getMessage("label.admin.asset.noPicture")}</span>
			</div>
		</#if>
	</div>
</#if>

