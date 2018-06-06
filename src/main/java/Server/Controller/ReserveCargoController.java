package Server.Controller;

import java.util.HashMap;

public class ReserveCargoController {
    private HashMap<String, Integer> reservedCargo = new HashMap<>();

    public void addReservedCargo(String username, int amount) {
        synchronized (reservedCargo) {
            reservedCargo.merge(username, amount, (oldValue, value) -> oldValue + value);
        }
    }

    public void removeReservedCargo(String username, int amount) {
        synchronized (reservedCargo) {
            Integer value = reservedCargo.get(username);
            if (value == null) return;
            if (value - amount <= 0) reservedCargo.remove(username);
            else reservedCargo.put(username, value - amount);
        }

    }

    public int getReservedCargo(String username) {
        Integer value = reservedCargo.get(username);
        if(value == null) return 0;
        else return value;
    }
}
