<html>
	<head>
		<title>${siteName}</title>
		
		<#-- FIXME : variable for path -->
		<link rel="stylesheet" type="text/css" media="all" href="/nuxeo/site/sitesroot/${siteName}/skin/resources/css/page_blocs.css"/>
	</head>
	<body>
		<div id="header">
		HEADER
		</div>
		<div id="content">
		Bienvenue sur le site '${siteName}'
		<div id="sidebar">SIDEBAR</div>
		<#assign i=1>
		<#list rootFolder as root>
			<div id="bloc${i}" class="bloc">
				${root.title}
				<#list Session.getChildren(root.ref) as child>
					${child.title}
				</#list>
			</div>
			<#assign i=i+1>
		</#list>
		<#-- FIXME -->
		<div id="bloc${i}" class="bloc">MANUTE FIXME</div>
		<div id="footer">
		FOOTER
		</div>
	</body>
</html>