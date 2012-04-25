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
	 	<div style="width: 100%; text-align: right; margin-bottom: 5px;">
	  		<a style="margin-right: 5px;" class="btn" href="${This.path}/@views/addtopic?props=open"><i class="icon-plus"></i>Ajouter un topic</a>
		</div>
		<#if 0 < allTopics?size>
  			<div class="hero-unit" style="margin-bottom: 5px;">
			  <center><h4 id="titleCommentsPage">Liste des topics :</h4></center>
			  <table class="table table-striped bs table-bordered labstable" >
			  	<thead>
	          		<tr>
	            		<th width="25%">Sujet</th>
			            <th width="20%"><center>Auteur</center></th>
			            <th width="10%">Réponses</th>
			            <#if (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite') || Context.principal.name == topic.document.author)>
				            <th width="37%">Dernier message</th>
				            <th width="8%"></th>
				        <#else>
				       		<th width="45%">Dernier message</th>
				        </#if>
	          		</tr>
		        </thead>
			  	
				<#list allTopics as topic>
				  	<tr>
				  		<td>
				  			<a href="${This.path}/${topic.title}">
					  			<strong>${topic.title}</strong><br/>
							  	${topic.description}
							</a>
				  		</td>
				  		<td>
				  			<center>
					  			<#if topic.document.author != 'Administrator'>
					  				<span><img width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/${topic.document.author}/picture"></span><br/>
					  			<#else>
					  				<span><img width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/10060732/picture"></span><br/>
					  			</#if>
								<span>${This.getFullName(topic.document.author)}</span>
							</center>
				  		</td>
				  		<td>
				  			<center>${topic.nbComments}</center>
				  		</td>
				  		<td>
				  			<#if 0 <  topic.comments?size>
					  			${topic.comments?last.comment.author} (${topic.comments?last.comment.creationDate}) :<br/>
								${topic.comments?last.comment.text}
							</#if>
				  		</td>
				  		<#if (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite') || Context.principal.name == topic.document.author)>
					  		<td>
							  	<div class=" editblock btn-group" style="float: right;">
							      	<a class="btn dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i><span class="caret"></span></a>
									<ul class="dropdown-menu" style="left: auto;right: 0px;">
										<li>
											<a href="${This.path}/${topic.title}/@modify"><i class="icon-edit"></i>Editer</a>
											<a href="#"><i class="icon-eye-close"></i>Masquer</a>
										</li>
									</ul>
								</div>
					  		</td>
					  	</#if>
					</tr>
				</#list>
			  </table>
			  <div style="width: 100%; text-align: right; margin-bottom: 5px;">
			  	<a style="margin-right: 5px;" class="btn" href="${This.path}/@views/addtopic?props=open"><i class="icon-plus"></i>Ajouter un topic</a>
			  </div>
		  </div>
	  	</#if>
	</div>	 	
  </@block>
</@extends>