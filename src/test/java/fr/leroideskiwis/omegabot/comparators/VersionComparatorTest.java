package fr.leroideskiwis.omegabot.comparators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionComparatorTest {

    @Test
    void compare() {

        VersionComparator versionComparator = new VersionComparator();
        assertEquals(0, versionComparator.compare("1.0.0", "1.0.0"));
        assertEquals(0, versionComparator.compare("1.0", "1.0"));
        assertEquals(0, versionComparator.compare("1", "1"));
        assertEquals(0, versionComparator.compare("", ""));
        check("1.0.1", "1.0.0");
        check("1.1.0", "1.0.0");
        check("2.0.0", "1.0.0");
        check("1.4", "1.3.5");
        check("1.4.5","1.4");
        check("1.4.5", "");

    }

    /**
     * Check if the first version is greater than the second one
     * @param version1
     * @param version2
     */
    private void check(String version1, String version2){
        assertEquals(1, new VersionComparator().compare(version1, version2));
        assertEquals(-1, new VersionComparator().compare(version2, version1));
    }
}