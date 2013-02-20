@import "../theme/${This.theme.name}/specific.less";

${styleProperties}

<#if !withoutaddedstyle >
  ${This.theme.style}
<#else>
/* withoutaddedstyle = ${withoutaddedstyle} */
/* ************************* NO THEME 'EXPERT' STYLE ************************* */
</#if>

@FontAwesomePath: '${skinPath}/less/font';
@skinPath: '${skinPath}';