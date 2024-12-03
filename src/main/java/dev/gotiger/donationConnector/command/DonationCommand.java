package dev.gotiger.donationConnector.command;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.config.StreamerManager;
import dev.gotiger.donationConnector.service.ChzzkService;
import dev.gotiger.donationConnector.service.DonationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonationCommand implements CommandExecutor, TabCompleter {
    private final DonationConnector plugin;
    private final ConfigManager configManager;
    private final StreamerManager streamerManager;
    private final ChzzkService chzzkService;
    private final DonationService donationService;

    public DonationCommand(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
        this.streamerManager = new StreamerManager(plugin);
        this.chzzkService = new ChzzkService(plugin);
        this.donationService = new DonationService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        switch (args[0].toLowerCase()) {
            case "add":
                String nickname = player.getName();
                String platform = args[1].toLowerCase();
                String broadcastUUID = args[2];

                if ("chzzk".equals(platform)) {
                    String result = chzzkService.addChzzkStreamer(nickname, uuid, broadcastUUID);
                    sender.sendMessage(result);
                    chzzkService.connectChzzkStreamer(uuid, broadcastUUID);
                } else {
                    sender.sendMessage("추가 할 수 없는 플랫폼 입니다.");
                }
                break;

            case "edit":
                nickname = player.getName();
                platform = args[1].toLowerCase();
                broadcastUUID = args[2];

                if ("chzzk".equals(platform)) {
                    String result = chzzkService.updateChzzkStreamer(nickname, uuid, broadcastUUID);
                    sender.sendMessage(result);
                    chzzkService.disconnectChzzkStreamer(uuid);
                    chzzkService.connectChzzkStreamer(uuid, broadcastUUID);
                } else {
                    sender.sendMessage("추가 할 수 없는 플랫폼 입니다.");
                }
                break;

            case "remove":
                nickname = player.getName();
                platform = args[1].toLowerCase();
                
                if ("chzzk".equals(platform)) {
                    String result = chzzkService.deleteChzzkStreamer(nickname, uuid);
                    sender.sendMessage(result);
                    chzzkService.disconnectChzzkStreamer(uuid);
                } else {
                    sender.sendMessage("추가 할 수 없는 플랫폼 입니다.");
                }
                break;

            case "on":
                sender.sendMessage(donationService.enableDonation(uuid));
                donationService.reconnectDonation(uuid);
                break;

            case "off":
                sender.sendMessage(donationService.disableDonation(uuid));
                chzzkService.disconnectChzzkStreamer(uuid);
                break;

            case "reconnect":
                if (sender.isOp()) {
                    if (args.length == 1) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            UUID playerUUID = onlinePlayer.getUniqueId();
                            String result = donationService.reconnectDonation(playerUUID);
                            onlinePlayer.sendMessage(result);
                        }
                    } else {
                        String targetName = args[1];
                        Player targetPlayer = Bukkit.getPlayer(targetName);

                        if (targetPlayer != null) {
                            UUID targetUUID = targetPlayer.getUniqueId();
                            String result = donationService.reconnectDonation(targetUUID);
                            targetPlayer.sendMessage(result);
                        } else {
                            sender.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    String result = donationService.reconnectDonation(uuid);
                    sender.sendMessage(result);
                }
                break;

            case "debug":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "사용법: /dc debug <add|edit|remove|on|off> <플레이어> [<플랫폼> <치지직UUID/숲아이디>]");
                    return true;
                }

                String debugAction = args[1].toLowerCase();
                String targetName = args.length > 2 ? args[2] : null;
                String debugPlatform = args.length > 3 ? args[3].toLowerCase() : null;
                String id = args.length > 4 ? args[4] : null;

                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "이 명령어는 OP 권한이 필요합니다.");
                    return true;
                }

                Player targetPlayer = targetName != null ? Bukkit.getPlayer(targetName) : null;
                UUID targetUUID = targetPlayer != null ? targetPlayer.getUniqueId() : null;

                switch (debugAction) {
                    case "add":
                        if (targetName == null || debugPlatform == null || id == null) {
                            sender.sendMessage(ChatColor.RED + "사용법: /dc debug add <플레이어> <플랫폼> <치지직UUID/숲아이디>");
                            return true;
                        }
                        if (targetUUID == null) {
                            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                            return true;
                        }
                        String addResult = chzzkService.addChzzkStreamer(targetName, targetUUID, id);
                        sender.sendMessage(addResult);
                        chzzkService.connectChzzkStreamer(targetUUID, id);
                        break;

                    case "edit":
                        if (targetName == null || debugPlatform == null || id == null) {
                            sender.sendMessage(ChatColor.RED + "사용법: /dc debug edit <플레이어> <플랫폼> <치지직UUID/숲아이디>");
                            return true;
                        }
                        if (targetUUID == null) {
                            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                            return true;
                        }
                        String editResult = chzzkService.updateChzzkStreamer(targetName, targetUUID, id);
                        sender.sendMessage(editResult);
                        chzzkService.disconnectChzzkStreamer(targetUUID);
                        chzzkService.connectChzzkStreamer(targetUUID, id);
                        break;

                    case "remove":
                        if (targetName == null || debugPlatform == null) {
                            sender.sendMessage(ChatColor.RED + "사용법: /dc debug remove <플레이어> <플랫폼>");
                            return true;
                        }
                        if (targetUUID == null) {
                            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                            return true;
                        }
                        String removeResult = chzzkService.deleteChzzkStreamer(targetName, targetUUID);
                        sender.sendMessage(removeResult);
                        chzzkService.disconnectChzzkStreamer(targetUUID);
                        break;

                    case "on":
                        if (targetName == null) {
                            sender.sendMessage(ChatColor.RED + "사용법: /dc debug on <플레이어>");
                            return true;
                        }
                        if (targetUUID == null) {
                            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                            return true;
                        }
                        String enableResult = donationService.enableDonation(targetUUID);
                        sender.sendMessage(enableResult);
                        donationService.reconnectDonation(targetUUID);
                        break;

                    case "off":
                        if (targetName == null) {
                            sender.sendMessage(ChatColor.RED + "사용법: /dc debug off <플레이어>");
                            return true;
                        }
                        if (targetUUID == null) {
                            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                            return true;
                        }
                        String disableResult = donationService.disableDonation(targetUUID);
                        sender.sendMessage(disableResult);
                        chzzkService.disconnectChzzkStreamer(targetUUID);
                        break;

                    default:
                        sender.sendMessage(ChatColor.RED + "알 수 없는 debug 명령어입니다. 사용법: /dc debug <add|edit|remove|on|off>");
                        break;
                }
                break;

            case "give":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "사용법: /dc give <플레이어> <후원금액>");
                    return true;
                }

                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "이 명령어는 OP 권한이 필요합니다.");
                    return true;
                }

                String giveTargetName = args[1];
                Player giveTargetPlayer = Bukkit.getPlayer(giveTargetName);

                if (giveTargetPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                    return true;
                }

                int donationAmount;
                try {
                    donationAmount = Integer.parseInt(args[2]);
                    if (donationAmount <= 0) {
                        sender.sendMessage(ChatColor.RED + "후원 금액은 0보다 커야 합니다.");
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "올바른 후원 금액을 입력하세요.");
                    return true;
                }

                donationService.executeCommands(giveTargetPlayer, donationAmount);

                sender.sendMessage(ChatColor.GREEN + giveTargetName + "님에게 " + donationAmount + "원의 보상을 지급했습니다.");
                giveTargetPlayer.sendMessage(ChatColor.GREEN + "후원이 접수되었습니다! 후원 금액: " + donationAmount + "원");
                break;

            case "reload":
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "이 명령어는 OP 권한이 필요합니다.");
                    return true;
                }
                configManager.reloadConfig();
                configManager.getStreamerManager().reloadStreamerConfig();
                sender.sendMessage(ChatColor.GREEN + "플러그인이 성공적으로 리로드되었습니다.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "명령어를 다시 확인해주세요.");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("dc")) {
            return null;
        }
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) {
                completions.addAll(PLAYER_COMMANDS);
                completions.addAll(OP_COMMANDS);
            } else if (sender instanceof Player) {
                completions.addAll(PLAYER_COMMANDS);
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "add":
                case "edit":
                case "remove":
                    completions.addAll(PLATFORMS);
                    break;
                case "reconnect":
                    if (sender.isOp()) {
                        completions.addAll(getOnlinePlayerNames());
                    }
                    break;
                case "debug":
                    completions.addAll(DEBUG_SUBCOMMANDS);
                    break;
                case "give":
                    completions.addAll(getOnlinePlayerNames());
                    break;
                default:
                    break;
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();

            if (subCommand.equals("add") || subCommand.equals("edit")) {
                completions.add("<치지직UUID/숲ID>");
            } else if (subCommand.equals("debug")) {
                String debugSubCommand = args[1].toLowerCase();
                if (DEBUG_SUBCOMMANDS.contains(debugSubCommand)) {
                    completions.addAll(getOnlinePlayerNames());
                }
            } else if (subCommand.equals("give")) {
                completions.add("<후원금액>");
            }
        } else if (args.length == 4) {
            String subCommand = args[0].toLowerCase();
            String debugSubCommand = args[1].toLowerCase();

            if (subCommand.equals("debug") && (debugSubCommand.equals("add") || debugSubCommand.equals("edit") || debugSubCommand.equals("remove"))) {
                completions.addAll(PLATFORMS);
            }
        } else if (args.length == 5) {
            String subCommand = args[0].toLowerCase();
            String debugSubCommand = args[1].toLowerCase();

            if (subCommand.equals("debug") && (debugSubCommand.equals("add") || debugSubCommand.equals("edit"))) {
                completions.add("<치지직UUID/숲ID>");
            }
        }
        return filterCompletions(completions, args[args.length - 1]);
    }

    private List<String> filterCompletions(List<String> completions, String input) {
        if (input == null || input.isEmpty()) {
            return completions;
        }
        String lowerInput = input.toLowerCase();
        List<String> filtered = new ArrayList<>();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(lowerInput)) {
                filtered.add(completion);
            }
        }
        return filtered;
    }

    private List<String> getOnlinePlayerNames() {
        return new ArrayList<>(org.bukkit.Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList()));
    }

    private static final List<String> PLATFORMS = Arrays.asList("chzzk", "soop");
    private static final List<String> PLAYER_COMMANDS = Arrays.asList("add", "edit", "remove", "on", "off", "reconnect");
    private static final List<String> OP_COMMANDS = Arrays.asList("reconnect", "debug", "give", "reload");
    private static final List<String> DEBUG_SUBCOMMANDS = Arrays.asList("add", "edit", "remove", "on", "off");
}
