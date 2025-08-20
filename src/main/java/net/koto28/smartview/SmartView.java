package net.koto28.smartview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod(modid = SmartView.MODID, version = SmartView.VERSION)
public class SmartView {
    public static final String MODID = "smartview";
    public static final String VERSION = "1.0";

    // Key binding definition
    @SideOnly(Side.CLIENT)
    public static KeyBinding viewThirdBack;
    public static KeyBinding viewThirdFront;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialization code
        System.out.println("SmartView is initializing!");

        // Register key binding on client side
        if (event.getSide().isClient()) {
            initKeyBindings();
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SideOnly(Side.CLIENT)
    private void initKeyBindings() {
        // Create key binding (default key: G) with custom category
        viewThirdBack = new KeyBinding("key.smartview.third.back", Keyboard.KEY_G, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(viewThirdBack);
        viewThirdFront = new KeyBinding("key.smartview.third.front", Keyboard.KEY_H, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(viewThirdFront);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (viewThirdBack.isPressed()) {
            // Process when key is pressed
            changeView(0);
        } else if (viewThirdFront.isPressed()) {
            // Process when H key is pressed
            changeView(1);
        }
    }

    @SideOnly(Side.CLIENT)
    private void changeView(int viewType) {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;

        if (viewType == 0) {
            // Change to third-person back view
            settings.thirdPersonView = 1;
            if (mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText("Third Person Back View"));
            }
        } else if (viewType == 1) {
            // Change to third-person front view
            settings.thirdPersonView = 2;
            if (mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText("Third Person Front View"));
            }
        }
    }
}
