package fr.black_eyes.noswear.listeners;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import fr.black_eyes.noswear.Config;
import fr.black_eyes.noswear.Main;
import fr.black_eyes.noswear.Utils;



public class ChatListener implements Listener  {
	


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Object ess = null;
		if(Main.getInstance().isEssentials()) {
			ess = (net.ess3.api.IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			if(((net.ess3.api.IEssentials)ess).getUser(p).isMuted()) {
				return;
			}
		}
		if(p.hasPermission("noswear.ignore")){
			return;
		}

		boolean found_bad_word = false;
		List<String> toReplace = new ArrayList<>();
		String message = e.getMessage();
		for (String word : Main.getInstance().getBadWords().getBadWords()) {
			String word_found = Utils.containsIgnoreCase(message, word);
			if(word_found != null) {
				toReplace.add(word_found);
				found_bad_word = true;
			}
		}

		if(Config.getInstance().Censor_message){
			for (String word : toReplace) {
				int length = word.length();
				StringBuilder replacement = new StringBuilder();
				for (int i = 0; i < length; i++) {
					replacement.append(Config.getInstance().CensorShip_Character);
				}
				message = message.replace(word, replacement.toString());
			}
			e.setMessage(message);
		}

		if(found_bad_word){
			if(Config.getInstance().GhostMute) {
				e.setCancelled(true);
				// get the server chat formatting
				String format = e.getFormat(); 
				// respect the format
				String complete_message = format.replace("%1$s", p.getDisplayName()).replace("%2$s", e.getMessage());
				// send the message to the player
				Utils.sendMultilineMessage(complete_message, p);
				if(Config.getInstance().ConsoleMessages) {
					// send the message to the console
					Main.getInstance().logInfo(Config.getInstance().format_of_swearing_messages_for_admins.replace("[Player]", p.getName()).replace("[message]", e.getMessage()));
				}
				// check if a player has the noswear.log permission
				if(Config.getInstance().send_messages_to_admins) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						if(player.hasPermission("noswear.log")) {
							Utils.sendMultilineMessage(Config.getInstance().format_of_swearing_messages_for_admins.replace("[Player]", p.getName()).replace("[message]", e.getMessage()), player);
						}
					}
				}

			}
			if(Config.getInstance().show_title_at_player) {
				p.sendTitle(Utils.getMsg("title_text"), Utils.getMsg("title_subtext"));
			}
		}
    }
}
