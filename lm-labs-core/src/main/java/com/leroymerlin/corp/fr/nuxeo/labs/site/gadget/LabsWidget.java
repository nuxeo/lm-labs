package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;

public interface LabsWidget {

    void setName(String name);

    String getName();

    void setType(WidgetType type);

    WidgetType getType();
}
