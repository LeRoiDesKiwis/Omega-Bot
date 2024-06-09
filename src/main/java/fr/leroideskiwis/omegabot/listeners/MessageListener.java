package fr.leroideskiwis.omegabot.listeners;

import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MessageListener extends ListenerAdapter {

    private final EventManager eventManager;
    private final UserManager userManager;
    private Map<OmegaUser, Long> userTimes = new HashMap<>();

    public MessageListener(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        eventManager.handle(event);

        OmegaUser user = userManager.from(event.getMember());
        if(userTimes.containsKey(user)) {
            long time = userTimes.get(user);
            if(System.currentTimeMillis()-time < 5000) return; //5000 pour des raisons des test, 120000 en prod

        }
        user.givePoints(1);
        userTimes.put(user, System.currentTimeMillis());
    }
}
