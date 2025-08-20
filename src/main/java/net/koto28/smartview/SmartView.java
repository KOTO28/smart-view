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

    // Key state tracking
    @SideOnly(Side.CLIENT)
    private boolean wasThirdBackPressed = false;
    @SideOnly(Side.CLIENT)
    private boolean wasThirdFrontPressed = false;

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
        // Create key binding
        viewThirdBack = new KeyBinding("key.smartview.third.back", Keyboard.KEY_G, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(viewThirdBack);
        viewThirdFront = new KeyBinding("key.smartview.third.front", Keyboard.KEY_H, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(viewThirdFront);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        boolean isThirdBackPressed = viewThirdBack.isKeyDown();
        boolean isThirdFrontPressed = viewThirdFront.isKeyDown();

        if (isThirdBackPressed != wasThirdBackPressed) {
            if (isThirdBackPressed) {
                changeView(1);
            } else {
                changeView(0);
            }
        }
        if (isThirdFrontPressed != wasThirdFrontPressed) {
            if (isThirdFrontPressed) {
                changeView(2);
            } else {
                changeView(0);
            }
        }

        // Update previous state
        wasThirdBackPressed = isThirdBackPressed;
        wasThirdFrontPressed = isThirdFrontPressed;
    }

    /**
     * changeView
     * 
     * @param viewType The type of view to switch to.
     *                 <ul>
     *                 <li>0 - First-person view</li>
     *                 <li>1 - Third-person back view</li>
     *                 <li>2 - Third-person front view</li>
     *                 </ul>
     */
    @SideOnly(Side.CLIENT)
    private void changeView(int viewType) {
        if (viewType < 0 || viewType > 2) {
            throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;
        // Change to third-person back view
        settings.thirdPersonView = viewType;
    }
}
