<#if (entry.text?length > 0)>
	<div rel="popoverEditLine" data-content="${entry.text?replace("\"", "'")}" data-original-title="${header.name?replace("\"", "'")}" class="ellipsisText" ellipsisTextOptions="{ max_rows: 2, alt_text_e: false, alt_text_t: false }">
			<div>${entry.text?replace("\n", "<br />")}</div>
	</div>
</#if>