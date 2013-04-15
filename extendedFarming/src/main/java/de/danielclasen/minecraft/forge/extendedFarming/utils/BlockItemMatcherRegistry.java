package de.danielclasen.minecraft.forge.extendedFarming.utils;

import java.util.HashMap;

public final class BlockItemMatcherRegistry{
	
	
	private HashMap<Integer, BlockItemMatcher> registryBlock = new HashMap<Integer, BlockItemMatcher>();
	private HashMap<Integer, BlockItemMatcher> registryItem = new HashMap<Integer, BlockItemMatcher>();
	
	
	public BlockItemMatcherRegistry(){
		
	}
	
	
	public void add(BlockItemMatcher bim){
		this.registryBlock.put(bim.blockId, bim);
		this.registryItem.put(bim.itemId, bim);
	}
	
	public BlockItemMatcher getByBlockId(int blockId) throws NullPointerException{
		if (this.registryBlock.containsKey(blockId))
			return this.registryBlock.get(blockId);
		else
			throw new NullPointerException("Block not mapped");
	}
	
	public BlockItemMatcher getByItemId(int itemId) throws NullPointerException{
		if (this.registryItem.containsKey(itemId))
			return this.registryItem.get(itemId);
		else
			throw new NullPointerException("Item not mapped");
	}
	
	
	
	
}
