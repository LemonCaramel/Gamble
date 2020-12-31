package com.division.data;

import java.util.HashMap;
import java.util.UUID;

public class CardData {

    private HashMap<UUID, UUID> cardRequest;
    private HashMap<UUID, Integer> cardBet;
    private static CardData instance;

    static {
        instance = new CardData();
    }

    private CardData() {
        cardRequest = new HashMap<>();
        cardBet = new HashMap<>();
    }

    public enum ResponseType {
        REQUESTER,
        TARGET,
        NONE
    }

    public static CardData getInstance() {
        return instance;
    }

    public ResponseType findPlayer(UUID target) {
        for (UUID data : cardRequest.keySet()) {
            if (data.equals(target))
                return ResponseType.REQUESTER;
            else if (cardRequest.get(data).equals(target))
                return ResponseType.TARGET;
        }
        return ResponseType.NONE;
    }

    public void remove(UUID target) {
        UUID value = getTarget(target);
        cardRequest.remove(target);
        cardRequest.entrySet().removeIf(data -> data.getValue().equals(target));
        cardBet.remove(target);
        if (value != null) {
            cardRequest.remove(value);
            cardRequest.entrySet().removeIf(data -> data.getValue().equals(value));
            cardBet.remove(target);
        }
    }

    public UUID getTarget(UUID target) {
        ResponseType type = findPlayer(target);
        switch (type) {
            case REQUESTER:
                return cardRequest.get(target);
            case TARGET:
                for (UUID data : cardRequest.keySet())
                    if (cardRequest.get(data).equals(target))
                        return data;
            case NONE:
                return null;
        }
        return null;
    }

    public void addValue(UUID request, UUID target, int value) {
        cardRequest.put(request, target);
        cardBet.put(request, value);
    }

    public int getMoney(UUID data){
        return cardBet.get(data);
    }
}
