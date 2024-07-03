package baguchan.chamber_bta;

import baguchan.chamber_bta.world.gen.TrialChamberGen;
import useless.terrainapi.api.TerrainAPI;
import useless.terrainapi.generation.Parameters;
import useless.terrainapi.generation.overworld.api.ChunkDecoratorOverworldAPI;

import java.util.Random;

public class ChamberTerrainAPI implements TerrainAPI {
	@Override
	public String getModID() {
		return ChamberBTA.MOD_ID;
	}

	@Override
	public void onInitialize() {
		ChunkDecoratorOverworldAPI.structureFeatures.addFeature(this::trialChamber, new Object[]{});

	}

	public Void trialChamber(Parameters parameters) {
		int x = parameters.chunk.xPosition * 16;
		int z = parameters.chunk.zPosition * 16;
		int px = x + parameters.random.nextInt(16);
		int pz = z + parameters.random.nextInt(16);
		int k8 = parameters.chunk.world.getHeightValue(px, pz) - (parameters.random.nextInt(2) + 2);
		if (parameters.random.nextInt(500) == 0) {
			Random lRand = parameters.chunk.getChunkRandom(94251327L);
			new TrialChamberGen().generate(parameters.chunk.world, lRand, px, k8, pz);
		}
		return null;
	}
}
