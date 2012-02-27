    <#-- NOTIFICATION AREA --> 
    <#include "views/common/notification_area.ftl" />
    <div style="margin-bottom:10px;"></div><#-- TEMPORAIRE : attente refonte complète graphique -->
    <#-- SITEMAP AREA --> 
    <#include "views/common/sitemap_area.ftl" />
    <#-- LAST MESSAGE AREA --> 
    <#include "views/common/last_message_area.ftl" />
    <#-- EXTERNAL URL AREA --> 
    <#include "views/common/external_url_area.ftl" />
    <#-- LATEST UPLOADS AREA --> 
    <#include "views/common/latestuploads_area.ftl" />
    <#include "views/common/toppages_area.ftl" />
    <#include "macros/children_block.ftl" />
    <@children_block parentDoc=Document title="Sous-pages" spanClass="" />
    