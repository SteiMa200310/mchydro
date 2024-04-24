package org.aec.hydro.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.json.JSONObject;
import org.json.JSONTokener;
import static org.aec.hydro.AECHydro.LOGGER;

import java.io.*;

public class Config {

    private static final String CONFIG_STRING = """
            {
                "_description": "Config file for Hydrogen in Minecraft, a mod for simulating the cycle of hydrogen",
                
                "di_do_list": {
                    "_description": "list of blocks that act like DI/DOs",
                    "di": [],
                    "do": []
                },
                
                "pin_list": {
                    "_description": "list of pins that can be used (based on pi4j documentation, BCM pinout)",
                    "pins": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]
                }
            }
            """;

    public static void saveConfig() {
        File fabDir = FabricLoader.getInstance().getConfigDir().toFile();
        File dir = new File(fabDir, "hydro");

        // if dir hydro does not exist in config folder, create it
        if (!dir.exists()) {
            dir.mkdirs();

            LOGGER.info(Text.translatable("generic.hydro.config_folder").getString());
        }

        // check if config exists, else save it to config.json
        File f = new File(dir, "config.json");

        if (!f.exists()) {

            try {
                f.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(f);

                outputStream.write(CONFIG_STRING.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LOGGER.info(Text.translatable("generic.hydro.config_created").getString());
        }

    }

    public static JSONObject getConfig() {

        // stuff to get dir of config file
        File fabDir = FabricLoader.getInstance().getConfigDir().toFile();
        File dir = new File(fabDir, "hydro");
        File f = new File(dir, "config.json");

        // define reader
        FileReader reader = null;

        // Read the JSON file
        try {
            reader = new FileReader(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            LOGGER.error(String.valueOf(e));
        }
        // create AssertionError if file not found, will not crash game, just show up in console
        assert reader != null;
        JSONTokener tokener = new JSONTokener(reader);

        return new JSONObject(tokener);
    }

    public static void updateConfig(JSONObject config) {

        // stuff to get dir of config file
        File fabDir = FabricLoader.getInstance().getConfigDir().toFile();
        File dir = new File(fabDir, "hydro");
        File f = new File(dir, "config.json");

        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(config.toString(4).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
