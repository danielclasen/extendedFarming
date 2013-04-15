package de.danielclasen.minecraft.forge.extendedFarming;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class SeedReplanterGuiHandler  implements IGuiHandler {
        //returns an instance of the Container you made earlier
        public Object getServerGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityTiny){
                        return new SeedReplanterContainer(player.inventory, (TileEntityTiny) tileEntity);
                }
                return null;
        }

        //returns an instance of the Gui you made earlier
        public Object getClientGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityTiny){
                        return new SeedReplanterGui(player.inventory, (TileEntityTiny) tileEntity);
                }
                return null;

        }
}
