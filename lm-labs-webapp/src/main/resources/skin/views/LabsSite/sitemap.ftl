<@extends src="/views/labs-base.ftl">

	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

	<@block name="scripts">
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
	 </@block>

	 <@block name="css">
	  	<@superBlock/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
	</@block>
	
	<@block name="content">
		<div id="content">
			<h1>PLAN DU SITE</h1>
		    <#include "views/LabsSite/sitemap_switch_control.ftl">	
		    <div id="treeviewControl">
		    	<a href="#" id="reduceLink">Tout réduire</a>
		    </div>
		    <div>
			  <ul id="tree">
			    <li class="hasChildren">
			      <span>Arborescence du site ${This.document.title}</span>
			      <ul>
			        <li><span class="placeholder">&nbsp;</span></li>
			      </ul>
			    </li>
			  </ul>
			</div>
		
		    <script type="text/javascript">
		    jQuery(document).ready( function() {
			    jQuery("#tree").treeview({
			      url: "${This.path}/treeview",
			      ajax: {
			        data: { },
			        type: "post"
			      },
			      control: "#treeviewControl"
		    	});
			  	jQuery(".hitarea").click();
		  	});
		    </script>
		</div>
	</@block>
</@extends>	