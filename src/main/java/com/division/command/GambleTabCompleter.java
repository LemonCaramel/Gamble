package com.division.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GambleTabCompleter implements TabCompleter {

    private final ArrayList<String> firstCmd;
    private final ArrayList<String> firstNonOpCmd;

    public GambleTabCompleter() {
        firstCmd = new ArrayList<>(Arrays.asList("슬롯머신", "주사위", "블랙잭", "룰렛", "동전", "인디언포커", "카드", "포커", "주식", "블랙리스트", "저장", "리로드"));
        firstNonOpCmd = new ArrayList<>(Arrays.asList("슬롯머신", "주사위", "블랙잭", "룰렛", "동전", "인디언포커", "카드", "포커", "주식"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] arg) {
        List<String> value = new ArrayList<>();
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (arg.length == 1) {
                if (p.isOp())
                    value.addAll(firstCmd.stream().filter(data -> data.contains(arg[0])).collect(Collectors.toList()));
                else
                    value.addAll(firstNonOpCmd.stream().filter(data -> data.contains(arg[0])).collect(Collectors.toList()));
            }
            else if (arg.length == 2) {
                if (arg[0].equalsIgnoreCase("주사위") || arg[0].equalsIgnoreCase("블랙잭"))
                    value.addAll(Stream.of("룰", "베팅").filter(data -> data.contains(arg[1])).collect(Collectors.toList()));
                else if (arg[0].equalsIgnoreCase("룰렛"))
                    value.addAll(Stream.of("설명", "베팅").filter(data -> data.contains(arg[1])).collect(Collectors.toList()));
                else if (arg[0].equalsIgnoreCase("인디언포커") || arg[0].equalsIgnoreCase("카드") || arg[0].equalsIgnoreCase("포커"))
                    value.addAll(Stream.of("설명", "수락", "거부", "시작").filter(data -> data.contains(arg[1])).collect(Collectors.toList()));
                else if (arg[0].equalsIgnoreCase("주식") && p.isOp())
                    value.addAll(Stream.of("추가", "삭제").filter(data -> data.contains(arg[1])).collect(Collectors.toList()));
                else if (arg[0].equalsIgnoreCase("블랙리스트"))
                    value.addAll(Stream.of("추가", "삭제", "목록", "초기화").filter(data -> data.contains(arg[1])).collect(Collectors.toList()));
            }

        }
        return value;
    }
}
