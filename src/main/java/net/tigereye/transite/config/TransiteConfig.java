package net.tigereye.transite.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.tigereye.transite.Transite;

@Config(name = Transite.MODID)
public class TransiteConfig implements ConfigData {
    @ConfigEntry.Category("config")
    public int TRANSITION_FREQUENCY = 1200;
}
