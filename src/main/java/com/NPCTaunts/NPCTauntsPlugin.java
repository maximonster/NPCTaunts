package com.NPCTaunts;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


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
	private Random r = new Random();
	private boolean Died;
	private Instant ForgetTime;
	private Actor lastOpponent;
	private Actor Killer;
	private int lastdamagetaken;
	private int finaldamagetaken;
	private static final int ZULRAH_SPAWN_REGION_ID = 9007;
	private static final int ZULRAH_REGION_ID = 9008;
	static final int FIGHT_CAVE_REGION = 9551;
	private boolean Jad;
	static final int INFERNO_REGION = 9043;
	private boolean Zuk;
	private static final List<Integer> ZulRegionIDs = Arrays.asList(ZULRAH_SPAWN_REGION_ID,ZULRAH_REGION_ID);
	private static final List<String> Gauntletminions = Arrays.asList("Crystalline Bat","Crystalline Rat","Crystalline Spider","Corrupted Rat","Corrupted Spider","Corrupted Bat");

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
	private final List<String> PriffPhrases = Arrays.asList();
	private final List<String> BankerPhrases = Arrays.asList();
	private final List<String> SquirePhrases = Arrays.asList();
	private final List<String> FeroxPhrases = Arrays.asList();
	private final List<String> WedgePhrases = Arrays.asList();
	private final List<String> ElisePhrases = Arrays.asList();
	private final List<String> TowncrierPhrases = Arrays.asList();
	private final List<String> PreJadPhrases = Arrays.asList();
	private final List<String> JadPhrases = Arrays.asList();
	private final List<String> PreZukPhrases = Arrays.asList();
	private final List<String> ZukPhrases = Arrays.asList();
	//If you die to a rat spider or bat
	private final List<String> BrynrsbPhrases = Arrays.asList();
	//if you die before going to hunleff
	private final List<String> BrynphPhrases = Arrays.asList();
	//if you die during hunleff
	private final List<String> BrynPhrases = Arrays.asList();
	private final List<String> ZulPhrases = Arrays.asList("Snake?? SNAKE??? SNAAAAAAAAAAAAAAAAAAKEEE");
	private final List<String> OlmPhrases = Arrays.asList();

	private final List<String> SixNinePhrases = Arrays.asList();
	private final List<String> SevenThreePhrases = Arrays.asList();
	private final List<String> OnePhrases = Arrays.asList();

	private List<String> Recentlycommented;



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
			rand = r.nextInt(p.size());
		}
		String s = p.get(rand);
		if (s.contains("{Enemyname}"))
		{
			String killername = (String) Killer.getName();
			s = s.replace("{Enemyname}", killername);
		}
		if (s.contains("{Playername}"))
		{
			String playername = (String) client.getLocalPlayer().getName();
			s = s.replace("{Playername}", playername);
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
			Recentlycommented.clear();
			lastOpponent = null;
			Killer = null;
			lastdamagetaken = 0;
			finaldamagetaken = 0;
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
	public void onHitsplatApplied(Actor actor, Hitsplat hitsplat) {
		if (actor instanceof Player) {
			Player player = (Player) actor;

			if (player == client.getLocalPlayer())
			{
				lastdamagetaken = hitsplat.getAmount();
			}
		}
	}


	@Subscribe
	public void onActorDeath(ActorDeath event) {
		Actor actor = event.getActor();
		if (actor instanceof Player) {
			Player player = (Player) actor;

			if (player == client.getLocalPlayer())
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
				final int GauntletMaze = client.getVarbitValue(9178);
				if (GauntletMaze == 1)
				{
					if (Gauntletminions.contains(Killer.getName()))
					{
						prehunleffminion = true;
						return;
					}
					prehunleffdeath = true;
				}
				//gauntlet boss
				final int Hunleff = client.getVarbitValue(9177);
				if (Hunleff == 1)
				{
					hunleffdeath = true;
				}
				//In chambers
				if (client.getVarbitValue(Varbits.IN_RAID)>0)
				{
					chambersdeath = true;
				}

				Killer = lastOpponent;
				finaldamagetaken = lastdamagetaken;
				lastOpponent = null;
				Recentlycommented.clear();
				final Duration forgetduration = Duration.ofMinutes(config.ForgetTimeDelay());
				ForgetTime = Instant.now().plus(forgetduration);
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
				String Khaledtaunt = Phrase(KhaledPhrases);
				event.getNpc().setOverheadCycle(200);
				event.getNpc().setOverheadText(Khaledtaunt);
			}
			return;
		}
		if (Recentlycommented.contains(event.getNpc().getName())) { return;}
		String taunt;
		if (finaldamagetaken == 69)
		{
			if ( r.nextInt(100)>50){
				taunt = Phrase(SixNinePhrases);
				event.getNpc().setOverheadCycle(200);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getName());
			}
		}
		if (finaldamagetaken ==73)
		{
			if ( r.nextInt(100)>70){
				taunt = Phrase(SevenThreePhrases);
				event.getNpc().setOverheadCycle(200);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getName());
			}
		}
		if (finaldamagetaken ==1)
		{
			if ( r.nextInt(100)>90){
				taunt = Phrase(OnePhrases);
				event.getNpc().setOverheadCycle(200);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getName());
			}
		}
		if (event.getNpc().getName().equals("Hans"))
		{
			taunt = Phrase(HansPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Hatius Cosaintus"))
		{
			taunt = Phrase(HaitusPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Vaire"))
		{
			taunt = Phrase(PriffPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Banker"))
		{
			taunt = Phrase(BankerPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			if ( r.nextInt(100)>70){
			Recentlycommented.add(event.getNpc().getName());
			}
		}
		if (event.getNpc().getName().equals("Squire"))
		{
			taunt = Phrase(SquirePhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Ferox"))
		{
			taunt = Phrase(FeroxPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("The 'Wedge'"))
		{
			taunt = Phrase(WedgePhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Elise"))
		{
			taunt = Phrase(ElisePhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Town Crier"))
		{
			taunt = Phrase(TowncrierPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}




		if (event.getNpc().getName().equals("TzHaar-Mej-Jal"))
		{
			if (prejaddeath)
			{
				taunt = Phrase(PreJadPhrases);
			}
			else if (jaddeath)
			{
				taunt = Phrase(JadPhrases);
			}
			else{
				return;
			}
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("TzHaar-Ket-Keh"))
		{
			if (prezukdeath)
			{
				taunt = Phrase(PreZukPhrases);
			}
			else if (zukdeath)
			{
				taunt = Phrase(ZukPhrases);
			}
			else{
				return;
			}
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}

		if (event.getNpc().getName().equals("Bryn"))
		{
			if (prehunleffminion)
			{
				taunt = Phrase(BrynrsbPhrases);
			}
			else if (prehunleffdeath)
			{
				taunt = Phrase(BrynphPhrases);
			}
			else if (hunleffdeath) {
				taunt = Phrase(BrynphPhrases);
			}
			else{
				return;
			}
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
		}
		if (event.getNpc().getName().equals("Priestess Zul-Gwenwynig"))
		{
			if (zulrahdeath){
			taunt = Phrase(ZulPhrases);
			event.getNpc().setOverheadCycle(200);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getName());
			}
			else return;
		}
		if (event.getNpc().getName().equals("Great Olm"))
		{
			if (chambersdeath){
				taunt = Phrase(OlmPhrases);
				event.getNpc().setOverheadCycle(400);
				event.getNpc().setOverheadText(taunt);
			}
			else return;
		}

	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
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
	NPCTauntsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NPCTauntsConfig.class);
	}
}