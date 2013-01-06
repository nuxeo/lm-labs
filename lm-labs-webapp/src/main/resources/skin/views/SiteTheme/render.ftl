@import "../theme/${This.theme.name}/specific.less";

${styleProperties}

<#if ! withoutAddedStyle>
  ${This.theme.style}
</#if>

@FontAwesomePath: '${skinPath}/less/font';
@skinPath: '${skinPath}';