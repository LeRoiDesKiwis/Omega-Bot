package fr.leroideskiwis.omegabot.listeners;

import fr.leroideskiwis.omegabot.events.EventManager;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageListenerTest {

    private MessageListener messageListener;
    private MessageReceivedEvent event;
    private OmegaUser user;
    private EventManager eventManager;
    private UserManager userManager;
    private User realUser;

    @BeforeEach
    void setUp() {
        user = mock(OmegaUser.class);
        realUser = mock(User.class);
        when(realUser.isBot()).thenReturn(false);
        event = mock(MessageReceivedEvent.class);
        when(event.getAuthor()).thenReturn(realUser);

        eventManager = mock(EventManager.class);

        userManager = mock(UserManager.class);
        when(userManager.from(any())).thenReturn(user);

        messageListener = new MessageListener(eventManager, userManager);
    }

    @Test
    void onMessageReceived() {
        messageListener.onMessageReceived(event);
        messageListener.onMessageReceived(event);
        verify(user, times(1)).givePoints(1);

    }
}