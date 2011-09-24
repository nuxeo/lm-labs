<div class="media-grid">
<#list Session.getChildren(Document.ref) as doc>
  <#if !doc.isFolder >
  <a onclick="sendToCKEditor('${This.path}/${doc.name}/@blob');return false;">
    <img src="${This.path}/${doc.name}/@blob" width="120"/>
    ${doc.name}
  </a>
  </#if>
</#list>
</div>