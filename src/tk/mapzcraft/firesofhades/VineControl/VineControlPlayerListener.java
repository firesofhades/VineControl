package tk.mapzcraft.firesofhades.VineControl;

import java.util.Iterator;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.material.Dye;
import org.bukkit.material.Vine;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class VineControlPlayerListener implements Listener {
	public VineControl plugin;

	public VineControlPlayerListener(VineControl plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block cBlock;
		if (event.getClickedBlock() != null) {
			cBlock = event.getClickedBlock();
		} else {
			return;
		}
		Boolean boneMeal = true;// config
		boolean vEnabled = true;
		boneMeal = plugin.config
				.getBoolean("global.bonemeal_enabled", boneMeal);
		vEnabled = plugin.config.getBoolean("global.vinecontrol_enabled",
				vEnabled);
		if (plugin.config.contains(event.getPlayer().getWorld().getName()
				.toString())) {
			boneMeal = plugin.config.getBoolean(event.getPlayer().getWorld()
					.getName().toString()
					+ ".bonemeal_enabled", boneMeal);
			vEnabled = plugin.config.getBoolean(event.getPlayer().getWorld()
					.getName().toString()
					+ ".vinecontrol_enabled", vEnabled);
		}
		ApplicableRegionSet rSet = WGBukkit.getRegionManager(cBlock.getWorld())
				.getApplicableRegions(cBlock.getLocation());
		Iterator<ProtectedRegion> i = rSet.iterator();
		ProtectedRegion region = null;
		ProtectedRegion pRegion = null;
		while (i.hasNext()) {
			region = i.next();
			if (plugin.config.contains(cBlock.getWorld().getName().toString()
					+ "." + region.getId().toString())) {
				if (pRegion == null) {
					pRegion = region;
				}
				if (region.getPriority() > pRegion.getPriority()) {
					pRegion = region;
				}
			}
		}
		if (pRegion != null) {
			if (plugin.config.contains(cBlock.getWorld().getName().toString()
					+ "." + pRegion.getId().toString())) {
				boneMeal = plugin.config.getBoolean(cBlock.getWorld().getName()
						.toString()
						+ "."
						+ pRegion.getId().toString()
						+ ".bonemeal_enabled", boneMeal);
				vEnabled = plugin.config.getBoolean(cBlock.getWorld().getName()
						.toString()
						+ "."
						+ pRegion.getId().toString()
						+ ".vinecontrol_enabled", vEnabled);
			}
		}

		if (event.getClickedBlock() != null) {
			Dye dye = null;
			if (cBlock.getState().getData() instanceof Vine) {
				if (event.getItem() == null) {
					return;
				}
				ItemStack stack = event.getItem();
				if (event.getItem().getData() instanceof Dye) {
					dye = (Dye) event.getItem().getData();
				}
				if (event.hasItem()
						&& event.getItem().getType().equals(Material.INK_SACK)
						&& dye.getColor().equals(DyeColor.WHITE)
						&& event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
						&& boneMeal) {
					if (!event.getPlayer()
							.hasPermission("vinecontrol.bonemeal")) {
						event.getPlayer()
								.sendMessage(
										"you don't have permission, vinecontrol.bonemeal");
						return;
					}
					Byte vD = 0000;
					while (cBlock.getType().equals(Material.VINE)) {
						vD = cBlock.getData();
						cBlock = cBlock.getRelative(BlockFace.DOWN);
					}
					if (cBlock.isEmpty()) {
						VineControl.aVines.add(cBlock.getX() + " "
								+ cBlock.getY() + " " + cBlock.getZ() + " "
								+ cBlock.getWorld().getName());
						BukkitTask task = new aVinesClearTask(this.plugin)
								.runTaskLater(this.plugin, 20);
						cBlock.setType(Material.VINE);
						BukkitTask task2 = new vineAttachTask(this.plugin,
								cBlock).runTaskLater(this.plugin, 1);
						cBlock.setData(vD);
						if (event.getPlayer().getGameMode()
								.equals(GameMode.SURVIVAL)) {
							stack.setAmount(stack.getAmount() - 1);
						}

						if (vEnabled) {
							plugin.vineHandle(cBlock);
						}
					}
				}

			}
		}
	}
}
