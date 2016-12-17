package cofh.thermalfoundation.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.thermalfoundation.entity.monster.EntityBlitz;
import cofh.thermalfoundation.render.model.ModelElemental;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class RenderEntityBlitz extends RenderLiving<EntityBlitz> {

	public static final RenderEntityBlitz instance = new RenderEntityBlitz(Minecraft.getMinecraft().getRenderManager());

	static ResourceLocation texture;

	static {
		RenderingRegistry.registerEntityRenderingHandler(EntityBlitz.class, instance);
	}

	public static void initialize() {

		texture = new ResourceLocation("thermalfoundation:textures/entity/" + "Blitz.png");
	}

	public RenderEntityBlitz(RenderManager rendermanager) {

		super(rendermanager, ModelElemental.instance, 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBlitz entityBlitz) {
		
		return texture;
	}

}
