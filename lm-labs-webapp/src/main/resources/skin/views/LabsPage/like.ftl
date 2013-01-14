<#assign thisPage = This.page /> 
<div class="count pull-right">
<i class="icon-spin icon-spinner" style="display:none;"></i>
<span class="badge" title="${Context.getMessage('tooltip.page.like.count', thisPage.likesCount)}" >${thisPage.likesCount}</span>
</div>
<#assign pageLiked = false />
<#if Context.principal.anonymous >
	<#if 0 < thisPage.likesCount >
		<#assign pageLiked = true />
	</#if>
<#else>
	<#assign pageLiked = thisPage.isLiked(Context.principal.name) />
</#if>
<div class="error pull-right" style="display:none;" >
	<i class="icon-warning-sign" title="${Context.getMessage('tooltip.page.like.failed')}" ></i>
</div>
<div class="action pull-right" >
<i data-serverurl= "${Context.serverURL}${Context.modulePath}" data-docid="${Document.id}" 
	class="icon-heart<#if !pageLiked >-empty</#if> pull-right <#if !Context.principal.anonymous ><#if pageLiked>dis</#if>like-action</#if>" 
	<#if !Context.principal.anonymous >
	title="<#if pageLiked >${Context.getMessage('tooltip.page.like.dislike')}<#else>${Context.getMessage('tooltip.page.like.like')}</#if>"
	</#if>
	></i>
</div>
<script>
function reloadDivLike(obj) {
    jQuery(obj).load('${This.path}/@views/like');
}
</script>
