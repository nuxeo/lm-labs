/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentRef;


/**
 * @author fvandaele
 *
 */
public class EntriesLine {
    
    private List<Entry> entries;
    
    private DocumentRef docRef;

    public EntriesLine() {
        this.entries = new ArrayList<Entry>();
        this.docRef = null;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> pEntries) {
        this.entries = pEntries;
    }
    
    public void clearEntries(){
        this.entries = new ArrayList<Entry>();
    }
    
    public void addEntry(Entry pEntry){
        this.entries.add(pEntry);
    }

    public DocumentRef getDocRef() {
        return docRef;
    }

    public void setDocRef(DocumentRef docRef) {
        this.docRef = docRef;
    }
    
    public Entry getEntryByIdHead(int pIdHead){
        for (Entry entry:this.entries){
            if (pIdHead == entry.getIdHeader()){
                return entry;
            }
        }
        return null;
    }

}
