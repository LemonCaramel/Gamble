package com.division.command;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GambleTabCompleter implements TabCompleter {

    public GambleTabCompleter() {}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        ObjectList<String> completions = new ObjectArrayList<>();
        if (args.length == 1) {
            completions = new ObjectArrayList<>(Arrays.asList("슬롯머신", "주사위", "블랙잭",
                    "룰렛", "동전", "인디언포커", "카드", "포커", "주식", "정보"));
            if (player.isOp())
                completions.addAll(Arrays.asList("블랙리스트", "저장", "리로드"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("주사위") || args[0].equalsIgnoreCase("블랙잭"))
                completions.addAll(Arrays.asList("룰", "베팅"));
            else if (args[0].equalsIgnoreCase("룰렛"))
                completions.addAll(Arrays.asList("설명", "베팅"));
            else if (args[0].equalsIgnoreCase("인디언포커") || args[0].equalsIgnoreCase("카드") || args[0].equalsIgnoreCase("포커"))
                completions.addAll(Arrays.asList("설명", "수락", "거부", "시작"));
            else if (args[0].equalsIgnoreCase("주식") && player.isOp())
                completions.addAll(Arrays.asList("추가", "삭제"));
            else if (args[0].equalsIgnoreCase("블랙리스트") && player.isOp())
                completions.addAll(Arrays.asList("추가", "삭제", "목록", "초기화"));
        }
        return completions.size() == 0 ? null : completions;
    }
}
