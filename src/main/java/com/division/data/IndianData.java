package com.division.data;

import java.util.HashMap;
import java.util.UUID;

public class IndianData {

    private static IndianData instance;
    private HashMap<UUID, UUID> indianRequest;
    private HashMap<UUID, Integer> indianBet;

    public enum RequestType {
        REQUESTER,
        TARGET,
        NONE
    }

    static {
        instance = new IndianData();
    }

    private IndianData() {
        indianRequest = new HashMap<>();
        indianBet = new HashMap<>();
    }

    public static IndianData getInstance() {
        return instance;
    }

    public RequestType findPlayer(UUID target) {
        for (UUID data : indianRequest.keySet()) {
            if (data.equals(target))
                return RequestType.REQUESTER;
            else if (indianRequest.get(data).equals(target))
                return RequestType.TARGET;
        }
        return RequestType.NONE;
    }

    public void remove(UUID target) {
        UUID value = getTarget(target);
        indianRequest.remove(target);
        indianRequest.entrySet().removeIf(data -> data.getValue().equals(target));
        indianBet.remove(target);
        if (value != null){
            indianRequest.remove(value);
            indianRequest.entrySet().removeIf(data -> data.getValue().equals(value));
            indianBet.remove(value);
        }
    }

    public UUID getTarget(UUID target) {
        RequestType type = findPlayer(target);
        switch (type) {
            case REQUESTER:
                return indianRequest.get(target);
            case TARGET:
                for (UUID data : indianRequest.keySet())
                    if (indianRequest.get(data).equals(target))
                        return data;
            case NONE:
                return null;
        }
        return null;
    }

    public void addValue(UUID request, UUID target, int value) {
        indianRequest.put(request, target);
        indianBet.put(request, value);
    }

    public int getMoney(UUID request) {
        return indianBet.get(request);
    }
}
