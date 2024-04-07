package com.ocado.basket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {

    /*
    * Parametrized Test for 2 example baskets checking whether number of
    * items before split is equal to number of items after split.
    */
    @ParameterizedTest
    @ValueSource(strings = {"basket-1.json", "basket-2.json"})
    @DisplayName("Number of items before split should equal number of items after split")
    void numberOfInputItemsEqualsNumberOfOutputItems(String path){
        // create new BasketSplitter using config.json file from main dir
        BasketSplitter basketSplitter =new BasketSplitter(System.getProperty("user.dir") + "/config.json");

        // create List of items using basket json file from main dir
        List<String> items = BasketSplitter.readItemsFromJSON(System.getProperty("user.dir") + "/" + path);

        // number of items is equal to List size
        int inputSize = items.size();

        // number of items after split will be counted from 0
        int outputSize = 0;

        // splitting the basket
        Map<String, List<String>> output = basketSplitter.split(items);

        // we sum up items from every delivery method category
        for(Map.Entry<String, List<String>> entry : output.entrySet())
            outputSize += entry.getValue().size();

        assertEquals(inputSize, outputSize);
    }

    /*
    * Test checking if elements in map are placed in a
    * way so that their values are in descending order
    */
    @Test
    @DisplayName("Values in map should be sorted in descending order")
    void valuesInMapShouldBeSortedInDescendingOrder(){
        // create Map<String, Integer> which elements values are in ascending order
        Map<String, Integer> testMap = new LinkedHashMap<>();
        testMap.put("val 1", 1);
        testMap.put("val 2", 2);
        testMap.put("val 3", 3);
        testMap.put("val 4", 4);
        testMap.put("val 5", 5);
        testMap.put("val 6", 6);
        testMap.put("val 7", 7);
        testMap.put("val 8", 8);
        testMap.put("val 9", 9);
        testMap.put("val 10", 10);

        // sort map by elements values
        var sortedTestMap = new BasketSplitter(System.getProperty("user.dir") + "/config.json").sortMapByValues(testMap);

        // create list of Map.Entry
        List<Map.Entry<String, Integer>> tempList = new ArrayList<>(sortedTestMap.entrySet());

        for(int i = 0; i < sortedTestMap.size() - 1; ++i){
            // if any pair of the two following elements is in ascending order throw Exception
            if(tempList.get(i).getValue() < tempList.get(i+1).getValue())
                throw new RuntimeException();
        }
    }
}