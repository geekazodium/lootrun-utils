package com.geekazodium.lootrunutil.events;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.geekazodium.lootrunutil.core.Chests.*;

public class ClientEvents {

    private final HashMap<GuiScreen,Integer> openedGuis = new HashMap<>();

    @SubscribeEvent
    public void onGuiTick(GuiScreenEvent.DrawScreenEvent event){
        GuiScreen gui = event.getGui();
        if(!(gui instanceof GuiChest)) return;
        if(!openedGuis.containsKey(gui)){
            openedGuis.put(gui, chestOpenDelayLimit +10);
        }else{
            openedGuis.put(gui,Math.max(openedGuis.get(gui),9));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        List<GuiScreen> toRemove = new ArrayList<>();
        openedGuis.forEach((guiScreen, integer) -> {
            openedGuis.replace(guiScreen,integer-1);
            if(integer<=0){
                toRemove.add(guiScreen);
            }
            if(!isChestEmpty(guiScreen)&&integer>10){
                integer = 10;
                openedGuis.replace(guiScreen,10);
            }
            if(integer == 10){
                if(guiScreen == null)return;
                if(!(guiScreen instanceof GuiChest)) return;
                handleChestOpen((GuiChest) guiScreen);
                openedGuis.put(guiScreen,9);
            }
        });
        toRemove.forEach(openedGuis::remove);
    }

    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent event){
        Integer integer = openedGuis.get(event.getGui());
        if(integer ==null)return;
        if(integer>=10){
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onKeyInput(GuiScreenEvent.KeyboardInputEvent event){
        Integer integer = openedGuis.get(event.getGui());
        if(integer == null)return;
        if(integer>=10){
            event.setCanceled(true);
        }
    }
}
