<html>
	<body>
		<h2>${newsTitlesList?size} nouvelle<#if newsTitlesList?size &gt; 1>s</#if> actualité<#if newsTitlesList?size &gt; 1>s</#if> dans la page de d'actualités '${htmlEscape(docTitle)}' du site <a href="${baseUrl}/site/labssites/${site.URL}">${site.title}</a> :</h2>
		<ul>
		<#list newsTitlesList as title>
			<li>${htmlEscape(title)}</li>
		</#list>
		</ul>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}">Cliquez ici pour accéder à la page d'actualités</a></p>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}/@unsubscribe">Cliquez ici pour vous désabonner de la page d'actualités</a></p>
		
	</body>
</html>