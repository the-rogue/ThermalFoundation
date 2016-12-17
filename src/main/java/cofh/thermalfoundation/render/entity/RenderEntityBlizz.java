package cofh.thermalfoundation.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.lib.util.helpers.HolidayHelper;
import cofh.thermalfoundation.entity.monster.EntityBlizz;
import cofh.thermalfoundation.render.model.ModelElemental;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class RenderEntityBlizz extends RenderLiving<EntityBlizz> {

	public static final RenderEntityBlizz instance = new RenderEntityBlizz(Minecraft.getMinecraft().getRenderManager());

	static ResourceLocation texture;

	static {
		RenderingRegistry.registerEntityRenderingHandler(EntityBlizz.class, instance);
	}

	public static void initialize() {

		if (HolidayHelper.isChristmas()) {
			texture = new ResourceLocation("thermalfoundation:textures/entity/" + "xmas/Blizz.png");
			return;
		}
		texture = new ResourceLocation("thermalfoundation:textures/entity/" + "Blizz.png");
	}

	public RenderEntityBlizz(RenderManager rendermanager) {

		super(rendermanager, ModelElemental.instance, 0.5F);
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityBlizz entityblizz) {

		return texture;
	}

}
