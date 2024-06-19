package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.database.Database;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagerTest {

    private UserManager userManager;
    private OmegaUser omegaUser = mock(OmegaUser.class);
    private static Database database;

    @BeforeEach
    void setUp() throws SQLException {
        database = mock(Database.class);
        when(database.getFirst(any(), any(), eq(Integer.class), anyInt())).thenReturn(Optional.empty());
        when(Database.getDatabase()).thenReturn(database);
    }

    @Test
    void from() throws SQLException {
        userManager = new UserManager(omegaUser);
        assertThrows(NullPointerException.class, () -> userManager.from(null));

        Member member = mock(Member.class);
        User user = mock(User.class);
        when(member.getUser()).thenReturn(user);
        when(user.isBot()).thenReturn(false);
        when(omegaUser.isMember(member)).thenReturn(true);

        OmegaUser from = userManager.from(member);
        assertEquals(from, omegaUser);
        assertInstanceOf(OmegaUser.class, from);
        verify(omegaUser, never()).load();
        verify(database, never()).getFirst(any(), any(), eq(Integer.class), any());

        Member member1 = mock(Member.class);
        when(member1.getUser()).thenReturn(user);
        assertNotEquals(userManager.from(member1), omegaUser);
        verify(database, times(1)).getFirst(any(), any(), eq(Integer.class), any());
        assertNotEquals(userManager.from(member1), omegaUser);
        verify(database, times(1)).getFirst(any(), any(), eq(Integer.class), any());

        when(user.isBot()).thenReturn(true);
        assertInstanceOf(OmegaBotUser.class, userManager.from(member));
    }

    @Test
    void stream() {
        userManager = new UserManager(omegaUser);
        assertEquals(userManager.stream().count(), 1);
        userManager.stream().forEach(omegaUser1 -> assertEquals(omegaUser1, omegaUser));

        Member member = mock(Member.class);
        User user = mock(User.class);
        when(member.getUser()).thenReturn(user);
        when(user.isBot()).thenReturn(false);
        when(omegaUser.isMember(member)).thenReturn(false);

        userManager.from(member);
        assertEquals(userManager.stream().count(), 2);
    }
}