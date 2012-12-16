<div class="bloc pageinfo-widget" >
    <div class="header">Informations de la Page</div>
    <div class="pageinfo-content">
    <table class="table table-striped table-condensed" >
    	<tbody>
    		<tr>
    			<td>Créateur</td><td><a href="http://intralm2.fr.corp.leroymerlin.com/contact/id/${Document['dc:creator']}" >${userFullName(Document['dc:creator'])}</a></td>
    		</tr>
    		<tr>
    			<td>Création</td><td>${Document['dc:created']?datetime?string("dd-MM-yyyy HH:mm")}</td>
    		</tr>
    		<tr>
    			<td>Dernière Modification</td><td>${Document['dc:modified']?datetime?string("dd-MM-yyyy HH:mm")}</td>
    		</tr>
    		<tr>
    			<td>Contributeurs</td>
    			<td>
    			<#list Document['dc:contributors'] as contributor >
    			<a href="http://intralm2.fr.corp.leroymerlin.com/contact/id/${contributor}" >${userFullName(contributor)}</a>
    			</#list>
    			</td>
    		</tr>
    	</tbody>
    </table>
    </div>
</div>