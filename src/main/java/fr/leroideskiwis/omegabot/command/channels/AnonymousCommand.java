package fr.leroideskiwis.omegabot.command.channels;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class AnonymousCommand implements Command {

    private final int PRICE = 20;

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("anonyme", "Poste un message anonyme (" + PRICE + " pts)")
                .addOption(OptionType.STRING, "message", "Le message à envoyer", true)
                .addOption(OptionType.CHANNEL, "channel", "Le channel où envoyer le message", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        Channel channel = event.getOption("channel") == null ? event.getGuildChannel() : event.getOption("channel").getAsChannel();

        if(!(channel instanceof TextChannel) || !user.canSendAt((TextChannel)channel)){
            event.reply("Vous ne pouvez pas envoyer de message dans ce channel.").setEphemeral(true).queue();
            return;
        }
        if(!user.buy(event, PRICE)) return;
        ((TextChannel)channel).sendMessage(event.getOption("message").getAsString()).queue();
	event.reply("Message envoyé !").setEphemeral(true).queue();
    }

    @Override
    public int price() {
        return PRICE;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_CANAUX;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }

}
