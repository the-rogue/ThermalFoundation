package cofh.thermalfoundation.plugins.mfr;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import powercrystals.minefactoryreloaded.api.ILiquidDrinkHandler;

public class DrinkHandlerCoal implements ILiquidDrinkHandler {

	public static DrinkHandlerCoal instance = new DrinkHandlerCoal();

	@Override
	public void onDrink(EntityLivingBase player) {

		player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("haste"), 60 * 20, 0));
		player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("blindness"), 10 * 20, 0));
	}

}
