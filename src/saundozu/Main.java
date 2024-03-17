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
import mindustry.core.*;
import mindustry.Vars.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

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
        

// this is based on https://github.com/BnDLett/custom-dagger-sounds/

public class Main extends Plugin {
    //    public class TriggerEvent {
    //        private final Trigger trigger;
    //
    //        public TriggerEvent(Trigger trigger) {
    //            this.trigger = trigger;
    //        }
    //
    //        public Trigger getTrigger() {
    //            return trigger;
    //        }
    //    } // ends TriggerEvent

    @Override
    public void init() {
        Sound dingC5     = Vars.tree.loadSound("ding432-C5");
        Sound dingD5     = Vars.tree.loadSound("ding432-D5");
        Sound dingDb5    = Vars.tree.loadSound("ding432-Db5");
        Sound dingE5     = Vars.tree.loadSound("ding432-E5");
        Sound dingEb5    = Vars.tree.loadSound("ding432-Eb5");
        Sound dingF5     = Vars.tree.loadSound("ding432-F5");
        Sound dingG5     = Vars.tree.loadSound("ding432-G5");
        Sound dingGb5    = Vars.tree.loadSound("ding432-Gb5");
        Sound dingA5     = Vars.tree.loadSound("ding432-A5");
        Sound dingAb5    = Vars.tree.loadSound("ding432-Ab5");
        Sound dingB5     = Vars.tree.loadSound("ding432-B5");
        Sound dingBb5    = Vars.tree.loadSound("ding432-Bb5");
        Sound elec01     = Vars.tree.loadSound("elec01");
        Sound testzeal01 = Vars.tree.loadSound("test-zeal01");
        Sound testzeal02 = Vars.tree.loadSound("test-zeal02");
        Sound testzeal03 = Vars.tree.loadSound("test-zeal03");
        Sound testzeal04 = Vars.tree.loadSound("test-zeal04");
        Sound testwrai11 = Vars.tree.loadSound("test-wrai11");
        Sound testwrai12 = Vars.tree.loadSound("test-wrai12");
        Sound testwrai13 = Vars.tree.loadSound("test-wrai13");
        Sound testwrai14 = Vars.tree.loadSound("test-wrai14");
        Sound testwrai15 = Vars.tree.loadSound("test-wrai15");
        Sound testwrai16 = Vars.tree.loadSound("test-wrai16");
        Sound testwrai17 = Vars.tree.loadSound("test-wrai17");
        Sound testwrai18 = Vars.tree.loadSound("test-wrai18");
        Sound testwrai19 = Vars.tree.loadSound("test-wrai19");
        // TODO; these are dummy files in assets/sounds, eventually they will be real
        // then they will be like dagger/damage-01.ogg, dagger/die-02.ogg
        // et cetera. there's 50 units so each will have their own folder
        // with however many sounds as are recorded
        // (units that get used all the time with high APM will have more sounds)
        // (units that nobody makes bc they suck, like the navanax, will just have like 4)
        // the way im thinking of doing this is something like:
        // doing a "ls" equivalent on the directory to get its tree, then loading all the files
        // this means that if e.g. i add a "dagger/die-06.ogg" to the folder
        // i don't have to go through this java file and hardcode another line to load that
        // it would just parse the ls output and keep loading files
        // until it got to the end of however many they were for a unit and event

        // if i were using python or js this would be a simple dict
        // i.e. sth like, for each result:
        // parse the directory into "dirstring", filename prior to dash as "event"
        // then store it as soundfiles[dirstring][event]
        // so e.g. soundfiles['dagger']['die'] would be an array
        // with 6 elements, or 8 or however many there happened to be

        // in java i assume it is some gigantic excruciating object oriented 
        // beans.spring.beans.beans.ObjectStrategyFactory.subclass.fart

        // but anyway that is then and this is now, we will just go with this for now


        Events.on(UnitDamageEvent.class, e -> {
            if (!e.unit.isPlayer()) return;
            if (!String.valueOf(e.unit.type()).equals("flare")) return;
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
            Log.info("unitDestroyEvent");
            if (!e.unit.isPlayer()) return;
            if (String.valueOf(e.unit.type()).equals("flare")) {;
                elec01.at(e.unit.x, e.unit.y);
            }
            if (String.valueOf(e.unit.type()).equals("quasar")) {;
                elec01.at(e.unit.x, e.unit.y);
            }
        });    
        Events.on(UnitDestroyEvent.class, e -> {
            Log.info("unitDestroyEvent");

            ////////////////////////////////////////////////////////////////////////////////
            // Log all details of the unit object using reflection
            //Class<?> unitClass = Vars.control.input.selectedUnits.getClass();
            //Class<?> unitClass = itemArray[0].getClass();
            //// note that if you try this with no units selected it crashes the game desu
//
            //Log.info("Class Name: " + unitClass.getName());
//
            //// Log all fields
            //Log.info("Fields:");
            //Arrays.stream(unitClass.getDeclaredFields()).forEach(field -> {
                //field.setAccessible(true); // Make private fields accessible
                //try {
                    //Log.info("  " + field.getName() + " - " + field.get(itemArray[0]));
                //} catch (IllegalArgumentException | IllegalAccessException ex) {
                    //Log.info("  Unable to access value for field: " + field.getName());
                //}
            //});
//
            //// Log all methods
            //Log.info("Methods:");
            //Arrays.stream(unitClass.getMethods()).forEach(method -> {
                //Log.info("  " + method.getName());
            //});
            ////////////////////////////////////////////////////////////////////////////////

            if (!e.unit.isPlayer()) return;
            if ("flare".equals(String.valueOf(e.unit.type()))) {
                elec01.at(e.unit.x, e.unit.y);
            }
            if ("quasar".equals(String.valueOf(e.unit.type()))) {
                elec01.at(e.unit.x, e.unit.y);
            }
        });  

        Events.run(Trigger.unitCommandChange, () -> {
            Log.info("unitCommandChange");
            // if (!String.valueOf(unit.type()).equals("quasar")) return;
            testzeal01.at(0,0);
            // Look at what the selection is.
            Object[] itemArray = Vars.control.input.selectedUnits.toArray();
            for (Object item : itemArray) {
                // Printing item gives something like: "Unit#982:poly" -- so we will just split that.
                Log.info(item.toString().split(":")[1]);
                // Log.info(item + " (Type: " + item.getClass().getSimpleName() + ")");
            }
        });  

        Events.run(Trigger.unitCommandAttack, () -> {
            Log.info("unitCommandChange");
            // if (!String.valueOf(unit.type()).equals("quasar")) return;
            dingDb5.at(0,0);
        });  
        // unitComp contains:
        // x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, shield, ammo, dragMultiplier, armorOverride, speedMultiplier
        // team, id, mineTile, vel, mounts, stack
        // lastCommanded, shadowAlpha, healTime, lastFogPos, resupplyTime, wasPlayer, wasHealed
        // unloaded(?)
        // type
        // isPlayer, getPlayer

        // playerComp contains:
        // team, 


//        Events.on(unitCommandAttack.class, e -> {
//            if (!String.valueOf(e.unit.type()).equals("quasar")) return;
//            testzeal02.at(e.unit.x, e.unit.y);
//        });      '
        // This is when the player jumps into the unit, not when they command it.
        Events.on(UnitControlEvent.class, e -> {
            Log.info("UnitControlEvent");
            if (!String.valueOf(e.unit.type()).equals("quasar")) return;
            testzeal04.at(e.unit.x, e.unit.y);
        });      
        // this crashes.     
        //        Events.on(UnitCreateEvent.class, e -> {
        //            elec01.at(e.spawnerUnit.x, e.spawnerUnit.y);
        //            if (!String.valueOf(e.spawnerUnit.type()).equals("quasar")) return;
        //            testzeal03.at(e.spawnerUnit.x, e.spawnerUnit.y);
        //        });      

        Events.on(UnitCreateEvent.class, e -> {
            Log.info("UnitCreateEvent");
            elec01.at(e.spawner.x, e.spawner.y);
            if (e.unit.team().equals(Vars.player.team())) {
                Log.info("on player team");
                testzeal04.at(e.spawner.x, e.spawner.y);
                }
            if(e.unit.isCommandable()) {
                // this should indicate whether it's actually been taken out of the factory yet
                // (so that, say, we don't trigger this when a t2 is made and immediately put into a t3 factory)
                // but it doesn't -- it does indicate whether it's on the player's team tho!
                Log.info("is commandable");
                Log.info(String.valueOf(e.unit.isCommandable()));
                testzeal01.at(e.spawner.x, e.spawner.y);
                if (String.valueOf(e.unit.type()).equals("quasar")) {
                    Log.info("quasar");
                    testzeal03.at(e.spawner.x, e.spawner.y);
                };
            }
        });      
    } // ends init
} // ends Main
        // TODO: note that there are like an additional 7 types of events and there's also 50 unit types
        // having it like this is utterly unsustainable. what we need is this:
        // each event listener calls a function "emitSound" and passes the event type and the unit to that.

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
    // TODO: implement this lator
    //    private void emitSound(Unit unit, String eventType) {
    //        String unitType = unit.type().name;
    //        Sound sound = getSoundForEvent(unitType, eventType);
    //        if (sound != null) {
    //            sound.at(unit.x, unit.y);
    //        } else {
    //            Log.info("No sound found for unit type " + unitType + " and event " + eventType);
    //        }
    //    }