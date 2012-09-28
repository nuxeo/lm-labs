<#assign gadgetName = "dispoappli" />
<#assign specUrl = Common.getExternalOpensocilaGadgetSpecUrl(gadgetName) />
<#assign displayMode = "small" />
<#assign numberOfLine = "all" />
<#assign textColor = "white" />
<#assign backColor = "#205042" />
<#if 0 < specUrl?length >
<div id="sidebar-gadget-${gadgetName}" class="opensocialGadgets gadget-${gadgetName} bloc" style="display: block;"
	data-gadget-title="${gadgetName}"
	data-gadget-specurl="${specUrl}"
	data-gadget-user-preferences="{'displayMode':{name:'displayMode',value:'${displayMode}',default:'wide'},'numberOfLine':{name:'numberOfLine',value:'${numberOfLine}',default:''},'textColor':{name:'textColor',value:'${textColor}',default:''},'backColor':{name:'backColor',value:'${backColor}',default:''}}"
>
</div>
</#if>