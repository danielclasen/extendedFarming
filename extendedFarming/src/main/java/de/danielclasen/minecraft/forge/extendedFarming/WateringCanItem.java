package de.danielclasen.minecraft.forge.extendedFarming;


import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class WateringCanItem extends Item {

    public WateringCanItem(int id) {
		super(id);
		// Constructor Configuration
        setCreativeTab(CreativeTabs.tabTools);
        //setUnlocalizedName("Watering Can");
        setItemName("Watering Can");
        setMaxDamage(512);
        setIconIndex(3);
        setMaxStackSize(1);
	}

   
    public String getTextureFile() {
            return CommonProxy.ITEMS_PNG;
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack,
    		EntityPlayer par2EntityPlayer, World par3World, int targetX, int targetY,
    		int targetZ, int par7, float par8, float par9, float par10) {
    	// TODO Auto-generated method stub
    	
    	System.out.println("onItemUse triggered");
    	
    	System.out.println(targetX);
    	System.out.println(targetY);
    	System.out.println(targetZ);
    	
    	int targetBlockId = par3World.getBlockId(targetX, targetY, targetZ);
    	int targetBlockMeta = par3World.getBlockMetadata(targetX, targetY, targetZ);
    	
    	System.out.println("targetBlockId: "+targetBlockId);
    	System.out.println("targetBlockMeta: "+targetBlockMeta);
    	
    	if (targetBlockId!=0 && (targetBlockId==60 && targetBlockMeta<7)){
    		//farmland clicked
    		if (waterCanBlock(par3World, targetX, targetY, targetZ)){
    			par1ItemStack.damageItem(1, par2EntityPlayer);
    			return true;
    		}
    			
    	}else if (targetBlockId!=0 && targetBlockId==8){
    		//refill watering can
    		par1ItemStack.damageItem(par1ItemStack.getItemDamage()*(-1), par2EntityPlayer);
    	}
    	
    	try {
        	if (targetBlockId!=0 && ExtendedFarming.blockItemMatcherRegistry.getByBlockId(targetBlockId).canSeed){
        		//Crops clicked, so get the block underneath
        		if (waterCanBlock(par3World, targetX, targetY-1, targetZ)){
        			par1ItemStack.damageItem(1, par2EntityPlayer);
        			return true;
        		}
        	}
		} catch (NullPointerException e) {
		}

    	
    	return false;
    }
    
    
    public boolean waterCanBlock(World world, int x, int y, int z){
    	
    	int targetBlockId = world.getBlockId(x, y, z);
    	int targetBlockMeta = world.getBlockMetadata(x, y, z);
    	
    	if (targetBlockId!=0 && (targetBlockId==60 && targetBlockMeta<7)){
    		world.setBlockAndMetadataWithUpdate(x, y, z, 60, 7, true);
    		return true;
    	}
    	
		return false;    	
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
    		EntityPlayer par3EntityPlayer) {
    	// TODO Auto-generated method stub
    	System.out.println("onItemRightClick triggered");
    	
    	
        float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
        boolean var11 = par1ItemStack.getItemDamage() != 0;
        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, var11);

        if (var12 == null)
        {
            return par1ItemStack;
        }
        else
        {
            FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, var12);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return par1ItemStack;
            }

            if (event.getResult() == Event.Result.ALLOW)
            {
                if (par3EntityPlayer.capabilities.isCreativeMode)
                {
                    return par1ItemStack;
                }
                
                par1ItemStack.damageItem(par1ItemStack.getItemDamage()*(-1), par3EntityPlayer);
                return par1ItemStack;
            }

            if (var12.typeOfHit == EnumMovingObjectType.TILE)
            {
                int var13 = var12.blockX;
                int var14 = var12.blockY;
                int var15 = var12.blockZ;

                if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
                {
                    return par1ItemStack;
                }

                if (par1ItemStack.getItemDamage() != 0)
                {
                    if (!par3EntityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, par1ItemStack))
                    {
                        return par1ItemStack;
                    }

                    if (par2World.getBlockMaterial(var13, var14, var15) == Material.water && par2World.getBlockMetadata(var13, var14, var15) == 0)
                    {
                        //par2World.setBlockWithNotify(var13, var14, var15, 0);

                        if (par3EntityPlayer.capabilities.isCreativeMode)
                        {
                            return par1ItemStack;
                        }
                        par1ItemStack.damageItem(par1ItemStack.getItemDamage()*(-1), par3EntityPlayer);

                        return par1ItemStack;
                    }

                }

            }

            return par1ItemStack;
        }
    	
    	

    }
    
}