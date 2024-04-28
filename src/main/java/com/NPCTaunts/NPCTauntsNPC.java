package com.NPCTaunts;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;

class NPCTauntsNPC {

    public String name;

    public List<String> phrases = new ArrayList<>();

    public NPCTauntsNPC(String npcname, String[] npcphrases) {
        name=npcname;
        for (int i=0; i< npcphrases.length;i++){
        phrases.add(npcphrases[i]);
        }
    }
}
