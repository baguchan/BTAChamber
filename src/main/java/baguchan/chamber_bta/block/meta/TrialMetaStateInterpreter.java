package baguchan.chamber_bta.block.meta;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class TrialMetaStateInterpreter extends MetaStateInterpreter {

	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
		int hRotation = meta & 2;
		HashMap<String, String> result = new HashMap<>();
		result.put("state", new String[]{"active", "open", "inactive"}[hRotation]);
		return result;
	}
}
