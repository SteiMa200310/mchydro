package org.aec.hydro.commands;


import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aec.hydro.AECHydro;
import org.aec.hydro.utils.Command;

public class ConfigureCommand extends Command {
    public ConfigureCommand(String cmdName) {
        super(cmdName);
    }

    @Override
    protected int cmdFunction(CommandContext<ServerCommandSource> ctx) {

        // code to run
        AECHydro.LOGGER.info(Text.translatable("generic.hydro.configure").getString());
        AECHydro.PI4JHELPER.configure(ctx);

        return 1;
    }
}
