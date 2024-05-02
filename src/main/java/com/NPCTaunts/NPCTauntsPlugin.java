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
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.Text;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.api.MessageNode;
import net.runelite.api.events.GameStateChanged;


@Slf4j
@PluginDescriptor(
        name = "NPCTaunts"
)
public class NPCTauntsPlugin extends Plugin {
    static final int FIGHT_CAVE_REGION = 9551;
    static final int INFERNO_REGION = 9043;
    static final int COLOSSEUM_REGION = 7216;
    private static final int ZULRAH_SPAWN_REGION_ID = 9007;
    private static final int ZULRAH_REGION_ID = 9008;
    private static final List<Integer> ZulRegionIDs = Arrays.asList(ZULRAH_SPAWN_REGION_ID, ZULRAH_REGION_ID);
    private static final List<String> Gauntletminions = Arrays.asList("Crystalline Bat", "Crystalline Rat", "Crystalline Spider", "Corrupted Rat", "Corrupted Spider", "Corrupted Bat");
    private static final int[] VARBIT_MULTILOC_IDS_CHEST = new int[]{
            14356, 14357, 14358, 14359, 14360, 14370, 14371, 14372
    };
    private static final int VARBIT_VALUE_CHEST_KEY = 2;
    private static final int VARBIT_ID_SARCOPHAGUS = 14373;
    private static final int VARBIT_MODIFIER_SELECTED = 9788;
    private static final int SCRIPT_MODIFIER_SELECT_INIT = 4931;
    private final List<Integer> modifierOptions = new ArrayList<>(3);
    private final List<String> modifiers = Arrays.asList("Mantimayhem", "Reentry", "Bees!", "Volatility", "Blasphemy", "Relentless", "Quartet", "Totemic", "Doom", "Dynamic Duo", "Solarflare", "Myopia", "Frailty", "Red Flag");
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
    private boolean Jad;
    private boolean Zuk;
    private boolean Sol;
    private int TOAREWARD_REGION_ID = 14672;
    private boolean prehunleffminion;
    private boolean pvpdeath;
    private boolean sarcophagusIsPurple;
    private boolean purpleIsMine;
    private int gauntletdropamount;
    private List<String> PKTaunts = Arrays.asList("Sit rat", "Smited", "Back to lumby", "Stick to F2p nub", "Sit ******* ******", "Cya in Lumby", "L000000000L", "Sit", "Rat", "TY");
    private List<Integer> Recentlycommented = new ArrayList<>();
    //Prevents minimus from commenting during waves
    private List<Integer> Blacklist = new ArrayList<>(12808);
    private List<NPC> SurroundingNPCS = new ArrayList<>();
    private List<String> DropsReceived = new ArrayList<>();
    private String colotitle = "";
    private int wave;
    private int Modifierselected = -1;

    @Override
    protected void startUp() throws Exception {
        log.info("NPC Taunts started!");
        if (config.exlusivecustomtaunts()) {
            NPCTauntsNPClist.bosslist.clear();
            NPCTauntsNPClist.npclist.clear();
        } else {
            NPCTauntsNPClist.loadbosslist();
            NPCTauntsNPClist.loadnpclist();
        }
        NPCTauntsNPClist.loadcustomlists(config);
    }

    @Override
    protected void shutDown() throws Exception {
        Forget();
        Modifierselected = -1;
        colotitle = "";
        wave = 0;
        log.info("NPC Taunts stopped!");
    }


    private int getRegionId() {
        Player player = client.getLocalPlayer();
        if (player == null) {
            return -1;
        }
        return WorldPoint.fromLocalInstance(client, player.getLocalLocation()).getRegionID();
    }

    private String Phrase(List<String> p) {
        int rand = r.nextInt(p.size());
        int b = 0;
        while (p.get(rand).contains("{Enemyname}") && Killer == null && b < 4) {
            rand = r.nextInt(p.size());
            b++;
        }
        String s = p.get(rand);
        if (s.contains("{Enemyname}")) {
            if (b < 4) {
                String killername = Killer;
                s = s.replace("{Enemyname}", killername);
            } else {
                s = s.replace("{Enemyname}", "mighty foe");

            }
        }
        if (s.contains("{Playername}")) {
            String playername = (String) client.getLocalPlayer().getName();
            s = s.replace("{Playername}", playername);
        }
        if (s.contains("{Title}")) {
            if (!colotitle.matches("")) {
                s = s.replace("{Title}", colotitle);
            } else {
                s = s.replace("{Title}", "colosseum enjoyer");
            }
        }
        if (s.contains("{Wave}")) {
            String Wave = Integer.toString(wave);
            s = s.replace("{Wave}", Wave);
        }
        if (s.contains("{Mod}")) {
            if (Modifierselected == -1) {
                s = s.replace("{Mod}", "That last modifier");
                ;
            } else {
                String mod = modifiers.get(Modifierselected);
                s = s.replace("{Mod}", mod);
            }
        }
        if (s.contains("{Lasthit}")) {
            String lasthit = Integer.toString(lastdamagetaken);
            s = s.replace("{Lasthit}", lasthit);
        }
        return s;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (Died == false || ForgetTime == null) {
            return;
        }
        if (Instant.now().compareTo(ForgetTime) >= 0) {
            Forget();
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks
        if (chatMsg.startsWith("Minimus: A") && (chatMsg.contains("approaches!"))) {
            String colosseumtitle = chatMsg.replace("Minimus: A ", "").replace(" approaches!", "");
            colotitle = colosseumtitle;
        }
        if (chatMsg.startsWith("Wave:")) {
            wave = Integer.valueOf(chatMsg.replace("Wave: ", ""));
        }
        if ((chatMsg.contains(client.getLocalPlayer().getName() + " received a drop:")
                || chatMsg.contains("Valuable drop:")
                || chatMsg.contains("Untradeable drop:"))
                && getRegionId() == 12127) {//&& !Died put this back after testing
            NPC Brynn = getSurroundingNPCnames("Bryn");
            DropsReceived.add(chatMsg);
            if (DropsReceived.size() >= gauntletdropamount) {
                if (getDrops("Enhanced")) {
                    taunt(Brynn, Phrase(Arrays.asList(NPCTauntsPhrases.BrynnEnhanced)));
                } else if (getDrops("weapon seed")) {
                    taunt(Brynn, Phrase(Arrays.asList(NPCTauntsPhrases.BrynnWeaponseed)));
                } else if (getDrops("armour seed")) {
                    taunt(Brynn, Phrase(Arrays.asList(NPCTauntsPhrases.BrynnArmorseed)));
                } else {
                    taunt(Brynn, Phrase(Arrays.asList(NPCTauntsPhrases.BrynnNoUniq)));
                }
                DropsReceived.clear();
            }
        }


        messageNode = event.getMessageNode();
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired pre) {
        if (pre.getScriptId() == SCRIPT_MODIFIER_SELECT_INIT) {
            modifierOptions.clear();
            Object[] args = pre.getScriptEvent().getArguments();
            modifierOptions.add((Integer) args[2]);
            modifierOptions.add((Integer) args[3]);
            modifierOptions.add((Integer) args[4]);
        }
    }

    private void SelectedModifier() {
        if (modifierOptions.isEmpty()) {
            log.debug("Modifier options were not loaded");
            return;
        }

        int selectedoption = client.getVarbitValue(VARBIT_MODIFIER_SELECTED);
        if (selectedoption == 0) {
            log.debug("Modifier selected varbit = 0");
            return;
        }

        Modifierselected = modifierOptions.get(selectedoption - 1);
        modifierOptions.clear();
        if (Modifierselected == -1) {
            log.debug("Failed to select modifier");
            ;
        }
    }


    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        Actor opponent;
        if (event.getSource().equals(client.getLocalPlayer())) {
            opponent = event.getTarget();
            if (event.getTarget() instanceof Player) {
                pvpactive = true;
            } else {
                pvpactive = false;
            }

        } else if (event.getTarget() == null) {
            return;
        } else if (event.getTarget().equals(client.getLocalPlayer())) {
            opponent = event.getSource();
            if (event.getSource() instanceof Player) {
                pvpactive = true;
            } else {
                pvpactive = false;
            }
        } else // if neither source or target was the player, skip
        {
            return;
        }
        if (opponent != null) {
            lastOpponent = opponent.getName();
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor actor = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        if (actor instanceof Player) {
            Player player = (Player) actor;

            if (player == client.getLocalPlayer()) {
                lastdamagetaken = hitsplat.getAmount();
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        final int Hunleff = client.getVarbitValue(9177);
        if (Hunleff == 1) {
            if (getSurroundingNPCnames("Crystalline Hunllef") != null) {
                gauntletdropamount = 3;
            }
            if (getSurroundingNPCnames("Corrupted Hunllef") != null) {
                gauntletdropamount = 4;
            }
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath event) {
        Actor actor = event.getActor();
        if (actor instanceof Player) {
            Player player = (Player) actor;

            if (player == client.getLocalPlayer()) {
                Died = true;
                Killer = lastOpponent;
                finaldamagetaken = lastdamagetaken;
                lastOpponent = null;
                Recentlycommented.clear();
                final Duration forgetduration = Duration.ofMinutes(config.ForgetTimeDelay());
                ForgetTime = Instant.now().plus(forgetduration);
                prehunleffminion = false;

                pvpdeath = false;

                //Zulrah
                final int regionId = getRegionId();
                if (ZulRegionIDs.contains(regionId)) {
                    if (!NPCTauntsNPClist.npclist.contains(new NPCTauntsNPC("Priestess Zul-Gwenwynig", NPCTauntsPhrases.Zul))) {
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Priestess Zul-Gwenwynig", NPCTauntsPhrases.Zul));
                    }
                } else {
                    int T = NPCTauntsNPClist.npclistcontains("Priestess Zul-Gwenwynig");
                    while (T > 0) {
                        NPCTauntsNPClist.npclist.remove(T);
                        T = NPCTauntsNPClist.npclistcontains("Priestess Zul-Gwenwynig");
                    }
                }

                //Fightcaves
                if (regionId == FIGHT_CAVE_REGION) {
                    if (Jad) {
                        int T = NPCTauntsNPClist.npclistcontains("TzHaar-Mej-Jal");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("TzHaar-Mej-Jal");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("TzHaar-Mej-Jal", NPCTauntsPhrases.Jad));
                    } else {
                        int T = NPCTauntsNPClist.npclistcontains("TzHaar-Mej-Jal");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("TzHaar-Mej-Jal");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("TzHaar-Mej-Jal", NPCTauntsPhrases.PreJad));
                    }
                }
                //Inferno
                if (regionId == INFERNO_REGION) {
                    if (Zuk) {
                        int T = NPCTauntsNPClist.npclistcontains("TzHaar-Ket-Keh");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("TzHaar-Ket-Keh");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("TzHaar-Ket-Kehl", NPCTauntsPhrases.Zuk));
                    } else {
                        int T = NPCTauntsNPClist.npclistcontains("TzHaar-Ket-Keh");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("TzHaar-Ket-Keh");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("TzHaar-Ket-Kehl", NPCTauntsPhrases.PreZuk));
                    }
                }
                if (regionId == COLOSSEUM_REGION) {
                    if (wave == 12) {
                        int T = NPCTauntsNPClist.npclistcontains("Minimus");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("Minimus");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Minimus", NPCTauntsPhrases.Sol));
                    } else {
                        int T = NPCTauntsNPClist.npclistcontains("Minimus");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("Minimus");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Minimus", NPCTauntsPhrases.PreSol));
                    }
                }

                //gauntlet maze
                final int GauntletMaze = client.getVarbitValue(9178);
                if (GauntletMaze == 1) {
                    if (Killer != null) {
                        int i = 0;
                        while (i < Gauntletminions.size()) {
                            String min = Gauntletminions.get(i);
                            if (min.contains(Killer)) {
                                prehunleffminion = true;
                                int T = NPCTauntsNPClist.npclistcontains("Bryn");
                                while (T > 0) {
                                    NPCTauntsNPClist.npclist.remove(T);
                                    T = NPCTauntsNPClist.npclistcontains("Bryn");
                                }
                                NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Bryn", NPCTauntsPhrases.Brynrsb));
                                log.debug("You died to a rat/bat/spoder");
                                return;
                            }
                            i++;
                        }
                    }
                    if (prehunleffminion == false) {
                        int T = NPCTauntsNPClist.npclistcontains("Bryn");
                        while (T > 0) {
                            NPCTauntsNPClist.npclist.remove(T);
                            T = NPCTauntsNPClist.npclistcontains("Bryn");
                        }
                        NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Bryn", NPCTauntsPhrases.Brynph));
                    }
                }
                //gauntlet boss
                final int Hunleff = client.getVarbitValue(9177);
                if (Hunleff == 1) {
                    int T = NPCTauntsNPClist.npclistcontains("Bryn");
                    while (T > 0) {
                        NPCTauntsNPClist.npclist.remove(T);
                        T = NPCTauntsNPClist.npclistcontains("Bryn");
                    }
                    NPCTauntsNPClist.npclist.add(new NPCTauntsNPC("Bryn", NPCTauntsPhrases.Bryn));
                }

                if (pvpactive) {
                    pvpdeath = true;
                }

                //triggers boss taunts
                if (config.Bosstaunts() && !config.pktaunts()) {
                    for (NPC npc : SurroundingNPCS) {
                        int bossid = NPCTauntsNPClist.bosslistcontains(npc.getName());
                        if (bossid != -1) {
                            bosstaunt(npc, NPCTauntsNPClist.bosslist.get(bossid));
                        }
                    }
                }

                if (config.pktaunts()) {
                    for (NPC npc : SurroundingNPCS) {
                        taunt(npc, Phrase(PKTaunts));
                    }
                }

                if (config.Pleae()) {
                    client.getLocalPlayer().setOverheadCycle(200);
                    client.getLocalPlayer().setOverheadText("Pleae");
                    if (config.dialogbox()) {
                        String chatMessage = new ChatMessageBuilder()
                                .append(getPublicChatMessageColor(), "Pleae")
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
    public void onNpcSpawned(NpcSpawned event) {
        SurroundingNPCS.add(event.getNpc());
        if (event.getNpc().getName() == null) {
            return;
        }
        if (event.getNpc().getName().equals("TzTok-Jad")) {
            Jad = true;

        }
        if (event.getNpc().getName().equals("TzKal-Zuk")) {
            Zuk = true;
        }

        String taunt = null;

        if (Died == false) {
            if (event.getNpc().getName().equals("Hatius Cosaintus")&& (config.ShutHaitus()==false)) {
                taunt(event.getNpc(), Phrase(Arrays.asList(NPCTauntsPhrases.Khaled)));
            }
            return;
        }
        if (Recentlycommented.contains(event.getNpc().getId())) {
            return;
        }
        if (Blacklist.contains(event.getNpc().getId())) {
            return;
        }
        if (event.getNpc().getName().equals("Banker")) {
            if (r.nextInt(100) > 50) return;
        }
        int npclistid = NPCTauntsNPClist.npclistcontains(event.getNpc().getName());
        if (npclistid != -1) {
            npctaunt(event.getNpc(), NPCTauntsNPClist.npclist.get(npclistid));
        }

        if (event.getNpc().getName().equals("Osmumten") && getRegionId() == TOAREWARD_REGION_ID && config.Loottaunts()) {
            ToApurpleCheck();
            if (sarcophagusIsPurple) {
                if (purpleIsMine) {
                    taunt = Phrase(Arrays.asList(NPCTauntsPhrases.OsmuYourPurple));
                } else {
                    taunt = Phrase(Arrays.asList(NPCTauntsPhrases.OsmuFriendPurple));
                }
            } else {
                taunt = Phrase(Arrays.asList(NPCTauntsPhrases.OsmuNoPurple));
            }
            event.getNpc().setOverheadCycle(500);
            event.getNpc().setOverheadText(taunt);
            Sendchatmessage(event.getNpc(), taunt);
        }
        if (finaldamagetaken == 69) {
            if (r.nextInt(100) > 50) {
                taunt(event.getNpc(), Phrase(Arrays.asList(NPCTauntsPhrases.SixNine)));
                Recentlycommented.add(event.getNpc().getId());
            }
        }
        if (finaldamagetaken == 73) {
            if (r.nextInt(100) > 70) {
                taunt(event.getNpc(), Phrase(Arrays.asList(NPCTauntsPhrases.SevenThree)));
                Recentlycommented.add(event.getNpc().getId());
            }
        }
        if (finaldamagetaken == 1) {
            if (r.nextInt(100) > 90) {
                taunt(event.getNpc(), Phrase(Arrays.asList(NPCTauntsPhrases.One)));
                Recentlycommented.add(event.getNpc().getId());
            }
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        SurroundingNPCS.remove(event.getNpc());
        if (event.getNpc().getName() == null) {
            return;
        }
        if (event.getNpc().getName().equals("TzTok-Jad")) {
            Jad = false;
        }
        if (event.getNpc().getName().equals("TzKal-Zuk")) {
            Zuk = false;
        }
        final int regionId = getRegionId();
        if (regionId == COLOSSEUM_REGION && event.getNpc().getName().equals("Minimus")) {
            SelectedModifier();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        //Forget on logout
        if (gameStateChanged.getGameState().getState() < GameState.LOADING.getState()) {
            Forget();
        }
    }

    @Provides
    NPCTauntsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(NPCTauntsConfig.class);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (config.exlusivecustomtaunts()) {
            NPCTauntsNPClist.bosslist.clear();
            NPCTauntsNPClist.npclist.clear();
        } else {
            NPCTauntsNPClist.loadbosslist();
            NPCTauntsNPClist.loadnpclist();
        }
        NPCTauntsNPClist.loadcustomlists(config);
    }

    private void bosstaunt(NPC npc, NPCTauntsNPC tauntNPC) {
        taunt(npc, Phrase(tauntNPC.phrases));
    }

    private void npctaunt(NPC npc, NPCTauntsNPC tauntNPC) {
        taunt(npc, Phrase(tauntNPC.phrases));
        Recentlycommented.add(npc.getId());
    }

    private void taunt(NPC npc, String taunt) {
        npc.setOverheadCycle(300);
        npc.setOverheadText(taunt);
        Sendchatmessage(npc, taunt);
    }

    private void Sendchatmessage(NPC npc, String taunt) {
        if (config.dialogbox()) {
            String chatMessage = new ChatMessageBuilder()
                    .append(getPublicChatMessageColor(), taunt)
                    .build();

            chatMessageManager.queue(QueuedMessage.builder()
                    .type(ChatMessageType.PUBLICCHAT)
                    .sender(npc.getName())
                    .name(npc.getName())
                    .runeLiteFormattedMessage(chatMessage)
                    .build());
        }
        if (config.texttospeech()) {
            OverheadTextChanged chatmessage1 = new OverheadTextChanged(npc, taunt);
            eventBus.post(chatmessage1);
        }
    }

    private Color getPublicChatMessageColor() {
        boolean isChatboxTransparent = client.isResized() && client.getVarbitValue(Varbits.TRANSPARENT_CHATBOX) == 1;
        Color messageColor;


        if (isChatboxTransparent) {
            messageColor = JagexColors.CHAT_PUBLIC_TEXT_TRANSPARENT_BACKGROUND;//default

            if (chatColorConfig.transparentPublicChat() != null) {
                messageColor = chatColorConfig.transparentPublicChat();
            }
        } else {

            messageColor = JagexColors.CHAT_PUBLIC_TEXT_OPAQUE_BACKGROUND;//default

            if (chatColorConfig.opaquePublicChat() != null) {
                messageColor = chatColorConfig.opaquePublicChat();
            }
        }
        return messageColor;
    }

    private NPC getSurroundingNPCnames(String name) {
        NPC npc = null;
        for (int i = 0; i < SurroundingNPCS.size(); i++) {
            if (SurroundingNPCS.get(i).getName().equals(name)) {
                npc = SurroundingNPCS.get(i);
            }
        }
        return npc;
    }

    private boolean getDrops(String name) {
        boolean bool = false;
        for (int i = 0; i < DropsReceived.size(); i++) {

            if (DropsReceived.get(i).contains(name)) {
                bool = true;
            }
        }
        return bool;
    }

    private void ToApurpleCheck() {
        sarcophagusIsPurple = client.getVarbitValue(VARBIT_ID_SARCOPHAGUS) % 2 != 0;
        purpleIsMine = true;

        for (final int varbitId : VARBIT_MULTILOC_IDS_CHEST) {
            if (client.getVarbitValue(varbitId) == VARBIT_VALUE_CHEST_KEY) {
                purpleIsMine = false;
                break;
            }
        }
    }

    private void Forget() {
        Died = false;
        Recentlycommented.clear();
        lastOpponent = null;
        Killer = null;
        lastdamagetaken = 0;
        finaldamagetaken = 0;
    }

}

