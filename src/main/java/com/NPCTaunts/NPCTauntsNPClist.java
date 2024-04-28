package com.NPCTaunts;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NPCTauntsNPClist {

    @Inject
    private NPCTauntsConfig config;
    public static List<NPCTauntsNPC> bosslist = new ArrayList<>();
    public static List<NPCTauntsNPC> npclist = new ArrayList<>();



    public static void loadnpclist()
    {

        npclist.add(new NPCTauntsNPC("Hans",NPCTauntsPhrases.Hans));
        npclist.add(new NPCTauntsNPC("Hatius Cosaintus",NPCTauntsPhrases.Haitus));
        npclist.add(new NPCTauntsNPC("Vaire",NPCTauntsPhrases.Priff));
        npclist.add(new NPCTauntsNPC("Banker",NPCTauntsPhrases.Banker));
        npclist.add(new NPCTauntsNPC("Squire",NPCTauntsPhrases.Squire));
        npclist.add(new NPCTauntsNPC("Ferox",NPCTauntsPhrases.Ferox));
        npclist.add(new NPCTauntsNPC("The 'Wedge'",NPCTauntsPhrases.Wedge));
        npclist.add(new NPCTauntsNPC("Elise",NPCTauntsPhrases.Elise));
        npclist.add(new NPCTauntsNPC("Town Crier",NPCTauntsPhrases.Towncrier));
        npclist.add(new NPCTauntsNPC("Nieve",NPCTauntsPhrases.Nieve));
        npclist.add(new NPCTauntsNPC("Steve",NPCTauntsPhrases.Steve));

    }

    public static void loadbosslist()
    {

        bosslist.add(new NPCTauntsNPC("Ahrim the Blighted",NPCTauntsPhrases.Ahrimt));
        bosslist.add(new NPCTauntsNPC("Karil the Tainted",NPCTauntsPhrases.Karilt));
        bosslist.add(new NPCTauntsNPC("Dharok the Wretched",NPCTauntsPhrases.Dharok));
        bosslist.add(new NPCTauntsNPC("Guthan the Infested",NPCTauntsPhrases.Guthan));
        bosslist.add(new NPCTauntsNPC("Torag the Corrupted",NPCTauntsPhrases.Toragt));
        bosslist.add(new NPCTauntsNPC("Verac the Defiled",NPCTauntsPhrases.Veract));
        bosslist.add(new NPCTauntsNPC("Scurrius",NPCTauntsPhrases.Scurri));
        bosslist.add(new NPCTauntsNPC("Giant Mole",NPCTauntsPhrases.GiantM));
        bosslist.add(new NPCTauntsNPC("Deranged Archaeologist",NPCTauntsPhrases.Derang));
        bosslist.add(new NPCTauntsNPC("Dagannoth Supreme",NPCTauntsPhrases.Supreme));
        bosslist.add(new NPCTauntsNPC("Dagannoth Rex",NPCTauntsPhrases.Rex));
        bosslist.add(new NPCTauntsNPC("Dagannoth Prime",NPCTauntsPhrases.Prime));
        bosslist.add(new NPCTauntsNPC("Sarachnis",NPCTauntsPhrases.Sarach));
        bosslist.add(new NPCTauntsNPC("Blood Moon",NPCTauntsPhrases.BloodM));
        bosslist.add(new NPCTauntsNPC("Blue Moon",NPCTauntsPhrases.BlueMo));
        bosslist.add(new NPCTauntsNPC("Eclipse Moon",NPCTauntsPhrases.Eclips));
        bosslist.add(new NPCTauntsNPC("Kalphite Queen",NPCTauntsPhrases.Kalphi));
        bosslist.add(new NPCTauntsNPC("Kree'arra",NPCTauntsPhrases.Kree));
        bosslist.add(new NPCTauntsNPC("Commander Zilyana",NPCTauntsPhrases.Comman));
        bosslist.add(new NPCTauntsNPC("General Graardor",NPCTauntsPhrases.Genera));
        bosslist.add(new NPCTauntsNPC("K'ril Tsutsaroth",NPCTauntsPhrases.Kril));
        bosslist.add(new NPCTauntsNPC("Corporeal Beast",NPCTauntsPhrases.Corpor));
        bosslist.add(new NPCTauntsNPC("Nex",NPCTauntsPhrases.Nex));
        bosslist.add(new NPCTauntsNPC("Chaos Fanatic",NPCTauntsPhrases.ChaosF));
        bosslist.add(new NPCTauntsNPC("Crazy archaeologist",NPCTauntsPhrases.Crazya));
        bosslist.add(new NPCTauntsNPC("Scorpia",NPCTauntsPhrases.Scorpi));
        bosslist.add(new NPCTauntsNPC("King Black Dragon",NPCTauntsPhrases.KingBl));
        bosslist.add(new NPCTauntsNPC("Chaos Elemental",NPCTauntsPhrases.ChaosE));
        bosslist.add(new NPCTauntsNPC("Revenant maledictus",NPCTauntsPhrases.Revena));
        bosslist.add(new NPCTauntsNPC("Calvar'ion",NPCTauntsPhrases.Calvar));
        bosslist.add(new NPCTauntsNPC("Vet'ion",NPCTauntsPhrases.Vetio));
        bosslist.add(new NPCTauntsNPC("Spindel",NPCTauntsPhrases.Spinde));
        bosslist.add(new NPCTauntsNPC("Venenatis",NPCTauntsPhrases.Venena));
        bosslist.add(new NPCTauntsNPC("Artio",NPCTauntsPhrases.Artio));
        bosslist.add(new NPCTauntsNPC("Callisto",NPCTauntsPhrases.Callis));
        bosslist.add(new NPCTauntsNPC("Zulrah",NPCTauntsPhrases.Zulrah));
        bosslist.add(new NPCTauntsNPC("Vorkath",NPCTauntsPhrases.Vorkat));
        bosslist.add(new NPCTauntsNPC("Phantom Muspah",NPCTauntsPhrases.Phanto));
        bosslist.add(new NPCTauntsNPC("The Nightmare",NPCTauntsPhrases.TheNight));
        bosslist.add(new NPCTauntsNPC("Phosani's Nightmare",NPCTauntsPhrases.Phosan));
        bosslist.add(new NPCTauntsNPC("Duke Sucellus",NPCTauntsPhrases.DukeSu));
        bosslist.add(new NPCTauntsNPC("The Leviathan",NPCTauntsPhrases.TheLev));
        bosslist.add(new NPCTauntsNPC("The Whisperer",NPCTauntsPhrases.TheWhi));
        bosslist.add(new NPCTauntsNPC("Vardorvis",NPCTauntsPhrases.Vardor));
        bosslist.add(new NPCTauntsNPC("Obor",NPCTauntsPhrases.Obor));
        bosslist.add(new NPCTauntsNPC("Bryophyta",NPCTauntsPhrases.Bryoph));
        bosslist.add(new NPCTauntsNPC("The Mimic",NPCTauntsPhrases.TheMim));
        bosslist.add(new NPCTauntsNPC("Hespori",NPCTauntsPhrases.Hespor));
        bosslist.add(new NPCTauntsNPC("Skotizo",NPCTauntsPhrases.Skotiz));
        bosslist.add(new NPCTauntsNPC("Dusk",NPCTauntsPhrases.GrotesDu));
        bosslist.add(new NPCTauntsNPC("Dawn",NPCTauntsPhrases.GrotesDa));
        bosslist.add(new NPCTauntsNPC("Abyssal Sire",NPCTauntsPhrases.Abyssa));
        bosslist.add(new NPCTauntsNPC("Kraken",NPCTauntsPhrases.Kraken));
        bosslist.add(new NPCTauntsNPC("Cerberus",NPCTauntsPhrases.Cerber));
        bosslist.add(new NPCTauntsNPC("Thermonuclear smoke devil",NPCTauntsPhrases.Thermo));
        bosslist.add(new NPCTauntsNPC("Alchemical Hydra",NPCTauntsPhrases.Alchem));
        bosslist.add(new NPCTauntsNPC("Crystalline Hunllef",NPCTauntsPhrases.Hun));
        bosslist.add(new NPCTauntsNPC("Corrupted Hunllef",NPCTauntsPhrases.Hun));
        bosslist.add(new NPCTauntsNPC("TzTok-Jad",NPCTauntsPhrases.TzTok));
        bosslist.add(new NPCTauntsNPC("TzKal-Zuk",NPCTauntsPhrases.TzKal));
        bosslist.add(new NPCTauntsNPC("Wintertodt",NPCTauntsPhrases.Winter));
        bosslist.add(new NPCTauntsNPC("Zalcano",NPCTauntsPhrases.Zalcan));
        bosslist.add(new NPCTauntsNPC("Tekton",NPCTauntsPhrases.Tekton));
        bosslist.add(new NPCTauntsNPC("Vanguard",NPCTauntsPhrases.Vangua));
        bosslist.add(new NPCTauntsNPC("Vespula",NPCTauntsPhrases.Vespul));
        bosslist.add(new NPCTauntsNPC("Vasa Nistirio",NPCTauntsPhrases.VasaNi));
        bosslist.add(new NPCTauntsNPC("Muttadile",NPCTauntsPhrases.Muttad));
        bosslist.add(new NPCTauntsNPC("Great Olm",NPCTauntsPhrases.GreatO));
        bosslist.add(new NPCTauntsNPC("The Maiden of Sugadinti",NPCTauntsPhrases.TheMai));
        bosslist.add(new NPCTauntsNPC("Pestilent Bloat",NPCTauntsPhrases.Pestil));
        bosslist.add(new NPCTauntsNPC("Nylocas Vasilias",NPCTauntsPhrases.Nyloca));
        bosslist.add(new NPCTauntsNPC("Sotetseg",NPCTauntsPhrases.Sotets));
        bosslist.add(new NPCTauntsNPC("Xarpus",NPCTauntsPhrases.Xarpus));
        bosslist.add(new NPCTauntsNPC("Verzik Vitur",NPCTauntsPhrases.Verzik));
        bosslist.add(new NPCTauntsNPC("Akkha",NPCTauntsPhrases.Akkha));
        bosslist.add(new NPCTauntsNPC("Ba-Ba",NPCTauntsPhrases.BaBa));
        bosslist.add(new NPCTauntsNPC("Kephri",NPCTauntsPhrases.Kephri));
        bosslist.add(new NPCTauntsNPC("Zebak",NPCTauntsPhrases.Zebak));
        bosslist.add(new NPCTauntsNPC("Tumeken's Warden",NPCTauntsPhrases.Tumeke));
        bosslist.add(new NPCTauntsNPC("Elidinis' Warden",NPCTauntsPhrases.Elidin));
        bosslist.add(new NPCTauntsNPC("Gunthor the brave",NPCTauntsPhrases.Guntho));
    }

    public static void loadcustomlists(NPCTauntsConfig config){
        String[] custombosslist = config.custombosstaunts().split("\n");
        for (int i = 0; i < custombosslist.length; i++) {
            if (custombosslist[i].split(";").length <2){
                continue;
            }
            String bossname = custombosslist[i].split(";")[0];
            String [] taunts = custombosslist[i].split(";")[1].split(":");
            int T =bosslistcontains(bossname);
            if (T!=-1){
                for (int j = 0; j < taunts.length; j++) {
                    bosslist.get(T).phrases.add(taunts[j]);
                }
            }
            else {
            bosslist.add(new NPCTauntsNPC(bossname,taunts));
            }
        }

        String[] customnpclist = config.customnpctaunts().split("\n");
        for (int i = 0; i < customnpclist.length; i++) {
            String npcname = customnpclist[i].split(";")[0];
            String [] taunts = customnpclist[i].split(";")[1].split(":");
            int T =npclistcontains(npcname);
            if (T!=-1){
                for (int j = 0; j < taunts.length; j++) {
                    npclist.get(T).phrases.add(taunts[j]);
                }
            }
            else {
                npclist.add(new NPCTauntsNPC(npcname,taunts));
            }
        }
    }

    public static int npclistcontains(String name)
    {
        int f =-1;
        for (int i = 0; i < NPCTauntsNPClist.npclist.size(); i++) {

            if (NPCTauntsNPClist.npclist.get(i).name.contains(name)) {
                f=i;
            }
        }

        return f;
    }
    public static int bosslistcontains(String name)
    {
        int f =-1;

        for (int i = 0; i < NPCTauntsNPClist.bosslist.size(); i++) {
            if (NPCTauntsNPClist.bosslist.get(i).name.contains(name)) {
                f=i;
            }
        }
        return f;
    }
}
