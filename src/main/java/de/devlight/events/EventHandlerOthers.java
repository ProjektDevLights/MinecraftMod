package de.devlight.events;

import de.devlight.api.Api;
import de.devlight.utils.Color;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

@OnlyIn(Dist.CLIENT)
public class EventHandlerOthers {

    private final String[] splashes = {
            "Buy DevLight!",
            "Light your Minecraft up",
            "Soon in Haskell",
            "Buy TECKboards!",
            "TECKboard > Padlet",
            "Linux > Windows",
            "Also on Linux"
    };

    Screen currentScreen;
    @SubscribeEvent
    public void onGuiScreenOpened(GuiScreenEvent event){
        Screen screen = event.getGui();
        if(checkMenuForRunner(screen) && this.currentScreen != screen){
            this.currentScreen = screen;
            try {
                Api.turnOn().get();
                Api.setRunnerColor(new Color("#ff0403"), 100);
            } catch (InterruptedException | ExecutionException  exception) {}


            if(screen instanceof MainMenuScreen){
                try {
                    Field splashText = MainMenuScreen.class.getDeclaredFields()[5];
                    splashText.setAccessible(true);
                    int index = (int)Math.round(Math.random()*(splashes.length-1));
                    splashText.set(screen, splashes[index]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkMenuForRunner(Screen screen){
        return screen instanceof MainMenuScreen || screen instanceof MultiplayerScreen || screen instanceof WorldSelectionScreen;
    }
}
