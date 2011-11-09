<#macro addFileDialog action onSubmit>
	<div id="addFileDialog">
	    <h1>Ajouter un Fichier</h1>
	   	<form id="addFileForm" action="${action}" onSubmit="${onSubmit}" method="post" enctype="multipart/form-data">
	      <fieldset>
	        <div class="clearfix">
	                <label for="title">Choisir le fichier</label>
	                  <div class="input">
	                    <input type="file" name="file"/>
	                  </div>
	                </div><!-- /clearfix -->
	
	                <div class="clearfix">
	                <label for="description">Description</label>
	                  <div class="input">
	                    <textarea name="description"></textarea>
	                  </div>
	                </div><!-- /clearfix -->
	      </fieldset>
	      <div class="actions">
	        <button type="submit" class="btn primary">Ajouter</button>
	      </div>
	  </form>
	</div>
</#macro>