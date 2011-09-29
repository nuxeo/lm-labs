<@extends src="/views/labs-base.ftl">
  <@block name="scripts">
      <@superBlock/>
      <meta name="gwt:property" content="locale=fr">
      <link rel="search" type="application/opensearchdescription+xml" title="Intralm" href="${skinPath}/searchIntralm.xml">
      <script>
          var nuxeo = {
            baseURL:"${This.baseUrl}",
            container: {
              repositoryName: "default",
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

      <script type="text/javascript" language="javascript" src="/nuxeo/opensocial/gadgets/js/rpc:pubsub:lmselectvalue.js?c=1"></script>
      <script type="text/javascript" language="javascript" src="${Context.basePath}/gwt-container/gwtContainer/gwtContainer.nocache.js"></script>
    </@block>

  <@block name="content">
    <div>
        <div id="contentContainer" style="min-height:300px;height:auto !important;height:300px;">
          <a href="#" onclick="nuxeo.container.openContainerBuilder();return false;" class="btn">Mise en page</a>
          <a href="#" onclick="nuxeo.container.addGadget('wchtml',undefined);return false;" class="btn">Ajouter HTML</a>
          <a href="#" onclick="nuxeo.container.addGadget('wcpicture',undefined);return false;" class="btn">Ajouter Image</a>

          <div id="content">
                <div id="gwtContainerDiv"></div>
          </div>
        </div>
    </div>
  </@block>

</@extends>

