package com.whatcanicraft;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WhatCanICraftPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(WhatCanICraftPlugin.class);
		RuneLite.main(args);
	}
}