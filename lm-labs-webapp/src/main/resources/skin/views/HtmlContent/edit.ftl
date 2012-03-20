<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
	
	<@block name="title">${Common.siteDoc(Document).site.title}-${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script> 
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
	</@block>

	<@block name="content">	
       <div class="container">
       	<div class="row">
       		<div class="span4 columns">	       		
	       		<span class="help-block">
	        		Utilisez l'outil à droite pour éditer le contenu.
	      		</span>
       		</div>
       		
       		<div class="span12 columns">
       			<h3>Modifier le contenu</h3>
       			<form id="editcontent" action="${This.path}" method="post">
       			<fieldset>
                <div class="control-group">
                <label class="control-label" for="title">Contenu</label>
                  <div class="controls">
                    <textarea name="content">${content.html}</textarea>
                  </div>
            
                </div>
                <div class="form-actions">
                	<input type="submit" class="btn btn-primary" value="Modifier"/>
                </div>
                </fieldset>
       			</form>
       		</div>
       	</div>
	   </div>
	</@block>
</@extends>	