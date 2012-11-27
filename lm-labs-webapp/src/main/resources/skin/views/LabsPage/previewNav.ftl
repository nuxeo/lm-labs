					<div onclick="javascript:window.location.href='${Context.modulePath}/${page.getPath()}'" class="hrefTitleHeaderNews">
	        			<h2>${page.title?html}</h2>
	        		</div>
	        		<br>
	        		<#if page.description ?? && page.description != null >
	        			${page.description?replace("[[TOC]]", "")}
	        		</#if>