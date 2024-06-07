package fr.leroideskiwis.omegabot.commands.listeners;

import fr.leroideskiwis.omegabot.commands.events.OmegaEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageListener extends ListenerAdapter {

    private final List<OmegaEvent> events = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        Iterator<OmegaEvent> iterator = events.iterator();
        while(iterator.hasNext()){
            OmegaEvent omegaEvent = iterator.next();
            if(omegaEvent.isApplicable(event)){
                if(omegaEvent.isFinished()) iterator.remove();
                else omegaEvent.apply(event);
            }
        }
    }
}
