package cofh.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cofh.core.CoFHProps;
import cofh.core.network.PacketCore;
import cofh.core.render.ItemRenderRegistry;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.core.util.fluid.BucketHandler;
import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.util.helpers.ServerHelper;
import cofh.lib.util.helpers.StringHelper;

public class FMLEventHandler {

	public static FMLEventHandler instance = new FMLEventHandler();

	public static void initialize() {

		MinecraftForge.EVENT_BUS.register(instance);
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {

		EntityPlayer player = event.player;
		if (ServerHelper.isMultiPlayerServer() && CoFHProps.enableOpSecureAccess && CoFHProps.enableOpSecureAccessWarning) {
			player.addChatMessage(new TextComponentString(StringHelper.YELLOW + "[CoFH] ").appendSibling(new TextComponentTranslation("chat.cofh.secureNotice")));
		}
		PacketCore.sendConfigSyncPacketToClient(event.player);
		handleIdMappingEvent(null);
	}

	@EventHandler
	public void handleIdMappingEvent(FMLModIdMappingEvent event) {

		BucketHandler.refreshMap();
		FurnaceFuelHandler.refreshMap();
		ItemRenderRegistry.refreshMap();
		OreDictionaryArbiter.initialize();
	}

}
