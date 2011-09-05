<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
    	<title>Importer une image</title>
    	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
    	<script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
    	<script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
	    
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/browse_tree.css"/>
    </head>
    <body>
    	<#-- [BEGIN] DETERMINE SITE NAME -->
    	<#assign siteName = ''/>
    	<@prev doc = This.document />
    	
    	<#macro prev doc>
		  <#if doc.type == "Site" >
		  	<#assign siteName = doc.webcontainer.url/>
		    <#return>
		  </#if>
		  <@prev doc = Session.getParentDocument(doc.ref) /> 
		</#macro>
		<#-- [END] DETERMINE SITE NAME -->
		
		<div id="treeview">
	    	<div id="treeviewControl">
		    	<a href="#" id="reduceLink">Tout réduire</a>
		    </div>
	    	<div id="treeviewDisplay">
				 <ul id="tree">
				    <li class="hasChildren">
				      <span><a onclick="addJs(this);return false;" href="#" id="" class="rootNode" class="">ARBORESCENCE</a></span>
				      <ul>
				        <li><span class="placeholder">&nbsp;</span></li>
				      </ul>
				    </li>
				  </ul>
	    	</div>
	    </div>
    	<div id="resources">
		</div>
	
		<script type="text/javascript">
		 jQuery(document).ready( function() {
		    jQuery("#tree").treeview({
		      url: "${Context.basePath}/labssites/${siteName}/browseTree",
		      ajax: {
		        data: { },
		        type: "post"
		      },
		      control: "#treeviewControl"
	    	});
	    	
		  	jQuery.ajax({
		  		url: "${Context.basePath}/labssites/${siteName}/@assets/id",
		  		type: "get",
		  		success: function(id) {
					jQuery(".rootNode").attr("id", id);
				  	jQuery(".hitarea").click();
				  	jQuery(".rootNode").click();
				}
		  	});
		  	
		  	
	  	});
	  	
	  	function addJs(obj) {
			jQuery.ajax({
				url: "${Context.basePath}/labssites/${siteName}/getPictures",
				type: "post",
				data: "docId="+obj.id,
				success: function(msg) {
					jQuery("#resources").html(msg);
				}
			});
	  	}
		
		function sendToCKEditor(href) {
			window.opener.CKEDITOR.tools.callFunction('2', href);
			window.close();
		}
		</script>
  </body>
</html>	