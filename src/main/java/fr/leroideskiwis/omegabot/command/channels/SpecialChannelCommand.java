package fr.leroideskiwis.omegabot.command.channels;

import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.events.OmegaEvent;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.EnumSet;
import java.util.Objects;

public class SpecialChannelCommand implements Command{

    private EventManager eventManager;

    public SpecialChannelCommand(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("specialwrite", "permet d'écrire dans le channel spécial");
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        TextChannel channel = event.getGuild().getTextChannelById(System.getenv("LIVRE_DARGENT_ID"));
        DeletePermissionEvent deletePermissionEvent = new DeletePermissionEvent(channel, event.getMember());
        if(eventManager.contains(deletePermissionEvent)){
            event.reply("Vous avez déjà l'autorisation d'écrire !").setEphemeral(true).queue();
            return;
        }
        if(!user.buy(event, price())) return;
        channel.getManager().putPermissionOverride(event.getMember(), EnumSet.of(Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION), null).queue();
        event.reply("Vous avez maintenant la permission d'écrire dans le channel spécial").setEphemeral(true).queue();
        eventManager.addEvent(deletePermissionEvent);
    }

    @Override
    public int price() {
        return 15;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_CANAUX;
    }

    public class DeletePermissionEvent implements OmegaEvent{

        private final TextChannel channel;
        private final Member member;
        private boolean finished;

        public DeletePermissionEvent(TextChannel channel, Member member) {
            this.channel = channel;
            this.member = member;
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public boolean isApplicable(MessageReceivedEvent event) {
            return event.getMember().equals(member) && event.getChannel().equals(channel);
        }

        @Override
        public void apply(MessageReceivedEvent event) {
            channel.getManager().putPermissionOverride(member, null, EnumSet.of(Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION)).queue();
            this.finished = true;
        }

        @Override
        public void end() {

        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DeletePermissionEvent that)) return false;

            return channel.equals(that.channel) && member.equals(that.member);
        }

        @Override
        public int hashCode() {
            return Objects.hash(channel, member);
        }
    }
}
