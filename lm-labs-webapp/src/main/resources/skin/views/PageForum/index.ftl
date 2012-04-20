<#-- assign canWrite = Session.hasPermission(Document.ref, 'Write') -->
<#assign canWrite = This.page?? && This.page.isContributor(Context.principal.name)>
<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).site />
  <@block name="title">${ mySite.title}-${This.document.title}</@block>

  <@block name="css">
    <@superBlock/>
     
  </@block>

  <@block name="scripts">
    <@superBlock/>
     
  </@block>

  <#assign allTopics = This.getLabsForum().getTopics(Session)/>
  <@block name="content">
  	<div id="content" class="container-fluid">
  		<#include "views/common/page_header.ftl">
  	  <#if 0 < allTopics?size>
  	  		<div class="hero-unit" style="margin-bottom: 5px;">
			  <center><h4 id="titleCommentsPage">Liste des topics :</h4></center>
			  <table class="table table-striped">
			  	<thead>
	          		<tr>
	            		<th width="25%">Auteur</th>
			            <th width="60%">Topic</th>
			            <th width="15%">Actions</th>
	          		</tr>
		        </thead>
			  	
				<#list allTopics as topic>
				  	<tr>
				  		<td colspan="3">
					  		<div class="row-fluid">
								<div class="span3">
										<span><img src="http://intralm2.fr.corp.leroymerlin.com/contact/id/${This.document.author}/picture"></span><br/>
										<span>${This.document.author}</span><br/>
							  			Nb de réponses : ${topic.nbComments}
							  	</div>
						  		<div class="span7">
						  			<strong>${topic.title}</strong><br/>
						  			${topic.description}
						  		</div>
						  		<div class="span2">
									  <a style="margin-right: 5px;" class="btn" href="${This.path}/${topic.title}">Voir topic</a>
						  		</div>
							</div>
							
							<#if 0 <  topic.comments?size>
								<div class="row-fluid">
									<div class="span3">&nbsp;
									</div>
									<div class="span9">
										Dernier post de ${topic.comments?last.comment.author} (${topic.comments?last.comment.creationDate}) :<br/>
										${topic.comments?last.comment.text}
									</div>
								</div>
							</#if>
						</td>
				  	</tr>
				</#list>
			  </table>
		  </div>
	  </#if>
	  <div style="width: 100%; text-align: right; margin-bottom: 5px;">
	  	<a style="margin-right: 5px;" class="btn" href="${This.path}/@views/addtopic?props=open"><i class="icon-plus"></i>Ajouter un topic</a>
	  </div>
	</div>	 	
  </@block>
</@extends>