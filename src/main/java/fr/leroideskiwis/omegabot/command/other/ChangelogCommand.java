package fr.leroideskiwis.omegabot.command.other;

import fr.leroideskiwis.omegabot.Main;
import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ChangelogCommand implements Command {
    /**
     * This enum keep track of all existing types of modifications possible in a changelog
     */
    private enum ChangeType {
        FEAT("Features", "feat:"),
        FIX("Fixes", "fix");

        /**
         * Static method to handle everything not in any category given by {@code ChangeType}.
         * @param line the string to test
         * @return if the given line isn't in any category.
         */
        static private boolean isNotInCategory(String line) {
            AtomicBoolean returnValue = new AtomicBoolean(true);
            Arrays.stream(ChangeType.values()).iterator().forEachRemaining(
                    changeType -> {
                        if (changeType.stringStartByIdentifier(line)) {
                            returnValue.set(false);
                        }
                    }
            );
            return returnValue.get();
        }

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
         * @param line the {@code String} to test
         * @return if {@code line} is in this category.
         */
        private boolean stringStartByIdentifier(String line) {
            return Pattern.compile("^" + identifier).matcher(line).find();
        }
    }

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("changelog", "Dernières nouveautés du Bot.").addOption(OptionType.STRING, "version", "Le numéro de version (de la forme X.Y.Z). Mettre `list` pour une liste des changelogs existant.", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OptionMapping userInput = event.getOption("version");
        String changelogNumber = (userInput == null) ? Main.version : userInput.getAsString();

        boolean isValidChangelog = isValidChangelogVersion(changelogNumber) || changelogNumber.equals("list");

        if (!isValidChangelog) {
            EmbedBuilder errorBuilder = new EmbedBuilder().setTitle("Numéro de version invalide").setColor(Color.red);
            errorBuilder.addField(changelogNumber + " n'est pas un numéro de version valide", "Le numéro de version doit être de la forme X.Y.Z (ex: " + Main.version + ")", false);
            event.replyEmbeds(errorBuilder.build()).setEphemeral(true).queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder().setTitle("Nouveautés de la version v" + changelogNumber).setColor(new Color(109, 50, 109));

        String changelogDirectory = "changelog/";
        if (!changelogNumber.equals("list")) {
            String changelogFilePath = changelogDirectory + changelogNumber + ".md";

            try {
                List<String> changelog = Files.readAllLines(Path.of(changelogFilePath));
                List<String> other = new ArrayList<>();
                Arrays.stream(ChangeType.values()).iterator().forEachRemaining(
                        changeType -> {
                            builder.appendDescription("## " + changeType.categoryName + ":\n");
                            changelog.forEach(
                                    line -> {
                                        if (changeType.stringStartByIdentifier(line)) {
                                            builder.appendDescription("- " + line.substring(changeType.identifier.length()) + "\n");
                                        } else if (ChangeType.isNotInCategory(line) && !other.contains(line)){
                                            other.add(line);
                                        }
                                    }
                            );
                        }
                );
                builder.appendDescription("## Other:\n");
                other.forEach(line -> builder.appendDescription("- " + line + "\n"));

            } catch (Exception e) {
                builder.setColor(Color.red);
                builder.setDescription("Pas de changelog pour la v" + changelogNumber + " trouvé");
            }
        } else {
            builder.setTitle("Changelogs disponibles:");
            File[] fileList = new File(changelogDirectory).listFiles();

            if (fileList != null) {
                List<String> availableChangelogs = Stream.of(fileList)
                        .filter(file -> !file.isDirectory())
                        .map(File::getName)
                        .toList();

                availableChangelogs.forEach(changelog -> {
                    if (isValidChangelogVersion(changelog.substring(0, 5))) {
                        builder.appendDescription("- " + changelog.substring(0, 5) + "\n");
                    }
                });
            } else {
                builder.appendDescription("**No changelog founds.**");
            }
        }

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    /**
     * Return wether a string is a valid version number (eg: 1.3.2)
     *
     * @param changelog The string to test
     * @return true if the provided string is a valid version number, otherwise false.
     */
    private boolean isValidChangelogVersion(String changelog) {
        return Pattern.compile("(^(\\d+\\.)?(\\d+\\.)?\\d+$)").matcher(changelog).find();
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return false;
    }

    @Override
    public Category category() {
        return Category.DIVERS;
    }

}
