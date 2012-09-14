@import "../theme/${This.theme.name}/specific.less";

${styleProperties}

<#if ! withoutAddedStyle>
  ${This.theme.style}
</#if>

@fontAwesomePath: '${skinPath}/less/font';
@skinPath: '${skinPath}';