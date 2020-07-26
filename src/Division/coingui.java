package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class coingui {
    private ItemStack edge;
    private ItemStack[] items;
    private String st = Command.st;
    private Inventory inv;
    public coingui(){
        itemset();
        guicreate();
    }
    public void itemset(){
        int i;
        List lore = new ArrayList();
        edge = new ItemStack(Material.IRON_FENCE);
        ItemMeta meta = edge.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "]");
        edge.setItemMeta(meta);
        items = new ItemStack[3];
        ItemMeta[] metas = new ItemMeta[3];
        items[0] = new ItemStack(Material.RECORD_3);
        items[1] = new ItemStack(Material.PAPER);
        items[2] = new ItemStack(Material.RECORD_3);
        for (i = 0; i < 3; i++){
            metas[i] = items[i].getItemMeta();
        }
        metas[0].setDisplayName(st + ChatColor.RED + "앞면");
        metas[2].setDisplayName(st + ChatColor.AQUA + "뒷면");
        metas[1].setDisplayName(st + ChatColor.GOLD + "설명");
        lore.add(" ");
        lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 앞면을 예측합니다.");
        lore.add(" ");
        metas[0].setLore(lore);
        items[0].setItemMeta(metas[0]);
        lore.remove(1);
        lore.add(1,ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "동전의 앞면과 뒷면을 예측하는 도박입니다.");
        lore.add(2,ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "1회당 " + ChatColor.GOLD + Main.coinneed + ChatColor.WHITE + " 원이 소모되며, 앞/뒷면을 선택할시 차감됩니다.");
        lore.add(3,ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "앞면 혹은 뒷면을 선택할 시 결과가 공개되며, 승리시 " + ChatColor.GOLD + Main.coinneed*1.5 + ChatColor.WHITE + " 원을 흭득합니다.");
        metas[1].setLore(lore);
        items[1].setItemMeta(metas[1]);
        lore.remove(1);
        lore.remove(1);
        lore.remove(1);
        lore.add(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 뒷면을 예측합니다.");
        metas[2].setLore(lore);
        items[2].setItemMeta(metas[2]);
    }
    public void guicreate(){
        int i;
        inv = Bukkit.createInventory(null, 27, st + ChatColor.GRAY + "동전");
        for (i = 0; i < 9; i++){
            inv.setItem(i, edge);
            inv.setItem(i+18, edge);
        }
        inv.setItem(9, edge);
        inv.setItem(17, edge);
        inv.setItem(11, items[0]);
        inv.setItem(13, items[1]);
        inv.setItem(15, items[2]);
    }
    public void opengui(Player p){
        p.openInventory(inv);
    }
    public static void gamestop(Player p, boolean wincheck){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (p.getOpenInventory().getTitle().contains("동전")) {
                    if (wincheck) {
                        p.sendTitle(Command.st, ChatColor.WHITE + "베팅에 성공하여 " + ChatColor.GOLD + Main.coinneed * 1.5 + ChatColor.WHITE + " 원을 흭득하셨습니다.", 5, 50, 5);
                        Main.instance.econ.depositPlayer(p, Main.coinneed * 1.5);
                        Hashmap.removeplayer(p);
                        p.closeInventory();
                        Logger.addlog(p.getName() + "님이 동전 도박 베팅에 성공하여 " + Main.coinneed * 1.5 + "원 흭득.");
                    } else {
                        p.sendTitle(Command.st, ChatColor.GRAY + "베팅에 실패하셨습니다..", 5, 50, 5);
                        Hashmap.removeplayer(p);
                        p.closeInventory();
                    }
                }
            }
        },20);
    }
    public static void check(Player p, int input){
        int result = (int)(Math.random()*2+1);
        ItemStack[] resultitem = new ItemStack[2];
        ItemMeta[] resultmeta = new ItemMeta[2];
        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
        resultitem[0] = new ItemStack(Material.ENCHANTED_BOOK);
        resultmeta[0] = resultitem[0].getItemMeta();
        resultmeta[0].setDisplayName(Command.st + ChatColor.RED + "결과가 공개 되었습니다!");
        resultitem[0].setItemMeta(resultmeta[0]);
        resultitem[1] = new ItemStack(Material.DIAMOND);
        resultmeta[1] = resultitem[1].getItemMeta();
        p.getOpenInventory().setItem(13, resultitem[0]);
        if (result == 1){
            p.getOpenInventory().setItem(15, new ItemStack(Material.AIR));
            resultmeta[1].setDisplayName(Command.st + ChatColor.RED + "앞면이 나왔습니다!");
            resultitem[1].setItemMeta(resultmeta[1]);
            p.getOpenInventory().setItem(11, resultitem[1]);
        }
        else{
            p.getOpenInventory().setItem(11, new ItemStack(Material.AIR));
            resultmeta[1].setDisplayName(Command.st + ChatColor.AQUA + "뒷면이 나왔습니다!");
            resultitem[1].setItemMeta(resultmeta[1]);
            p.getOpenInventory().setItem(15, resultitem[1]);
        }
        if (input == result){
            gamestop(p, true);
        }
        else{
            gamestop(p, false);
        }
    }
}
