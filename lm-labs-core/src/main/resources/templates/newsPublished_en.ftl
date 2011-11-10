<html>
	<body>
		<h2>${newsTitlesList?size} new News in News Page '${htmlEscape(docTitle)}' of site <a href="${baseUrl}/site/labssites/${site.URL}">${site.title}</a> :</h2>
		<ul>
		<#list newsTitlesList as title>
			<li>${htmlEscape(title)}</li>
		</#list>
		</ul>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}">Click here to open the News Page</a></p>
		<p><a href="${baseUrl}/site/labssites/${pageNewsUrl}/@unsubscribe">Click here to unsubscribe</a></p>
		
	</body>
</html>