package baguchan.chamber_bta.block;

import baguchan.chamber_bta.tileentity.TileEntityTrialMobSpawner;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockTrialSpawner extends BlockTileEntity {
	public BlockTrialSpawner(String key, int id) {
		super(key, id, Material.stone);
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xPlaced, double yPlaced) {
		if (player.getGamemode() != Gamemode.creative) {
			return false;
		}
		player.displayGUIMobSpawnerPicker(x, y, z);
		return true;
	}

	@Override
	public TileEntity getNewBlockEntity() {
		return new TileEntityTrialMobSpawner();
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
		TileEntityMobSpawner tileEntity = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
		tileEntity.setMobId("none");
	}

	@Override
	public boolean collidesWithEntity(Entity entity, World world, int x, int y, int z) {
		return !(entity instanceof EntityItem);
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		if (!world.isClientSide) {
			TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
			if (tileEntityMobSpawner == null) {
				return;
			}
			int amountToDrop = world.rand.nextInt(10) + 10;
			for (int l = 0; l < amountToDrop; ++l) {
				ItemStack itemstack;
				String mobInSpawner = tileEntityMobSpawner.getMobId();
				if (mobInSpawner == null) continue;
				switch (mobInSpawner) {
					case "Zombie": {
						itemstack = new ItemStack(Item.cloth);
						break;
					}
					case "Skeleton": {
						if (world.rand.nextInt(2) == 0) {
							itemstack = new ItemStack(Item.bone);
							break;
						}
						itemstack = new ItemStack(Item.ammoArrow);
						break;
					}
					case "ArmouredZombie": {
						itemstack = new ItemStack(Item.chainlink);
						break;
					}
					case "Spider": {
						itemstack = new ItemStack(Item.string);
						break;
					}
					case "Snowman": {
						itemstack = new ItemStack(Item.ammoSnowball);
						break;
					}
					default: {
						itemstack = null;
					}
				}
				if (itemstack == null) continue;
				float f = world.rand.nextFloat() * 0.8f + 0.1f;
				float f1 = world.rand.nextFloat() * 0.8f + 0.1f;
				float f2 = world.rand.nextFloat() * 0.8f + 0.1f;
				while (itemstack.stackSize > 0) {
					int i1 = world.rand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
					float f3 = 0.05f;
					entityitem.xd = (float) world.rand.nextGaussian() * f3;
					entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2f;
					entityitem.zd = (float) world.rand.nextGaussian() * f3;
					world.entityJoinedWorld(entityitem);
				}
			}
		}
		super.onBlockRemoved(world, x, y, z, data);
	}

	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		if (dropCause == EnumDropCause.SILK_TOUCH) {
			return new ItemStack[]{new ItemStack(Block.mobspawnerDeactivated)};
		}
		return null;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}

