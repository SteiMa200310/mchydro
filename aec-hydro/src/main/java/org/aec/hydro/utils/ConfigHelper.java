package org.aec.hydro.utils;

import net.minecraft.util.math.BlockPos;
import org.json.JSONArray;

import java.util.ArrayList;

public class ConfigHelper {
    public static String blockPosToString(BlockPos blockPos) {
        return blockPos.getX() + "," + blockPos.getY() + "," + blockPos.getZ();
    }

    public static BlockPos stringToBlockPos(String blockPosString) {
        String[] coordinates = blockPosString.split(",");

        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        int z = Integer.parseInt(coordinates[2]);

        return new BlockPos(x, y, z);
    }

    public static boolean posInList(JSONArray list, BlockPos pos) {
        // cycle through array and check if pos has already been added to list
        for (int i = 0; i < list.length(); i += 1) {
            if (list.getJSONObject(i).get("pos").equals(blockPosToString(pos))) return true;
        }

        // if pos does not exist in list, return false
        return false;
    }

    public static int getIndexForPos(JSONArray list, BlockPos pos) {
        // cycle through array and check if pos is in list, return index
        for (int i = 0; i < list.length(); i += 1) {
            if (list.getJSONObject(i).get("pos").equals(blockPosToString(pos))) return i;
        }

        // if pos does not exist in list, return index 0
        return 0;
    }

    public static ArrayList<Integer> getIndicesForPin(JSONArray list, int pinNumber) {

        ArrayList<Integer> indices = new ArrayList<>();

        // cycle through array and check if pin is in list, add found index to array
        for (int i = 0; i < list.length(); i += 1) {
            if (list.getJSONObject(i).get("pin").equals(pinNumber)) indices.add(i);
        }

        // return all found occurrences of given pin
        return indices;
    }
}
