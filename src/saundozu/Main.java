package saundozu;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
// above imports are from template
import arc.audio.Sound;
import java.util.Random;

        // units:

        // ----- SERPULO -----
        // alpha     beta      gamma
        // -----
        // dagger    mace      fortress  scepter   reign
        // nova      pulsar    quasar    vela      corvus
        // crawler   atrax     spiroct   arkyid    toxopid
        // -----
        // flare     horizon   zenith    antumbra  eclipse
        // mono      poly      mega      quad      oct
        // -----
        // risso     minke     bryde     sei       omura
        // retusa    oxynoe    cyerce    aegires   navanax

        // ----- EREKIR -----
        // evoke     incite    emanate
        // -----
        // stell     locus     precept   vanquish  conquer
        // -----
        // merui     cleroi    anthicus  tecta     collaris
        // -----
        // elude     avert     obviate   quell     disrupt
        // -----
        // latum     renale
        // manifold 
        // assemblydrone

        // missile (unused)

        // 10 t1-t5 trees (50), 2 t1-t3 trees (6)
        // two unused neoplasm (latum+renale)
        // two buildings that are technically units (manifold, assemblydrone)
        // and "missile" (unused)
        
        // for event types we want
        // Mindustry/core/src/mindustry/game/EventType.java
        // potentially useful:

        // PickupEvent which has .carrier, .unit, .build
        // PayloadDropEvent, has .carrier, .unit, .build (for building)

        // UnitControlEvent (.player, .unit)

        // BuildingCommandEvent, has player, building, position

        // unitCommandChange,
        // unitCommandAttack,

        // deaths

        // UnitDrownEvent, has .unit
        // UnitBulletDestroyEvent has .unit, .bullet
        //       Called when a unit is directly killed by a bullet. May not fire in all circumstances
        // UnitDestroyEvent, has .unit
        // UnitDamageEvent, has .unit, .bullet
        // UnitCreateEvent, has .unit, .spawner, .spawnerUnit
        //    this is for being made in a constructor
        // UnitSpawnEvent, has .unit
        //    for spawning in a wave
        // UnitUnloadEvent, has .unit
        //    for being "dumped from any payload block"
        // UnitChangeEvent, has .player and .unit
        //    ?

// this is based on https://github.com/BnDLett/custom-dagger-sounds/

public class Main extends Plugin {
    @Override
    public void init() {
        Sound dingC5  = Vars.tree.loadSound("ding432-C5");
        Sound dingD5  = Vars.tree.loadSound("ding432-D5");
        Sound dingDb5 = Vars.tree.loadSound("ding432-Db5");
        Sound dingE5  = Vars.tree.loadSound("ding432-E5");
        Sound dingEb5 = Vars.tree.loadSound("ding432-Eb5");
        Sound dingF5  = Vars.tree.loadSound("ding432-F5");
        Sound dingG5  = Vars.tree.loadSound("ding432-G5");
        Sound dingGb5 = Vars.tree.loadSound("ding432-Gb5");
        Sound dingA5  = Vars.tree.loadSound("ding432-A5");
        Sound dingAb5 = Vars.tree.loadSound("ding432-Ab5");
        Sound dingB5  = Vars.tree.loadSound("ding432-B5");
        Sound dingBb5 = Vars.tree.loadSound("ding432-Bb5");
        Sound elec01  = Vars.tree.loadSound("elec01");

        Events.on(UnitDamageEvent.class, e -> {
            if (!e.unit.isPlayer()) return;
            if (!String.valueOf(e.unit.type()).equals("dagger")) return;
            Random rand = new Random();
            int num = rand.nextInt(3);
            if (num == 0) dingC5.at(e.unit.x, e.unit.y);
            else if (num == 1) dingDb5.at(e.unit.x, e.unit.y);
            else if (num == 2) dingF5.at(e.unit.x, e.unit.y);
        });     
        Events.on(UnitDamageEvent.class, e -> {
            if (!e.unit.isPlayer()) return;
            if (!String.valueOf(e.unit.type()).equals("gamma")) return;
            elec01.at(e.unit.x, e.unit.y);
        });       

        Events.on(UnitDestroyEvent.class, e -> {
            if (!e.unit.isPlayer()) return;
            if (!String.valueOf(e.unit.type()).equals("dagger")) return;
            elec01.at(e.unit.x, e.unit.y);
        });      
    }
}