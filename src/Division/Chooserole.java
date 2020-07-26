package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Chooserole {
    private static String st;
    private Player p;
    private ItemStack[] roleitem;
    private ItemMeta[] rolemeta;
    private List lore;
    public Chooserole(Player p){
        this.st = Command.st;
        this.p = p;
        this.roleitem = new ItemStack[4];
        this.lore = new ArrayList();
        this.rolemeta = new ItemMeta[4];
        itemset();
    }
    public void dicerole(){
        Inventory roleinv = Bukkit.createInventory(null, 36, st + ChatColor.BLACK + "주사위 룰 선택");
        roleinv.setItem(4, this.roleitem[0]);
        roleinv.setItem(19, this.roleitem[1]);
        roleinv.setItem(22, this.roleitem[2]);
        roleinv.setItem(25, this.roleitem[3]);
        p.openInventory(roleinv);
    }
    public void itemset(){
        int i;
        this.roleitem[0] = new ItemStack(Material.NETHER_STAR);
        this.rolemeta[0] = this.roleitem[0].getItemMeta();
        for (i = 1; i < 4; i++){
            this.roleitem[i] = new ItemStack(Material.ENCHANTED_BOOK);
            this.rolemeta[i] = this.roleitem[i].getItemMeta();
        }
        this.rolemeta[0].setDisplayName(st + ChatColor.RED + "주사위 룰 선택");
        this.rolemeta[1].setDisplayName(st + ChatColor.YELLOW + "로우");
        this.rolemeta[2].setDisplayName(st + ChatColor.GOLD + "미드");
        this.rolemeta[3].setDisplayName(st + ChatColor.RED + "하이");
        this.lore.add(" ");
        this.lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "게임에 사용될 룰을 선택해주세요.");
        this.lore.add(" ");
        this.rolemeta[0].setLore(this.lore);
        this.lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "낮은 숫자를 가진 사람이 승리합니다.");
        this.rolemeta[1].setLore(this.lore);
        this.lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "50에 가까운 숫자를 가진 사람이 승리합니다.");
        this.rolemeta[2].setLore(this.lore);
        this.lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "높은 숫자를 가진 사람이 승리합니다.");
        this.rolemeta[3].setLore(this.lore);
        for (i = 0; i < 4; i++){
            this.roleitem[i].setItemMeta(this.rolemeta[i]);
        }

    }
}
