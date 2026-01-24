package net.koto28.smartview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.CameraType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

@Mod(SmartView.MODID)
@Mod.EventBusSubscriber(modid = SmartView.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SmartView {
    public static final String MODID = "smartview";
    public static final String VERSION = "1.1";

    // Key binding definition
    @OnlyIn(Dist.CLIENT)
    public static KeyMapping BackView;
    @OnlyIn(Dist.CLIENT)
    public static KeyMapping FrontView;

    // Key state tracking
    @OnlyIn(Dist.CLIENT)
    private static boolean wasBackViewPressed = false;
    @OnlyIn(Dist.CLIENT)
    private static boolean wasFrontViewPressed = false;

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void onClientSetup(RegisterKeyMappingsEvent event) {
            // Create key bindings
            BackView = new KeyMapping("key.smartview.view.back", GLFW.GLFW_KEY_UNKNOWN, "key.categories.smartview");
            event.register(BackView);
            FrontView = new KeyMapping("key.smartview.view.front", GLFW.GLFW_KEY_UNKNOWN, "key.categories.smartview");
            event.register(FrontView);

            MinecraftForge.EVENT_BUS.register(InputHandler.class);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class InputHandler {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                handleInput();
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void handleInput() {
            if (BackView == null || FrontView == null) {
                return;
            }

            boolean isBackViewPressed = BackView.isDown();
            boolean isFrontViewPressed = FrontView.isDown();

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
        @OnlyIn(Dist.CLIENT)
        private static void changeView(int viewType) {
            if (viewType < 0 || viewType > 2) {
                throw new IllegalArgumentException("Invalid view type: " + viewType);
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.options != null) {
                CameraType[] cameraTypes = CameraType.values();
                // System.out.println("Available camera types:");
                // for (int i = 0; i < cameraTypes.length; i++) {
                // System.out.println(i + " - " + cameraTypes[i]);
                // }
                // System.out.println("Setting view to: " + viewType);
                if (viewType < cameraTypes.length) {
                    mc.options.setCameraType(cameraTypes[viewType]);
                }
            }
        }
    }
}
