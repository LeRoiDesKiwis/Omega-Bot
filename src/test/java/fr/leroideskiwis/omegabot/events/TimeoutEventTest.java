package fr.leroideskiwis.omegabot.events;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import fr.leroideskiwis.omegabot.user.UserManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TimeoutEventTest {

    private TimeoutEvent timeoutEvent;
    private long id = 123456L;
    private Message message;
    private UserManager userManager;
    private OmegaUser omegaUser;

    @BeforeEach
    void setUp() {
        this.message = mock(Message.class);
        this.userManager = mock(UserManager.class);
        this.omegaUser = mock(OmegaUser.class);
        MessageChannelUnion textChannel = mock(MessageChannelUnion.class);
        when(textChannel.getIdLong()).thenReturn(id);

        this.timeoutEvent = new TimeoutEvent(message, textChannel, userManager);
    }

    @Test
    void isFinished() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);
        Member member = mock(Member.class);
        when(userManager.from(member)).thenReturn(omegaUser);
        when(event.getMember()).thenReturn(member);

        assertFalse(timeoutEvent.isFinished());
        timeoutEvent.apply(event);
        assertFalse(timeoutEvent.isFinished());
        timeoutEvent.apply(event);
        assertFalse(timeoutEvent.isFinished());
        timeoutEvent.apply(event);
        assertTrue(timeoutEvent.isFinished());
    }

    @Test
    void isApplicable() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);
        when(userManager.from(any())).thenReturn(omegaUser);

        MessageChannelUnion channel = mock(MessageChannelUnion.class);
        when(event.getChannel()).thenReturn(channel);

        when(omegaUser.isImmune(BuyType.TIMEOUT_BOMB)).thenReturn(false);

        when(channel.getIdLong()).thenReturn(654321L);
        assertFalse(timeoutEvent.isApplicable(event), "applicable but channel id is different");

        when(channel.getIdLong()).thenReturn(id);
        assertTrue(timeoutEvent.isApplicable(event), "not applicable but channel id is the same");

        when(omegaUser.isImmune(BuyType.TIMEOUT_BOMB)).thenReturn(true);
        assertFalse(timeoutEvent.isApplicable(event), "applicable but user is immune");

        when(omegaUser.isImmune(BuyType.TIMEOUT_BOMB)).thenReturn(false);
        assertTrue(timeoutEvent.isApplicable(event), "not applicable but user is not immune");

        when(omegaUser.isImmune(BuyType.BOMB)).thenReturn(true);
        when(omegaUser.isImmune(BuyType.GOULAG)).thenReturn(true);
        when(omegaUser.isImmune(BuyType.RUSSIAN_ROULETTE)).thenReturn(true);
        assertTrue(timeoutEvent.isApplicable(event), "not applicable but user is not immune (but is immune to others)");

    }

    @Test
    void apply() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class);
        Member member = mock(Member.class);
        when(userManager.from(member)).thenReturn(omegaUser);
        when(event.getAuthor()).thenReturn(mock(User.class));
        when(userManager.from(any())).thenReturn(omegaUser);

        timeoutEvent.apply(event);
        verify(omegaUser).goulag(anyInt(), any());
    }

    @Test
    void end() {
        MessageEditAction action = mock(MessageEditAction.class);
        when(message.editMessage(anyString())).thenReturn(action);
        doNothing().when(action).queue();

        timeoutEvent.end();

        verify(action).queue();
    }
}