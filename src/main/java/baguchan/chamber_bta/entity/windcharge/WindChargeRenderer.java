package baguchan.chamber_bta.entity.windcharge;

import baguchan.chamber_bta.item.ModItems;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;

public class WindChargeRenderer extends EntityRenderer<EntityBreezeWindCharge> {
	public void renderWindCharge(EntityBreezeWindCharge entity, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glDisable(2896);
		GL11.glEnable(32826);
		float scalar = 2.0f;
		GL11.glScalef(1.0f, 1.0f, 1.0f);
		IconCoordinate i = ((ItemModel) ItemModelDispatcher.getInstance().getDispatch(ModItems.wind_charge)).getIcon(null, ModItems.wind_charge.getDefaultStack());
		i.parentAtlas.bindTexture();
		Tessellator tessellator = Tessellator.instance;
		float f3 = (float) i.getIconUMin();
		float f4 = (float) i.getIconUMax();
		float f5 = (float) i.getIconVMin();
		float f6 = (float) i.getIconVMax();
		float f7 = 1.0f;
		float f8 = 0.5f;
		float f9 = 0.25f;
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glRotatef(180.0f - this.renderDispatcher.viewLerpYaw, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(-this.renderDispatcher.viewLerpPitch, 1.0f, 0.0f, 0.0f);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0f, 0.0f, 0.0f);
		tessellator.addVertexWithUV(0.0f - f8, 0.0f - f9, 0.0, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0f - f9, 0.0, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0f - f9, 0.0, f4, f5);
		tessellator.addVertexWithUV(0.0f - f8, 1.0f - f9, 0.0, f3, f5);
		tessellator.draw();
		GL11.glDisable(32826);
		GL11.glPopMatrix();
		GL11.glEnable(2896);
	}

	@Override
	public void doRender(Tessellator tessellator, EntityBreezeWindCharge entity, double x, double y, double z, float yaw, float partialTick) {
		this.renderWindCharge(entity, x, y, z, yaw, partialTick);
	}
}

