package com.division.command;

import com.division.Gamble;
import com.division.data.CardData;
import com.division.data.DataManager;
import com.division.data.GameData;
import com.division.data.IndianData;
import com.division.file.GambleLogger;
import com.division.game.gambles.*;
import com.division.util.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GambleCommand implements CommandExecutor {

    private Gamble Plugin;
    private String header;

    public GambleCommand(Gamble Plugin) {
        this.Plugin = Plugin;
        header = DataManager.getInstance().getHeader();
    }

    public enum DenyReason {
        HAND,
        SLEEP,
        JOIN,
        NONE,
        BLACK
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arg) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            DenyReason reason = checkPlayer(p); //API 사용부분
            if (reason != DenyReason.NONE)
                sendDenyMessage(p, reason);
            else if (arg.length == 0) {
                if (p.isOp())
                    p.sendMessage(header + "§f/도박 <슬롯머신/주사위/블랙잭/룰렛/동전/인디언포커/카드/블랙리스트/저장/리로드>");
                else
                    p.sendMessage(header + "§f/도박 <슬롯머신/주사위/블랙잭/룰렛/동전/인디언포커/카드>");
                p.sendMessage(header + "§c게임의 GUI가 닫혀 돈이 증발하는 문제는 개인의 잘못으로 간주하여 복구해드리지 않습니다.");
            }
            else {
                if (checkParameter(arg[0])) {
                    if (arg[0].equalsIgnoreCase("슬롯머신")) {
                        new SlotMachine(p.getUniqueId(), Plugin);
                    }
                    else if (arg[0].equalsIgnoreCase("주사위")) {
                        if (arg.length == 1) {
                            p.sendMessage(header + "§f/도박 주사위 <룰/베팅> <금액>");
                        }
                        else {
                            if (arg[1].equalsIgnoreCase("룰") || arg[1].equalsIgnoreCase("베팅")) {
                                if (arg[1].equalsIgnoreCase("룰")) {
                                    p.sendMessage(" ");
                                    p.sendMessage(header + "§f/도박 주사위 베팅 <금액> 명령어로 게임을 시작합니다.");
                                    p.sendMessage(header + "§f게임은 컴퓨터와 1대1로 진행되며 룰을 직접 선택할 수 있습니다.");
                                    p.sendMessage(header + "§b컴퓨터는 베팅액의 0.5 ~ 1.5배를 추가로 베팅합니다.");
                                    p.sendMessage(header + "§f승자는 게임에 베팅된 총 금액을 가져가게 됩니다.");
                                    p.sendMessage(header + "§f서로 비긴경우 총 베팅된 금액의 절반을 가져가게 됩니다.");
                                    p.sendMessage(header + "§f룰은 하이 / 미드 / 로우로 나뉩니다.");
                                    p.sendMessage(header + "§e로우 §7: §f주사위를 던졌을때 낮은 숫자를 가진 사람이 승리합니다.");
                                    p.sendMessage(header + "§6미드 §7: §f주사위를 던졌을때 50에 가까운 숫자를 가진 사람이 승리합니다.");
                                    p.sendMessage(header + "§c하이 §7: §f주사위를 던졌을때 높은 숫자를 가진 사람이 승리합니다");
                                    p.sendMessage(header + "§f주사위는 1 ~ 99 사이의 랜덤한 숫자가 나옵니다.");
                                    p.sendMessage(" ");
                                }
                                else {
                                    if (arg.length == 2) {
                                        p.sendMessage(header + "§f베팅 금액을 적어주세요.");
                                    }
                                    else {
                                        if (checkIntParameter(arg[2])) {
                                            int value = Integer.parseInt(arg[2]);
                                            if (EconomyAPI.getInstance().getMoney(p) < value) {
                                                p.sendMessage(header + "§c돈이 부족합니다..");
                                            }
                                            else if (value < DataManager.getInstance().getDiceMin()) {
                                                p.sendMessage(header + "§c최소 베팅금액보다 적습니다. §7[ §6" + DataManager.getInstance().getDiceMin() + "§f원 §7]");
                                            }
                                            else if (value > DataManager.getInstance().getDiceMax()) {
                                                p.sendMessage(header + "§c최대 베팅금액보다 많습니다. §7[ §6" + DataManager.getInstance().getDiceMax() + "§f원 §7]");
                                            }
                                            else {
                                                GameData.getInstance().stopGame(p.getUniqueId()); //버그 방지를 위한 값 임시 제거
                                                new Dice(p.getUniqueId(), value, Plugin);
                                            }
                                        }
                                        else
                                            p.sendMessage(header + "§c숫자만 입력해주세요!");
                                    }
                                }
                            }
                        }

                    }

                    else if (arg[0].equalsIgnoreCase("블랙잭")) {
                        if (arg.length == 1) {
                            p.sendMessage(header + "§f/도박 블랙잭 <룰/베팅> <금액>");
                        }
                        else {
                            if (arg[1].equalsIgnoreCase("룰") || arg[1].equalsIgnoreCase("베팅")) {
                                if (arg[1].equalsIgnoreCase("룰")) {
                                    p.sendMessage(" ");
                                    p.sendMessage(header + ChatColor.WHITE + "/도박 블랙잭 베팅 <금액> 명령어로 블랙잭 게임을 시작합니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "컴퓨터는 딜러로, 추가적인 베팅을 진행하지 않습니다.");
                                    p.sendMessage(header + ChatColor.AQUA + "일반 승리시 베팅한 금액 만큼, 블랙잭시 베팅한 금액의 50%를 추가로 받습니다.");
                                    p.sendMessage(header + ChatColor.GREEN + "기본 룰은 지급 받은 카드의 숫자의 합을 21에 가깝게 유지하면 됩니다.");
                                    p.sendMessage(header + ChatColor.RED + "Hit 버튼을 우클릭 하게 될경우 베팅액의 100%를 추가로 베팅하며, 한장만 받고 Stay가 됩니다.");
                                    p.sendMessage(header + ChatColor.DARK_RED + "A는 1 또는 11, Q/J/K는 10으로 취급합니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "카드는 딜러와 플레이어 둘다 2장씩 지급되며, 원하는 만큼 뽑으실 수 있습니다.");
                                    p.sendMessage(header + ChatColor.RED + "단, 카드의 숫자합이 21을 넘어가는 순간 패배하게 됩니다. (버스트)");
                                    p.sendMessage(header + ChatColor.WHITE + "처음 지급받은 2개의 카드의 숫자합이 21이라면 블랙잭 승리가 됩니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "딜러가 블랙잭 승리일경우 절대 승리할 수 없습니다.");
                                    p.sendMessage(header + ChatColor.RED + "딜러의 처음 공개된 카드가 A / Q / J / K인경우 insurance(보험)을 들수 있습니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "보험의 경우 베팅금의 50%를 제거 하는대신 딜러가 블랙잭인 경우");
                                    p.sendMessage(header + ChatColor.WHITE + "보험 지불액의 2배 (원금)을 받게 됩니다. (딜러 A공개시 활성화)");
                                    p.sendMessage(header + ChatColor.WHITE + "Insurance가 활성화 되어있을시 당신의 패 (종이)를 클릭해서 거부할 수 있습니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "그 외의 경우 딜러의 카드 숫자합이 플레이어 보다 높은경우 딜러 승리,");
                                    p.sendMessage(header + ChatColor.WHITE + "플레이어 보다 낮은경우 플레이어 승리가 되며, 비길경우 베팅한 금액을 그대로 지급합니다.");
                                    p.sendMessage(header + ChatColor.AQUA + "딜러의 카드는 플레이어의 차례가 끝날때 공개하며, 버스트가 발생할 수도 있습니다.");
                                    p.sendMessage(" ");
                                }
                                else if (arg[1].equalsIgnoreCase("베팅")) {
                                    if (arg.length == 2) {
                                        p.sendMessage(header + "§f베팅 금액을 적어주세요. ");
                                    }
                                    else {
                                        if (checkIntParameter(arg[2])) {
                                            int value = Integer.parseInt(arg[2]);
                                            if (EconomyAPI.getInstance().getMoney(p) < value) {
                                                p.sendMessage(header + "§c돈이 부족합니다..");
                                            }
                                            else if (value < DataManager.getInstance().getBlackjackMin()) {
                                                p.sendMessage(header + ChatColor.RED + "최소 베팅금액보다 적습니다. §8[ §6" + DataManager.getInstance().getBlackjackMin() + "§f원 §8]");
                                            }
                                            else if (value > DataManager.getInstance().getBlackjackMax()) {
                                                p.sendMessage(header + ChatColor.RED + "최대 베팅금액보다 많습니다. §8[ §6" + DataManager.getInstance().getBlackjackMax() + "§f원 §8]");
                                            }
                                            else {
                                                EconomyAPI.getInstance().steelMoney(p, value);
                                                new Blackjack(p.getUniqueId(), Plugin, value);
                                                GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭에 " + value + "원 베팅");
                                            }
                                        }
                                        else
                                            p.sendMessage(header + "§c숫자만 입력해주세요!");
                                    }
                                }
                            }
                        }
                    }
                    else if (arg[0].equalsIgnoreCase("룰렛")) {
                        if (arg.length == 1)
                            p.sendMessage(header + "§f/도박 룰렛 <설명/베팅> <금액>");
                        else {
                            if (arg[1].equalsIgnoreCase("설명") || arg[1].equalsIgnoreCase("베팅")) {
                                if (arg[1].equalsIgnoreCase("설명")) {
                                    p.sendMessage(" ");
                                    p.sendMessage(header + ChatColor.AQUA + "/도박 룰렛 베팅 <금액> 명령어로 베팅 후 게임을 시작합니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "가운데에 위치한 버튼을 누를 시 룰렛이 돌아갑니다.");
                                    p.sendMessage(header + ChatColor.WHITE + "3개는 꽝, 나머지 3개는 배율이 적혀있습니다.");
                                    p.sendMessage(header + ChatColor.RED + "룰렛이 멈췄을때 붉게 표시된 부분의 보상을 흭득합니다.");
                                    p.sendMessage(header + ChatColor.DARK_RED + "배율이 적힌 부분에 멈췄을때, 베팅액 x 배율의 금액을 흭득합니다!");
                                    p.sendMessage(" ");
                                }
                                else {
                                    if (arg.length == 2) {
                                        p.sendMessage(header + ChatColor.WHITE + "베팅 금액을 적어주세요. ");
                                    }
                                    else {
                                        if (checkIntParameter(arg[2])) {
                                            int value = Integer.parseInt(arg[2]);
                                            if (EconomyAPI.getInstance().getMoney(p) < value) {
                                                p.sendMessage(header + "§c돈이 부족합니다..");
                                            }
                                            else if (value < DataManager.getInstance().getRouletteMin()) {
                                                p.sendMessage(header + ChatColor.RED + "최소 베팅금액보다 적습니다. §8[ §6" + DataManager.getInstance().getRouletteMin() + "§f원 §8]");
                                            }
                                            else if (value > DataManager.getInstance().getRouletteMax()) {
                                                p.sendMessage(header + ChatColor.RED + "최대 베팅금액보다 많습니다. §8[ §6" + DataManager.getInstance().getRouletteMax() + "§f원 §8]");
                                            }
                                            else {
                                                EconomyAPI.getInstance().steelMoney(p, value);
                                                new Roulette(p.getUniqueId(), Plugin, value);
                                                GambleLogger.getInstance().addLog(p.getName() + " 님이 룰렛에 " + value + "원 베팅");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (arg[0].equalsIgnoreCase("동전")) {
                        new Coin(p.getUniqueId(), Plugin);
                    }
                    else if (arg[0].equalsIgnoreCase("인디언포커")) {
                        if (arg.length == 1)
                            p.sendMessage(header + "§f/도박 인디언포커 <설명/수락/거부/시작>");
                        else if (arg.length == 2 && arg[1].equalsIgnoreCase("설명") || arg[1].equalsIgnoreCase("시작") || arg[1].equalsIgnoreCase("수락") || arg[1].equalsIgnoreCase("거부")) {
                            if (arg[1].equalsIgnoreCase("설명")) {
                                p.sendMessage(" ");
                                p.sendMessage(header + "§b/도박 인디언포커 시작 <대상> <최대금액> 명령어로 대상에게 1대1 인디언 포커 요청을 합니다.");
                                p.sendMessage(header + "§f두 사람 모두가 게임 불참상태 및 최대금액의 베팅액을 가지고 있어야 합니다.");
                                p.sendMessage(header + "§c게임 수락시 양측이 최대금액의 5%를 보증금으로 베팅하고 시작합니다.");
                                p.sendMessage(header + "§c중간에 GUI를 닫는등 게임이 강제 종료될경우 나간사람의 배팅액은 상대에게 들어갑니다.");
                                p.sendMessage(header + "§f게임이 시작될경우 상자 아이템을 클릭할경우 상대방의 패를 확인할 수 있습니다.");
                                p.sendMessage(header + "§c먼저 게임 요청자가 상대방의 패를 보고 베팅을 진행하게 됩니다.");
                                p.sendMessage(header + "§f레드스톤 램프인 경우 상대방의 차례, 레드스톤 블럭인경우 자신의 차례 입니다.");
                                p.sendMessage(header + "§f상대방의 배팅액은 레드스톤 램프 혹은 레드스톤 블럭 또는 베팅 버튼을 통해 확인할 수 있습니다.");
                                p.sendMessage(header + "§cBet(베팅)버튼을 누를경우 최대금액의 5%를 베팅하게 됩니다.");
                                p.sendMessage(header + "§bDie(다이)버튼을 누를경우 베팅을 포기하고 게임을 종료하게되며 베팅액은 살아있는 사람에게 돌아갑니다.");
                                p.sendMessage(header + "§f베팅 버튼을 눌러 충분히 베팅이 끝났으면 베팅 버튼을 우클릭해 차례를 끝냅니다.");
                                p.sendMessage(header + "§f베팅 버튼 1번 클릭당 최대 베팅금액의 5%씩 베팅이 됩니다. 만약 최대금액까지 남은금액이 5% 미만일경우");
                                p.sendMessage(header + "§f최대금액에 맞춰 베팅이 진행됩니다. (10%가 50원, 최대금액까지 남은 금액 30원 -> 30원 베팅)");
                                p.sendMessage(header + "§f게임 요청자가 베팅을 완료할경우 상대방의 차례가 됩니다.");
                                p.sendMessage(header + "§f상대방은 이전 차례의 사람이 베팅한 금액 만큼 배팅하고 오픈(콜) 상태로 베팅을 종료하거나");
                                p.sendMessage(header + "§f상대방의 베팅액보다 더 많은 액수를 베팅한다음 베팅을 종료하여 베팅을 이어나갈 수 있습니다.");
                                p.sendMessage(header + "§b패는 1~10사이의 숫자로 결정되며, 상대방보다 자신이 높은 숫자를 가지고 있으면 승리합니다.");
                                p.sendMessage(header + "§f만약 베팅액이 최대금액에 다다랐을경우 추가 베팅은 불가능하며 베팅 종료를 할 수 있습니다.");
                                p.sendMessage(header + "§4본 도박 시스템을 이용하여 고의적인 어뷰징등과 같은 행위 발생시 명령어 악용으로 처벌될 수 있습니다.");

                            }
                            else if (arg[1].equalsIgnoreCase("수락")) {
                                IndianData.RequestType type = IndianData.getInstance().findPlayer(p.getUniqueId());
                                switch (type) {
                                    case REQUESTER:
                                        p.sendMessage(header + "§b이미 다른사람에게 요청중인 게임이 존재합니다.");
                                        break;
                                    case TARGET:
                                        Player target = Bukkit.getPlayer(IndianData.getInstance().getTarget(p.getUniqueId()));
                                        if (target == null) {
                                            p.sendMessage(header + "§c해당 플레이어가 온라인이 아니므로 요청을 삭제합니다.");
                                            IndianData.getInstance().remove(p.getUniqueId());
                                        }
                                        else if (checkPlayer(target) != DenyReason.NONE) {
                                            p.sendMessage(header + "§c대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                            IndianData.getInstance().remove(p.getUniqueId());
                                        }
                                        else if (EconomyAPI.getInstance().getMoney(p) < IndianData.getInstance().getMoney(target.getUniqueId()) || EconomyAPI.getInstance().getMoney(target) < IndianData.getInstance().getMoney(target.getUniqueId())) {
                                            p.sendMessage(header + "§c해당 플레이어 혹은 당신의 돈이 모자라므로 요청이 삭제됩니다.");
                                            IndianData.getInstance().remove(p.getUniqueId());
                                        }
                                        else {
                                            int val = IndianData.getInstance().getMoney(target.getUniqueId()) / 20;
                                            EconomyAPI.getInstance().steelMoney(p, val);
                                            EconomyAPI.getInstance().steelMoney(target, val);
                                            new Indian(target.getUniqueId(), p.getUniqueId(), Plugin);
                                            GambleLogger.getInstance().addLog(p.getName() + "님이 " + target.getName() + " 의 인디언포커 요청 수락");
                                        }
                                        break;
                                    case NONE:
                                        p.sendMessage(header + "§c현재 들어온 요청이 없습니다.");
                                        break;
                                }
                            }
                            else if (arg[1].equalsIgnoreCase("거부")) {
                                if (IndianData.getInstance().findPlayer(p.getUniqueId()) != IndianData.RequestType.NONE) {
                                    IndianData.getInstance().remove(p.getUniqueId());
                                    p.sendMessage(header + ChatColor.RED + "모든 요청을 제거하였습니다.");
                                    GambleLogger.getInstance().addLog(p.getName() + "님이 인디언포커 요청 거부");
                                }
                                else
                                    p.sendMessage(header + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                            }
                            else {
                                if (arg.length == 4) {
                                    if (checkIntParameter(arg[3])) {
                                        int value = Integer.parseInt(arg[3]);
                                        if (value < DataManager.getInstance().getIndianMin())
                                            p.sendMessage(header + "§c최소금액보다 적습니다.. §7[ §f" + DataManager.getInstance().getIndianMin() + " §7]");
                                        else if (value > DataManager.getInstance().getIndianMax())
                                            p.sendMessage(header + "§c최대금액보다 많습니다.. §7[ §f" + DataManager.getInstance().getIndianMax() + " §7]");
                                        else {
                                            Player target = Bukkit.getPlayer(arg[2]);
                                            if (target == null || p.getName().equalsIgnoreCase(target.getName()))
                                                p.sendMessage(header + "§4해당 플레이어는 온라인이 아닙니다..");
                                            else {
                                                if (checkPlayer(target) != DenyReason.NONE)
                                                    p.sendMessage(header + "§c대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                                else if (EconomyAPI.getInstance().getMoney(target) < value || EconomyAPI.getInstance().getMoney(p) < value)
                                                    p.sendMessage(header + "§c대상 플레이어나 플레이어가 최대 금액을 보유하고 있지 않습니다.");
                                                else {
                                                    if (IndianData.getInstance().findPlayer(target.getUniqueId()) != IndianData.RequestType.NONE || IndianData.getInstance().findPlayer(p.getUniqueId()) != IndianData.RequestType.NONE)
                                                        p.sendMessage(header + "§c이미 게임을 요청한 사람이 존재합니다.");
                                                    else {
                                                        IndianData.getInstance().addValue(p.getUniqueId(), target.getUniqueId(), value);
                                                        p.sendMessage(header + "§b" + target.getName() + " §f님에게 §c인디언 포커 §f요청을 넣었습니다.");
                                                        target.sendMessage(header + "§b" + p.getName() + " §f님이 당신에게 인디언 포커를 요청하였습니다. §7[ §6" + value + "§f원 §7]");
                                                        target.sendMessage(header + "§c수락을 원하시면 /도박 인디언포커 수락, §b거절을 원하시면 /도박 인디언포커 거부");
                                                        GambleLogger.getInstance().addLog(p.getName() + "님이 " + target.getName() + " 에게 인디언포커 요청");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                        p.sendMessage(header + "§c숫자만 입력하세요..");
                                }
                                else
                                    p.sendMessage(header + "§f/도박 인디언포커 시작 <대상> <금액>");
                            }
                        }
                    }
                    else if (arg[0].equalsIgnoreCase("카드")) {
                        if (arg.length == 1) {
                            p.sendMessage(header + "§f/도박 카드 <설명/수락/거부/시작> <대상> <금액>");
                        }
                        else if (arg.length == 2 && arg[1].equalsIgnoreCase("설명") || arg[1].equalsIgnoreCase("시작") || arg[1].equalsIgnoreCase("수락") || arg[1].equalsIgnoreCase("거부")) {
                            if (arg[1].equalsIgnoreCase("설명")) {
                                p.sendMessage(" ");
                                p.sendMessage(header + "§b/도박 카드 시작 <대상> <최대금액> 명령어로 대상에게 1대1 카드 도박 요청을 합니다.");
                                p.sendMessage(header + "§f게임 시작후 자신의 턴에 3장의 카드중 하나를 고를 수 있습니다.");
                                p.sendMessage(header + "§b카드는 King(왕), Citizen(시민), Slave(노예) 카드가 있습니다.");
                                p.sendMessage(header + "§f카드를 고른후 상대방의 턴으로 넘어가게 되며, 위에 셜커상자의 색이 붉은색일경우 상대 턴, 초록색인경우 자신의 턴 입니다.");
                                p.sendMessage(header + "§f양측이 카드를 모두 선택하셨을경우 카드의 우열을 가리게 됩니다.");
                                p.sendMessage(header + "§cKing(왕) 카드는 Citizen(시민) 카드를 이기며, Citizen(시민) 카드는 Slave(노예) 카드를 이깁니다.");
                                p.sendMessage(header + "§c그리고 Slave(노예) 카드는 King(왕) 카드를 이기게 됩니다. [ 왕 > 시민 > 노예 > 왕 ]");
                                p.sendMessage(header + "§cSlave(노예)는 King(왕) 카드를 이기지만, 시민(Citizen)의 카드는 이기지 못합니다.");
                                p.sendMessage(header + "§f왕은 보, 시민은 바위, 노예는 가위 인 가위바위보라고 생각하시면 됩니다.");
                                p.sendMessage(header + "§f승자는 배팅액을 전부 가져가고, 비길시 자신의 배팅액만 가져갑니다.");
                                p.sendMessage(" ");
                            }
                            else if (arg[1].equalsIgnoreCase("수락")) {
                                CardData.ResponseType type = CardData.getInstance().findPlayer(p.getUniqueId());
                                switch (type) {
                                    case REQUESTER:
                                        p.sendMessage(header + ChatColor.AQUA + "이미 다른사람에게 요청중인 게임이 존재합니다.");
                                        break;
                                    case TARGET:
                                        Player target = Bukkit.getPlayer(CardData.getInstance().getTarget(p.getUniqueId()));
                                        if (target == null) {
                                            p.sendMessage(header + "§c해당 플레이어가 온라인이 아니므로 요청을 삭제합니다.");
                                            CardData.getInstance().remove(p.getUniqueId());
                                        }
                                        else if (checkPlayer(target) != DenyReason.NONE) {
                                            p.sendMessage(header + "§c대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                            CardData.getInstance().remove(p.getUniqueId());
                                        }
                                        else if (EconomyAPI.getInstance().getMoney(p) < CardData.getInstance().getMoney(target.getUniqueId()) || EconomyAPI.getInstance().getMoney(target) < CardData.getInstance().getMoney(target.getUniqueId())) {
                                            p.sendMessage(header + ChatColor.RED + "해당 플레이어 혹은 당신의 돈이 모자라므로 요청이 삭제됩니다.");
                                            CardData.getInstance().remove(p.getUniqueId());
                                        }
                                        else {
                                            EconomyAPI.getInstance().steelMoney(p, CardData.getInstance().getMoney(target.getUniqueId()));
                                            EconomyAPI.getInstance().steelMoney(target, CardData.getInstance().getMoney(target.getUniqueId()));
                                            new CardGamble(target.getUniqueId(), p.getUniqueId(), CardData.getInstance().getMoney(target.getUniqueId()), Plugin);
                                            GambleLogger.getInstance().addLog(p.getName() + "님이 " + target.getName() + "님의 카드도박 요청 수락");
                                        }
                                        break;
                                    case NONE:
                                        p.sendMessage(header + "§c현재 들어온 요청이 없습니다.");
                                        break;
                                }


                        }
                        else if (arg[1].equalsIgnoreCase("거부")) {
                            if (CardData.getInstance().findPlayer(p.getUniqueId()) != CardData.ResponseType.NONE) {
                                CardData.getInstance().remove(p.getUniqueId());
                                p.sendMessage(header + "§c모든 요청을 제거하였습니다.");
                                GambleLogger.getInstance().addLog(p.getName() + "님이 카드도박 요청 취소");
                            }
                            else
                                p.sendMessage(header + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                        }
                        else {
                            if (arg.length == 4) {
                                if (checkIntParameter(arg[3])) {
                                    int value = Integer.parseInt(arg[3]);
                                    if (value < DataManager.getInstance().getCardMin())
                                        p.sendMessage(header + "§c최소금액보다 적습니다.. §7[ §f" + DataManager.getInstance().getCardMin() + " §7]");
                                    else if (value > DataManager.getInstance().getCardMax())
                                        p.sendMessage(header + "§c최대금액보다 많습니다.. §7[ §f" + DataManager.getInstance().getCardMax() + " §7]");
                                    else {
                                        Player target = Bukkit.getServer().getPlayer(arg[2]);
                                        if (target == null || p.getName().equalsIgnoreCase(target.getName()))
                                            p.sendMessage(header + ChatColor.DARK_RED + "해당 플레이어는 온라인이 아닙니다..");
                                        else {
                                            if (checkPlayer(target) != DenyReason.NONE) {
                                                p.sendMessage(header + ChatColor.RED + "대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                            }
                                            else if (EconomyAPI.getInstance().getMoney(p) < value || EconomyAPI.getInstance().getMoney(target) < value)
                                                p.sendMessage(header + ChatColor.RED + "대상 플레이어나 플레이어가 최대 금액을 보유하고 있지 않습니다.");
                                            else {
                                                if (CardData.getInstance().findPlayer(p.getUniqueId()) != CardData.ResponseType.NONE || CardData.getInstance().findPlayer(target.getUniqueId()) != CardData.ResponseType.NONE)
                                                    p.sendMessage(header + ChatColor.RED + "이미 게임을 요청한 사람이 존재합니다.");
                                                else {
                                                    CardData.getInstance().addValue(p.getUniqueId(), target.getUniqueId(), value);
                                                    p.sendMessage(header + "§b" + target.getName() + "§f님에게 §c카드 도박§f요청을 넣었습니다.");
                                                    target.sendMessage(header + "§b" + p.getName() + "§f님이 당신에게 카드 도박을 요청하였습니다. §7[ §6" + value + "§f원 §7]");
                                                    target.sendMessage(header + "§b수락을 원하시면 /도박 카드 수락, §b거절을 원하시면 /도박 카드 거부");
                                                    GambleLogger.getInstance().addLog(p.getName() + "님이 " + target.getName() + "에게 카드도박 요청");
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                    p.sendMessage(header + "§4숫자만 입력하세요..");
                            }
                            else
                                p.sendMessage(header + "§f/도박 카드 시작 <대상> <최대금액>");

                        }
                    }
                }
                    /*else if (arg[0].equalsIgnoreCase("블랙리스트")) {
                        if (p.isOp()) {
                            if (arg.length == 1) {
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                                p.sendMessage(" ");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 추가 <닉네임> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에 추가합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 삭제 <번호> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에서 삭제합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 목록 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 확인합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 초기화 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 초기화 합니다.");
                                p.sendMessage(" ");
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                            }
                            else if (arg[1].equalsIgnoreCase("추가") || arg[1].equalsIgnoreCase("삭제") || arg[1].equalsIgnoreCase("목록") || arg[1].equalsIgnoreCase("초기화")) {
                                if (arg[1].equalsIgnoreCase("추가")) {
                                    if (arg.length == 2) {
                                        p.sendMessage(header + ChatColor.AQUA + "/도박 블랙리스트 추가 <닉네임>");
                                    }
                                    else {
                                        Player target = Bukkit.getServer().getPlayer(arg[2]);
                                        if (target == null) {
                                            p.sendTitle(header, ChatColor.RED + "온라인 플레이어만 블랙리스트에 추가 할 수 있습니다..", 5, 50, 5);
                                        }
                                        else {
                                            Blacklist.addplayer(target);
                                            Logger.addlog(p.getName() + "님이 " + target.getName() + "님을 블랙리스트에 등재시킴.");
                                            p.sendTitle(header, ChatColor.WHITE + "플레이어 " + ChatColor.AQUA + target.getName() + ChatColor.WHITE + "를 " + ChatColor.GRAY + "블랙리스트" + ChatColor.WHITE + "에 추가 하였습니다.", 5, 50, 5);
                                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3f, 1f);
                                        }
                                    }
                                }
                                else if (arg[1].equalsIgnoreCase("삭제")) {
                                    if (arg.length == 2) {
                                        p.sendMessage(header + ChatColor.AQUA + "/도박 블랙리스트 삭제 <번호>");
                                    }
                                    else {
                                        try {
                                            int input = Integer.parseInt(arg[2]) - 1;
                                            if (Blacklist.isindexout(input)) {
                                                p.sendMessage(header + ChatColor.RED + "인덱스 범위를 벗어났습니다..");
                                            }
                                            else {
                                                Blacklist.removeindex(input);
                                                Logger.addlog(p.getName() + " 님이 블랙리스트의 " + (input + 1) + "번째 인덱스를 삭제함.");
                                                p.sendTitle(header, ChatColor.WHITE + "해당 " + ChatColor.AQUA + "플레이어" + ChatColor.WHITE + "를 " + ChatColor.GRAY + "블랙리스트" + ChatColor.WHITE + "에서 " + ChatColor.RED + "삭제 " + ChatColor.WHITE + "하였습니다.", 5, 50, 5);
                                            }
                                        }
                                        catch (NumberFormatException e) {
                                            p.sendMessage(header + ChatColor.RED + "숫자만 입력해주세요!");
                                        }
                                    }
                                }
                                else if (arg[1].equalsIgnoreCase("목록")) {
                                    if (Blacklist.isindexout(0)) {
                                        p.sendMessage(header + ChatColor.RED + "현재 블랙리스트 목록이 구성되어 있지 않습니다.");
                                    }
                                    else {
                                        int i, size;
                                        size = Gamble.blacklist.size();
                                        p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        p.sendMessage(" ");
                                        for (i = 0; i < size; i++) {
                                            p.sendMessage(ChatColor.DARK_GRAY + "[ " + ChatColor.WHITE + (i + 1) + ChatColor.DARK_GRAY + " ] " + ChatColor.AQUA + Gamble.blacklist.get(i) + " " + ChatColor.GRAY + "[ " + ChatColor.GOLD + Blacklist.getplayer(i) + ChatColor.GRAY + " ]");
                                        }
                                        p.sendMessage(" ");
                                        p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    }
                                }
                                else if (arg[1].equalsIgnoreCase("초기화")) {
                                    Blacklist.clear();
                                    p.sendTitle(header, ChatColor.DARK_RED + "도박 블랙리스트가 초기화 되었습니다!", 5, 50, 5);
                                    Logger.addlog(p.getName() + " 님이 블랙리스트를 초기화 함.");
                                }
                            }
                            else {
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                                p.sendMessage(" ");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 추가 <닉네임> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에 추가합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 삭제 <인덱스> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에서 삭제합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 목록 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 확인합니다.");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 블랙리스트 초기화 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 초기화 합니다.");
                                p.sendMessage(" ");
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                            }
                        }
                        else {
                            p.sendMessage(header + ChatColor.DARK_RED + "접근권한이 없습니다.");
                        }
                    }


                        else {
                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                            p.sendMessage(" ");
                            p.sendMessage(header + ChatColor.WHITE + "/도박 카드 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "카드 도박에 대한 설명을 듣습니다.");
                            p.sendMessage(header + ChatColor.WHITE + "/도박 카드 수락 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신에게 들어온 카드 도박을 수락합니다.");
                            p.sendMessage(header + ChatColor.WHITE + "/도박 카드 취소 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신이 요청한 카드 도박이나 자신에게 요청된 카드 도박을 거절합니다.");
                            p.sendMessage(header + ChatColor.WHITE + "/도박 카드 시작 <대상> <최대금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어에게 카드 도박을 요청합니다.");
                            p.sendMessage(" ");
                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                        }
                    }
                    else {
                        if (p.isOp()) {
                            if (arg.length == 1) {
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                                p.sendMessage(" ");
                                p.sendMessage(header + ChatColor.WHITE + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전/인디언포커/카드> <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 도박의 최대/최소 금액을 지정합니다.");
                                p.sendMessage(header + ChatColor.AQUA + "슬롯머신은 최소 금액 (1회당 사용금액)만 지정 가능합니다.");
                                p.sendMessage(header + ChatColor.RED + "/도박 금액 누적금액 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "슬롯머신에 누적된 금액을 지정합니다.");
                                p.sendMessage(" ");
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                            }
                            else {
                                if (arg[1].equalsIgnoreCase("최대") || arg[1].equalsIgnoreCase("최소") || arg[1].equalsIgnoreCase("누적금액")) {
                                    if (arg[1].equalsIgnoreCase("최대")) {
                                        if (arg.length == 2) {
                                            p.sendMessage(header + ChatColor.AQUA + "/도박 금액 <최대> <주사위/블랙잭/룰렛> <금액>");
                                        }
                                        else {
                                            if (arg[2].equalsIgnoreCase("슬롯머신")) {
                                                p.sendMessage(header + ChatColor.RED + "슬롯머신은 최대 금액을 지정할 수 없습니다.");
                                            }
                                            else if (arg[2].equalsIgnoreCase("동전")) {
                                                p.sendMessage(header + ChatColor.RED + "동전 도박은 최대 금액을 지정할 수 없습니다.");
                                            }
                                            else if (arg[2].equalsIgnoreCase("주사위") || arg[2].equalsIgnoreCase("블랙잭") || arg[2].equalsIgnoreCase("룰렛") || arg[2].equalsIgnoreCase("인디언포커") || arg[2].equalsIgnoreCase("카드")) {
                                                if (arg[2].equalsIgnoreCase("주사위")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.dicemaximum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a < Gamble.diceminimum) {
                                                                p.sendMessage(header + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.dicemaximum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "주사위 도박" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("블랙잭")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.blackmaximum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a < Gamble.blackminimum) {
                                                                p.sendMessage(header + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.blackmaximum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "블랙잭" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("룰렛")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.rolletmaximum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a < Gamble.rolletminimum) {
                                                                p.sendMessage(header + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.rolletmaximum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "룰렛" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("인디언포커")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.indiammaximum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a < Gamble.indianminimum) {
                                                                p.sendMessage(header + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.indiammaximum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "인디언 포커" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }

                                                }
                                                else if (arg[2].equalsIgnoreCase("카드")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.cardmaximum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a < Gamble.cardminimum) {
                                                                p.sendMessage(header + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.cardmaximum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "카드 도박" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }

                                            }
                                            else {
                                                p.sendMessage(header + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                            }

                                        }
                                    }
                                    else if (arg[1].equalsIgnoreCase("최소")) {
                                        if (arg.length == 2) {
                                            p.sendMessage(header + ChatColor.AQUA + "/도박 금액 <최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                        }
                                        else {
                                            if (arg[2].equalsIgnoreCase("주사위") || arg[2].equalsIgnoreCase("블랙잭") || arg[2].equalsIgnoreCase("슬롯머신") || arg[2].equalsIgnoreCase("룰렛") || arg[2].equalsIgnoreCase("동전") || arg[2].equalsIgnoreCase("인디언포커") || arg[2].equalsIgnoreCase("카드")) {
                                                if (arg[2].equalsIgnoreCase("슬롯머신")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.slotneed + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else {
                                                                Gamble.slotneed = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "슬롯머신" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "1회당 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("주사위")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.diceminimum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a > Gamble.dicemaximum) {
                                                                p.sendMessage(header + ChatColor.RED + "최대금액 보다 큰 값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.diceminimum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "주사위 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("룰렛")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.rolletminimum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a > Gamble.rolletmaximum) {
                                                                p.sendMessage(header + ChatColor.RED + "최대금액 보다 큰값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.rolletminimum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "룰렛" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("동전")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.coinneed + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else {
                                                                Gamble.coinneed = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "동전 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "1회당 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("인디언포커")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.indianminimum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else {
                                                                Gamble.indianminimum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "인디언 포커" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else if (arg[2].equalsIgnoreCase("카드")) {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.cardminimum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else {
                                                                Gamble.cardminimum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "카드 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                                else {
                                                    if (arg.length == 3) {
                                                        p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.blackminimum + " 원");
                                                    }
                                                    else {
                                                        try {
                                                            int a = Integer.parseInt(arg[3]);
                                                            if (a <= 0) {
                                                                p.sendMessage(header + ChatColor.RED + "값이 이상합니다..");
                                                            }
                                                            else if (a > Gamble.blackmaximum) {
                                                                p.sendMessage(header + ChatColor.RED + "최대금액 보다 큰값을 입력하셨습니다.");
                                                            }
                                                            else {
                                                                Gamble.blackminimum = a;
                                                                p.sendTitle(header, ChatColor.AQUA + "블랙잭" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                            }
                                                        }
                                                        catch (NumberFormatException e) {
                                                            p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                p.sendMessage(header + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                            }
                                        }
                                    }
                                    else {
                                        if (arg.length == 2) {
                                            p.sendMessage(header + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.GREEN + "현재 누적 금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Gamble.slotaccumoney + " 원");
                                        }
                                        else {
                                            try {
                                                int a = Integer.parseInt(arg[2]);
                                                if (a < 1) {
                                                    p.sendMessage(header + ChatColor.RED + "최소 1원이상으로 설정해야 합니다..");
                                                }
                                                else {
                                                    Gamble.slotaccumoney = a;
                                                    p.sendTitle(header, ChatColor.AQUA + "슬롯머신" + ChatColor.WHITE + "의 " + ChatColor.DARK_RED + "누적 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                }
                                            }
                                            catch (NumberFormatException e) {
                                                p.sendMessage(header + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                            }
                                        }
                                    }
                                }
                                else {
                                    p.sendMessage(header + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                }
                            }
                        }
                        else {
                            p.sendMessage(header + ChatColor.DARK_RED + "접근권한이 없습니다.");
                        }
                    }*/
            }
        }
        return true;
    }
        return false;
}

    public DenyReason checkPlayer(Player p) {
        if (p.getInventory().getItemInMainHand().getType() != Material.AIR || p.getInventory().getItemInOffHand().getType() != Material.AIR)
            return DenyReason.HAND;
        else if (p.isSleeping())
            return DenyReason.SLEEP;
        /*else if (GameVars.GamePlayers.contains(p.getUniqueId()) || GameVars.WaitPlayers.contains(p.getUniqueId()))
            return DenyReason.JOIN;*/
        else if (DataManager.getInstance().containBlackList(p.getUniqueId()))
            return DenyReason.BLACK;
        else
            return DenyReason.NONE;
    }

    public void sendDenyMessage(Player p, DenyReason reason) {
        switch (reason) {
            case HAND:
                p.sendTitle(header, "§b손에 아무것도 들지 않은 상태에서 진행해주세요", 5, 50, 5);
                break;
            case SLEEP:
                p.sendTitle(header, "§b잠을 자지 않는 상태에서 진행해주세요", 5, 50, 5);
                break;
            case JOIN:
                p.sendTitle(header, "§b게임에 참여하지 않은 상태에서 진행해주세요 </나가기>", 5, 50, 5);
                break;
            case BLACK:
                p.sendTitle(header, "§c당신은 도박 블랙리스트에 등재되어 있습니다.", 5, 50, 5);
                break;
        }
    }

    public boolean checkParameter(String value) {
        return value.equalsIgnoreCase("슬롯머신") || value.equalsIgnoreCase("주사위") || value.equalsIgnoreCase("블랙잭") || value.equalsIgnoreCase("룰렛") || value.equalsIgnoreCase("인디언포커") || value.equalsIgnoreCase("카드") || value.equalsIgnoreCase("동전") || value.equalsIgnoreCase("블랙리스트") || value.equalsIgnoreCase("저장") || value.equalsIgnoreCase("리로드");
    }

    public boolean checkIntParameter(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
