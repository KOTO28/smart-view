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
    public static KeyBinding FrontView;
    public static KeyBinding BackView;

    // Key state tracking
    @SideOnly(Side.CLIENT)
    private boolean wasFrontViewPressed = false;
    @SideOnly(Side.CLIENT)
    private boolean wasBackViewPressed = false;

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
        FrontView = new KeyBinding("key.smartview.view.front", Keyboard.KEY_G, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(FrontView);
        BackView = new KeyBinding("key.smartview.view.back", Keyboard.KEY_H, "key.categories.smartview");
        ClientRegistry.registerKeyBinding(BackView);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        boolean isFrontViewPressed = FrontView.isKeyDown();
        boolean isBackViewPressed = BackView.isKeyDown();

        if (isFrontViewPressed != wasFrontViewPressed) {
            if (isFrontViewPressed) {
                changeView(1);
            } else {
                changeView(0);
            }
        }
        if (isBackViewPressed != wasBackViewPressed) {
            if (isBackViewPressed) {
                changeView(2);
            } else {
                changeView(0);
            }
        }

        // Update previous state
        wasFrontViewPressed = isFrontViewPressed;
        wasBackViewPressed = isBackViewPressed;
    }

    /**
     * changeView
     * 
     * @param viewType The type of view to switch to.
     *                 <ul>
     *                 <li>0 - First-person view</li>
     *                 <li>1 - Third-person front view</li>
     *                 <li>2 - Third-person back view</li>
     *                 </ul>
     */
    @SideOnly(Side.CLIENT)
    private void changeView(int viewType) {
        if (viewType < 0 || viewType > 2) {
            throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;
        settings.thirdPersonView = viewType;
    }
}
