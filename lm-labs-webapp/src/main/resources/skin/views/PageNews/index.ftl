<#assign mySite=Common.siteDoc(Document).site />
<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign isAuthorized = This.page?? && This.page.isContributor(Context.principal.name)>

  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <#include "views/common/datepicker_css.ftl">
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>

  <@block name="content">
  	<#include "views/LabsNews/macroNews.ftl">
      <div id="content" class="container-fluid">

    	<#include "views/common/page_header.ftl">
    	
        <#if isAuthorized>
        	<div class="editblock" style="width: 100%;text-align: right;margin-bottom: 5px;margin-top: 5px;">
          		<a href="${This.path}/@views/addnews?props=open" class="btn" style="margin-right: 5px;"><i class="icon-plus"></i>${Context.getMessage('label.labsNews.add.news')}</a>
          	</div>
        </#if>

        <#list pageNews.allNews as news>
          <#assign path=This.path + "/" + news.documentModel.name />
          <section class="labsnews">
	          	<div class="row-fluid" id="summaryNews${news.documentModel.ref}">
	          		<@generateSummaryNews news=news path=path withHref=true/>
				  	<#-- Collapse -->
					<div class="span1" style="margin-left: 15px;float: right;">
						<img src="${skinPath}/images/newsOpen.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:openNews('${news.documentModel.ref}', '${skinPath}');"/>
					</div>
	          	</div>
	          	
	          	<div id="contentNews${news.documentModel.ref}" style="display: none;">
	              <div class="row-fluid">
	              	 <#if news.hasSummaryPicture()>
			          		<#-- Image -->
			          		<div class="span2">
			          			<img src="${This.path}/${news.documentModel.name}/summaryPictureTruncated" style="margin-top: 5px;"/>
			          		</div>
			          		<#-- Central -->
			          		<div class="span9">
			          			<@generateHeaderNews news=news path=path withHref=true withBy=true />
				            </div>
		          	<#else>
		              	<div class="span11">
			                <@generateHeaderNews news=news path=path withHref=true withBy=true />
			            </div>
			        </#if>
	                <div class="span1" style="margin-left: 15px;float: right;">
	                	<img src="${skinPath}/images/newsClose.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:closeNews('${news.documentModel.ref}', '${skinPath}');"/>
	                </div>
	              </div>
	
				  <div id="contentContentNews${news.documentModel.ref}">
		              <@generateContentHtmlNews news=news />
          		  </div>
	          	</div>
          
          
            </section>
        </#list>
        <hr />
        <a href="${This.path}/@labsrss/topnews" title="${Context.getMessage('tooltip.PageNews.rss')}<#if !mySite.visible> ${Context.getMessage('tooltip.PageNews.rss.siteNotPublish')}</#if>" target="_blank"><img src="${skinPath}/images/iconRss.gif"/></a>
      </div>
  </@block>
</@extends>

