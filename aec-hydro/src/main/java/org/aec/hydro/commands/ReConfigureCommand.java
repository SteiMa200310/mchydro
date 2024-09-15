package org.aec.hydro.commands;


import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aec.hydro.AECHydro;

public class ReConfigureCommand extends ACommand {
    public ReConfigureCommand(String name) {
        super(name);
    }

    public int Execute(CommandContext<ServerCommandSource> ctx) {

        // code to run
        AECHydro.LOGGER.info(Text.translatable("generic.hydro.configure").getString());
        AECHydro.PI4JHELPER.configure(ctx);

        return 1;
    }
}