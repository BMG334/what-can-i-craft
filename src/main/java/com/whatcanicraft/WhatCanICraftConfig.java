package com.whatcanicraft;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("whatcanicraft")
public interface WhatCanICraftConfig extends Config
{
	@ConfigItem(
		keyName = "includeInventory",
		name = "Include inventory",
		description = "Also count items in your inventory when looking for recipes"
	)
	default boolean includeInventory()
	{
		return false;
	}
}
