package baguchan.chamber_bta.item;

import baguchan.chamber_bta.ChamberBTA;
import baguchan.chamber_bta.util.IDUtils;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public class ModItems {
	public static final Item breeze_rod = new ItemBuilder(ChamberBTA.MOD_ID).build(new Item("breeze_rod", IDUtils.getCurrItemId()));

	public static final Item wind_charge = new ItemBuilder(ChamberBTA.MOD_ID).build(new ItemWindCharge("wind_charge", IDUtils.getCurrItemId()));


	public static void onInitialize() {
	}
}
