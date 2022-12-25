package com.geekazodium.lootrunutil.events;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.geekazodium.lootrunutil.LootrunUtil.logger;
import static com.geekazodium.lootrunutil.core.Chests.handleChestOpen;

public class ClientEvents {

    private final HashMap<GuiScreen,Integer> openedGuis = new HashMap<>();

    @SubscribeEvent
    public void onGuiTick(GuiScreenEvent.DrawScreenEvent event){
        GuiScreen gui = event.getGui();
        if(!openedGuis.containsKey(gui)){
            openedGuis.put(gui,20);
        }else{
            openedGuis.put(gui,Math.max(openedGuis.get(gui),10));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        List<GuiScreen> toRemove = new ArrayList<>();
        openedGuis.forEach((guiScreen, integer) -> {
            //logger.info(integer);
            openedGuis.replace(guiScreen,integer-1);
            if(integer<=0){
                toRemove.add(guiScreen);
            }
            if(integer == 15){
                if(guiScreen == null)return;
                if(!(guiScreen instanceof GuiChest)) return;
                handleChestOpen((GuiChest) guiScreen);
            }
        });
        toRemove.forEach(openedGuis::remove);
    }

    @SubscribeEvent
    public void onInteract(GuiScreenEvent.ActionPerformedEvent event){
        if(openedGuis.get(event.getGui())>=15){
            event.setCanceled(true);
        }
    }
}
