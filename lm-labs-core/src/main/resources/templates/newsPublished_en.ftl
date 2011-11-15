<html>
	<body>
		<h2>${newsTitlesList?size} new News in News Page '${htmlEscape(docTitle)}' :</h2>
		<ul>
		<#list newsTitlesList as title>
			<li>${htmlEscape(title)}</li>
		</#list>
		</ul>
		<p><a href="${baseUrl}/site/labssites/${pageUrl}">Click here to open the News Page</a></p>
		<p><a href="${baseUrl}/site/labssites/${siteUrl}">Click here to open site ${siteTitle}</a></p>
		<p><a href="${baseUrl}/site/labssites/${pageUrl}/@unsubscribe">Click here to unsubscribe</a></p>
		
	</body>
</html>