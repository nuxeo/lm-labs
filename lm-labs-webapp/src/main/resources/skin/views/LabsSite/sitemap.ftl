<@extends src="views/LabsSite/sitemap-base.ftl">
	<@block name="FKtopContent">
	   	<@superBlock/>
	 	<@block name="pageTags"></@block>
	</@block>
	<@block name="sitemap-content">
		<#include "macros/treeview.ftl" />
		<@labsTreeview />
    </@block>
</@extends>