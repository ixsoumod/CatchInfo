package fr.ixsou.catchinfo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CatchInfo extends JavaPlugin implements Listener {

    private final Map<String, String> playerIps = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("CatchInfo plugin activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("CatchInfo plugin désactivé !");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String ip = player.getAddress().getAddress().getHostAddress();
        playerIps.put(player.getName(), ip);
        getLogger().info(player.getName() + " s'est connecté avec l'IP : " + ip);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String ip = playerIps.get(player.getName());
        if (ip != null) {
            getLogger().info(player.getName() + " s'est déconnecté avec l'IP : " + ip);
        }
        playerIps.remove(player.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("catchinfo.ip")) {
                player.sendMessage("Vous n'avez pas la permission de faire cela.");
                return false;
            }
        }
        if (args.length == 1) {
            String playerName = args[0];
            if (playerIps.containsKey(playerName)) {
                String ip = playerIps.get(playerName);
                sender.sendMessage(playerName + " est connecté avec l'IP : " + ip);
            } else {
                sender.sendMessage("Le joueur " + playerName + " n'a pas d'IP enregistrée.");
            }
            return true;
        }
        return false;
    }
}