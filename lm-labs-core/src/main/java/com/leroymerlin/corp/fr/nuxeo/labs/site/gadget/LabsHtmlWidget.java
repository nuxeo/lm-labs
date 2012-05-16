package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;

public class LabsHtmlWidget implements LabsWidget {

    private String name;

    public LabsHtmlWidget(String name) {
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setType(WidgetType type) {
        // ignored
    }

    @Override
    public WidgetType getType() {
        return WidgetType.HTML;
    }

}
