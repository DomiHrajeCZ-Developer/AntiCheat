package xyz.msws.anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import xyz.msws.anticheat.NOPE;
import xyz.msws.anticheat.modules.checks.Check;
import xyz.msws.anticheat.modules.checks.CheckType;
import xyz.msws.anticheat.modules.data.CPlayer;

/**
 * Checks CPS over long period of time
 * 
 * @author imodm
 *
 */
public class HighCPS3 implements Check, Listener {

	private NOPE plugin;

	@Override
	public CheckType getType() {
		return CheckType.COMBAT;
	}

	private Map<UUID, Integer> clicks = new HashMap<>();

	@Override
	public void register(NOPE plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (Player target : Bukkit.getOnlinePlayers()) {
//				plugin.getCPlayer(target).setTempData("highCpsClicks2", 0);
				clicks.put(target.getUniqueId(), 0);
			}

		}, 0, checkEvery);
	}

	private final int maxCps = 8, checkEvery = 1200;

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		CPlayer cp = plugin.getCPlayer(player);

		Block block = event.getClickedBlock();

		ItemStack hand = player.getInventory().getItemInMainHand();

		if (hand != null && hand.containsEnchantment(Enchantment.DIG_SPEED))
			return;

		if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
			return;

		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			return;

		if (block != null && (!block.getType().isSolid() || block.getType() == Material.SLIME_BLOCK))
			return;

		int c = clicks.getOrDefault(player.getUniqueId(), 0);

		clicks.put(player.getUniqueId(), c + 1);

		if (c < (checkEvery / 20) * maxCps)
			return;

		cp.flagHack(this, (c - ((checkEvery / 20) * maxCps)) * 3 + 5,
				"Clicks: &e" + c + "&7 >= &a" + (checkEvery / 20) * maxCps + "\n&7CPS: &e" + c / (checkEvery / 20));
	}

	@Override
	public String getCategory() {
		return "HighCPS";
	}

	@Override
	public String getDebugName() {
		return "HighCPS#3";
	}

	@Override
	public boolean lagBack() {
		return false;
	}
}
