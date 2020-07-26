package Division;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hashmap {
    public static String card = "♠A,♠2,♠3,♠4,♠5,♠6,♠7,♠8,♠9,10,♠Q,♠J,♠K,♥A,♥2,♥3,♥4,♥5,♥6,♥7,♥8,♥9,♥10,♥Q,♥J,♥K,♦A,♦2,♦3,♦4,♦5,♦6,♦7,♦8,♦9,♦10,♦Q,♦J,♦K,♣A,♣2,♣3,♣4,♣5,♣6,♣7,♣8,♣9,♣10,♣Q,♣J,♣K";
    public static HashMap<Player, Integer> slotuse = new HashMap<Player, Integer>();
    public static HashMap<Player, List> blackcard = new HashMap<>();
    public static HashMap<String, String> indian = new HashMap<>();
    public static HashMap<String, Integer> indiannmax = new HashMap<>();
    public static HashMap<String, String> cardrequest = new HashMap<>();
    public static HashMap<String, Integer> cardmax = new HashMap<>();
    public static HashMap<String, Character> cardsel = new HashMap<>();
    public static void addplayer(Player p, int a){
        if (slotuse.get(p) == null){
            slotuse.put(p, a);
        }
    }
    public static void removeplayer(Player p){
        if (slotuse.get(p) != null){
            slotuse.remove(p);
        }
    }
    public static boolean hasplayer(Player p){
        if (slotuse.get(p) != null){
            return true;
        }
        else{
            return false;
        }
    }
    public static int getbetmoney(Player p){
        if (hasplayer(p) == true){
            return slotuse.get(p);
        }
        return 0;
    }
    public static void getcard(Player p){
        if (blackcard.get(p) == null){
            ArrayList cards = new ArrayList<String>(Arrays.asList(card.split(",")));
            blackcard.put(p, cards);
        }
    }
    public static boolean isrequested(String uuid){
        for (String id : indian.values())
            if (id.equalsIgnoreCase(uuid)) {
                return true;
            }
        return false;
    }
    public static boolean iscardrequested(String uuid){
        for (String id : cardrequest.values())
            if (id.equalsIgnoreCase(uuid)) {
                return true;
            }
        return false;
    }
    public static String getrequester(String uuid){
        for (String key : indian.keySet()){
            if (indian.get(key).equalsIgnoreCase(uuid))
                return key;
        }
        return "NULL";
    }
    public static String getcardrequester(String uuid){
        for (String key : cardrequest.keySet()){
            if (cardrequest.get(key).equalsIgnoreCase(uuid))
                return key;
        }
        return "NULL";
    }
    public static void delrequest(String uuid){
        String resultkey = "TEMP";
        indian.remove(uuid);
        indiannmax.remove(uuid);
        for (String key : indian.keySet())
            if (indian.get(key).equalsIgnoreCase(uuid)){
               resultkey = key;
            }
        if (!resultkey.equalsIgnoreCase("TEMP")) {
            indian.remove(resultkey);
            indiannmax.remove(resultkey);
        }

    }
    public static void delcardrequest(String uuid){
        String resultkey = "TEMP";
        cardrequest.remove(uuid);
        cardmax.remove(uuid);
        for (String key : cardrequest.keySet())
            if (cardrequest.get(key).equalsIgnoreCase(uuid)){
                resultkey = key;
            }
        if (!resultkey.equalsIgnoreCase("TEMP")) {
            cardrequest.remove(resultkey);
            cardmax.remove(resultkey);
        }

    }
    public static List returncards(Player p){
        if (blackcard.get(p) != null){
            return blackcard.get(p);
        }
        return null;
    }
    public static void setcard(Player p, List list){
        blackcard.put(p, list);
    }
    public static void removecard(Player p){
        if (blackcard.get(p) != null){
            blackcard.remove(p);
        }
    }
    public static void deletelist(){
        slotuse.clear();
        blackcard.clear();
    }

}
