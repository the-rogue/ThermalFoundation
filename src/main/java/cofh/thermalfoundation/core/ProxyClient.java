package cofh.thermalfoundation.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.thermalfoundation.entity.projectile.EntityBasalzBolt;
import cofh.thermalfoundation.entity.projectile.EntityBlazeBolt;
import cofh.thermalfoundation.entity.projectile.EntityBlitzBolt;
import cofh.thermalfoundation.entity.projectile.EntityBlizzBolt;
import cofh.thermalfoundation.item.TFItems;
import cofh.thermalfoundation.render.entity.RenderEntityAsIcon;
import cofh.thermalfoundation.render.entity.RenderEntityBasalz;
import cofh.thermalfoundation.render.entity.RenderEntityBlitz;
import cofh.thermalfoundation.render.entity.RenderEntityBlizz;

public class ProxyClient extends Proxy {

	static RenderEntityAsIcon renderBlazeBolt;
	static RenderEntityAsIcon renderBlizzBolt;
	static RenderEntityAsIcon renderBlitzBolt;
	static RenderEntityAsIcon renderBasalzBolt;

	@SuppressWarnings("deprecation")
	@Override
	public void registerRenderInformation() {
		RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		renderBlazeBolt = new RenderEntityAsIcon(manager, new ResourceLocation("thermalfoundation:textures/items/material/" + "DustBlaze.png"));
		renderBlizzBolt = new RenderEntityAsIcon(manager, new ResourceLocation("thermalfoundation:textures/items/material/" + "DustBlizz.png"));
		renderBlitzBolt = new RenderEntityAsIcon(manager, new ResourceLocation("thermalfoundation:textures/items/material/" + "DustBlitz.png"));
		renderBasalzBolt = new RenderEntityAsIcon(manager, new ResourceLocation("thermalfoundation:textures/items/material/" + "DustBasalz.png"));

		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, renderBlazeBolt);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlizzBolt.class, renderBlizzBolt);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlitzBolt.class, renderBlitzBolt);
		RenderingRegistry.registerEntityRenderingHandler(EntityBasalzBolt.class, renderBasalzBolt);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {
			if (TFProps.iconBlazePowder) {
				Items.BLAZE_POWDER.setRegistryName("thermalfoundation", "material/DustBlaze");
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Items.BLAZE_POWDER, 0, new ModelResourceLocation("thermalfoundation:DustBlaze", "inventory"));
			}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void initializeIcons(TextureStitchEvent.Post event) {
		RenderEntityBlizz.initialize();
		RenderEntityBlitz.initialize();
		RenderEntityBasalz.initialize();
		
		renderBlazeBolt.setTextureItem(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("blaze_powder"))));
		renderBlizzBolt.setTextureItem(TFItems.dustBlizz);
		renderBlitzBolt.setTextureItem(TFItems.dustBlitz);
		renderBasalzBolt.setTextureItem(TFItems.dustBasalz);
	}

}
