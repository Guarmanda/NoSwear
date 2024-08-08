package fr.black_eyes.noswear;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public class Utils  {

	public Utils() {
	}

	public static String color(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	//message functions that automatically get a message from config lang file
	public static void msg(CommandSender p, String path, String replacer, String replacement) {
		String message = path;
		if(Main.getInstance().getConfigFiles().getLang().isSet(path)) {
			message = getMsg(path, replacer, replacement);
		}
		sendMultilineMessage(message, p);
	}
	
	//message functions that automatically get a message from config lang file
	public static void msg(CommandSender p, String path, String replacer, String replacement, String replacer2, String replacement2) {
		String message = Main.getInstance().getConfigFiles().getLang().getString(path).replace(replacer, replacement).replace(replacer2, replacement2);
		sendMultilineMessage(message, p);
	}
	
	/*
	 * This function is only for messages of chest spawning.
	 * 
	 */
	public static void msg(CommandSender p, String path,  String r1, String r1b, String r2, String r2b, String r3, String r3b, String r4, String r4b,  String r5, String r5b) {
		String message = path;
		if(Main.getInstance().getConfigFiles().getLang().isSet(path)) {
			message = Main.getInstance().getConfigFiles().getLang().getString(path);
		}
		message = message.replace(r1, r1b).replace(r2, r2b).replace(r3, r3b).replace(r4, r4b).replace(r5, r5b);
		sendMultilineMessage(message, p);
	}
	
	public static void sendMultilineMessage(String message, CommandSender player) {
		List<String> msgs = Arrays.asList(message.split("\\\\n"));
		msgs.stream().forEach(msg -> player.sendMessage(color(msg)));
	}
	
	public static String getMsg(String path, String replacer, String replacement,  String replacer2, String replacement2) {
		return color(getMsg(path, replacer, replacement).replace( replacer2, replacement2));
	}
	public static String getMsg(String path, String replacer, String replacement) {
		return color(getMsg(path).replace(replacer, replacement));
	}
	public static String getMsg(String path) {
		return color(Main.getInstance().getConfigFiles().getLang().getString(path));
	}

	/**
	 * search for a string in a string, ignoring case. If we find the badword, return its original form in the string so we can replace it
	 * For this, we have to remember its position and length in the original string
	 */
	public static String containsIgnoreCase(String the_string, String bad_word) {
		String the_string_lower = the_string.toLowerCase().replace('@', 'a').replace('0', 'o').replace('1', 'i').replace('3', 'e').replace('4', 'a').replace('5', 's').replace('7', 't').replace('8', 'b').replace('9', 'g');
		String bad_word_lower = bad_word.toLowerCase();
		int index = the_string_lower.indexOf(bad_word_lower);
		if(index != -1) {
			return the_string.substring(index, index + bad_word.length());
		}
		return null;

		
	}
	


	

	
	
}
