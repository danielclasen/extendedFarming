package de.danielclasen.minecraft.forge.extendedFarming;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    public void onPacketData(INetworkManager manager,
                    Packet250CustomPayload packet, Player player) {
           
            if (packet.channel.equals("GenericFarmArea")) {
                    handleArea(packet,player);
            }
    }
   
    private void handleArea(Packet250CustomPayload packet, Player player) {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
           
            int leftArea;
            int rightArea;
            int forwardArea;
            int backwardArea;
            
            int x;
            int y;
            int z;
           
            try {
            	leftArea = inputStream.readInt();
            	rightArea = inputStream.readInt();
            	forwardArea = inputStream.readInt();
            	backwardArea = inputStream.readInt();
                    
                    x = inputStream.readInt();
                    y = inputStream.readInt();
                    z = inputStream.readInt();
            } catch (IOException e) {
                    e.printStackTrace();
                    return;
            }
            
            TileEntityTiny te;
			if (player instanceof EntityPlayerMP){
				te = (TileEntityTiny) ((EntityPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				te.leftArea = leftArea;
				te.rightArea = rightArea;
				te.forwardArea = forwardArea;
				te.backwardArea = backwardArea;
				System.out.println("Server Packet recieved: "+leftArea + " " + rightArea+ " " + forwardArea+ " " + backwardArea);
				((EntityPlayerMP)player).worldObj.markBlockForUpdate(x, y, z);
			}else if (player instanceof EntityClientPlayerMP){
				te = (TileEntityTiny) ((EntityClientPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				te.leftArea = leftArea;
				te.rightArea = rightArea;
				te.forwardArea = forwardArea;
				te.backwardArea = backwardArea;
				System.out.println("Client Packet recieved: "+leftArea + " " + rightArea+ " " + forwardArea+ " " + backwardArea);
			}
            
            
    }

}