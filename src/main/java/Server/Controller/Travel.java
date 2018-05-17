package Server.Controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Travel implements Runnable {
    private HashMap<String, Integer> map = new HashMap<String, Integer>();// username, time remaining

    public void run() {
        while(true) {
            long start = System.currentTimeMillis();
            long diff = System.currentTimeMillis() - start;
            while(diff < 1000) {
                diff = System.currentTimeMillis() - start;
            }
            start = System.currentTimeMillis();
            Iterator it = map.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                pair.setValue(Math.max((int)(((Integer) pair.getValue()) - (diff / 1000)), 0));// only seconds
            }
        }
    }

    public void addTrain(String username, int ETA) {
        map.put(username, ETA);
    }

    public Integer getETA(String username) {
        return map.get(username);
    }
}
