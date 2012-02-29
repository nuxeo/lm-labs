<#-- genere le contenu entier de la news -->
<#macro generateContentHtmlNews news>
	<#list news.rows as row>
	    <div class="row-fluid" id="row_s${news_index}_r${row_index}">
	      <#list row.contents as content>
	        <div class="span${content.colNumber} columns">
	          <#if content.html == "">
	            &nbsp;
	          <#else>
	            ${content.html}
	          </#if>
	
	        </div>
	      </#list>
	    </div>
	  </#list>
</#macro>

<#-- genere l'entete de la news -->
<#macro generateHeaderNews news path withHref withBy>
	<h2 style="line-height: 24px;">
		<#if withHref>
    		<a href="${path}">${news.title}</a>
    	<#else>
    		${news.title}
    	</#if>
    	<#if withBy>
    		<small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small>
    	</#if>
    </h2>
   	<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
</#macro>

<#-- genere l'image du résumé de la news -->
<#macro generateSummaryPictureNews path>
	<img src="${path}/summaryPictureTruncated" style="margin-top: 5px;"/>
</#macro>

<#-- genere le contenu tronqué dans le résumé de la news -->
<#macro generateHeaderNewsEllipsis news path withHref withBy>
	<@generateHeaderNews news=news path=path withHref=withHref withBy=false/>
	<#if (news.accroche?trim?length < 1)>
		<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">
			<@generateContentHtmlNews news=news />
		</div>
	<#else>
		${news.accroche?replace("\n", "<br />")}
	</#if>
</#macro>

<#-- genere le résumé de la news -->
<#macro generateSummaryNews news path withHref>
	<#if news.hasSummaryPicture()>
      		<#-- Image -->
      		<div class="span2">
      			<@generateSummaryPictureNews path=path/>
      		</div>
      		<#-- Central -->
      		<div class="span9">
      			<@generateHeaderNewsEllipsis news=news path=path withHref=withHref withBy=false/>
      		</div>
  	<#else>
  		<#-- Central -->
  		<div class="span11">
  			<@generateHeaderNewsEllipsis news=news path=path withHref=withHref withBy=false/>
  		</div>
  	</#if>
</#macro>