<textarea ${txtDisabled} id="${header.idHeader}" name="${header.idHeader}" class="input span4 error ${txtDisabled} ${txtRequired}" style="height: 150px;"><#if line != null && line.getEntryByIdHead(header.idHeader) != null>${line.getEntryByIdHead(header.idHeader).text}</#if></textarea>