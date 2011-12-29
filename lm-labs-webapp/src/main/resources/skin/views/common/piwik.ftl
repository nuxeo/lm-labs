<#if site?? && site.piwikEnabled >
<!-- Piwik -->
<script type="text/javascript">
var pkBaseURL = (("https:" == document.location.protocol) ? "https://piwik.cocfr2.fr.corp.leroymerlin.com/" : "http://piwik.cocfr2.fr.corp.leroymerlin.com/");
<#--
var pkBaseURL = (("https:" == document.location.protocol) ? "https://10.2.55.196/piwik/" : "http://10.2.55.196/piwik/");
-->
document.write(unescape("%3Cscript src='" + pkBaseURL + "piwik.js' type='text/javascript'%3E%3C/script%3E"));
</script><script type="text/javascript">
try {
var piwikTracker = Piwik.getTracker(pkBaseURL + "piwik.php", ${site.piwikId});
piwikTracker.trackPageView();
piwikTracker.enableLinkTracking();
} catch( err ) {}
</script>
</script><noscript><p><img src="http://localhost/piwik/piwik.php?idsite=${site.piwikId}" style="border:0" alt="" /></p></noscript>
<!-- End Piwik Tracking Code -->
</#if>
