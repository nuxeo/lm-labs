<@extends src="/views/labs-common-base.ftl">
<#import "libs/LabsUtils.ftl" as LabsUtils />
<#assign adminTreeviewType = "Pages" />
<#assign mySite=Common.siteDoc(Document).site />
<#assign parentIds = LabsUtils.getHomePageDocIdSelectorsStr(Document) />

<@block name="title">${mySite.title}-${This.document.title}-Sélection Page</@block>

<@block name="css">
    <@superBlock/>
	<link rel="stylesheet/less" href="${Context.modulePath}/${mySite.URL}/generated.less" />
</@block>

<@block name="scripts">
	<@superBlock/>
	<script type="text/javascript" src="${skinPath}/js/jstree/jquery.jstree.js"></script>
	<script type="text/javascript" >
<#include "common/jstree-icons-labels-js.ftl" >
<#include "macros/jstree-controls.ftl" />

function sendToCallFunction(href) {
	window.opener.${This.activeAdapter.getCallFunction()}('${This.activeAdapter.getCalledRef()}', href);
    window.close();
}

jQuery(document).ready(function() {
	jQuery("#jstree")
	.bind("loaded.jstree", function (event, data) {
		changeIconOfHomePage();
		addStatusLabels(data);
	})
	.bind("before.jstree", function (e, data) {
<#--
		//console.log('before.jstree ' + data.func + ' ' + data.args.length);
-->
		if(data.func === "reload_nodes") {
			changeIconOfHomePage();
			addStatusLabels(data);
		} else if(data.func === "after_open") {
			addNodesStatusLabels(data);
		}
	})
	.bind("select_node.jstree", function (e, data) {
		var id = jQuery(data.rslt.obj).attr("id");
		var url = '${Context.modulePath}/' + data.rslt.obj.data("url");
		sendToCallFunction(url);
	})
	.jstree({
		"core": {
			"html_titles" : true,
			"strings": { loading : "${Context.getMessage('label.admin.loading')} ..." }
			, "initially_open" : ${parentIds}
		},
		"themes" : {
            "theme" : "classic"
        },
		"types" : {
			"valid_children" : [ "Tree" ],
			"types" : {
				"Tree" : {
					"remove" : false,
					"rename" : false,
					"icon" : {
						"image" : "/nuxeo/icons/site.jpeg"
					}
				},
				"File" : {
					"icon" : {
						"image" : "/nuxeo/icons/page_text.gif"
					}
				},
				"Picture" : {
					"icon" : {
						"image" : "/nuxeo/icons/picture.gif"
					}
				},
				"default" : {
					"icon" : {
						"image" : "/nuxeo/icons/page_text.gif"
					}
				}
			}
		},
		"json_data" : {
			"ajax" : {
				"url" : "${Root.getLink(mySite.document)}/@treeview"
				, "data" : function (n) {
					return {
						"view" : "admin",
						"id" : n.attr ? n.attr("id") : 0
					};
				}
				//, "error" : function(jqXHR, textStatus, errorThrown) { alert("ERROR: " + jqXHR.status + ", " + jqXHR.statusText + ", " + textStatus + ", "+ errorThrown);}
			}
		},
		"plugins" : [ "json_data", "themes", "ui", "types"
		]
	}); <#-- jQuery.jstree() -->
});
	</script>
</@block>

<@block name="topbar" />

	<div id="FKtopContent">
<@block name="FKtopContent">
		<div class="container">
			<div class="row"><div class="span12">
				<@jsTreeControls treeId="jstree" />
			</div></div>
			<div class="row">
				<div class="span12" style="margin-top:10px;margin-bottom:10px">
					<div id="jstree"></div>
				</div>
			</div><#-- row -->
			<div class="row"><div class="span12">
				<@jsTreeControls treeId="jstree" />
			</div></div>
		</div><#-- container -->
</@block>
	</div>

<@block name="FKfooter" />

<@block name="bottom-page-js" />

</@extends>