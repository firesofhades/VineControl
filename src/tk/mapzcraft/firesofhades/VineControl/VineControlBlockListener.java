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
		ProtectedRegion region = null;
		ProtectedRegion pRegion = null;
		// looping through the set
		while (i.hasNext()) {
			region = i.next();
			// checking if the regionID is found in the config (regions in
			// seperate worlds can have the same name, so in the config you will
			// need to collect regions under their worlds so you can
			// diffrentiate
			if (plugin.config.contains(cBlock.getWorld().getName().toString()
					+ "." + region.getId().toString())) {
				// if its the first region defined in the config the region is
				// passed from "region" to "pregion"
				if (pRegion == null) {
					pRegion = region;
				}
				// if its not the first match priorities are compared if the
				// priority of "region" is higher as the previous
				// match("pRegion"), "region" will be passed to "pRegion"
				if (region.getPriority() > pRegion.getPriority()) {
					pRegion = region;
				}
			}
		}

		// if a match was found...
		if (pRegion != null) {
			// get your setting...
			vEnabled = plugin.config.getBoolean(
					cBlock.getWorld().getName().toString() + "."
							+ pRegion.getId().toString()
							+ ".vinecontrol_enabled", vEnabled);

		}

		if (!cBlock.getType().equals(Material.VINE) || !vEnabled) {
			return;
		}
		if (cBlock.getType().equals(Material.VINE)) {
			cBlock = cBlock.getRelative(BlockFace.DOWN);
			if (cBlock.isEmpty()) {
				cBlock.setType(Material.VINE);
				cBlock.setData(event.getSource().getData());
				if (plugin.vineHandle(cBlock)) {
					event.setCancelled(true);
				}
			}

		}
	}

}
