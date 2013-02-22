package com.leroymerlin.common.freemarker;

import java.util.List;

import org.jsoup.Jsoup;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class HtmlToText implements TemplateMethodModel {

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if(arguments.size() > 1) {
            throw new TemplateModelException("htmlToText takes only one parameter");
        }

        String html = (String) arguments.get(0);
        return Jsoup.parse(html).text();
    }

}
