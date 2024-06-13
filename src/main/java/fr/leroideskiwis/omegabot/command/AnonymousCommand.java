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

    private final int PRICE = 20;

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
        Channel channel = event.getOption("channel") == null ? (GuildChannelUnion) event.getGuildChannel() : event.getOption("channel").getAsChannel();

        if(!(channel instanceof TextChannel) || !user.canSendAt((TextChannel)channel)){
            event.reply("Vous ne pouvez pas envoyer de message dans ce channel.").setEphemeral(true).queue();
            return;
        }
        ((TextChannel)channel).sendMessage(event.getOption("message").getAsString()).queue();
        user.takePoints(PRICE);
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

}
