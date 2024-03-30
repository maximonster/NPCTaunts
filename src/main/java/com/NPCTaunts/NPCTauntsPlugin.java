package com.NPCTaunts;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
import net.runelite.client.util.Text;


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
	private String lastOpponent;
	private String Killer;
	private int lastdamagetaken;
	private int finaldamagetaken;
	private static final int ZULRAH_SPAWN_REGION_ID = 9007;
	private static final int ZULRAH_REGION_ID = 9008;
	static final int FIGHT_CAVE_REGION = 9551;
	private boolean Jad;
	static final int INFERNO_REGION = 9043;
	static final int COLOSSEUM_REGION = 7216;
	private boolean Zuk;
	private boolean Sol;
	private static final List<Integer> ZulRegionIDs = Arrays.asList(ZULRAH_SPAWN_REGION_ID,ZULRAH_REGION_ID);
	private static final List<String> Gauntletminions = Arrays.asList("Crystalline Bat","Crystalline Rat","Crystalline Spider","Corrupted Rat","Corrupted Spider","Corrupted Bat");

	private boolean prejaddeath;
	private boolean jaddeath;
	private boolean prezukdeath;
	private boolean zukdeath;
	private boolean presoldeath;
	private boolean soldeath;
	private boolean zulrahdeath;
	private boolean prehunleffminion;
	private boolean prehunleffdeath;
	private boolean hunleffdeath;
	private boolean chambersdeath;


	private final List<String> HansPhrases = Arrays.asList("You'll get them next time Tiger!","I've seen everyone end up here again, so don't worry","Get back on that unicorn and show them!","Unlucky!","They got lucky","It's okay {Playername}. I'm here for you");
	private final List<String> HaitusPhrases = Arrays.asList("RIP Bozo","Sit idiot","Get some better gear maybe you'll live longer","Obtain good pleb","Just buy your cape like I did","Im living in your game rent free","AH! You're just fresh out of tutorial island","Been caught lackin","Dying is not very stylish is it?","Pauper Poor Primitive Peasant","Good thing they don't measure fashionscape skill levels for you","If only your list of deaths was a small as your staff","Buy an adamant longer sword, yours is too short","I fart in your general direction","If only your staff was as mighty as mine","{Playername} sucks at staying alive!");
	private final List<String> KhaledPhrases = Arrays.asList("I call her chandelier","Let's go golfing","Call me a tennis ball.","We ain't stoppin, let's go shoppin.","That's when you know you're the greatest: when you're the greatest, and people still put odds against you." , "I tell myself every day I love my Jacuzzi, I love my marble floors, I love my high ceilings");
	private final List<String> PriffPhrases = Arrays.asList("Do all humans die this much?","I think that one is special","All that foreign armor doesn't seem to protect them");
	private final List<String> BankerPhrases = Arrays.asList("Losing the hardcore status: completed","Please stop dying, your bank value can't handle it","Can you ask Death to give us a commission?","Your bank is full of food! Use it!","Next time if you don't respawn on time your bank will be forclosed");
	private final List<String> SquirePhrases = Arrays.asList("Why do you even respawn here?","You know all the knights make fun of you right?");
	private final List<String> FeroxPhrases = Arrays.asList("I see {Playername} got PKed","Death is inevitable around here","This isn't good for your KD");
	private final List<String> WedgePhrases = Arrays.asList("We're gonna be winning, and frankly, we're gonna be tired of winning.","When you get killed they let you do anything. Grab 'em by the hellcat.","Despite all the negative deaths, covfefe","If {Enemyname} weren't my daughter I'd be dating her","{Playername} is gonna build a wall, and make {Enemyname} pay for it.");
	private final List<String> ElisePhrases = Arrays.asList("Dying is not the vibe, STOP!!!!!!!!!","You loser, RAT!!!");
	private final List<String> TowncrierPhrases = Arrays.asList("Hear ye, hear ye {Playername} has died to a {Enemyname}!","Listen all a {Enemyname} got the better of {Playername}");
	private final List<String> PreJadPhrases = Arrays.asList("JalYts! JalYt {Playername} failed the fight caves!","JalYt {Playername} died fighting {Enemyname}!");
	private final List<String> JadPhrases = Arrays.asList("JalYt {Playername} thought they could take a hit from TzTok-Jad","JalYt {Playername} thought they could outdamage Yt-HurKot","Can someone clean up JalYt {Playername}'s remains? TzTok-Jad made a mess again");
	private final List<String> PreZukPhrases = Arrays.asList("JalYt {Playername} should avoid taking hits from Jal-Xil's heavy boulders","JalYt {Playername} should avoid taking hits from Jal-Zek's powerful magical abilities","JalYt {Playername} should avoid chasing Jal-Nib","JalYt {Playername} has succumbed to the inferno","Cmon man its easier than the colloseum");
	private final List<String> ZukPhrases = Arrays.asList("TzKal-Zuk is unbeatable","Was it worth your fire cape?","JalYt {Playername} you must stay behind the shield");
	//If you die to a rat spider or bat
	private final List<String> BrynrsbPhrases = Arrays.asList("You died to a {Enemyname}????","How in Seren's name did you die to a {Enemyname}?");
	//if you die before going to hunleff
	private final List<String> BrynphPhrases = Arrays.asList("Stay away from the corrupted gauntlet if you can't even make it to Hunleff","Have you considered doing easier content instead?");
	//if you die during hunleff
	private final List<String> BrynPhrases = Arrays.asList("Hey everyone {Playername} died so will get some cod!","Get stomped","Trying running away from the tornadoes","The floor really is lava","Standing still will get you nill","This one would've been the Enhanced weapon seed");
	private final List<String> ZulPhrases = Arrays.asList("Snake?? SNAKE??? SNAAAAAAAAAAAAAAAAAAKEEE","You have blessed us with a bountiful eel harvest","Zulrah was pleased","Your sacrifice is appreciated","The sacred contract has been fullfilled once again","Zulrah dislikes the taste of your equipment");
	private final List<String> OlmPhrases = Arrays.asList("No purple for you","If you scuff I will not provide stuff");
	private final List<String> PreSolPhrases = Arrays.asList("A {Title} has passed away!","{Playername} made it to wave {Wave}","{Mod} was too much for you","Death smiles at us all. All a man can do is smile back","Were you not entertained?","This is FORTIS","Maybe don't select {Mod} next time","Cmon man its easier than inferno","Morituri te salutant");
	private final List<String> SolPhrases = Arrays.asList("A {Title} was defeated by Sol Heredit!","Sol is known for twisting your knob off","People should know when they are conquered","{Mod} makes Sol stronger");

	private final List<String> SixNinePhrases = Arrays.asList("hehe nice");
	private final List<String> SevenThreePhrases = Arrays.asList("You little ginger prick","Thanks for the zerker ring man");
	private final List<String> OnePhrases = Arrays.asList("Seriously, you died to a 1? Eat a cabbage next time, then you might survive. Yuck.","Gratz on your Nightmare Mode loss","EAT {Playername}! EAT!");

	private List<Integer> Recentlycommented = new ArrayList<>();
	private String colotitle = "";
private int wave;
	private static final int VARBIT_MODIFIER_SELECTED = 9788;
	private static final int SCRIPT_MODIFIER_SELECT_INIT = 4931;
	private final List<Integer> modifierOptions = new ArrayList<>(3);
private int Modifierselected =-1;
	private final List<String> modifiers = Arrays.asList("The Doom Scorpion","Reentry","Bees!","Volatility","Blasphemy","Relentless","Quartet","Totemic","Doom","Dynamic Duo","Solarflare","Myopia","Frailty","Red Flag");

    @Override
	protected void startUp() throws Exception
	{
		log.info("NPC Taunts started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		Died = false;
		prejaddeath=false;
		jaddeath=false;
		prezukdeath=false;
		zukdeath=false;
		presoldeath=false;
		soldeath=false;
		zulrahdeath=false;
		prehunleffminion=false;
		prehunleffdeath=false;
		hunleffdeath=false;
		chambersdeath=false;
		Modifierselected =-1;
		Recentlycommented.clear();
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
			String killername = Killer;
			s = s.replace("{Enemyname}", killername);
			log.debug(killername+" was recorded as killer name");
		}
		if (s.contains("{Playername}"))
		{
			String playername = (String) client.getLocalPlayer().getName();
			s = s.replace("{Playername}", playername);
		}
		if (s.contains("{Title}"))
		{
			s = s.replace("{Title}", colotitle);
		}
		if (s.contains("{Wave}"))
		{
			String Wave = Integer.toString(wave);
			s = s.replace("{Wave}", Wave);
		}
		if (s.contains("{Mod}"))
		{
			if (Modifierselected ==-1)
			{
				s = s.replace("{Mod}", "That last modifier");;
			}
			else{
			String mod = modifiers.get(Modifierselected);
			s = s.replace("{Mod}", mod);
			}
		}
		return s;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (Died == false ||ForgetTime == null){ return;}
		if (Instant.now().compareTo(ForgetTime) >= 0)
		{
			Died = false;
			Recentlycommented.clear();
			lastOpponent = null;
			Killer = null;
			lastdamagetaken = 0;
			finaldamagetaken = 0;
			log.debug("Most recent death has been forgotten");
		}
	}
	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks
		log.debug(chatMsg);
		if (chatMsg.startsWith("Minimus: A") && (chatMsg.contains("approaches!")))
		{
			String colosseumtitle = chatMsg.replace("Minimus: A ","").replace(" approaches!","");
			colotitle = colosseumtitle;
		}
		if (chatMsg.startsWith("Wave:"))
		{
			wave =  Integer.valueOf(chatMsg.replace("Wave: ",""));
		}
	}

	@Subscribe
	public void onScriptPreFired(ScriptPreFired pre)
	{
		if (pre.getScriptId() == SCRIPT_MODIFIER_SELECT_INIT)
		{
			modifierOptions.clear();
			Object[] args = pre.getScriptEvent().getArguments();
			modifierOptions.add((Integer) args[2]);
			modifierOptions.add((Integer) args[3]);
			modifierOptions.add((Integer) args[4]);
		}
	}

	private void SelectedModifier()
	{
		if (modifierOptions.isEmpty())
		{
			log.debug("Modifier options were not loaded");
			return;
		}

		int selectedoption = client.getVarbitValue(VARBIT_MODIFIER_SELECTED);
		if (selectedoption == 0)
		{
			log.debug("Modifier selected varbit = 0");
			return;
		}

		Modifierselected = modifierOptions.get(selectedoption - 1);
		modifierOptions.clear();
		if (Modifierselected == -1)
		{
			log.debug("Failed to select modifier");;
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
		lastOpponent = opponent.getName();
		log.debug(lastOpponent+" was last opponent");
	}
	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
		Actor actor = hitsplatApplied.getActor();
		Hitsplat hitsplat = hitsplatApplied.getHitsplat();
		if (actor instanceof Player) {
			Player player = (Player) actor;

			if (player == client.getLocalPlayer())
			{
				lastdamagetaken = hitsplat.getAmount();
			}
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged){
		if (client.getVarbitValue(Varbits.IN_RAID)==0)
		{
			chambersdeath = false;
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
				Killer = lastOpponent;
				finaldamagetaken = lastdamagetaken;
				lastOpponent = null;
				log.debug("Has commented on previous death: "+Recentlycommented.toString());
				Recentlycommented.clear();
				log.debug("Should be empty: "+ Recentlycommented.toString());
				final Duration forgetduration = Duration.ofMinutes(config.ForgetTimeDelay());
				ForgetTime = Instant.now().plus(forgetduration);

				prejaddeath=false;
				jaddeath=false;
				prezukdeath=false;
				zukdeath=false;
				presoldeath=false;
				soldeath=false;
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
				if (regionId == COLOSSEUM_REGION)
				{
					if (Sol)
					{
						soldeath = true;
					}
					else
					{
						presoldeath = true;
					}
				}

				//gauntlet maze
				final int GauntletMaze = client.getVarbitValue(9178);
				if (GauntletMaze == 1)
				{
					if(Killer !=null) {
						int i = 0;
						while (i < Gauntletminions.size()) {
							String min = Gauntletminions.get(i);
							if (min.contains(Killer)) {
								prehunleffminion = true;
								log.debug("You died to a rat/bat/spoder");
								return;
							}
							i++;
						}
					}
					if (prehunleffminion == false)
					{
						prehunleffdeath = true;
					}
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
		if (event.getNpc().getName().equals("Sol Heredit"))
		{
			Sol = true;
		}

		if (event.getNpc().getName().equals("Great Olm"))
		{
			if (chambersdeath){
				String otaunt = Phrase(OlmPhrases);
				event.getNpc().setOverheadCycle(400);
				event.getNpc().setOverheadText(otaunt);
			}
			else return;
		}

		if (Died == false)
		{
			if (event.getNpc().getName().equals("Hatius Cosaintus"))
			{
				String Khaledtaunt = Phrase(KhaledPhrases);
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(Khaledtaunt);
			}
			return;
		}
		if (Recentlycommented.contains(event.getNpc().getId())) { return;}
		String taunt;

		if (event.getNpc().getName().equals("Hans"))
		{
			taunt = Phrase(HansPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Hatius Cosaintus"))
		{
			taunt = Phrase(HaitusPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Vaire"))
		{
			taunt = Phrase(PriffPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Banker"))
		{
			taunt = Phrase(BankerPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			if ( r.nextInt(100)>70){
			Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (event.getNpc().getName().equals("Squire"))
		{
			taunt = Phrase(SquirePhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Ferox"))
		{
			taunt = Phrase(FeroxPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("The 'Wedge'"))
		{
			taunt = Phrase(WedgePhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Elise"))
		{
			taunt = Phrase(ElisePhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Town Crier"))
		{
			taunt = Phrase(TowncrierPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
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
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
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
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Minimus") && getRegionId() != COLOSSEUM_REGION)
		{
			if (presoldeath)
			{
				taunt = Phrase(PreSolPhrases);
			}
			else if (soldeath)
			{
				taunt = Phrase(SolPhrases);
			}
			else{
				return;
			}
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
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
				taunt = Phrase(BrynPhrases);
			}
			else{
				return;
			}
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Priestess Zul-Gwenwynig"))
		{
			if (zulrahdeath){
			taunt = Phrase(ZulPhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
			}
			else return;
		}


		//every NPC per ID has a chance  to comment for the following cases
		if (Recentlycommented.contains(event.getNpc().getId())) { return;}

		if (finaldamagetaken == 69)
		{
			if ( r.nextInt(100)>50){
				taunt = Phrase(SixNinePhrases);
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (finaldamagetaken ==73)
		{
			if ( r.nextInt(100)>70){
				taunt = Phrase(SevenThreePhrases);
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (finaldamagetaken ==1)
		{
			if ( r.nextInt(100)>90){
				taunt = Phrase(OnePhrases);
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getId());
			}
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
		final int regionId = getRegionId();
		if (regionId == COLOSSEUM_REGION && event.getNpc().getName().equals("Minimus"))
		{
			log.debug("Modifier selected");
			SelectedModifier();
		}
	}

	@Provides
	NPCTauntsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NPCTauntsConfig.class);
	}
}