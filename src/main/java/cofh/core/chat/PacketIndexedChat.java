package cofh.core.chat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.CoFHCore;
import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;

public class PacketIndexedChat extends PacketCoFHBase {

	public static void initialize() {

		PacketHandler.instance.registerPacket(PacketIndexedChat.class);
	}

	public PacketIndexedChat() {

	}

	public PacketIndexedChat(ITextComponent chat) {

	}

	public PacketIndexedChat(ITextComponent chat, int index) {

		addString(ChatHelper.toJSON(chat));
		addInt(index);
	}

	@Override
	public void handlePacket(EntityPlayer player, boolean isServer) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer player) {

		String string = getString();
		int offset = getInt();
		CoFHCore.proxy.addIndexedChatMessage(ChatHelper.fromJSON(string), offset);
	}

}
