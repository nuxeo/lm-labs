<#if site?? && site.piwikEnabled >
<!-- Piwik -->
<script type="text/javascript">
var pkBaseURL = (("https:" == document.location.protocol) ? "https://piwik.cocfr2.fr.corp.leroymerlin.com/" : "http://piwik.cocfr2.fr.corp.leroymerlin.com/");
document.write(unescape("%3Cscript src='" + pkBaseURL + "piwik.js' type='text/javascript'%3E%3C/script%3E"));
</script><script type="text/javascript">
try {
var piwikTracker = Piwik.getTracker(pkBaseURL + "piwik.php", ${site.piwikId});
piwikTracker.trackPageView();
piwikTracker.enableLinkTracking();
} catch( err ) {}
</script>
<!-- End Piwik Tag -->
</#if>