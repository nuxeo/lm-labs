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
		  <div style="height:140px;width:125px;border:1px groove #DDDDDD;border-radius:4px;box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);padding: 4px;float:left;margin:0.4em">
		    <img src="/nuxeo/nxpicsfile/default/${doc.id}/Thumbnail:content/any_value" style="float: left;"/>
			<img src="${skinPath}/images/asset/bin.png" onclick="deletePicture('${doc.id}');" style="cursor: pointer;float:right">
			<div style="clear:both"></div>
		    <div class="ellipsisText" ellipsisTextOptions="{max_rows:2,whole_word:false,ellipsis_string:'...',valid_delimiters:[' ', ',', '.']}" style="width:125px;margin-top:10px">${doc.name}</div>
		  </div>
		  </#if>
		</#list>
	</div>
</#if>

