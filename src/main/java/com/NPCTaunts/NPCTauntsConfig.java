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
		return 5;
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
			description = "Replaces all boss taunts with PKer taunts",
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
			keyName = "ShutUpHaitus",
			name = "Shut up Haitus",
			description = "Shuts Haitus up outside of death",
			position = 7
	)
	default boolean ShutHaitus()
	{
		return false;
	}

	@ConfigItem(
			keyName = "exlusivecustomtaunts",
			name = "Only custom taunts",
			description = "Disables the default taunts in the plugin and only uses custom taunts added in the config",
			position = 8
	)
	default boolean exlusivecustomtaunts()
	{
		return false;
	}
	@ConfigItem(
			keyName = "custombosstaunts",
			name = "Custom Boss Taunts",
			description = "Adds your own taunts to a boss. See readme on how to use",
			position = 9
	)
	default String custombosstaunts()
	{
		return "ExactBossname;Custom Taunt 1:Custom Taunt 2\n"+"ExactBossname2;Custom Taunt 3:Custom Taunt 4";
	}
	@ConfigItem(
			keyName = "customnpctaunts",
			name = "Custom NPC Taunts",
			description = "Adds your own taunts to a NPC. See readme on how to use",
			position = 10
	)
	default String customnpctaunts()
	{
		return "ExactNPCname;Custom Taunt 1:Custom Taunt 2\n"+"ExactNPCname2;Custom Taunt 3:Custom Taunt 4";
	}

	@ConfigItem(
			keyName = "texttospeech",
			name = "Natural Speech TTS",
			description = "Allows the taunts to trigger the Natural Speech TTS plugin",
			position = 11
	)
	default boolean texttospeech()
	{
		return false;
	}



}
