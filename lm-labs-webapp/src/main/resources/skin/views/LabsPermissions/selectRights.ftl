${Context.getMessage('label.security.grant')} ${Context.getMessage('label.security.permission')}
<select id="permissionName" size="1">
    <#list rightSelectItems as item >
      <option <#if rightSelectItems?first == item>selected="selected"</#if> value="${item.right}">${Context.getMessage('label.security.portal.permission.' + item.right)}</option>
    </#list>
</select>
<button id="addPerm" onClick="javascript:addPerm();" title="${Context.getMessage('command.security.addPerm')}" class="btn primary" style="margin-left:20px;" >${Context.getMessage('command.security.addPerm')}</button>