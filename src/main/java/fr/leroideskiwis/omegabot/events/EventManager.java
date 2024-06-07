package fr.leroideskiwis.omegabot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<OmegaEvent> events = new ArrayList<>();

    public void addEvent(OmegaEvent event) {
        events.add(event);
    }

    public void handle(MessageReceivedEvent event) {
        for(OmegaEvent omegaEvent : new ArrayList<>(events)){
            if(omegaEvent.isApplicable(event)) omegaEvent.apply(event);
            if(omegaEvent.isFinished()) {
                omegaEvent.end();
                events.remove(omegaEvent);
            }
        }
    }
}
