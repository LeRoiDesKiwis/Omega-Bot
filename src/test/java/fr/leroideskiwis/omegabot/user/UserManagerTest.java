package fr.leroideskiwis.omegabot.user;

import fr.leroideskiwis.omegabot.database.Database;
import net.dv8tion.jda.api.entities.Member;
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

    @BeforeAll
    static void setUpAll() throws SQLException {
        database = mock(Database.class);
        when(database.getFirst(any(), any(), eq(Integer.class), anyInt())).thenReturn(Optional.empty());

        mockStatic(Database.class).when(Database::getDatabase).thenReturn(database);
    }

    @Test
    void from() throws SQLException {
        userManager = new UserManager(omegaUser);
        assertThrows(NullPointerException.class, () -> userManager.from(null));

        Member member = mock(Member.class);
        when(omegaUser.isMember(member)).thenReturn(true);

        assertEquals(userManager.from(member), omegaUser);
        verify(omegaUser, never()).load();

        Member member1 = mock(Member.class);
        assertNotEquals(userManager.from(member1), omegaUser);
        verify(database, times(1)).getFirst(any(), any(), eq(Integer.class), any());
        assertNotEquals(userManager.from(member1), omegaUser);
        verify(database, times(1)).getFirst(any(), any(), eq(Integer.class), any());
    }

    @Test
    void stream() {
        userManager = new UserManager(omegaUser);
        assertEquals(userManager.stream().count(), 1);
        userManager.stream().forEach(omegaUser1 -> assertEquals(omegaUser1, omegaUser));

        userManager.from(mock(Member.class));
        assertEquals(userManager.stream().count(), 2);
    }
}