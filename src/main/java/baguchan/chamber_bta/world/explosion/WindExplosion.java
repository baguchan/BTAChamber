package baguchan.chamber_bta.world.explosion;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockButton;
import net.minecraft.core.block.BlockTrapDoor;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

import java.util.ArrayList;
import java.util.List;

public class WindExplosion extends Explosion {
	public WindExplosion(World world, Entity entity, double x, double y, double z, float explosionSize) {
		super(world, entity, x, y, z, explosionSize);
		this.destroyBlocks = true;
	}

	public void doExplosionB(boolean particles) {
		if (!this.worldObj.isClientSide) {
			this.worldObj.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
		}
		ArrayList<ChunkPosition> arraylist = new ArrayList<ChunkPosition>();
		arraylist.addAll(this.destroyedBlockPositions);
		for (int i = arraylist.size() - 1; i >= 0; --i) {
			ChunkPosition chunkposition = (ChunkPosition) arraylist.get(i);
			int x = chunkposition.x;
			int y = chunkposition.y;
			int z = chunkposition.z;
			int id = this.worldObj.getBlockId(x, y, z);
			TileEntity tileEntity = this.worldObj.getBlockTileEntity(x, y, z);
			if (particles) {
				double xPos = (double) x + (double) this.worldObj.rand.nextFloat();
				double yPos = (double) y + (double) this.worldObj.rand.nextFloat();
				double zPos = (double) z + (double) this.worldObj.rand.nextFloat();
				double d3 = xPos - this.explosionX;
				double d4 = yPos - this.explosionY;
				double d5 = zPos - this.explosionZ;
				double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
				d3 /= d6;
				d4 /= d6;
				d5 /= d6;
				double d7 = 0.5 / (d6 / (double) this.explosionSize + 0.1);
				this.worldObj.spawnParticle("explode", (xPos + this.explosionX) / 2.0, (yPos + this.explosionY) / 2.0, (zPos + this.explosionZ) / 2.0, d3 *= (d7 *= (double) (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f)), d4 *= d7, d5 *= d7, 0);
				this.worldObj.spawnParticle("smoke", xPos, yPos, zPos, d3, d4, d5, 0);
			}
			if (id <= 0) continue;

			if (Block.blocksList[id] instanceof BlockButton) {
				onBlockButtonActive(worldObj, x, y, z, id);
			}

			if (Block.blocksList[id] instanceof BlockTrapDoor) {
				onBlockTrapDoorActive(worldObj, x, y, z);
			}

			//Block.blocksList[id].updateTick(this.worldObj, x, y, z, this.worldObj.rand);
		}
	}

	public boolean onBlockTrapDoorActive(World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, l ^ 4);
		return true;
	}

	public boolean onBlockButtonActive(World world, int x, int y, int z, int id) {
		int l = world.getBlockMetadata(x, y, z);
		int i1 = l & 7;
		int j1 = 8 - (l & 8);
		if (j1 == 0) {
			return true;
		}
		world.setBlockMetadataWithNotify(x, y, z, i1 + j1);
		world.markBlocksDirty(x, y, z, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y, z, id);
		if (i1 == 1) {
			world.notifyBlocksOfNeighborChange(x - 1, y, z, id);
		} else if (i1 == 2) {
			world.notifyBlocksOfNeighborChange(x + 1, y, z, id);
		} else if (i1 == 3) {
			world.notifyBlocksOfNeighborChange(x, y, z - 1, id);
		} else if (i1 == 4) {
			world.notifyBlocksOfNeighborChange(x, y, z + 1, id);
		} else if (i1 == 5) {
			world.notifyBlocksOfNeighborChange(x, y + 1, z, id);
		} else {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, id);
		}
		world.scheduleBlockUpdate(x, y, z, id, Block.blocksList[id].tickRate());
		return true;
	}

	protected void damageEntities() {
		float explosionSize2 = this.explosionSize * 2.0f;
		int x1 = MathHelper.floor_double(this.explosionX - (double) explosionSize2 - 1.0);
		int x2 = MathHelper.floor_double(this.explosionX + (double) explosionSize2 + 1.0);
		int y1 = MathHelper.floor_double(this.explosionY - (double) explosionSize2 - 1.0);
		int y2 = MathHelper.floor_double(this.explosionY + (double) explosionSize2 + 1.0);
		int z1 = MathHelper.floor_double(this.explosionZ - (double) explosionSize2 - 1.0);
		int z2 = MathHelper.floor_double(this.explosionZ + (double) explosionSize2 + 1.0);
		List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AABB.getBoundingBoxFromPool(x1, y1, z1, x2, y2, z2));
		Vec3d vec3d = Vec3d.createVector(this.explosionX, this.explosionY, this.explosionZ);
		for (Entity entity : list) {
			double d4 = entity.distanceTo(this.explosionX, this.explosionY, this.explosionZ) / (double) explosionSize2;
			if (!(d4 <= 1.0)) continue;
			double d6 = entity.x - this.explosionX;
			double d8 = entity.y - this.explosionY;
			double d10 = entity.z - this.explosionZ;
			double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
			d6 /= d11;
			d8 /= d11;
			d10 /= d11;
			double d12 = this.worldObj.func_675_a(vec3d, entity.bb);
			double d13 = (1.0 - d4) * d12;
			double d14 = d13;
			entity.xd += d6 * d14;
			entity.yd += d8 * d14;
			entity.zd += d10 * d14;
		}
	}
}
