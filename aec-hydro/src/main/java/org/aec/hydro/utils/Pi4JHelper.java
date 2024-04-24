package org.aec.hydro.utils;

import com.mojang.brigadier.context.CommandContext;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.i2c.PiGpioI2CProvider;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.DIBlock;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Pi4JHelper {

    Context pi4jContext;
    PiGpio pi4jGpio;
    HashMap<DigitalInput, Integer> inputHashMap = new HashMap<>();
    HashMap<Integer, DigitalOutput> outputHashMap = new HashMap<>();

    CommandContext<ServerCommandSource> ctxGlobal;
    JSONArray diList;
    JSONArray doList;

    public Pi4JHelper() {

        AECHydro.LOGGER.info(Text.translatable("generic.hydro.pi4j_helper").getString());

    }

    // cycle through all DI/DO blocks and configure pins
    public void configure(CommandContext<ServerCommandSource> ctx) {

        // create global ctx to allow state change of blocks
        ctxGlobal = ctx;

        // shutdown Pi4J context to create a new one and therefore allow to reconfigure pins
        // also clear HashMaps to configured pins
        if (pi4jContext != null) {
            inputHashMap.clear();
            outputHashMap.clear();

            pi4jContext.shutdown();
            pi4jGpio.shutdown();
        }

        // initialize Pi4J with an auto context
        // An auto context includes AUTO-DETECT BINDINGS enabled
        // which will load all detected Pi4J extension libraries
        // (Platforms and Providers) in the class path
        //pi4jContext = Pi4J.newAutoContext();
        pi4jGpio = PiGpio.newNativeInstance();
        pi4jGpio.initialize();

        pi4jContext = Pi4J.newContextBuilder()
                .noAutoDetectProviders()
                .add(
                        PiGpioI2CProvider.newInstance(pi4jGpio),
                        PiGpioDigitalOutputProvider.newInstance(pi4jGpio),
                        PiGpioDigitalInputProvider.newInstance(pi4jGpio)
                )
                .build();

        diList = Config.getConfig().getJSONObject("di_do_list").getJSONArray("di");
        doList = Config.getConfig().getJSONObject("di_do_list").getJSONArray("do");

        Set<Integer> diPinList = new HashSet<>();
        Set<Integer> doPinList = new HashSet<>();

        // get all pins to configure, use Set to remove duplicates because pin can be used multiple times
        for (Object diBlock: diList) {
            diPinList.add(((JSONObject) diBlock).getInt("pin"));
        }

        for (Object doBlock: doList) {
            doPinList.add(((JSONObject) doBlock).getInt("pin"));
        }

        // configure all pins
        for(int diPin: diPinList) {
            //createDI(diPin, System.out::println);
            createDI(diPin, this::updateDIBlocks);
            setStateForDIBlocks(diPin, false);
        }

        for(int doPin: doPinList) {
            createDO(doPin);
        }

        ctxGlobal.getSource().sendMessage(Text.translatable("generic.hydro.configure_cmd").formatted(Formatting.GREEN));

    }

    private void createDI(int pinNumber, DigitalStateChangeListener listener) {
        DigitalInputConfig inputConfig = DigitalInput.newConfigBuilder(pi4jContext)
                .id("PIN-" + pinNumber) // e.g. PIN-9
                .name("Digital Input Pin " + pinNumber) // e.g. Digital Input Pin 9
                .address(pinNumber)
                .pull(PullResistance.PULL_UP) // configure input as pull-up
                .provider("pigpio-digital-input")
                .build();

        DigitalInput input = pi4jContext.din().create(inputConfig);
        input.addListener(listener);

        // put configured input into HashMap to access it later
        //inputHashMap.put(pinNumber, input);
        inputHashMap.put(input, pinNumber);
    }

    private void createDO(int pinNumber) {
        DigitalOutputConfig outputConfig = DigitalOutput.newConfigBuilder(pi4jContext)
                .id("PIN-" + pinNumber) // e.g. PIN-11
                .name("Digital Output Pin " + pinNumber) // e.g. Digital Output Pin 11
                .address(pinNumber)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output")
                .build();

        DigitalOutput output = pi4jContext.dout().create(outputConfig);

        // put configured input into HashMap to access it later
        outputHashMap.put(pinNumber, output);
    }

    private void setStateForDIBlocks(int pinNumber, boolean shouldPower) {

        // reset all found blocks for given pinNumber
        for (int pinIndex: ConfigHelper.getIndicesForPin(diList, pinNumber)) {

            BlockPos blockPos = ConfigHelper.stringToBlockPos(diList.getJSONObject(pinIndex).getString("pos"));

            ((DIBlock) ctxGlobal.getSource().getWorld().getBlockState(blockPos).getBlock()).setRedstoneOutput(ctxGlobal.getSource().getWorld(), blockPos, shouldPower);

        }

        AECHydro.LOGGER.info(Text.translatable("generic.hydro.di_update", pinNumber, shouldPower ? "HIGH" : "LOW").getString());

    }

    private void updateDIBlocks(DigitalStateChangeEvent e) {
        // inverse logic, default state is HIGH (0), so if pin is LOW --> 1
        setStateForDIBlocks(inputHashMap.get((DigitalInput) e.source()), e.state().getName().equals("LOW"));
    }

    public void updateDOs(int pinNumber, boolean state) {

        if (outputHashMap.get(pinNumber) == null) return;

        outputHashMap.get(pinNumber).setState(state);

        AECHydro.LOGGER.info(Text.translatable("generic.hydro.do_update", pinNumber, state ? "HIGH" : "LOW").getString());
    }

}
