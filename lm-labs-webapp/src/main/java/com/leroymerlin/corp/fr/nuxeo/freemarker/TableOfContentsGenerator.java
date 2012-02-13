package com.leroymerlin.corp.fr.nuxeo.freemarker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableOfContentsGenerator {
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
    private final String tocHtml;
    private final Document htmlDoc;
    
    public static class Builder {
        // Required parameters
        private final String html;
        // Otional parameters - initialized to default values
        private String tag = DEFAULT_TOC_TAG;
        private String ulClass = DEFAULT_UL_CLASS;
        private String selector = DEFAULT_SELECTOR;
        private String noReplaceParentClass = DEFAULT_NO_REPLACE_CLASS;
        private final Document htmlDoc;
        
        public Builder(String html) {
            this.html = html;
            this.htmlDoc = Jsoup.parseBodyFragment(html);
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
    
    public String getToc() {
        return tocHtml;
    }
    
    private TableOfContentsGenerator(Builder builder) {
        html = builder.html;
        htmlDoc = builder.htmlDoc;
        tag = builder.tag;
        ulClass = builder.ulClass;
        selector = builder.selector;
        noReplaceParentClass = builder.noReplaceParentClass;
        tocHtml = generateToc();
        processedHtml = replaceTag();
    }

    private String generateToc() {
        Elements anchorElements = htmlDoc.select(selector);
        if (!anchorElements.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<ul class=\"").append(ulClass).append("\">");
            for (Element elem : anchorElements) {
                sb.append("<li><a href=\"#").append(elem.select("a[name]").attr("name")).append("\">").append(elem.select("h1").html()).append("</a></li>");
            }
            sb.append("</ul>");
            return sb.toString();
        }
        return "";
    }

    private String replaceTag() {
        String tocReplacement = "[[No anchors found]]";
        Document htmlDoc = Jsoup.parseBodyFragment(html);
        Elements anchorElements = htmlDoc.select(selector);
        if (!anchorElements.isEmpty()) {
            tocReplacement = tocHtml;
            String result = "";
            if (!noReplaceParentClass.isEmpty()) {
                Elements elementsWithTag = htmlDoc.select(":containsOwn(" + tag + ")");
                for (Element elem : elementsWithTag) {
                    boolean hasClass = false;
                    if (elem.hasClass(noReplaceParentClass)) {
                        hasClass = true;
                        break;
                    }
                    for (Element parent: elem.parents()) {
                        if (parent.hasClass(noReplaceParentClass)) {
                            hasClass = true;
                            break;
                        }
                    }
                    if (!hasClass) {
                        String htmlWithTag = elem.html();
                        elem.html(htmlWithTag.replace(tag, tocReplacement));
                    }
                }
                result = htmlDoc.body().html();
            } else {
                result = html.replace(tag, tocReplacement);
            }
            return result;
        }
        return html;
    }
    
}
