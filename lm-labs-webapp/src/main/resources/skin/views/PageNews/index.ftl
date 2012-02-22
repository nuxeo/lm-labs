<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
    <script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>

  <@block name="content">
      <div id="content" class="">

    	<#include "views/common/page_header.ftl">
    	
        <#if isAuthorized>
        	<div class="editblock" style="width: 100%;text-align: right;margin-bottom: 5px;">
          		<a href="${This.path}/@views/addnews">${Context.getMessage('label.labsNews.add.news')}</a>
          	</div>
        </#if>

        <#list pageNews.allNews as news>
          <section class="labsnews">
	          	<div class="row-fluid" id="summaryNews${news.documentModel.ref}">
	          		<#if news.hasSummaryPicture()>
	          			<div class="span11">
			          		<#-- Image -->
			          		<div class="span3">
			          			<@generateSummaryPictureNews news=news />
			          		</div>
			          		<#-- Central -->
			          		<div class="span8">
			          			<h2 style="line-height: 24px;"><a href="${This.path}/${news.documentModel.name}">${news.title}</a></h2>
			          			<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
			          			<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">
			          				<@generateContentHtmlNews news=news />
			          			</div>
			          		</div>
			          	</div>
		          	<#else>
		          		<#-- Central -->
		          		<div class="span11">
		          			<h2 style="line-height: 24px;"><a href="${This.path}/${news.documentModel.name}">${news.title}</a></h2>
			          		<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
			          		<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">
		          				<@generateContentHtmlNews news=news />
					        </div>
		          		</div>
		          	</#if>
	          		<#-- Collapse -->
	          		<div class="span1" style="margin-left: 15px;">
	          			<img src="${skinPath}/images/newsOpen.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:openNews('${news.documentModel.ref}', '${skinPath}');"/>
	          		</div>
	          	</div>
	          	
	          	<div id="contentNews${news.documentModel.ref}" style="display: none;">
	              <div class="row-fluid">
	              	 <#if news.hasSummaryPicture()>
	              	 	<div class="span11">
			          		<#-- Image -->
			          		<div class="span3">
			          			<@generateSummaryPictureNews news=news />
			          		</div>
			          		<#-- Central -->
			          		<div class="span8">
			          			<@generateHeaderNews news=news />
				            </div>
				        </div>
		          	<#else>
		              	<div class="span11">
			                <@generateHeaderNews news=news />
			            </div>
			        </#if>
	                <div class="span1" style="margin-left: 15px;">
	                	<img src="${skinPath}/images/newsOpen.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:closeNews('${news.documentModel.ref}', '${skinPath}');"/>
	                </div>
	              </div>
	
				  <div id="contentContentNews${news.documentModel.ref}">
		              <@generateContentHtmlNews news=news />
          		  </div>
	          	</div>
          
          
            </section>
        </#list>
        <hr />
        <a href="${This.path}/@labsrss/topnews" target="_blank"><img src="${skinPath}/images/iconRss.gif"/></a>
      </div>
  </@block>
</@extends>

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

<#macro generateHeaderNews news>
	<h2 style="width: 100%">
    	<a href="${This.path}/${news.documentModel.name}">${news.title}</a> 
    	<small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small>
    </h2>
   	<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
</#macro>

<#macro generateSummaryPictureNews news>
	<img src="${This.path}/${news.documentModel.name}/summaryPictureTruncated" style="margin-top: 5px;"/>
</#macro>