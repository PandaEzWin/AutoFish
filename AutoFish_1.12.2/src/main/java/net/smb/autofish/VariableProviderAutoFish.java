package net.smb.autofish;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.client.Minecraft.getMinecraft;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderAutoFish extends VariableCache {

   public static final Minecraft mc = getMinecraft();
   public static boolean autoFishEnabled;

   public Object getVariable(String variableName) {
      return this.getCachedValue(variableName);
   }

   public void updateVariables(boolean clock) {
      this.storeVariable("AUTOFISH", true);
   }

   @SubscribeEvent
   public void onPlaySoundSource(PlaySoundSourceEvent event) {
      if (mc.player == null) {
         return;
      }

      if (mc.player.fishEntity == null) {
         return;
      }

      ResourceLocation id = event.getSound().getSoundLocation();

      if (!id.getResourceDomain().equals("minecraft")) {
         return;
      }

      if (!id.getResourcePath().equals("entity.bobber.splash")) {
         return;
      }

      if (autoFishEnabled) {
         if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemFishingRod) {
            new Thread(() -> {
               try {
                  mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                  Thread.sleep(1000);
                  mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }).start();
         }
      }
   }

   public void onInit() {
      ScriptContext.MAIN.getCore().registerVariableProvider(this);

      try {
         FMLCommonHandler.instance().bus().register(this);
         MinecraftForge.EVENT_BUS.register(this);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
