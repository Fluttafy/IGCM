package com.creativemd.igcm.packets;

import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMConfig;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.client.gui.SubGuiConfigSegement;
import com.creativemd.igcm.jei.JEIHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BranchInformationPacket extends CreativeCorePacket {
	
	public ConfigBranch branch;
	
	public BranchInformationPacket() {
		
	}
	
	public BranchInformationPacket(ConfigBranch branch) {
		this.branch = branch;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		branch.onPacketSend(side);
		
		writeString(buf, branch.getPath());
		;
		
		NBTTagCompound nbt = new NBTTagCompound();
		branch.save(nbt);
		writeNBT(buf, nbt);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		//System.out.println("Receiving new packet");
		branch = (ConfigBranch) ConfigTab.getSegmentByPath(readString(buf));
		
		branch.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide());
		branch.load(readNBT(buf));
	}
	
	public void receiveUpdate(Side side) {
		
		branch.onRecieveFromPre(side);
		branch.onRecieveFrom(side);
		branch.onRecieveFromPost(side);
		
		if (side == Side.CLIENT && JEIHandler.isActive)
			branch.updateJEI();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeClient(EntityPlayer player) {
		receiveUpdate(Side.CLIENT);
		
		if (player != null && player.openContainer instanceof ContainerSub && ((ContainerSub) player.openContainer).gui.getTopLayer() instanceof SubGuiConfigSegement) {
			SubGuiConfigSegement gui = (SubGuiConfigSegement) ((ContainerSub) player.openContainer).gui.getTopLayer();
			if (gui.element == branch) {
				double scrolled = ((GuiScrollBox) gui.get("scrollbox")).scrolled.aimed();
				gui.createSegmentControls(true);
				GuiScrollBox box = (GuiScrollBox) gui.get("scrollbox");
				box.scrolled.setStart(scrolled);
				if (box.scrolled.aimed() > box.maxScroll)
					box.scrolled.setStart(box.maxScroll);
			}
			//InGameConfigManager.openBranchGui(player, branch);
		}
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		
		if (IGCM.gui.checkPermission(player.getServer(), player)) {
			receiveUpdate(Side.SERVER);
			
			IGCM.sendUpdatePacket(branch);
			IGCMConfig.saveConfig();
			
			branch.onUpdateSendToClient(player);
		} else
			IGCM.sendUpdatePacket(branch, player);
	}
	
}