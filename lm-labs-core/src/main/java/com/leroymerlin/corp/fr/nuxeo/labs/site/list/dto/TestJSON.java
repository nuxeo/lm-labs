/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author fvandaele
 *
 */
public class TestJSON {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Header> listHeaders = new ArrayList<Header>();
        Header head = new Header();
        head.setName("name");
//        Map<String, String> select = new HashMap<String, String>();
//        select.put("select_0", "toto");
//        select.put("select_1", "tata");
//        head.setSelectlist(select);
        listHeaders.add(head);
        head = new Header();
        head.setName("name2");
//        select = new HashMap<String, String>();
//        select.put("select_0", "popo");
//        select.put("select_1", "papa");
//        head.setSelectlist(select);
        listHeaders.add(head);
        Gson gson = new Gson();
        String userJson = gson.toJson(listHeaders);
        System.out.println(userJson);
    }

}
