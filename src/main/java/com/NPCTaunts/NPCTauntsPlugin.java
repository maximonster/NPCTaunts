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
		int b =0;
		while (p.get(rand).contains("{Enemyname}") && Killer == null && b<20)
		{
			rand = r.nextInt(p.size());
			b++;
		}
		String s = p.get(rand);
		if (s.contains("{Enemyname}")) {
			if (b < 20) {
				String killername = Killer;
				s = s.replace("{Enemyname}", killername);
				log.debug(killername + " was recorded as killer name");
			} else {
				s = s.replace("{Enemyname}", "that last foe");

			}
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
				String otaunt = Phrase(Arrays.asList(NPCTauntsPhrases.Olm));
				event.getNpc().setOverheadCycle(400);
				event.getNpc().setOverheadText(otaunt);
			}
			else return;
		}

		if (Died == false)
		{
			if (event.getNpc().getName().equals("Hatius Cosaintus"))
			{
				String Khaledtaunt = Phrase(Arrays.asList(NPCTauntsPhrases.Khaled));
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(Khaledtaunt);
			}
			return;
		}
		if (Recentlycommented.contains(event.getNpc().getId())) { return;}
		String taunt;

		if (event.getNpc().getName().equals("Hans"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Hans));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Hatius Cosaintus"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Haitus));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Vaire"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Priff));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Banker"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Banker));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			if ( r.nextInt(100)>70){
			Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (event.getNpc().getName().equals("Squire"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Squire));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Ferox"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Ferox));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("The 'Wedge'"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Wedge));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Elise"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Elise));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Town Crier"))
		{
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Towncrier));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}




		if (event.getNpc().getName().equals("TzHaar-Mej-Jal"))
		{
			if (prejaddeath)
			{
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.PreJad));
			}
			else if (jaddeath)
			{
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Jad));
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
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.PreZuk));
			}
			else if (zukdeath)
			{
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Zuk));
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
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.PreSol));
			}
			else if (soldeath)
			{
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Sol));
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
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Brynrsb));
			}
			else if (prehunleffdeath)
			{
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Brynph));
			}
			else if (hunleffdeath) {
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Bryn));
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
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Zul));
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
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.SixNine));
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (finaldamagetaken ==73)
		{
			if ( r.nextInt(100)>70){
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.SevenThree));
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
				Recentlycommented.add(event.getNpc().getId());
			}
		}
		if (finaldamagetaken ==1)
		{
			if ( r.nextInt(100)>90){
				taunt = Phrase(Arrays.asList(NPCTauntsPhrases.One));
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