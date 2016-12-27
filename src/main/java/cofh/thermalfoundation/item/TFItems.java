package cofh.thermalfoundation.item;

import static cofh.lib.util.helpers.ItemHelper.ShapedRecipe;
import static cofh.lib.util.helpers.ItemHelper.ShapelessRecipe;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cofh.api.core.IInitializer;
import cofh.core.item.ItemBase;
import cofh.core.item.ItemBucketCoFH;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.block.TFBlocks;
import cofh.thermalfoundation.core.TFProps;
import cofh.thermalfoundation.fluid.TFFluids;

public class TFItems {
	
	private static final ArrayList<IInitializer> ITEMS = new ArrayList<IInitializer>();
	
	public static void preInit() {

		itemMaterial = (ItemBase) new ItemBase("thermalfoundation", "material").setCreativeTab(ThermalFoundation.tabCommon);
		
		ITEMS.add(itemLexicon = new ItemLexicon("lexicon"));
		
		ITEMS.add(bucketRedstone = new ItemBucketCoFH(TFBlocks.blockFluidRedstone, TFFluids.fluidRedstone, "thermalfoundation", "bucketRedstone", ThermalFoundation.tabCommon));
		ITEMS.add(bucketGlowstone = new ItemBucketCoFH(TFBlocks.blockFluidGlowstone, TFFluids.fluidGlowstone, "thermalfoundation", "bucketGlowstone", ThermalFoundation.tabCommon));
		ITEMS.add(bucketEnder = new ItemBucketCoFH(TFBlocks.blockFluidEnder, TFFluids.fluidEnder, "thermalfoundation", "bucketEnder", ThermalFoundation.tabCommon));
		ITEMS.add(bucketPyrotheum = new ItemBucketCoFH(TFBlocks.blockFluidPyrotheum, TFFluids.fluidPyrotheum, "thermalfoundation", "bucketPyrotheum", ThermalFoundation.tabCommon));
		ITEMS.add(bucketCryotheum = new ItemBucketCoFH(TFBlocks.blockFluidCryotheum, TFFluids.fluidCryotheum, "thermalfoundation", "bucketCryotheum", ThermalFoundation.tabCommon));
		ITEMS.add(bucketAerotheum = new ItemBucketCoFH(TFBlocks.blockFluidAerotheum, TFFluids.fluidAerotheum, "thermalfoundation", "bucketAerotheum", ThermalFoundation.tabCommon));
		ITEMS.add(bucketPetrotheum = new ItemBucketCoFH(TFBlocks.blockFluidPetrotheum, TFFluids.fluidPetrotheum, "thermalfoundation", "bucketPetrotheum", ThermalFoundation.tabCommon));
		ITEMS.add(bucketMana = new ItemBucketCoFH(TFBlocks.blockFluidMana, TFFluids.fluidMana, "thermalfoundation", "bucketMana", ThermalFoundation.tabCommon));
		ITEMS.add(bucketCoal = new ItemBucketCoFH(TFBlocks.blockFluidCoal, TFFluids.fluidCoal, "thermalfoundation", "bucketCoal", ThermalFoundation.tabCommon));
		
		for (IInitializer item : ITEMS) {
			item.preInit();
		}

		lexicon = new ItemStack(itemLexicon);
		itemLexicon.setEmpoweredState(lexicon, false);
		
		itemMaterial.preInit();
		
		/* Vanilla Derived */
		dustCoal = itemMaterial.addOreDictItem(2, "thermalfoundation", "dustCoal");
		OreDictionary.registerOre("dyeBlack", dustCoal.copy());
		dustCharcoal = itemMaterial.addOreDictItem(3, "thermalfoundation", "dustCharcoal");
		OreDictionary.registerOre("dyeBlack", dustCharcoal.copy());
		dustObsidian = itemMaterial.addOreDictItem(4, "thermalfoundation", "dustObsidian");

		dustSulfur = itemMaterial.addOreDictItem(16, "thermalfoundation", "dustSulfur");
		OreDictionary.registerOre("dyeYellow", dustSulfur.copy());
		dustNiter = itemMaterial.addItem(17, "thermalfoundation", "dustNiter");
		OreDictionary.registerOre("dustSaltpeter", dustNiter);

		crystalCinnabar = itemMaterial.addOreDictItem(20, "thermalfoundation", "crystalCinnabar");

		dustIron = itemMaterial.addOreDictItem(0, "thermalfoundation", "dustIron");
		dustGold = itemMaterial.addOreDictItem(1, "thermalfoundation", "dustGold");
		/* Dusts */
		dustCopper = itemMaterial.addOreDictItem(32, "thermalfoundation", "dustCopper");
		dustTin = itemMaterial.addOreDictItem(33, "thermalfoundation", "dustTin");
		dustSilver = itemMaterial.addOreDictItem(34, "thermalfoundation", "dustSilver");
		dustLead = itemMaterial.addOreDictItem(35, "thermalfoundation", "dustLead");
		dustNickel = itemMaterial.addOreDictItem(36, "thermalfoundation", "dustNickel");
		dustPlatinum = itemMaterial.addOreDictItem(37, "thermalfoundation", "dustPlatinum", 1);
		dustMithril = itemMaterial.addOreDictItem(38, "thermalfoundation", "dustMithril", 2);
		dustElectrum = itemMaterial.addOreDictItem(39, "thermalfoundation", "dustElectrum");
		dustInvar = itemMaterial.addOreDictItem(40, "thermalfoundation", "dustInvar");
		dustBronze = itemMaterial.addOreDictItem(41, "thermalfoundation", "dustBronze");
		dustSignalum = itemMaterial.addOreDictItem(42, "thermalfoundation", "dustSignalum", 1);
		dustLumium = itemMaterial.addOreDictItem(43, "thermalfoundation", "dustLumium", 1);
		dustEnderium = itemMaterial.addOreDictItem(44, "thermalfoundation", "dustEnderium", 2);

		/* Ingots */
		ingotCopper = itemMaterial.addOreDictItem(64, "thermalfoundation", "ingotCopper");
		ingotTin = itemMaterial.addOreDictItem(65, "thermalfoundation", "ingotTin");
		ingotSilver = itemMaterial.addOreDictItem(66, "thermalfoundation", "ingotSilver");
		ingotLead = itemMaterial.addOreDictItem(67, "thermalfoundation", "ingotLead");
		ingotNickel = itemMaterial.addOreDictItem(68, "thermalfoundation", "ingotNickel");
		ingotPlatinum = itemMaterial.addOreDictItem(69, "thermalfoundation", "ingotPlatinum", 1);
		ingotMithril = itemMaterial.addOreDictItem(70, "thermalfoundation", "ingotMithril", 2);
		ingotElectrum = itemMaterial.addOreDictItem(71, "thermalfoundation", "ingotElectrum");
		ingotInvar = itemMaterial.addOreDictItem(72, "thermalfoundation", "ingotInvar");
		ingotBronze = itemMaterial.addOreDictItem(73, "thermalfoundation", "ingotBronze");
		ingotSignalum = itemMaterial.addOreDictItem(74, "thermalfoundation", "ingotSignalum", 1);
		ingotLumium = itemMaterial.addOreDictItem(75, "thermalfoundation", "ingotLumium", 1);
		ingotEnderium = itemMaterial.addOreDictItem(76, "thermalfoundation", "ingotEnderium", 2);

		nuggetIron = itemMaterial.addOreDictItem(8, "thermalfoundation", "nuggetIron");
		/* Nuggets */
		nuggetCopper = itemMaterial.addOreDictItem(96, "thermalfoundation", "nuggetCopper");
		nuggetTin = itemMaterial.addOreDictItem(97, "thermalfoundation", "nuggetTin");
		nuggetSilver = itemMaterial.addOreDictItem(98, "thermalfoundation", "nuggetSilver");
		nuggetLead = itemMaterial.addOreDictItem(99, "thermalfoundation", "nuggetLead");
		nuggetNickel = itemMaterial.addOreDictItem(100, "thermalfoundation", "nuggetNickel");
		nuggetPlatinum = itemMaterial.addOreDictItem(101, "thermalfoundation", "nuggetPlatinum", 1);
		nuggetMithril = itemMaterial.addOreDictItem(102, "thermalfoundation", "nuggetMithril", 2);
		nuggetElectrum = itemMaterial.addOreDictItem(103, "thermalfoundation", "nuggetElectrum");
		nuggetInvar = itemMaterial.addOreDictItem(104, "thermalfoundation", "nuggetInvar");
		nuggetBronze = itemMaterial.addOreDictItem(105, "thermalfoundation", "nuggetBronze");
		nuggetSignalum = itemMaterial.addOreDictItem(106, "thermalfoundation", "nuggetSignalum", 1);
		nuggetLumium = itemMaterial.addOreDictItem(107, "thermalfoundation", "nuggetLumium", 1);
		nuggetEnderium = itemMaterial.addOreDictItem(108, "thermalfoundation", "nuggetEnderium", 2);

		gearIron = itemMaterial.addOreDictItem(12, "thermalfoundation", "gearIron");
		gearGold = itemMaterial.addOreDictItem(13, "thermalfoundation", "gearGold");
		/* Gears */
		gearCopper = itemMaterial.addOreDictItem(128, "thermalfoundation", "gearCopper");
		gearTin = itemMaterial.addOreDictItem(129, "thermalfoundation", "gearTin");
		gearSilver = itemMaterial.addOreDictItem(130, "thermalfoundation", "gearSilver");
		gearLead = itemMaterial.addOreDictItem(131, "thermalfoundation", "gearLead");
		gearNickel = itemMaterial.addOreDictItem(132, "thermalfoundation", "gearNickel");
		gearPlatinum = itemMaterial.addOreDictItem(133, "thermalfoundation", "gearPlatinum", 1);
		gearMithril = itemMaterial.addOreDictItem(134, "thermalfoundation", "gearMithril", 2);
		gearElectrum = itemMaterial.addOreDictItem(135, "thermalfoundation", "gearElectrum");
		gearInvar = itemMaterial.addOreDictItem(136, "thermalfoundation", "gearInvar");
		gearBronze = itemMaterial.addOreDictItem(137, "thermalfoundation", "gearBronze");
		gearSignalum = itemMaterial.addOreDictItem(138, "thermalfoundation", "gearSignalum", 1);
		gearLumium = itemMaterial.addOreDictItem(139, "thermalfoundation", "gearLumium", 1);
		gearEnderium = itemMaterial.addOreDictItem(140, "thermalfoundation", "gearEnderium", 2);

		/* Additional Items */
		dustPyrotheum = itemMaterial.addOreDictItem(512, "thermalfoundation", "dustPyrotheum", 2);
		dustCryotheum = itemMaterial.addOreDictItem(513, "thermalfoundation", "dustCryotheum", 2);
		dustAerotheum = itemMaterial.addOreDictItem(514, "thermalfoundation", "dustAerotheum", 2);
		dustPetrotheum = itemMaterial.addOreDictItem(515, "thermalfoundation", "dustPetrotheum", 2);
		dustMana = itemMaterial.addItem(516, "thermalfoundation", "dustMana", 3);
		
		FurnaceFuelHandler.registerFuel(dustPyrotheum, TFProps.dustPyrotheumFuel);

		/* Mob Drops */
		rodBlizz = itemMaterial.addOreDictItem(1024, "thermalfoundation", "rodBlizz");
		dustBlizz = itemMaterial.addOreDictItem(1025, "thermalfoundation", "dustBlizz");
		rodBlitz = itemMaterial.addOreDictItem(1026, "thermalfoundation", "rodBlitz");
		dustBlitz = itemMaterial.addOreDictItem(1027, "thermalfoundation", "dustBlitz");
		rodBasalz = itemMaterial.addOreDictItem(1028, "thermalfoundation", "rodBasalz");
		dustBasalz = itemMaterial.addOreDictItem(1029, "thermalfoundation", "dustBasalz");
		
		/* Equipment */
		Equipment.preInit();
	}

	public static void initialize() {

		ingotIron = new ItemStack(Items.IRON_INGOT);
		ingotGold = new ItemStack(Items.GOLD_INGOT);
		nuggetGold = new ItemStack(Items.GOLD_NUGGET);

		for (IInitializer item : ITEMS) {
			item.initialize();
		}
		
		itemMaterial.initialize();
		
		/* Equipment */
		Equipment.initialize();
	}

	public static void postInit() {

		for (IInitializer item : ITEMS) {
			item.postInit();
		}
		
		ItemHelper.addRecipe(ShapedRecipe(lexicon, new Object[] { " D ", "GBI", " R ", 'D', Items.DIAMOND, 'G', "ingotGold", 'B', Items.BOOK, 'I', "ingotIron",
				'R', "dustRedstone" }));

		// @formatter: off
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustPyrotheum, 2), new Object[] { "dustCoal", "dustSulfur", "dustRedstone",
				Items.BLAZE_POWDER }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustCryotheum, 2), new Object[] { Items.SNOWBALL, "dustSaltpeter", "dustRedstone",
				"dustBlizz" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustAerotheum, 2), new Object[] { "sand", "dustSaltpeter", "dustRedstone", "dustBlitz" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustPetrotheum, 2), new Object[] { Items.CLAY_BALL, "dustObsidian", "dustRedstone",
				"dustBasalz" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustBlizz, 2), "rodBlizz"));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustBlitz, 2), "rodBlitz"));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustBasalz, 2), "rodBasalz"));
		// @formatter: on

		/* Smelting */
		ItemHelper.addSmelting(ingotIron, dustIron, 0.0F);
		ItemHelper.addSmelting(ingotGold, dustGold, 0.0F);
		ItemHelper.addSmelting(ingotCopper, dustCopper, 0.0F);
		ItemHelper.addSmelting(ingotTin, dustTin, 0.0F);
		ItemHelper.addSmelting(ingotSilver, dustSilver, 0.0F);
		ItemHelper.addSmelting(ingotLead, dustLead, 0.0F);
		ItemHelper.addSmelting(ingotNickel, dustNickel, 0.0F);
		ItemHelper.addSmelting(ingotPlatinum, dustPlatinum, 0.0F);
		ItemHelper.addSmelting(ingotMithril, dustMithril, 0.0F);
		ItemHelper.addSmelting(ingotElectrum, dustElectrum, 0.0F);
		ItemHelper.addSmelting(ingotInvar, dustInvar, 0.0F);
		ItemHelper.addSmelting(ingotBronze, dustBronze, 0.0F);
		ItemHelper.addSmelting(ingotSignalum, dustSignalum, 0.0F);
		ItemHelper.addSmelting(ingotLumium, dustLumium, 0.0F);
		// No Enderium

		/* Alloy Recipes */
		// @formatter: off
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustElectrum, 2), new Object[] { "dustGold", "dustSilver" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustInvar, 3), new Object[] { "dustIron", "dustIron", "dustNickel" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustBronze, 4), new Object[] { "dustCopper", "dustCopper", "dustCopper", "dustTin" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustSignalum, 4), new Object[] { "dustCopper", "dustCopper", "dustCopper", "dustSilver",
				"bucketRedstone" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustLumium, 4), new Object[] { "dustTin", "dustTin", "dustTin", "dustSilver",
				"bucketGlowstone" }));
		ItemHelper.addRecipe(ShapelessRecipe(ItemHelper.cloneStack(dustEnderium, 4), new Object[] { "dustTin", "dustTin", "dustSilver", "dustPlatinum",
				"bucketEnder" }));
		// @formatter: on

		/* Storage */
		ItemHelper.addTwoWayStorageRecipe(ingotIron, "ingotIron", nuggetIron, "nuggetIron");
		ItemHelper.addTwoWayStorageRecipe(ingotCopper, "ingotCopper", nuggetCopper, "nuggetCopper");
		ItemHelper.addTwoWayStorageRecipe(ingotTin, "ingotTin", nuggetTin, "nuggetTin");
		ItemHelper.addTwoWayStorageRecipe(ingotSilver, "ingotSilver", nuggetSilver, "nuggetSilver");
		ItemHelper.addTwoWayStorageRecipe(ingotLead, "ingotLead", nuggetLead, "nuggetLead");
		ItemHelper.addTwoWayStorageRecipe(ingotNickel, "ingotNickel", nuggetNickel, "nuggetNickel");
		ItemHelper.addTwoWayStorageRecipe(ingotPlatinum, "ingotPlatinum", nuggetPlatinum, "nuggetPlatinum");
		ItemHelper.addTwoWayStorageRecipe(ingotMithril, "ingotMithril", nuggetMithril, "nuggetMithril");
		ItemHelper.addTwoWayStorageRecipe(ingotElectrum, "ingotElectrum", nuggetElectrum, "nuggetElectrum");
		ItemHelper.addTwoWayStorageRecipe(ingotInvar, "ingotInvar", nuggetInvar, "nuggetInvar");
		ItemHelper.addTwoWayStorageRecipe(ingotBronze, "ingotBronze", nuggetBronze, "nuggetBronze");
		ItemHelper.addTwoWayStorageRecipe(ingotSignalum, "ingotSignalum", nuggetSignalum, "nuggetSignalum");
		ItemHelper.addTwoWayStorageRecipe(ingotLumium, "ingotLumium", nuggetLumium, "nuggetLumium");
		ItemHelper.addTwoWayStorageRecipe(ingotEnderium, "ingotEnderium", nuggetEnderium, "nuggetEnderium");

		ItemHelper.addReverseStorageRecipe(ingotCopper, "blockCopper");
		ItemHelper.addReverseStorageRecipe(ingotTin, "blockTin");
		ItemHelper.addReverseStorageRecipe(ingotSilver, "blockSilver");
		ItemHelper.addReverseStorageRecipe(ingotLead, "blockLead");
		ItemHelper.addReverseStorageRecipe(ingotNickel, "blockNickel");
		ItemHelper.addReverseStorageRecipe(ingotPlatinum, "blockPlatinum");
		ItemHelper.addReverseStorageRecipe(ingotMithril, "blockMithril");
		ItemHelper.addReverseStorageRecipe(ingotElectrum, "blockElectrum");
		ItemHelper.addReverseStorageRecipe(ingotInvar, "blockInvar");
		ItemHelper.addReverseStorageRecipe(ingotBronze, "blockBronze");
		ItemHelper.addReverseStorageRecipe(ingotSignalum, "blockSignalum");
		ItemHelper.addReverseStorageRecipe(ingotLumium, "blockLumium");
		ItemHelper.addReverseStorageRecipe(ingotEnderium, "blockEnderium");

		/* Gears */
		ItemHelper.addGearRecipe(gearIron, "ingotIron");
		ItemHelper.addGearRecipe(gearGold, "ingotGold");
		ItemHelper.addGearRecipe(gearCopper, "ingotCopper");
		ItemHelper.addGearRecipe(gearTin, "ingotTin");
		ItemHelper.addGearRecipe(gearSilver, "ingotSilver");
		ItemHelper.addGearRecipe(gearLead, "ingotLead");
		ItemHelper.addGearRecipe(gearNickel, "ingotNickel");
		ItemHelper.addGearRecipe(gearPlatinum, "ingotPlatinum");
		ItemHelper.addGearRecipe(gearMithril, "ingotMithril");
		ItemHelper.addGearRecipe(gearElectrum, "ingotElectrum");
		ItemHelper.addGearRecipe(gearInvar, "ingotInvar");
		ItemHelper.addGearRecipe(gearBronze, "ingotBronze");
		ItemHelper.addGearRecipe(gearSignalum, "ingotSignalum");
		ItemHelper.addGearRecipe(gearLumium, "ingotLumium");
		ItemHelper.addGearRecipe(gearEnderium, "ingotEnderium");

		itemMaterial.postInit();
		
		/* Equipment */
		Equipment.postInit();
	}

	public static ItemLexicon itemLexicon;
	public static ItemBase itemMaterial;

	public static ItemBucketCoFH bucketRedstone;
	public static ItemBucketCoFH bucketGlowstone;
	public static ItemBucketCoFH bucketEnder;
	public static ItemBucketCoFH bucketPyrotheum;
	public static ItemBucketCoFH bucketCryotheum;
	public static ItemBucketCoFH bucketAerotheum;
	public static ItemBucketCoFH bucketPetrotheum;
	public static ItemBucketCoFH bucketMana;
	public static ItemBucketCoFH bucketCoal;

	public static ItemStack lexicon;

	public static ItemStack ingotIron;
	public static ItemStack ingotGold;
	public static ItemStack nuggetGold;

	public static ItemStack dustIron;
	public static ItemStack dustGold;
	public static ItemStack dustCoal;
	public static ItemStack dustCharcoal;
	public static ItemStack dustObsidian;

	public static ItemStack nuggetIron;
	public static ItemStack gearIron;
	public static ItemStack gearGold;

	public static ItemStack dustSulfur;
	public static ItemStack dustNiter;

	public static ItemStack crystalCinnabar;

	public static ItemStack ingotCopper;
	public static ItemStack ingotTin;
	public static ItemStack ingotSilver;
	public static ItemStack ingotLead;
	public static ItemStack ingotNickel;
	public static ItemStack ingotPlatinum;
	public static ItemStack ingotMithril;
	public static ItemStack ingotElectrum;
	public static ItemStack ingotInvar;
	public static ItemStack ingotBronze;
	public static ItemStack ingotSignalum;
	public static ItemStack ingotLumium;
	public static ItemStack ingotEnderium;

	public static ItemStack dustCopper;
	public static ItemStack dustTin;
	public static ItemStack dustSilver;
	public static ItemStack dustLead;
	public static ItemStack dustNickel;
	public static ItemStack dustPlatinum;
	public static ItemStack dustMithril;
	public static ItemStack dustElectrum;
	public static ItemStack dustInvar;
	public static ItemStack dustBronze;
	public static ItemStack dustSignalum;
	public static ItemStack dustLumium;
	public static ItemStack dustEnderium;

	public static ItemStack nuggetCopper;
	public static ItemStack nuggetTin;
	public static ItemStack nuggetSilver;
	public static ItemStack nuggetLead;
	public static ItemStack nuggetNickel;
	public static ItemStack nuggetPlatinum;
	public static ItemStack nuggetMithril;
	public static ItemStack nuggetElectrum;
	public static ItemStack nuggetInvar;
	public static ItemStack nuggetBronze;
	public static ItemStack nuggetSignalum;
	public static ItemStack nuggetLumium;
	public static ItemStack nuggetEnderium;

	public static ItemStack gearCopper;
	public static ItemStack gearTin;
	public static ItemStack gearSilver;
	public static ItemStack gearLead;
	public static ItemStack gearNickel;
	public static ItemStack gearPlatinum;
	public static ItemStack gearMithril;
	public static ItemStack gearElectrum;
	public static ItemStack gearInvar;
	public static ItemStack gearBronze;
	public static ItemStack gearSignalum;
	public static ItemStack gearLumium;
	public static ItemStack gearEnderium;

	public static ItemStack dustPyrotheum;
	public static ItemStack dustCryotheum;
	public static ItemStack dustAerotheum;
	public static ItemStack dustPetrotheum;
	public static ItemStack dustMana;

	public static ItemStack rodBlizz;
	public static ItemStack dustBlizz;
	public static ItemStack rodBlitz;
	public static ItemStack dustBlitz;
	public static ItemStack rodBasalz;
	public static ItemStack dustBasalz;

}
