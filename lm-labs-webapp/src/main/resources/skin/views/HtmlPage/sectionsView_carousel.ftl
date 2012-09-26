<#include "macros/HtmlPageSection.ftl" />
<#assign idsPrefix = "carousel_" />
<div id="${idsPrefix}divSections" class="viewblock carousel slide">
<!-- Carousel items -->
	<div class="carousel-inner">
<#list sections as section>
	<@displayHtmlPageSection page=This.page section=section section_index=section_index viewMode="carousel" idsPrefix=idsPrefix />
</#list>
	</div>
<!-- Carousel nav -->
	<a class="carousel-control left" href="#${idsPrefix}divSections" data-slide="prev" style="left: 0px; top: 0;" >&lsaquo;</a>
	<a class="carousel-control right" href="#${idsPrefix}divSections" data-slide="next" style="right: 0px; top: 0;">&rsaquo;</a>
</div>
