<div class="controls">
  <label class="checkbox" for="${header.idHeader}">
    <input id="${header.idHeader}" type="checkbox" name="${header.idHeader}" <#if line != null && line.getEntryByIdHead(header.idHeader) != null && line.getEntryByIdHead(header.idHeader).isCheckbox() >checked</#if> />
  </label>
</div>