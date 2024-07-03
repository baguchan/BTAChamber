package baguchan.chamber_bta.tileentity;

import baguchan.chamber_bta.block.ModBlocks;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumCreatureType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;

import java.util.List;

public class TileEntityTrialMobSpawner extends TileEntityMobSpawner {
	public int nearby;
	protected int spawnCount = -1;

	protected long cooldown;
	protected int itemSpitTime = -1;
	private int itemID = -1;

	private int entityMaxCount = 4;
	private int itemCount = 1;
	public String mobId = "Pig";

	public String getMobId() {
		return this.mobId;
	}

	public void setMobId(String mobId) {
		this.mobId = mobId != null && mobId.equalsIgnoreCase("none") ? null : mobId;
	}

	public void setEntityMaxCount(int entityMaxCount) {
		this.entityMaxCount = entityMaxCount;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public boolean anyPlayerInRange() {
		if (this.worldObj != null) {
			EntityPlayer player = this.worldObj.getClosestPlayer((double) this.x + 0.5, (double) this.y + 0.5, (double) this.z + 0.5, 12.0);

			if (player != null) {
				Vec3d newPosition = Vec3d.createVector((double) this.x + 0.5, (double) this.y + 0.5, (double) this.z + 0.5);
				Vec3d oldPosition = Vec3d.createVector(player.x, player.y + player.getHeadHeight(), player.z);
				HitResult hitResult = this.worldObj.checkBlockCollisionBetweenPoints(oldPosition, newPosition, false, true);
				return hitResult != null && this.worldObj.getBlockTileEntity(hitResult.x, hitResult.y, hitResult.z) == this;
			}
		}

		return false;
	}

	@Override
	public void tick() {
		if (this.worldObj.getBlockId(this.x, this.y, this.z) != ModBlocks.trial_spawner.id) {
			return;
		}
		this.yaw2 = this.yaw;

		if (this.cooldown <= this.worldObj.getWorldTime()) {

			if (this.spawnCount >= this.entityMaxCount) {
				this.itemSpitTime = 0;
				this.cooldown = this.worldObj.getWorldTime() + 20 * 60 * 30;
				this.worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, 1);
				this.spawnCount = -1;
				return;
			} else {
				if (this.worldObj.getBlockMetadata(this.x, this.y, this.z) != 0) {
					this.worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, 0);
				}
			}

			if (!this.anyPlayerInRange()) {
				return;
			} else if (this.spawnCount <= -1) {
				for (int k = 0; k < 20; ++k) {
					double d1 = (double) this.x + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
					double d3 = (double) this.y + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
					double d5 = (double) this.z + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
					this.worldObj.spawnParticle("trial", d1, d3, d5, 0.0, 0.25, 0.0, 0);
				}
				this.spawnCount = 0;
				this.delay = 30;
				return;
			}
			double xPos = (double) this.x + (double) this.worldObj.rand.nextFloat();
			double yPos = (double) this.y + (double) this.worldObj.rand.nextFloat();
			double zPos = (double) this.z + (double) this.worldObj.rand.nextFloat();
			this.worldObj.spawnParticle("smoke", xPos, yPos, zPos, 0.0, 0.0, 0.0, 0);
			this.worldObj.spawnParticle("flame", xPos, yPos, zPos, 0.0, 0.0, 0.0, 0);
			this.yaw += (double) (1000.0f / ((float) this.delay + 200.0f));
			while (this.yaw > 360.0) {
				this.yaw -= 360.0;
				this.yaw2 -= 360.0;
			}
			if (!this.worldObj.isClientSide) {
				if (this.worldObj.difficultySetting == 0) {
					return;
				}
				if (this.getMobId() == null || this.mobId.equalsIgnoreCase("none")) {
					return;
				}
				if (this.delay == -1) {
					this.updateDelay();
				}
				if (this.delay > 0) {
					--this.delay;
					return;
				}
				if (this.mobId == null) {
					this.nearby = this.countNearbySpawners();
				}
				int byte0 = 4;
				for (int i = 0; i < byte0; ++i) {
					EntityLiving entityliving = null;
					if (this.mobId.equalsIgnoreCase("random")) {
						List<SpawnListEntry> list = this.worldObj.getBlockBiome(this.x, this.y, this.z).getSpawnableList(EnumCreatureType.monster);
						SpawnListEntry entry = list.get(this.worldObj.rand.nextInt(list.size()));
						entityliving = (EntityLiving) EntityDispatcher.createEntityInWorld(EntityDispatcher.classToKeyMap.get(entry.entityClass), this.worldObj);
					} else {
						entityliving = (EntityLiving) EntityDispatcher.createEntityInWorld(this.mobId, this.worldObj);
					}
					if (entityliving == null) {
						return;
					}
					int j = this.worldObj.getEntitiesWithinAABB(entityliving.getClass(), AABB.getBoundingBoxFromPool(this.x, this.y, this.z, this.x + 1, this.y + 1, this.z + 1).expand(8.0, 4.0, 8.0)).size();
					if (j >= 6) {
						this.updateDelay();
						return;
					}
					double d6 = (double) this.x + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 4.0;
					double d7 = this.y + this.worldObj.rand.nextInt(3) - 1;
					double d8 = (double) this.z + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 4.0;
					entityliving.moveTo(d6, d7, d8, this.worldObj.rand.nextFloat() * 360.0f, 0.0f);
					if (!(worldObj.checkIfAABBIsClear(entityliving.bb) && worldObj.getCubes(entityliving, entityliving.bb).size() == 0)) {
						this.updateDelay();
						continue;
					}
					this.spawnCount++;
					this.worldObj.entityJoinedWorld(entityliving);
					for (int k = 0; k < 20; ++k) {
						double d1 = (double) this.x + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
						double d3 = (double) this.y + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
						double d5 = (double) this.z + 0.5 + ((double) this.worldObj.rand.nextFloat() - 0.5) * 2.0;
						this.worldObj.spawnParticle("smoke", d1, d3, d5, 0.0, 0.0, 0.0, 0);
						this.worldObj.spawnParticle("flame", d1, d3, d5, 0.0, 0.0, 0.0, 0);
						this.worldObj.playSoundAtEntity(null, entityliving, "mob.ghast.fireball", 0.025f, 0.75f);
					}
					entityliving.spawnExplosionParticle();
					this.updateDelay();
					break;
				}
			}
		} else {
			if (this.itemSpitTime < 60) {
				if (this.itemSpitTime == 30) {
					if (this.itemID <= -1) {
						this.worldObj.dropItem(this.x, this.y + 1, this.z, new ItemStack(Item.foodAppleGold));
					} else {
						this.worldObj.dropItem(this.x, this.y + 1, this.z, new ItemStack(this.itemID, itemCount, 0));
					}
				}
				this.itemSpitTime++;
			} else {


				if (this.worldObj.getBlockMetadata(this.x, this.y, this.z) != 2) {
					this.worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, 2);
				}
			}
		}
	}

	private void updateDelay() {
		this.delay = 120 + this.worldObj.rand.nextInt(40);
		if (this.getMobId() == null) {
			this.delay *= 2;
			System.out.println("x2");
			if (this.countNearbySpawners() > 1) {
				System.out.println("x4");
				this.delay *= 4;
			}
		}
	}

	@Override
	public void readFromNBT(CompoundTag tag) {
		super.readFromNBT(tag);
		this.cooldown = tag.getLong("Cooldown");
		this.spawnCount = tag.getInteger("SpawnCount");
		this.itemSpitTime = tag.getInteger("ItemSpitTime");
		this.entityMaxCount = tag.getInteger("EntitySpawnCount");
		this.itemID = tag.getInteger("ItemID");
		this.itemCount = tag.getInteger("ItemCount");
		this.delay = tag.getShort("Delay");
		this.setMobId(tag.getString("EntityId"));
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		super.writeToNBT(tag);
		tag.putLong("Cooldown", this.cooldown);
		tag.putInt("SpawnCount", this.spawnCount);
		tag.putInt("ItemSpitTime", this.itemSpitTime);
		tag.putInt("EntitySpawnCount", this.entityMaxCount);
		tag.putInt("ItemID", this.itemID);
		tag.putInt("ItemCount", this.itemCount);
		tag.putShort("Delay", (short) this.delay);
		tag.putString("EntityId", this.mobId != null ? this.mobId : "none");
	}
}
