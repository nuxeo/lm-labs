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
	}
</style>
<#assign children=Session.getChildren(Document.ref) />
<#if children?size==0>
	<div id="contentAdminPictures" class="media-grid" style="width: 664px;text-align:center">
 		<span class="emptyMsg">${Context.getMessage("label.admin.asset.noPicture")}</span>
 	</div>
<#else>
	<div id="contentAdminPictures" class="media-grid" style="width: 664px;">
		<#assign hasPicture="0" />
		<#list children as doc>
		  <#if !doc.isFolder >
		  <div class="assetVignette">
		    <img src="/nuxeo/nxpicsfile/default/${doc.id}/Thumbnail:content/any_value" style="float: left;"/>
			<img src="${skinPath}/images/asset/bin.png" onclick="deletePicture('${doc.id}');" style="cursor: pointer;float:right">
			<div style="clear:both"></div>
		    <div class="ellipsisText" ellipsisTextOptions="{max_rows:2,whole_word:false,ellipsis_string:'...',valid_delimiters:[' ', ',', '.']}" style="width:125px;margin-top:10px">${doc.name}</div>
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

