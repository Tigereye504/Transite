package net.tigereye.transite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.tigereye.transite.register.TransiteModelPredicates;

@Environment(EnvType.CLIENT)
public class TransiteClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TransiteModelPredicates.register();
    }
}
