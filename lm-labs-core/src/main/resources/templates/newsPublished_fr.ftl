<html>
	<body>
		<h2>${newsTitlesList?size} nouvelle<#if newsTitlesList?size &gt; 1>s</#if> news dans la page de news '${htmlEscape(docTitle)}' :</h2>
		<ul>
		<#list newsTitlesList as title>
			<li>${htmlEscape(title)}</li>
		</#list>
		</ul>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}">Cliquez ici pour accéder à la page de news</a></p>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}/@unsubscribe">Cliquez ici pour vous désabonner de la page de news</a></p>
		
	</body>
</html>