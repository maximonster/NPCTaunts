package com.NPCTaunts;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("NPCTaunts")
public interface NPCTauntsConfig extends Config
{
	@ConfigItem(
			keyName = "ForgetTime",
			name = "Forget time",
			description = "How long should everyone remember your death. Time specified in minutes",
			position = 1
	)
	default int ForgetTimeDelay()
	{
		return 30;
	}
}
