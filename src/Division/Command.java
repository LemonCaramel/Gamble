package Division;

import moe.caramel.counterzombie.database.GameVars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Command implements CommandExecutor {
    private Main instance;
    private Player p;
    public static String st = ChatColor.RED + "[ " + ChatColor.WHITE + "Gambling " + ChatColor.RED + "] ";
    public Command(){
        instance = JavaPlugin.getPlugin(Main.class);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("도박")){
            if (commandSender instanceof ConsoleCommandSender){
                commandSender.sendMessage(st + ChatColor.WHITE + "콘솔은 사용 불가능한 명령어 입니다.");
            }
            else {
                p = (Player) commandSender;
                if (Blacklist.findplayer(p)) {
                    p.sendTitle(st, ChatColor.RED + "당신은 블랙리스트에 등재되어 있습니다.", 5, 50, 5);
                }
                else if (moe.caramel.counterzombie.database.GameVars.isGameing  && moe.caramel.counterzombie.database.GameVars.GamePlayers.contains(p.getName()) && GameVars.WaitPlayers.contains(p.getName())){
                    p.sendTitle(st, ChatColor.RED + "게임도중 도박을 진행 하실 수 없습니다. /나가기", 5, 50, 5);
                }
                else if (strings.length == 0) {
                    commandSender.sendMessage(ChatColor.WHITE + "======================================================================");
                    commandSender.sendMessage(" ");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 슬롯머신 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "슬롯머신 도박 GUI를 오픈합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 주사위 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "주사위 도박 명령어를 확인합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 블랙잭 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙잭 도박 명령어를 확인합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "룰렛 도박 명령어를 확인합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 동전 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "동전 뒤집기 도박 GUI를 오픈합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "인디언 포커 명령어를 확인합니다.");
                    commandSender.sendMessage(st + ChatColor.WHITE + "/도박 카드 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "카드 도박 명령어를 확인합니다.");
                    commandSender.sendMessage(st + ChatColor.RED + "모든 도박은 게임 도중 GUI를 강제로 닫을경우 돈이 증발합니다.");
                    if (commandSender.isOp()) {
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "도박 블랙리스트를 관리합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 금액 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "도박에 사용되는 금액을 관리합니다.");
                    }
                    commandSender.sendMessage(" ");
                    commandSender.sendMessage(ChatColor.WHITE + "======================================================================");
                } else {
                    if (strings[0].equalsIgnoreCase("슬롯머신") || strings[0].equalsIgnoreCase("주사위") || strings[0].equalsIgnoreCase("블랙잭") || strings[0].equalsIgnoreCase("동전")|| strings[0].equalsIgnoreCase("룰렛") || strings[0].equalsIgnoreCase("블랙리스트") || strings[0].equalsIgnoreCase("금액") ||strings[0].equalsIgnoreCase("인디언포커") || strings[0].equalsIgnoreCase("카드")) {
                        if (strings[0].equalsIgnoreCase("슬롯머신")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                            } else {
                                Slot sl = new Slot();
                                sl.createslot(p);
                            }
                        } else if (strings[0].equalsIgnoreCase("주사위")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                            } else {
                                if (strings.length == 1) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 주사위 룰 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 게임의 룰을 확인합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 주사위 베팅 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "돈을 베팅하고 게임을 시작합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else {
                                    if (strings[1].equalsIgnoreCase("룰") || strings[1].equalsIgnoreCase("베팅")) {
                                        if (strings[1].equalsIgnoreCase("룰")) {
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                            p.sendMessage(" ");
                                            p.sendMessage(st + ChatColor.WHITE + "/도박 주사위 베팅 <금액> 명령어로 게임을 시작합니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "게임은 컴퓨터와 1대1로 진행되며 룰을 직접 선택할 수 있습니다.");
                                            p.sendMessage(st + ChatColor.AQUA + "컴퓨터는 베팅액의 0.5 ~ 1.5배를 추가로 베팅합니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "승자는 게임에 베팅된 총 금액을 가져가게 됩니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "서로 비긴경우 총 베팅된 금액의 절반을 가져가게 됩니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "룰은 하이 / 미드 / 로우로 나뉩니다.");
                                            p.sendMessage(st + ChatColor.YELLOW + "로우 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "주사위를 던졌을때 낮은 숫자를 가진 사람이 승리합니다.");
                                            p.sendMessage(st + ChatColor.GOLD + "미드 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "주사위를 던졌을때 50에 가까운 숫자를 가진 사람이 승리합니다.");
                                            p.sendMessage(st + ChatColor.RED + "하이" + ChatColor.GRAY + ": " + ChatColor.WHITE + "주사위를 던졌을때 높은 숫자를 가진 사람이 승리합니다");
                                            p.sendMessage(st + ChatColor.WHITE + "주사위는 1 ~ 99 사이의 랜덤한 숫자가 나옵니다.");
                                            p.sendMessage(" ");
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        } else {
                                            if (strings.length == 2) {
                                                p.sendMessage(st + ChatColor.WHITE + "베팅 금액을 적어주세요. " + ChatColor.RED + "최소금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.diceminimum + ChatColor.WHITE + "원, " + ChatColor.AQUA + "최대금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.dicemaximum + ChatColor.WHITE + "원");
                                            } else {
                                                try {
                                                    int bet = Integer.parseInt(strings[2]);
                                                    if (instance.econ.getBalance(p) < bet) {
                                                        p.sendMessage(st + ChatColor.RED + "돈이 부족합니다..");
                                                    } else if (bet < Main.diceminimum) {
                                                        p.sendMessage(st + ChatColor.RED + "최소 베팅금액보다 적습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.diceminimum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else if (bet > Main.dicemaximum) {
                                                        p.sendMessage(st + ChatColor.RED + "최대 베팅금액보다 많습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.dicemaximum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else {
                                                        Hashmap.removeplayer(p);
                                                        Chooserole role = new Chooserole(p);
                                                        role.dicerole();
                                                        Hashmap.addplayer(p, bet);
                                                        Logger.addlog(p.getName() + "님이 주사위 도박에 " + bet + "원 베팅");
                                                    }
                                                } catch (NumberFormatException e) {
                                                    p.sendMessage(st + ChatColor.RED + "숫자만 입력해주세요!");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (strings[0].equalsIgnoreCase("블랙잭")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                            } else {
                                if (strings.length == 1) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙잭 룰 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 게임의 룰을 확인합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙잭 베팅 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "돈을 베팅하고 게임을 시작합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else {
                                    if (strings[1].equalsIgnoreCase("룰") || strings[1].equalsIgnoreCase("베팅")) {
                                        if (strings[1].equalsIgnoreCase("룰")) {
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                            p.sendMessage(" ");
                                            p.sendMessage(st + ChatColor.WHITE + "/도박 블랙잭 베팅 <금액> 명령어로 블랙잭 게임을 시작합니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "컴퓨터는 딜러로, 추가적인 베팅을 진행하지 않습니다.");
                                            p.sendMessage(st + ChatColor.AQUA + "일반 승리시 베팅한 금액 만큼, 블랙잭시 베팅한 금액의 50%를 추가로 받습니다.");
                                            p.sendMessage(st + ChatColor.GREEN + "기본 룰은 지급 받은 카드의 숫자의 합을 21에 가깝게 유지하면 됩니다.");
                                            p.sendMessage(st + ChatColor.DARK_RED + "A는 11, Q/J/K는 10으로 취급합니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "카드는 딜러와 플레이어 둘다 2장씩 지급되며, 원하는 만큼 뽑으실 수 있습니다.");
                                            p.sendMessage(st + ChatColor.RED + "단, 카드의 숫자합이 21을 넘어가는 순간 패배하게 됩니다. (버스트)");
                                            p.sendMessage(st + ChatColor.WHITE + "처음 지급받은 2개의 카드의 숫자합이 21이라면 블랙잭 승리가 됩니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "딜러가 블랙잭 승리일경우 절대 승리할 수 없습니다.");
                                            p.sendMessage(st + ChatColor.RED + "딜러의 처음 공개된 카드가 A / Q / J / K인경우 insurance(보험)을 들수 있습니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "보험의 경우 베팅금의 50%를 제거 하는대신 딜러가 블랙잭인 경우");
                                            p.sendMessage(st + ChatColor.WHITE + "보험 지불액의 2배 (원금)을 받게 됩니다. (딜러 A공개시 활성화)");
                                            p.sendMessage(st + ChatColor.WHITE + "Insurance가 활성화 되어있을시 당신의 패 (종이)를 클릭해서 거부할 수 있습니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "그 외의 경우 딜러의 카드 숫자합이 플레이어 보다 높은경우 딜러 승리,");
                                            p.sendMessage(st + ChatColor.WHITE + "플레이어 보다 낮은경우 플레이어 승리가 되며, 비길경우 베팅한 금액을 그대로 지급합니다.");
                                            p.sendMessage(st + ChatColor.AQUA + "딜러의 카드는 플레이어의 차례가 끝날때 공개하며, 버스트가 발생할 수도 있습니다.");
                                            p.sendMessage(" ");
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        } else if (strings[1].equalsIgnoreCase("베팅")) {
                                            if (strings.length == 2) {
                                                p.sendMessage(st + ChatColor.WHITE + "베팅 금액을 적어주세요. " + ChatColor.RED + "최소금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.blackminimum + ChatColor.WHITE + "원, " + ChatColor.AQUA + "최대금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.blackmaximum + ChatColor.WHITE + "원");
                                            } else {
                                                try {
                                                    int betmoney = Integer.parseInt(strings[2]);
                                                    if (instance.econ.getBalance(p) < betmoney) {
                                                        p.sendMessage(st + ChatColor.RED + "돈이 부족합니다..");
                                                    } else if (betmoney < Main.blackminimum) {
                                                        p.sendMessage(st + ChatColor.RED + "최소 베팅금액보다 적습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.blackminimum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else if (betmoney > Main.blackmaximum) {
                                                        p.sendMessage(st + ChatColor.RED + "최대 베팅금액보다 많습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.blackmaximum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else {
                                                        instance.econ.withdrawPlayer(p, betmoney);
                                                        Hashmap.getcard(p);
                                                        Hashmap.addplayer(p, betmoney);
                                                        Blackjack jack = new Blackjack(p);
                                                        jack.openblack();
                                                        Logger.addlog(p.getName() + "님이 블랙잭에 " + betmoney + "원 베팅");
                                                    }
                                                } catch (NumberFormatException e) {
                                                    p.sendMessage(st + ChatColor.RED + "숫자만 입력해주세요!");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (strings[0].equalsIgnoreCase("룰렛")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                            } else {
                                if (strings.length == 1) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "룰렛에 대한 설명을 확인합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 베팅 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "돈을 베팅하고 게임을 시작합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else {
                                    if (strings[1].equalsIgnoreCase("설명") || strings[1].equalsIgnoreCase("베팅")) {
                                        if (strings[1].equalsIgnoreCase("설명")) {
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                            p.sendMessage(" ");
                                            p.sendMessage(st + ChatColor.AQUA + "/도박 룰렛 베팅 <금액> 명령어로 베팅 후 게임을 시작합니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "가운데에 위치한 버튼을 누를 시 룰렛이 돌아갑니다.");
                                            p.sendMessage(st + ChatColor.WHITE + "3개는 꽝, 나머지 3개는 배율이 적혀있습니다.");
                                            p.sendMessage(st + ChatColor.RED + "룰렛이 멈췄을때 붉게 표시된 부분의 보상을 흭득합니다.");
                                            p.sendMessage(st + ChatColor.DARK_RED + "배율이 적힌 부분에 멈췄을때, 베팅액 x 배율의 금액을 흭득합니다!");
                                            p.sendMessage(" ");
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        } else {
                                            if (strings.length == 2) {
                                                p.sendMessage(st + ChatColor.WHITE + "베팅 금액을 적어주세요. " + ChatColor.RED + "최소금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.rolletminimum + ChatColor.WHITE + "원, " + ChatColor.AQUA + "최대금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.rolletmaximum + ChatColor.WHITE + "원");
                                            } else {
                                                try {
                                                    int betmoney = Integer.parseInt(strings[2]);
                                                    if (instance.econ.getBalance(p) < betmoney) {
                                                        p.sendMessage(st + ChatColor.RED + "돈이 부족합니다..");
                                                    } else if (betmoney > Main.rolletmaximum) {
                                                        p.sendMessage(st + ChatColor.RED + "최대 베팅금액보다 많습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.rolletmaximum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else if (betmoney < Main.rolletminimum) {
                                                        p.sendMessage(st + ChatColor.RED + "최소 베팅금액보다 적습니다. " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + Main.rolletminimum + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " ]");
                                                    } else {
                                                        instance.econ.withdrawPlayer(p, betmoney);
                                                        Hashmap.addplayer(p, betmoney);
                                                        rollet rol = new rollet();
                                                        rol.openrollet(p);
                                                        Logger.addlog(p.getName() + " 님이 룰렛에 " + betmoney + "원 베팅");
                                                    }
                                                } catch (NumberFormatException e) {
                                                    p.sendMessage(st + ChatColor.RED + "숫자만 입력해주세요!");
                                                }
                                            }
                                        }
                                    } else {
                                        p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        p.sendMessage(" ");
                                        p.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "룰렛에 대한 설명을 확인합니다.");
                                        p.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 베팅 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "돈을 베팅하고 게임을 시작합니다.");
                                        p.sendMessage(" ");
                                        p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    }
                                }
                            }
                        } else if (strings[0].equalsIgnoreCase("동전")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                            } else {
                                coingui coin = new coingui();
                                coin.opengui(p);
                            }
                        } else if (strings[0].equalsIgnoreCase("블랙리스트")) {
                            if (p.isOp()) {
                                if (strings.length == 1) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 추가 <닉네임> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에 추가합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 삭제 <번호> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에서 삭제합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 목록 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 확인합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 초기화 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 초기화 합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else if (strings[1].equalsIgnoreCase("추가") || strings[1].equalsIgnoreCase("삭제") || strings[1].equalsIgnoreCase("목록") || strings[1].equalsIgnoreCase("초기화")) {
                                    if (strings[1].equalsIgnoreCase("추가")) {
                                        if (strings.length == 2) {
                                            p.sendMessage(st + ChatColor.AQUA + "/도박 블랙리스트 추가 <닉네임>");
                                        } else {
                                            Player target = Bukkit.getServer().getPlayer(strings[2]);
                                            if (target == null) {
                                                p.sendTitle(st, ChatColor.RED + "온라인 플레이어만 블랙리스트에 추가 할 수 있습니다..", 5, 50, 5);
                                            } else {
                                                Blacklist.addplayer(target);
                                                Logger.addlog(p.getName() + "님이 " + target.getName() + "님을 블랙리스트에 등재시킴.");
                                                p.sendTitle(st, ChatColor.WHITE + "플레이어 " + ChatColor.AQUA + target.getName() + ChatColor.WHITE + "를 " + ChatColor.GRAY + "블랙리스트" + ChatColor.WHITE + "에 추가 하였습니다.", 5, 50, 5);
                                                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3f, 1f);
                                            }
                                        }
                                    } else if (strings[1].equalsIgnoreCase("삭제")) {
                                        if (strings.length == 2) {
                                            p.sendMessage(st + ChatColor.AQUA + "/도박 블랙리스트 삭제 <번호>");
                                        } else {
                                            try {
                                                int input = Integer.parseInt(strings[2]) - 1;
                                                if (Blacklist.isindexout(input)) {
                                                    p.sendMessage(st + ChatColor.RED + "인덱스 범위를 벗어났습니다..");
                                                } else {
                                                    Blacklist.removeindex(input);
                                                    Logger.addlog(p.getName() + " 님이 블랙리스트의 " + (input + 1) + "번째 인덱스를 삭제함.");
                                                    p.sendTitle(st, ChatColor.WHITE + "해당 " + ChatColor.AQUA + "플레이어" + ChatColor.WHITE + "를 " + ChatColor.GRAY + "블랙리스트" + ChatColor.WHITE + "에서 " + ChatColor.RED + "삭제 " + ChatColor.WHITE + "하였습니다.", 5, 50, 5);
                                                }
                                            } catch (NumberFormatException e) {
                                                p.sendMessage(st + ChatColor.RED + "숫자만 입력해주세요!");
                                            }
                                        }
                                    } else if (strings[1].equalsIgnoreCase("목록")) {
                                        if (Blacklist.isindexout(0)) {
                                            p.sendMessage(st + ChatColor.RED + "현재 블랙리스트 목록이 구성되어 있지 않습니다.");
                                        } else {
                                            int i, size;
                                            size = Main.blacklist.size();
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                            p.sendMessage(" ");
                                            for (i = 0; i < size; i++) {
                                                p.sendMessage(ChatColor.DARK_GRAY + "[ " + ChatColor.WHITE + (i + 1) + ChatColor.DARK_GRAY + " ] " + ChatColor.AQUA + Main.blacklist.get(i) + " " + ChatColor.GRAY + "[ " + ChatColor.GOLD + Blacklist.getplayer(i) + ChatColor.GRAY + " ]");
                                            }
                                            p.sendMessage(" ");
                                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                                        }
                                    } else if (strings[1].equalsIgnoreCase("초기화")) {
                                        Blacklist.clear();
                                        p.sendTitle(st, ChatColor.DARK_RED + "도박 블랙리스트가 초기화 되었습니다!", 5, 50, 5);
                                        Logger.addlog(p.getName() + " 님이 블랙리스트를 초기화 함.");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 추가 <닉네임> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에 추가합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 삭제 <인덱스> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어를 블랙리스트에서 삭제합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 목록 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 확인합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 초기화 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙리스트 목록을 초기화 합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                }
                            } else {
                                p.sendMessage(st + ChatColor.DARK_RED + "접근권한이 없습니다.");
                            }
                        } else if (strings[0].equalsIgnoreCase("인디언포커")) {
                            if (!itemcheck(p) || !sleepcheck(p)) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                                return true;
                            }
                            if (strings.length == 1) {
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                                p.sendMessage(" ");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "인디언 포커에 대한 규칙을 확인합니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 수락 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신에게 들어온 인디언 포커 게임을 수락합니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 취소 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신이 요청한 인디언포커 요청이나 자신에게 들어온 게임을 거절합니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 시작 <대상> <최대금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어에게 인디언 포커를 요청합니다.");
                                p.sendMessage(" ");
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                            } else if (strings.length == 2 && strings[1].equalsIgnoreCase("설명") || strings[1].equalsIgnoreCase("시작") || strings[1].equalsIgnoreCase("수락") || strings[1].equalsIgnoreCase("취소")) {
                                if (strings[1].equalsIgnoreCase("설명")) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.AQUA + "/도박 인디언포커 시작 <대상> <최대금액> 명령어로 대상에게 1대1 인디언 포커 요청을 합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "두 사람 모두가 게임 불참상태 및 최대금액의 베팅액을 가지고 있어야 합니다.");
                                    p.sendMessage(st + ChatColor.RED + "게임 수락시 양측이 최대금액의 10%를 보증금으로 베팅하고 시작합니다.");
                                    p.sendMessage(st + ChatColor.RED + "중간에 GUI를 닫는등 게임이 강제 종료될경우 나간사람의 배팅액은 증발합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "게임이 시작될경우 상자 아이템을 클릭할경우 상대방의 패를 확인할 수 있습니다.");
                                    p.sendMessage(st + ChatColor.RED + "먼저 게임 요청자가 상대방의 패를 보고 베팅을 진행하게 됩니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "레드스톤 블럭이 있을경우 상대방의 차례, 다이아 블럭이 있을경우 자신의 차례 입니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "상대방의 배팅액은 레드스톤 블럭 혹은 다이아 블럭또는 베팅 버튼을 통해 확인할 수 있습니다.");
                                    p.sendMessage(st + ChatColor.RED + "Bet(베팅)버튼을 누를경우 최대금액의 10%를 베팅하게 됩니다.");
                                    p.sendMessage(st + ChatColor.AQUA + "Die(다이)버튼을 누를경우 베팅을 포기하고 게임을 종료하게되며 베팅액은 살아있는 사람에게 돌아갑니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "베팅 버튼을 눌러 충분히 베팅이 끝났으면 베팅 버튼을 우클릭해 차례를 끝냅니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "베팅 버튼 1번 클릭당 최대 베팅금액의 10%씩 베팅이 됩니다. 만약 최대금액까지 남은금액이 10% 미만일경우");
                                    p.sendMessage(st + ChatColor.WHITE + "최대금액에 맞춰 베팅이 진행됩니다. (10%가 500원, 최대금액까지 남은 금액 300원 -> 300원 베팅)");
                                    p.sendMessage(st + ChatColor.WHITE + "게임 요청자가 베팅을 완료할경우 상대방의 차례가 됩니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "상대방은 이전 차례의 사람이 베팅한 금액 만큼 배팅하고 오픈(콜) 상태로 베팅을 종료하거나");
                                    p.sendMessage(st + ChatColor.WHITE + "상대방의 베팅액보다 더 많은 액수를 베팅한다음 베팅을 종료하여 베팅을 이어나갈 수 있습니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "만약 베팅액이 최대금액에 다다랐을경우 추가 베팅은 불가능하며 베팅 종료를 할 수 있습니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "승리 조건은 콜 상태에서 상대방보다 자신이 높은 숫자를 가지고 있으면 승리합니다.");
                                    p.sendMessage(st + ChatColor.DARK_RED + "본 도박 시스템을 이용하여 고의적인 어뷰징등과 같은 행위 발생시 명령어 악용으로 처벌될 수 있습니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else if (strings[1].equalsIgnoreCase("수락")) {
                                    if (Hashmap.isrequested(p.getUniqueId().toString())) {
                                        if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString()))) == null) {
                                            p.sendMessage(st + ChatColor.RED + "해당 플레이어가 온라인이 아니므로 요청을 삭제합니다.");
                                            Hashmap.delrequest(p.getUniqueId().toString());
                                            return true;
                                        }
                                        Player target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString())));
                                       if (moe.caramel.counterzombie.database.GameVars.isGameing  && moe.caramel.counterzombie.database.GameVars.GamePlayers.contains(target.getName()) && GameVars.WaitPlayers.contains(target.getName())) {
                                            p.sendMessage(st + ChatColor.RED + "대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                            Hashmap.delrequest(p.getUniqueId().toString());
                                            return true;
                                        }
                                        if (instance.econ.getBalance(p) < Hashmap.indiannmax.get(target.getUniqueId().toString()) || instance.econ.getBalance(target) < Hashmap.indiannmax.get(target.getUniqueId().toString())) {
                                            p.sendMessage(st + ChatColor.RED + "해당 플레이어 혹은 당신의 돈이 모자라므로 요청이 삭제됩니다.");
                                            Hashmap.delrequest(p.getUniqueId().toString());
                                        } else {
                                            instance.econ.withdrawPlayer(p, (double) Hashmap.indiannmax.get(Hashmap.getrequester(p.getUniqueId().toString())) / 10);
                                            instance.econ.withdrawPlayer(Bukkit.getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString()))), (double) Hashmap.indiannmax.get(Hashmap.getrequester(p.getUniqueId().toString())) / 10);
                                            Hashmap.addplayer(p, (int) ((double) Hashmap.indiannmax.get(Hashmap.getrequester(p.getUniqueId().toString())) / 10));
                                            Hashmap.addplayer(target, (int) ((double) Hashmap.indiannmax.get(Hashmap.getrequester(p.getUniqueId().toString())) / 10));
                                            new indianGui(p);
                                            new indianGui(target);
                                            Logger.addlog(p.getName() + "님이 " + target.getName() + " 의 인디언포커 요청 수락");
                                        }
                                    } else if (Hashmap.indian.get(p.getUniqueId().toString()) != null)
                                        p.sendMessage(st + ChatColor.AQUA + "이미 다른사람에게 요청중인 게임이 존재합니다.");
                                    else {
                                        p.sendMessage(st + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                                    }
                                } else if (strings[1].equalsIgnoreCase("취소")) {
                                    if (Hashmap.isrequested(p.getUniqueId().toString()) || Hashmap.indian.get(p.getUniqueId().toString()) != null) {
                                        Hashmap.delrequest(p.getUniqueId().toString());
                                        p.sendMessage(st + ChatColor.RED + "모든 요청을 제거하였습니다.");
                                        Logger.addlog(p.getName() + "님이 인디언포커 요청 취소");
                                    } else {
                                        p.sendMessage(st + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                                    }
                                } else {
                                    if (strings.length == 4) {
                                        try {
                                            int betmoney = Integer.parseInt(strings[3]);
                                            if (betmoney < Main.indianminimum)
                                                p.sendMessage(st + ChatColor.RED + "최소금액보다 적습니다.. " + ChatColor.GRAY + "[ " + ChatColor.WHITE + Main.indianminimum + ChatColor.GRAY + " ]");
                                            else if (betmoney > Main.indiammaximum)
                                                p.sendMessage(st + ChatColor.RED + "최대금액보다 많습니다.. " + ChatColor.GRAY + "[ " + ChatColor.WHITE + Main.indiammaximum + ChatColor.GRAY + " ]");
                                            else {
                                                if (Bukkit.getServer().getPlayer(strings[2]) == null || Bukkit.getServer().getPlayer(strings[2]).getName().equalsIgnoreCase(p.getName()))
                                                    p.sendMessage(st + ChatColor.DARK_RED + "해당 플레이어는 온라인이 아닙니다..");
                                                else {
                                                    Player target = Bukkit.getServer().getPlayer(strings[2]);
                                                    if (moe.caramel.counterzombie.database.GameVars.isGameing  && moe.caramel.counterzombie.database.GameVars.GamePlayers.contains(target.getName()) && GameVars.WaitPlayers.contains(target.getName())){
                                                        p.sendMessage(st + ChatColor.RED + "대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                                        return true;
                                                    }
                                                    if (instance.econ.getBalance(target) < betmoney || instance.econ.getBalance(p) < betmoney)
                                                        p.sendMessage(st + ChatColor.RED + "대상 플레이어나 플레이어가 최대 금액을 보유하고 있지 않습니다.");
                                                    else {
                                                        if (Hashmap.indian.get(p.getUniqueId().toString()) != null || Hashmap.indian.get(target.getUniqueId().toString()) != null || Hashmap.isrequested(target.getUniqueId().toString()) || Hashmap.isrequested(p.getUniqueId().toString()))
                                                            p.sendMessage(st + ChatColor.RED + "이미 게임을 요청한 사람이 존재합니다.");
                                                        else {
                                                            Hashmap.indian.put(p.getUniqueId().toString(), target.getUniqueId().toString());
                                                            p.sendMessage(st + ChatColor.AQUA + target.getName() + ChatColor.WHITE + " 님에게 " + ChatColor.RED + "인디언 포커 " + ChatColor.WHITE + "요청을 넣었습니다.");
                                                            target.sendMessage(st + ChatColor.AQUA + p.getName() + ChatColor.WHITE + " 님이 당신에게 인디언 포커를 요청하였습니다. " + ChatColor.GRAY + "[ " + ChatColor.GOLD + betmoney + "원 " + ChatColor.DARK_GRAY + "]");
                                                            target.sendMessage(st + ChatColor.RED + "수락을 원하시면 /도박 인디언포커 수락, " + ChatColor.AQUA + "취소를 원하시면 /도박 인디언포커 취소");
                                                            Hashmap.indiannmax.put(p.getUniqueId().toString(), betmoney);
                                                            Logger.addlog(p.getName() + "님이 " + target.getName() + " 에게 인디언포커 요청");
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력하세요..");
                                        }
                                    } else {
                                        p.sendMessage(" ");
                                        p.sendMessage(st + ChatColor.RED + "/도박 인디언포커 시작 <대상> <최대금액>");
                                        p.sendMessage(" ");
                                    }
                                }
                            }
                        } else if (strings[0].equalsIgnoreCase("카드")) {
                            if (itemcheck(p) != true || sleepcheck(p) != true) {
                                p.sendTitle(st, ChatColor.AQUA + "아이템을 들거나 자는 상태에서 해당 명령어를 치시면 안됩니다.", 5, 50, 5);
                                return true;
                            }
                            if (strings.length == 1) {
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                                p.sendMessage(" ");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 카드 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "카드 도박에 대한 설명을 듣습니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 카드 수락 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신에게 들어온 카드 도박을 수락합니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 카드 취소 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신이 요청한 카드 도박이나 자신에게 요청된 카드 도박을 거절합니다.");
                                p.sendMessage(st + ChatColor.WHITE + "/도박 카드 시작 <대상> <최대금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어에게 카드 도박을 요청합니다.");
                                p.sendMessage(" ");
                                p.sendMessage(ChatColor.WHITE + "======================================================================");
                            } else if (strings.length == 2 && strings[1].equalsIgnoreCase("설명") || strings[1].equalsIgnoreCase("시작") || strings[1].equalsIgnoreCase("수락") || strings[1].equalsIgnoreCase("취소")) {
                                if (strings[1].equalsIgnoreCase("설명")) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.AQUA + "/도박 카드 시작 <대상> <최대금액> 명령어로 대상에게 1대1 카드 도박 요청을 합니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "게임 시작후 자신의 턴에 3장의 카드중 하나를 고를 수 있습니다.");
                                    p.sendMessage(st + ChatColor.AQUA + "카드는 King(왕), Citizen(시민), Slave(노예) 카드가 있습니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "카드를 고른후 상대방의 턴으로 넘어가게 되며, 위에 셜커상자의 색이 붉은색일경우 상대 턴, 초록색인경우 자신의 턴 입니다.");
                                    p.sendMessage(st + ChatColor.WHITE + "양측이 카드를 모두 선택하셨을경우 카드의 우열을 가리게 됩니다.");
                                    p.sendMessage(st + ChatColor.RED + "King(왕) 카드는 Citizen(시민) 카드를 이기며, Citizen(시민) 카드는 Slave(노예) 카드를 이깁니다.");
                                    p.sendMessage(st + ChatColor.RED + "그리고 Slave(노예) 카드는 King(왕) 카드를 이기게 됩니다. [ 왕 > 시민 > 노예 > 왕 ]");
                                    p.sendMessage(st + ChatColor.RED + "Slave(노예)는 King(왕) 카드를 이기지만, 시민(Citizen)의 카드는 이기지 못합니다.");
                                    p.sendMessage(st + ChatColor.AQUA + "승자는 배팅액을 전부 가져가고, 비길시 자신의 배팅액만 가져갑니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else if (strings[1].equalsIgnoreCase("수락")) {
                                    if (Hashmap.iscardrequested(p.getUniqueId().toString())) {
                                        if (Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString()))) == null) {
                                            p.sendMessage(st + ChatColor.RED + "해당 플레이어가 온라인이 아니므로 요청을 삭제합니다.");
                                            Hashmap.delcardrequest(p.getUniqueId().toString());
                                            return true;
                                        }
                                        Player target = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString())));
                                        if (moe.caramel.counterzombie.database.GameVars.isGameing  && moe.caramel.counterzombie.database.GameVars.GamePlayers.contains(target.getName()) && GameVars.WaitPlayers.contains(target.getName())) {
                                            p.sendMessage(st + ChatColor.RED + "대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                            Hashmap.delcardrequest(p.getUniqueId().toString());
                                            return true;
                                        }
                                        if (instance.econ.getBalance(p) < Hashmap.cardmax.get(target.getUniqueId().toString()) || instance.econ.getBalance(target) < Hashmap.cardmax.get(target.getUniqueId().toString())) {
                                            p.sendMessage(st + ChatColor.RED + "해당 플레이어 혹은 당신의 돈이 모자라므로 요청이 삭제됩니다.");
                                            Hashmap.delcardrequest(p.getUniqueId().toString());
                                        } else {
                                            instance.econ.withdrawPlayer(p, Hashmap.cardmax.get(Hashmap.getcardrequester(p.getUniqueId().toString())));
                                            instance.econ.withdrawPlayer(target, Hashmap.cardmax.get(Hashmap.getcardrequester(p.getUniqueId().toString())));
                                            Hashmap.addplayer(p, Hashmap.cardmax.get(Hashmap.getcardrequester(p.getUniqueId().toString())));
                                            Hashmap.addplayer(target, Hashmap.cardmax.get(Hashmap.getcardrequester(p.getUniqueId().toString())));
                                            new CardGui(p);
                                            new CardGui(target);
                                            Logger.addlog(p.getName() + "님이 " + target.getName() + " 의 카드도박 요청 수락");
                                        }
                                    } else if (Hashmap.cardrequest.get(p.getUniqueId().toString()) != null)
                                        p.sendMessage(st + ChatColor.AQUA + "이미 다른사람에게 요청중인 게임이 존재합니다.");
                                    else {
                                        p.sendMessage(st + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                                    }
                                } else if (strings[1].equalsIgnoreCase("취소")) {
                                    if (Hashmap.iscardrequested(p.getUniqueId().toString()) || Hashmap.cardrequest.get(p.getUniqueId().toString()) != null) {
                                        Hashmap.delcardrequest(p.getUniqueId().toString());
                                        p.sendMessage(st + ChatColor.RED + "모든 요청을 제거하였습니다.");
                                        Logger.addlog(p.getName() + "님이 카드도박 요청 취소");
                                    } else {
                                        p.sendMessage(st + ChatColor.RED + "현재 들어온 요청이 없습니다.");
                                    }
                                } else {
                                    if (strings.length == 4) {
                                        try {
                                            int betmoney = Integer.parseInt(strings[3]);
                                            if (betmoney < Main.cardminimum)
                                                p.sendMessage(st + ChatColor.RED + "최소금액보다 적습니다.. " + ChatColor.GRAY + "[ " + ChatColor.WHITE + Main.cardminimum + ChatColor.GRAY + " ]");
                                            else if (betmoney > Main.cardmaximum)
                                                p.sendMessage(st + ChatColor.RED + "최대금액보다 많습니다.. " + ChatColor.GRAY + "[ " + ChatColor.WHITE + Main.cardmaximum + ChatColor.GRAY + " ]");
                                            else {
                                                if (Bukkit.getServer().getPlayer(strings[2]) == null || Bukkit.getServer().getPlayer(strings[2]).getName().equalsIgnoreCase(p.getName()))
                                                    p.sendMessage(st + ChatColor.DARK_RED + "해당 플레이어는 온라인이 아닙니다..");
                                                else {
                                                    Player target = Bukkit.getServer().getPlayer(strings[2]);
                                                    if (moe.caramel.counterzombie.database.GameVars.isGameing  && moe.caramel.counterzombie.database.GameVars.GamePlayers.contains(target.getName()) && GameVars.WaitPlayers.contains(target.getName()) && GameVars.WaitPlayers.contains(p.getName())){
                                                        p.sendMessage(st + ChatColor.RED + "대상 플레이어가 게임에 참여할 수 있는 상태가 아닙니다.");
                                                        return true;
                                                    }
                                                    if (instance.econ.getBalance(target) < betmoney || instance.econ.getBalance(p) < betmoney)
                                                        p.sendMessage(st + ChatColor.RED + "대상 플레이어나 플레이어가 최대 금액을 보유하고 있지 않습니다.");
                                                    else {
                                                        if (Hashmap.cardrequest.get(p.getUniqueId().toString()) != null || Hashmap.cardrequest.get(target.getUniqueId().toString()) != null || Hashmap.iscardrequested(target.getUniqueId().toString()) || Hashmap.iscardrequested(p.getUniqueId().toString()))
                                                            p.sendMessage(st + ChatColor.RED + "이미 게임을 요청한 사람이 존재합니다.");
                                                        else {
                                                            Hashmap.cardrequest.put(p.getUniqueId().toString(), target.getUniqueId().toString());
                                                            p.sendMessage(st + ChatColor.AQUA + target.getName() + ChatColor.WHITE + " 님에게 " + ChatColor.RED + "카드 도박 " + ChatColor.WHITE + "요청을 넣었습니다.");
                                                            target.sendMessage(st + ChatColor.AQUA + p.getName() + ChatColor.WHITE + " 님이 당신에게 카드 도박을 요청하였습니다. " + ChatColor.GRAY + "[ " + ChatColor.GOLD + betmoney + "원 " + ChatColor.DARK_GRAY + "]");
                                                            target.sendMessage(st + ChatColor.RED + "수락을 원하시면 /도박 카드 수락, " + ChatColor.AQUA + "취소를 원하시면 /도박 카드 취소");
                                                            Hashmap.cardmax.put(p.getUniqueId().toString(), betmoney);
                                                            Logger.addlog(p.getName() + "님이 " + target.getName() + " 에게 카드도박 요청");
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력하세요..");
                                        }
                                    } else {
                                        p.sendMessage(" ");
                                        p.sendMessage(st + ChatColor.RED + "/도박 카드 시작 <대상> <최대금액>");
                                        p.sendMessage(" ");
                                    }

                            }
                        } else {
                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                            p.sendMessage(" ");
                            p.sendMessage(st + ChatColor.WHITE + "/도박 카드 설명 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "카드 도박에 대한 설명을 듣습니다.");
                            p.sendMessage(st + ChatColor.WHITE + "/도박 카드 수락 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신에게 들어온 카드 도박을 수락합니다.");
                            p.sendMessage(st + ChatColor.WHITE + "/도박 카드 취소 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "자신이 요청한 카드 도박이나 자신에게 요청된 카드 도박을 거절합니다.");
                            p.sendMessage(st + ChatColor.WHITE + "/도박 카드 시작 <대상> <최대금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 플레이어에게 카드 도박을 요청합니다.");
                            p.sendMessage(" ");
                            p.sendMessage(ChatColor.WHITE + "======================================================================");
                        }
                    }
                    else{
                            if (p.isOp()) {
                                if (strings.length == 1) {
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                    p.sendMessage(" ");
                                    p.sendMessage(st + ChatColor.WHITE + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전/인디언포커/카드> <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "해당 도박의 최대/최소 금액을 지정합니다.");
                                    p.sendMessage(st + ChatColor.AQUA + "슬롯머신은 최소 금액 (1회당 사용금액)만 지정 가능합니다.");
                                    p.sendMessage(st + ChatColor.RED + "/도박 금액 누적금액 <금액> " + ChatColor.GRAY + ": " + ChatColor.WHITE + "슬롯머신에 누적된 금액을 지정합니다.");
                                    p.sendMessage(" ");
                                    p.sendMessage(ChatColor.WHITE + "======================================================================");
                                } else {
                                    if (strings[1].equalsIgnoreCase("최대") || strings[1].equalsIgnoreCase("최소") || strings[1].equalsIgnoreCase("누적금액")) {
                                        if (strings[1].equalsIgnoreCase("최대")) {
                                            if (strings.length == 2) {
                                                p.sendMessage(st + ChatColor.AQUA + "/도박 금액 <최대> <주사위/블랙잭/룰렛> <금액>");
                                            } else {
                                                if (strings[2].equalsIgnoreCase("슬롯머신")) {
                                                    p.sendMessage(st + ChatColor.RED + "슬롯머신은 최대 금액을 지정할 수 없습니다.");
                                                }
                                                else if (strings[2].equalsIgnoreCase("동전")) {
                                                    p.sendMessage(st + ChatColor.RED + "동전 도박은 최대 금액을 지정할 수 없습니다.");
                                                } else if (strings[2].equalsIgnoreCase("주사위") || strings[2].equalsIgnoreCase("블랙잭") || strings[2].equalsIgnoreCase("룰렛") || strings[2].equalsIgnoreCase("인디언포커") || strings[2].equalsIgnoreCase("카드")) {
                                                    if(strings[2].equalsIgnoreCase("주사위")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.dicemaximum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a < Main.diceminimum){
                                                                    p.sendMessage(st + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.dicemaximum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "주사위 도박" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else if (strings[2].equalsIgnoreCase("블랙잭")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.blackmaximum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a < Main.blackminimum){
                                                                    p.sendMessage(st + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.blackmaximum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "블랙잭" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else if (strings[2].equalsIgnoreCase("룰렛")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.rolletmaximum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a < Main.rolletminimum){
                                                                    p.sendMessage(st + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.rolletmaximum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "룰렛" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else if (strings[2].equalsIgnoreCase("인디언포커")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.indiammaximum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a < Main.indianminimum){
                                                                    p.sendMessage(st + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.indiammaximum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "인디언 포커" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }

                                                    }
                                                    else if (strings[2].equalsIgnoreCase("카드")) {
                                                        if (strings.length == 3) {
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.cardmaximum + " 원");
                                                        } else {
                                                            try {
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0) {
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                } else if (a < Main.cardminimum) {
                                                                    p.sendMessage(st + ChatColor.RED + "최소금액 보다 적은값을 입력하셨습니다.");
                                                                } else {
                                                                    Main.cardmaximum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "카드 도박" + ChatColor.WHITE + "의 " + ChatColor.RED + "최대 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e) {
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    p.sendMessage(st + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                                }

                                            }
                                        }
                                        else if (strings[1].equalsIgnoreCase("최소")) {
                                            if (strings.length == 2) {
                                                p.sendMessage(st + ChatColor.AQUA + "/도박 금액 <최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                            } else {
                                                 if (strings[2].equalsIgnoreCase("주사위") || strings[2].equalsIgnoreCase("블랙잭") || strings[2].equalsIgnoreCase("슬롯머신") || strings[2].equalsIgnoreCase("룰렛") || strings[2].equalsIgnoreCase("동전") || strings[2].equalsIgnoreCase("인디언포커") || strings[2].equalsIgnoreCase("카드")) {
                                                    if(strings[2].equalsIgnoreCase("슬롯머신")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.slotneed + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else {
                                                                    Main.slotneed = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "슬롯머신" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "1회당 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else if(strings[2].equalsIgnoreCase("주사위")) {
                                                         if (strings.length == 3) {
                                                             p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. " + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.diceminimum + " 원");
                                                         } else {
                                                             try {
                                                                 int a = Integer.parseInt(strings[3]);
                                                                 if (a <= 0){
                                                                     p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                 }
                                                                 else if (a > Main.dicemaximum){
                                                                     p.sendMessage(st + ChatColor.RED + "최대금액 보다 큰 값을 입력하셨습니다.");
                                                                 }
                                                                 else {
                                                                     Main.diceminimum = a;
                                                                     p.sendTitle(st, ChatColor.AQUA + "주사위 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                     p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                 }
                                                                 } catch (NumberFormatException e) {
                                                                 p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                             }
                                                         }
                                                     }
                                                    else if (strings[2].equalsIgnoreCase("룰렛")) {
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.rolletminimum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a > Main.rolletmaximum){
                                                                    p.sendMessage(st + ChatColor.RED + "최대금액 보다 큰값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.rolletminimum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "룰렛" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                     else if(strings[2].equalsIgnoreCase("동전")){
                                                         if (strings.length == 3){
                                                             p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.coinneed + " 원");
                                                         }
                                                         else{
                                                             try{
                                                                 int a = Integer.parseInt(strings[3]);
                                                                 if (a <= 0){
                                                                     p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                 }
                                                                 else {
                                                                     Main.coinneed = a;
                                                                     p.sendTitle(st, ChatColor.AQUA + "동전 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "1회당 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                     p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                 }
                                                             } catch (NumberFormatException e){
                                                                 p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                             }
                                                         }
                                                     }
                                                     else if (strings[2].equalsIgnoreCase("인디언포커")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.indianminimum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else {
                                                                    Main.indianminimum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "인디언 포커" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else if (strings[2].equalsIgnoreCase("카드")){
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.cardminimum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else {
                                                                    Main.cardminimum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "카드 도박" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        if (strings.length == 3){
                                                            p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.RED + "현재금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.blackminimum + " 원");
                                                        }
                                                        else{
                                                            try{
                                                                int a = Integer.parseInt(strings[3]);
                                                                if (a <= 0){
                                                                    p.sendMessage(st + ChatColor.RED + "값이 이상합니다..");
                                                                }
                                                                else if (a > Main.blackmaximum){
                                                                    p.sendMessage(st + ChatColor.RED + "최대금액 보다 큰값을 입력하셨습니다.");
                                                                }
                                                                else {
                                                                    Main.blackminimum = a;
                                                                    p.sendTitle(st, ChatColor.AQUA + "블랙잭" + ChatColor.WHITE + "의 " + ChatColor.YELLOW + "최소 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                                }
                                                            } catch (NumberFormatException e){
                                                                p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    p.sendMessage(st + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                                    }
                                                }
                                            }
                                        else {
                                            if (strings.length == 2){
                                               p.sendMessage(st + ChatColor.AQUA + "금액을 입력해주세요. "  + ChatColor.GREEN + "현재 누적 금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Main.slotaccumoney + " 원");
                                            }
                                            else{
                                                try{
                                                    int a = Integer.parseInt(strings[2]);
                                                    if (a < 1){
                                                        p.sendMessage(st + ChatColor.RED + "최소 1원이상으로 설정해야 합니다..");
                                                    }
                                                    else{
                                                        Main.slotaccumoney = a;
                                                        p.sendTitle(st, ChatColor.AQUA + "슬롯머신" + ChatColor.WHITE + "의 " + ChatColor.DARK_RED + "누적 금액" + ChatColor.WHITE + "이 " + ChatColor.GOLD + a + ChatColor.WHITE + " 원 으로 지정되었습니다.", 5, 50, 5);
                                                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                                                    }
                                                } catch (NumberFormatException e){
                                                    p.sendMessage(st + ChatColor.DARK_RED + "숫자만 입력해주세요..");
                                                }
                                                }
                                            }
                                        }
                                        else {
                                            p.sendMessage(st + ChatColor.AQUA + "/도박 금액 <최대/최소> <슬롯머신/주사위/블랙잭/룰렛/동전> <금액>");
                                        }
                                    }
                                }
                            else{
                                p.sendMessage(st + ChatColor.DARK_RED  + "접근권한이 없습니다.");
                            }
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.WHITE + "======================================================================");
                        commandSender.sendMessage(" ");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 슬롯머신 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "슬롯머신 도박 GUI를 오픈합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 주사위 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "주사위 도박 명령어를 확인합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 블랙잭 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "블랙잭 도박 명령어를 확인합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 룰렛 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "룰렛 도박 명령어를 확인합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 동전 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "동전 뒤집기 도박 GUI를 오픈합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 인디언포커 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "인디언 포커 명령어를 확인합니다.");
                        commandSender.sendMessage(st + ChatColor.WHITE + "/도박 카드 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "카드 도박 명령어를 확인합니다.");
                        commandSender.sendMessage(st + ChatColor.BLUE + "모든 도박은 게임 도중 GUI를 강제로 닫을경우 돈이 증발합니다.");
                        if (commandSender.isOp()) {
                            commandSender.sendMessage(st + ChatColor.WHITE + "/도박 블랙리스트 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "도박 블랙리스트를 관리합니다.");
                            commandSender.sendMessage(st + ChatColor.WHITE + "/도박 금액 " + ChatColor.GRAY + ": " + ChatColor.WHITE + "도박에 사용되는 금액을 관리합니다.");
                        }
                        commandSender.sendMessage(" ");
                        commandSender.sendMessage(ChatColor.WHITE + "======================================================================");
                    }
                }
                return true;
            }
        }
        return false;
    }
    public boolean itemcheck(Player p){
        return p.getInventory().getItemInMainHand().getType() == Material.AIR && p.getInventory().getItemInOffHand().getType() == Material.AIR;
    }
    public boolean sleepcheck(Player p){
        if (p.isSleeping()){
            return false;
        }
        return true;
    }
}
