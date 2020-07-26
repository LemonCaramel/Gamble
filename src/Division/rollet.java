package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class rollet {
    private static String st = Command.st;
    private List multi;
    private Double[] mutiply;
    private ItemStack edge;
    private ItemStack btn;
    private ItemStack[] roll;
    private ItemMeta[] rollmeta;
    private Inventory inv;
    public rollet(){
        this.multi = mutiplyset();
        mutiplyset();
        itemset();
        this.inv = guisetting();
    }
    public List mutiplyset(){
        //1~3는 0.0배 4는 0.1~0.5배 5는 0.5~2.0배 6은 1.5~3.0배
        mutiply = new Double[6];
        mutiply[0] = 0.0;
        mutiply[1] = 0.0;
        mutiply[2] = 0.0;
        mutiply[3] = Math.round((Math.random()*1.1+0.1)*10)/10.0;
        mutiply[4] = Math.round((Math.random()*0.5+0.1)*10)/10.0;
        mutiply[5] = Math.round((Math.random()*1.6+1.5)*10)/10.0;
        List retnval = Arrays.asList(mutiply);
        Collections.shuffle(retnval);
        //무작위로 섞기
        return retnval;
    }
    public void itemset(){
        int i;
        edge = new ItemStack(Material.IRON_FENCE);
        ItemMeta meta = edge.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "]");
        edge.setItemMeta(meta);
        btn = new ItemStack(Material.STONE_BUTTON);
        ItemMeta btnmeta = btn.getItemMeta();
        btnmeta.setDisplayName(st + ChatColor.AQUA + "룰렛 돌리기");
        List lore = new ArrayList();
        lore.add(" ");
        lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 룰렛을 돌립니다.");
        lore.add(" ");
        btnmeta.setLore(lore);
        btn.setItemMeta(btnmeta);
        roll = new ItemStack[6];
        rollmeta = new ItemMeta[6];
        for (i = 0; i < 6; i++){
            roll[i] = new ItemStack(Material.WOOL, 1, (short)15);
            rollmeta[i] = roll[i].getItemMeta();
            rollmeta[i].setDisplayName(st + ChatColor.GOLD + (i+1) + ChatColor.WHITE + " 번째 칸");
            lore.remove(1);
            lore.add(1,ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "배율 " + ChatColor.GRAY + ": " + ChatColor.RED + multi.get(i) + ChatColor.WHITE + " 배");
            rollmeta[i].setLore(lore);
            roll[i].setItemMeta(rollmeta[i]);
        }
    }
    public Inventory guisetting(){
        int i;
        Inventory inv = Bukkit.createInventory(null, 54, st + ChatColor.DARK_RED + "룰렛");
        for (i = 0; i < 9; i++){
            inv.setItem(45+i, edge);
        }
        inv.setItem(22, btn);
        inv.setItem(4, roll[0]);
        inv.setItem(15, roll[1]);
        inv.setItem(33, roll[2]);
        inv.setItem(40, roll[3]);
        inv.setItem(29, roll[4]);
        inv.setItem(11, roll[5]);
        return inv;
    }
    public void openrollet(Player p){
        p.openInventory(inv);
    }

}
