package cofh.thermalfoundation.item;

import static cofh.lib.util.helpers.ItemHelper.ShapedRecipe;

import java.util.Locale;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.item.ItemArmorAdv;
import cofh.core.item.tool.ItemAxeAdv;
import cofh.core.item.tool.ItemBowAdv;
import cofh.core.item.tool.ItemFishingRodAdv;
import cofh.core.item.tool.ItemHoeAdv;
import cofh.core.item.tool.ItemPickaxeAdv;
import cofh.core.item.tool.ItemShearsAdv;
import cofh.core.item.tool.ItemShovelAdv;
import cofh.core.item.tool.ItemSickleAdv;
import cofh.core.item.tool.ItemSwordAdv;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.core.TFProps;

public enum Equipment {

	// @formatter:off
	/* Name, Level, Uses, Speed, Damage, Ench, Dura, Absorption, Toughness */
	Copper(      1,  175,  4.0F,  0.75F,    6,    6, new int[] { 1, 3, 3, 1 },  4.0F),
	Tin(         1,  200,  4.5F,   1.0F,    7,    8, new int[] { 1, 4, 3, 1 },  4.5F),
	Silver(      2,  200,  6.0F,   1.5F,   20,   11, new int[] { 2, 4, 4, 1 },  6.0F),
	Lead(        1,  150,  5.0F,   1.0F,    9,   15, new int[] { 2, 5, 4, 3 },  5.0F) {

		@Override
		protected final void createArmor() {

			AttributeModifier knockbackBonus;
			AttributeModifier movementBonus;

			knockbackBonus = new AttributeModifier("lead weight bonus", .25, 0);
			movementBonus = new AttributeModifier("lead weight bonus", -.15, 2);
			itemHelmet = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.HEAD);
			itemHelmet.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemHelmet.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("lead weight bonus", .25, 0);
			movementBonus = new AttributeModifier("lead weight bonus", -.15, 2);
			itemPlate = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.CHEST);
			itemPlate.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemPlate.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("lead weight bonus", .25, 0);
			movementBonus = new AttributeModifier("lead weight bonus", -.15, 2);
			itemLegs = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.LEGS);
			itemLegs.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemLegs.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("lead weight bonus", .25, 0);
			movementBonus = new AttributeModifier("lead weight bonus", -.15, 2);
			itemBoots = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.FEET);
			itemBoots.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemBoots.putAttribute("generic.movementSpeed", movementBonus);
		}
	},
	Nickel(      2,  300,  6.5F,   2.5F,   18,   15, new int[] { 2, 5, 5, 2 },  6.5F),
	Electrum(    0,  100, 14.0F,   0.5F,   30,    8, new int[] { 2, 4, 4, 2 }, 14.0F),
	Invar(       2,  450,  7.0F,   3.0F,   16,   21, new int[] { 2, 7, 5, 2 },  7.0F),
	Bronze(      2,  500,  6.0F,   2.0F,   15,   18, new int[] { 3, 6, 6, 2 },  6.0F),
	Platinum(    4, 1700,  9.0F,   4.0F,    9,   40, new int[] { 3, 8, 6, 3 },  9.0F) {

		@Override
		protected final void createArmor() {

			AttributeModifier knockbackBonus;
			AttributeModifier movementBonus;

			knockbackBonus = new AttributeModifier("platinum weight bonus", .20, 0);
			movementBonus = new AttributeModifier("platinum weight bonus", -.08, 2);
			itemHelmet = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.HEAD);
			itemHelmet.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemHelmet.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("platinum weight bonus", .25, 0);
			movementBonus = new AttributeModifier("platinum weight bonus", -.08, 2);
			itemPlate = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.CHEST);
			itemPlate.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemPlate.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("platinum weight bonus", .25, 0);
			movementBonus = new AttributeModifier("platinum weight bonus", -.08, 2);
			itemLegs = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.LEGS);
			itemLegs.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemLegs.putAttribute("generic.movementSpeed", movementBonus);
			knockbackBonus = new AttributeModifier("platinum weight bonus", .20, 0);
			movementBonus = new AttributeModifier("platinum weight bonus", -.08, 2);
			itemBoots = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.FEET);
			itemBoots.putAttribute("generic.knockbackResistance", knockbackBonus);
			itemBoots.putAttribute("generic.movementSpeed", movementBonus);
		}
	},
	;
	// @formatter:on

	public final ToolMaterial TOOL_MATERIAL;
	public final ArmorMaterial ARMOR_MATERIAL;

	private final String ingot;
	private final float arrowSpeed = 2.0F;
	private float arrowDamage = 1.0F;
	private int luckModifier = 0;
	private int speedModifier = 0;

	public boolean enableArmor = true;
	public boolean[] enableTools = new boolean[9];

	public ItemArmorAdv itemHelmet;
	public ItemArmorAdv itemPlate;
	public ItemArmorAdv itemLegs;
	public ItemArmorAdv itemBoots;

	public ItemSwordAdv itemSword;
	public ItemShovelAdv itemShovel;
	public ItemPickaxeAdv itemPickaxe;
	public ItemAxeAdv itemAxe;
	public ItemHoeAdv itemHoe;
	public ItemShearsAdv itemShears;
	public ItemFishingRodAdv itemFishingRod;
	public ItemSickleAdv itemSickle;
	public ItemBowAdv itemBow;

	public ItemStack armorHelmet;
	public ItemStack armorPlate;
	public ItemStack armorLegs;
	public ItemStack armorBoots;

	public ItemStack toolSword;
	public ItemStack toolShovel;
	public ItemStack toolPickaxe;
	public ItemStack toolAxe;
	public ItemStack toolHoe;
	public ItemStack toolShears;
	public ItemStack toolFishingRod;
	public ItemStack toolSickle;
	public ItemStack toolBow;

	private Equipment(int level, int uses, float speed, float damage, int enchant, int durability, int[] absorb, float toughness) {

		TOOL_MATERIAL = EnumHelper.addToolMaterial("TF:" + name().toUpperCase(Locale.US), level, uses, speed, damage, enchant);
		ARMOR_MATERIAL = EnumHelper.addArmorMaterial("TF:" + name().toUpperCase(Locale.US),"TF:" + name().toUpperCase(Locale.US), durability, absorb, enchant, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_generic")), toughness);
		ingot = "ingot" + name();

		/* Fishing Rod */
		luckModifier = level / 2;
		speedModifier = (int) (speed / 5);

		/* Bow */
		// arrowSpeed = 2.0F + speed / 8F;
		arrowDamage = 1.0F + damage / 8F;
	}

	protected void createArmor() {

		itemHelmet = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.HEAD);
		itemPlate = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.CHEST);
		itemLegs = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.LEGS);
		itemBoots = new ItemArmorAdv(ARMOR_MATERIAL, EntityEquipmentSlot.FEET);
	}

	protected void createTools() {

		itemSword = new ItemSwordAdv(TOOL_MATERIAL);
		itemShovel = new ItemShovelAdv(TOOL_MATERIAL);
		itemPickaxe = new ItemPickaxeAdv(TOOL_MATERIAL);
		itemAxe = new ItemAxeAdv(TOOL_MATERIAL);
		itemHoe = new ItemHoeAdv(TOOL_MATERIAL);
		itemShears = new ItemShearsAdv(TOOL_MATERIAL);
		itemFishingRod = new ItemFishingRodAdv(TOOL_MATERIAL);
		itemSickle = new ItemSickleAdv(TOOL_MATERIAL);
		itemBow = new ItemBowAdv(TOOL_MATERIAL);
	}

	protected void preInitv() {

		final String NAME = name();
		final String ARMOR = "thermalfoundation:armor" + NAME;
		final String TOOL = "thermalfoundation:tool" + NAME;

		String category = "Equipment." + NAME;
		enableArmor = ThermalFoundation.config.get(category, "Armor", true);
		enableArmor &= !TFProps.disableAllArmor;

		category += ".Tools";
		enableTools[0] = ThermalFoundation.config.get(category, "Sword", true);
		enableTools[1] = ThermalFoundation.config.get(category, "Shovel", true);
		enableTools[2] = ThermalFoundation.config.get(category, "Pickaxe", true);
		enableTools[3] = ThermalFoundation.config.get(category, "Axe", true);
		enableTools[4] = ThermalFoundation.config.get(category, "Hoe", true);
		enableTools[5] = ThermalFoundation.config.get(category, "Shears", true);
		enableTools[6] = ThermalFoundation.config.get(category, "FishingRod", true);
		enableTools[7] = ThermalFoundation.config.get(category, "Sickle", true);
		enableTools[8] = ThermalFoundation.config.get(category, "Bow", true);

		for (int i = 0; i < enableTools.length; i++) {
			enableTools[i] &= !TFProps.disableAllTools;
		}
		

		createArmor();
		itemHelmet.setRepairIngot(ingot).setShowInCreative(enableArmor | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabArmor).setUnlocalizedName(ARMOR + "Helmet");
		itemPlate.setRepairIngot(ingot).setShowInCreative(enableArmor | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabArmor).setUnlocalizedName(ARMOR + "Plate");
		itemLegs.setRepairIngot(ingot).setShowInCreative(enableArmor | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabArmor).setUnlocalizedName(ARMOR + "Legs");;
		itemBoots.setRepairIngot(ingot).setShowInCreative(enableArmor | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabArmor).setUnlocalizedName(ARMOR + "Boots");

		createTools();
		itemSword.setRepairIngot(ingot).setShowInCreative(enableTools[0] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Sword");
		itemShovel.setRepairIngot(ingot).setShowInCreative(enableTools[1] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Shovel");
		itemPickaxe.setRepairIngot(ingot).setShowInCreative(enableTools[2] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Pickaxe");
		itemAxe.setRepairIngot(ingot).setShowInCreative(enableTools[3] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Axe");
		itemHoe.setRepairIngot(ingot).setShowInCreative(enableTools[4] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Hoe");
		itemShears.setRepairIngot(ingot).setShowInCreative(enableTools[5] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Shears");
		itemFishingRod.setRepairIngot(ingot).setShowInCreative(enableTools[6] | TFProps.showDisabledEquipment).setLuckModifier(luckModifier).setSpeedModifier(speedModifier).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "FishingRod");
		itemSickle.setRepairIngot(ingot).setShowInCreative(enableTools[7] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Sickle");

		itemBow.setRepairIngot(ingot).setShowInCreative(enableTools[8] | TFProps.showDisabledEquipment).setArrowSpeed(arrowSpeed).setArrowDamage(arrowDamage).setCreativeTab(ThermalFoundation.tabTools).setUnlocalizedName(TOOL + "Bow");

		GameRegistry.register(itemHelmet);
		GameRegistry.register(itemPlate);
		GameRegistry.register(itemLegs);
		GameRegistry.register(itemBoots);

		GameRegistry.register(itemSword);
		GameRegistry.register(itemShovel);
		GameRegistry.register(itemPickaxe);
		GameRegistry.register(itemAxe);
		GameRegistry.register(itemHoe);
		GameRegistry.register(itemShears);
		GameRegistry.register(itemFishingRod);
		GameRegistry.register(itemSickle);
		GameRegistry.register(itemBow);

	}

	protected void initializev() {

		// Armor
		itemHelmet.registertexture();
		itemPlate.registertexture();
		itemLegs.registertexture();
		itemBoots.registertexture();
		
		armorHelmet = new ItemStack(itemHelmet);
		armorPlate = new ItemStack(itemPlate);
		armorLegs = new ItemStack(itemLegs);
		armorBoots = new ItemStack(itemBoots);
		// Tools
		itemSword.registertexture();;
		itemShovel.registertexture();;
		itemPickaxe.registertexture();;
		itemAxe.registertexture();;
		itemHoe.registertexture();;
		itemShears.registertexture();;
		itemFishingRod.registertexture();;
		itemSickle.registertexture();;
		itemBow.registertexture();;
		
		toolSword = new ItemStack(itemSword);
		toolShovel = new ItemStack(itemShovel);
		toolPickaxe = new ItemStack(itemPickaxe);
		toolAxe = new ItemStack(itemAxe);
		toolHoe = new ItemStack(itemHoe);
		toolShears = new ItemStack(itemShears);
		toolFishingRod = new ItemStack(itemFishingRod);
		toolSickle = new ItemStack(itemSickle);
		toolBow = new ItemStack(itemBow);
	}

	protected void postInitv() {

		// Armor
		if (enableArmor) {
			GameRegistry.addRecipe(ShapedRecipe(armorHelmet, new Object[] { "III", "I I", 'I', ingot }));
			GameRegistry.addRecipe(ShapedRecipe(armorPlate, new Object[] { "I I", "III", "III", 'I', ingot }));
			GameRegistry.addRecipe(ShapedRecipe(armorLegs, new Object[] { "III", "I I", "I I", 'I', ingot }));
			GameRegistry.addRecipe(ShapedRecipe(armorBoots, new Object[] { "I I", "I I", 'I', ingot }));
		}

		// Tools
		if (enableTools[0]) {
			GameRegistry.addRecipe(ShapedRecipe(toolSword, new Object[] { "I", "I", "S", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[1]) {
			GameRegistry.addRecipe(ShapedRecipe(toolShovel, new Object[] { "I", "S", "S", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[2]) {
			GameRegistry.addRecipe(ShapedRecipe(toolPickaxe, new Object[] { "III", " S ", " S ", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[3]) {
			GameRegistry.addRecipe(ShapedRecipe(toolAxe, new Object[] { "II", "IS", " S", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[4]) {
			GameRegistry.addRecipe(ShapedRecipe(toolHoe, new Object[] { "II", " S", " S", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[5]) {
			GameRegistry.addRecipe(ShapedRecipe(toolShears, new Object[] { " I", "I ", 'I', ingot }));
		}
		if (enableTools[6]) {
			GameRegistry.addRecipe(ShapedRecipe(toolFishingRod, new Object[] { "  I", " I#", "S #", 'I', ingot, 'S', "stickWood", '#', Items.STRING }));
		}
		if (enableTools[7]) {
			GameRegistry.addRecipe(ShapedRecipe(toolSickle, new Object[] { " I ", "  I", "SI ", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[8]) {
			GameRegistry.addRecipe(ShapedRecipe(toolBow, new Object[] { " I#", "S #", " I#", 'I', ingot, 'S', "stickWood", '#', Items.STRING }));
		}
	}

	public static void preInit() {

		VanillaEquipment.preInit();
		for (Equipment e : values()) {
			e.preInitv();
		}
	}

	public static void initialize() {

		VanillaEquipment.initialize();
		for (Equipment e : values()) {
			e.initializev();
		}
	}

	public static void postInit() {

		VanillaEquipment.postInit();
		for (Equipment e : values()) {
			e.postInitv();
		}
	}

}
