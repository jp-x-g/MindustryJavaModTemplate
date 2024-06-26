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

// to list dir contents
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Main extends Plugin {
    // Actions:
    // "UnitCreateEvent"     "BLD"
    // "UnitUnloadEvent"     "ULD"
    // "UnitControlEvent"    "CTL"
    // "unitCommandChange"   "SEL"
    // "unitCommandPosition" "CMD"
    // "unitCommandAttack"   "ATK"
    // "UnitDamageEvent"     "DMG"
    // "UnitDestroyEvent"    "DIE"

    public Map<String, Map<String, Seq<Sound>>> unitSounds = new HashMap<>();
                    //  |           |       |
                    //  "risso"     |       |
                    //              "ATK"   |
                    //                      array of individual sound files
                    // so e.g. you can fetch unitSounds.get("risso").get("ATK")

//     public static class FileFetcher {
        // public static Map<String, Map<String, Seq<Sound>>> fillSounds(Map<String, Map<String, Seq<Sound>>> unitSounds) throws IOException, URISyntaxException {
// 
//             return "Never, because this class is stupid."
//         }
//     }

    Map<String, Sound> sounds = new HashMap<>();

    UnitTree unitTree = new UnitTree();

    public static class UnitTree {
        private List<List<String>> units;
        public UnitTree() {
            units = new ArrayList<>(Arrays.asList(
                // EREKIR:  CORE SHIPS
                Arrays.asList("evoke",    "incite",   "emanate"                           ),
                //
                // EREKIR:  UNITS
                Arrays.asList("merui",    "cleroi",   "anthicus", "tecta",    "collaris"  ),
                Arrays.asList("stell",    "locus",    "precept",  "vanquish", "conquer"   ),
                Arrays.asList("elude",    "avert",    "obviate",  "quell",    "disrupt"   ),
                //
                // SERPULO: CORE SHIPS
                Arrays.asList("alpha",    "beta",     "gamma"                             ),
                //,
                // SERPULO: UNITS
                Arrays.asList("risso",    "minke",    "bryde",    "sei",      "omura"     ),
                Arrays.asList("retusa",   "oxynoe",   "cyerce",   "aegires",  "navanax"   ),
                //
                Arrays.asList("nova",     "pulsar",   "quasar",   "vela",     "corvus"    ),
                Arrays.asList("crawler",  "atrax",    "spiroct",  "arkyid",   "toxopid"   ),
                Arrays.asList("dagger",   "mace",     "fortress", "scepter",  "reign"     ),
                //
                Arrays.asList("mono",     "poly",     "mega",     "quad",     "oct"       ),
                Arrays.asList("flare",    "horizon",  "zenith",   "antumbra", "eclipse"   ),
                //
                // EREKIR:  BUILDING PARTS (technically units in code)
                Arrays.asList("assemblydrone"                                             ),
                Arrays.asList("manifold"                                                  ),
                //
                // EREKIR:  NEOPLASM UNITS (unused)
                Arrays.asList("latum",    "renale"                                        )
                //
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
        } // prev

        public List<String> flatList() {
            List<String> flatList = new ArrayList<>();
            for (List<String> sublist : units) {
                flatList.addAll(sublist);
            }
            return flatList;
        } // flatList

        public List<String> tierList() {
            // return ranked list of which units take precedence when emitting sounds for a selection group
            return(Arrays.asList(
                "emanate",   "gamma",      "incite",     "beta",       "evoke",      "alpha",
                "collaris",  "conquer",    "disrupt",    "omura",      "navanax",    "corvus",     "toxopid",    "reign",      "oct",        "eclipse",
                "tecta",     "vanquish",   "quell",      "sei",        "aegires",    "vela",       "arkyid",     "scepter",    "quad",       "antumbra",  
                "anthicus",  "precept",    "obviate",    "bryde",      "cyerce",     "quasar",     "spiroct",    "fortress",   "mega",       "zenith", 
                "cleroi",    "locus",      "avert",      "minke",      "oxynoe",     "pulsar",     "atrax",      "mace",       "poly",       "horizon",
                "merui",     "stell",      "elude",      "risso",      "retusa",     "nova",       "crawler",    "dagger",     "mono",       "flare",
                "latum",     "renale",
                "manifold",  "assemblydrone"
            ));
        } // tierList
    } // ends UnitTree

    public static class unitActions {
        public static Map<String, String> map() {
            Map<String, String> actions = new HashMap<>();
            
            actions.put("UnitCreateEvent",     "BLD");
            actions.put("UnitUnloadEvent",     "ULD");
            actions.put("UnitControlEvent",    "CTL");
            actions.put("unitCommandChange",   "SEL");
            actions.put("unitCommandPosition", "CMD");
            actions.put("unitCommandAttack",   "ATK");
            actions.put("UnitDamageEvent",     "DMG");
            actions.put("UnitDestroyEvent",    "DIE");
            
            return actions;
        } // map()
        public static Map<String, String> maprv() {
            // Mildly stupid way to return it the other way around
            // (i.e. so key "BLD" returns value "UnitCreateEvent")
            Map<String, String> actions = map(); // Use the unitActions method to get the original mapping
            Map<String, String> abbrevActions = new HashMap<>();
        
            // Iterate through the original map and reverse the mappings
            for (Map.Entry<String, String> entry : actions.entrySet()) {
                abbrevActions.put(entry.getValue(), entry.getKey());
            }
            return abbrevActions;
        } // maprv()
    } // ends unitActions
    

    // public void playSound(Unit unit, String event, Map<String, Map<String, Seq<Sound>>> unitSounds) {
    public void playSound(Unit unit, String event) {
        //sounds.get("elec01").at(unit.x, unit.y);
        String unitType   = String.valueOf(unit.type());
        String actionCode = unitActions.map().get(event);
        // converts e.g. "UnitCreateEvent" -> "BLD".
        Log.info("Received a " + actionCode + " for a " + unitType + ".");
        if (unit.team().equals(Vars.player.team())) {
            //if(unit.isCommandable()) {
            if(true) {
                Log.info("on player team and commandable");
                if(unitSounds.get(unitType) != null) {
                    Log.info("unitSounds.get(" + unitType + ").get(" + actionCode + ")");
                    if (unitSounds.get(unitType).get(actionCode).isEmpty() == false) {
                        Log.info("Playing sound");
                        unitSounds.get(unitType).get(actionCode).random().at(unit.x, unit.y);
                        //unitSounds.get(unitType).get(actionCode).first().at(unit.x, unit.y);
                    }else{
                       Log.info("No sound for " + actionCode + " for " + unitType + ".");
                    } // end of thing to check if sound exists and play it or not
                }else{
                    Log.info("No sounds.");
                }
            } // if isCommandable
        } // if on player's team
            // this should indicate whether it's actually been taken out of the factory yet
            // (so that, say, we don't trigger this when a t2 is made and immediately put into a t3 factory)
            // but it doesn't -- it does indicate whether it's on the player's team tho!
            //Log.info("is commandable");
            //Log.info(String.valueOf(unit.isCommandable()));
            //sounds.get("testzeal01").at(e.spawner.x, e.spawner.y);
    } // ends playSound

    //public void playGroupSound(Seq<Unit> selectedUnits, String event, Map<String, Map<String, Seq<Sound>>> unitSounds) {
    public void playGroupSound(Seq<Unit> selectedUnits, String event) {
        Log.info("Playing group sound");
        // if (!String.valueOf(unit.type()).equals("quasar")) return;
        //testzeal01.at(0,0);
        // Look at what the selection is, scan all units out of it.
        List<String> tierList = unitTree.tierList();
        //Unit[] itemArray    = Vars.control.input.selectedUnits.toArray();
        if (selectedUnits.size < 1){
            return;
        } else {
            Unit highestUnit = selectedUnits.get(0);
            Integer highestTier = 999;

            for (Unit item : selectedUnits) {
                // Printing item gives something like: "Unit#982:poly" -- so we will just split that.
                String unitName = item.toString().split(":")[1];
                Integer tier = tierList.indexOf(unitName);

                Log.info(tier);

                if((tier < highestTier) && (tier != -1)){
                    highestTier = tier;
                    highestUnit = item;
                }
            } // for each array item
            Log.info("Highest-tier unit in selection: " + highestTier);
            if (highestTier != 999) {
                // "anthicus-missile" is a unit, for example, so we need to filter out random crap like that
                playSound(highestUnit, event);
            } // dispatch sound for highest tier of unit selected, unless no valid units were in the selection
        } // if there's a size for selectedunits

    } // ends playGroupSound

    @Override
    public void init() {
        // test for file listing
        Log.info("Starting to open");
        //try {
            //List<String> filesInDir = FileFetcher.listSounds();
            // Log.info("Starting to open 1");
            //filesInDir.forEach(filePath -> Log.info(filePath));
            // Log.info("Starting to open 2");
        //} catch (IOException | URISyntaxException e) {
            //Log.info("Le failed");
            //e.printStackTrace();
        //}
        //////////////////////////////////////////////////
        // BELOW: Set up unit tree, initialize sounds.
        //////////////////////////////////////////////////

        UnitTree unitTree = new UnitTree();
        Log.info("flare | tier: " + unitTree.tier("flare") + " / first: " + unitTree.first("flare") + " / prev: " + unitTree.prev("flare") + " / next: " + unitTree.next("flare") );
        Log.info("omura | tier: " + unitTree.tier("omura") + " / first: " + unitTree.first("omura") + " / prev: " + unitTree.prev("omura") + " / next: " + unitTree.next("omura") );
        Log.info("bryde | tier: " + unitTree.tier("bryde") + " / first: " + unitTree.first("bryde") + " / prev: " + unitTree.prev("bryde") + " / next: " + unitTree.next("bryde") );
        Log.info("gamma | tier: " + unitTree.tier("gamma") + " / first: " + unitTree.first("gamma") + " / prev: " + unitTree.prev("gamma") + " / next: " + unitTree.next("gamma") );

        //////////////////////////////////////////////////
        // Set up skeleton for the big unitSounds object
        // that we'll put all of the sound objects in later
        // as we read them out of the directories.
        //////////////////////////////////////////////////

        //  initialize basic structure for this huge duesy object:
        //Map<String, Map<String, ArrayList<Sound>>> unitSounds = new HashMap<>();
        //  |           |       |
        //  "risso"     |       |
        //              "ATK"   |
        //                      array of individual sound files
        // so e.g. you can fetch unitSounds.get("risso").get("ATK")

        Map<String, String> actions = unitActions.maprv(); // Use maprv() for abbreviations as keys

        // could also be
        // for (int i = 0; i < unitTree.flatList().size(); i++) {
        for (String unit : unitTree.flatList()) {
            Map<String, Seq<Sound>> actionSounds = new HashMap<>();
            // Iterate over action abbreviations (e.g., "BLD", "ULD")
            for (String actionAbbrev : actions.keySet()) {
                Log.info("Setting up skelly for " + unit + " / " + actionAbbrev);
                // Initialize each action with an empty sequence of sounds
                actionSounds.put(actionAbbrev, new Seq<Sound>());
            }
            // Put the initialized map for this unit into the unitSounds map
            unitSounds.put(unit, actionSounds);
        } // iterates over unitTree.flatlist()

        // Now we have the skeleton created for the unitSounds object.

        //////////////////////////////////////////////////
        // Now we load all the sounds
        //////////////////////////////////////////////////

        try {
            String[] actionsArray = unitActions.maprv().keySet().toArray(new String[0]);
            // Gets a simple array of the unit actions, i.e. "CMD", "ATK", "DIE" etc.
            List<String> fileList = new ArrayList<>();
            //Log.info("asdf1");
            // Get a reference to the JAR file that contains this class
            URI uri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            //Log.info("asdf2");
            JarFile jarFile = new JarFile(new java.io.File(uri));
            //Log.info("asdf3");
            // Iterate through the entries of the JAR file
            Enumeration<JarEntry> entries = jarFile.entries();
            //Log.info("asdf4");
            while (entries.hasMoreElements()) {
               // Log.info("asdf5");
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                //Log.info(name);

                // Check if the entry is in the specified directory and add it to the list
                String lastFour = name.toLowerCase().substring(name.length() - 4);
                // (it should parse .ogg but also .OGG and .OgG and whatever)
                if (lastFour.contains(".ogg") || lastFour.contains(".mp3")) {
                    String[] nameparts = name.split("/");
                    Log.info(name);
                    String ultimate = nameparts[(nameparts.length - 1)];
                    String penultim = nameparts[(nameparts.length - 2)];
                    String stripped = name.substring(0, name.length() - 4);
                    // remove file extension
                    stripped = stripped.substring(7);
                    // sounds/
                    // 0123456
                    if (unitSounds.containsKey(penultim)){
                        Log.info("ultimate= " + ultimate);
                        Log.info("penultim= " + penultim);
                        Log.info("stripped= " + stripped);
                        for(String act : actionsArray) {
                            if(ultimate.contains(act)) {
                                Log.info("This is a " + act + " for the " + penultim + ".");
                                Log.info("Trying to load " + stripped);
                                Sound strippedSound = Vars.tree.loadSound(stripped);
                                unitSounds.get(penultim).get(act).add(strippedSound);
                                //unitSounds.get(penultim).get(act).random().play();
                                // Actually add the sound file to the proper place in the unitSounds skelly.
                            } // If that audio file is for that action.
                        }   // For each action type.
                    }else{
                        //Log.info("Does not contain");
                    }  // handle if there's a key for the unit type
                } // if last four are .ogg or .mp3, meaning if it's a valid soud
            }   // while entries has more elements
            
            jarFile.close();
            //return fileList;

            Log.info("Loaded the sounds.");
            Log.info("Here is a risso CMDing.");
            if(true) {
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
                unitSounds.get("risso").get("ULD").add(Vars.tree.loadSound("risso/risso/risso-ULD-001"));
            } // if true

            //////////////////////////////////////////////////
            // BELOW: Set up actual event listeners
            //////////////////////////////////////////////////
            Events.on(UnitDamageEvent.class, e -> {
                // when unit takes damage
                // has .unit, .bullet
                Log.info("UnitDamageEvent: " + String.valueOf(e.unit.type()));
                playSound(e.unit, "UnitDamageEvent");
            });       
            Events.on(UnitDestroyEvent.class, e -> {
                // when unit is destroyed
                Log.info("UnitDestroyEvent: " + String.valueOf(e.unit.type()));
                playSound(e.unit, "UnitDestroyEvent");
            });    
            Events.on(UnitControlEvent.class, e -> {
                // this is when the player starts manually piloting a unit
                // NOT when the user selects it and gives it a RTS command
                Log.info("UnitControlEvent: " + String.valueOf(e.unit.type()));
                playSound(e.unit, "UnitControlEvent");
            });    
            Events.on(UnitUnloadEvent.class, e -> {
                // for being "dumped from any payload block"
                // THIS fires when the unit becomes commandable
                // (i.e. doesn't fire for intermediate construction stages)
                // also when you spawn from a payload source
                Log.info("UnitUnloadEvent: " + String.valueOf(e.unit.type()));
                playSound(e.unit, "UnitUnloadEvent");
            });    

            Events.on(UnitCreateEvent.class, e -> {
                // when unit is made in a constructor
                // has .unit, .spawner, .spawnerUnit

                // note: fires even if it is being popped into another constructor,
                // or a payload conveyor, or is blocked from exiting the constructor
                // by a wall or whatever. this doesn't mean the unit is usable yet

                Log.info("UnitCreateEvent: " + String.valueOf(e.unit.type()));
                playSound(e.unit, "UnitCreateEvent");
                if (e.spawner != null) {
                    sounds.get("elec01").at(e.spawner.x, e.spawner.y);
                }
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
            } catch (IOException | URISyntaxException e) {
            Log.info("Le failed to load sounds");
            e.printStackTrace();
            //final Map<String, Map<String, Seq<Sound>>> unitSounds = new HashMap<>();
        } // end gigantic cursed try block
        
        // The unitCommandPosition commit has been merged but not into main branch so commenting this out for now
        //Events.run(Trigger.unitCommandPosition, () -> {
        //    // for when a select group is assigned a target location that doesn't involve attacking
        //    Log.info("unitCommandPosition");
        //    playGroupSound(Vars.control.input.selectedUnits, "unitCommandPosition");
        //});  
        // other events we dont have listeners for:
        // Mindustry/core/src/mindustry/game/EventType.java

        // PickupEvent which has .carrier, .unit, .build
        // PayloadDropEvent, has .carrier, .unit, .build (for building)
        // BuildingCommandEvent, has player, building, position

        // UnitDrownEvent, has .unit
        // UnitBulletDestroyEvent has .unit, .bullet
        //       Called when a unit is directly killed by a bullet. May not fire in all circumstances
        // UnitSpawnEvent, has .unit
        //    for spawning in a wave
        // UnitChangeEvent, has .player and .unit
        //    ?

        // unitComp contains:
        // x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, shield, ammo, dragMultiplier, armorOverride, speedMultiplier
        // team, id, mineTile, vel, mounts, stack
        // lastCommanded, shadowAlpha, healTime, lastFogPos, resupplyTime, wasPlayer, wasHealed
        // unloaded(?)
        // type
        // isPlayer, getPlayer
    } // ends init
} // ends Main