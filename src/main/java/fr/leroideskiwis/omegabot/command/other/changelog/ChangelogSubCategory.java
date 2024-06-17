package fr.leroideskiwis.omegabot.command.other.changelog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Class dealing with changelog lines having 2 "category", for example:
 * <p>
 * {@code
 * fix(givepoints): now show target's solde instead of sender
 * }
 * <p>
 * The subcategory is between the {@code ()}.
 */
class ChangelogSubCategory extends AbstractChangelogCategory {
    private static final String regexExpression = "^( |)\\(([^)]+)\\)";
    private String name;

    public ChangelogSubCategory(String content) {
        super();
        this.name = getSubCategoryName(content);
        addContent(content);
    }

    public String getName() {
        return name;
    }

    /**
     * Static method to know if a line belongs in a sub category.
     *
     * @param line The line to examine.It should be stripped from its identifier.
     * @return If this {@code line} belongs to a sub category.
     */
    static public boolean isInASubCategory(String line) {
        return Pattern.compile(regexExpression).matcher(line).find();
    }

    public String getSubCategoryName(String line) {
        if (ChangelogSubCategory.isInASubCategory(line)) {
            String rawName = Pattern.compile(regexExpression).matcher(line).group();
            return rawName.substring(1, rawName.length() - 1);
        } else {
            return "";
        }

    }

    @Override
    public boolean addContent(String content) {
        // getting everything after ": " OR ":"
        String trimmedContent = Pattern.compile("(?<=(: )|:).*").matcher(content).group();
        return this.content.add("- " + trimmedContent);
    }

    @Override
    public String format() {
        return "### " + name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangelogSubCategory that)) return false;

        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
