package baguchan.chamber_bta.entity.fx;

import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class EntityTrialFX extends EntityFX {
	private float field_672_a;

	public EntityTrialFX(World world, double d, double d1, double d2, double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		this.xd = this.xd * 0.01 + d3;
		this.yd = this.yd * 0.01 + d4;
		this.zd = this.zd * 0.01 + d5;
		d += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.05f);
		d1 += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.05f);
		d2 += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.05f);
		this.field_672_a = this.particleScale;
		this.particleBlue = 1.0f;
		this.particleGreen = 1.0f;
		this.particleRed = 1.0f;
		this.particleMaxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2)) + 4;
		this.noPhysics = true;
	}

	@Override
	public void renderParticle(Tessellator t, float partialTick, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		float f6 = ((float) this.particleAge + partialTick) / (float) this.particleMaxAge;
		this.particleScale = this.field_672_a * (1.0f - f6 * f6 * 0.5f);
		super.renderParticle(t, partialTick, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
	}

	@Override
	public float getBrightness(float partialTick) {
		float decay = MathHelper.clamp(((float) this.particleAge + partialTick) / (float) this.particleMaxAge, 0.0f, 1.0f);
		return super.getBrightness(partialTick) * decay + (1.0f - decay);
	}

	@Override
	public int getLightmapCoord(float partialTicks) {
		return LightmapHelper.setBlocklightValue(super.getLightmapCoord(partialTicks), 15);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.particleAge++ >= this.particleMaxAge) {
			this.remove();
		}
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.96;
		this.yd *= 0.96;
		this.zd *= 0.96;
		if (this.onGround) {
			this.xd *= 0.7;
			this.zd *= 0.7;
		}
	}
}

