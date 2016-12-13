package cofh.thermalfoundation.fluid;

import cofh.core.util.fluid.DispenserEmptyBucketHandler;
import cofh.core.util.fluid.DispenserFilledBucketHandler;
import cofh.thermalfoundation.item.TFItems;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TFFluids {

	public static void preInit() {

		fluidRedstone = new Fluid("redstone").setLuminosity(7).setDensity(1200).setViscosity(1500).setTemperature(300).setRarity(EnumRarity.UNCOMMON);
		fluidGlowstone = new Fluid("glowstone").setLuminosity(15).setDensity(-500).setViscosity(100).setTemperature(300).setGaseous(true)
				.setRarity(EnumRarity.UNCOMMON);
		fluidEnder = new Fluid("ender").setLuminosity(3).setDensity(4000).setViscosity(3000).setTemperature(300).setRarity(EnumRarity.UNCOMMON);
		fluidPyrotheum = new Fluid("pyrotheum").setLuminosity(15).setDensity(2000).setViscosity(1200).setTemperature(4000).setRarity(EnumRarity.RARE);
		fluidCryotheum = new Fluid("cryotheum").setLuminosity(0).setDensity(4000).setViscosity(3000).setTemperature(50).setRarity(EnumRarity.RARE);
		fluidAerotheum = new Fluid("aerotheum").setLuminosity(0).setDensity(-800).setViscosity(100).setTemperature(300).setGaseous(true)
				.setRarity(EnumRarity.RARE);
		fluidPetrotheum = new Fluid("petrotheum").setLuminosity(0).setDensity(4000).setViscosity(1500).setTemperature(400).setRarity(EnumRarity.RARE);
		fluidMana = new Fluid("mana").setLuminosity(15).setDensity(600).setViscosity(6000).setTemperature(350).setRarity(EnumRarity.EPIC);
		fluidSteam = new Fluid("steam").setLuminosity(0).setDensity(-1000).setViscosity(200).setTemperature(750).setGaseous(true);
		fluidCoal = new Fluid("coal").setLuminosity(0).setDensity(900).setViscosity(2000).setTemperature(300);

		registerFluid(fluidRedstone, "redstone");
		registerFluid(fluidGlowstone, "glowstone");
		registerFluid(fluidEnder, "ender");
		registerFluid(fluidPyrotheum, "pyrotheum");
		registerFluid(fluidCryotheum, "cryotheum");
		registerFluid(fluidAerotheum, "aerotheum");
		registerFluid(fluidPetrotheum, "petrotheum");
		registerFluid(fluidMana, "mana");
		registerFluid(fluidSteam, "steam");
		registerFluid(fluidCoal, "coal");
	}

	public static void initialize() {

	}

	public static void postInit() {

	}

	public static void registerFluid(Fluid fluid, String fluidName) {

		if (!FluidRegistry.isFluidRegistered(fluidName)) {
			FluidRegistry.registerFluid(fluid);
		}
		fluid = FluidRegistry.getFluid(fluidName);
	}

	public static void registerDispenserHandlers() {

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TFItems.itemBucket, new DispenserFilledBucketHandler());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new DispenserEmptyBucketHandler());
	}

	public static Fluid fluidRedstone;
	public static Fluid fluidGlowstone;
	public static Fluid fluidEnder;
	public static Fluid fluidPyrotheum;
	public static Fluid fluidCryotheum;
	public static Fluid fluidAerotheum;
	public static Fluid fluidPetrotheum;
	public static Fluid fluidMana;
	public static Fluid fluidSteam;
	public static Fluid fluidCoal;

}
