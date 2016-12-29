package cofh.thermalfoundation.item;

import static cofh.lib.util.helpers.ItemHelper.ShapedRecipe;

import java.util.Locale;

import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.item.tool.ItemBowAdv;
import cofh.core.item.tool.ItemFishingRodAdv;
import cofh.core.item.tool.ItemShearsAdv;
import cofh.core.item.tool.ItemSickleAdv;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.core.TFProps;

public enum VanillaEquipment {

	// @formatter:off
    Wood(ToolMaterial.WOOD, "plankWood") {

    	@Override
    	protected void createTools(String TOOL) {

    		itemShears = new ItemShearsAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Shears");
    		itemFishingRod = Items.FISHING_ROD;
    		itemSickle = new ItemSickleAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Bow");
    		itemBow = Items.BOW;
    	}
    },
    Stone(ToolMaterial.STONE, "cobblestone"),
    Iron {

    	@Override
    	protected void createTools(String TOOL) {

    		itemShears =  Items.SHEARS;
    		itemFishingRod = new ItemFishingRodAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".FishingRod");
    		itemSickle = new ItemSickleAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Sickle");
    		itemBow = new ItemBowAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Bow");
    	}
    },
    Diamond(ToolMaterial.DIAMOND, "gemDiamond"),
    Gold
	;
	// @formatter:on

	public final ToolMaterial TOOL_MATERIAL;

	private final String ingot;
	private final float arrowSpeed = 1.0F;
	private float arrowDamage = 1.0F;
	private int luckModifier = 0;
	private int speedModifier = 0;

	public boolean[] enableTools = new boolean[4];

	public ItemShears itemShears;
	public ItemFishingRod itemFishingRod;
	public ItemSickleAdv itemSickle;
	public ItemBow itemBow;

	public ItemStack toolShears;
	public ItemStack toolFishingRod;
	public ItemStack toolSickle;
	public ItemStack toolBow;

	private VanillaEquipment() {

		this(null, null);
	}

	private VanillaEquipment(ToolMaterial material, String ingot) {

		TOOL_MATERIAL = material == null ? ToolMaterial.valueOf(name().toUpperCase(Locale.US)) : material;
		this.ingot = ingot == null ? "ingot" + name() : ingot;

		/* Fishing Rod */
		luckModifier = TOOL_MATERIAL.getHarvestLevel() / 2;
		speedModifier = (int) (TOOL_MATERIAL.getEfficiencyOnProperMaterial() / 5);

		/* Bow */
		// arrowSpeed = 2.0F + speed / 8F;
		arrowDamage = 1.0F + TOOL_MATERIAL.getDamageVsEntity() / 8F;
	}

	protected void createTools(String TOOL) {

		itemShears = new ItemShearsAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Shears");
		itemFishingRod = new ItemFishingRodAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".FishingRod");
		itemSickle = new ItemSickleAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Sickle");
		itemBow = new ItemBowAdv(TOOL_MATERIAL, "thermalfoundation", TOOL + ".Bow");
	}

	protected void preInitv() {

		final String NAME = name();
		final String TOOL = "tool." + NAME;

		String category = "Equipment." + NAME;

		category += ".Tools";
		if (this != Iron) {
			enableTools[0] = ThermalFoundation.config.get(category, "Shears", true);
		}
		if (this != Wood) {
			enableTools[1] = ThermalFoundation.config.get(category, "FishingRod", true);
		}
		enableTools[2] = ThermalFoundation.config.get(category, "Sickle", true);
		if (this != Wood) {
			enableTools[3] = ThermalFoundation.config.get(category, "Bow", true);
		}

		for (int i = 0; i < enableTools.length; i++) {
			enableTools[i] &= !TFProps.disableAllTools;
		}

		createTools(TOOL);

		if (itemShears instanceof ItemShearsAdv) {
			ItemShearsAdv itemShears = (ItemShearsAdv) this.itemShears;
			itemShears.setRepairIngot(ingot).setShowInCreative(enableTools[0] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools);
			itemShears.preInit();
		}

		if (itemFishingRod instanceof ItemFishingRodAdv) {
			ItemFishingRodAdv itemFishingRod = (ItemFishingRodAdv) this.itemFishingRod;
			itemFishingRod.setRepairIngot(ingot).setShowInCreative(enableTools[1] | TFProps.showDisabledEquipment).setLuckModifier(luckModifier).setSpeedModifier(speedModifier).setCreativeTab(ThermalFoundation.tabTools);
			itemFishingRod.preInit();
		}

		itemSickle.setRepairIngot(ingot).setShowInCreative(enableTools[2] | TFProps.showDisabledEquipment).setCreativeTab(ThermalFoundation.tabTools);
		itemSickle.preInit();

		if (itemBow instanceof ItemBowAdv) {
			ItemBowAdv itemBow = (ItemBowAdv) this.itemBow;
			itemBow.setRepairIngot(ingot).setShowInCreative(enableTools[3] | TFProps.showDisabledEquipment).setArrowSpeed(arrowSpeed).setArrowDamage(arrowDamage).setCreativeTab(ThermalFoundation.tabTools);
			itemBow.preInit();
		}
	}

	protected void initializev() {
		// Tools
		toolShears = new ItemStack(itemShears);
		toolFishingRod = new ItemStack(itemFishingRod);
		toolSickle = new ItemStack(itemSickle);
		toolBow = new ItemStack(itemBow);
		
		if (itemShears instanceof ItemShearsAdv) {
			ItemShearsAdv itemShears = (ItemShearsAdv) this.itemShears;
			itemShears.initialize();
		}
		if (itemFishingRod instanceof ItemFishingRodAdv) {
			ItemFishingRodAdv itemFishingRod = (ItemFishingRodAdv) this.itemFishingRod;
			itemFishingRod.initialize();
		}
		itemSickle.initialize();
		if (itemBow instanceof ItemBowAdv) {
			ItemBowAdv itemBow = (ItemBowAdv) this.itemBow;
			itemBow.initialize();
		}
	}

	protected void postInitv() {

		if (itemShears instanceof ItemShearsAdv) {
			ItemShearsAdv itemShears = (ItemShearsAdv) this.itemShears;
			itemShears.postInit();
		}
		if (itemFishingRod instanceof ItemFishingRodAdv) {
			ItemFishingRodAdv itemFishingRod = (ItemFishingRodAdv) this.itemFishingRod;
			itemFishingRod.postInit();
		}
		itemSickle.postInit();
		if (itemBow instanceof ItemBowAdv) {
			ItemBowAdv itemBow = (ItemBowAdv) this.itemBow;
			itemBow.postInit();
		}
		// Tools
		if (enableTools[0]) {
			GameRegistry.addRecipe(ShapedRecipe(toolShears, new Object[] { " I", "I ", 'I', ingot }));
		}
		if (enableTools[1]) {
			GameRegistry.addRecipe(ShapedRecipe(toolFishingRod, new Object[] { "  I", " I#", "S #", 'I', ingot, 'S', "stickWood", '#', Items.STRING }));
		}
		if (enableTools[2]) {
			GameRegistry.addRecipe(ShapedRecipe(toolSickle, new Object[] { " I ", "  I", "SI ", 'I', ingot, 'S', "stickWood" }));
		}
		if (enableTools[3]) {
			GameRegistry.addRecipe(ShapedRecipe(toolBow, new Object[] { " I#", "S #", " I#", 'I', ingot, 'S', "stickWood", '#', Items.STRING }));
		}
	}

	public static void preInit() {

		for (VanillaEquipment e : values()) {
			e.preInitv();
		}
	}

	public static void initialize() {

		for (VanillaEquipment e : values()) {
			e.initializev();
		}
	}

	public static void postInit() {

		for (VanillaEquipment e : values()) {
			e.postInitv();
		}
	}

}
