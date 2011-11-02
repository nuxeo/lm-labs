<@extends src="/views/labs-base.ftl">

<#assign canManage = Session.hasPermission(Document.ref, 'Everything') />

	<@block name="css">
	<@superBlock/>
	<style>
	#jstree {
		background-color: white;
	}
	</style>
	</@block>
	<@block name="scripts">
	<@superBlock/>
	<script type="text/javascript" src="${skinPath}/js/jstree/jquery.jstree.js"></script>
	<script type="text/javascript" >
		jQuery().ready(function() {
			jQuery("#jstree")
			.jstree({
				"core": {
					"html_titles" : true,
					"strings": { loading : "${Context.getMessage('label.admin.loading')} ..." }
				},
				"plugins" : [ "json_data", "themes", "ui", "types"
				<#if canManage >
				, "crrm", "hotkeys", "contextmenu", "dnd"
				</#if>
				],
				"json_data" : {
					"ajax" : { 
						"url" : "${This.path}/@treeview"
						, "data" : function (n) {
							return {
								"view" : "admin",
								"id" : n.attr ? n.attr("id") : 0
							};
						}
						//, "error" : function(jqXHR, textStatus, errorThrown) { alert("ERROR: " + jqXHR.status + ", " + jqXHR.statusText + ", " + textStatus + ", "+ errorThrown);}
					}
				},
				"types" : {
					"valid_children" : [ "Tree" ],
					"types" : {
						"Tree" : {
							"icon" : {
								"image" : "/nuxeo/icons/site.jpeg"
							}
						},
						"default" : {
							"icon" : {
								"image" : "/nuxeo/icons/page_text.gif"
							}
						}
					}
				},
				"contextmenu" : {
					"items" : function(node) { 
						<#--console.log("node type: " + node.attr('rel'));-->
						if (node.attr('rel') == 'Tree') {
							return { create:false, rename:false, remove:false,
								"ccp" : {
									"separator_before"	: true,
									"icon"				: false,
									"separator_after"	: false,
									"label"				: "${Context.getMessage('command.admin.edit')}",
									"action"			: false,
									"submenu" : { 
										"cut" : {
											"separator_before"	: false,
											"separator_after"	: false,
											"label"				: "${Context.getMessage('command.admin.cut')}",
											"action"			: function (obj) { this.cut(obj); }
										},
										"copy" : {
											"separator_before"	: false,
											"icon"				: false,
											"separator_after"	: false,
											"label"				: "${Context.getMessage('command.admin.copy')}",
											"action"			: function (obj) { this.copy(obj); }
										},
										"paste" : {
											"separator_before"	: false,
											"icon"				: false,
											"separator_after"	: false,
											"label"				: "${Context.getMessage('command.admin.paste')}",
											"action"			: function (obj) { this.paste(obj); }
										}
									}
								}
							};
						}
						return {
						"create" : 
					<#--
						{
							"_disabled"         : true,
							"separator_before"	: false,
							"separator_after"	: true,
							"label"				: "",
							"action"			: function (obj) { this.create(obj); }
						}
					-->
						false
						,
						"rename" : {
							"separator_before"	: false,
							"separator_after"	: false,
							"label"				: "${Context.getMessage('command.admin.rename')}",
							"action"			: function (obj) { this.rename(obj); }
						},
						"remove" : {
							"separator_before"	: false,
							"icon"				: false,
							"separator_after"	: false,
							"label"				: "${Context.getMessage('command.admin.delete')}",
							"action"			: function (obj) {
								if (confirm("${Context.getMessage('label.admin.page.deleteConfirm')}")) {
									if(this.is_selected(obj)) {
										this.remove();
									} else {
										this.remove(obj);
									}
								}
							}
						},
						"ccp" : {
							"separator_before"	: true,
							"icon"				: false,
							"separator_after"	: false,
							"label"				: "${Context.getMessage('command.admin.edit')}",
							"action"			: false,
							"submenu" : { 
								"cut" : {
									"separator_before"	: false,
									"separator_after"	: false,
									"label"				: "${Context.getMessage('command.admin.cut')}",
									"action"			: function (obj) { this.cut(obj); }
								},
								"copy" : {
									"separator_before"	: false,
									"icon"				: false,
									"separator_after"	: false,
									"label"				: "${Context.getMessage('command.admin.copy')}",
									"action"			: function (obj) { this.copy(obj); }
								},
								"paste" : {
									"separator_before"	: false,
									"icon"				: false,
									"separator_after"	: false,
									"label"				: "${Context.getMessage('command.admin.paste')}",
									"action"			: function (obj) { this.paste(obj); }
								}
							}
						}
					}
				} // function
				} // contextmenu
			})
			.bind("loaded.jstree", function (event, data) {
			<#--
				alert("TREE IS LOADED");
			-->
			})
			.bind("move_node.jstree", function (event, data) {
				<#--
				console.log("move_node: " + data.rslt.o.length);
				-->
				data.rslt.o.each(function (i) {
					if (data.rslt.cr === -1) {
					} else {
						var operation = data.rslt.cy ? "copy" : "move";
						<#--
						console.log("operation:" + operation);
						-->
						jQuery.ajax({
							async : false,
							type: 'POST',
							url: "${This.path}/@pageUtils/" + operation,
							data : {
								"source" : jQuery(this).attr("id"), 
								"destination" : data.rslt.np.attr("id")
							},
							success : function (r) {
						        if (r.redirect) {
						            // data.redirect contains the string URL to redirect to
						            window.location.replace(r.redirect);
						        } else {
						        	<#--
						        	console.log("no redirect to " + r);
						        	-->
									if(data.rslt.cy ) {
										<#--
										console.log("refresh parent of " + data.rslt.oc);
										-->
										data.inst.refresh(data.inst._get_parent(data.rslt.oc));
									}
						        }
							}
						});
					}
				});
			})
			.bind("remove.jstree", function (e, data) {
				if (confirm("${Context.getMessage('label.admin.page.deleteConfirm')}")) {
					data.rslt.obj.each(function () {
						if (jQuery(this).attr('rel') != 'Tree') {
							jQuery.ajax({
								async : false,
								type: 'GET',
								url: "${This.previous.path}/id/" + this.id + "/@delete", 
								success : function (r) {
									if(!r.status) {
										data.inst.refresh();
									}
								}
								, error : function (r, responseData) {
									jQuery.jstree.rollback(data.rlbk);
								}
							});
						} else {
							jQuery.jstree.rollback(data.rlbk);
							data.inst.refresh();
						}
					});
				} else {
					jQuery.jstree.rollback(data.rlbk);
					data.inst.refresh();
				}
			})
			.bind("rename.jstree", function (e, data) {
				if (data.rslt.obj.attr('rel') != 'Tree') {
					jQuery.post(
						"${This.previous.path}/id/" + data.rslt.obj.attr("id") + "/@put", 
						{ 
							"dc:title" : data.rslt.new_name
						}, 
						function (r) {
							if(!r.status) {
								//jQuery.jstree.rollback(data.rlbk);
							}
						}
					);
				} else {
					jQuery.jstree.rollback(data.rlbk);
				}
			})
			;
		})
	</script>
	</@block>

  <@block name="tabs">
    <div class="container">
      <ul class="pills">
        <li><a href="${This.path}/@views/edit">Général</a></li>
        <li><a href="${This.path}/theme/${site.siteThemeManager.theme.name}">Thème</a></li>
        <li><a href="${This.path}/@views/edit_perms">Permissions</a></li>
        <li class="active"><a href="${This.path}/@views/administer_pages">Gérer les Pages</a></li>
        <li><a href="${This.path}/@views/edit_trash">Poubelle</a></li>
      </ul>
    </div>
  </@block>

	<@block name="content">
		<div id="content" class="container">
			<section>
				<div class="page-header">
				<h1>Gérer les Pages
				<img style="cursor:pointer;" src="${skinPath}/images/theme/header_help.png" onclick="jQuery('#help-jstree').show();return false;"/>
				</h1>
				</div>
				<div id="help-jstree" class="alert-message block-message success" style="display:none;">
					<a class="close" href="#">&times;</a>
					<#assign i18nPfx = "label.admin.help." />
					<p>
					${Context.getMessage(i18nPfx + 'intro')}<br/>
					<#list ["drag", "ctrldrag", "f2", "leftclick", "rightclick"] as fn>
						<strong>${Context.getMessage(i18nPfx + 'functions.' + fn)}</strong> ${Context.getMessage(i18nPfx + 'functions.' + fn + '.text')}<br/>
					</#list>
					</p>
				</div>
				<div class="row">
					<@jsTreeControls id="jsTreeControlsTop" />
					<div class="span16 columns">
						<div id="jstree">
						</div>
					</div>
					<@jsTreeControls id="jsTreeControlsBottom" />
				</div>
			</section>
		</div>
	</@block>
	
</@extends>
<#macro jsTreeControls id>
<div id="${id}" class="span16 columns jsTreeControls">
	<button class="small btn" id="expandTreeBt" onclick="jQuery('#jstree').jstree('open_all');return false;">
	${Context.getMessage('command.admin.tree.expandAll')}
	</button>
	<button class="small btn" id="collapseTreeBt" onclick="jQuery('#jstree').jstree('close_all');return false;">
	${Context.getMessage('command.admin.tree.collapseAll')}
	</button>
</div>
</#macro>