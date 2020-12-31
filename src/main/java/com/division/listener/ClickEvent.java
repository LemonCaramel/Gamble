package com.division.listener;

import com.division.Gamble;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClickEvent implements Listener {
    /*private static String st = Command.st;

    @EventHandler(priority = EventPriority.NORMAL)
    public void slotclick(InventoryClickEvent event) {
        if (event.getRawSlot() == -999) {
            return;
        } else {
            String title = event.getInventory().getTitle();
            Player p = (Player) event.getWhoClicked();
            Material type = null;
            if (event.getCurrentItem() != null) {
                type = event.getCurrentItem().getType();
            }
            if (title.contains("슬롯머신")) {
                event.setCancelled(true);
                if (type == Material.LEVER) {
                    if (Hashmap.hasplayer(p)) {
                        p.sendMessage(st + ChatColor.RED + "슬롯머신을 이미 사용중입니다..!");
                    } else {
                        Slotmechnism slot = new Slotmechnism(p);
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2.0f, 1.0f);
                        slot.rollslot();
                        Logger.addlog(p.getName() + "님이 슬롯머신 1회 사용함");
                    }
                }
            } else if (title.contains("주사위 룰 선택")) {
                event.setCancelled(true);
                if (type == Material.ENCHANTED_BOOK) {
                    if (event.getRawSlot() == 19) {
                        Dicemechnism dice = new Dicemechnism(p);
                        dice.dicecreate(19);
                    } else if (event.getRawSlot() == 22) {
                        Dicemechnism dice = new Dicemechnism(p);
                        dice.dicecreate(22);
                    } else {
                        Dicemechnism dice = new Dicemechnism(p);
                        dice.dicecreate(25);
                    }
                }
            } else if (title.contains("주사위 도박")) {
                event.setCancelled(true);
                if (type == Material.GOLD_BLOCK) {
                    p.getOpenInventory().setItem(event.getRawSlot(), Dicemechnism.dicecheck(p, event.getCurrentItem()));
                }
            } else if (title.contains("블랙잭")) {
                event.setCancelled(true);
                if (type == Material.STONE_BUTTON) {
                    //insurance체크
                    if (Blackjack.insurance(p)) {
                        //블랙잭 여부 확인시 메시지
                        p.sendMessage(st + ChatColor.RED + "Insurance(보험) 여부를 선택해주세요. (종이 클릭시 거부)");
                    } else {
                        //일반처리
                        Blackjack.hitcard("user", p);
                    }
                } else if (type == Material.WOOD_BUTTON) {
                    //insurance체크
                    if (Blackjack.insurance(p)) {
                        //블랙잭 여부 확인시 메시지
                        p.sendMessage(st + ChatColor.RED + "Insurance(보험) 여부를 선택해주세요. (종이 클릭시 거부)");
                    } else {
                        //일반처리
                        Blackjack.endcheck(p);
                    }
                } else if (type == Material.REDSTONE_TORCH_ON) {
                    //insurance시
                    event.getInventory().setItem(40, Blackjack.insurancecheck(p));
                } else if (type == Material.PAPER) {
                    //insurance 거부시
                    if (Blackjack.insurance(p)) {
                        Blackjack.insurancedeny(p);
                    }
                }
            } else if (title.contains("룰렛")) {
                event.setCancelled(true);
                if (type == Material.STONE_BUTTON) {
                    rolletmech roll = new rolletmech();
                    roll.rolletstart(p);
                }
            } else if (title.contains("동전")) {
                event.setCancelled(true);
                if (type == Material.RECORD_3) {
                    if (Gamble.instance.econ.getBalance(p) >= Gamble.coinneed) {
                        Hashmap.addplayer(p, Gamble.coinneed);
                        Gamble.instance.econ.withdrawPlayer(p, Gamble.coinneed);
                        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("앞면")) {
                            coingui.check(p, 1);
                        } else {
                            coingui.check(p, 2);
                        }
                    } else {
                        p.sendTitle(st, ChatColor.RED + "돈이 부족합니다..", 5, 50, 5);
                        event.getWhoClicked().closeInventory();
                        p.playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                    }
                }
            }
         else if (title.contains("카드 도박")) {
            event.setCancelled(true);
            Player target;
            if (Hashmap.cardrequest.get(p.getUniqueId().toString()) == null){
                target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString())));
            }
            else{
                target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.cardrequest.get(p.getUniqueId().toString())));
            }
            if (type == Material.NOTE_BLOCK) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 0.7f, 1.0f);
                target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 0.7f, 1.0f);
            }
            else if (type == Material.BOOK){
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("King"))
                    Hashmap.cardsel.put(p.getUniqueId().toString(), 'K');
                else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Citizen"))
                    Hashmap.cardsel.put(p.getUniqueId().toString(), 'C');
                else
                    Hashmap.cardsel.put(p.getUniqueId().toString(), 'S');
                if (Hashmap.cardrequest.get(p.getUniqueId().toString()) != null) {
                    CardGui.setstatus(p, false);
                    CardGui.setstatus(target, true);
                }
                else{
                    CardGui.check(p);
                }
            }
        }
            else if (title.contains("인디언포커")){
                event.setCancelled(true);
                if (event.getCurrentItem() != null)
                    if (event.getCurrentItem().getType() == Material.CHEST)
                        p.getOpenInventory().setItem(31, indianGui.checkdeck());
                    else if (event.getCurrentItem().getType() == Material.STONE_BUTTON){
                        if (p.getOpenInventory().getItem(13).getType() == Material.DIAMOND_BLOCK){
                            Player target;
                            if (Hashmap.indiannmax.get(p.getUniqueId().toString()) == null)
                                target = Bukkit.getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString())));
                            else
                                target = Bukkit.getPlayer(UUID.fromString(Hashmap.indian.get(p.getUniqueId().toString())));
                            p.sendTitle(st, ChatColor.WHITE + "게임을 포기하셨습니다.", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 3.0f, 1.0f);
                            target.sendTitle(st, ChatColor.WHITE + "상대방이 게임을 포기하여 승리하셨습니다!", 5, 50, 5);
                            target.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                            int resultval = Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target);
                            Gamble.instance.econ.depositPlayer(target, resultval);
                            Hashmap.removeplayer(target);
                            Hashmap.removeplayer(p);
                            p.closeInventory();
                            target.closeInventory();
                            Hashmap.delrequest(p.getUniqueId().toString());
                            Logger.addlog(p.getName() + "님이 인디언 포커 도중 포기해 " + target.getName() + "님이 " + resultval + "원 흭득");
                        }
                        else{
                            p.sendMessage(st + ChatColor.WHITE + "당신의 차례가 아닙니다.");
                        }
                    }
                    else if (event.getCurrentItem().getType() == Material.WOOD_BUTTON)
                        if (p.getOpenInventory().getItem(13).getType() == Material.DIAMOND_BLOCK) {
                            Player target;
                            if (event.getClick() == ClickType.LEFT){
                                //베팅시

                            else if (event.getClick() == ClickType.RIGHT){
                                //베팅종료시
                                if (p.getOpenInventory().getItem(31).getType() == Material.CHEST) {
                                    p.sendMessage(st + ChatColor.WHITE + "상대의 패를 확인해주세요..");
                                    return;
                                }
                                if (Hashmap.indian.get(p.getUniqueId().toString()) != null){
                                    target = Bukkit.getPlayer(UUID.fromString(Hashmap.indian.get(p.getUniqueId().toString())));
                                    if (Hashmap.getbetmoney(p) == (int)((double)Hashmap.indiannmax.get(p.getUniqueId().toString())/10)){
                                        p.sendMessage(st + ChatColor.DARK_RED + "베팅을 진행하고 종료해주세요..!");
                                    }
                                    else if (Hashmap.getbetmoney(p) < Hashmap.getbetmoney(target)){
                                        p.sendMessage(st + ChatColor.DARK_RED + "상대보다 같거나 더 많이 베팅하셔야 합니다.");
                                    }
                                    else{
                                        if (Hashmap.getbetmoney(p) > Hashmap.getbetmoney(target)){
                                            //턴 넘기기
                                            ItemStack red,bet,die;
                                            ItemMeta red1,bet1,die1;
                                            List redlore,betlore,dielore;
                                            red = new ItemStack(Material.REDSTONE_BLOCK);
                                            bet = new ItemStack(Material.WOOD_BUTTON);
                                            die = new ItemStack(Material.STONE_BUTTON);
                                            red1 = red.getItemMeta();
                                            bet1 = bet.getItemMeta();
                                            die1 = die.getItemMeta();
                                            red1.setDisplayName(Command.st + ChatColor.RED + "상대방의 차례 입니다.");
                                            redlore = new ArrayList();
                                            redlore.add(" ");
                                            redlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            redlore.add(" ");
                                            red1.setLore(redlore);
                                            red.setItemMeta(red1);
                                            bet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
                                            betlore = new ArrayList();
                                            betlore.add(" ");
                                            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            betlore.add(" ");
                                            bet1.setLore(betlore);
                                            bet.setItemMeta(bet1);
                                            die1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
                                            dielore = new ArrayList();
                                            dielore.add(" ");
                                            dielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다. (현재 사용불가)");
                                            dielore.add(" ");
                                            die1.setLore(dielore);
                                            die.setItemMeta(die1);
                                            p.getOpenInventory().setItem(13, red);
                                            p.getOpenInventory().setItem(29, bet);
                                            p.getOpenInventory().setItem(33, die);
                                            ItemStack dia,attackbet,attackdie;
                                            ItemMeta dia1,attackbet1,attackdie1;
                                            List dialore,attackbetlore,attackdielore;
                                            dia = new ItemStack(Material.DIAMOND_BLOCK);
                                            attackbet = new ItemStack(Material.WOOD_BUTTON);
                                            attackdie = new ItemStack(Material.STONE_BUTTON);
                                            dia1 = dia.getItemMeta();
                                            attackbet1 = attackbet.getItemMeta();
                                            attackdie1 = attackdie.getItemMeta();
                                            dia1.setDisplayName(Command.st + ChatColor.AQUA + "당신의 차례 입니다!");
                                            dialore = new ArrayList();
                                            dialore.add(" ");
                                            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "베팅 혹은 다이를 선택해주세요.");
                                            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            dialore.add(" ");
                                            dia1.setLore(dialore);
                                            dia.setItemMeta(dia1);
                                            attackbet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
                                            attackbetlore = new ArrayList();
                                            attackbetlore.add(" ");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "좌클릭시 " + ChatColor.GOLD + (int)((double)Hashmap.indiannmax.get(p.getUniqueId().toString())/10) +  ChatColor.WHITE + "원을 베팅합니다.");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.GREEN + "우클릭시 베팅을 종료합니다. 단 상대방의 베팅액보다 크거나 같아야 합니다.");
                                            attackbetlore.add(" ");
                                            attackbet1.setLore(attackbetlore);
                                            attackbet.setItemMeta(attackbet1);
                                            attackdie1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
                                            attackdielore = new ArrayList();
                                            attackdielore.add(" ");
                                            attackdielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다.");
                                            attackdielore.add(" ");
                                            attackdie1.setLore(attackdielore);
                                            attackdie.setItemMeta(attackdie1);
                                            target.getOpenInventory().setItem(13, dia);
                                            target.getOpenInventory().setItem(29, attackbet);
                                            target.getOpenInventory().setItem(33, attackdie);
                                        }
                                        else{
                                            String pdeck, targetdeck;
                                            pdeck = target.getOpenInventory().getItem(31).getItemMeta().getDisplayName().replace(st, "");
                                            pdeck = ChatColor.stripColor(pdeck);
                                            pdeck = pdeck.replace(" ", "");
                                            targetdeck = p.getOpenInventory().getItem(31).getItemMeta().getDisplayName().replace(st, "");
                                            targetdeck = ChatColor.stripColor(targetdeck);
                                            targetdeck = targetdeck.replace(" ", "");
                                            p.getOpenInventory().setItem(13, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(31, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(13, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(31, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
                                            ItemStack[] stacks = new ItemStack[2];
                                            ItemMeta[] metas = new ItemMeta[2];
                                            stacks[0] = new ItemStack(Material.DIAMOND_BLOCK);
                                            stacks[1] = new ItemStack(Material.REDSTONE);
                                            metas[0] = stacks[0].getItemMeta();
                                            metas[1] = stacks[1].getItemMeta();
                                            metas[0].setDisplayName(st + ChatColor.RED + p.getName() + ChatColor.WHITE + "님의 패 " + ChatColor.GRAY + ": " +ChatColor.GOLD + pdeck);
                                            metas[1].setDisplayName(st + ChatColor.RED + target.getName() + ChatColor.WHITE + "님의 패 " + ChatColor.GRAY + ": " +ChatColor.GOLD + targetdeck);
                                            stacks[0].setItemMeta(metas[0]);
                                            stacks[1].setItemMeta(metas[1]);
                                            p.getOpenInventory().setItem(29, stacks[0]);
                                            p.getOpenInventory().setItem(33, stacks[1]);
                                            target.getOpenInventory().setItem(29, stacks[1]);
                                            target.getOpenInventory().setItem(33, stacks[0]);
                                            String finalPdeck = pdeck;
                                            String finalTargetdeck = targetdeck;
                                            Bukkit.getScheduler().runTaskLaterAsynchronously(Gamble.instance, new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (p.getOpenInventory().getTitle().contains("인디언포커")) {
                                                        if (Integer.parseInt(finalPdeck) > Integer.parseInt(finalTargetdeck)) {
                                                            p.sendTitle(st, ChatColor.WHITE + "승리! 총 베팅한 금액을 받습니다!", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "패배 하였습니다 ㅠㅠㅠㅠ", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target));
                                                            Logger.addlog(p.getName() + "님이 승리하여 " + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target)) + "원 흭득");

                                                        } else if (Integer.parseInt(finalPdeck) == Integer.parseInt(finalTargetdeck)) {
                                                            p.sendTitle(st, ChatColor.WHITE + "무승부! 베팅한 돈을 그대로 돌려받습니다.", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "무승부! 베팅한 돈을 그대로 돌려받습니다.", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p));
                                                            Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(target));
                                                            Logger.addlog(p.getName() + "님과 " + target.getName() + " 무승부");
                                                        } else {
                                                            p.sendTitle(st, ChatColor.WHITE + "패배 하였습니다 ㅠㅠㅠㅠ", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "승리! 총 베팅한 금액을 받습니다!", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target));
                                                            Logger.addlog(target.getName() + "님이 승리하여 " + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target)) + "원 흭득");
                                                        }
                                                    }
                                                    Hashmap.removeplayer(p);
                                                    Hashmap.removeplayer(target);
                                                    p.closeInventory();
                                                    target.closeInventory();
                                                    Hashmap.delrequest(p.getUniqueId().toString());
                                                }
                                            },40L);
                                        }
                                    }
                                }
                                else{
                                    target = Bukkit.getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString())));
                                    if (Hashmap.getbetmoney(p) == (int)((double)Hashmap.indiannmax.get(target.getUniqueId().toString())/10)){
                                        p.sendMessage(st + ChatColor.DARK_RED + "베팅을 진행하고 종료해주세요..!");
                                    }
                                    else if (Hashmap.getbetmoney(p) < Hashmap.getbetmoney(target)){
                                        p.sendMessage(st + ChatColor.DARK_RED + "상대보다 같거나 더 많이 베팅하셔야 합니다.");
                                    }
                                    else{
                                        if (Hashmap.getbetmoney(p) > Hashmap.getbetmoney(target)){
                                            //턴 넘기기
                                            ItemStack red,bet,die;
                                            ItemMeta red1,bet1,die1;
                                            List redlore,betlore,dielore;
                                            red = new ItemStack(Material.REDSTONE_BLOCK);
                                            bet = new ItemStack(Material.WOOD_BUTTON);
                                            die = new ItemStack(Material.STONE_BUTTON);
                                            red1 = red.getItemMeta();
                                            bet1 = bet.getItemMeta();
                                            die1 = die.getItemMeta();
                                            red1.setDisplayName(Command.st + ChatColor.RED + "상대방의 차례 입니다.");
                                            redlore = new ArrayList();
                                            redlore.add(" ");
                                            redlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            redlore.add(" ");
                                            red1.setLore(redlore);
                                            red.setItemMeta(red1);
                                            bet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
                                            betlore = new ArrayList();
                                            betlore.add(" ");
                                            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            betlore.add(" ");
                                            bet1.setLore(betlore);
                                            bet.setItemMeta(bet1);
                                            die1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
                                            dielore = new ArrayList();
                                            dielore.add(" ");
                                            dielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다. (현재 사용불가)");
                                            dielore.add(" ");
                                            die1.setLore(dielore);
                                            die.setItemMeta(die1);
                                            p.getOpenInventory().setItem(13, red);
                                            p.getOpenInventory().setItem(29, bet);
                                            p.getOpenInventory().setItem(33, die);
                                            ItemStack dia,attackbet,attackdie;
                                            ItemMeta dia1,attackbet1,attackdie1;
                                            List dialore,attackbetlore,attackdielore;
                                            dia = new ItemStack(Material.DIAMOND_BLOCK);
                                            attackbet = new ItemStack(Material.WOOD_BUTTON);
                                            attackdie = new ItemStack(Material.STONE_BUTTON);
                                            dia1 = dia.getItemMeta();
                                            attackbet1 = attackbet.getItemMeta();
                                            attackdie1 = attackdie.getItemMeta();
                                            dia1.setDisplayName(Command.st + ChatColor.AQUA + "당신의 차례 입니다!");
                                            dialore = new ArrayList();
                                            dialore.add(" ");
                                            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "베팅 혹은 다이를 선택해주세요.");
                                            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            dialore.add(" ");
                                            dia1.setLore(dialore);
                                            dia.setItemMeta(dia1);
                                            attackbet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
                                            attackbetlore = new ArrayList();
                                            attackbetlore.add(" ");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "좌클릭시 " + ChatColor.GOLD + (int)((double)Hashmap.indiannmax.get(target.getUniqueId().toString())/10) +  ChatColor.WHITE + "원을 베팅합니다.");
                                            attackbetlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.GREEN + "우클릭시 베팅을 종료합니다. 단 상대방의 베팅액보다 크거나 같아야 합니다.");
                                            attackbetlore.add(" ");
                                            attackbet1.setLore(attackbetlore);
                                            attackbet.setItemMeta(attackbet1);
                                            attackdie1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
                                            attackdielore = new ArrayList();
                                            attackdielore.add(" ");
                                            attackdielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다.");
                                            attackdielore.add(" ");
                                            attackdie1.setLore(attackdielore);
                                            attackdie.setItemMeta(attackdie1);
                                            target.getOpenInventory().setItem(13, dia);
                                            target.getOpenInventory().setItem(29, attackbet);
                                            target.getOpenInventory().setItem(33, attackdie);
                                        }
                                        else{
                                            String pdeck, targetdeck;
                                            pdeck = target.getOpenInventory().getItem(31).getItemMeta().getDisplayName().replace(st, "");
                                            pdeck = ChatColor.stripColor(pdeck);
                                            pdeck = pdeck.replace(" ", "");
                                            targetdeck = p.getOpenInventory().getItem(31).getItemMeta().getDisplayName().replace(st, "");
                                            targetdeck = ChatColor.stripColor(targetdeck);
                                            targetdeck = targetdeck.replace(" ", "");
                                            p.getOpenInventory().setItem(13, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(31, new ItemStack(Material.AIR));
                                            p.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(13, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(31, new ItemStack(Material.AIR));
                                            target.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
                                            ItemStack[] stacks = new ItemStack[2];
                                            ItemMeta[] metas = new ItemMeta[2];
                                            stacks[0] = new ItemStack(Material.DIAMOND_BLOCK);
                                            stacks[1] = new ItemStack(Material.REDSTONE_BLOCK);
                                            metas[0] = stacks[0].getItemMeta();
                                            metas[1] = stacks[1].getItemMeta();
                                            metas[0].setDisplayName(st + ChatColor.RED + p.getName() + ChatColor.WHITE + "님의 패 " + ChatColor.GRAY + ": " +ChatColor.GOLD + pdeck);
                                            metas[1].setDisplayName(st + ChatColor.RED + target.getName() + ChatColor.WHITE + "님의 패 " + ChatColor.GRAY + ": " +ChatColor.GOLD + targetdeck);
                                            stacks[0].setItemMeta(metas[0]);
                                            stacks[1].setItemMeta(metas[1]);
                                            p.getOpenInventory().setItem(29, stacks[0]);
                                            p.getOpenInventory().setItem(33, stacks[1]);
                                            target.getOpenInventory().setItem(29, stacks[1]);
                                            target.getOpenInventory().setItem(33, stacks[0]);
                                            String finalPdeck = pdeck;
                                            String finalTargetdeck = targetdeck;
                                            Bukkit.getScheduler().runTaskLaterAsynchronously(Gamble.instance, new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (p.getOpenInventory().getTitle().contains("인디언포커")) {
                                                        if (Integer.parseInt(finalPdeck) > Integer.parseInt(finalTargetdeck)) {
                                                            p.sendTitle(st, ChatColor.WHITE + "승리! 총 베팅한 금액을 받습니다!", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "패배 하였습니다 ㅠㅠㅠㅠ", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target));
                                                            Logger.addlog(p.getName() + "님이 승리하여 " + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target)) + "원 흭득");
                                                        } else if (Integer.parseInt(finalPdeck) == Integer.parseInt(finalTargetdeck)) {
                                                            p.sendTitle(st, ChatColor.WHITE + "무승부! 베팅한 돈을 그대로 돌려받습니다.", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "무승부! 베팅한 돈을 그대로 돌려받습니다.", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p));
                                                            Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(target));
                                                            Logger.addlog(p.getName() + "님과 " + target.getName() + " 무승부");
                                                        } else {
                                                            p.sendTitle(st, ChatColor.WHITE + "패배 하였습니다 ㅠㅠㅠㅠ", 5, 50, 5);
                                                            target.sendTitle(st, ChatColor.WHITE + "승리! 총 베팅한 금액을 받습니다!", 5, 50, 5);
                                                            p.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 3.0f, 1.0f);
                                                            target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                                                            Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target));
                                                            Logger.addlog(target.getName() + "님이 승리하여 " + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(target)) + "원 흭득");

                                                        }
                                                    }
                                                    Hashmap.removeplayer(p);
                                                    Hashmap.removeplayer(target);
                                                    p.closeInventory();
                                                    target.closeInventory();
                                                    Hashmap.delrequest(p.getUniqueId().toString());
                                                }
                                            },40L);
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            p.sendMessage(st + ChatColor.RED + "당신의 차례가 아닙니다.");
                        }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void invclose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        String title = event.getInventory().getTitle();
        if (title.contains("슬롯머신")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "슬롯머신 사용도중 GUI창을 닫아 슬롯머신이 강제종료 되었습니다.", 5, 50, 5);
                Logger.addlog(p.getName() + "님이 슬롯머신 사용 도중 GUI를 닫음");
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
            }
        } else if (title.contains("주사위") && title.contains("진행중")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "주사위 도박 도중 GUI창을 닫아 주사위 도박이 중단 되었습니다.", 5, 50, 5);
                Logger.addlog(p.getName() + "님이 주사위 도박 도중 GUI를 닫음");
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
            }
        } else if (title.contains("블랙잭")) {
            if (Hashmap.hasplayer((Player) event.getPlayer())) {
                p.sendTitle(st, ChatColor.WHITE + "블랙잭 도중 GUI창을 닫아 블랙잭이 중단 되었습니다.", 5, 50, 5);
                Logger.addlog(p.getName() + "님이 블랙잭 도중 GUI를 닫음");
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
                Hashmap.removecard(p);
                CardHashmap.personalclear(p);

            }
        } else if (title.contains("룰렛")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "룰렛 도중 GUI창을 닫아 룰렛이 중단 되었습니다.", 5, 50, 5);
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Logger.addlog(p.getName() + "님이 룰렛 도중 GUI를 닫음");
                Hashmap.removeplayer(p);
            }
        } else if (title.contains("동전")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "동전 도박 도중 GUI창을 닫아 도박이 중단 되었습니다.", 5, 50, 5);
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
                Logger.addlog(p.getName() + "님이 동전 도박 도중 GUI를 닫음");
            }
        } else if (title.contains("인디언포커")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "인디언 포커 도중 GUI창을 닫아 도박이 중단 되었습니다.", 5, 50, 5);
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
                if (Hashmap.indian.get(p.getUniqueId().toString()) != null) {
                    //자기가 주최
                    if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.indian.get(p.getUniqueId().toString()))) != null) {
                        Player target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.indian.get(p.getUniqueId().toString())));
                        target.sendTitle(st, ChatColor.WHITE + "인디언 포커 도중 상대방이 GUI를 닫아 게임이 종료되고 돈이 환급되었습니다.", 5, 50, 5);
                        target.playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                        Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(target));
                        Hashmap.removeplayer(target);
                        target.closeInventory();
                    }
                    Hashmap.delrequest(p.getUniqueId().toString());
                }
                else {
                    //남이 주최
                    if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString()))) != null) {
                        Player owner = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString())));
                        owner.sendTitle(st, ChatColor.WHITE + "인디언 포커 도중 상대방이 GUI를 닫아 게임이 종료되고 돈이 환급되었습니다.", 5, 50, 5);
                        owner.playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                        Gamble.instance.econ.depositPlayer(owner, Hashmap.getbetmoney(owner));
                        Hashmap.removeplayer(owner);
                        owner.closeInventory();
                    }
                    Hashmap.delrequest(p.getUniqueId().toString());
                }
                Logger.addlog(p.getName() + "님이 인디언 포커 도중 GUI를 닫음");
            }

            }
        else if (title.contains("카드 도박")) {
            if (Hashmap.hasplayer(p)) {
                p.sendTitle(st, ChatColor.WHITE + "카드 도박 도중 GUI창을 닫아 도박이 중단 되었습니다.", 5, 50, 5);
                p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f, 1.0f);
                Hashmap.removeplayer(p);
                Hashmap.cardsel.remove(p.getUniqueId().toString());
                if (Hashmap.cardrequest.get(p.getUniqueId().toString()) != null) {
                    //자기가 주최
                    if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.cardrequest.get(p.getUniqueId().toString()))) != null) {
                        Player target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.cardrequest.get(p.getUniqueId().toString())));
                        target.sendTitle(st, ChatColor.WHITE + "카드 도박 도중 상대방이 GUI를 닫아 게임이 종료되고 돈이 환급되었습니다.", 5, 50, 5);
                        target.playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                        Gamble.instance.econ.depositPlayer(target, Hashmap.getbetmoney(target));
                        Hashmap.removeplayer(target);
                        target.closeInventory();
                        Hashmap.cardsel.remove(target.getUniqueId().toString());
                    }
                    Hashmap.delcardrequest(p.getUniqueId().toString());
                }
                else {
                    //남이 주최
                    if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString()))) != null) {
                        Player owner = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString())));
                        owner.sendTitle(st, ChatColor.WHITE + "카드도박 도중 상대방이 GUI를 닫아 게임이 종료되고 돈이 환급되었습니다.", 5, 50, 5);
                        owner.playSound(event.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                        Gamble.instance.econ.depositPlayer(owner, Hashmap.getbetmoney(owner));
                        Hashmap.removeplayer(owner);
                        Hashmap.cardsel.remove(owner.getUniqueId().toString());
                        owner.closeInventory();

                    }
                    Hashmap.delcardrequest(p.getUniqueId().toString());

                }
                Logger.addlog(p.getName() + "님이 카드 도박 도중 GUI를 닫음");
            }

        }
        }*/
    }
