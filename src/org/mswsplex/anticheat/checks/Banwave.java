package org.mswsplex.anticheat.checks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mswsplex.anticheat.data.CPlayer;
import org.mswsplex.anticheat.msws.AntiCheat;

public class Banwave {

	private AntiCheat plugin;

	private long lastBanwave;

	public Banwave(AntiCheat plugin) {
		this.plugin = plugin;
		runBanwave(false).runTaskTimer(this.plugin, 0, plugin.config.getInt("BanwaveRate"));
	}

	public BukkitRunnable runBanwave(boolean forced) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					CPlayer cp = plugin.getCPlayer(player);
					if (!cp.hasSaveData("isBanwaved"))
						continue;
					cp.ban(cp.getSaveString("isBanwaved"), Timing.BANWAVE);
					cp.removeSaveData("isBanwaved");
					cp.saveData();
				}
				if (!forced)
					lastBanwave = System.currentTimeMillis();
			}
		};
	}

	public long timeToNextBanwave() {
		return plugin.config.getInt("BanwaveRate") * 50 + lastBanwave - System.currentTimeMillis();
	}
}
