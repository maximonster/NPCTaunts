package com.NPCTaunts;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.awt.Color;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.Text;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.api.MessageNode;

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
	@Inject
	private ChatMessageManager chatMessageManager;
@Inject
private EventBus eventBus;

	@Inject
	private ChatColorConfig chatColorConfig;
	private MessageNode messageNode;
	private boolean Died;
	private Instant ForgetTime;
	private String lastOpponent;
	private String Killer;
	private boolean pvpactive;
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
	private int TOAREWARD_REGION_ID = 14672;
	private static final int[] VARBIT_MULTILOC_IDS_CHEST = new int[]{
			14356, 14357, 14358, 14359, 14360, 14370, 14371, 14372
	};

	private static final int VARBIT_VALUE_CHEST_KEY = 2;
	private static final int VARBIT_ID_SARCOPHAGUS = 14373;
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
    private boolean pvpdeath;
    private boolean sarcophagusIsPurple;
    private boolean purpleIsMine;
    private boolean Corrupted;

    private final List<String> NievePhrases = Arrays.asList("Lol how did you die idiot","You shouldn't die to {Enemyname}. That's such a noob move","Maybe a metal dragon task will make you stronger");
    private final List<String> StevePhrases = Arrays.asList("I see why Nieve died...","Tureal is stronger than you","Brine rats are a better opponent for you");

    private final List<String> AhrimtPhrases = Arrays.asList("Ahrim away Ahrim away, Ahrim away Ahrim awa...","Pro tip: Only I can eat blighted food outside PvP areas.");
    private final List<String> KariltPhrases = Arrays.asList("I guess you didn't change your quick prayers to Protect from Missiles?","Haha now I drained your Agility to 0.");
    private final List<String> DharokPhrases = Arrays.asList("{Lasthit}! That's a good one.","Come back with more HP so I can hit higher.","Did you know I can hit even higher than {Lasthit}?");
    private final List<String> GuthanPhrases = Arrays.asList("You're weaker than Torag.");
    private final List<String> ToragtPhrases = Arrays.asList("See? Dual wielding is the future of PKing.","Screw all of your run energy!","I wish they named me Torag the akimbo.","See? Eating glue does make you strong and smart.");
    private final List<String> VeractPhrases = Arrays.asList("Let me defile your corpse. Fancy some Earl Grey?","Maybe my cousin dung eater would like your corpse.");
    private final List<String> ScurriPhrases = Arrays.asList("Who's the rat now?","I'm the king of the rats, bow to me.");
    private final List<String> GiantMPhrases = Arrays.asList("Giant Mole used Dig! It was super effective!","You forgot to sip your prayer potion, didn't ya?");
    private final List<String> DerangPhrases = Arrays.asList("Plant food!","I stopped one of them but what if more will come now?");
    private final List<String> SupremePhrases = Arrays.asList("Split I ranged!","You are supreme embarassing.");
    private final List<String> RexPhrases = Arrays.asList("Split I specced!","Good luck on the run back.","Get Rext!");
    private final List<String> PrimePhrases = Arrays.asList("Split I TBed!","Guess you are not in your prime.","Turned you into primeordial soup.");
    private final List<String> SarachPhrases = Arrays.asList("You're about to throw as Hsss'y fit, aren't you?");
    private final List<String> BloodMPhrases = Arrays.asList("Thanks for the heals.","Upgrade your defenses.");
    private final List<String> BlueMoPhrases = Arrays.asList("The cold seeps into your bones.","Not even a blue moon will help you beat me.");
    private final List<String> EclipsPhrases = Arrays.asList("Now you see me, now you don't.","I've eclipsed you.");
    private final List<String> KalphiPhrases = Arrays.asList("Clearly a skill issue.");
    private final List<String> KreePhrases = Arrays.asList("Justice has been served.","Your issue is your skill.","Hopefully your death doesn't cause another rodent problem.");
    private final List<String> CommanPhrases = Arrays.asList("Saradomin has helped you sit.","{Playername} stood against Saradomin and failed.","Saradomin has guided you to your place, 6 feet under.");
    private final List<String> GeneraPhrases = Arrays.asList("Meat's back on the menu boys.","{Playername} weak like goblin.");
    private final List<String> KrilPhrases = Arrays.asList("What's the matter you gonna cry?","Your skull shall make a nice goblet.");
    private final List<String> CorporPhrases = Arrays.asList("Bwaaaaaak bwuk bwuk.","Flee from me, {playername}!","Begone, {playername}!","Bwuk bwuk bwuk.");
    private final List<String> NexPhrases = Arrays.asList("Next.","Zaros speaks through me, his message: Sit.");
    private final List<String> ChaosFPhrases = Arrays.asList("Ah there you are squidgy, you were inside their skull all this time.","x n + 1 = r x n ( x n − 1 )");
    private final List<String> CrazyaPhrases = Arrays.asList("Finally peace.","Doesn't knowledge taste good?");
    private final List<String> ScorpiPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> KingBlPhrases = Arrays.asList("Say hi to Bob for me","They call me the king. This dragonfire you ain't resisting");
    private final List<String> ChaosEPhrases = Arrays.asList("Lorem ipsum","{Playername}? {Playername}! {Playername}.","Ad sedes, rattus!.");
    private final List<String> RevenaPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> CalvarPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> VetioPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> SpindePhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> VenenaPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> ArtioPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> CallisPhrases = Arrays.asList("Sit rat.","Smited.","Back to lumby.","Stick to F2p nub.","Sit ******* ********.","Cya in Lumby.","L000000000L.","Sit.","Rat.","TY.");
    private final List<String> ZulrahPhrases = Arrays.asList("Zulratatata.","ssssucker.","You don't need a blowpipe, you blow enough pipe as is.","ssssit.");
    private final List<String> VorkatPhrases = Arrays.asList("Now let me sleep.","Tell Torfinn to give me half.","The acid reflux is worth it if they die so easily.","Vork you.");
    private final List<String> PhantoPhrases = Arrays.asList("Get grumbled!","Grumbler wins again.");
    private final List<String> TheNightPhrases = Arrays.asList("Goodnight!","Sleep well.","Sweet dreams.");
    private final List<String> PhosanPhrases = Arrays.asList("Goodnight!","Sleep well.","Sweet dreams.","Seems like I'm not just Phosani's nightmare.");
    private final List<String> DukeSuPhrases = Arrays.asList("*Nom nom*","Food for baron.");
    private final List<String> TheLevPhrases = Arrays.asList("Pew pew pew.","What's the matter, can't pray switch?");
    private final List<String> TheWhiPhrases = Arrays.asList("And so another voice joins the Silent Choir.","Your voice will be one of thousands, singing an eternal silence.","Do not be afraid, my child.");
    private final List<String> VardorPhrases = Arrays.asList("Keep your head in the game.","Don't get ahead of yourself.","You were 2/3 for sure.");
    private final List<String> OborPhrases = Arrays.asList("Obor smash!");
    private final List<String> BryophPhrases = Arrays.asList("Go touch grass.");
    private final List<String> TheMimPhrases = Arrays.asList("Oh no you overdosed on sugar","You got stuck on the sticky","I'll mimic a coffin for you");
    private final List<String> HesporPhrases = Arrays.asList("I needed 32 hours to grow to become stronger than you. What's your excuse?","Stop using me as a cheap bank, you ungrateful UIM");
    private final List<String> SkotizPhrases = Arrays.asList("Hopfully I get the 1/65 Lil' {Playername} this kill!","What are the odds to get a Jar of Humanity this kill?");
    private final List<String> GrotesDuPhrases = Arrays.asList("This is hopeless go to bed");
    private final List<String> GrotesDaPhrases = Arrays.asList("");
    private final List<String> AbyssaPhrases = Arrays.asList("Imma firin' mah laz0r!","Sire used Explode! It was super effective!");
    private final List<String> KrakenPhrases = Arrays.asList("I'm better at splashing than you!","Splish splash!");
    private final List<String> CerberPhrases = Arrays.asList("Arrooo","Thhhhbt!","Woof woof");
    private final List<String> ThermoPhrases = Arrays.asList("Now you see me, now you're dead.","You're an inspiration for birth control","Your ass is grass, and I've got the weed whacker","Your face, your ass - what's the difference?");
    private final List<String> AlchemPhrases = Arrays.asList("Stay hydrated.","Go back to chemistry class.","Tell Konar I said hi.");
    private final List<String> HunPhrases = Arrays.asList("The only enhanced you'll see is enhanced failure.","You're stuck in this prison with me.","Stick to regular gauntlet.","I don't think perfected armor is even enough for you...");
    private final List<String> TzTokPhrases = Arrays.asList("No cheese strats for cheese cape.","I gave you plenty of time to react...","Your hands aren't the only thing I can vibrate.");
    private final List<String> TzKalPhrases = Arrays.asList("lol","git gud","gf","rip bozo","idiot","?");
    private final List<String> WinterPhrases = Arrays.asList("I pour out this cold for the boys.","Getting interrupted by cold is a skill issue.","Feelin' hot, hot, hot...");
    private final List<String> ZalcanPhrases = Arrays.asList("Imagine dying to a skilling boss.","Even lvl 3 skillers consider me a 0-damage boss.");
    private final List<String> TektonPhrases = Arrays.asList("Tekton bonk you!","Tekton win!","Tekton strongest!","Hammertime!");
    private final List<String> VanguaPhrases = Arrays.asList("You should consider investing in a bulwark.","Triple threat supplies your death.","Go back to school to learn your combat triangle.");
    private final List<String> VespulPhrases = Arrays.asList("I'll see you vespulater.","You've failed your redemption.");
    private final List<String> VasaNiPhrases = Arrays.asList("My victory is crystal clear.","I can't hear you with that boulder in your face.");
    private final List<String> MuttadPhrases = Arrays.asList("*Munch munch munch*","Clearly a skill issue.");
    private final List<String> GreatOPhrases = Arrays.asList("No purple for you.","If you scuff you will not get stuff.","The only twisted you'll see is me twisting your nuts.","You're lucky to get some pure essence, provided you can actually finish...");
    private final List<String> TheMaiPhrases = Arrays.asList("Sugadis nuts.","You're a bloody mess.");
    private final List<String> PestilPhrases = Arrays.asList("You are the weakest link. Goodbye.","Why are you runnning?");
    private final List<String> NylocaPhrases = Arrays.asList("Switching gear and prayer too hard for you?","Good job on keeping the pillars up and then dying on the easy part");
    private final List<String> SotetsPhrases = Arrays.asList("Amazing how bad you are.","Looks like you're going to the Shadow realm, {Playername}.");
    private final List<String> XarpusPhrases = Arrays.asList("Xarpus used counter. It's super effective!","Your painting skills are lacking.");
    private final List<String> VerzikPhrases = Arrays.asList("I see {Playername} is the least valuable player.","Your fingers are fatter than me.","Stick to entry mode.","Your night at the theater have become your grave at the theater.","The reviews are in, you're cancelled.");
    private final List<String> AkkhaPhrases = Arrays.asList("Akkhan't with you.","Where's your butterfly now?");
    private final List<String> BaBaPhrases = Arrays.asList("Cake pop! Boop boop","Ba-ba-Byeeeee","Mind the gap! Your skill gap");
    private final List<String> KephriPhrases = Arrays.asList("Get pooped on!","Seems like you need this shield more than me.","Don't let it bug you.");
    private final List<String> ZebakPhrases = Arrays.asList("I'll wave you goodbye!","I wish you died closer now I have to move for my snack.");
    private final List<String> TumekePhrases = Arrays.asList("ALL YOUR BASE BELONG TO US","EXTERMINATE! EXTERMINATE! EXTERMINATE!","ERROR 404 STRONG OPPONENT NOT FOUND");
    private final List<String> ElidinPhrases = Arrays.asList("ALL YOUR BASE BELONG TO US","EXTERMINATE! EXTERMINATE! EXTERMINATE!","ERROR 404 STRONG OPPONENT NOT FOUND");
    private final List<String> GunthoPhrases = Arrays.asList("YAAAAAAAARGH! GUNTHOR STRONG!","BARBARIAN MIGHT SMITES YOU");

    private List<String> PKTaunts= Arrays.asList("Sit rat","Smited","Back to lumby","Stick to F2p nub","Sit ******* ******","Cya in Lumby", "L000000000L","Sit","Rat","TY");

    private List<String> BrynnNoUniqPhrases;
    private List<String> BrynnArmorseedPhrases;
    private List<String> BrynnWeaponseedPhrases;
    private List<String> BrynnEnhancedPhrases;

    private List<String> OsmuYourPurplePhrases;
    private List<String> OsmuFriendPurplePhrases;
    private List<String> OsmuNoPurplePhrases;

    private List<Integer> Recentlycommented = new ArrayList<>();
    private List<NPC> SurroundingNPCS = new ArrayList<>();
    private List<String> DropsReceived = new ArrayList<>();
    
    
	private List<Integer> Recentlycommented = new ArrayList<>();
	private List<NPC> SurroundingNPCS = new ArrayList<>();
	private List<String> DropsReceived = new ArrayList<>();

	private String colotitle = "";
	private int wave;
	private static final int VARBIT_MODIFIER_SELECTED = 9788;
	private static final int SCRIPT_MODIFIER_SELECT_INIT = 4931;
	private final List<Integer> modifierOptions = new ArrayList<>(3);
private int Modifierselected =-1;
	private final List<String> modifiers = Arrays.asList("Mantimayhem","Reentry","Bees!","Volatility","Blasphemy","Relentless","Quartet","Totemic","Doom","Dynamic Duo","Solarflare","Myopia","Frailty","Red Flag");



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
		if (s.contains("{Lasthit}"))
		{
			String lasthit = Integer.toString(lastdamagetaken);
			s = s.replace("{Lasthit}", lasthit);
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
		}
	}
	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks
		if (chatMsg.startsWith("Minimus: A") && (chatMsg.contains("approaches!")))
		{
			String colosseumtitle = chatMsg.replace("Minimus: A ","").replace(" approaches!","");
			colotitle = colosseumtitle;
		}
		if (chatMsg.startsWith("Wave:"))
		{
			wave =  Integer.valueOf(chatMsg.replace("Wave: ",""));
		}
		if (chatMsg.startsWith(client.getLocalPlayer().getName()+"re")&& !Died && getSurroundingNPCnames("Brynn")!= null){
			NPC Brynn = getSurroundingNPCnames("Brynn");
			DropsReceived.add(chatMsg);
			int drops = 100;
			if(Corrupted) {
				drops = 4;
			}
			else {
				drops = 3;
			}
			if (DropsReceived.size() >= drops);{
				if (getDrops("Enhanced")){
					taunt(Brynn, Phrase(BrynnEnhancedPhrases));
				} else if (getDrops("weapon seed")) {
					taunt(Brynn, Phrase(BrynnWeaponseedPhrases));
				}
				else if (getDrops("armour seed")){
					taunt(Brynn, Phrase(BrynnArmorseedPhrases));
				}else {
					taunt(Brynn, Phrase(BrynnNoUniqPhrases));
				}
				DropsReceived.clear();
			}
		}



		messageNode = event.getMessageNode();
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
		Actor opponent;
		if (event.getSource().equals(client.getLocalPlayer()))
		{
			opponent = event.getTarget();
			if (event.getTarget() instanceof Player)
			{
				pvpactive = true;
			}
			else{
				pvpactive = false;
			}

		} else if (event.getTarget() ==null)
		{
			return;
		} else if (event.getTarget().equals(client.getLocalPlayer()))
		{
			opponent = event.getSource();
			if (event.getSource() instanceof Player)
			{
				pvpactive = true;
			}			else{
				pvpactive = false;
			}
		}
		else // if neither source or target was the player, skip
		{
			return;
		}
		if (opponent !=null){
		lastOpponent = opponent.getName();
		}
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
		final int Hunleff = client.getVarbitValue(9177);
		if (Hunleff == 1)
		{
			if (getSurroundingNPCnames("Crystalline Hunllef") != null)
			{
				Corrupted = false;
			}
			if (getSurroundingNPCnames("Corrupted Hunllef")!= null)
			{
				Corrupted = true;
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
				Killer = lastOpponent;
				finaldamagetaken = lastdamagetaken;
				lastOpponent = null;
				Recentlycommented.clear();
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
				pvpdeath=false;
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
					if (wave == 12)
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
				if (pvpactive){
					pvpdeath = true;
				}
				if(config.Bosstaunts()&&!config.pktaunts()){
					NPC olm =getSurroundingNPCnames("Great Olm");
					if (olm != null)
					{
						taunt(olm,Phrase(OlmPhrases));
					}
					NPC hun =getSurroundingNPCnames("Crystalline Hunllef");
					if(hun ==null){
						hun =getSurroundingNPCnames("Corrupted Hunllef");
					}
					if (hun != null)
					{
						taunt(hun,Phrase(HunPhrases));
					}
					NPC Ahrimt = getSurroundingNPCnames("Ahrim the Blighted");
					if (Ahrimt != null)
					{
						taunt(Ahrimt, Phrase(AhrimtPhrases));
					}
					NPC Karilt = getSurroundingNPCnames("Karil the Tainted");
					if (Karilt != null)
					{
						taunt(Karilt, Phrase(KariltPhrases));
					}
					NPC Dharok = getSurroundingNPCnames("Dharok the Wretched");
					if (Dharok != null)
					{
						taunt(Dharok, Phrase(DharokPhrases));
					}
					NPC Guthan = getSurroundingNPCnames("Guthan the Infested");
					if (Guthan != null)
					{
						taunt(Guthan, Phrase(GuthanPhrases));
					}
					NPC Toragt = getSurroundingNPCnames("Torag the Corrupted");
					if (Toragt != null)
					{
						taunt(Toragt, Phrase(ToragtPhrases));
					}
					NPC Veract = getSurroundingNPCnames("Verac the Defiled");
					if (Veract != null)
					{
						taunt(Veract, Phrase(VeractPhrases));
					}
					NPC Scurri = getSurroundingNPCnames("Scurrius");
					if (Scurri != null)
					{
						taunt(Scurri, Phrase(ScurriPhrases));
					}
					NPC GiantM = getSurroundingNPCnames("Giant Mole");
					if (GiantM != null)
					{
						taunt(GiantM, Phrase(GiantMPhrases));
					}
					NPC Derang = getSurroundingNPCnames("Deranged Archaeologist");
					if (Derang != null)
					{
						taunt(Derang, Phrase(DerangPhrases));
					}
					NPC Supreme = getSurroundingNPCnames("Dagannoth Supreme");
					if (Supreme != null)
					{
						taunt(Supreme, Phrase(SupremePhrases));
					}
					NPC Rex = getSurroundingNPCnames("Dagannoth Rex");
					if (Rex != null)
					{
						taunt(Rex, Phrase(RexPhrases));
					}
					NPC Prime = getSurroundingNPCnames("Dagannoth Prime");
					if (Prime != null)
					{
						taunt(Prime, Phrase(PrimePhrases));
					}
					NPC Sarach = getSurroundingNPCnames("Sarachnis");
					if (Sarach != null)
					{
						taunt(Sarach, Phrase(SarachPhrases));
					}
					NPC BloodM = getSurroundingNPCnames("Blood Moon");
					if (BloodM != null)
					{
						taunt(BloodM, Phrase(BloodMPhrases));
					}
					NPC BlueMo = getSurroundingNPCnames("Blue Moon");
					if (BlueMo != null)
					{
						taunt(BlueMo, Phrase(BlueMoPhrases));
					}
					NPC Eclips = getSurroundingNPCnames("Eclipse Moon");
					if (Eclips != null)
					{
						taunt(Eclips, Phrase(EclipsPhrases));
					}
					NPC Kalphi = getSurroundingNPCnames("Kalphite Queen");
					if (Kalphi != null)
					{
						taunt(Kalphi, Phrase(KalphiPhrases));
					}
					NPC Kreea = getSurroundingNPCnames("Kree'arra");
					if (Kreea != null)
					{
						taunt(Kreea, Phrase(KreePhrases));
					}
					NPC Comman = getSurroundingNPCnames("Commander Zilyana");
					if (Comman != null)
					{
						taunt(Comman, Phrase(CommanPhrases));
					}
					NPC Genera = getSurroundingNPCnames("General Graardor");
					if (Genera != null)
					{
						taunt(Genera, Phrase(GeneraPhrases));
					}
					NPC KrilT = getSurroundingNPCnames("K'ril Tsutsaroth");
					if (KrilT != null)
					{
						taunt(KrilT, Phrase(KrilPhrases));
					}
					NPC Corpor = getSurroundingNPCnames("Corporeal Beast");
					if (Corpor != null)
					{
						taunt(Corpor, Phrase(CorporPhrases));
					}
					NPC Nex = getSurroundingNPCnames("Nex");
					if (Nex != null)
					{
						taunt(Nex, Phrase(NexPhrases));
					}
					NPC ChaosF = getSurroundingNPCnames("Chaos Fanatic");
					if (ChaosF != null)
					{
						taunt(ChaosF, Phrase(ChaosFPhrases));
					}
					NPC Crazya = getSurroundingNPCnames("Crazy archaeologist");
					if (Crazya != null)
					{
						taunt(Crazya, Phrase(CrazyaPhrases));
					}
					NPC Scorpi = getSurroundingNPCnames("Scorpia");
					if (Scorpi != null)
					{
						taunt(Scorpi, Phrase(ScorpiPhrases));
					}
					NPC KingBl = getSurroundingNPCnames("King Black Dragon");
					if (KingBl != null)
					{
						taunt(KingBl, Phrase(KingBlPhrases));
					}
					NPC ChaosE = getSurroundingNPCnames("Chaos Elemental");
					if (ChaosE != null)
					{
						taunt(ChaosE, Phrase(ChaosEPhrases));
					}
					NPC Revena = getSurroundingNPCnames("Revenant maledictus");
					if (Revena != null)
					{
						taunt(Revena, Phrase(RevenaPhrases));
					}
					NPC Calvar = getSurroundingNPCnames("Calvar'ion");
					if (Calvar != null)
					{
						taunt(Calvar, Phrase(CalvarPhrases));
					}
					NPC Vetio = getSurroundingNPCnames("Vet'ion");
					if (Vetio != null)
					{
						taunt(Vetio, Phrase(VetioPhrases));
					}
					NPC Spinde = getSurroundingNPCnames("Spindel");
					if (Spinde != null)
					{
						taunt(Spinde, Phrase(SpindePhrases));
					}
					NPC Venena = getSurroundingNPCnames("Venenatis");
					if (Venena != null)
					{
						taunt(Venena, Phrase(VenenaPhrases));
					}
					NPC Artio = getSurroundingNPCnames("Artio");
					if (Artio != null)
					{
						taunt(Artio, Phrase(ArtioPhrases));
					}
					NPC Callis = getSurroundingNPCnames("Callisto");
					if (Callis != null)
					{
						taunt(Callis, Phrase(CallisPhrases));
					}
					NPC Zulrah = getSurroundingNPCnames("Zulrah");
					if (Zulrah != null)
					{
						taunt(Zulrah, Phrase(ZulrahPhrases));
					}
					NPC Vorkat = getSurroundingNPCnames("Vorkath");
					if (Vorkat != null)
					{
						taunt(Vorkat, Phrase(VorkatPhrases));
					}
					NPC Phanto = getSurroundingNPCnames("Phantom Muspah");
					if (Phanto != null)
					{
						taunt(Phanto, Phrase(PhantoPhrases));
					}
					NPC TheNight = getSurroundingNPCnames("The Nightmare");
					if (TheNight != null)
					{
						taunt(TheNight, Phrase(TheNightPhrases));
					}
					NPC Phosan = getSurroundingNPCnames("Phosani's Nightmare");
					if (Phosan != null)
					{
						taunt(Phosan, Phrase(PhosanPhrases));
					}
					NPC DukeSu = getSurroundingNPCnames("Duke Sucellus");
					if (DukeSu != null)
					{
						taunt(DukeSu, Phrase(DukeSuPhrases));
					}
					NPC TheLev = getSurroundingNPCnames("The Leviathan");
					if (TheLev != null)
					{
						taunt(TheLev, Phrase(TheLevPhrases));
					}
					NPC TheWhi = getSurroundingNPCnames("The Whisperer");
					if (TheWhi != null)
					{
						taunt(TheWhi, Phrase(TheWhiPhrases));
					}
					NPC Vardor = getSurroundingNPCnames("Vardorvis");
					if (Vardor != null)
					{
						taunt(Vardor, Phrase(VardorPhrases));
					}
					NPC Obor = getSurroundingNPCnames("Obor");
					if (Obor != null)
					{
						taunt(Obor, Phrase(OborPhrases));
					}
					NPC Bryoph = getSurroundingNPCnames("Bryophyta");
					if (Bryoph != null)
					{
						taunt(Bryoph, Phrase(BryophPhrases));
					}
					NPC TheMim = getSurroundingNPCnames("The Mimic");
					if (TheMim != null)
					{
						taunt(TheMim, Phrase(TheMimPhrases));
					}
					NPC Hespor = getSurroundingNPCnames("Hespori");
					if (Hespor != null)
					{
						taunt(Hespor, Phrase(HesporPhrases));
					}
					NPC Skotiz = getSurroundingNPCnames("Skotizo");
					if (Skotiz != null)
					{
						taunt(Skotiz, Phrase(SkotizPhrases));
					}
					NPC Grotes = getSurroundingNPCnames("Dawn");
					if (Grotes != null)
					{
						taunt(Grotes, Phrase(GrotesDaPhrases));
					}
					NPC Dusk = getSurroundingNPCnames("Dusk");
					if (Dusk != null)
					{
						taunt(Dusk, Phrase(GrotesDuPhrases));
					}
					NPC Abyssa = getSurroundingNPCnames("Abyssal Sire");
					if (Abyssa != null)
					{
						taunt(Abyssa, Phrase(AbyssaPhrases));
					}
					NPC Kraken = getSurroundingNPCnames("Kraken");
					if (Kraken != null)
					{
						taunt(Kraken, Phrase(KrakenPhrases));
					}
					NPC Cerber = getSurroundingNPCnames("Cerberus");
					if (Cerber != null)
					{
						taunt(Cerber, Phrase(CerberPhrases));
					}
					NPC Thermo = getSurroundingNPCnames("Thermonuclear smoke devil");
					if (Thermo != null)
					{
						taunt(Thermo, Phrase(ThermoPhrases));
					}
					NPC Alchem = getSurroundingNPCnames("Alchemical Hydra");
					if (Alchem != null)
					{
						taunt(Alchem, Phrase(AlchemPhrases));
					}
					NPC Crysta = getSurroundingNPCnames("Crystalline Hunllef");
					if (Crysta != null)
					{
						taunt(Crysta, Phrase(HunPhrases));
					}
					NPC Corrup = getSurroundingNPCnames("Corrupted Hunllef");
					if (Corrup != null)
					{
						taunt(Corrup, Phrase(HunPhrases));
					}
					NPC TzTok = getSurroundingNPCnames("TzTok-Jad");
					if (TzTok != null)
					{
						taunt(TzTok, Phrase(TzTokPhrases));
					}
					NPC TzKal = getSurroundingNPCnames("TzKal-Zuk");
					if (TzKal != null)
					{
						taunt(TzKal, Phrase(TzKalPhrases));
					}
					NPC Winter = getSurroundingNPCnames("Wintertodt");
					if (Winter != null)
					{
						taunt(Winter, Phrase(WinterPhrases));
					}
					NPC Zalcan = getSurroundingNPCnames("Zalcano");
					if (Zalcan != null)
					{
						taunt(Zalcan, Phrase(ZalcanPhrases));
					}
					NPC Tekton = getSurroundingNPCnames("Tekton");
					if (Tekton != null)
					{
						taunt(Tekton, Phrase(TektonPhrases));
					}
					NPC Vangua = getSurroundingNPCnames("Vanguard");
					if (Vangua != null)
					{
						taunt(Vangua, Phrase(VanguaPhrases));
					}
					NPC Vespul = getSurroundingNPCnames("Vespula");
					if (Vespul != null)
					{
						taunt(Vespul, Phrase(VespulPhrases));
					}
					NPC VasaNi = getSurroundingNPCnames("Vasa Nistirio");
					if (VasaNi != null)
					{
						taunt(VasaNi, Phrase(VasaNiPhrases));
					}
					NPC Muttad = getSurroundingNPCnames("Muttadile");
					if (Muttad != null)
					{
						taunt(Muttad, Phrase(MuttadPhrases));
					}
					NPC GreatO = getSurroundingNPCnames("Great Olm");
					if (GreatO != null)
					{
						taunt(GreatO, Phrase(GreatOPhrases));
					}
					NPC TheMai = getSurroundingNPCnames("The Maiden of Sugadinti");
					if (TheMai != null)
					{
						taunt(TheMai, Phrase(TheMaiPhrases));
					}
					NPC Pestil = getSurroundingNPCnames("Pestilent Bloat");
					if (Pestil != null)
					{
						taunt(Pestil, Phrase(PestilPhrases));
					}
					NPC Nyloca = getSurroundingNPCnames("Nylocas Vasilias");
					if (Nyloca != null)
					{
						taunt(Nyloca, Phrase(NylocaPhrases));
					}
					NPC Sotets = getSurroundingNPCnames("Sotetseg");
					if (Sotets != null)
					{
						taunt(Sotets, Phrase(SotetsPhrases));
					}
					NPC Xarpus = getSurroundingNPCnames("Xarpus");
					if (Xarpus != null)
					{
						taunt(Xarpus, Phrase(XarpusPhrases));
					}
					NPC Verzik = getSurroundingNPCnames("Verzik Vitur");
					if (Verzik != null)
					{
						taunt(Verzik, Phrase(VerzikPhrases));
					}
					NPC Akkha = getSurroundingNPCnames("Akkha");
					if (Akkha != null)
					{
						taunt(Akkha, Phrase(AkkhaPhrases));
					}
					NPC BaBa = getSurroundingNPCnames("Ba-Ba");
					if (BaBa != null)
					{
						taunt(BaBa, Phrase(BaBaPhrases));
					}
					NPC Kephri = getSurroundingNPCnames("Kephri");
					if (Kephri != null)
					{
						taunt(Kephri, Phrase(KephriPhrases));
					}
					NPC Zebak = getSurroundingNPCnames("Zebak");
					if (Zebak != null)
					{
						taunt(Zebak, Phrase(ZebakPhrases));
					}
					NPC Tumeke = getSurroundingNPCnames("Tumeken's Warden");
					if (Tumeke != null)
					{
						taunt(Tumeke, Phrase(TumekePhrases));
					}
					NPC Elidin = getSurroundingNPCnames("Elidinis' Warden");
					if (Elidin != null)
					{
						taunt(Elidin, Phrase(ElidinPhrases));
					}
					NPC Guntho = getSurroundingNPCnames("Gunthor the brave");
					if (Guntho != null)
					{
						taunt(Guntho, Phrase(GunthoPhrases));
					}
				}
				if (config.pktaunts())
				{
					int i = 0;
					while (i<SurroundingNPCS.size())
					{
						taunt(SurroundingNPCS.get(i),Phrase(PKTaunts));
						i++;
					}
				}
				if (config.Pleae())
				{
					client.getLocalPlayer().setOverheadCycle(200);
					client.getLocalPlayer().setOverheadText("Pleae");
					if (config.dialogbox()){
						String chatMessage = new ChatMessageBuilder()
								.append(getPublicChatMessageColor(),"Pleae")
								.build();

						chatMessageManager.queue(QueuedMessage.builder()
								.type(ChatMessageType.PUBLICCHAT)
								.sender(client.getLocalPlayer().getName())
								.name(client.getLocalPlayer().getName())
								.runeLiteFormattedMessage(chatMessage)
								.build());
					}
				}
				SurroundingNPCS.clear();
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		SurroundingNPCS.add(event.getNpc());
		if (event.getNpc().getName() == null) { return;}
		if (event.getNpc().getName().equals("TzTok-Jad"))
		{
			Jad  = true;
		}
		if (event.getNpc().getName().equals("TzKal-Zuk"))
		{
			Zuk = true;
		}

		String taunt = null;

		if (Died == false)
		{
			if (event.getNpc().getName().equals("Hatius Cosaintus"))
			{
                taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Khaled));
				event.getNpc().setOverheadCycle(300);
				event.getNpc().setOverheadText(taunt);
			}
			return;
		}
		if (Recentlycommented.contains(event.getNpc().getId())) { return;}
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
			if ( r.nextInt(100)>30){
			taunt = Phrase(Arrays.asList(NPCTauntsPhrases.Banker));
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			}
			if ( r.nextInt(100)>60){
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
		if (event.getNpc().getName().equals("Nieve"))
		{
			taunt = Phrase(NievePhrases);
			event.getNpc().setOverheadCycle(300);
			event.getNpc().setOverheadText(taunt);
			Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Steve"))
		{
		taunt = Phrase(StevePhrases);
		event.getNpc().setOverheadCycle(300);
		event.getNpc().setOverheadText(taunt);
		Recentlycommented.add(event.getNpc().getId());
		}
		if (event.getNpc().getName().equals("Osmumten") && getRegionId() == TOAREWARD_REGION_ID &&config.Loottaunts())
		{
			ToApurpleCheck();
			if (sarcophagusIsPurple){
				if(purpleIsMine){
					taunt = Phrase(OsmuYourPurplePhrases);
				}
				else {
					taunt = Phrase(OsmuFriendPurplePhrases);
				}
			} else {
				taunt = Phrase(OsmuNoPurplePhrases);
			}
			event.getNpc().setOverheadCycle(500);
			event.getNpc().setOverheadText(taunt);
		}
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
		if (taunt !=null){
			Sendchatmessage(event.getNpc(), taunt);
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		SurroundingNPCS.remove(event.getNpc());
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
			SelectedModifier();
		}
	}

	@Provides
	NPCTauntsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NPCTauntsConfig.class);
	}

	private void taunt(NPC npc, String taunt)
	{
		npc.setOverheadCycle(300);
		npc.setOverheadText(taunt);
		Sendchatmessage(npc, taunt);
	}
	private void Sendchatmessage(NPC npc, String taunt)
	{
		if (config.dialogbox()){
			String chatMessage = new ChatMessageBuilder()
					.append(getPublicChatMessageColor(),taunt)
					.build();

			chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.PUBLICCHAT)
					.sender(npc.getName())
					.name(npc.getName())
					.runeLiteFormattedMessage(chatMessage)
					.build());
		}
		if (config.texttospeech())
		{
			OverheadTextChanged chatmessage1 = new OverheadTextChanged(npc,taunt);
			eventBus.post(chatmessage1);
		}
	}

	private Color getPublicChatUsernameColor()
	{
		boolean isChatboxTransparent = client.isResized() && client.getVar(Varbits.TRANSPARENT_CHATBOX) == 1;
		Color usernameColor;

		if (isChatboxTransparent)
		{
			usernameColor = Color.WHITE; //default - is missing from JagexColors

			if (chatColorConfig.transparentPlayerUsername() != null)
			{
				usernameColor = chatColorConfig.transparentPlayerUsername();
			}
		}
		else
		{
			usernameColor = Color.BLACK; //default - is missing from JagexColors

			if (chatColorConfig.opaquePlayerUsername() != null)
			{
				usernameColor = chatColorConfig.opaquePlayerUsername();
			}
		}
		return usernameColor;
	}
	private Color getPublicChatMessageColor()
	{
		boolean isChatboxTransparent = client.isResized() && client.getVar(Varbits.TRANSPARENT_CHATBOX) == 1;
		Color messageColor;


		if (isChatboxTransparent)
		{
			messageColor = JagexColors.CHAT_PUBLIC_TEXT_TRANSPARENT_BACKGROUND;//default

			if (chatColorConfig.transparentPublicChat() != null)
			{
				messageColor = chatColorConfig.transparentPublicChat();
			}
		}
		else
		{

			messageColor = JagexColors.CHAT_PUBLIC_TEXT_OPAQUE_BACKGROUND;//default

			if (chatColorConfig.opaquePublicChat() != null)
			{
				messageColor = chatColorConfig.opaquePublicChat();
			}
		}
		return messageColor;
	}
	private NPC getSurroundingNPCnames(String name)
	{
		int i = 0;
		while (i<SurroundingNPCS.size()){
			if (SurroundingNPCS.get(i).getName().equals(name))
			{
				return SurroundingNPCS.get(i);
			}
			i++;
		}
		return null;
	}
	private boolean getDrops(String name)
	{
		int i = 0;
		while (i<SurroundingNPCS.size()){
			if (SurroundingNPCS.get(i).getName().contains(name))
			{
				return true;
			}
			i++;
		}
		return false;
	}
	private void ToApurpleCheck()
	{
		sarcophagusIsPurple = client.getVarbitValue(VARBIT_ID_SARCOPHAGUS) % 2 != 0;
		purpleIsMine = true;

		for (final int varbitId : VARBIT_MULTILOC_IDS_CHEST)
		{
			if (client.getVarbitValue(varbitId) == VARBIT_VALUE_CHEST_KEY)
			{
				purpleIsMine = false;
				break;
			}
		}
	}
}

