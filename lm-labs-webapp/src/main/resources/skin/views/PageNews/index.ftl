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
          <a class="btn editblock" href="${This.path}/@views/addnews">${Context.getMessage('label.labsNews.add.news')}</a>
        </#if>

        <#list pageNews.allNews as news>
          <section class="labsnews">
	          	<div class="row" id="summaryNews${news.documentModel.ref}">
	          		<#if news.hasSummaryPicture()>
	          			<div class="span11">
			          		<#-- Image -->
			          		<div class="span3">
			          			<@generateSummaryPictureNews news=news />
			          		</div>
			          		<#-- Central -->
			          		<div class="span8">
			          			<h2>${news.title}</h2>
			          			<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:3, alt_text_e:true, alt_text_t:true }">
			          				<@generateContentHtmlNews news=news />
			          			</div>
			          		</div>
			          	</div>
		          	<#else>
		          		<#-- Central -->
		          		<div class="span11">
		          			<h2>${news.title}</h2>
		          			<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:3, alt_text_e:true, alt_text_t:true }">
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
	              <div class="row">
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
	    <div class="row" id="row_s${news_index}_r${row_index}">
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
	<img src="${This.path}/${news.documentModel.name}/summaryPicture" style="width: 130px;height: 88px;margin-top: 5px;"/>
</#macro>