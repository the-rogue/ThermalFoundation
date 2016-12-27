package cofh.thermalfoundation.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.lib.util.helpers.StringHelper;

public class ItemBlockOre extends ItemBlock {

	public ItemBlockOre(Block block) {

		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public String getItemStackDisplayName(ItemStack item) {

		return StringHelper.localize(getUnlocalizedName(item));
	}

	@Override
	public String getUnlocalizedName(ItemStack item) {

		return super.getUnlocalizedName(item) + BlockOre.EnumType.getTypeFromMeta(item.getItemDamage()).getName();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		for(BlockOre.EnumType oretype : BlockOre.EnumType.values()) {
			subItems.add(new ItemStack(item, 1, oretype.getMeta()));
		}
	}
	
	@Override
	public int getMetadata(int damage) {

		return damage;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return EnumRarity.values()[BlockOre.EnumType.getTypeFromMeta(stack.getItemDamage()).getRarity()];
	}
	
	@SideOnly(Side.CLIENT)
	public void registertexture() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(this.getUnlocalizedName().substring(5), "inventory"));
	}
}
