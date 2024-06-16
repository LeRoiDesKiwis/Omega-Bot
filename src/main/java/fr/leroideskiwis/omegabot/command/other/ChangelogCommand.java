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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class ChangelogCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("changelog", "Dernières nouveautés du Bot.").addOption(OptionType.STRING, "version", "Le numéro de version (de la forme X.Y.Z)", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OptionMapping userInput = event.getOption("version");
        String changelogNumber = (userInput == null) ? Main.version : userInput.getAsString();

        // check if
        boolean isValidChangelog = Pattern.compile("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$").matcher(changelogNumber).find();
        if (!isValidChangelog){
            EmbedBuilder errorBuilder = new EmbedBuilder().setTitle("Numéro de version invalide").setColor(Color.red);
            errorBuilder.addField(changelogNumber + " n'est pas un numéro de version valide", "Le numéro de version doit être de la forme X.Y.Z (ex: " + Main.version + ")", false);
            event.replyEmbeds(errorBuilder.build()).setEphemeral(true).queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder().setTitle("Nouveautés de la version v" + changelogNumber).setColor(new Color(109, 50, 109));

        String changelogFilePath = "changelog/" + changelogNumber + ".md";

        try {
            List<String> changelog = Files.readAllLines(Path.of(changelogFilePath));
            changelog.forEach(line -> builder.appendDescription(line + "\n"));

        } catch (Exception e) {
            builder.setColor(Color.red);
            builder.setDescription("Pas de changelog pour la v" + changelogNumber + " trouvé");
        }

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
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