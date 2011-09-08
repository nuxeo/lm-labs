    <div class="topbar-wrapper" style="z-index: 5;">            
    <div class="topbar">
    	<div class="topbar-inner">      
        <div class="container">
          <h3><a href="#"><#if site??>${site.title}</#if></a></h3>
          
          
          <ul class="nav secondary-nav">
            <li>
          	  <form>
            	<input class="normal" placeholder="Rechercher dans le site" name="q"/>
          	  </form>
            </li>
            <li><#include "common/login.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>           
            <li class="dropdown	">
              <a href="#" class="dropdown-toggle">${Context.principal.firstName} ${Context.principal.lastName}</a>
              <ul class="dropdown-menu">
                <@block name="docactions"></@block>
                <@block name="siteactions"></@block>                                
                <li><a id="logout" href="#">Déconnexion</a></li>
              </ul>
            </li>
            </#if>
          </ul>
        </div>
      </div><!-- /fill -->
    </div><!-- /topbar -->
    </div><!-- /topbar-wrapper -->
    &nbsp;