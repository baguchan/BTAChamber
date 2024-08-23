package baguchan.chamber_bta.item;

import baguchan.chamber_bta.entity.windcharge.EntityBreezeWindCharge;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

public class ItemWindCharge extends Item {
	public ItemWindCharge(String name, int id) {
		super(name, id);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		Vec3d vec3d = entityplayer.getViewVector(1.0f);
		double d8 = 0.9;
		double vX = 0;
		double vY = 0;
		double vZ = 0;

		EntityBreezeWindCharge entityfireball = new EntityBreezeWindCharge(entityplayer.world, entityplayer, vX, vY, vZ);
		entityfireball.x = entityplayer.x + vec3d.xCoord * d8;
		entityfireball.y = entityplayer.y + (double) entityplayer.getHeadHeight() + vec3d.yCoord * d8;
		entityfireball.z = entityplayer.z + vec3d.zCoord * d8;
		entityfireball.xd = vec3d.xCoord;
		entityfireball.yd = vec3d.yCoord;
		entityfireball.zd = vec3d.zCoord;
		entityfireball.accelX = entityfireball.xd * 0.01;
		entityfireball.accelY = entityfireball.yd * 0.01;
		entityfireball.accelZ = entityfireball.zd * 0.01;
		world.playSoundAtEntity(entityplayer, entityplayer, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
		if (!world.isClientSide) {
			world.entityJoinedWorld(entityfireball);
		}
		itemstack.consumeItem(entityplayer);

		return itemstack;
	}
}
