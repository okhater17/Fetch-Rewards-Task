package edu.okhaterfandm.fetchtask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
public class ExpandListData {
    //Custom comparator to sort item names and list ids
    public static class CustomComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String[] arrOfStr1 = s1.split(" ");
            String[] arrOfStr2 = s2.split(" ");
            return Integer.valueOf(arrOfStr1[1]).compareTo(Integer.valueOf(arrOfStr2[1]));
        }
    }
    //Static method to assign item names to corresponding IDs
    public static HashMap<String, ArrayList<String>> returnData(JSONArray jsonArr) {
        HashMap<String, ArrayList<String>> expandableListData = new HashMap<>();
        try {
            for(int i = 0; i < jsonArr.length(); i++){
                //Get JSONobject at the ith index and get the name and listID
                JSONObject subObject = jsonArr.getJSONObject(i);
                ArrayList<String> list = new ArrayList<>();
                String itemName = subObject.getString("name");
                String listID = "listID " + subObject.getInt("listId");
                //If it's empty or null, continue
                if(subObject.getString("name").equals("") || subObject.isNull("name")){
                    continue;
                }
                //If not first item name with this ID then get the list that is already in there and update it
                if (expandableListData.containsKey(listID)) {
                    list = expandableListData.get(listID);
                }
                list.add(itemName);
                //Sort the list of item names
                Collections.sort(list, new CustomComparator());
                expandableListData.put(listID, list);

            }
            return expandableListData;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return expandableListData;
    }
}
