package net.koto28.smartview;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmartView implements ClientModInitializer {
    public static final String MOD_ID = "smartview";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // Key binding definition
    public static KeyBinding BackView;
    public static KeyBinding FrontView;

    // Key state tracking
    private static boolean wasBackViewPressed = false;
    private static boolean wasFrontViewPressed = false;

    @Override
    public void onInitializeClient() {
        // LOGGER.info("Hello Fabric world!");

        // Key binding
        BackView = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.smartview.view.back",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "key.categories.smartview"));
        FrontView = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.smartview.view.front",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "key.categories.smartview"));

        ClientTickEvents.END_CLIENT_TICK.register(SmartView::onClientEndTick);
    }

    private static void onClientEndTick(MinecraftClient client) {
        handleInput();
    }

    private static void handleInput() {
        if (BackView == null || FrontView == null) {
            return;
        }

        boolean isBackViewPressed = BackView.isPressed();
        boolean isFrontViewPressed = FrontView.isPressed();

        if (isBackViewPressed != wasBackViewPressed) {
            if (isBackViewPressed) {
                changeView(1);
            } else {
                changeView(0);
            }
        }
        if (isFrontViewPressed != wasFrontViewPressed) {
            if (isFrontViewPressed) {
                changeView(2);
            } else {
                changeView(0);
            }
        }

        // Update previous state
        wasBackViewPressed = isBackViewPressed;
        wasFrontViewPressed = isFrontViewPressed;
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
    private static void changeView(int viewType) {
        if (viewType < 0 || viewType > 2) {
            throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            Perspective[] cameraTypes = Perspective.values();
            // System.out.println("Available camera types:");
            // for (int i = 0; i < cameraTypes.length; i++) {
            // System.out.println(i + " - " + cameraTypes[i]);
            // }
            // System.out.println("Setting view to: " + viewType);
            if (viewType < cameraTypes.length) {
                mc.options.setPerspective(cameraTypes[viewType]);
            }
        }
    }
}
