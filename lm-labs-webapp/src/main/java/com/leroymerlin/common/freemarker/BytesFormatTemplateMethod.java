package com.leroymerlin.common.freemarker;

import java.util.List;

import com.leroymerlin.common.core.utils.BytesFormatUtils;

import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class BytesFormatTemplateMethod implements TemplateMethodModelEx {

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() > 3) {
            throw new TemplateModelException(
                    "BytesFormat takes only three parameters !");
        }
        long size = ((SimpleNumber) arguments.get(0)).getAsNumber().longValue();
        // "", "K", "M", "G"
        String format = ((SimpleScalar) arguments.get(1)).getAsString();
        // "fr", "en", ...
        String language = ((SimpleScalar) arguments.get(2)).getAsString();
        return BytesFormatUtils.BytesInWord(size, format, language);
    }

}
