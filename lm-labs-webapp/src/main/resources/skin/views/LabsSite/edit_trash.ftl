<@extends src="/views/labs-base.ftl">

<@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
    <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
		  jQuery("table[class*='zebra-striped']").tablesorter({
		      sortList: [[4,0]],
		      textExtraction: function(node) {
		            // extract data from markup and return it
		        var sortValues = jQuery(node).find('span[class=sortValue]');
		        if (sortValues.length > 0) {
		          return sortValues[0].innerHTML;
		        }
		            return node.innerHTML;
		        }
		  });
		});
	</script>
  </@block>
  
<@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
 </@block>

  <@block name="breadcrumbs">
    <a href="${This.getPath()}">${site.title}</a> > Administration
  </@block>


  <@block name="content">
    <div class="container">
      <ul class="pills">
        <li><a href="${This.path}/@views/edit">Général</a></li>
        <li><a href="${This.path}/theme/${site.siteThemeManager.theme.name}">Thème</a></li>
        <li><a href="${This.path}/@views/edit_perms">Permissions</a></li>
        <li class="active"><a href="#">Poubelle</a></li>
      </ul>

      <section>
        <#assign deletedPages = This.getDeletedPage() />
        <div class="page-header">
          <h3>${Context.getMessage('label.lifeCycle.trash.title')} (${deletedPages?size})</h3>
        </div>
        
        <table class="zebra-striped bs">
            <thead>
            <tr>
              <th>${Context.getMessage('label.lifeCycle.trash.page')}</th>
              <th>${Context.getMessage('label.lifeCycle.trash.createdBy')}</th>
              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
              <th>${Context.getMessage('label.lifeCycle.trash.lastModifiedBy')}</th>
              <th>${Context.getMessage('label.lifeCycle.trash.the')}</th>
              <th>&nbsp;</th>
            </tr>
          </thead>
          <tbody>
          <#list deletedPages as deletedPage>
            <tr>
              <#assign doc=deletedPage.document />
              <td>
              	<a href="#" rel="popover" data-content="${deletedPage.description}" data-original-title="${Context.getMessage('label.description')}">${deletedPage.title} (${deletedPage.document.type})</a>
              </td>
              <td>${userFullName(doc.dublincore.creator)}</td>
              <td>
                ${doc.dublincore.created?string.medium}
                <span class="sortValue">${doc.dublincore.created?string("yyyyMMddHHmmss")}</span>
              </td>
              <#assign modified=doc.dublincore.modified/>
              <td>${userFullName(doc.dublincore.lastContributor)}</td>
              <td>
                ${modified?string.medium}
                <span class="sortValue">${modified?string("yyyyMMddHHmmss")}</span>
              </td>
              <td>
              	<a  href="#" class="btn" onclick="javascript:undeletePage('${deletedPage.document.ref}');">${Context.getMessage('command.docactions.undelete')}</a>
              </td>
            </tr>
          </#list>
          </tbody>
        </table>
      </section>
    </div>
    <script type="text/javascript">
		function undeletePage(ref){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouUndelete')}")){
    			jQuery.ajax({
					type: 'GET',
				    async: false,
				    url: '${This.path}/@labspublish/undelete/' + ref,
				    success: function(data) {
				    	if (data == 'undelete') {
				          alert("${Context.getMessage('label.lifeCycle.page.hasUndeleted')}");
				          document.location.href = '${This.path}';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotUndeleted')}");
				        }
				    }
				});
			}
		}
		
		$(function () {
				$("a[rel=popover]")
					.popover({offset: 10})
					.click(
						function(e) {e.preventDefault()}
					)
			}
		) 
	</script>
  </@block>
</@extends>