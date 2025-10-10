package com.terraformersmc.modmenu.api;

import net.minecraft.client.gui.screens.Screen;

@SuppressWarnings("unused")
@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen> {
    S create(Screen parent);
}