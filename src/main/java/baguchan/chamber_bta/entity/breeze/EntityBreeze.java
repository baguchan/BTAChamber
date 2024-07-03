package baguchan.chamber_bta.entity.breeze;

import baguchan.better_ai.api.IPathGetter;
import baguchan.better_ai.util.BlockPath;
import baguchan.chamber_bta.entity.windcharge.EntityBreezeWindCharge;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import org.useless.dragonfly.model.entity.AnimationState;

public class EntityBreeze extends EntityMonster implements IPathGetter {
	private Entity currentTarget;

	public AnimationState slideAnimationState = new AnimationState();
	public AnimationState idleAnimationState = new AnimationState();
	public AnimationState shootAnimationState = new AnimationState();


	public EntityBreeze(World world) {
		super(world);
		this.scoreValue = 500;
		this.moveSpeed = 1.4F;
		this.setSize(0.6F, 1.7F);
		this.setPos(this.x, this.y, this.z);
		this.setPathfindingMalus(this, BlockPath.WATER, -1.0F);
	}

	@Override
	protected void init() {
		super.init();
		this.entityData.define(15, this.attackTime);
		this.entityData.define(16, BreezeAction.NONE.getName());
	}

	@Override
	public void onLivingUpdate() {
		float f;
		if (this.world.isClientSide) {
			this.attackTime = this.entityData.getInt(15);
		} else {
			this.entityData.set(15, this.attackTime);
		}

		super.onLivingUpdate();

		if (this.world.isClientSide) {
			slideAnimationState.animateWhen(this.moveForward > 0.0, this.tickCount);
			idleAnimationState.startIfStopped(this.tickCount);
		}
	}

	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 24.0);
		if (entityplayer != null && this.canEntityBeSeen(entityplayer) && entityplayer.getGamemode().areMobsHostile()) {
			return entityplayer;
		}
		return null;
	}

	@Override
	protected void updatePlayerActionState() {
		this.hasAttacked = this.isMovementCeased();
		float sightRadius = 16.0f;
		if (this.entityToAttack == null) {
			this.entityToAttack = this.findPlayerToAttack();
			if (this.entityToAttack != null) {
				int x = (int) (this.entityToAttack.x + (random.nextInt(2) - random.nextInt(2) + 1) * 10);
				int y = (int) (this.entityToAttack.y + (random.nextInt(2) - random.nextInt(2) + 1) * 8);
				int z = (int) (this.entityToAttack.z + (random.nextInt(2) - random.nextInt(2) + 1) * 10);
				this.pathToEntity = this.world.getEntityPathToXYZ(this, x, y, z, 24.0f);
			}
		} else if (!this.entityToAttack.isAlive()) {
			this.entityToAttack = null;
		} else {
			float distanceToEntity = this.entityToAttack.distanceTo(this);
			if (this.canEntityBeSeen(this.entityToAttack)) {
				this.attackEntity(this.entityToAttack, distanceToEntity);
			} else {
				this.attackBlockedEntity(this.entityToAttack, distanceToEntity);
			}
		}
		if (!(this.hasAttacked || this.entityToAttack == null || this.pathToEntity != null && this.random.nextInt(20) != 0)) {
			if (this.entityToAttack != null) {
				int x = (int) (this.entityToAttack.x + (random.nextInt(2) - random.nextInt(2) + 1) * 10);
				int y = (int) (this.entityToAttack.y + (random.nextInt(2) - random.nextInt(2) + 1) * 8);
				int z = (int) (this.entityToAttack.z + (random.nextInt(2) - random.nextInt(2) + 1) * 10);
				this.pathToEntity = this.world.getEntityPathToXYZ(this, x, y, z, 24.0f);
			}
		} else if (!this.hasAttacked && this.closestFireflyEntity == null && (this.pathToEntity == null && this.random.nextInt(80) == 0 || this.random.nextInt(80) == 0)) {
			this.roamRandomPath();
		}
		int i = MathHelper.floor_double(this.bb.minY + 0.5);
		boolean inWater = this.isInWater();
		boolean inLava = this.isInLava();
		this.xRot = 0.0f;
		if (this.pathToEntity == null || this.random.nextInt(100) == 0) {
			this.defaultPlayerActionState();
			this.pathToEntity = null;
			return;
		}
		Vec3d coordsForNextPath = this.pathToEntity.getPos(this);
		double d = this.bbWidth * 2.0f;
		while (coordsForNextPath != null && coordsForNextPath.squareDistanceTo(this.x, coordsForNextPath.yCoord, this.z) < d * d) {
			this.pathToEntity.next();
			if (this.pathToEntity.isDone()) {
				this.closestFireflyEntity = null;
				coordsForNextPath = null;
				this.pathToEntity = null;
				continue;
			}
			coordsForNextPath = this.pathToEntity.getPos(this);
		}
		this.isJumping = false;
		if (coordsForNextPath != null) {
			float f3;
			double x1 = coordsForNextPath.xCoord - this.x;
			double z1 = coordsForNextPath.zCoord - this.z;
			double y1 = coordsForNextPath.yCoord - (double) i;
			float f2 = (float) (Math.atan2(z1, x1) * 180.0 / 3.1415927410125732) - 90.0f;
			this.moveForward = this.moveSpeed;
			for (f3 = f2 - this.yRot; f3 < -180.0f; f3 += 360.0f) {
			}
			while (f3 >= 180.0f) {
				f3 -= 360.0f;
			}
			if (f3 > 30.0f) {
				f3 = 30.0f;
			}
			if (f3 < -30.0f) {
				f3 = -30.0f;
			}
			this.yRot += f3;
			if (this.hasAttacked && this.entityToAttack != null) {
				double d4 = this.entityToAttack.x - this.x;
				double d5 = this.entityToAttack.z - this.z;
				float f5 = this.yRot;
				this.yRot = (float) (Math.atan2(d5, d4) * 180.0 / 3.1415927410125732) - 90.0f;
				float f4 = (f5 - this.yRot + 90.0f) * 3.141593f / 180.0f;
				this.moveStrafing = -MathHelper.sin(f4) * this.moveForward * 1.0f;
				this.moveForward = MathHelper.cos(f4) * this.moveForward * 1.0f;
			}
			if (y1 > 0.0) {
				this.isJumping = true;
			}
		}

		if (this.entityToAttack != null) {
			this.faceEntity(this.entityToAttack, 30.0f, 30.0f);
		}

		if (this.horizontalCollision && !this.hasPath()) {
			this.isJumping = true;
		}
		if (this.random.nextFloat() < 0.8f && (inWater || inLava)) {
			this.isJumping = true;
		}

	}

	protected void defaultPlayerActionState() {
		++this.entityAge;
		this.tryToDespawn();
		this.moveStrafing = 0.0f;
		this.moveForward = 0.0f;
		float f = 8.0f;
		if (this.random.nextFloat() < 0.02f) {
			EntityPlayer entityplayer1 = this.world.getClosestPlayerToEntity(this, f);
			if (entityplayer1 != null) {
				this.currentTarget = entityplayer1;
				this.numTicksToChaseTarget = 10 + this.random.nextInt(20);
			} else {
				this.randomYawVelocity = (this.random.nextFloat() - 0.5f) * 20.0f;
			}
		}
		if (this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0f, this.func_25026_x());
			if (this.numTicksToChaseTarget-- <= 0 || this.currentTarget.removed || this.currentTarget.distanceToSqr(this) > (double) (f * f)) {
				this.currentTarget = null;
			}
		} else {
			if (this.random.nextFloat() < 0.05f) {
				this.randomYawVelocity = (this.random.nextFloat() - 0.5f) * 20.0f;
			}
			this.yRot += this.randomYawVelocity;
			this.xRot = this.defaultPitch;
		}
	}

	@Override
	public int getMaxHealth() {
		return 30;
	}

	@Override
	protected void attackBlockedEntity(Entity entity, float f) {
		super.attackBlockedEntity(entity, f);
		if (!this.getAction().equals(BreezeAction.NONE.getName())) {
			this.setAction(BreezeAction.NONE.getName());
			this.attackTime = 0;
		}
	}

	@Override
	protected float getSoundVolume() {
		return 2.0F;
	}

	@Override
	protected void attackEntity(Entity entity, float distance) {
		if (distance < 24.0f) {

			double d = entity.x - this.x;
			double d1 = entity.z - this.z;
			if (this.attackTime <= 0 && this.getAction().equals(BreezeAction.NONE.getName())) {
				this.setAction(BreezeAction.SHOOT.getName());
				this.attackTime = 15;
				this.shootAnimationState.start(this.tickCount);
				this.world.playSoundAtEntity(null, this, "chamber_bta.mob.breeze.breeze_shoot", this.getSoundVolume(), 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
				this.hasAttacked = true;
			} else if (this.getAction().equals(BreezeAction.SHOOT.getName())) {

				if (this.attackTime == 0) {
					if (!this.world.isClientSide) {
						double d8 = 0.9;

						Vec3d vec3d = this.getViewVector(1.0f);
						double dX = entity.x - this.x;
						double dY = entity.y - this.y;
						double dZ = entity.z - this.z;
						double dist = MathHelper.sqrt_double(dX * dX + dY * dY + dZ * dZ);
						double vX = dX + entity.xd * dist / 7.5 - vec3d.xCoord * d8;
						double vY = dY + entity.yd * dist / 7.5 - ((double) (this.bbHeight * 0.85F) + 0.5);
						double vZ = dZ + entity.zd * dist / 7.5 - vec3d.zCoord * d8;

						EntityBreezeWindCharge entityfireball = new EntityBreezeWindCharge(this.world, this, vX, vY, vZ);
						entityfireball.x = this.x + vec3d.xCoord * d8;
						entityfireball.y = this.y + (double) (this.bbHeight / 2.0f) + 0.5;
						entityfireball.z = this.z + vec3d.zCoord * d8;
						this.world.entityJoinedWorld(entityfireball);
						this.world.playSoundAtEntity(null, this, "random.bow", 1.0f, 1.0f / (this.random.nextFloat() * 0.4f + 0.8f));
						this.attackTime = 120;
						this.setAction(BreezeAction.NONE.getName());

					}
				}
				this.hasAttacked = true;
			}
			//this.yRot = (float)(Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - 90.0f;
		}
	}

	@Override
	public String getLivingSound() {
		return "chamber_bta.mob.breeze.breeze_idle";
	}

	@Override
	protected String getHurtSound() {
		return "chamber_bta.mob.breeze.breeze_hurt";
	}

	@Override
	protected String getDeathSound() {
		return "chamber_bta.mob.breeze.breeze_death";
	}

	@Override
	protected void checkFallDamage(double d, boolean flag) {
	}

	public void setAction(String action) {
		this.entityData.set(16, action);
	}

	public String getAction() {
		return this.entityData.getString(16);
	}

	@Override
	public String getEntityTexture() {
		return "/assets/chamber_bta/textures/entity/breeze/breeze.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/chamber_bta/textures/entity/breeze/breeze.png";
	}

	public enum BreezeAction {
		NONE("none"),
		AVOID("avoid"),
		SHOOT("shoot");

		public final String name;

		BreezeAction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static BreezeAction get(String nameIn) {
			for (BreezeAction role : values()) {
				if (role.name().equals(nameIn))
					return role;
			}
			return NONE;
		}

	}
}
