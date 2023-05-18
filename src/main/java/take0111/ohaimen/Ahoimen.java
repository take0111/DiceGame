package take0111.ohaimen;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.*;
import java.util.List;
import org.bukkit.entity.Player;

public final class Ahoimen extends JavaPlugin implements Listener {
    VaultManager vault;
    public static JavaPlugin plugin;
    List<Player> even = new ArrayList<>();
    List<Player> odd = new ArrayList<>();
    List<Player> sankasya = new ArrayList<>();
    double money;
    double goukei = 0;
    boolean game = false;
    String prefix = "§7[§5Dice§8Game§7]§r";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        vault = new VaultManager(plugin);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Random rand = new Random();
        Player player = (Player) sender;
        int dice1;
        int dice2;
        int sum;
        if (command.getName().equals("dg")) {
            if (args.length > 1) {
                ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                sender.sendMessage(prefix);
                sender.sendMessage("§f/dg §ehelp §r§7→ ヘルプを開きます");
                sender.sendMessage("§f/dg §e(金額) §r§7→ 指定した金額でゲームを募集します");
                sender.sendMessage("§f/dg §einfo §r§7→ 募集中のゲームの詳細を表示します");
                sender.sendMessage("§f/dg §eeven §r§7→ 偶数に参加します");
                sender.sendMessage("§f/dg §eodd §r§7→ 奇数に参加します");
                return true;
            }
            if (args.length == 0) {
                ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                sender.sendMessage(prefix);
                sender.sendMessage("§f/dg §ehelp §r§7→ ヘルプを開きます");
                sender.sendMessage("§f/dg §e(金額) §r§7→ 指定した金額でゲームを募集します");
                sender.sendMessage("§f/dg §einfo §r§7→ 募集中のゲームの詳細を表示します");
                sender.sendMessage("§f/dg §eeven §r§7→ 偶数に参加します");
                sender.sendMessage("§f/dg §eodd §r§7→ 奇数に参加します");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equals("help")) {
                    ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    sender.sendMessage(prefix);
                    sender.sendMessage("§f/dg §ehelp §r§7→ ヘルプを開きます");
                    sender.sendMessage("§f/dg §e(金額) §r§7→ 指定した金額でゲームを募集します");
                    sender.sendMessage("§f/dg §einfo §r§7→ 募集中のゲームの詳細を表示します");
                    sender.sendMessage("§f/dg §eeven §r§7→ 偶数に参加します");
                    sender.sendMessage("§f/dg §eodd §r§7→ 奇数に参加します");
                    return true;
                }
                if (args[0].equals("info")) {
                    if (!game) {
                        player.sendMessage(prefix + " §c§l現在募集中のゲームはありません");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return false;
                    } else {
                        ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                        sender.sendMessage(prefix + " §l現在募集中のゲーム §e§l金額:" + money + "§e§l円");
                        sender.sendMessage("§c§l偶数§f§lに参加中のプレイヤー:");
                        for (Player i : even) {
                            sender.sendMessage("§7" + i.getName());
                        }
                        sender.sendMessage("§f§l奇数に参加中のプレイヤー:");
                        for (Player i : odd) {
                            sender.sendMessage("§7" + i.getName());
                        }
                        return true;
                    }
                }
                if (args[0].equals("even")) {
                    if (!game) {
                        player.sendMessage(prefix + " §c§l現在募集中のゲームはありません");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (vault.getBal(player.getUniqueId()) < money) {
                        player.sendMessage(prefix + " §c§l所持金が不足しています");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (even.contains(player)) {
                        player.sendMessage(prefix + " §c§l既に偶数に参加しています");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (!sankasya.contains(player)) {
                        even.add(player);
                        ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 2.0f);
                        vault.withdraw(player, money);
                        sankasya.add(player);
                        goukei += money;
                        for (Player i : sankasya) {
                            i.sendMessage(prefix + " §f" + player.getName() + "§fさんが§c§l偶数§fに参加しました");
                            i.sendMessage("§c§l偶数§f§lに参加中のプレイヤー:");
                            for (Player n : even) {
                                i.sendMessage("§7" + n.getName());
                            }
                            i.sendMessage("§f§l奇数に参加中のプレイヤー:");
                            for (Player n : odd) {
                                i.sendMessage("§7" + n.getName());
                            }
                        }
                        return true;
                    }
                    if (odd.contains(player)) {
                        odd.remove(player);
                        even.add(player);
                        ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 2.0f);
                        for (Player i : sankasya) {
                            i.sendMessage(prefix + " §f" + player.getName() + "§fさんが§c§l偶数§fに参加しました");
                            i.sendMessage("§c§l偶数§f§lに参加中のプレイヤー:");
                            for (Player n : even) {
                                i.sendMessage("§7" + n.getName());
                            }
                            i.sendMessage("§f§l奇数に参加中のプレイヤー:");
                            for (Player n : odd) {
                                i.sendMessage("§7" + n.getName());
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equals("odd")) {
                    if (!game) {
                        player.sendMessage(prefix + " §c§l現在募集中のゲームはありません");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (vault.getBal(player.getUniqueId()) < money) {
                        player.sendMessage(prefix + " §c§l所持金が不足しています");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (odd.contains(player)) {
                        player.sendMessage(prefix + " §c§l既に奇数に参加しています");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return true;
                    }
                    if (!sankasya.contains(player)) {
                        odd.add(player);
                        ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 2.0f);
                        vault.withdraw(player, money);
                        sankasya.add(player);
                        goukei += money;
                        for (Player i : sankasya) {
                            i.sendMessage(prefix + " §f" + player.getName() + "§fさんが§l奇数§fに参加しました");
                            i.sendMessage("§c§l偶数§f§lに参加中のプレイヤー:");
                            for (Player n : even) {
                                i.sendMessage("§7" + n.getName());
                            }
                            i.sendMessage("§f§l奇数に参加中のプレイヤー:");
                            for (Player n : odd) {
                                i.sendMessage("§7" + n.getName());
                            }
                        }
                        return true;
                    }
                    if (even.contains(player)) {
                        even.remove(player);
                        odd.add(player);
                        ((Player) sender).playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 2.0f);
                        for (Player i : sankasya) {
                            i.sendMessage(prefix + " §f" + player.getName() + "§fさんが§l奇数§fに参加しました");
                            i.sendMessage("§c§l偶数§f§lに参加中のプレイヤー:");
                            for (Player n : even) {
                                i.sendMessage("§7" + n.getName());
                            }
                            i.sendMessage("§f§l奇数に参加中のプレイヤー:");
                            for (Player n : odd) {
                                i.sendMessage("§7" + n.getName());
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equals("leave")) {
                    if (sankasya.contains(player)) {
                        ((Player) sender).playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.5f);
                        sender.sendMessage(prefix + " §7§l参加中のゲームから退出しました");
                        odd.remove(player);
                        even.remove(player);
                        sankasya.remove(player);
                        vault.deposit(player, money);
                        goukei -= money;
                        for (Player i : sankasya) {
                            i.sendMessage(prefix + " §8" + player.getName() + "§8さんが退出しました");
                        }
                        return true;
                    } else {
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        sender.sendMessage(prefix + " §c§l偶数,奇数どちらにも参加していません");
                        return false;
                    }
                }
                money = Integer.parseInt(args[0]); // args[0]をmoneyに代入
                if (vault.getBal(player.getUniqueId()) > money) { // 参加費が払えるか確認
                    if (game) {
                        sender.sendMessage(prefix + " §c§l既に進行中のゲームがあります");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                        return false;
                    }
                    if (vault.getBal(player.getUniqueId()) < money) {
                        sender.sendMessage(prefix + " §c§l所持金が不足しています");
                        ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                    }
                    game = true;
                    getServer().broadcastMessage(prefix + " §f§l現在§e§l" + money + "§f§l円のゲームが開催されています!");

                    TextComponent message1 = new TextComponent("§c§l[偶数に参加]");
                    message1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dg even"));
                    TextComponent message2 = new TextComponent("§f§l[奇数に参加]");
                    TextComponent space = new TextComponent(" ");
                    message2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dg odd"));
                    getServer().spigot().broadcast(message1, space, message2);

                    dice1 = rand.nextInt(6) + 1; // 1~6の乱数をdice1に代入
                    dice2 = rand.nextInt(6) + 1; // 1~6の乱数をdice2に代入
                    sum = dice1 + dice2; // dice1とdice2の合計をsumに代入

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        getServer().broadcastMessage(prefix + " §7ゲーム開始まで§e5§7秒");
                        for (Player i : sankasya) {
                            i.playSound(i, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
                        }
                    }, 200);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        getServer().broadcastMessage(prefix + " §7ゲーム開始まで§e4§7秒");
                        for (Player i : sankasya) {
                            i.playSound(i, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
                        }
                    }, 220);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        getServer().broadcastMessage(prefix + " §7ゲーム開始まで§e3§7秒");
                        for (Player i : sankasya) {
                            i.playSound(i, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
                        }
                    }, 240);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        getServer().broadcastMessage(prefix + " §7ゲーム開始まで§e2§7秒");
                        for (Player i : sankasya) {
                            i.playSound(i, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
                        }
                    }, 260);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        getServer().broadcastMessage(prefix + " §7ゲーム開始まで§e1§7秒");
                        for (Player i : sankasya) {
                            i.playSound(i, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
                        }
                    }, 280);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (even.isEmpty()) {
                            getServer().broadcastMessage(prefix + " §c§lゲームに必要な参加者が集まりませんでした");
                            for (Player i : sankasya) {
                                i.playSound(i, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                                vault.deposit(i, money);
                            }
                            sankasya.clear();
                            even.clear();
                            odd.clear();
                            goukei = 0;
                            game = false;
                            return;
                        }
                        if (odd.isEmpty()) {
                            getServer().broadcastMessage(prefix + " §c§lゲームに必要な参加者が集まりませんでした");
                            for (Player i : sankasya) {
                                i.playSound(i, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                                vault.deposit(i, money);
                            }
                            sankasya.clear();
                            even.clear();
                            odd.clear();
                            goukei = 0;
                            game = false;
                            return;
                        }
                        getServer().broadcastMessage(prefix + " §7§l§kZZZ§f§lサイコロを振っています§7§l§kZZZ");
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            getServer().broadcastMessage(" §l出目: §e§l" + dice1 + "§f§l,§e§l" + dice2);
                            if (sum % 2 == 0) {
                                getServer().broadcastMessage(prefix + "§c§l偶数§f§lです");
                                for (Player i : even) {
                                    vault.deposit(i, goukei / even.size());
                                }
                                sankasya.clear();
                                even.clear();
                                odd.clear();
                                goukei = 0;
                                game = false;
                            } else {
                                getServer().broadcastMessage(prefix + "§f§l奇数§f§lです");
                                for (Player i : odd) {
                                    vault.deposit(i, goukei / odd.size());
                                }
                                sankasya.clear();
                                even.clear();
                                odd.clear();
                                goukei = 0;
                                game = false;
                            }
                        }, 60);
                    }, 300);
                } else {
                    ((Player) sender).playSound(((Player) sender), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                    sender.sendMessage(prefix + " §c§l所持金が不足しています");
                    return false;
                }
                return false;
            }
        }
        return false;
    }
}