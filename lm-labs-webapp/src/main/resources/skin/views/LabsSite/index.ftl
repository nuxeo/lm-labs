<html>
	<head>
		<title>${siteName}</title>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/labssite.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_blocs.css"/>
	</head>
	<body>
		<div id="header">
		HEADER
		</div>
		<div id="content" class="pageBlocs">
			<div id="sidebar">
				SIDEBAR
			</div>
			<div id="comment">
				Bienvenue sur le site '${siteName}'
			</div>
			<#assign i=1>
			<#list rootFolder as root>
				<div id="bloc${i}" class="bloc">
					${root.title}
					<#list Session.getChildren(root.ref) as child>
						<p>${child.title}</p>
					</#list>
				</div>
				<#assign i=i+1>
			</#list>
			<#-- FIXME -->
			<div id="bloc${i}" class="bloc">
				MANUTE FIXME
			</div>
		</div>
		<div id="footer">
			FOOTER
		</div>
	</body>
</html>