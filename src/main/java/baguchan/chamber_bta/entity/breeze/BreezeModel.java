package baguchan.chamber_bta.entity.breeze;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.MathHelper;
import org.useless.dragonfly.helper.AnimationHelper;
import org.useless.dragonfly.model.entity.AnimationState;
import org.useless.dragonfly.model.entity.BenchEntityModel;
import org.useless.dragonfly.model.entity.animation.Animation;
import org.useless.dragonfly.model.entity.animation.AnimationData;
import org.useless.dragonfly.model.entity.processor.BenchEntityBones;

import static baguchan.chamber_bta.ChamberBTA.MOD_ID;

public class BreezeModel extends BenchEntityModel {

	private static EntityBreeze breeze;

	@Override
	public void setLivingAnimations(EntityLiving entityliving, float limbSwing, float limbYaw, float renderPartialTicks) {
		super.setLivingAnimations(entityliving, limbSwing, limbYaw, renderPartialTicks);
		if (entityliving instanceof EntityBreeze) {
			breeze = (EntityBreeze) entityliving;
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		this.getIndexBones().forEach((s, benchEntityBones) -> {
			benchEntityBones.resetPose();
		});
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		BenchEntityBones tornadoTop = this.getIndexBones().get("tornado_top");
		BenchEntityBones tornadoMid = this.getIndexBones().get("tornado_mid");
		BenchEntityBones tornadoBottom = this.getIndexBones().get("tornado_bottom");
		BenchEntityBones head = this.getIndexBones().get("head");
		BenchEntityBones rods = this.getIndexBones().get("rods");

		float f = ticksExisted * 3.1415927F * -0.1F;
		tornadoTop.rotationPointX = MathHelper.cos(f) * 1.0F * 0.6F;
		tornadoTop.rotationPointZ = MathHelper.sin(f) * 1.0F * 0.6F;
		tornadoMid.rotationPointX = MathHelper.sin(f) * 0.5F * 0.8F;
		tornadoMid.rotationPointZ = MathHelper.cos(f) * 0.8F;
		tornadoBottom.rotationPointX = MathHelper.cos(f) * -0.25F * 1.0F;
		tornadoBottom.rotationPointZ = MathHelper.sin(f) * -0.25F * 1.0F;
		head.rotationPointY -= 4.0F + MathHelper.cos(f) / 4.0F;
		rods.rotateAngleY = ticksExisted * 3.1415927F * 0.1F;
		Animation testAnimation = AnimationHelper.getOrCreateEntityAnimation(MOD_ID, "breeze.animation");

		if (breeze != null) {

			//animateWalk(testAnimation.getAnimations().get("animation.breeze.idle"), limbSwing, limbYaw, 2F, 2.5F);
			animateWalk(testAnimation.getAnimations().get("animation.breeze.slide"), limbSwing, limbYaw, 2F, 2.5F);

			animate(breeze.shootAnimationState, testAnimation.getAnimations().get("animation.breeze.shoot"), ticksExisted, 1F);

		}
	}

	protected void animate(AnimationState animationState, AnimationData animationData, float p_233388_, float p_233389_) {
		animationState.updateTime(p_233388_, p_233389_);
		animationState.ifStarted((p_233392_) -> {
			AnimationHelper.animate(this, animationData, p_233392_.getAccumulatedTime(), 0.5F, this.VEC_ANIMATION);
		});
	}
}
