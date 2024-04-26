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
		return 10;
	}
	@ConfigItem(
			keyName = "Chatmessage",
			name = "Chat message",
			description = "Adds taunts to chatbox",
			position = 2
	)
	default boolean dialogbox()
	{
		return true;
	}
	@ConfigItem(
			keyName = "BossTaunts",
			name = "Boss taunts",
			description = "Makes bosses tell you to sit as soon as you die",
			position = 3
	)
	default boolean Bosstaunts()
	{
		return true;
	}
	@ConfigItem(
			keyName = "LootTaunts",
			name = "Loot taunts",
			description = "Enables taunts when you don't receive",
			position = 4
	)
	default boolean Loottaunts()
	{
		return false;
	}
	@ConfigItem(
			keyName = "PKtaunts",
			name = "PK taunts",
			description = "Replaces all taunts with PK taunts",
			position = 5
	)
	default boolean pktaunts()
	{
		return false;
	}

	@ConfigItem(
			keyName = "Pleae",
			name = "Pleae",
			description = "Pleae",
			position = 6
	)
	default boolean Pleae()
	{
		return false;
	}

	@ConfigItem(
			keyName = "texttospeech",
			name = "Natural Speech TTS",
			description = "Allows the taunts to trigger the Natural Speech TTS plugin",
			position = 7
	)
	default boolean texttospeech()
	{
		return false;
	}
}
