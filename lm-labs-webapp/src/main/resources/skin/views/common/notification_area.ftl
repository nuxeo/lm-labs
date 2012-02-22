<#if Context.principal.isAnonymous() == false>
	<div id="notification" >
		<#if Common.getNotifiableTypes()?seq_contains(Document.type) >
			<a id="subscribeBt" <#if This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(true);">
				<button class="btn btn-primary">
				<#if Document.type == "PageNews">
					 ${Context.getMessage('command.contextmenu.PageNews.subscribe')}
				<#elseif Document.type == "Site">
					${Context.getMessage('command.contextmenu.Site.subscribe')}
                <#else>
                    ${Context.getMessage('command.contextmenu.Page.subscribe')}
				</#if>
				</button>
			</a>
			<a id="unsubscribeBt" <#if !This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(false);">
				<button class="btn btn-primary">
				<#if Document.type == "PageNews">
					${Context.getMessage('command.contextmenu.PageNews.unsubscribe')}
                <#elseif Document.type == "Site">
                    ${Context.getMessage('command.contextmenu.Site.unsubscribe')}
				<#else>
					${Context.getMessage('command.contextmenu.Page.unsubscribe')}
				</#if>
				</button>
			</a>
			<script type="text/javascript">	
				function subscribePage(subscribe) {
				    <#if Document.type == "Site" >
				    var confirmMsg = '';
				    if (subscribe) {
				        confirmMsg = "${Context.getMessage('command.contextmenu.Site.subscribe.confirm')}";
				    } else {
                        confirmMsg = "${Context.getMessage('command.contextmenu.Site.unsubscribe.confirm')}";
				    }
				    if (!confirm(confirmMsg)) {
				        return;
				    }
				    </#if>
					jQuery.ajax({
						type: 'GET',
					    async: false,
					    url: "${This.path}/@" + (subscribe ? 'subscribe' : 'unsubscribe'),
					    success: function(data) {
					    	if (subscribe) {
					    		jQuery('#subscribeBt').hide();
					    		jQuery('#unsubscribeBt').show();
					        }
					        else {
					    		jQuery('#subscribeBt').show();
					    		jQuery('#unsubscribeBt').hide();
					        }
					    },
					    error: function() {
				    		<#-- TODO alert
					    	console.log('subscribe failed.');
					        -->
					    }
					});
					return false;
				}
			</script>
		</#if>
	</div>
</#if>