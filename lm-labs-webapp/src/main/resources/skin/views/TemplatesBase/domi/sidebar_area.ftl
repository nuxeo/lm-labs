<#assign gadgetName = "dispoappli" />
<#assign specUrl = Common.getExternalOpensocilaGadgetSpecUrl(gadgetName) />
<#assign displayMode = "wide" />
<#assign numberOfLine = "all" />
<#assign textColor = "white" />
<#assign backColor = "black" />
<#if 0 < specUrl?length >
<div id="sidebar-gadget-${gadgetName}" class="opensocialGadgets gadget-${gadgetName} bloc" 
	data-gadget-title="${gadgetName}"
	data-gadget-specurl="${specUrl}"
	data-gadget-user-preferences="{'displayMode':{name:'displayMode',value:'${displayMode}',default:'wide'},'numberOfLine':{name:'numberOfLine',value:'${numberOfLine}',default:''},'textColor':{name:'textColor',value:'${textColor}',default:''},'backColor':{name:'backColor',value:'${backColor}',default:''}}"
>
</div>
</#if>