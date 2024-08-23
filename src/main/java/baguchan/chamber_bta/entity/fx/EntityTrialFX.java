package baguchan.chamber_bta.entity.fx;

import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

import static baguchan.chamber_bta.ChamberBTA.MOD_ID;

public class EntityTrialFX extends EntityFX {

	public EntityTrialFX(World world, double d, double d1, double d2, double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		this.xd = this.xd * 0.01 + d3;
		this.yd = this.yd * 0.01 + d4;
		this.zd = this.zd * 0.01 + d5;
		this.particleBlue = 1.0f;
		this.particleGreen = 1.0f;
		this.particleRed = 1.0f;
		this.particleMaxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2)) + 4;
		this.noPhysics = true;
		this.particleTexture = TextureRegistry.getTexture(MOD_ID + ":particle/trial");
	}

	@Override
	public float getBrightness(float partialTick) {
		float decay = MathHelper.clamp(((float) this.particleAge + partialTick) / (float) this.particleMaxAge, 0.0f, 1.0f);
		return super.getBrightness(partialTick) * decay + (1.0f - decay);
	}

	@Override
	public int getLightmapCoord(float partialTicks) {
		return LightmapHelper.setBlocklightValue(15, 15);
	}

	public void renderParticle(Tessellator t, float partialTick, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		if (this.particleTexture == null) {
			return;
		}
		float texMinU = (float) this.particleTexture.getIconUMin();
		float texMaxU = (float) this.particleTexture.getIconUMax();
		float texMinV = (float) this.particleTexture.getIconVMin();
		float texMaxV = (float) this.particleTexture.getIconVMax();
		float scale = 0.1f * this.particleScale;
		float posX = (float) (this.xo + (this.x - this.xo) * (double) partialTick - lerpPosX);
		float posY = (float) (this.yo + (this.y - this.yo) * (double) partialTick - lerpPosY);
		float posZ = (float) (this.zo + (this.z - this.zo) * (double) partialTick - lerpPosZ);
		float brightness = 1.0f;
		if (LightmapHelper.isLightmapEnabled()) {
			t.setLightmapCoord(this.getLightmapCoord(partialTick));
		} else {
			brightness = 1.0F;
		}
		t.setColorOpaque_F(this.particleRed * brightness, this.particleGreen * brightness, this.particleBlue * brightness);
		t.addVertexWithUV(posX - rotationX * scale - rotationYZ * scale, posY - rotationXZ * scale, posZ - rotationZ * scale - rotationXY * scale, texMaxU, texMaxV);
		t.addVertexWithUV(posX - rotationX * scale + rotationYZ * scale, posY + rotationXZ * scale, posZ - rotationZ * scale + rotationXY * scale, texMaxU, texMinV);
		t.addVertexWithUV(posX + rotationX * scale + rotationYZ * scale, posY + rotationXZ * scale, posZ + rotationZ * scale + rotationXY * scale, texMinU, texMinV);
		t.addVertexWithUV(posX + rotationX * scale - rotationYZ * scale, posY - rotationXZ * scale, posZ + rotationZ * scale - rotationXY * scale, texMinU, texMaxV);
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
		this.particleTexture = TextureRegistry.getTexture(MOD_ID + ":particle/trial");
	}

	@Override
	public int getFXLayer() {
		return 2;
	}
}

