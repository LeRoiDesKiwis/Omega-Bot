package fr.leroideskiwis.omegabot.listeners;

import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.events.OmegaEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageListener extends ListenerAdapter {

    private final EventManager eventManager;

    public MessageListener(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        eventManager.handle(event);
    }
}
