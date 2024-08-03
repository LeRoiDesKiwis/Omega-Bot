package fr.leroideskiwis.omegabot.events;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimeoutEvent implements OmegaEvent{

    private int counter = 3;
    private UserManager userManager;
    private long textChannelId;
    private Message message;
    private List<TimeoutUser> users = new ArrayList<>();
    private Random random = new Random();

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
        int duration = random.nextInt(3, 6);
        userManager.from(event.getMember()).goulag(duration, "timeout bomb");
        users.add(new TimeoutUser(event.getAuthor(), duration));
        counter--;
    }

    @Override
    public void end(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nCamarades tombés au combat : \n");
        for(TimeoutUser user : users){
            stringBuilder.append(String.format("- %s : %d minutes\n", user.user.getAsMention(), user.duration));
        }

        message.editMessage(message.getContentDisplay()+ stringBuilder).queue();
    }

    private class TimeoutUser{
        public final User user;
        public final int duration;

        public TimeoutUser(User user, int duration){
            this.user = user;
            this.duration = duration;
        }
    }
}
