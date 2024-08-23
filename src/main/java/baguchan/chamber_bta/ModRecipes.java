package baguchan.chamber_bta;

import baguchan.chamber_bta.item.ModItems;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

import static baguchan.chamber_bta.ChamberBTA.MOD_ID;

public class ModRecipes {
	public static void init() {
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(ModItems.breeze_rod)
			.create("wind_charge", new ItemStack(ModItems.wind_charge, 4));

	}
}
