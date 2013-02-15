<#assign width = "550" />
<#assign height = "310" />
<#assign nuxeoBaseUrl = Context.serverURL + "/nuxeo/" />
<#assign staticPreview = nuxeoBaseUrl + This.activeAdapter.getURLForStaticPreview(Document) />
<#assign mp4URL = nuxeoBaseUrl + This.activeAdapter.getTranscodedVideoURL(Document, 'MP4 480p') />
<#assign webmURL = nuxeoBaseUrl + This.activeAdapter.getTranscodedVideoURL(Document, 'WebM 480p') />
<#assign safariHTML5 = This.activeAdapter.safariHTML5 />
<#assign chromeHTML5 = This.activeAdapter.chromeHTML5 />
<#assign firefoxHTML5 = This.activeAdapter.firefoxHTML5 />
<#assign HTML5Available = (safariHTML5 || chromeHTML5 || firefoxHTML5) />

<#if (mp4URL != nuxeoBaseUrl && safariHTML5) || (webmURL != nuxeoBaseUrl && (chromeHTML5 || firefoxHTML5) ) >
<div class="video-js-box"> 
  <!-- HTML5 player -->
  <video class="video-js" width="${width}" height="${height}" controls="controls" preload="auto" poster="${staticPreview}">
      <#if mp4URL != nuxeoBaseUrl && safariHTML5>
      <source src="${mp4URL}" type="video/mp4"></source>
      <#elseif webmURL != nuxeoBaseUrl && (chromeHTML5 || firefoxHTML5) >
      <source src="${webmURL}" type="video/webm"></source>
      </#if>
      <#if mp4URL != nuxeoBaseUrl >
  <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,45,2"
      width="${width}" height="${height}" class="vjs-flash-fallback">
    <param name="movie" value="${nuxeoBaseUrl}swf/JarisFLVPlayer.swf" />
    <param name="menu" value="false"/>
    <param name="scale" value="noscale" />
    <param name="allowFullscreen" value="true"/>
    <param name="allowScriptAccess" value="always"/>
    <param name="bgcolor" value="#000000"/>
    <param name="quality" value="high"/>
    <param name="wmode" value="opaque"/>
    <param name="flashvars" value="source=${mp4URL}&amp;type=video&amp;streamtype=file&amp;controltype=0&amp;duration=&amp;poster=${staticPreview}&amp;aspectratio=&amp;autostart=false&amp;;hardwarescaling=false&amp;controls=true&amp;darkcolor=000000&amp;brightcolor=4c4c4c&amp;controlcolor=FFFFFF&amp;hovercolor=67A8C1&amp;seekcolor=D3D3D3&amp;jsapi=flase&amp;controltype=1" />
    <embed type="application/x-shockwave-flash" class="vjs-flash-fallback"
      pluginspage="http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"
      width="${width}" height="${height}"
      src="${nuxeoBaseUrl}swf/JarisFLVPlayer.swf"
      menu="false"
      scale="noscale"
      allowfullscreen="true"
      allowscriptaccess="always"
      bgcolor="#000000"
      quality="high"
      wmode="opaque"
      flashvars="source=${mp4URL}&amp;type=video&amp;streamtype=file&amp;controltype=0&amp;duration=&amp;poster=${staticPreview}&amp;aspectratio=&amp;autostart=false&amp;;hardwarescaling=false&amp;controls=true&amp;darkcolor=000000&amp;brightcolor=4c4c4c&amp;controlcolor=FFFFFF&amp;hovercolor=67A8C1&amp;seekcolor=D3D3D3&amp;jsapi=flase&amp;controltype=1"
      seamlesstabbing="false">
      <img src="${staticPreview}" height="480" alt="Poster Image"
        title="No video playback capabilities." />
    </embed>
  </object>
      </#if>
  </video>
</div>
<#elseif mp4URL != nuxeoBaseUrl >
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,45,2"
    width="${width}" height="${height}" class="vjs-flash-fallback">
  <param name="movie" value="${nuxeoBaseUrl}swf/JarisFLVPlayer.swf" />
  <param name="menu" value="false"/>
  <param name="scale" value="noscale" />
  <param name="allowFullscreen" value="true"/>
  <param name="allowScriptAccess" value="always"/>
  <param name="bgcolor" value="#000000"/>
  <param name="quality" value="high"/>
  <param name="wmode" value="opaque"/>
  <param name="flashvars" value="source=${mp4URL}&amp;type=video&amp;streamtype=file&amp;controltype=0&amp;duration=&amp;poster=${staticPreview}&amp;aspectratio=&amp;autostart=false&amp;;hardwarescaling=false&amp;controls=true&amp;darkcolor=000000&amp;brightcolor=4c4c4c&amp;controlcolor=FFFFFF&amp;hovercolor=67A8C1&amp;seekcolor=D3D3D3&amp;jsapi=flase&amp;controltype=1" />
  <embed type="application/x-shockwave-flash"
    pluginspage="http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"
    width="${width}" height="${height}"
    src="${nuxeoBaseUrl}swf/JarisFLVPlayer.swf"
    menu="false"
    scale="noscale"
    allowfullscreen="true"
    allowscriptaccess="always"
    bgcolor="#000000"
    quality="high"
    wmode="opaque"
    flashvars="source=${mp4URL}&amp;type=video&amp;streamtype=file&amp;controltype=0&amp;duration=&amp;poster=${staticPreview}&amp;aspectratio=&amp;autostart=false&amp;;hardwarescaling=false&amp;controls=true&amp;darkcolor=000000&amp;brightcolor=4c4c4c&amp;controlcolor=FFFFFF&amp;hovercolor=67A8C1&amp;seekcolor=D3D3D3&amp;jsapi=flase&amp;controltype=1"
    seamlesstabbing="false">
  </embed>
</object>
</#if>