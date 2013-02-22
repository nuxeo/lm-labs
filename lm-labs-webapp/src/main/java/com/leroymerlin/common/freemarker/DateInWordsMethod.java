package com.leroymerlin.common.freemarker;

import java.util.List;

import org.joda.time.DateTime;

import com.leroymerlin.common.core.utils.DistanceOfTime;

import freemarker.template.SimpleDate;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class DateInWordsMethod implements TemplateMethodModelEx{

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() > 1) {
            throw new TemplateModelException(
                    "dateInWords takes only one parameter !");
        }

        SimpleDate date = (SimpleDate) arguments.get(0);
        return DistanceOfTime.inWord(new DateTime(date.getAsDate()));


    }

}
