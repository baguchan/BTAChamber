package baguchan.chamber_bta.entity.windcharge;

import baguchan.chamber_bta.world.explosion.WindExplosion;
import net.minecraft.core.HitResult;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.projectile.EntityProjectile;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

public class EntityBreezeWindCharge extends EntityProjectile {
	public double accelX = 0.0;
	public double accelY = 0.0;
	public double accelZ = 0.0;

	public EntityBreezeWindCharge(World world) {
		super(world);
		this.setSize(0.4f, 0.4f);
	}

	public EntityBreezeWindCharge(World world, double x, double y, double z, double vX, double vY, double vZ) {
		super(world);
		this.setSize(0.4f, 0.4f);
		this.moveTo(x, y, z, this.yRot, this.xRot);
		this.setPos(x, y, z);
		this.zd = 0.0;
		this.yd = 0.0;
		this.xd = 0.0;
		this.setAccel(vX, vY, vZ);
	}

	public EntityBreezeWindCharge(World world, EntityLiving entityliving, double vX, double vY, double vZ) {
		super(world);
		this.setSize(1.0f, 1.0f);
		this.moveTo(entityliving.x, entityliving.y, entityliving.z, entityliving.yRot, entityliving.xRot);
		this.setPos(this.x, this.y, this.z);
		this.heightOffset = 0.0f;
		this.zd = 0.0;
		this.yd = 0.0;
		this.xd = 0.0;
		this.setAccel(vX += this.random.nextGaussian() * 0.4, vY += this.random.nextGaussian() * 0.4, vZ += this.random.nextGaussian() * 0.4);
	}

	private void setAccel(double vX, double vY, double vZ) {
		double velocity = MathHelper.sqrt_double(vX * vX + vY * vY + vZ * vZ);
		if (velocity != 0.0) {
			this.accelX = vX / velocity * 0.1;
			this.accelY = vY / velocity * 0.1;
			this.accelZ = vZ / velocity * 0.1;
		} else {
			this.accelX = 0.0;
			this.accelY = 0.0;
			this.accelZ = 0.0;
		}
	}

	@Override
	protected void init() {
		this.damage = 1;
		this.defaultGravity = 0.0f;
		this.defaultProjectileSpeed = 1.0f;
	}

	@Override
	public void tick() {
		this.world.spawnParticle("smoke", this.x, this.y, this.z, this.xd * 0.05, this.yd * 0.05 - 0.1, this.zd * 0.05, 0);
		this.world.spawnParticle("smoke", this.x + this.xd * 0.5, this.y + this.yd * 0.5, this.z + this.zd * 0.5, this.xd * 0.05, this.yd * 0.05 - 0.1, this.zd * 0.05, 0);
		super.tick();
	}

	@Override
	public void onHit(HitResult result) {
		if (this.tickCount > 3 || result.hitType == HitResult.HitType.TILE) {
			if (!this.world.isClientSide) {
				if (result.entity != null) {
					result.entity.hurt(this.owner, this.damage, DamageType.COMBAT);
				}
				WindExplosion breezeWindCharge = new WindExplosion(this.world, this.owner, this.x, this.y, this.z, 3.0f);
				breezeWindCharge.doExplosionA();
				breezeWindCharge.doExplosionB(true);
			}
			this.remove();
		}
	}

	@Override
	public void afterTick() {
		super.afterTick();
		this.xd += this.accelX;
		this.yd += this.accelY;
		this.zd += this.accelZ;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public float getPickRadius() {
		return 1.0f;
	}

	@Override
	public boolean hurt(Entity entity, int i, DamageType type) {
		this.markHurt();
		if (entity != null) {
			Vec3d vec3d = entity.getLookAngle();
			if (entity instanceof EntityLiving) {
				this.owner = (EntityLiving) entity;
			}
			if (vec3d != null) {
				this.xd = vec3d.xCoord;
				this.yd = vec3d.yCoord;
				this.zd = vec3d.zCoord;
				this.accelX = this.xd * 0.1;
				this.accelY = this.yd * 0.1;
				this.accelZ = this.zd * 0.1;
			}
			return true;
		}
		return false;
	}

	@Override
	public void lerpMotion(double xd, double yd, double zd) {
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
	}
}

