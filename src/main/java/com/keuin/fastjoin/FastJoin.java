package com.keuin.fastjoin;

import net.fabricmc.api.ModInitializer;

public class FastJoin implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		System.out.println("FastJoin is enabled. If you have any issue, please report it at https://github.com/keuin/fastjoin");
	}
}
