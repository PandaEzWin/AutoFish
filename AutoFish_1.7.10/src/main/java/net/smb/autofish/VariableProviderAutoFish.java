package net.smb.autofish;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.VariableCache;
import net.eq2online.macros.scripting.api.APIVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.common.MinecraftForge;
import net.smb.autofish.ModuleInfo;

import static net.minecraft.client.Minecraft.getMinecraft;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderAutoFish extends VariableCache {

    public static final Minecraft mc = getMinecraft();
    public static boolean autoFishEnabled;

    @Override
    public void updateVariables(boolean clock) {
        this.setCachedVariable("AUTOFISH", true);
    }

    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    @SubscribeEvent
    public void onPlaySoundSource(PlaySoundSourceEvent event) {
        if (mc.thePlayer == null) {
            return;
        }

        if (mc.thePlayer.fishEntity == null) {
            return;
        }

        ResourceLocation id = event.sound.getPositionedSoundLocation();

        if (!id.getResourceDomain().equals("minecraft")) {
            return;
        }

        if (!id.getResourcePath().equals("random.splash")) {
            return;
        }

        if (autoFishEnabled) {
            if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFishingRod) {
                new Thread(() -> {
                    try {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                        Thread.sleep(1000);
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    @Override
    public void onInit() {
        ScriptContext.MAIN.getCore().registerVariableProvider(this);

        try {
            FMLCommonHandler.instance().bus().register(this);
            MinecraftForge.EVENT_BUS.register(this);
        } catch (Exception e) {
            //empty catch
        }
    }
}
