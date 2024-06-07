package fr.leroideskiwis.omegabot.events;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeoutEvent implements OmegaEvent{

    private int counter = 3;
    private UserManager userManager;
    private long textChannelId;
    private Message message;
    private List<User> users = new ArrayList<>();

    public TimeoutEvent(Message message, MessageChannelUnion textChannel, UserManager userManager){
        this.userManager = userManager;
        this.textChannelId = textChannel.getIdLong();
        this.message = message;
    }

    @Override
    public boolean isFinished() {
        return counter == 0;
    }

    @Override
    public boolean isApplicable(MessageReceivedEvent event) {
        return event.getChannel().getIdLong() == textChannelId
                && !userManager.from(event.getMember()).isImmune(BuyType.TIMEOUT_BOMB);
    }

    @Override
    public void apply(MessageReceivedEvent event) {
        userManager.from(event.getMember()).goulag(2, TimeUnit.SECONDS);
        users.add(event.getAuthor());
        counter--;
    }

    @Override
    public void end(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nCamarades tombes au combat : \n");
        for(User user : users){
            stringBuilder.append("- ").append(user.getName()).append("\n");
        }

        message.editMessage(message.getContentDisplay()+stringBuilder.toString()).queue();
    }
}
