package cofh.core.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;

public class ItemAxeAdv extends ItemToolAdv {
	
	public ItemAxeAdv(Item.ToolMaterial toolMaterial) {
		
		super(3.0F, 3.0F, toolMaterial);
		addToolClass("axe");

		effectiveBlocks.addAll(new ItemAxeFake().geteffectiveblocks());
		effectiveMaterials.add(Material.WOOD);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.CACTUS);
		effectiveMaterials.add(Material.GOURD);
	}
	public void registertexture() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(this.getUnlocalizedName().substring(5), "inventory"));
	}
	public class ItemAxeFake extends ItemAxe {

		public ItemAxeFake()
		{
			super(Item.ToolMaterial.WOOD);
		}
		public Set<Block> geteffectiveblocks(){
			return effectiveBlocks;
		}
	}
}
