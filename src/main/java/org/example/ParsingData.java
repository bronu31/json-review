package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ParsingData {

    public void parsingANdCalculating(JsonObject parser){
        LinkedList<Integer> prices= new LinkedList<>();
        HashMap<String, Integer> speed= new HashMap<>();
        Set<String> carriersNames= new HashSet<>();
        JsonObject obj;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");
        int flightTIme;

        for (JsonElement project : parser.get("tickets").getAsJsonArray()) {
            obj = project.getAsJsonObject();
            //must be calculated everytime anyway so better to store it as an variable
            if (!obj.get("origin").getAsString().equals("VVO")||
                    !obj.get("destination").getAsString().equals("TLV")) {
                continue;}
            flightTIme=calculate_difference_in_date(
                    LocalDateTime.parse(obj.get("arrival_date").getAsString()
                            +" "+obj.get("arrival_time").getAsString(), formatter),
                    LocalDateTime .parse(obj.get("departure_date").getAsString()
                            +" "+obj.get("departure_time").getAsString(), formatter));

            // check if carrier already stored in hashmap
            if(speed.containsKey(obj.get("carrier").getAsString())){
                //if yes check if old flight time greater than the new one
                if(speed.get(obj.get("carrier").getAsString())>flightTIme){
                    //if yes then replace
                    speed.put(obj.get("carrier").getAsString(),flightTIme);
                }
            }
            else {
                // if no then add carrier and time
                speed.put(obj.get("carrier").getAsString(), flightTIme);
                carriersNames.add(obj.get("carrier").getAsString());
            }

            prices.add(obj.get("price").getAsInt());
        }

        for (String string : carriersNames){
            System.out.printf("Fastest travel time for carrier %s is %d \n",string, speed.get(string));
        }

        System.out.printf("Difference between median and average is %d",cost_difference(prices));

    }

    public static int cost_difference(LinkedList<Integer> prices){
        int sum=0;
        Collections.sort(prices);
        for(int i : prices){
            sum+=i;
        }
        if (prices.size()%2==0){
            return sum/prices.size()-((prices.get(prices.size()/2)+ prices.get(prices.size()/2+1))/2);
        }
        return sum/prices.size()-(prices.get(prices.size()/2));
    }

    //calculating  flight time in minutes and in theory there is no flight path longer then 1 day
    //allows much easier comparison
    public static int calculate_difference_in_date(LocalDateTime past, LocalDateTime future){

        return (int) future.until(past, ChronoUnit.MINUTES);
    }
}
