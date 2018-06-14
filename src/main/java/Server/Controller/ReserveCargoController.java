package Server.Controller;

import Utils.ReadWriteLock;

import java.util.HashMap;

public class ReserveCargoController {
    private HashMap<String, Integer> reservedCargo = new HashMap<>();
    private ReadWriteLock lock = new ReadWriteLock();

    public void addReservedCargo(String username, int amount) {
        try {
            lock.lockWrite();
            reservedCargo.merge(username, amount, (oldValue, value) -> oldValue + value);
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeReservedCargo(String username, int amount) {
        try {
            lock.lockRead();
            Integer value = reservedCargo.get(username);
            if (value == null) {
                lock.unlockRead();
                return;
            }
            lock.unlockRead();

            lock.lockWrite();
            if (value - amount <= 0) reservedCargo.remove(username);
            else reservedCargo.put(username, value - amount);
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public int getReservedCargo(String username) {
        Integer value = null;
        try {
            lock.lockRead();
            value = reservedCargo.get(username);
            lock.unlockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (value == null) return 0;
        else return value;
    }
}
