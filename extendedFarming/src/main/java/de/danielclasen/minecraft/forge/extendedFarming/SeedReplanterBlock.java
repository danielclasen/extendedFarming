package de.danielclasen.minecraft.forge.extendedFarming;


import java.util.Random;

import de.danielclasen.minecraft.forge.extendedFarming.utils.BlockItemMatcherRegistry;

import net.minecraft.item.Item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;

public class SeedReplanterBlock extends BlockContainer {

	public SeedReplanterBlock(int id, int texture, Material material) {
		super(id, texture, material);

		// TileEntityChest tec = new TileEntityChest();
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		// code to open gui explained later

		player.openGui(ExtendedFarming.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public void onBlockAdded(World par1World, int x, int y, int z) {
		// TODO Auto-generated method stub
		super.onBlockAdded(par1World, x, y, z);
		par1World.scheduleBlockUpdate(x, y, z, this.blockID, 4);
	}

	@Override
	public void updateTick(World par1World, int x, int y, int z,
			Random par5Random) {
		// TODO Auto-generated method stub
		super.updateTick(par1World, x, y, z, par5Random);

		if (par1World.isBlockGettingPowered(x, y, z)) {
			lookForFields(par1World, x, y, z);
		} else {
			// System.out.println("Lampe AUS! "+x+" "+y+" "+z);
		}

		par1World.scheduleBlockUpdate(x, y, z, this.blockID, 100);
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side) {
		// TODO Auto-generated method stub
		return true;
	}

	protected void lookForFields(World world, int x, int y, int z) {

		// System.out.println("x:"+x+" y:"+y+" z:"+z);
		//
		// System.out.println("MATERIAL: "+world.getBlockMaterial(x, y,
		// z).toString());
		// System.out.println("ID: "+world.getBlockId(x, y, z));
		// System.out.println("META: "+world.getBlockMetadata(x, y, z));

		int xCenter = x;
		int yCenter = y;
		int zCenter = z;

		TileEntityTiny te = (TileEntityTiny) world.getBlockTileEntity(x, y, z);

		te.updateContainingBlockInfo();
		te.updateEntity();

		y++; // shift 1 up, because yCenter is the y-coord of the planterblock

		for (int xI = (te.backwardArea * -1); xI <= te.forwardArea; xI++) {
			for (int zI = (te.leftArea * -1); zI <= te.rightArea; zI++) {

				if ((world.getBlockId(x + xI, y, z + zI) == 2 || world
						.getBlockId(x + xI, y, z + zI) == 3)
						&& world.getBlockId(x + xI, y + 1, z + zI) == 0) { 
					// found a grass or dirt block and above is air
					if (hoeFarmland(world, te, x + xI, y, z + zI)) {
						// hoe it ;-)

						if (!seedFarmland(world, te, x + xI, y + 1, z + zI))
							break;
					}
				} else if (world.getBlockId(x + xI, y, z + zI) == 60
						&& world.getBlockId(x + xI, y + 1, z + zI) == 0) {
					// found farmland and above is air
					if (!seedFarmland(world, te, x + xI, y + 1, z + zI))
						break;
				}

			}
		}

	}

	protected boolean hoeFarmland(World world, TileEntityTiny te, int x, int y,
			int z) {
		if (te.getSizeInventory() > 0) {
			for (int i = 0; i < te.getSizeInventory(); i++) {
				ItemStack currItem = te.getStackInSlot(i);
				if (currItem != null
						&& currItem.stackSize > 0
						&& (currItem.getItem().equals(Item.hoeDiamond)
								|| currItem.getItem().equals(Item.hoeGold)
								|| currItem.getItem().equals(Item.hoeSteel)
								|| currItem.getItem().equals(Item.hoeStone) 
								|| currItem.getItem().equals(Item.hoeWood))
						&& currItem.getItemDamage() < currItem.getItem()
								.getMaxDamage()) {
					world.setBlockAndMetadataWithNotify(x, y, z, 60, 0);
					if (currItem.getItemDamage() == currItem.getItem()
							.getMaxDamage() - 1) {
						te.setInventorySlotContents(i, null);
					} else {
						ItemStack editCurrItem = new ItemStack(
								currItem.getItem(), 1,
								currItem.getItemDamage() + 1);
						te.setInventorySlotContents(i, editCurrItem);
					}
					world.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
					return true;
				}

			}
		}
		return false;
	}

	protected boolean seedFarmland(World world, TileEntityTiny te, int x,
			int y, int z) {
		if (te.getSizeInventory() > 0) {
			for (int i = 0; i < te.getSizeInventory(); i++) {
				ItemStack currItem = te.getStackInSlot(i);
				
				try {
					if (currItem != null && ExtendedFarming.blockItemMatcherRegistry.getByItemId(currItem.getItem().itemID).canSeed
							&& currItem.stackSize > 0) {
						world.setBlockAndMetadataWithNotify(x, y, z, ExtendedFarming.blockItemMatcherRegistry.getByItemId(currItem.getItem().itemID).blockId, 0);
						te.decrStackSize(i, 1);
						world.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
						return true;
					}
				} catch (NullPointerException e) {
					// TODO: handle exception
				}


			}
		}
		return false;

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int i, int j) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, i, j);
	}

	private void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();

		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

		if (!(tile_entity instanceof IInventory)) {
			return;
		}

		IInventory inventory = (IInventory) tile_entity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.6F + 0.1F;
				float ry = rand.nextFloat() * 0.6F + 0.1F;
				float rz = rand.nextFloat() * 0.6F + 0.1F;

				EntityItem entity_item = new EntityItem(world, x + rx, y + ry,
						z + rz, new ItemStack(item.itemID, item.stackSize,
								item.getItemDamage()));

				if (item.hasTagCompound()) {
					entity_item.getEntityItem().setTagCompound(
							(NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.1F;

				entity_item.motionX = rand.nextGaussian() * factor;
				entity_item.motionY = rand.nextGaussian() * factor + 0.2F;
				entity_item.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entity_item);
				item.stackSize = 0;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTiny();
	}

}
