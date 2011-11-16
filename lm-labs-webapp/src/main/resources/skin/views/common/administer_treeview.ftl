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
	<#if adminTreeviewType=="Assets">
		<#assign formPath=This.path+"/@assets" />
	</#if>
	<script type="text/javascript" src="${skinPath}/js/jstree/jquery.jstree.js"></script>
	<script type="text/javascript">
		jQuery().ready(function() {
			jQuery('#addFileForm').ajaxForm(function() { 
				jQuery("#addFileDialog").dialog2('close');
                jQuery("#jstree").jstree("refresh");
            }); 
			jQuery("#addFileDialog").dialog2({
				autoOpen : false,
				closeOnOverlayClick : true,
				removeOnClose : false,
				showCloseHandle : true,
			});
			
			function customMenu(node) {
				<#-- ALL ITEM MENU -->
				var items = {
					"create" : {
		                "separator_before"  : false,
		                "separator_after"   : true,
		                "label"             : "${Context.getMessage('command.admin.create')}",
		                "action"            : false,
		                "submenu" :{
		                    "create_file" : {
		                        "seperator_before" : false,
		                        "seperator_after" : false,
		                        "label" : "${Context.getMessage('command.admin.upload_file')}",
		                        action : function (obj) {
		                            jQuery('#addFileForm').attr('action', "${formPath}/id/"+obj.attr("id"));
		    						jQuery("#addFileDialog").dialog2('open');
		                        }
		                    },
		                    "create_folder" : {
		                        "seperator_before" : false,
		                        "seperator_after" : false,
		                        "label" : "${Context.getMessage('command.admin.create_folder')}",
		                        action : function (obj) {
			                        this.create(obj, "Folder");
			                    }
			                }
		                }
		            },
					"ccp_tree" : {
						"separator_before"	: true,
						"icon"				: false,
						"separator_after"	: false,
						"label"				: "${Context.getMessage('command.admin.edit')}",
						"action"			: false,
						"submenu" : { 
							"cut" : false,
							"copy" : false,
							"paste" : {
								"separator_before"	: false,
								"icon"				: false,
								"separator_after"	: false,
								"label"				: "${Context.getMessage('command.admin.paste')}",
								"action"			: function (obj) { this.paste(obj); }
							}
						}
					},
		            "goto" : {
						"separator_before"	: false,
						"separator_after"	: false,
						"label"				: "${Context.getMessage('command.admin.gotoPage')}",
						"action"			: function (nodes) {
							window.location = "${Context.modulePath}/" + this._get_node(nodes[0]).data("url");
						}
					},
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
							<#if adminTreeviewType=="Assets">
								if (confirm("${Context.getMessage('label.admin.asset.deleteConfirm')}")) {
							<#else>
								if (confirm("${Context.getMessage('label.admin.page.deleteConfirm')}")) {
							</#if>
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
				};
				
				<#-- selected item we want -->
				if(jQuery(node).attr('rel') == 'Tree' || jQuery(node).attr('rel') == 'Assets') {
					delete items.goto;
					delete items.rename;
					delete items.remove;
					delete items.ccp;
					<#if adminTreeviewType=="Pages">
						delete items.create;
					</#if>
				} else if(jQuery(node).attr('rel') == 'Folder') {
					delete items.ccp_tree;
					<#if adminTreeviewType=="Assets">
						delete items.goto;
					</#if>
				} else {
					delete items.ccp_tree;
					delete items.create;
					<#if adminTreeviewType=="Assets">
						delete items.goto;
					</#if>
				}
				return items;
			}
		
			jQuery("#jstree")
			<#if adminTreeviewType=="Assets">
			.bind("loaded.jstree", function (event, data) {
				loadPictures(jQuery("li[rel=Assets]").attr("id"));
			})
			</#if>
			<#--
			.bind("load_node.jstree", function (event, data) {
				console.log(jQuery(data.rslt.obj).attr("id") + " node LOADED " + data.rslt.obj.data("lifecyclestate"));
			})
			-->
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
								<#if adminTreeviewType=="Assets">
									"view" : "admin_asset",
								<#else>
									"view" : "admin",
								</#if>
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
							"remove" : false,
							"rename" : false,
							"icon" : {
								"image" : "/nuxeo/icons/site.jpeg"
							}
						},
						<#if adminTreeviewType=="Assets">
							"File" : {
								"icon" : {
									"image" : "/nuxeo/icons/page_text.gif"
								}
							},
							"Picture" : {
								"icon" : {
									"image" : "/nuxeo/icons/page_text.gif"
								}
							}
						<#else>
							"default" : {
								"icon" : {
									"image" : "/nuxeo/icons/page_text.gif"
								}
							}
						</#if>
					}
				},
				"contextmenu" : { "items" : customMenu }
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
			.bind("create.jstree", function (e, data) {
			
			    //if creating a root node
			    if(!$(data.rslt.parent).attr("id")) var id = 1;
			    //else get parent
			    else var id = data.rslt.parent.attr("id").replace("node_","");
			    
   			   jQuery.ajax({
					async : false,
					type: 'POST',
					url: "${This.path}/@pageUtils/addFolder",
					data : {
						"title" : data.rslt.name,
						"doctype" : data.args[1],
						"destination" : id
					},
					success : function (r) {
			        	$(data.rslt.obj).attr("id", "node_" + r.id);
			        	jQuery("#jstree").jstree("refresh");
			        },
			        error : function (r) {
			            $.jstree.rollback(data.rlbk);
			        }
				});
			})
			.bind("select_node.jstree", function (e, data) {
				rel = $(data.rslt.obj).attr("rel");
				if(rel=='Assets' || rel=='Folder') {
					loadPictures($(data.rslt.obj).attr("id"));
				} else if(rel=='Picture') {
					jQuery("#contentAdminPictures").html("<img src='${This.path}/@assets/id/"+$(data.rslt.obj).attr("id")+"/@blob' />");
				}					
			})
			;
		})
	</script>
	</@block>

  <@block name="tabs">
	<#include "macros/admin_menu.ftl" />
	<#if adminTreeviewType=="Assets">
		<@adminMenu item="admin_asset"/>
	<#else>
		<@adminMenu item="admin_page"/>
	</#if>
  </@block>

	<@block name="content">
		<div id="content" class="container">
			<section>
				<#if adminTreeviewType=="Assets">
					<div style="display: none">
						<#include "macros/add_file_dialog.ftl" />
						<@addFileDialog action="${formPath}" onSubmit=""/>
				    </div>
				</#if>
			
				<div class="page-header">
				<h1><#if adminTreeviewType=="Assets">Gérer les médias<#else>Gérer les Pages</#if>	
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
		
		<#if adminTreeviewType=="Assets">
		    <div id="fileContent" class="span11 columns well" style="min-height:300px;">
		        <#include "views/AssetFolder/content_admin.ftl"/>
		    </div>
			<#-- load picture in terms of treeview's node -->
			<script type="text/javascript">
				function loadPictures(id){
				    jQuery.ajax({
				      type: 'GET',
				      async: false,
				      url: '${This.path}' + '/@assets/id/'+id+'/@views/content_admin',
				      success: function(data) {
				      	jQuery("#contentAdminPictures").html(data);
				      }
				    });
				}
			</script>
		</#if>
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