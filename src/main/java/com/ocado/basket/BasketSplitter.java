package com.ocado.basket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import org.json.*;
import static java.util.Collections.reverseOrder;

/**
 * Class for the optimal split of items into delivery methods.
 */
public class BasketSplitter {

    /**
     * Map loaded from config file with item name as key and List of delivery method names as value.
     */
    private final Map<String, List<Object>> configMap;

    /**
     * Constructor of the class reading JSON file from given path and creating
     * configMap with item name as key and List of delivery method names as value.
     *
     * @param absolutePathToConfigFile absolute path to config file
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        // read JSON file as String
        String JSONasString = readJSONFromFile(absolutePathToConfigFile);
        // construct JSONObject from String
        JSONObject config = new JSONObject(JSONasString);

        // initialize configMap variable
        configMap = new TreeMap<>();

        // add all data to configMap
        var keys = config.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            configMap.put(key, config.getJSONArray(key).toList());
        }
    }

    /**
     * Method reading JSON file from given path.
     *
     * @param absolutePathToConfigFile absolute path to config file
     * @return JSON config file as String
     */
    static String readJSONFromFile(String absolutePathToConfigFile){
        StringBuilder temp = new StringBuilder();
        try {
            // read file using BufferedReader
            BufferedReader f = new BufferedReader(new FileReader(absolutePathToConfigFile) );
            String s;
            // while there still is next line, read it and append to StringBuilder
            while( (s = f.readLine()) != null )
                temp.append(s);
        } catch( Throwable e ) {
            System.err.println(e.getMessage());
        }

        // return String from StringBuilder
        return temp.toString();
    }

    /**
     * Method returning map based on frequency of delivery method names appearance.
     *
     * @param items List with names of items
     * @return map with delivery method name as key and number of its appearances as value
     */
    Map<String, Integer> countDeliveryFreq(List<String> items){
        // map with key String representing delivery method name
        // and corresponding value Integer representing number of
        // times delivery method appeared for items List
        Map<String, Integer> dlvMethOccNum = new LinkedHashMap<>();

        // iterating through items List
        for(String item : items){
            // List holding all available delivery methods for given item
            List<Object> deliveryMethods = configMap.get(item);

            // iterating through delivery methods List
            for(Object delMethod : deliveryMethods){
                String method = (String) delMethod;

                if(dlvMethOccNum.containsKey(method)){

                    dlvMethOccNum.put(method, dlvMethOccNum.get(method) + 1);
                }else{
                    // if delivery methods appears first time
                    // add it to map as key and set number of its appearances to 1
                    dlvMethOccNum.put(method, 1);
                }
            }
        }

        // returning  a map with the frequency of delivery methods appearance
        return dlvMethOccNum;
    }

    /**
     * Method rearranging its elements in a way that it is sorted by values.
     *
     * @param mapToSort map to be sorted by values
     * @return map sorted by values
     */
    Map<String, Integer> sortMapByValues(Map<String, Integer> mapToSort){
        // rearranging map so that it is sorted by values
        // create List of Map.Entry
        List<Map.Entry<String, Integer>> tempList = new ArrayList<>(mapToSort.entrySet());
        // sort the list in descending order
        tempList.sort(reverseOrder(Map.Entry.comparingByValue()));

        // create new map with String keys and Integer values
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        // add values from list into map in desired way
        for (Map.Entry<String, Integer> entry : tempList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // return map with the same keys and corresponding values but sorted by values
        return sortedMap;
    }

    /**
     * Method converting JSON file to List of elements from array in that file.
     *
     * @param absolutePath absolute path to JSON file with array of item names
     * @return List of item names
     */
    public static List<String> readItemsFromJSON(String absolutePath){
        // read JSON from file as String
        String itemString = readJSONFromFile(absolutePath);
        // create List of String type for items
        List<String> items = new ArrayList<>();
        // initialize JSONArray with String
        JSONArray jsonArray = new JSONArray(itemString);
        // add every element from JSONArray to items List
        for (int i=0;i<jsonArray.length();++i)
            items.add((String)jsonArray.get(i));

        return items;
    }

    /**
     * Method splitting given items in an optimal way based on config file.
     *
     * @param items List of items to split
     * @return map with delivery method name as key and List of items for given delivery method as value
     */
    public Map<String, List<String>> split(List<String> items) {
        // map with delivery methods and its frequency of appearance
        Map<String, Integer> dlvMethOccNum = countDeliveryFreq(items);

        // the same map but sorted by values (number of appearances)
        Map<String, Integer> sortedDlvMethOccNum = sortMapByValues(dlvMethOccNum);

        // map that will store results - optimally splitted basket
        Map<String, List<String>> result = new TreeMap<>();

        // iterate through sorted map
        // for every delivery method, starting with
        // the most popular, assign the available items
        for (String deliveryMethod : sortedDlvMethOccNum.keySet()) {
            // if all items are already assigned then break
            if(items.isEmpty())
                break;
            // List that will store items for particular delivery method
            List<String> itemGroup = new ArrayList<>();
            // iterate through the copy of List of items
            // using copy allows to remove item from original
            // list while still iterating through copy
            for (String item : new ArrayList<>(items)){
                // if all items are already assigned then break
                if(items.isEmpty())
                    break;
                // if delivery method is available for particular item
                // add this item to List of items for that delivery method
                if(configMap.get(item).contains(deliveryMethod)){
                    itemGroup.add(item);
                    items.remove(item);
                }
            }
            // if at least one item was added for delivery method
            // add new element to map with delivery method name as key
            // and List of items as value
            if(!itemGroup.isEmpty())
                result.put(deliveryMethod, itemGroup);
        }

        // return map holding result
        return result;
    }
}