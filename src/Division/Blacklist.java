package Division;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Blacklist {
    public static void clear(){
        Main.blacklist.clear();
    }
    public static boolean findplayer(Player p){
        int i, size;
        if (Main.blacklist.isEmpty()){
            return false;
        }
        size = Main.blacklist.size();
        for (i = 0; i < size; i++){
            if (Main.blacklist.get(i).toString().equalsIgnoreCase(p.getUniqueId().toString())){
                return true;
            }
        }
        return false;
    }
    public static void addplayer(Player p){
        if (findplayer(p)){
            return;
        }
        else{
            Main.blacklist.add(p.getUniqueId().toString());
        }
    }
    public static String getplayer(int a){
       if (!isindexout(a)){
           Player target = Bukkit.getServer().getPlayer(UUID.fromString(Main.blacklist.get(a).toString()));
           if (target != null){
               return target.getName();
           }
           else{
               OfflinePlayer target1 = Bukkit.getOfflinePlayer(UUID.fromString(Main.blacklist.get(a).toString()));
               if (target1 != null){
                   return target1.getName();
               }
           }
       }
       return "확인되지 않는 플레이어";
    }
    public static void removeindex(int a){
        if (isindexout(a)){
            return;
        }
        else if (Main.blacklist.isEmpty()){
            return;
        }
        else{
            Main.blacklist.remove(a);
        }
    }
    public static boolean isindexout(int a){
        if (Main.blacklist.isEmpty()){
            return true;
        }
        else if (Main.blacklist.size()-1 < a){
            return true;
        }
        return false;
    }
}
