package baguchan.chamber_bta;

import baguchan.chamber_bta.block.ModBlocks;
import baguchan.chamber_bta.entity.breeze.BreezeRender;
import baguchan.chamber_bta.entity.breeze.EntityBreeze;
import baguchan.chamber_bta.entity.windcharge.EntityBreezeWindCharge;
import baguchan.chamber_bta.entity.windcharge.WindChargeRenderer;
import baguchan.chamber_bta.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.achievement.stat.StatMob;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Properties;


public class ChamberBTA implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = "chamber_bta";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigHandler config;

	static {
		Properties prop = new Properties();
		prop.setProperty("starting_block_id", "3600");
		prop.setProperty("starting_item_id", "26200");
		prop.setProperty("starting_entity_id", "640");
		config = new ConfigHandler(ChamberBTA.MOD_ID, prop);
		entityID = config.getInt("starting_entity_id");
		config.updateConfig();
	}

	public static int entityID;

    @Override
    public void onInitialize() {
	}

	@Override
	public void beforeGameStart() {
		ModBlocks.createBlocks();
		ModItems.onInitialize();
		EntityHelper.createEntity(EntityBreeze.class, entityID, "Breeze", () -> new BreezeRender(ChamberBTAClient.modelBreeze, ChamberBTAClient.modelWind, 0.5F));
		EntityHelper.createEntity(EntityBreezeWindCharge.class, entityID + 1, "WindCharge", () -> new WindChargeRenderer());
		StatList.mobEncounterStats.put("Breeze", new StatMob(0x1050000 + EntityDispatcher.getEntityID(EntityBreeze.class), "stat.encounterMob", "Breeze").registerStat());
	}

	@Override
	public void afterGameStart() {
		MobInfoRegistry.register(EntityBreeze.class, "section.chamber_bta.breeze.name", "section.chamber_bta.breeze.desc", 30, 500, new MobInfoRegistry.MobDrop[]{new MobInfoRegistry.MobDrop(new ItemStack(ModItems.breeze_rod), 1f, 1, 2)});
	}

	@Override
	public void onRecipesReady() {

		ModRecipes.init();
	}

	@Override
	public void initNamespaces() {

	}
}
