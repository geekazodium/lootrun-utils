package com.geekazodium.lootrunutil.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Field;

import static com.geekazodium.lootrunutil.LootrunUtil.logger;

public class Chests {
    public static int chestOpenDelayLimit = 50; // TODO: 12/25/2022 Make setting to allow adjusting this delay
    public static boolean logChestData = false; // TODO: 12/25/2022 add setting for this
    public static void handleChestOpen(GuiChest inventory) {
        IInventory chestInventory = getChestInventory(inventory);
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(chestInventory == null){
            return;
        }
        int unidentifiedCounter = getUnidentifiedCount(chestInventory);
        player.sendStatusMessage(new TextComponentString(String.format(
                "there were %d unidentified items in %s",
                unidentifiedCounter,
                chestInventory.getDisplayName().getUnformattedText()
        )),false);
    }

    public static int getUnidentifiedCount(IInventory inventory) {
        int unidentifiedCounter = 0;
        if(logChestData)logger.info("----chest data start----");
        for (int i = 0;i<inventory.getSizeInventory();i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if(logChestData)logger.info(itemStack.getDisplayName());
            if(!itemStack.getItem().equals(Items.STONE_SHOVEL))continue;
            if(!itemStack.getDisplayName().contains("Unidentified"))continue;
            unidentifiedCounter+=1;
        }
        if(logChestData)logger.info("----chest data end----");
        return unidentifiedCounter;
    }

    private static IInventory getChestInventory(GuiChest inventory) {
        try {
            Field[] fields = inventory.getClass().getDeclaredFields();
            for (Field f:fields) {
                if(f.isAccessible()){
                    continue;
                }
                f.setAccessible(true);
                Object o = f.get(inventory);
                f.setAccessible(false);
                if(o instanceof ContainerLocalMenu){
                    return (IInventory) o;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean isChestEmpty(GuiScreen chest){
        if(!(chest instanceof GuiChest))return true;
        IInventory inventory = getChestInventory((GuiChest) chest);
        if(inventory == null){
            return true;
        }
        for (int i = 0;i<inventory.getSizeInventory();i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if(itemStack.getCount() >0){
                return false;
            }
        }
        return true;
    }

    public static boolean isLootChest(GuiScreen chest){
        if(!(chest instanceof GuiChest))return false;
        IInventory inventory = getChestInventory((GuiChest) chest);
        if(inventory == null){
            return false;
        }
        return inventory.getDisplayName().getUnformattedText().contains("Loot Chest");
    }
}
