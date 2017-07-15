package me.nayuma.deathpoints;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathPoints extends JavaPlugin implements Listener {

    private Chat chat;


    private String createPrefix(int score) {
        return ChatColor.translateAlternateColorCodes('&', " &8[&6" + score + "&8]");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int score = getScore(event.getPlayer());
        chat.setPlayerPrefix(event.getPlayer(), createPrefix(score));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Player killer = dead.getKiller();

        if (killer != null) {
            int deadScore = getScore(dead) - 1;
            int killerScore = getScore(killer) + 1;

            setScore(killer, killerScore);
            setScore(dead, deadScore);
            chat.setPlayerPrefix(dead, createPrefix(deadScore));
            chat.setPlayerPrefix(killer, createPrefix(killerScore));
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chat = getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public int getScore(Player player) {
        return getConfig().getInt(player.getName(), 0);
    }

    public void setScore(Player player, int score) {
        getConfig().set(player.getName(), score);
    }
}
