<#if (This.page?? && This.page != null) >
	<#assign listTags=This.page.getLabsTags()>
	<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />
	<#if canWrite>
		<div id="divEditTags" class="container-fluid editblock">
			${Context.getMessage('label.labstags.title')}&nbsp;:&nbsp;
			<input type="hidden" id="idTags" style="width:85%" value="<#list listTags as tag>${tag?html}<#if (listTags?last != tag)>,</#if></#list>"/>
			
			<script type="text/javascript">
				var labstags = [<#list listTags as tag>"${tag?js_string}"<#if (listTags?last != tag)>,</#if></#list>];
				var labsTagsURL = "${This.path}/@labstags";
				
				jQuery(document).ready(function(){
					$("#idTags").select2({ 	
						placeholder: "Mots-clés",
						tags: labstags,
						formatNoMatches: function(trem){
								return '';
							}
						}
					);
					$("#idTags").on("change", function(event) {
						jQuery.ajax({
							type: "POST",
							url: labsTagsURL,
							data: "labsTags=" + $("#idTags").select2("val"),
							success: function(msg){
								jQuery("#divDisplayTags").show();
								jQuery("#divDisplayTags > i").html(' : ' + new String($("#idTags").select2("val")).replace(new RegExp("(,)", "g"), ', '));
								jQuery("#divDisplayTags").hide();
							},
							error: function(msg){
								alert('Mots clés non sauvegardés!');
								document.location.href=path;
							}
						});
					});
				});
			</script>
		</div>
		<div id="divDisplayTags" class="container-fluid<#if canWrite> viewblock</#if> ${listTags?size < 1}">
			<i class="icon-tags">&nbsp;:&nbsp;<#list listTags as tag>${tag}<#if (listTags?last != tag)>, </#if></#list></i>
		</div>
	<#else>
		<#if (listTags?size > 0)>
			<div id="divDisplayTags" class="container-fluid">
				<i class="icon-tags">&nbsp;:&nbsp;<#list listTags as tag>${tag}<#if (listTags?last != tag)>, </#if></#list></i>
			</div>
		</#if>
	</#if>
	
</#if>
