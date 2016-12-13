package cofh.core.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSpade;

public class ItemShovelAdv extends ItemToolAdv {

	@SuppressWarnings("unchecked")
	public ItemShovelAdv(ToolMaterial toolMaterial) {

		super(1.0F, toolMaterial);
		addToolClass("shovel");

		effectiveBlocks.addAll(ItemSpade.effectiveBlocks);
		effectiveMaterials.add(Material.GROUND);
		effectiveMaterials.add(Material.GRASS);
		effectiveMaterials.add(Material.SAND);
		effectiveMaterials.add(Material.SNOW);
		effectiveMaterials.add(Material.CRAFTED_SNOW);
		effectiveMaterials.add(Material.CLAY);
	}
	public void registertexture() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(this.getUnlocalizedName().substring(5), "inventory"));
	}

}
