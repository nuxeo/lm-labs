<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">

	<@block name="title">${Common.siteDoc(Document).getSite().title}-${This.document.title}</@block>

  <@block name="scripts">
      <@superBlock/>
      <meta name="gwt:property" content="locale=fr">
      <link rel="search" type="application/opensearchdescription+xml" title="Intralm" href="${skinPath}/searchIntralm.xml">

        <script type="text/javascript">
    jQuery(document).ready(function() {
      jQuery("#addGadgetButton").fancybox({
        'width'				: '75%',
        'height'			: '75%',
        'autoScale'			: true,
        'transitionIn'		: 'none',
        'transitionOut'		: 'none',
        'type'				: 'iframe',
        'enableEscapeButton': true,
        'centerOnScroll': true
      });
    });

    function addGadget(name, url) {
      jQuery.fancybox.close();
      nuxeo.container.addGadget('wcopensocial', [{WC_GADGET_DEF_URL:url}, {WC_GADGET_NAME:name}]);
    }


          var nuxeo = {
            baseURL:"${This.baseUrl}",
            container: {
              repositoryName: "${Session.repositoryName}",
              id:"${Document.id}",
              debug:false,
              builder: {
                width:false,
                sidebar:true,
                header:true,
                footer:true
              },
              parameters: {
                showPreferencesAfterAddingGadget: false,
                userLanguage: "fr"
              }
            }
        };
      </script>

      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/reset-fonts-grids.css">
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/container.css">
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/gadgets.css">

      <script type="text/javascript" language="javascript" src="${Context.basePath}/gwt-container/gwtContainer/gwtContainer.nocache.js"></script>
      <script type="text/javascript" language="javascript" src="/nuxeo/opensocial/gadgets/js/rpc:navigateto.js?c=1"></script>
    </@block>


  <@block name="docactions">
      <@superBlock/>
      <#if Session.hasPermission(This.document.ref, "Write")>
        <li><a href="#" onclick="nuxeo.container.openContainerBuilder();return false;">Mise en page</a></li>
        <li><a id="addGadgetButton" href="${Context.basePath}/gadgets?language=fr" >Ajouter un gadget</a></li>
        <li><a id="addGadgetWcPictureButton" href="#" onclick="nuxeo.container.addGadget('wcpicture', undefined);" >Ajouter un gadget Image</a></li>
        <li><a id="addGadgetWcHtmlButton" href="#" onclick="nuxeo.container.addGadget('wchtml', undefined);" >Ajouter un gadget Texte Riche</a></li>
      </#if>
  </@block>

  <@block name="content">
  	<#include "views/common/page_header.ftl">
    <div>
        <div id="contentContainer" style="min-height:300px;height:auto !important;height:300px;">
          <div id="content">
                <div id="gwtContainerDiv"></div>
          </div>
        </div>
    </div>
  </@block>

</@extends>

