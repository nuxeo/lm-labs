package com.leroymerlin.common.core.providers;

import java.util.Iterator;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelIterator;

public class SimpleDocumentModelIterator implements DocumentModelIterator {

    private static final long serialVersionUID = 3742039011948504441L;

    protected final Iterator<DocumentModel> iterator;
    protected final List<DocumentModel> list;


    public SimpleDocumentModelIterator(List<DocumentModel> list) {
        iterator = list.iterator();
        this.list = list;
    }

    @Override
    public long size() {
        return list.size();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public DocumentModel next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public Iterator<DocumentModel> iterator() {
        return iterator;
    }

}
