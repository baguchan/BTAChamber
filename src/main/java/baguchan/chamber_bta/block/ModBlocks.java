package baguchan.chamber_bta.block;

import baguchan.chamber_bta.ChamberBTA;
import baguchan.chamber_bta.block.meta.TrialMetaStateInterpreter;
import baguchan.chamber_bta.util.IDUtils;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.useless.dragonfly.model.block.DFBlockModelBuilder;
import turniplabs.halplibe.helper.BlockBuilder;

public class ModBlocks {


	public static final Block trial_spawner = new BlockBuilder(ChamberBTA.MOD_ID)
		.setHardness(100.0f)
		.setResistance(10000F)
		.setLightOpacity(1)
		.setTags(BlockTags.MINEABLE_BY_PICKAXE)
		.setBlockSound(BlockSounds.METAL)
		.setBlockModel(block -> new DFBlockModelBuilder(ChamberBTA.MOD_ID)
			.setBlockModel("block/trial_spawner.json")
			.setBlockState("trial_spawner.json")
			.setMetaStateInterpreter(new TrialMetaStateInterpreter())
			.setRender3D(true)
			.build(block))
		.build(new BlockTrialSpawner("trial_spawner", IDUtils.getCurrBlockId()));


	public static void createBlocks() {

	}

}
