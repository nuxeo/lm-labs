${Context.getMessage('label.pageList.edit.line.url')}:
<input id="${header.idHeader}" class="input error" type="text" size="20" name="${header.idHeader}" value="<#if line != null && line.getEntryByIdHead(header.idHeader) != null && line.getEntryByIdHead(header.idHeader).url != null>${line.getEntryByIdHead(header.idHeader).url.url}</#if>" />
<br />
${Context.getMessage('label.pageList.edit.line.url.display.text')}:
<input id="${header.idHeader}DisplayText" class="input error" type="text" size="20" name="${header.idHeader}DisplayText" value="<#if line != null && line.getEntryByIdHead(header.idHeader) != null && line.getEntryByIdHead(header.idHeader).url != null>${line.getEntryByIdHead(header.idHeader).url.name}</#if>" />