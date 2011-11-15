<html>
	<body>
		<h2>${newsTitlesList?size} nouvelle<#if newsTitlesList?size &gt; 1>s</#if> actualité<#if newsTitlesList?size &gt; 1>s</#if> dans la page d'actualités '${htmlEscape(docTitle)}' :</h2>
		<ul>
		<#list newsTitlesList as title>
			<li>${htmlEscape(title)}</li>
		</#list>
		</ul>
		<p><a href="${baseUrl}/site/labssites/${pageUrl}">Cliquez ici pour accéder à la page d'actualités</a></p>
		<p><a href="${baseUrl}/site/labssites/${siteUrl}">Cliquez ici pour accéder au site ${siteTitle}</a></p>
		<p><a href="${baseUrl}/site/labssites/${pageUrl}/@unsubscribe">Cliquez ici pour vous désabonner de la page d'actualités</a></p>
		
	</body>
</html>