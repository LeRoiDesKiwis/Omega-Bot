package fr.leroideskiwis.omegabot.events;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveMessageAutoEvent implements OmegaEvent {

    private OmegaUser omegaUser;
    private long end;

    public RemoveMessageAutoEvent(long end, OmegaUser user) {
        this.end = end;
        this.omegaUser = user;
    }

    @Override
    public boolean isFinished() {
        return end < System.currentTimeMillis();
    }

    @Override
    public boolean isApplicable(MessageReceivedEvent event) {
        return !isFinished() &&omegaUser.isMember(event.getMember());
    }

    @Override
    public void apply(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
    }

    @Override
    public void end() {

    }
}
