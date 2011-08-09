<html>
  <head>
    <title></title>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
    
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/labssite.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
  </head>
  <body>
  	TODO : extends LABS-BASE
    <!--ul id="tree">
		<li class="hasChildren">
			<span class="folder">Item 1</span>
			<ul>
				<li><span class="placeholder">&nbsp;</span></li>
			</ul>
		</li>
		<li id="36" class="hasChildren">
			<span>Item 2</span>
			<ul>
				<li><span class="placeholder">&nbsp;</span></li>
			</ul>
		</li>
	</ul-->
	
	<ul id="tree">
		<#list Session.getChildren(treeviewRootParent.ref) as child>
		   <li id="${child.ref}">
		   		<#assign subChildren = Session.getChildren(child.ref) /> 
		   		<#--if subChildren == null>
					<span>${child.title}</span>
				<#else>
					<span><a href="treeviewChildren/${child.ref}">${child.title}</a></span>
					<ul>
						<li><span class="placeholder">&nbsp;</span></li>
					</ul>
		   		</#if-->
			</li>
		</#list>
	</ul>
    
    <script type="text/javascript">
    jQuery(document).ready( function() {
		jQuery("#tree").treeview({
			url: "${This.path}/treeviewChildren/adfffbf1-8a1a-46ab-86f4-d0a1dffcf3ed",
			// add some additional, dynamic data and request with POST
			ajax: {
				data: { "additional": "testa" },
				type: "post"
			}
		});
	}); 
    </script>
  </body>
</html>