package com.NPCTaunts;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.util.Random;
import java.util.Timer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.Actor;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.raids.RaidsPlugin;

@Slf4j
@PluginDescriptor(
	name = "NPCTaunts"
)
public class NPCTauntsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private NPCTauntsConfig config;

	@Inject
	private final Random r = new Random();
	private boolean Died;
	private Instant ForgetTime;
	private boolean[] Commented = new boolean[50];

	private Actor lastOpponent;
	private Actor Killer;
	private static final int ZULRAH_SPAWN_REGION_ID = 9007;
	private static final int ZULRAH_REGION_ID = 9008;
	static final int FIGHT_CAVE_REGION = 9551;
	private boolean Jad;
	static final int INFERNO_REGION = 9043;
	private boolean Zuk;
	private static final List<Integer> ZulRegionIDs = Arrays.asList(ZULRAH_SPAWN_REGION_ID,ZULRAH_REGION_ID);

	private boolean prejaddeath;
	private boolean jaddeath;
	private boolean prezukdeath;
	private boolean zukdeath;
	private boolean zulrahdeath;
	private boolean prehunleffminion;
	private boolean prehunleffdeath;
	private boolean hunleffdeath;
	private boolean chambersdeath;


	private final List<String> HansPhrases = Arrays.asList("You'll get them next time Tiger!","I've seen everyone end up here again, so don't worry");
	private final List<String> HaitusPhrases = Arrays.asList("RIP Bozo","Sit idiot","Get some better gear maybe you'll live longer","Obtain good pleb","Just buy your cape like I did","Im living in your game rent free","AH! You're just fresh out of tutorial island", "Been caught lackin","Dying is not very stylish is it?","Pauper Poor Primitive Peasant","Good thing they don't measure fashionscape skill levels for you","If only your list of deaths was a small as your staff","Buy an adamant longer sword","I fart in your general direction","If only your staff was as mighty as mine");
	private final List<String> KhaledPhrases = Arrays.asList("I call her chandelier","Let's go golfing","Call me a tennis ball.","We ain't stoppin, let's go shoppin.","That's when you know you're the greatest: when you're the greatest, and people still put odds against you." , "I tell myself every day I love my Jacuzzi, I love my marble floors, I love my high ceilings");
	private final List<String> ZulPhrases = Arrays.asList("Snake?? SNAKE??? SNAAAAAAAAAAAAAAAAAAKEEE");


	@Override
	protected void startUp() throws Exception
	{
		log.info("NPC Taunts started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("NPC Taunts stopped!");
	}


	private int getRegionId() {
		Player player = client.getLocalPlayer();
		if (player == null) {
			return -1;
		}
		return WorldPoint.fromLocalInstance(client, player.getLocalLocation()).getRegionID();
	}
	private String Phrase(List<String> p)
	{
		int rand = r.nextInt(p.size());
		while (p.get(rand).contains("{Enemyname}") && Killer == null)
		{
			rand = r.nextInt(p.size())
		}
		String s = p.get(rand);
		if (s.contains("{Enemyname}"))
		{
			s.replace("{Enemyname}", Killer.GetName())
		}
		return s;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (Died == false){ return;}
		if (Instant.now().compareTo(ForgetTime) >= 0)
		{
			Died = false;
			lastOpponent = null;
		}
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged event)
	{
		if (event.getSource() != client.getLocalPlayer())
		{
			return;
		}

		Actor opponent = event.getTarget();

		if (opponent == null)
		{
			return;
		}
		lastOpponent = opponent;
	}



	@Subscribe
	public void onActorDeath(ActorDeath event) {
		Actor actor = event.getActor();
		if (actor instanceof Player) {
			Player player = (Player) actor;

			if (player == getLocalPlayer())
			{
				Died = true;
				prejaddeath=false;
				jaddeath=false;
				prezukdeath=false;
				zukdeath=false;
				zulrahdeath=false;
				prehunleffminion=false;
				prehunleffdeath=false;
				hunleffdeath=false;
				chambersdeath=false;
				//Zulrah
				final int regionId = getRegionId();
				if (ZulRegionIDs.contains(regionId))
				{
					zulrahdeath = true;
				}

				//Fightcaves
				if (regionId == FIGHT_CAVE_REGION)
				{
					if (Jad)
					{
						jaddeath = true;
					}
					else
					{
						prejaddeath = true;
					}
				}
				//Inferno
				if (regionId == INFERNO_REGION)
				{
					if (Zuk)
					{
						zukdeath = true;
					}
					else
					{
						prezukdeath = true;
					}
				}

				//gauntlet maze
				final boolean GauntletMaze = Client.getVarbitValue(9178);
				if (GauntletMaze)
				{
					//if last opponent rat spider bat
					//prehunleffminion = true;
					//else
					prehunleffdeath = true;
				}
				//gauntlet boss
				final boolean Hunleff = Client.getVarbitValue(9177);
				if (Hunleff)
				{
					hunleffdeath = true;
				}
				//In chambers
				if (isInRaidChambers())
				{
					chambersdeath = true;
				}

				//reset all npcs comments
				int i = 0;
				while (i<Commented.length)
				{
					Commented[i] = false;
					i++;
				}
				Killer = lastOpponent;
				lastOpponent = null;
				final Duration forgetduration = Duration.ofMinutes(config.ForgetTimeDelay())
				ForgetTime = Instant.now().plus(forgetduration)
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (event.getNpc().getName() == null) { return;}

		if (event.getNpc().getName().equals("TzTok-Jad"))
		{
			Jad  = true;
		}
		if (event.getNpc().getName().equals("TzKal-Zuk"))
		{
			Zuk = true;
		}
		if (Died == false)
		{
			if (event.getNpc().getName().equals("Hatius Cosaintus"))
			{
				String Khaledtaunt = Phrase(KhaledPhrases));
				event.getNpc().setOverheadCycle(200);
				event.getNpc().setOverheadText(Khaledtaunt);
			}
			return;
		}

		if (event.getNpc().getName().equals("Hans"))
		{
			String Hanstaunt = Phrase(HansPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(Hanstaunt);
		}
		if (event.getNpc().getName().equals("Hatius Cosaintus"))
		{
			String Haitustaunt =Phrase(HaitusPhrases)
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(Haitustaunt);
		}
		//priffspawn
		//edgespawn
		//infernospawn
		//faladorspawn
		//feroxspawn


		if (event.getNpc().getName().equals("Priestess Zul-Gwenwynig"))
		{
			String Zultaunt = Phrase(ZulPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(Zultaunt);
		}
		//brynn
		//fightcavestzaar
		//infernotzaar
		//olm

	}

	@Subscribe
	public void onNpcDepawned(NpcSpawned event)
	{
		if (event.getNpc().getName() == null) { return;}
		if (event.getNpc().getName().equals("TzTok-Jad"))
		{
			Jad= false;
		}
		if (event.getNpc().getName().equals("TzKal-Zuk"))
		{
			Zuk = false;
		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
