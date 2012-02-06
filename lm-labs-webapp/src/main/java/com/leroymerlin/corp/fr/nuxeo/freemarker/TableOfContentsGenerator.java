package com.leroymerlin.corp.fr.nuxeo.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableOfContentsGenerator {
    private static final Log LOG = LogFactory.getLog(TableOfContentsGenerator.class);
    public static final String DEFAULT_UL_CLASS = "page-toc";
    public static final String DEFAULT_TOC_TAG = "[[TOC]]";
    public static final String DEFAULT_SELECTOR = "section > div.page-header";
    public static final String DEFAULT_NO_REPLACE_CLASS = "toc-noreplace";

    private final String html;
    private final String tag;
    private final String ulClass;
    private final String selector;
    private final String noReplaceParentClass;
    private final String processedHtml;
    
    public static class Builder {
        // Required parameters
        private final String html;
        // Otional parameters - initialized to default values
        private String tag = DEFAULT_TOC_TAG;
        private String ulClass = DEFAULT_UL_CLASS;
        private String selector = DEFAULT_SELECTOR;
        private String noReplaceParentClass = DEFAULT_NO_REPLACE_CLASS;
        
        public Builder(String html) {
            this.html = html;
        }
        
        public Builder tag(String val) {
            if (val != null) {
                tag = val;
            }
            return this;
        }
        
        public Builder ulClass(String val) {
            if (val != null) {
                ulClass = val;
            }
            return this;
        }
        
        public Builder selector(String val) {
            if (val != null) {
                selector = val;
            }
            return this;
        }
        
        public Builder noReplaceParentClass(String val) {
            if (val != null) {
                noReplaceParentClass = val;
            }
            return this;
        }
        
        public TableOfContentsGenerator build() {
            return new TableOfContentsGenerator(this);
        }
    }
    
    public String getHtml() {
        return processedHtml;
    }
    
    private TableOfContentsGenerator(Builder builder) {
        html = builder.html;
        tag = builder.tag;
        ulClass = builder.ulClass;
        selector = builder.selector;
        noReplaceParentClass = builder.noReplaceParentClass;
        processedHtml = replaceTag(html, tag, noReplaceParentClass);
    }

    private String replaceTag(String html, String tocTag, String noReplaceParentClass) {
        String logPrefix = "<replaceTag> ";
        // TODO Don't replace in cKeditor using NoReplaceParentClass
        String tocReplacement = "[[No anchors found]]";
        Document htmlDoc = Jsoup.parse(html);
        Elements anchorElements = htmlDoc.select(selector);
        if (!anchorElements.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<ul class=\"").append(ulClass).append("\">");
            for (Element elem : anchorElements) {
                LOG.debug(logPrefix + elem.html());
                sb.append("<li><a href=\"#").append(elem.select("a[name]").attr("name")).append("\">").append(elem.select("h1").html()).append("</a></li>");
            }
            sb.append("</ul>");
            tocReplacement = sb.toString();
            String result = "";
            if (!noReplaceParentClass.isEmpty()) {
                Elements elementsWithTag = htmlDoc.select(":containsOwn(TOC)");
                for (Element elem : elementsWithTag) {
                    boolean hasClass = false;
                    for (Element parent: elem.parents()) {
                        if (parent.hasClass(noReplaceParentClass)) {
                            hasClass = true;
                            break;
                        }
                    }
                    if (!hasClass) {
                        String htmlWithTag = elem.html();
                        elem.html(htmlWithTag.replace(tocTag, tocReplacement));
                    }
                }
                result = htmlDoc.html();
            } else {
                result = html.replace(tocTag, tocReplacement);
            }
            return result;
        }
        return html;
    }
}
