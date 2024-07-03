package baguchan.chamber_bta;

import baguchan.chamber_bta.entity.breeze.BreezeModel;
import org.useless.dragonfly.helper.ModelHelper;
import org.useless.dragonfly.model.entity.BenchEntityModel;
import turniplabs.halplibe.helper.SoundHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static baguchan.chamber_bta.ChamberBTA.MOD_ID;

public class ChamberBTAClient implements ClientStartEntrypoint {

	public static final BenchEntityModel modelBreeze = ModelHelper.getOrCreateEntityModel(MOD_ID, "entity/breeze.geo.json", BreezeModel.class);
	public static final BenchEntityModel modelWind = ModelHelper.getOrCreateEntityModel(MOD_ID, "entity/breeze_wind.geo.json", BreezeModel.class);

	@Override
	public void beforeClientStart() {
		SoundHelper.addSound(MOD_ID, "mob/breeze/breeze_idle.wav");
		SoundHelper.addSound(MOD_ID, "mob/breeze/breeze_death.wav");
		SoundHelper.addSound(MOD_ID, "mob/breeze/breeze_hurt.wav");
		SoundHelper.addSound(MOD_ID, "mob/breeze/breeze_shoot.wav");
	}

	@Override
	public void afterClientStart() {

	}

}
