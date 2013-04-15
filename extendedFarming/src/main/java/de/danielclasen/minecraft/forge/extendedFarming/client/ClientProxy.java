package de.danielclasen.minecraft.forge.extendedFarming.client;

import net.minecraftforge.client.MinecraftForgeClient;
import de.danielclasen.minecraft.forge.extendedFarming.CommonProxy;

public class ClientProxy extends CommonProxy {
       
        @Override
        public void registerRenderers() {
                MinecraftForgeClient.preloadTexture(ITEMS_PNG);
                MinecraftForgeClient.preloadTexture(BLOCK_PNG);
        }
       
}