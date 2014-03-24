package tk.mapzcraft.firesofhades.VineControl;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.sk89q.worldguard.bukkit.WGBukkit;

public class VineControlCommand implements CommandExecutor {
public VineControl plugin;
	public VineControlCommand(VineControl plugin) {
		this.plugin = plugin;
	}
//maxcut >= 1, bonemealenable venable playerlistener
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		Integer pInt = 0;
		Boolean pBoo = true;
		if(arg0 == null){plugin.log.info("<VineControl> Im sorry, commands can not be used from the console at this time");return true;}
		
		if(arg3.length == 3){
			if(arg3[0].equalsIgnoreCase("global")){
				if (!Bukkit.getServer().getPlayer(arg0.getName()).hasPermission("vinecontrol.admin")) {
					arg0.sendMessage("you don't have permission, vinecontrol.admin");
					return true;
				}
				try{
				if(arg3[1].equalsIgnoreCase("maxlength")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set("global.max_length", pInt);
					arg0.sendMessage("global max length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("minlength")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set("global.min_length", pInt);
					arg0.sendMessage("global min length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("maxcut")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set("global.max_cut", pInt);
					arg0.sendMessage("global max cut modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("growthrate")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set("global.growthrate", pInt);
					arg0.sendMessage("global growthrate");
					plugin.saveYamls();
					return true;
				}
				}catch(NumberFormatException e){
					arg0.sendMessage("failed to parse, make sure you are using numbers where neccesary");
					return true;
				}
				if(arg3[1].equalsIgnoreCase("vinecontrol")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set("global.vinecontrol_enabled", pBoo);
					arg0.sendMessage("global vinecontrol enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("bonemeal")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set("global.bonemeal_enabled",pBoo);
					arg0.sendMessage("global bonemeal enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("ground")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set("global.above_ground", pBoo);
					arg0.sendMessage("global above ground modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("add")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.getStringList("global.blacklist").size()!= 0){
						blackList.addAll(plugin.config.getStringList("global.blacklist"));
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set("global.blacklist", blackList);
							arg0.sendMessage("global blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
					if(plugin.config.getStringList("global.blacklist").size()== 0){
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set("global.blacklist", blackList);
							arg0.sendMessage("global blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("remove")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.contains("global.blacklist")){
						blackList.addAll(plugin.config.getStringList("global.blacklist"));
						if(m!=Material.AIR ){
							blackList.remove(m.toString());
							plugin.config.set("global.blacklist", blackList);
							arg0.sendMessage("global blacklist modified");
							plugin.saveYamls();
							return true;
						}
						
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("delete")){
					plugin.config.set("global.blacklist", null);
					arg0.sendMessage("global blacklist modified");
					plugin.saveYamls();
					return true;
				}
			}
		}
		if(arg3.length == 4){
			if(arg3[0].equalsIgnoreCase("world")){
				if (!Bukkit.getServer().getPlayer(arg0.getName()).hasPermission("vinecontrol.admin")) {
					arg0.sendMessage("you don't have permission, vinecontrol.admin");
					return true;
				}
				try{
				if(arg3[1].equalsIgnoreCase("maxlength")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+".max_length", pInt);
					arg0.sendMessage("world: "+arg3[3]+", max length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("minlength")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+".min_length",pInt);
					arg0.sendMessage("world: "+arg3[3]+", min length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("maxcut")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+".max_cut", pInt);
					arg0.sendMessage("world: "+arg3[3]+", max cut modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("growthrate")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+".growthrate", pInt);
					arg0.sendMessage("world: "+arg3[3]+", growthrate modified");
					plugin.saveYamls();
					return true;
				}
				}catch(NumberFormatException e){
					arg0.sendMessage("failed to parse, make sure you are using numbers where neccesary");
					return true;
				}
				if(arg3[1].equalsIgnoreCase("vinecontrol")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+".vinecontrol_enabled", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", vinecontrol enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("bonemeal")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+".bonemeal_enabled", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", bonemeal enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("ground")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+".above_ground", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", above ground modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("add")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.getStringList(arg3[3]+".blacklist").size()!= 0){
						blackList.addAll(plugin.config.getStringList(arg3[3]+".blacklist"));
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set(arg3[3]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
					if(plugin.config.getStringList(arg3[3]+".blacklist").size()== 0){
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set(arg3[3]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("remove")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.contains(arg3[3]+".blacklist")){
						blackList.addAll(plugin.config.getStringList(arg3[3]+".blacklist"));
						if(m!=Material.AIR ){
							blackList.remove(m.toString());
							plugin.config.set(arg3[3]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
						
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("delete")){
					plugin.config.set(arg3[3]+".blacklist", null);
					arg0.sendMessage("world: "+arg3[3]+", blacklist modified");
					plugin.saveYamls();
					return true;
				}
			}
		}
		if(arg3.length == 5){
			if(arg3[0].equalsIgnoreCase("region")){
				if(!WGBukkit.getRegionManager(Bukkit.getServer().getWorld(arg3[3])).hasRegion(arg3[4])){arg0.sendMessage("invalid region!");return true;}
				if ((arg0.hasPermission("vinecontrol.user")&&WGBukkit.getRegionManager(Bukkit.getServer().getWorld(arg3[3])).getRegion(arg3[4]).isOwner(arg0.getName()))
						|| arg0.hasPermission("vinecontrol.admin")){
				try{
				if(arg3[1].equalsIgnoreCase("maxlength")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+"."+arg3[4]+".max_length", pInt);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", max length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("minlength")){					
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+"."+arg3[4]+".min_length", pInt);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", min length modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("maxcut")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+"."+arg3[4]+".max_cut", pInt);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", max cut modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("growthrate")){
					if(arg3[2].equalsIgnoreCase("delete")){pInt=null;}else{
						pInt = Integer.parseInt(arg3[2]);}
					if(pInt != null){if(pInt<0){arg0.sendMessage("illegal vallue, use positive numbers");return true;}}
					plugin.config.set(arg3[3]+"."+arg3[4]+".growthrate", pInt);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", growthrate modified");
					plugin.saveYamls();
					return true;
				}
				}catch(NumberFormatException e){
					arg0.sendMessage("failed to parse, make sure you are using numbers where neccesary");
					return true;
				}
				if(arg3[1].equalsIgnoreCase("vinecontrol")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+"."+arg3[4]+".vinecontrol_enabled", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", vinecontrol enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("bonemeal")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+"."+arg3[4]+".bonemeal_enabled", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", bonemeal enabled modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("ground")){
					if(!arg3[2].equalsIgnoreCase("delete")&&!arg3[2].equalsIgnoreCase("false")&&!arg3[2].equalsIgnoreCase("true")){arg0.sendMessage("illegal value, use delete, true or false");return true;}
					if(arg3[2].equalsIgnoreCase("delete")){pBoo=null;}else{
						pBoo = Boolean.parseBoolean(arg3[2]);}
					plugin.config.set(arg3[3]+"."+arg3[4]+".above_ground", pBoo);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", above ground modified");
					plugin.saveYamls();
					return true;
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("add")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.getStringList(arg3[3]+"."+arg3[4]+".blacklist").size()!= 0){
						blackList.addAll(plugin.config.getStringList(arg3[3]+"."+arg3[4]+".blacklist"));
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set(arg3[3]+"."+arg3[4]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
					if(plugin.config.getStringList(arg3[3]+"."+arg3[4]+".blacklist").size()== 0){
						if(m!=Material.AIR ){
							blackList.add(m.toString());
							plugin.config.set(arg3[3]+"."+arg3[4]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("remove")){
					Player p = Bukkit.getServer().getPlayer(arg0.getName());
					Material m = getTarget(p, 10).getType();
					ArrayList<String> blackList = new ArrayList<String>();
					if(plugin.config.contains(arg3[3]+"."+arg3[4]+".blacklist")){
						blackList.addAll(plugin.config.getStringList(arg3[3]+"."+arg3[4]+".blacklist"));
						if(m!=Material.AIR ){
							blackList.remove(m.toString());
							plugin.config.set(arg3[3]+"."+arg3[4]+".blacklist", blackList);
							arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", blacklist modified");
							plugin.saveYamls();
							return true;
						}
						
					}
				}
				if(arg3[1].equalsIgnoreCase("blacklist")&&arg3[2].equalsIgnoreCase("delete")){
					plugin.config.set(arg3[3]+"."+arg3[4]+".blacklist", null);
					arg0.sendMessage("world: "+arg3[3]+", region: "+arg3[4]+", blacklist modified");
					plugin.saveYamls();
					return true;
				}
			}else{
				arg0.sendMessage("you don't have permission, either vinecontrol.admin or vinecontrol.user, or you are not the owner of the region");
				return true;
			}
			}
		}
		arg0.sendMessage("Command doesn't exist or is used incorrect, please read the documentation");
		return true;
	}
	
	public static final Block getTarget(Player player, Integer range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }
        return lastBlock;
    }
}
