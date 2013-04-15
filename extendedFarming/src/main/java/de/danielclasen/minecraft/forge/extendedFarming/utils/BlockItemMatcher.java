package de.danielclasen.minecraft.forge.extendedFarming.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockItemMatcher{
	
	public int itemId;
	public int blockId;
	public boolean canSeed = false;
	
	
	public BlockItemMatcher(Block block, ItemStack item, boolean canSeed){
		this.itemId = item.getItem().itemID;
		this.blockId = block.blockID;
		this.canSeed = canSeed;
	}
	
	public BlockItemMatcher(int blockId, int itemId, boolean canSeed){
		this.itemId  = itemId;
		this.blockId = blockId;
		this.canSeed = canSeed;
	}
	

}
