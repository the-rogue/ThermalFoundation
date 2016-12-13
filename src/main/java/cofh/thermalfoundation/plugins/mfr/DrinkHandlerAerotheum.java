package cofh.thermalfoundation.plugins.mfr;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import powercrystals.minefactoryreloaded.api.ILiquidDrinkHandler;

public class DrinkHandlerAerotheum implements ILiquidDrinkHandler {

	public static DrinkHandlerAerotheum instance = new DrinkHandlerAerotheum();

	@Override
	public void onDrink(EntityLivingBase player) {

		player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("water_breathing"), 240 * 20, 0));
		player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 60 * 20, 0));
	}

}
