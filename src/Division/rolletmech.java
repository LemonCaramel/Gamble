package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class rolletmech {
    private int[] getslot;
    private int taskid;
    private int focus;
    public rolletmech(){
        getslot = new int[6];
        getslot[0] = 4;
        getslot[1] = 15;
        getslot[2] = 33;
        getslot[3] = 40;
        getslot[4] = 29;
        getslot[5] = 11;
    }
    public int previousnum(int a){
        if (a-1 == -1){
            return 5;
        }
        else{
            return a-1;
        }
    }
    public int flownum(int a){
        if (a+1 == 6){
            return 0;
        }
        else{
            return a+1;
        }
    }
    public ItemStack setfocus(ItemStack item){
        ItemStack temp = item;
        temp.setDurability((short)14);
        return temp;
    }
    public ItemStack returnfocus(ItemStack item){
        ItemStack temp = item;
        temp.setDurability((short)15);
        return temp;
    }
    public void giveresult(Player p){
        //로어 빼오기
        List lore = p.getOpenInventory().getItem(getslot[previousnum(focus)]).getItemMeta().getLore();
        String[] reallore = lore.get(1).toString().split(":");
        reallore[1] = ChatColor.stripColor(reallore[1]);
        reallore[1] = reallore[1].replace(" ", "");
        reallore[1] = reallore[1].replace("배", "");
        Double result = Double.parseDouble(reallore[1]);
        if (result == 0.0){
            p.sendTitle(Command.st, ChatColor.WHITE + "0.0 배에 당첨되어 배팅금을 잃으셨습니다..ㅜ",5,50,5);
        }
        else{
            p.sendTitle(Command.st, ChatColor.RED + String.valueOf(result) + ChatColor.WHITE + " 배" + ChatColor.WHITE + " 에 당첨되어 " + ChatColor.GOLD + Math.round(result*Hashmap.getbetmoney(p)*10)/10.0 + ChatColor.WHITE + " 원을 흭득하셨습니다.",5,50,5);
            Logger.addlog(p.getName() + "님이 룰렛에서 " + result + "배에 당첨되어 " + Math.round(result*Hashmap.getbetmoney(p)*10)/10.0 + "원 흭득.");
        }
        Main.instance.econ.depositPlayer(p, Math.round(result*Hashmap.getbetmoney(p)*10)/10.0);
    }
    public void rolletstart(Player p){
        //버튼 날려버리기
        p.getOpenInventory().setItem(22, new ItemStack(Material.AIR));
        taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
            int count = 1;
            int stoptimer = (int)(Math.random()*50+70)/2;
            @Override
            public void run() {
                if (count == stoptimer || !p.getOpenInventory().getTitle().contains("룰렛")){
                    Bukkit.getScheduler().cancelTask(taskid);
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                    if (count == stoptimer){
                        gamestop(p);
                    }
                }
                else {
                    p.getOpenInventory().setItem(getslot[focus], setfocus(p.getOpenInventory().getItem(getslot[focus])));
                    p.getOpenInventory().setItem(getslot[previousnum(focus)], returnfocus(p.getOpenInventory().getItem(getslot[previousnum(focus)])));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 3.0f, 3.0f);
                    focus = flownum(focus);
                    count++;
                }
            }
        },0L, 2L);
    }
    public void gamestop(Player p){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (p.getOpenInventory().getTitle().contains("룰렛")) {
                    giveresult(p);
                    Hashmap.removeplayer(p);
                    p.closeInventory();
                }
            }
        }, 20L);
    }
}
