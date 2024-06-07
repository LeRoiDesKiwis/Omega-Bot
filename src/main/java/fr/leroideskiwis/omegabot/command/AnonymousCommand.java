package fr.leroideskiwis.omegabot.command;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.ChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class AnonymousCommand implements Command{

    private final int PRICE = 100;

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("anonyme", "Poste un message anonyme")
                .addOption(OptionType.STRING, "message", "Le message à envoyer", true)
                .addOption(OptionType.CHANNEL, "channel", "Le channel où envoyer le message", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        if(!user.hasEnoughPoints(PRICE)){
            int missingPoints = PRICE - user.getPoints();
            event.reply(String.format("Vous n'avez pas assez de points pour utiliser cette commande. (il vous manque %d points).", missingPoints)).queue();
            return;
        }
        user.takePoints(PRICE);
        event.reply("Message envoyé !").setEphemeral(true).queue();
        TextChannel channel = event.getOption("channel") == null ? event.getGuildChannel().asTextChannel() : event.getOption("channel").getAsChannel().asTextChannel();
        channel.sendMessage(event.getOption("message").getAsString()).queue();
    }

}
