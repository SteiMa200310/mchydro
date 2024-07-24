package org.aec.hydro.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.aec.hydro.AECHydro;
import org.aec.hydro.utils.Config;
import org.aec.hydro.utils.ConfigHelper;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DIBlock extends Block {
    public static final BooleanProperty POWERED = Properties.POWERED;

    public DIBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.POWERED, false));
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public MutableText getName() {
        return Text.translatable("block.hydro.di_block");
    }

    // if block is being broken, remove it from list
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        // check if block is in DI/DO list
        JSONObject config = Config.getConfig();
        JSONArray diBlockList = config.getJSONObject("di_do_list").getJSONArray("di");

        // return if block is not in list
        if (!ConfigHelper.posInList(diBlockList, pos)) return;

        // remove block from list and update config
        int indexInList = ConfigHelper.getIndexForPos(diBlockList, pos);
        diBlockList.remove(indexInList);
        Config.updateConfig(config);
    }
    // right-click event
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;

        // check if block has already been added to DI/DO list
        JSONObject config = Config.getConfig();
        JSONArray diBlockList = config.getJSONObject("di_do_list").getJSONArray("di");
        JSONArray doBlockList = config.getJSONObject("di_do_list").getJSONArray("do");
        ArrayList<Object> usedDoPinList = new ArrayList<>();
        List<Object> pinList = config.getJSONObject("pin_list").getJSONArray("pins").toList();

        // remove pins used by DOs from available pins
        for (int i = 0; i < doBlockList.length(); i += 1) {
            usedDoPinList.add(doBlockList.getJSONObject(i).get("pin"));
        }
        // remove all pins used by DOs
        pinList.removeAll(usedDoPinList);
        // if block has not been added to list, add it to list with default configuration
        if (!ConfigHelper.posInList(diBlockList, pos)) {
            JSONObject object = new JSONObject();
            object.put("pin", -1);
            object.put("pos", ConfigHelper.blockPosToString(pos));

            diBlockList.put(object);

            Config.updateConfig(config);
        }

        // increment pin and display to player
        int indexInList = ConfigHelper.getIndexForPos(diBlockList, pos);
        int pin = (int) diBlockList.getJSONObject(indexInList).get("pin");

        // cycle through list of pins
        // increment pin to cycle through all possible pins
        //System.out.println(pinList.indexOf(pin));

        // if pin is not found in list, function returns -1 --> therefore set pin index to 0 (first)
        // return next entry in pin list if pin is part of list, else use first entry
        // at end of array, switch to first element to create a "cycle"
        pin = (int) (pinList.contains(pin) ? ((pinList.indexOf(pin) < (pinList.size() - 1)) ? (pinList.get(pinList.indexOf(pin) + 1)) : (pinList.get(0))) : (pinList.get(0)));
        //pin += 1;

        // update pin in config file and print set pin to player's actionbar
        diBlockList.getJSONObject(indexInList).put("pin", pin);
        Config.updateConfig(config);

        player.sendMessage(Text.of("Pin: " + pin), true);

        return ActionResult.SUCCESS;
    }

    public int getRedstoneOutput(BlockState state) {
        return state.get(POWERED) ? 100 : 0;
    }

    public void setRedstoneOutput(World world, BlockPos pos, boolean shouldPower) {
        world.setBlockState(pos, world.getBlockState(pos).with(POWERED, shouldPower), 2);
        updateNeighbors(world, pos);
    }

    protected void updateNeighbors(World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.down(), this);
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return this.getRedstoneOutput(state);
    }

    // allows block to connect to redstone
    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(!world.isClient()){
            AECHydro.LOGGER.info("Server Placed");
        } else {
            AECHydro.LOGGER.info("Client Placed");
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }
}
