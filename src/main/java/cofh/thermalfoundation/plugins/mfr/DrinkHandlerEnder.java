package cofh.thermalfoundation.plugins.mfr;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import powercrystals.minefactoryreloaded.api.ILiquidDrinkHandler;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.ServerHelper;

public class DrinkHandlerEnder implements ILiquidDrinkHandler {

	public static DrinkHandlerEnder instance = new DrinkHandlerEnder();

	@Override
	public void onDrink(EntityLivingBase player) {

		if (ServerHelper.isClientWorld(player.worldObj)) {
			return;
		}
		int x2 = (int) (player.posX - MFRPlugin.strawEnderRange + player.worldObj.rand.nextInt(MFRPlugin.strawEnderRange * 2));
		int y2 = (int) (player.posY + player.worldObj.rand.nextInt(8));
		int z2 = (int) (player.posZ - MFRPlugin.strawEnderRange + player.worldObj.rand.nextInt(MFRPlugin.strawEnderRange * 2));

		if (!player.worldObj.getBlockState(new BlockPos(x2, y2, z2)).getMaterial().isSolid()) {
			CoreUtils.teleportEntityTo(player, x2, y2, z2);
			player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.portal.trigger")), 1.0F, 1.0F);
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("nausea"), 15 * 20, 0));
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 15 * 20, 0));
		} else {
			for (int i = 0; i < 1 + player.worldObj.rand.nextInt(3); i++) {
				CoreUtils.dropItemStackIntoWorld(new ItemStack(Items.ENDER_PEARL), player.worldObj, player.posX, player.posY, player.posZ);
			}
		}
	}

}
