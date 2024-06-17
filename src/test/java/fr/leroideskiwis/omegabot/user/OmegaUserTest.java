package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.BuyType;
import fr.leroideskiwis.omegabot.database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class OmegaUserTest {

    private OmegaUser omegaUser;
    private Member member;

    @BeforeAll
    public static void setupAll() {
        mockStatic(Database.class);
    }

    @BeforeEach
    public void setup() throws SQLException {
        this.member = mock(Member.class);
        this.omegaUser = new OmegaUser(member);
        when(member.getAsMention()).thenReturn("@kiwi");
        when(member.getEffectiveName()).thenReturn("kiwi");
        Database database = mock(Database.class);
        when(Database.getDatabase()).thenReturn(database);
        doNothing().when(database).execute(any());
    }

    @Test
    void isImmune() {
        for (BuyType value : BuyType.values()) {
            assertFalse(omegaUser.isImmune(value));
        }
    }

    @Test
    void immune() {
        omegaUser.immune(BuyType.RUSSIAN_ROULETTE, new Date(System.currentTimeMillis() + 10000));
        assertTrue(omegaUser.isImmune(BuyType.RUSSIAN_ROULETTE));
        assertFalse(omegaUser.isImmune(BuyType.GOULAG));

        omegaUser.immune(BuyType.BOMB, new Date(System.currentTimeMillis() - 1000));
        assertFalse(omegaUser.isImmune(BuyType.BOMB));
    }

    @Test
    void testGoulag() {
        AuditableRestAction auditablebleRestAction = mock(AuditableRestAction.class);
        doNothing().when(auditablebleRestAction).queue();
        when(member.timeoutFor(anyLong(), any())).thenReturn(auditablebleRestAction);
        when(auditablebleRestAction.reason(anyString())).thenReturn(auditablebleRestAction);

        omegaUser.goulag(1, "damn");
        verify(member).timeoutFor(1, TimeUnit.MINUTES);
        verify(auditablebleRestAction, times(1)).reason(anyString());

        omegaUser.goulag(2, TimeUnit.SECONDS, "damn1");
        verify(member).timeoutFor(2, TimeUnit.SECONDS);
        verify(auditablebleRestAction, times(2)).reason(anyString());
    }

    @Test
    void takePoints() {
        omegaUser.takePoints(10);
        assertEquals(90, omegaUser.getPoints());

        omegaUser.takePoints(100);
        assertEquals(0, omegaUser.getPoints());

        omegaUser.takePoints(-10);
        assertEquals(0, omegaUser.getPoints());
    }

    @Test
    void givePoints() {
        omegaUser.givePoints(10);
        assertEquals(110, omegaUser.getPoints());

        omegaUser.givePoints(-10);
        assertEquals(100, omegaUser.getPoints());
    }

    @Test
    void hasEnoughPoints() {
        assertTrue(omegaUser.hasEnoughPoints(10));
        assertTrue(omegaUser.hasEnoughPoints(100));
        assertFalse(omegaUser.hasEnoughPoints(101));
    }

    @Test
    void buy() {
        SlashCommandInteraction interaction = mock(SlashCommandInteraction.class);
        ReplyCallbackAction replyCallbackAction = mock(ReplyCallbackAction.class);
        when(interaction.reply(anyString())).thenReturn(replyCallbackAction);
        when(replyCallbackAction.setEphemeral(true)).thenReturn(replyCallbackAction);
        doNothing().when(replyCallbackAction).queue();

        omegaUser.buy(interaction, 10);
        verify(interaction, never()).reply(anyString());
        assertEquals(90, omegaUser.getPoints());

        omegaUser.buy(interaction, 100);
        verify(interaction).reply(anyString());
        assertEquals(90, omegaUser.getPoints());

        assertThrows(IllegalArgumentException.class, () -> omegaUser.buy(interaction, -10));
    }

    @Test
    void isMember() {
        assertTrue(omegaUser.isMember(member));
        assertFalse(omegaUser.isMember(mock(Member.class)));
    }

    private AuditableRestAction<Void> fakeRestAction() {
        AuditableRestAction restAction = mock(AuditableRestAction.class);
        doNothing().when(restAction).queue();
        return restAction;
    }

    @Test
    void giveRole() {
        Guild guild = mock(Guild.class);
        when(member.getGuild()).thenReturn(guild);

        Role role = mock(Role.class);
        AuditableRestAction restAction = fakeRestAction();
        when(guild.addRoleToMember(member, role)).thenReturn(restAction);
        when(guild.getRoleById(10056486548974489L)).thenReturn(role);

        omegaUser.giveRole(10056486548974489L);

        verify(guild).addRoleToMember(member, role);

    }

    @Test
    void removeRole() {
        Guild guild = mock(Guild.class);
        when(member.getGuild()).thenReturn(guild);

        Role role = mock(Role.class);
        when(guild.getRoleById(10056486548974489L)).thenReturn(role);
        AuditableRestAction restAction = fakeRestAction();
        when(guild.removeRoleFromMember(member, role)).thenReturn(restAction);

        omegaUser.removeRole(10056486548974489L);

        verify(guild).removeRoleFromMember(member, role);
    }

    @Test
    void getAsMention() {
        assertEquals("@kiwi", omegaUser.getAsMention());
    }

    @Test
    void getName() {
        assertEquals("kiwi", omegaUser.getName());
    }

    @Test
    void canSendAt() {
        TextChannel textChannel = mock(TextChannel.class);
        when(textChannel.canTalk(member)).thenReturn(true);
        assertTrue(omegaUser.canSendAt(textChannel));

        when(textChannel.canTalk(member)).thenReturn(false);
        assertFalse(omegaUser.canSendAt(textChannel));
    }
}