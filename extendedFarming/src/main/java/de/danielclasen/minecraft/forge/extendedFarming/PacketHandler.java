package de.danielclasen.minecraft.forge.extendedFarming;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		System.out.println("Received Packet on Channel: " + packet.channel);

		if (packet.channel.equals("EFFarmArea")) {
			handleArea(packet, player);
		} else if (packet.channel.equals("EFParticle")) {
			handleParticle(packet, player);
		} else if (packet.channel.equals("EFSoundEffect")) {
			handleSoundEffect(packet, player);
		}

	}

	private void handleArea(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

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
		if (player instanceof EntityPlayerMP) {
			te = (TileEntityTiny) ((EntityPlayerMP) player).worldObj
					.getBlockTileEntity(x, y, z);
			te.leftArea = leftArea;
			te.rightArea = rightArea;
			te.forwardArea = forwardArea;
			te.backwardArea = backwardArea;
			System.out.println("Server Packet recieved: " + leftArea + " "
					+ rightArea + " " + forwardArea + " " + backwardArea);
			((EntityPlayerMP) player).worldObj.markBlockForUpdate(x, y, z);
		} else if (player instanceof EntityClientPlayerMP) {
			te = (TileEntityTiny) ((EntityClientPlayerMP) player).worldObj
					.getBlockTileEntity(x, y, z);
			te.leftArea = leftArea;
			te.rightArea = rightArea;
			te.forwardArea = forwardArea;
			te.backwardArea = backwardArea;
			System.out.println("Client Packet recieved: " + leftArea + " "
					+ rightArea + " " + forwardArea + " " + backwardArea);
		}

	}

	private void handleParticle(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		String particleType;
		double posX;
		double posY;
		double posZ;
		double velX;
		double velY;
		double velZ;
		try {
			particleType = inputStream.readUTF();
			posX = inputStream.readDouble();
			posY = inputStream.readDouble();
			posZ = inputStream.readDouble();
			velX = inputStream.readDouble();
			velY = inputStream.readDouble();
			velZ = inputStream.readDouble();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Received particleType: " + particleType);
		System.out.println("Received x: " + posX);
		System.out.println("Received y: " + posY);
		System.out.println("Received z: " + posZ);

		System.out.println("Received velX: " + velX);
		System.out.println("Received velY: " + velY);
		System.out.println("Received velZ: " + velZ);

		World worldObj = FMLClientHandler.instance().getClient().theWorld;

		 if (particleType.equals("rain")) {
		
		 FMLClientHandler.instance().getClient().effectRenderer
		 .addEffect(new EntityRainFX(worldObj, posX, posY, posZ));
		
		 } else {

		worldObj.spawnParticle(particleType, posX, posY, posZ, velX, velY, velZ);

		 }

	}

	private void handleSoundEffect(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		double posX;
		double posY;
		double posZ;
		String soundEffect;

		try {

			posX = inputStream.readDouble();
			posY = inputStream.readDouble();
			posZ = inputStream.readDouble();
			soundEffect = inputStream.readUTF();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Received soundEffect: " + soundEffect);
		System.out.println("Received x: " + posX);
		System.out.println("Received y: " + posY);
		System.out.println("Received z: " + posZ);

		World worldObj = FMLClientHandler.instance().getClient().theWorld;
		
		worldObj.playSoundEffect(posX, posY, posZ, soundEffect, 1.0F,
				worldObj.rand.nextFloat() * 0.1F + 0.9F);

	}

}