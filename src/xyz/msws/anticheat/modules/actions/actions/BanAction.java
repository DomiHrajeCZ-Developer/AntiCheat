package xyz.msws.anticheat.modules.actions.actions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import xyz.msws.anticheat.NOPE;
import xyz.msws.anticheat.modules.actions.AbstractAction;
import xyz.msws.anticheat.modules.bans.BanHook;
import xyz.msws.anticheat.modules.bans.Banwave;
import xyz.msws.anticheat.modules.checks.Check;
import xyz.msws.anticheat.modules.data.CPlayer;
import xyz.msws.anticheat.modules.data.Stats;
import xyz.msws.anticheat.utils.MSG;

/**
 * Bans, saves logs, and increments statistics
 * 
 * @author imodm
 *
 */
public class BanAction extends AbstractAction {

	private String reason;
	private long time;

	private boolean running;

	public BanAction(NOPE plugin, long time, String reason) {
		super(plugin);
		this.reason = reason;
		this.time = time;
	}

	@Override
	public void execute(OfflinePlayer player, Check check) {
		CPlayer cp = plugin.getCPlayer(player);
		if (running)
			return;
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			running = true;
			String token = cp.saveLog(check);
			String res = MSG.replaceCheckPlaceholder(reason, cp, check).replace("%token%", token);
			cp.clearVls();

			plugin.getModule(Stats.class).addBan();
			plugin.getModule(Banwave.class).removePlayer(player.getUniqueId());

			Bukkit.getScheduler().runTask(plugin, () -> {
				plugin.getModule(BanHook.class).ban(player.getUniqueId(), res, time);
				running = false;
			});
		});
	}

}
