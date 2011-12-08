<div id="contentAdminPictures" class="media-grid">
<#list Session.getChildren(Document.ref) as doc>
  <#if !doc.isFolder >
  <a>
  	<#-- TODO : externalize style -->
    <#--img src="${This.path}/${doc.name}/@blob" width="120" style="float: left;"/-->
    <img src="/nuxeo/nxpicsfile/default/${doc.id}/Thumbnail:content/any_value" style="float: left;"/>
	<img src="${skinPath}/images/asset/bin.png" onclick="deletePicture('${doc.id}');" style="float: left; margin-left: 5px; margin-right: 5px; cursor: pointer;">
    <span style="display: block;">${doc.name}</span>
  </a>
  </#if>
</#list>
</div>

