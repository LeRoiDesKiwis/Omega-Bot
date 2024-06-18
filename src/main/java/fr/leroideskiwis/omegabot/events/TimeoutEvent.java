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
    private Map<Integer, User> users = new HashMap<>();
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
        users.put(duration, event.getAuthor());
        counter--;
    }

    @Override
    public void end(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nCamarades tombes au combat : \n");
        for(Map.Entry<Integer, User> user : users.entrySet()){
            stringBuilder.append(String.format("- %s : %d minutes\n", user.getValue().getAsMention(), user.getKey()));
        }

        message.editMessage(message.getContentDisplay()+ stringBuilder).queue();
    }
}
