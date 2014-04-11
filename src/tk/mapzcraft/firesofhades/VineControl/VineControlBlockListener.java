package tk.mapzcraft.firesofhades.VineControl;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.Material;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class VineControlBlockListener implements Listener {
	public VineControl plugin;

	public VineControlBlockListener(VineControl plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockSpread(BlockSpreadEvent event) {
		Block cBlock = event.getSource();
		boolean vEnabled = true;// config
		ArrayList<String> blackList = new ArrayList<String>();
		blackList.add("GLASS");// config

		vEnabled = plugin.config.getBoolean("global.vinecontrol_enabled",
				vEnabled);
		if (plugin.config.contains(cBlock.getWorld().getName().toString())) {
			vEnabled = plugin.config.getBoolean(cBlock.getWorld().getName()
					.toString()
					+ ".vinecontrol_enabled", vEnabled);
		}
		// we define a regionset and populate it
		ApplicableRegionSet rSet = WGBukkit.getRegionManager(cBlock.getWorld())
				.getApplicableRegions(cBlock.getLocation());
		// we create an iterator to go through the region set
		Iterator<ProtectedRegion> i = rSet.iterator();	
		ArrayList<ProtectedRegion> rList = new ArrayList<ProtectedRegion>(); 
		//we put the regionset into a list in reversed order
		//so we get the lowest priority first
		
		while(i.hasNext()){
			ProtectedRegion r = i.next();
			//plugin.log.info(r.getId().toString()+" end list");
			rList.add(0,r);
		}
		i = rList.iterator();
		
		// looping through the list
		while (i.hasNext()) {
			//if you want to load multiple settings, put "i.next()" into a variable first
			ProtectedRegion r = i.next();
			vEnabled = plugin.config.getBoolean(
					cBlock.getWorld().getName().toString() + "."
							+ r.getId().toString()
							+ ".vinecontrol_enabled", vEnabled);
			//plugin.log.info(r.getId().toString()+" "+vEnabled);
		}
		
		if (!cBlock.getType().equals(Material.VINE) || !vEnabled) {
			return;
		}
		if (cBlock.getType().equals(Material.VINE)) {
			cBlock = event.getBlock();
			
			if (cBlock.isEmpty()) {
				cBlock.setType(Material.VINE);
				cBlock.setData(event.getSource().getData());
			}
				if (plugin.vineHandle(cBlock)) {
					event.setCancelled(true);
				}
			

		}
	}

}
