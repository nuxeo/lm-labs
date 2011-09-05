<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script> 
        <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
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
       			<form class="form-stacked" action="${This.path}" method="post">
       			<fieldset>
                <div class="clearfix">
                <label for="title">Contenu</label>
                  <div class="input-stacked">
                    <textarea name="content">${content.html}</textarea>
                  </div>
            
                </div><!-- /clearfix -->
                <div class="action">
                	<input type="submit" class="btn primary" value="Modifier"/>
                </div>
                </fieldset>
       			</form>
       		</div>
       	</div>
	   </div>
	</@block>
</@extends>	