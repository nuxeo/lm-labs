/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;


/**
 * @author fvandaele
 *
 */
public class EntriesLine {
    
    private List<Entry> entries;
    
    private DocumentModel docLine;
    
    private String userName;
    
    private int nbComments;
    
    public EntriesLine() {
        this.entries = new ArrayList<Entry>();
        this.docLine = null;
        this.nbComments = 0;
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
    
    public Entry getEntryByIdHead(int pIdHead){
        for (Entry entry:this.entries){
            if (pIdHead == entry.getIdHeader()){
                return entry;
            }
        }
        return null;
    }

    public DocumentModel getDocLine() {
        return docLine;
    }

    public void setDocLine(DocumentModel docLine) {
        this.docLine = docLine;
    }
    
    public int getNbComments(){
        return this.nbComments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNbrFiles(CoreSession session) throws ClientException, Exception{
        return docLine.getAdapter(PageListLine.class).getFiles(session).size();
    }

    public void setNbComments(int nbComments) {
        this.nbComments = nbComments;
    }
}
