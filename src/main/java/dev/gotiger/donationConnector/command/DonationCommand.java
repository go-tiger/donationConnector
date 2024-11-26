package dev.gotiger.donationConnector.command;

import dev.gotiger.donationConnector.DonationConnector;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DonationCommand implements CommandExecutor {
    private final DonationConnector plugin;

    public DonationCommand(DonationConnector plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                // dc add <플랫폼> <치지직UUID/숲아이디> : 입력한 플레이어의 플랫폼에 후원 연동 추가
                break;

            case "edit":
                // dc edit <플랫폼> <치지직UUID/숲아이디> : 입력한 플레이어의 플랫폼에 후원 연동 수정
                break;

            case "remove":
                // dc remove : 입력한 플레이어의 후원 연동 삭제
                break;

            case "on":
                // dc on : 입력한 플레이어의 후원 연동 활성화
                break;

            case "off":
                // dc off : 입력한 플레이어의 후원 연동 비활성화
                break;

            case "reconnect":
                // dc reconnect : 후원 재접속
                break;

            case "debug":
                // dc debug add <플레이어> <플랫폼> <치지직UUID/숲아이디> : 해당 플레이어의 플랫폼에 후원 연동 추가
                // dc debug edit <플레이어> <플랫폼> <치지직UUID/숲아이디> : 해당 플레이어의 플랫폼에 후원 연동 수정
                // dc debug remove <플레이어> <플랫폼> <치지직UUID/숲아이디> : 플레이어의 플랫폼에 후원 연동 삭제
                // dc debug on <플레이어> : 해당 플레이어 후원 연동 활성화
                // dc debug off <플레이어> : 해당 플레이어 후원 연동 비활성화
                break;

            case "give":
                // dc give <플레이어> <후원금액> : 해당 플레이어에게 <후원금액>에 해당하는 보상 실행
                break;

            case "reload":
                // dc reload : 설정 파일 리로드
                break;

            default:
                sender.sendMessage(ChatColor.RED + "명령어를 다시 확인해주세요.");
                break;
        }
        return true;
    }
}
