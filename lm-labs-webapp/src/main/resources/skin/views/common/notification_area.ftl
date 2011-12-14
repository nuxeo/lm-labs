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
		</#if>
	</div>
</#if>