<@extends src="/views/labs-base.ftl">


  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="theme" basePath="${This.previous.path}"/>
  </@block>

  <@block name="content">
    <div class="container">

      <section>
        <div class="page-header">
          <h3>Bandeau</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}" method="post" enctype="multipart/form-data">
              <fieldset>
                <legend>Uploadez un bandeau</legend>
                <div class="clearfix">
                  <label for="banner">${Context.getMessage('label.labssites.banner.file')}</label>
                  <div class="input">
                    <input class="required" name="banner" type="file" enctype="multipart/form-data"/>
                    <span class="help-block">Utilisez une image (.jpg ou .png) de 980px de large. La hauteur du bandeau
                    s'ajuste automtiquement. </span>
                  </div>
                </div><!-- /clearfix -->

                              </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.banner.edit.download')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>



    </div>
  </@block>
</@extends>