package xyz.msws.anticheat.modules.actions.actions;

import org.bukkit.OfflinePlayer;

import xyz.msws.anticheat.NOPE;
import xyz.msws.anticheat.modules.actions.AbstractAction;
import xyz.msws.anticheat.modules.checks.Check;
import xyz.msws.anticheat.modules.data.CPlayer;

public class SetVLAction extends AbstractAction {

	private int diff;

	public SetVLAction(NOPE plugin, int diff) {
		super(plugin);
		this.diff = diff;
	}

	@Override
	public void execute(OfflinePlayer player, Check check) {
		CPlayer cp = plugin.getCPlayer(player);
		cp.setVL(check.getCategory(), diff);
	}

}
