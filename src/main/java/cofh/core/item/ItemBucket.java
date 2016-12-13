package cofh.core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.core.util.fluid.BucketHandler;
import cofh.lib.render.IFluidOverlayItem;
import cofh.repack.codechicken.lib.texture.TextureUtils.IIconRegister;

public class ItemBucket extends ItemBase implements IFluidOverlayItem {

	Item container = Items.BUCKET;

	public ItemBucket() {

		super();
		setMaxStackSize(1);
		setContainerItem(container);
		itemMap.put(-1000, new ItemEntry("OverlayIcon"));
	}

	public ItemBucket(String modName) {

		super(modName);
		setMaxStackSize(1);
		setContainerItem(container);
		itemMap.put(-1000, new ItemEntry("OverlayIcon"));
	}

	public ItemBucket(String modName, Item container) {

		super(modName);
		setMaxStackSize(1);
		this.container = container;
		setContainerItem(container);
		itemMap.put(-1000, new ItemEntry("OverlayIcon"));
	}

	@SideOnly(Side.CLIENT)
	public void registertexture(IIconRegister ir) {

		if (!hasTextures) {
			return;
		}
		{
			ItemEntry item = itemMap.get(-1000);
			item.registertexture();
		}
		for (int i = 0; i < itemList.size(); i++) {
			ItemEntry item = itemMap.get(itemList.get(i));
			item.registertexture();
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

		RayTraceResult raytraceresult = this.rayTrace(world, player, false);

		if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}
		BlockPos pos = raytraceresult.getBlockPos();

		switch (raytraceresult.sideHit) {
		case DOWN:
			pos.add(0, -1, 0);
			break;
		case UP:
			pos.add(0, 1, 0);
			break;
		case NORTH:
			pos.add(0, 0, -1);
			break;
		case SOUTH:
			pos.add(0, 0, 1);
			break;
		case WEST:
			pos.add(-1, 0, 0);
			break;
		case EAST:
			pos.add(1, 0, 0);
			break;
		}
		if (!player.canPlayerEdit(pos, raytraceresult.sideHit, stack) || !world.isAirBlock(pos) && world.getBlockState(pos).getMaterial().isSolid()) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		if (BucketHandler.emptyBucket(player, world, pos, stack)) {
			if (!player.capabilities.isCreativeMode) {
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(container));
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

}
