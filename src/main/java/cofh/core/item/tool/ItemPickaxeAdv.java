package cofh.core.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;

public class ItemPickaxeAdv extends ItemToolAdv {

	public ItemPickaxeAdv(ToolMaterial toolMaterial) {

		super(2.0F, 2.0F, toolMaterial);
		addToolClass("pickaxe");

		effectiveBlocks.addAll(new ItemPickaxeFake().geteffectiveblocks());
		effectiveMaterials.add(Material.IRON);
		effectiveMaterials.add(Material.ANVIL);
		effectiveMaterials.add(Material.ROCK);
		effectiveMaterials.add(Material.ICE);
		effectiveMaterials.add(Material.PACKED_ICE);
		effectiveMaterials.add(Material.GLASS);
		effectiveMaterials.add(Material.REDSTONE_LIGHT);
	}
	public void registertexture() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(this.getUnlocalizedName().substring(5), "inventory"));
	}
	public class ItemPickaxeFake extends ItemPickaxe {

		public ItemPickaxeFake()
		{
			super(Item.ToolMaterial.WOOD);
		}
		public Set<Block> geteffectiveblocks(){
			return effectiveBlocks;
		}
	}
}
