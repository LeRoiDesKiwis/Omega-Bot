package fr.leroideskiwis.omegabot.command.other.changelog;

import java.util.regex.Pattern;

/**
 * This enum keep track of all existing types of modifications possible in a changelog
 */
enum ChangeType {
    FEAT("Features", "feat"),
    FIX("Fixes", "fix"),
    OTHER("Others", "");


    /**
     * The name of the category, as it's displayed in the embed.
     */
    final private String categoryName;

    /**
     * What string need to be found at the start of the line in the changelog.
     */
    final private String identifier;

    ChangeType(String categoryName, String identifier) {
        this.categoryName = categoryName;
        this.identifier = identifier;
    }

    /**
     * If the {@code line} argument belong to this category (if it starts by the category's {@code identifier}).
     *
     * @param line the {@code String} to test
     * @return if {@code line} is in this category.
     */
    public boolean startByIdentifier(String line) {
        if (identifier.isEmpty()) return true;
        return Pattern.compile("^" + identifier).matcher(line).find();
    }

    public String removeIdentifier(String line) {
        return line.replaceFirst(identifier, "");
    }

    public String getCategoryName() {
        return categoryName;
    }
}
