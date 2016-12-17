package cofh.core.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;

public class ItemBucketCoFH extends ItemBucket implements IItemIterable<Item> {

	public ItemBucketCoFH(Block containedBlock, String modname, String name, CreativeTabs tab)
	{
		super(containedBlock);
		setUnlocalizedName(modname + ":" + name);
		setCreativeTab(tab);
		setRegistryName(modname, name);
	}
	@Override
	public void registertextures() {
		
	}
}
