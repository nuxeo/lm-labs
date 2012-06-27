<ul class="thumbnails">
<#list Session.getChildren(Document.ref) as doc>
  <#if !doc.isFolder >
  <li class="">
  <div class="thumbnail" style="background-color:white;" >
  <a onclick="sendToCallFunction('${This.path}/${doc.name}/@blob');return false;">
    <img src="${This.path}/${doc.name}/@blob" width="120"/>
    <p style="font-size: smaller;margin: 3px;" >${doc.name}</p>
  </a>
  </div>
  </li>
  </#if>
</#list>
</ul>