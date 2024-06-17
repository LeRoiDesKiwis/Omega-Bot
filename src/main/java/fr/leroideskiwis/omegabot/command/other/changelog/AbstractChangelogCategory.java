package fr.leroideskiwis.omegabot.command.other.changelog;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChangelogCategory {
    protected List<String> content;

    public AbstractChangelogCategory() {
        content = new ArrayList<>();
    }

    public abstract String format();
    public abstract boolean addContent(String content);
}
