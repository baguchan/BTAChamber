package baguchan.chamber_bta.entity.breeze;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.model.ModelBase;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.model.entity.BenchEntityModel;

public class BreezeRender extends LivingRenderer<EntityBreeze> {
	public final BenchEntityModel breezeModel;
	public final BenchEntityModel windModel;

	public BreezeRender(BenchEntityModel orCreateEntityModel, BenchEntityModel windModel, float v) {
		super(orCreateEntityModel, v);
		this.setRenderPassModel(windModel);
		this.breezeModel = orCreateEntityModel;
		this.windModel = windModel;
	}

	protected boolean renderPassModel(EntityBreeze entity, int i, float f) {

		if (i == 0) {
			float f1 = (float) entity.tickCount + f;
			this.setRenderPassModel(windModel);
			this.loadTexture("/assets/chamber_bta/textures/entity/breeze/breeze_wind.png");

			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glLoadIdentity();
			GL11.glTranslatef(xOffset(entity.tickCount + f) % 1.0F, 0, 0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			return true;
		}

		if (i == 1) {
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			return true;
		}

		if (i == 2) {
			this.setRenderPassModel(breezeModel);
			this.loadTexture("/assets/chamber_bta/textures/entity/breeze/breeze_eyes.png");
			float brightness = entity.getBrightness(1.0f);
			if (LightmapHelper.isLightmapEnabled()) {
				LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15, 15));
			}
			float f1 = (1.0f - brightness) * 0.5f;
			GL11.glEnable(3042);
			GL11.glDisable(3008);
			GL11.glBlendFunc(770, 771);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, f1);
			return true;
		}
		return false;
	}

	@Override
	public void setOverlayModel(ModelBase modelbase, String texture) {
		super.setOverlayModel(modelbase, texture);
	}

	private float xOffset(float p_312086_) {
		return p_312086_ * 0.02F;
	}

	@Override
	protected boolean shouldRenderPass(EntityBreeze entity, int renderPass, float partialTick) {
		return this.renderPassModel(entity, renderPass, partialTick);
	}
}
