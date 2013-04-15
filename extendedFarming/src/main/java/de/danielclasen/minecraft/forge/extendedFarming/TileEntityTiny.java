package de.danielclasen.minecraft.forge.extendedFarming;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTiny extends TileEntity implements IInventory {

	private ItemStack[] inv;
	public int leftArea;
	public int rightArea;
	public int forwardArea;
	public int backwardArea;

	public TileEntityTiny() {
		inv = new ItemStack[96];
		leftArea = 0;
		rightArea = 0;
		forwardArea = 0;
		backwardArea = 0;
	}

	@Override
	public boolean canUpdate() {
		// TODO Auto-generated method stub
		return true;
	}

	public int getSizeInventory() {
		return inv.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		// TODO Auto-generated method stub
		super.onDataPacket(net, pkt);

		NBTTagCompound tag = pkt.customParam1;
		loadInfoFromNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		Packet132TileEntityData packet = (Packet132TileEntityData) super
				.getDescriptionPacket();
		NBTTagCompound tag = packet != null ? packet.customParam1
				: new NBTTagCompound();

		addInfoToNBT(tag);

		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}

	public void addInfoToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("leftArea", this.leftArea);
		tagCompound.setInteger("rightArea", this.rightArea);
		tagCompound.setInteger("forwardArea", this.forwardArea);
		tagCompound.setInteger("backwardArea", this.backwardArea);
	}

	public void loadInfoFromNBT(NBTTagCompound tagCompound) {
		leftArea = tagCompound.getInteger("leftArea");
		rightArea = tagCompound.getInteger("rightArea");
		forwardArea = tagCompound.getInteger("forwardArea");
		backwardArea = tagCompound.getInteger("backwardArea");

		System.out.println("tagCompound.getInteger(leftArea): "
				+ tagCompound.getInteger("leftArea"));
		System.out.println(leftArea);

		System.out.println("tagCompound.getInteger(rightArea): "
				+ tagCompound.getInteger("rightArea"));
		System.out.println(rightArea);

		System.out.println("tagCompound.getInteger(forwardArea): "
				+ tagCompound.getInteger("forwardArea"));
		System.out.println(forwardArea);

		System.out.println("tagCompound.getInteger(backwardArea): "
				+ tagCompound.getInteger("backwardArea"));
		System.out.println(backwardArea);

	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		loadInfoFromNBT(tagCompound);

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);

		addInfoToNBT(tagCompound);

	}

	public String getInvName() {
		return "de.danielclasen.minecraft.forge.extendedFarming.tileentitytiny";
	}
}