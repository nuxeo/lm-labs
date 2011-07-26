<#assign children = Session.getChildren(This.document.ref)>
<ul>
  <#list children as child>
  <li>
  icone, ${child.dc.title},
  <#if child.hasFacet('folderish')>
  ${child.dc.descrition},  taille, version, ${child.dc.modified.time?datetime?string("EEEE dd MMMM yyyy HH:mm")}, ${child.dc.creator}
  </#if> 
  </li>
  </#list>
</ul>