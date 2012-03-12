<@extends src="/views/labs-admin-base.ftl">

<#if adminTreeviewType == "Pages" >
	<#assign homepageDoc = Common.siteDoc(Document).site.indexDocument />
	<#assign parents = Session.getParentDocuments(homepageDoc.ref) />
	<#assign ids = [] />
	<#list parents?reverse as parent>
		<#if parent.type == "Tree" >
			<#break>
		</#if>
		<#assign ids = ids + [ parent.id ] />
	</#list>
	<#assign parentIds = "[" />
	<#list ids as id >
		<#if id_index &gt; 0 >
			<#assign parentIds = parentIds + "," />
		</#if>
		<#assign parentIds = parentIds + "'#" + id + "'" />
	</#list>
	<#assign parentIds = parentIds + "]" />
</#if>

<#assign canManage = Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')/>

	<@block name="scripts">
	<@superBlock/>
	<#if adminTreeviewType=="Assets">
		<#assign formPath=This.path+"/@assets" />
	</#if>
	<script type="text/javascript" src="${skinPath}/js/jstree/jquery.jstree.js"></script>
	<script type="text/javascript">
		function refreshTreeview() {
			jQuery("#jstree").jstree("refresh");
			jQuery('#waitingPopup').dialog2('close');
		}
		function deletePicture(id) {
			if (confirm("${Context.getMessage('label.admin.asset.deleteConfirm')}")) {
				jQuery('#waitingPopup').dialog2('open');
				jQuery.ajax({
					async : false,
					type: 'GET',
					url: "${This.previous.path}/id/" + id + "/@delete", 
					success : function (r) {
						refreshTreeview();
					}, 
					error : function (r, responseData) {
						//jQuery.jstree.rollback(data.rlbk);
						jQuery('#waitingPopup').dialog2('close');
					}
				});
			}
		}
		function openAddFileDialog(id) {
			jQuery('#addFileForm').attr('action', "${formPath}/id/"+id);
			jQuery("#addFileDialog").dialog2('open');
		}
		function createFolder(title, doctype, destId) {
			 jQuery('#waitingPopup').dialog2('open');
			 jQuery.ajax({
					async : false,
					type: 'POST',
					url: "${This.path}/@pageUtils/addFolder",
					data : {
						"title" : jQuery('input[name=\'dublincore:title\']').val(),
						"doctype" : jQuery('input[name=doctype]').val(),
						"destination" : jQuery('#currentNodeId').val()
					},
					success : function (r) {
						jQuery("#addFolderDialog").dialog2('close');
			        	refreshTreeview();
			        }, 
					error : function (r, responseData) {
						jQuery('#waitingPopup').dialog2('close');
					}
			  });
			return false;
		}
	
		var homePageId = '<#if adminTreeviewType == "Pages">${homepageDoc.id}</#if>';
		
		jQuery().ready(function() {
			jQuery("#currentNodeId").val(jQuery("li[rel=Assets]").attr("id"));
			initFileDrop('fileContent', '${This.path}/@assets/paramId', refreshTreeview, 'currentNodeId', '${Context.getMessage("label.admin.error.too_many_file", 25)}', '${Context.getMessage("label.admin.error.file_too_large", 5)}');
		
			jQuery('#addFileForm').ajaxForm(function() { 
				jQuery("#addFileDialog").dialog2('close');
                loadPictures(jQuery(jQuery("li[rel=Assets]").attr("id")));
                refreshTreeview();
            });
			jQuery("#addFileDialog").dialog2({
				autoOpen : false,
				closeOnOverlayClick : true,
				removeOnClose : false,
				showCloseHandle : true
			});
			
<#if adminTreeviewType == "Pages" >
			function changeIconOfHomePage() {
				jQuery('li#' + homePageId + ' > a > ins.jstree-icon:eq(0)').css('background-image', 'url(/nuxeo/icons/home.gif)');
			}
			
			function getLabelHtml(state) {
				var labelHtml = '';
				if (state == 'draft') {
					labelHtml = '<ins>&nbsp;<span class="label label-success">${Context.getMessage('label.status.draft')}</span></ins>';
				} else if (state == 'deleted') {
					labelHtml = '<ins>&nbsp;<span class="label label-warning">${Context.getMessage('label.status.deleted')}</span></ins>';
				}
				return labelHtml;
			}
			
			function appenStatusLabel(ahref, state) {
				var label = getLabelHtml(state);
				if (label.length > 0 && !jQuery(ahref).html().match(label + '$')) {
					jQuery(ahref).append(getLabelHtml(state));
				}
			}
			
			function addNodesStatusLabels(data) {
			<#--
				//console.log('<addNodesStatusLabels> ' + data.inst);
			-->
				jQuery.each(data.args, function (i, node) {
				<#--
					//console.log(i + '+++' + jQuery(node).html());
				-->
					data.inst._get_children(node)
					jQuery.each(data.inst._get_children(node), function (index, subnode) {
					<#--
						//console.log('  ' + index + '+++' + jQuery(subnode).children('a').html());
					-->
						appenStatusLabel(
							jQuery(subnode).children('a:first'),
							data.inst._get_node(subnode).data("lifecyclestate")
						);
					});
				});
			}
			
			function addStatusLabels(data) {
			<#--
				//console.log('<addStatusLabels> ' + data.inst);
			-->
				hrefs = jQuery('div.jstree li > a');
				jQuery.each(hrefs, function(i, href) {
				<#--
					//console.log(i + '---' + jQuery(href).html());
					//console.log(i + '---' + data.inst._get_node(jQuery(href).parent()).data("lifecyclestate"));
					//console.log(i + '---' + getLabelHtml(data.inst._get_node(jQuery(href).parent()).data("lifecyclestate")));
				-->
					appenStatusLabel(
						href,
						data.inst._get_node(jQuery(href).parent()).data("lifecyclestate")
					);
				});
			}
			
			function labsPublish(operation, url, node) {
			    jQuery('#waitingPopup').dialog2('open');
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: '${Context.modulePath}/' + url + '/@labspublish/' + operation,
				    success: function(data) {
				    	refreshTreeview();
				    },
					error: function(msg){
						alert( msg.responseText );
						jQuery('#waitingPopup').dialog2('close');
					}
				});
			}
			
			function setAsHome(url, node) {
			    jQuery('#waitingPopup').dialog2('open');
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: '${Context.modulePath}/' + url + '/@setHome',
				    success: function(data) {
				    	homePageId = jQuery(node).attr('id');
				    	refreshTreeview();
				    },
				    error: function(jqXHR, textStatus, errorThrown) {
				    	alert(jqXHR.statusText);
						jQuery('#waitingPopup').dialog2('close');
				    }
				});
			}
</#if>
			
			function customMenu(node) {
				<#-- ALL ITEM MENU -->
				var items = {
					"create" : {
		                "separator_before"  : false,
		                "separator_after"   : true,
		                "label"             : "${Context.getMessage('command.admin.create')}",
		                "action"            : false,
		                "icon"              : "/nuxeo/icons/action_add.gif",
		                "submenu" :{
		                    "create_file" : {
		                        "seperator_before" : false,
		                        "seperator_after" : false,
		                        "label" : "${Context.getMessage('command.admin.upload_file')}",
		                        action : function (obj) {
		    						openAddFileDialog(obj.attr("id"));
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
						"icon"				: "/nuxeo/icons/clipboard.gif",
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
						"icon"              : "/nuxeo/icons/contextuallink.png",
						"label"				: "${Context.getMessage('command.admin.gotoPage')}",
						"action"			: function (nodes) {
							window.location = "${Context.modulePath}/" + this._get_node(nodes[0]).data("url");
						}
					},
					"rename" : {
						"separator_before"	: false,
						"separator_after"	: false,
		                "icon"              : "/nuxeo/icons/rename.png",
						"label"				: "${Context.getMessage('command.admin.rename')}",
						"action"			: function (obj) { this.rename(obj); }
					},
					"home" : {
						"separator_before"	: false,
						"separator_after"	: false,
		                "icon"              : "/nuxeo/icons/home.gif",
						"label"				: "${Context.getMessage('command.admin.setAsHomePage')}",
						"action"			: function (nodes) {
							setAsHome(this._get_node(nodes[0]).data("url"), this._get_node(nodes[0]));
						}
					},
					"markasdeleted" : {
						"separator_before"	: false,
						"icon"				: "/nuxeo/icons/bin_closed.png",
						"separator_after"	: false,
						"label"				: "${Context.getMessage('command.admin.markAsDeleted')}",
						"action"			: function (nodes) {
							labsPublish("delete", this._get_node(nodes[0]).data("url"), this._get_node(nodes[0]));
						}
					},
					"undelete" : {
						"separator_before"	: false,
						"icon"				: false,
						"separator_after"	: false,
						"label"				: "${Context.getMessage('command.admin.undelete')}",
						"action"			: function (nodes) {
							labsPublish("undelete", this._get_node(nodes[0]).data("url"), this._get_node(nodes[0]));
						}
					},
					"remove" : {
						"separator_before"	: false,
						"icon"				: "/nuxeo/icons/action_delete.gif",
						"separator_after"	: false,
						"label"				: "${Context.getMessage('command.admin.delete')}",
						"action"			: function (obj) {
							if(this.is_selected(obj)) {
								this.remove();
							} else {
								this.remove(obj);
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
								"icon"				: "/nuxeo/icons/action_clipboard_copy.gif",
								"separator_after"	: false,
								"label"				: "${Context.getMessage('command.admin.copy')}",
								"action"			: function (obj) { this.copy(obj); }
							},
							"paste" : {
								"separator_before"	: false,
								"icon"				: "/nuxeo/icons/action_paste_all.gif",
								"separator_after"	: false,
								"label"				: "${Context.getMessage('command.admin.paste')}",
								"action"			: function (obj) { this.paste(obj); }
							}
						}
					}
				};
				
				<#if !Session.hasPermission(Document.ref, 'Everything')>
					delete items.remove;
				</#if>
				<#if !(site?? && site.isAdministrator(Context.principal.name)) >
					delete items.home;
				</#if>
				
				<#-- selected item we want -->
				if(jQuery(node).attr('rel') == 'Tree' || jQuery(node).attr('rel') == 'Assets') {
					delete items.goto;
					delete items.rename;
					delete items.remove;
					delete items.ccp;
					delete items.markasdeleted;
					delete items.undelete;
					delete items.home;
					<#if adminTreeviewType=="Pages">
						delete items.create;
					</#if>
				} else if(jQuery(node).attr('rel') == 'Folder') {
					delete items.ccp_tree;
					delete items.markasdeleted;
					delete items.undelete;
					delete items.home;
					<#if adminTreeviewType=="Assets">
						delete items.goto;
					</#if>
				} else {
					delete items.ccp_tree;
					delete items.create;
					<#if adminTreeviewType=="Assets">
						delete items.markasdeleted;
						delete items.undelete;
						delete items.goto;
						delete items.home;
					<#else>
						if(jQuery(node).data('lifecyclestate') == 'undefined') {
							delete items.markasdeleted;
							delete items.undelete;
							delete items.home;
						} else if(jQuery(node).data('lifecyclestate') == 'deleted') {
							delete items.markasdeleted;
							delete items.home;
						} else {
							delete items.undelete;
						}
						if(jQuery(node).data('ishomepage') == 'true') {
							delete items.home;
						}
					</#if>
				}
				return items;
			}
		
			jQuery("#jstree")
			.bind("loaded.jstree", function (event, data) {
			<#if adminTreeviewType=="Assets">
				jQuery('.jstree a:first').click();
			<#else>
				changeIconOfHomePage();
				addStatusLabels(data);
			</#if>
			})
			<#--
			.bind("load_node.jstree", function (event, data) {
				//console.log(jQuery(data.rslt.obj).attr("id") + " node LOADED " + data.rslt.obj.data("lifecyclestate"));
			})
			-->
			<#if adminTreeviewType=="Pages">
			.bind("before.jstree", function (e, data) {
				
				//console.log('before.jstree ' + data.func + ' ' + data.args.length);
				
				if(data.func === "reload_nodes") {
					changeIconOfHomePage();
					addStatusLabels(data);
				} else if(data.func === "after_open") {
					addNodesStatusLabels(data);
				}
			})
			</#if>
			.jstree({
				"core": {
					"html_titles" : true,
					"strings": { loading : "${Context.getMessage('label.admin.loading')} ..." }
					<#if adminTreeviewType == "Pages">
					, "initially_open" : ${parentIds}
					</#if>
				},
				"dnd" : {
					"drag_check" : function(data) {
						if(data.r.attr("id") == "phtml_1") {
							return false;
						}
						return {
							after : false,
							before : false,
							inside : true
						};
					},
					"drag_finish" : function(data) {
						jQuery.ajax({
							async : false,
							type: 'POST',
							url: "${This.path}/@pageUtils/move",
							data : {
								"source" : data.o.id, 
								"destinationContainer" : data.r.attr("id")
							},
							success : function (r) {
								loadPictures($('#currentNodeId').val());
							}
						});
					}
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
									"image" : "/nuxeo/icons/picture.gif"
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
						var finded = false;
						var after;
						var currentId = jQuery(this).attr("id");

						data.inst._get_children(data.rslt.np).each(function (index, element){
							if (finded){
								after = element;
								return false;
							}
							if (jQuery(element).attr("id") == currentId){
								finded = true;
							}
							
						});
						<#--
						console.log("operation:" + operation);
						-->
						jQuery.ajax({
							async : false,
							type: 'POST',
							url: "${This.path}/@pageUtils/" + operation,
							data : {
								"source" : currentId, 
								"destinationContainer" : data.rslt.np.attr("id"),
								"after" : jQuery(after).attr("id")
							},
							success : function (r) {
								alert("success");
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
									}
									data.inst.refresh(data.inst._get_parent(data.rslt.oc));
						        }
							}
						});
						
					}
				});
			})
			.bind("remove.jstree", function (e, data) {
			<#if adminTreeviewType=="Assets">
				if (confirm("${Context.getMessage('label.admin.asset.deleteConfirm')}")) {
			<#else>
				if (confirm("${Context.getMessage('label.admin.page.deleteConfirm')}")) {
			</#if>
					data.rslt.obj.each(function () {
						if (jQuery(this).attr('rel') != 'Tree') {
							rel = $(data.rslt.obj).attr("rel");
							jQuery.ajax({
								async : false,
								type: 'GET',
								url: "${This.previous.path}/id/" + this.id + "/@delete", 
								success : function (r) {
									if(!r.status) {
										data.inst.refresh();
										<#if adminTreeviewType=="Assets">
											loadPictures($(data.rslt.parent).attr("id"));
										</#if>
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
			    
			   if(data.rslt.name!='New node') {
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
				        	refreshTreeview();
				        },
				        error : function (r) {
				            $.jstree.rollback(data.rlbk);
				        }
				   });
			   } else {
			   	alert("${Context.getMessage('label.admin.asset.dirTitleRequire')}");
			   	refreshTreeview();
			   }
			    
			})
			.bind("select_node.jstree", function (e, data) {
				rel = $(data.rslt.obj).attr("rel");
				if(rel=='Assets' || rel=='Folder') {
					loadPictures($(data.rslt.obj).attr("id"));
				} else if(rel=='Picture') {
					jQuery("#contentAdminPictures").html("<img src='${This.path}/@assets/id/"+$(data.rslt.obj).attr("id")+"/@blob' width='500' />");
				}					
			})
			;
		})
	</script>
	</@block>

	<@block name="docactions"></@block>

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
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
		<div id="help-jstree" class="alert alert-block alert-success" style="display:none;">
			<a class="close" href="#">&times;</a>
			<#assign i18nPfx = "label.admin.help." />
			<p>
			${Context.getMessage(i18nPfx + 'intro')}<br/>
			<#list ["drag", "ctrldrag", "f2", "leftclick", "rightclick"] as fn>
				<#if adminTreeviewType=="Assets">
					<strong>${Context.getMessage(i18nPfx + 'functions.' + fn + '.asset')}</strong> ${Context.getMessage(i18nPfx + 'functions.' + fn + '.text' + '.asset')}<br/>
				<#else>
					<strong>${Context.getMessage(i18nPfx + 'functions.' + fn)}</strong> ${Context.getMessage(i18nPfx + 'functions.' + fn + '.text')}<br/>
				</#if>
			</#list>
			</p>
		</div>
		
		<#if adminTreeviewType=="Assets">
		<div style="margin: 0px 0px 8px 283px;">
            <a href="#" rel="addFileDialog" class="open-dialog btn" onclick="openAddFileDialog(jQuery('#currentNodeId').val())"><i class="icon-file"></i>Ajouter un fichier</a>
            <a href="#" rel="addFolderDialog" class="open-dialog btn"><i class="icon-folder-close"></i>Ajouter un répertoire</a>
			
			<div style="font-weight: bold;font-size:16px;margin:10px 0px -6px 0px">
				${Context.getMessage("label.admin.help.dragNDrop")}
			</div>
			
			<#-- TODO IN MACRO -->
	          <div id="addFolderDialog" style="display:none;">
	            <h1>Ajouter un répertoire</h1>
	            <form class="form-horizontal" id="addFolderForm" action="${This.path}" method="post" onSubmit="return createFolder('','','');">
	              <input type="hidden" name="doctype" value="Folder"/>
	              <fieldset>
	                <div class="control-group">
	                    <label class="control-label" for="title">Nom du répertoire</label>
	                      <div class="controls">
	                        <input name="dublincore:title" class="required input"/>
	                      </div>
	                    </div>
	              </fieldset>
	              <div class="actions">
	                <button type="submit" class="btn btn-primary required-fields" form-id="addFolderForm">Ajouter</button>
	              </div>
	           </form>
	         </div>
		</div>
		</#if>
	
		<div id="content" class="container jsTreeControlsContent">
			<section>
				<div>
					<@jsTreeControls id="jsTreeControlsTop" />
					<div class="span16 columns" style="margin-top:10px;margin-bottom:10px">
						<div id="jstree">
						</div>
					</div>
					<@jsTreeControls id="jsTreeControlsBottom" />
				</div>
			</section>
		</div>
		
		<#if adminTreeviewType=="Assets">
			<input type="hidden" id="currentNodeId" value="" />
		    <div id="fileContent" class="span11 columns" style="min-height:300px;width:570px;background-color:#F5F5F5;border:1px solid rgba(0, 0, 0, 0.05);border-radius:4px;box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05) inset;padding-left:65px;margin-bottom:20px">
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
				      	jQuery("#currentNodeId").val(id);
				      	doEllipsisText();
				      }
				    });
				}
			</script>
		</#if>
	</@block>
	
</@extends>
<#macro jsTreeControls id>
<style>
.mini {
	font-size: 11px;
	padding: 3px 5px 3px 6px;
}
</style>
<div id="${id}" class="span16 columns jsTreeControls">
	<button class="btn btn-mini" id="expandTreeBt" onclick="jQuery('#jstree').jstree('open_all');return false;">
	<i class="icon-plus"></i>${Context.getMessage('command.admin.tree.expandAll')}
	</button>
	<button class="btn btn-mini" id="collapseTreeBt" onclick="jQuery('#jstree').jstree('close_all');return false;">
	<i class="icon-minus"></i>${Context.getMessage('command.admin.tree.collapseAll')}
	</button>
</div>
</#macro>