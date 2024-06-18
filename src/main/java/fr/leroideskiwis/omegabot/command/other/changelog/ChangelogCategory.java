package fr.leroideskiwis.omegabot.command.other.changelog;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a single changelog category, like the "feature" category, with its title and content.
 */
public class ChangelogCategory extends AbstractChangelogCategory {

    /**
     * General information about the class's category
     */
    final private ChangeType changeType;

    /**
     * List containing every subcategory, each of them having their own content.
     */
    private List<ChangelogSubCategory> subCategories;

    public ChangelogCategory(ChangeType changeType) {
        super();
        this.changeType = changeType;
        this.subCategories = new ArrayList<>();
    }

    /**
     * Add content under this category.
     * The method check first if it belongs here.
     *
     * @param content The content to be added.
     * @return {@code true} if the content has been added, {@code false} otherwise.
     */
    @Override
    public boolean addContent(String content) {
        if (changeType.startByIdentifier(content)) {
            if (ChangelogSubCategory.isInASubCategory(changeType.removeIdentifier(content))) {
                ChangelogSubCategory contentSubCategory = new ChangelogSubCategory(changeType.removeIdentifier(content));

                // On regarde si il est dans une sous-catégorie déjà existante:
                for (ChangelogSubCategory subCategory : subCategories) {
                    if (contentSubCategory.equals(subCategory)) {
                        return subCategory.addContent(changeType.removeIdentifier(content));
                    }
                }
                return subCategories.add(contentSubCategory); // This subcategory didn't exist before.
            } else {
                return this.content.add(content);
            }
        } else {
            return false;
        }
    }

    /**
     * Format this category's information so that its embeddable.
     *
     * @return a String containing everything that need to be embedded.
     */
    @Override
    public String format() {
        StringBuilder formattedContent = new StringBuilder("## " + changeType.getCategoryName() + "\n");
        subCategories.forEach(changelogSubCategory -> {
            formattedContent.append(changelogSubCategory.format()).append("\n");
        });
        for (String line : content) {
            formattedContent.append("- ").append(changeType.removeIdentifier(line).replaceFirst(": |:", "")).append("\n");
        }
        return formattedContent.toString();
    }
}
