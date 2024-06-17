package fr.leroideskiwis.omegabot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventManagerTest {

    private EventManager eventManager;
    private OmegaEvent omegaEvent;

    @BeforeEach
    void setUp() {
        eventManager = new EventManager();
        omegaEvent = mock(OmegaEvent.class);
    }

    @Test
    void contains() {
        assertFalse(eventManager.contains(omegaEvent));
    }

    @Test
    void addEvent() {
        eventManager.addEvent(omegaEvent);
        assertTrue(eventManager.contains(omegaEvent));
    }

    @Test
    void handle() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);

        when(omegaEvent.isApplicable(event)).thenReturn(true);
        when(omegaEvent.isFinished()).thenReturn(false);
        doNothing().when(omegaEvent).apply(event);

        eventManager.addEvent(omegaEvent);
        eventManager.handle(event);
        verify(omegaEvent).apply(event);
        assertTrue(eventManager.contains(omegaEvent));

        when(omegaEvent.isApplicable(event)).thenReturn(false);
        eventManager.handle(event);
        verify(omegaEvent).apply(event);
        assertTrue(eventManager.contains(omegaEvent));

        when(omegaEvent.isFinished()).thenReturn(true);
        eventManager.handle(event);
        verify(omegaEvent).end();
        assertFalse(eventManager.contains(omegaEvent));
    }
}