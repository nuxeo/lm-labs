<#if Context.principal.isAnonymous() == false>
	<div id="notification" >
		<#if Common.getNotifiableTypes()?seq_contains(Document.type) >
			<a id="subscribeBt" <#if This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(true);">
				<button class="btn primary">
				<#if Document.type == "PageNews">
					 ${Context.getMessage('command.contextmenu.PageNews.subscribe')}
				<#else>
					${Context.getMessage('command.contextmenu.Page.subscribe')}
				</#if>
				</button>
			</a>
			<a id="unsubscribeBt" <#if !This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(false);">
				<button class="btn primary">
				<#if Document.type == "PageNews">
					${Context.getMessage('command.contextmenu.PageNews.unsubscribe')}
				<#else>
					${Context.getMessage('command.contextmenu.Page.unsubscribe')}
				</#if>
				</button>
			</a>
			<script type="text/javascript">	
				function subscribePage(subscribe) {
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