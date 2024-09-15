package org.aec.hydro.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aec.hydro.AECHydro;
import org.aec.hydro.commands.base.ACommand;

public class ReConfigureCommand extends ACommand {
    public ReConfigureCommand(String name) {
        super(name);
    }

    public int Execute(CommandContext<ServerCommandSource> ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.player.sendMessage(Text.translatable("generic.hydro.configure"), false);
        AECHydro.LOGGER.info(Text.translatable("generic.hydro.configure").getString());

        AECHydro.PI4JHELPER.configure(ctx);

        return 1;
    }
}