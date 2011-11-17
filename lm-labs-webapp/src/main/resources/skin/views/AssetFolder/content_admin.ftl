<div id="contentAdminPictures" class="media-grid">
<#list Session.getChildren(Document.ref) as doc>
  <#if !doc.isFolder >
  <a>
    <img src="${This.path}/${doc.name}/@blob" width="120"/>
    ${doc.name}
  </a>
  </#if>
</#list>
</div>