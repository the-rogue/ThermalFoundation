package cofh.thermalfoundation.fluid;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TFFluids {

	public static void preInit() {

		String fluidlocations = "thermalfoundation:textures/blocks/fluid/Fluid_";
		fluidRedstone = new Fluid("redstone", new ResourceLocation(fluidlocations + "Redstone_Still.png"), new ResourceLocation(fluidlocations + "Redstone_Flow.png"))
			.setLuminosity(7).setDensity(1200).setViscosity(1500).setTemperature(300).setRarity(EnumRarity.UNCOMMON);
		fluidGlowstone = new Fluid("glowstone", new ResourceLocation(fluidlocations + "Glowstone_Still.png"), new ResourceLocation(fluidlocations + "Glowstone_Flow.png"))
			.setLuminosity(15).setDensity(-500).setViscosity(100).setTemperature(300).setGaseous(true).setRarity(EnumRarity.UNCOMMON);
		fluidEnder = new Fluid("ender", new ResourceLocation(fluidlocations + "Ender_Still.png"), new ResourceLocation(fluidlocations + "Ender_Flow.png"))
			.setLuminosity(3).setDensity(4000).setViscosity(3000).setTemperature(300).setRarity(EnumRarity.UNCOMMON);
		fluidPyrotheum = new Fluid("pyrotheum", new ResourceLocation(fluidlocations + "Pyrotheum_Still.png"), new ResourceLocation(fluidlocations + "Pyrotheum_Flow.png"))
			.setLuminosity(15).setDensity(2000).setViscosity(1200).setTemperature(4000).setRarity(EnumRarity.RARE);
		fluidCryotheum = new Fluid("cryotheum", new ResourceLocation(fluidlocations + "Cryotheum_Still.png"), new ResourceLocation(fluidlocations + "Cryotheum_Flow.png"))
			.setLuminosity(0).setDensity(4000).setViscosity(3000).setTemperature(50).setRarity(EnumRarity.RARE);
		fluidAerotheum = new Fluid("aerotheum", new ResourceLocation(fluidlocations + "Aerotheum_Still.png"), new ResourceLocation(fluidlocations + "Aerotheum_Flow.png"))
			.setLuminosity(0).setDensity(-800).setViscosity(100).setTemperature(300).setGaseous(true).setRarity(EnumRarity.RARE);
		fluidPetrotheum = new Fluid("petrotheum", new ResourceLocation(fluidlocations + "Petrotheum_Still.png"), new ResourceLocation(fluidlocations + "Petrotheum_Flow.png"))
			.setLuminosity(0).setDensity(4000).setViscosity(1500).setTemperature(400).setRarity(EnumRarity.RARE);
		fluidMana = new Fluid("mana", new ResourceLocation(fluidlocations + "Mana_Still.png"), new ResourceLocation(fluidlocations + "Mana_Flow.png"))
			.setLuminosity(15).setDensity(600).setViscosity(6000).setTemperature(350).setRarity(EnumRarity.EPIC);
		fluidSteam = new Fluid("steam", new ResourceLocation(fluidlocations + "Steam_Still.png"), new ResourceLocation(fluidlocations + "Steam_Flow.png"))
			.setLuminosity(0).setDensity(-1000).setViscosity(200).setTemperature(750).setGaseous(true);
		fluidCoal = new Fluid("coal", new ResourceLocation(fluidlocations + "Coal_Still.png"), new ResourceLocation(fluidlocations + "Coal_Flow.png"))
			.setLuminosity(0).setDensity(900).setViscosity(2000).setTemperature(300);

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
/*
	public static void registerDispenserHandlers() {

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TFItems.itemBucket, new DispenserFilledBucketHandler());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new DispenserEmptyBucketHandler());
	}
*/
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
