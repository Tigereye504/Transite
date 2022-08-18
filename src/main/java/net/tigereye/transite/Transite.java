package net.tigereye.transite;

//import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
//import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.tigereye.transite.config.TransiteConfig;
import net.tigereye.transite.register.TransiteItems;
import net.tigereye.transite.register.TransiteOreFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transite implements ModInitializer {
	public static final String MODID = "transite";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static TransiteConfig config;



	@Override
	public void onInitialize() {
		AutoConfig.register(TransiteConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TransiteConfig.class).getConfig();

		TransiteItems.register();
		TransiteOreFeatures.register();
	}
}
