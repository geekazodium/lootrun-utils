package com.geekazodium.lootrunutil;

import com.geekazodium.lootrunutil.events.ClientEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = LootrunUtil.MODID, name = LootrunUtil.NAME, version = LootrunUtil.VERSION)
public class LootrunUtil
{
    public static final String MODID = "lootrun-utils";
    public static final String NAME = "Lootrun Utils";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("loading:"+MODID+"-"+VERSION);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
}
