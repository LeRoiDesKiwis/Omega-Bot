package fr.leroideskiwis.omegabot.events;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveMessageAutoEventTest {

    private OmegaUser omegaUser;
    private RemoveMessageAutoEvent removeMessageAutoEvent;
    private Member member;

    @BeforeEach
    void setUp() {
        this.omegaUser = mock(OmegaUser.class);
        removeMessageAutoEvent = new RemoveMessageAutoEvent(System.currentTimeMillis()+100000, omegaUser);

    }

    @Test
    void isFinished() {
        RemoveMessageAutoEvent removeMessageAutoEvent = new RemoveMessageAutoEvent(0, null);
        assertTrue(removeMessageAutoEvent.isFinished());

        removeMessageAutoEvent = new RemoveMessageAutoEvent(System.currentTimeMillis()+100000, null);
        assertFalse(removeMessageAutoEvent.isFinished());
    }

    @Test
    void isApplicable() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);
        when(event.getMember()).thenReturn(member);
        when(omegaUser.isMember(member)).thenReturn(true);
        assertTrue(removeMessageAutoEvent.isApplicable(event));

        when(omegaUser.isMember(member)).thenReturn(false);
        assertFalse(removeMessageAutoEvent.isApplicable(event));
    }

    @Test
    void apply() {
        Message receivedMessage = mock(Message.class);
        AuditableRestAction delete = mock(AuditableRestAction.class);
        when(receivedMessage.delete()).thenReturn(delete);
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);
        when(event.getMessage()).thenReturn(receivedMessage);
        removeMessageAutoEvent.apply(event);
        verify(delete).queue();
    }
}