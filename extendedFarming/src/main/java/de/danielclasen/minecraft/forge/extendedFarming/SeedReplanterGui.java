package de.danielclasen.minecraft.forge.extendedFarming;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.omg.IOP.TAG_INTERNET_IOP;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class SeedReplanterGui extends GuiContainer {

	/** The Y size of the inventory window in pixels. */
	protected int ySize = 222;
	protected int xSize = 256;

	protected EntityPlayer player;
	
	protected TileEntityTiny tileEntity;

	public SeedReplanterGui(InventoryPlayer inventoryPlayer, TileEntityTiny tileEntity) {
		// the container is instanciated and passed to the superclass for
		// handling
		super(new SeedReplanterContainer(inventoryPlayer, tileEntity));
		this.player = inventoryPlayer.player;

		ySize = 222;
		xSize = 256;

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Seed Replanter", 8, 6, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 97, 4210752);
		
		SeedReplanterContainer gc = (SeedReplanterContainer) this.inventorySlots;
		tileEntity = (TileEntityTiny) player.worldObj.getBlockTileEntity(gc.getTileEntity().xCoord, gc.getTileEntity().yCoord, gc.getTileEntity().zCoord);

		fontRenderer.drawString("Farm Area", 176, 8, 4210752);

		fontRenderer.drawString("Left: " + tileEntity.leftArea, 176, 16,
				4210752);

		fontRenderer.drawString("Right: " + tileEntity.rightArea, 176, 41,
				4210752);

		fontRenderer.drawString("Forward: " + tileEntity.forwardArea, 176, 66,
				4210752);

		fontRenderer.drawString("Backward: " + tileEntity.backwardArea, 176,
				91, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		// draw your Gui here, only thing you need to change is the path
		int texture = mc.renderEngine
				.getTexture("/Replanter.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x + 40, y + 27, 0, 0, xSize, ySize);
	}

	@Override
	public void initGui() {
		// TODO Auto-generated method stub
		super.initGui();
		
		int x = ((width - 256) / 2)+40;
		int y = ((height - 222) / 2)+27;
		
		
		
		controlList.add(new GuiButton(1, x+176, y+27, 10, 10, "+"));
		controlList.add(new GuiButton(2, x+186, y+27, 10, 10, "-"));
		
		controlList.add(new GuiButton(3, x+176, y+52, 10, 10, "+"));
		controlList.add(new GuiButton(4, x+186, y+52, 10, 10, "-"));
		
		controlList.add(new GuiButton(5, x+176, y+77, 10, 10, "+"));
		controlList.add(new GuiButton(6, x+186, y+77, 10, 10, "-"));
		
		controlList.add(new GuiButton(7, x+176, y+102, 10, 10, "+"));
		controlList.add(new GuiButton(8, x+186, y+102, 10, 10, "-"));

		GuiButton gb = new GuiButton(10, 150, 52, 20, 20, "view");

		// TODO: add iconButton
		controlList.add(new GuiButton(10, 150, 52, 20, 20, "view"));

	}

	protected void actionPerformed(GuiButton guibutton) {
		// id is the id you give your button

		SeedReplanterContainer gc = (SeedReplanterContainer) this.inventorySlots;
		TileEntityTiny tileEntity = gc.getTileEntity();

		switch (guibutton.id) {
		case 1:
			if(tileEntity.leftArea<25)
				tileEntity.leftArea++;
			else
				tileEntity.leftArea=25;
			break;
		case 2:
			if(tileEntity.leftArea>0)
				tileEntity.leftArea--;
			else
				tileEntity.leftArea=0;
			break;
		case 3:
			if(tileEntity.rightArea<25)
				tileEntity.rightArea++;
			else
				tileEntity.rightArea=25;
			break;
		case 4:
			if(tileEntity.rightArea>0)
				tileEntity.rightArea--;
			else
				tileEntity.rightArea=0;
			break;
		case 5:
			if(tileEntity.forwardArea<25)
				tileEntity.forwardArea++;
			else
				tileEntity.forwardArea=25;
			break;
		case 6:
			if(tileEntity.forwardArea>0)
				tileEntity.forwardArea--;
			else
				tileEntity.forwardArea=0;
			break;
		case 7:
			if(tileEntity.backwardArea<25)
				tileEntity.backwardArea++;
			else
				tileEntity.backwardArea=25;
			break;
		case 8:
			if(tileEntity.backwardArea>0)
				tileEntity.backwardArea--;
			else
				tileEntity.backwardArea=0;
			break;
		case 10:
			((EntityLiving) player).rotationYaw = -90;
			((EntityLiving) player).rotationPitch = 0;
			break;
		}

		Random random = new Random();
		int randomInt1 = random.nextInt();
		int randomInt2 = random.nextInt();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {

			outputStream.writeInt(tileEntity.leftArea);
			outputStream.writeInt(tileEntity.rightArea);
			outputStream.writeInt(tileEntity.forwardArea);
			outputStream.writeInt(tileEntity.backwardArea);

			outputStream.writeInt(tileEntity.xCoord);
			outputStream.writeInt(tileEntity.yCoord);
			outputStream.writeInt(tileEntity.zCoord);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EFFarmArea";
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		PacketDispatcher.sendPacketToServer(packet);
		PacketDispatcher.sendPacketToAllPlayers(packet);

		// tileEntity.worldObj.notifyBlockChange(tileEntity.xCoord,
		// tileEntity.yCoord, tileEntity.zCoord, tileEntity.worldObj
		// .getBlockId(tileEntity.xCoord, tileEntity.yCoord,
		// tileEntity.zCoord));

		// Packet code here
		// PacketDispatcher.sendPacketToServer(packet); //send packet
	}
}