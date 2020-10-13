package com.keuin.fastjoin;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FastJoin implements ModInitializer {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("FastJoin is enabled. If you have any issue, please report it at https://github.com/keuin/fastjoin");
	}
}
