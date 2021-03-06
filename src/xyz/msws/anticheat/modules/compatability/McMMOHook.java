package xyz.msws.anticheat.modules.compatability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.gmail.nossr50.datatypes.skills.SuperAbilityType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;

import xyz.msws.anticheat.NOPE;
import xyz.msws.anticheat.events.player.PlayerFlagEvent;
import xyz.msws.anticheat.modules.checks.Check;

public class McMMOHook extends AbstractHook {

	public McMMOHook(NOPE plugin) {
		super(plugin);
	}

	private Map<UUID, Long> lastTreeFeller = new HashMap<>();

	@EventHandler
	public void treeFellerCheck(PlayerFlagEvent event) {
		Check check = event.getCheck();
		Player player = event.getPlayer();
		if (!check.getCategory().equals("FastBreak"))
			return;

		if (System.currentTimeMillis() - lastTreeFeller.getOrDefault(player.getUniqueId(), 0L) > 1000)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onAbilityActivate(McMMOPlayerAbilityActivateEvent event) {
		Player player = event.getPlayer();
		if (event.getAbility() != SuperAbilityType.TREE_FELLER)
			return;
		lastTreeFeller.put(player.getUniqueId(), System.currentTimeMillis());
	}

	@Override
	public String getName() {
		return "mcMMO";
	}

}
