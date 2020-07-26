package Division;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardHashmap {
    public static HashMap<Player, List> dealer = new HashMap();
    public static HashMap<Player, List> user = new HashMap();
    public static boolean nulcheck(Player p){
        if (Hashmap.blackcard.get(p) == null){
            return true;
        }
        return false;
    }
    public static void addcard(String str, Player p){
        if (str.equalsIgnoreCase("dealer")){
            if (nulcheck(p) == false){
                List list = Hashmap.returncards(p);
                List dealercard = dealer.get(p);
                if (dealercard == null){
                    dealercard = new ArrayList();
                }
                int random = (int)(Math.random()*list.size());
                dealercard.add(list.get(random));
                list.remove(random);
                Hashmap.setcard(p, list);
                dealer.put(p, dealercard);
            }
        }
        else if (str.equalsIgnoreCase("user")){
            if (nulcheck(p) == false) {
                List list = Hashmap.returncards(p);
                List usercard = user.get(p);
                if (usercard == null){
                    usercard = new ArrayList();
                }
                int random = (int) (Math.random() * list.size());
                usercard.add(list.get(random));
                list.remove(random);
                user.put(p, usercard);
                Hashmap.setcard(p, list);
            }
        }
    }
    public static void errorcheck(Player p){
        List cards = Hashmap.returncards(p);
        List deal = dealer.get(p);
        List us = user.get(p);
        if (deal.get(0).toString().contains("A") && deal.get(1).toString().contains("A")){
            deal.set(1, "♥2");
            dealer.put(p, deal);
            cards.remove("♥2");
        }
        if (us.get(0).toString().contains("A") && us.get(1).toString().contains("A")){
            us.set(1, "♥3");
            cards.remove("♥3");
        }
        Hashmap.setcard(p, cards);
    }
    public static void personalclear(Player p){
        if (dealer.get(p) != null){
            dealer.remove(p);
        }
        if (user.get(p) != null){
            user.remove(p);
        }
    }
    public static void clear(){
        dealer.clear();
        user.clear();
    }
}
