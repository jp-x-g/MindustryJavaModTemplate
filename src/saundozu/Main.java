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
import arc.struct.Seq;
import arc.audio.Sound;
import java.util.Random;
import mindustry.core.*;
import mindustry.Vars.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main extends Plugin {

    Map<String, Sound> sounds = new HashMap<>();

    UnitTree unitTree = new UnitTree();


    public static class UnitTree {
        private List<List<String>> units;
        public UnitTree() {
            units = new ArrayList<>(Arrays.asList(
                // SERPULO: UNITS
                Arrays.asList("dagger",   "mace",     "fortress", "scepter",  "reign"     ),
                Arrays.asList("crawler",  "atrax",    "spiroct",  "arkyid",   "toxopid"   ),
                Arrays.asList("nova",     "pulsar",   "quasar",   "vela",     "corvus"    ),
                //
                Arrays.asList("flare",    "horizon",  "zenith",   "antumbra", "eclipse"   ),
                Arrays.asList("mono",     "poly",     "mega",     "quad",     "oct"       ),
                //
                Arrays.asList("risso",    "minke",    "bryde",    "sei",      "omura"     ),
                Arrays.asList("retusa",   "oxynoe",   "cyerce",   "aegires",  "navanax"   ),
                //
                // SERPULO: CORE SHIPS
                Arrays.asList("alpha",    "beta",     "gamma"                             ),
                //
                // EREKIR:  UNITS
                Arrays.asList("stell",    "locus",    "precept",  "vanquish", "conquer"   ),
                Arrays.asList("merui",    "cleroi",   "anthicus", "tecta",    "collaris"  ),
                Arrays.asList("elude",    "avert",    "obviate",  "quell",    "disrupt"   ),
                //
                // EREKIR:  CORE SHIPS
                Arrays.asList("evoke",    "incite",   "emanate"                           ),
                //
                // EREKIR:  NEOPLASM UNITS (unused)
                Arrays.asList("latum",    "renale"                                        ),
                //
                // EREKIR:  BUILDING PARTS (technically units in code)
                Arrays.asList("manifold"                                                  ),
                Arrays.asList("assemblydrone"                                             )
            ));

        // in total, we have:
        // 10 t1-t5 trees (50 units), 2 t1-t3 trees (6 units)
        // two unused neoplasm units (latum+renale)
        // two buildings that are technically units (manifold, assemblydrone)
        // and "missile" (unused anywhere even in the code afaict)
        // making 15 trees, i.e. 15 dirs in the sound assets folder
        // and 60 units, i.e. 60 second-level directories

        // every unit's files are subfolders of the unit tree's folder
        // e.g. assets/sounds/retusa/aegires/aegires-spawn00.ogg
        } // this sets up the UnitTree

        public String first(String unitName) {
            for (List<String> series : units) {
                if (series.contains(unitName)) {
                    return series.get(0);
                }
            }
            return "none";
        } // first

        public int tier(String unitName) {
            for (List<String> series : units) {
                if (series.contains(unitName)) {
                    return series.indexOf(unitName) + 1; // arrays are 0-indexed but units start at "T1"
                }
            }
            return 0;
        } // tier

        public String next(String unitName) {
            for (List<String> tier : units) {
                int index = tier.indexOf(unitName);
                if (index != -1 && index < tier.size() - 1) { // Check if unit is found and not the last one
                    return tier.get(index + 1);
                }
            }
            return "none";
        } // next

        public String prev(String unitName) {
            for (List<String> tier : units) {
                int index = tier.indexOf(unitName);
                if (index != -1 && index > 0) { // Check if unit is found and not the last one
                    return tier.get(index - 1);
                }
            }
            return "none";
        } // next
    } // ends UnitTree




    public void playSound(Unit unit, String event) {
        if (unit.team().equals(Vars.player.team())) {
            Log.info("on player team");
            //sounds.get("testzeal04").at(e.spawner.x, e.spawner.y);
            sounds.get("risso-die-001").at(unit.x, unit.y);
        }
        if(unit.isCommandable()) {
            // this should indicate whether it's actually been taken out of the factory yet
            // (so that, say, we don't trigger this when a t2 is made and immediately put into a t3 factory)
            // but it doesn't -- it does indicate whether it's on the player's team tho!
            Log.info("is commandable");
            Log.info(String.valueOf(unit.isCommandable()));
            //sounds.get("testzeal01").at(e.spawner.x, e.spawner.y);
            // unit type
            String.valueOf(unit.type());
            if (String.valueOf(unit.type()).equals("quasar")) {
                Log.info("quasar");
                //sounds.get("testzeal03").at(e.spawner.x, e.spawner.y);
            }
        }
    } // ends playSound

    public void playGroupSound(Seq<Unit> selectedUnits, String event) {
        Log.info("Playing group sound");
        // if (!String.valueOf(unit.type()).equals("quasar")) return;
        //testzeal01.at(0,0);
        // Look at what the selection is, scan all units out of it.
        Object[] itemArray = Vars.control.input.selectedUnits.toArray();

        // Now we will do something kind of complicated.

        // We will get the highest-tier unit out of the selection.
        // But what if there are, say, two t5s? They don't outrank each other.
        // so instead we will get the highest ranking unit from each tree

        // the dumbest way to do this is:
        // add each new unit in the group to a list
        // then go through and remove every unit lower than it in the same tree

        // alternate idea: nested loops

        // for each unit tree:
        // if selection contains (t5 of tree):
        //      add t5 to group
        //      else:
        //      if selection contains (t4 of tree):
        //          add t4 to group
        //          else:

        // and so on. this might be the least dumb thing

        // for the core ships, latum, manifold, assemblydrone
        // just don't do this at all: if they're in the selection they're in the sound group

        // any map that has those units commandable is a meme that wont work right anyway
        // prob less than .1% of games will feature any of these units being commanded

        // they probably aren't even going to have unit sounds so whatever

        // this whole thing is probably contingent on it not sounding awful to overlap the sounds
        // its very well possible that doing this sounds like trash no matter how sophisticated
        // so i just have to do a "pick highest ranking unit" from a somewhat arbitrary list anyway
        // 

        //ArrayList<String> tierFives = new ArrayList<>();
        //String highest = "";
        //int highestTier = 1;

        for (Object item : itemArray) {
            // Printing item gives something like: "Unit#982:poly" -- so we will just split that.
            Log.info(unitTree.tier(item.toString().split(":")[1]));
            Log.info(item.toString().split(":")[1]);
            // Log.info(item + " (Type: " + item.getClass().getSimpleName() + ")");
        } // for each array item
    } // ends playGroupSound

    @Override
    public void init() {
        


        sounds.put("risso-die-001",     Vars.tree.loadSound("risso/risso/risso-die-001"));
        sounds.put("risso-die-002",     Vars.tree.loadSound("risso/risso/risso-die-002"));
        sounds.put("dingC5",     Vars.tree.loadSound("ding432-C5"));
        sounds.put("dingD5",     Vars.tree.loadSound("ding432-D5"));
        sounds.put("dingDb5",    Vars.tree.loadSound("ding432-Db5"));
        sounds.put("dingE5",     Vars.tree.loadSound("ding432-E5"));
        sounds.put("dingEb5",    Vars.tree.loadSound("ding432-Eb5"));
        sounds.put("dingF5",     Vars.tree.loadSound("ding432-F5"));
        sounds.put("dingG5",     Vars.tree.loadSound("ding432-G5"));
        sounds.put("dingGb5",    Vars.tree.loadSound("ding432-Gb5"));
        sounds.put("dingA5",     Vars.tree.loadSound("ding432-A5"));
        sounds.put("dingAb5",    Vars.tree.loadSound("ding432-Ab5"));
        sounds.put("dingB5",     Vars.tree.loadSound("ding432-B5"));
        sounds.put("dingBb5",    Vars.tree.loadSound("ding432-Bb5"));
        sounds.put("elec01",     Vars.tree.loadSound("elec01"));
        sounds.put("testzeal01", Vars.tree.loadSound("test-zeal01"));
        sounds.put("testzeal02", Vars.tree.loadSound("test-zeal02"));
        sounds.put("testzeal03", Vars.tree.loadSound("test-zeal03"));
        sounds.put("testzeal04", Vars.tree.loadSound("test-zeal04"));
        sounds.put("testwrai11", Vars.tree.loadSound("test-wrai11"));
        sounds.put("testwrai12", Vars.tree.loadSound("test-wrai12"));
        sounds.put("testwrai13", Vars.tree.loadSound("test-wrai13"));
        sounds.put("testwrai14", Vars.tree.loadSound("test-wrai14"));
        sounds.put("testwrai15", Vars.tree.loadSound("test-wrai15"));
        sounds.put("testwrai16", Vars.tree.loadSound("test-wrai16"));
        sounds.put("testwrai17", Vars.tree.loadSound("test-wrai17"));
        sounds.put("testwrai18", Vars.tree.loadSound("test-wrai18"));
        sounds.put("testwrai19", Vars.tree.loadSound("test-wrai19"));
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

        // unitTree.loadAllSounds();

        // tests for the unitTree
        // Log.info("tests for unitTree");
        Log.info("flare | tier: " + unitTree.tier("flare") + " / first: " + unitTree.first("flare") + " / prev: " + unitTree.prev("flare") + " / next: " + unitTree.next("flare") );
        Log.info("omura | tier: " + unitTree.tier("omura") + " / first: " + unitTree.first("omura") + " / prev: " + unitTree.prev("omura") + " / next: " + unitTree.next("omura") );
        Log.info("bryde | tier: " + unitTree.tier("bryde") + " / first: " + unitTree.first("bryde") + " / prev: " + unitTree.prev("bryde") + " / next: " + unitTree.next("bryde") );
        Log.info("gamma | tier: " + unitTree.tier("gamma") + " / first: " + unitTree.first("gamma") + " / prev: " + unitTree.prev("gamma") + " / next: " + unitTree.next("gamma") );

            //if (!e.unit.isPlayer()) return;
            //if (!String.valueOf(e.unit.type()).equals("gamma")) return;
            //elec01.at(e.unit.x, e.unit.y);

        Events.on(UnitDamageEvent.class, e -> {
            // when unit takes damage
            Log.info("UnitDamageEvent: " + String.valueOf(e.unit.type()));
            playSound(e.unit, "Damage");
        });       
        Events.on(UnitDestroyEvent.class, e -> {
            // when unit is destroyed
            Log.info("UnitDestroyEvent: " + String.valueOf(e.unit.type()));
            playSound(e.unit, "Destroy");
        });    
        Events.on(UnitControlEvent.class, e -> {
            // this is when the player starts manually piloting a unit
            // NOT when the user selects it and gives it a RTS command
            Log.info("UnitControlEvent: " + String.valueOf(e.unit.type()));
            playSound(e.unit, "Control");
        });    
        Events.on(UnitUnloadEvent.class, e -> {
            // for being "dumped from any payload block"
            // THIS fires when the unit becomes commandable
            // (i.e. doesn't fire for intermediate construction stages)
            // also when you spawn from a payload source
            Log.info("UnitUnloadEvent: " + String.valueOf(e.unit.type()));
            playSound(e.unit, "Unload");
        });    

        Events.on(UnitCreateEvent.class, e -> {
            // when unit is made in a constructor

            // note: fires even if it is being popped into another constructor,
            // or a payload conveyor, or is blocked from exiting the constructor
            // by a wall or whatever. this doesn't mean the unit is usable yet

            Log.info("UnitCreateEvent: " + String.valueOf(e.unit.type()));
            playSound(e.unit, "Create");
            sounds.get("elec01").at(e.spawner.x, e.spawner.y);
            // Passing the whole event to playSound seems extremely difficult.
            // Something else will have to be done for this
            // if we want playSound to have spawner information
        });     
        Events.run(Trigger.unitCommandChange, () -> {
            // when a select group is created AND is distinct from whatever was
            // in the previous select group (i.e. this won't fire if you already
            // have a set of units selected, and drag a new box that selects the same set)
            Log.info("unitCommandChange");
            playGroupSound(Vars.control.input.selectedUnits, "unitCommandChange");
        });  
        Events.run(Trigger.unitCommandAttack, () -> {
            // for when a select group is commanded to a location that involves attacking
            // enemy units (i.e. the selector turns red)
            Log.info("unitCommandAttack");
            playGroupSound(Vars.control.input.selectedUnits, "unitCommandAttack");
        });  
        // The unitCommandPosition commit has been merged but not into main branch so commenting this out for now
        //Events.run(Trigger.unitCommandPosition, () -> {
        //    // for when a select group is assigned a target location that doesn't involve attacking
        //    Log.info("unitCommandPosition");
        //    playGroupSound(Vars.control.input.selectedUnits, "unitCommandPosition");
        //});  

        // unitComp contains:
        // x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, shield, ammo, dragMultiplier, armorOverride, speedMultiplier
        // team, id, mineTile, vel, mounts, stack
        // lastCommanded, shadowAlpha, healTime, lastFogPos, resupplyTime, wasPlayer, wasHealed
        // unloaded(?)
        // type
        // isPlayer, getPlayer
    } // ends init
} // ends Main

        // Mindustry/core/src/mindustry/game/EventType.java
        // potentially useful:
        // PickupEvent which has .carrier, .unit, .build
        // PayloadDropEvent, has .carrier, .unit, .build (for building)
        // BuildingCommandEvent, has player, building, position

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