package fr.black_eyes.noswear.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import fr.black_eyes.noswear.Config;
import fr.black_eyes.noswear.Files;
import fr.black_eyes.noswear.Main;
import fr.black_eyes.noswear.Utils;
import fr.black_eyes.noswear.BadWords;


public class NoSwearCommands implements CommandExecutor, TabCompleter  {

	private Main main;
	private Files configFiles;


	public NoSwearCommands() {
		main = Main.getInstance();
		configFiles = main.getConfigFiles();
	}
	


	 
	//variables for command completion
	private static final String[] completions0 = {"add", "remove", "reload", "list"};
		
	 
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
			String wordStr = "[Word]";

			
			if (args.length > 0 && !hasPerm(sender, args[0])) {
				return false;
			}

			if(args.length ==2) {
				
				switch(args[0]) {
				case "add":
					Main.getInstance().getBadWords().addBadWord(args[1]);
					Utils.msg(sender, "WordAdded", wordStr, args[1]);
					break;	
				case "remove":
					Main.getInstance().getBadWords().removeBadWord(args[1]);
					Utils.msg(sender, "WordRemoved", wordStr, args[1]);
					break;
				default:
					displayhelp(sender);
				}
			}
			else if(args.length == 1) {

				if(args[0].equalsIgnoreCase("reload")) {
					configFiles.reloadData();
					configFiles.reloadConfig();
					Main.setConfigs(Config.getInstance(configFiles.getConfig()));
					Main.getInstance().setBadWords(new BadWords());
					Utils.msg(sender, "PluginReloaded", " ", " ");
				}
				else if(args[0].equalsIgnoreCase("list")) {
					List<String> badWords = Main.getInstance().getBadWords().getBadWords();
					if(badWords.size() == 0) {
						Utils.msg(sender, "NoWords", " ", " ");
					}
					else {
						StringBuilder sb = new StringBuilder();
						for(String word : badWords) {
							sb.append(word).append(", ");
						}
						Utils.msg(sender, "WordList", "[Words]", sb.toString());
					}
				}
				else {
					displayhelp(sender);
				}
			}
			else {
				displayhelp(sender);
			}
		
		return false;
	}
	
	public void displayhelp(CommandSender p) {
		List<String> help = configFiles.getLang().getStringList("help");
		for(int i=0; i<help.size();i++) {
			p.sendMessage(Utils.color(help.get(i)));
		}
	}

	boolean hasPerm(CommandSender sender, String permission) {
		if (!sender.hasPermission("noswear." + permission) && !sender.hasPermission("noswear.admin") && !sender.hasPermission("noswear.*")) {
			Utils.msg(sender, "noPermission", "[Permission]", "noswear." + permission);
			return false;
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if(args.length == 1){
		    final List<String> completions = new ArrayList<>();
		    for(String string : completions0){
		        if(string.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(string);
		    }
		    return completions;
		}
		return null;
	}
	
	
}
