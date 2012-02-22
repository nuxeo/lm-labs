<#macro addFileDialog action onSubmit>
	<div id="addFileDialog">
	    <h1>Ajouter un Fichier</h1>
	   	<form class="form-horizontal" id="addFileForm" action="${action}" onSubmit="${onSubmit}" method="post" enctype="multipart/form-data">
	      <fieldset>
	        <div class="control-group">
	                <label class="control-label" for="title">Choisir le fichier</label>
	                  <div class="controls">
	                    <input type="file" name="file" class="required input-file"/>
	                  </div>
	                </div>
	
	                <div class="control-group">
	                <label class="control-label" for="description">Description</label>
	                  <div class="controls">
	                    <textarea name="description"></textarea>
	                  </div>
	                </div>
	      </fieldset>
	      <div class="actions">
	        <button type="submit" class="btn btn-primary required-fields" form-id="addFileForm">Ajouter</button>
	      </div>
	  </form>
	</div>
</#macro>