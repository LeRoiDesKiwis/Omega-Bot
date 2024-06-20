package fr.leroideskiwis.omegabot.command.fun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import fr.leroideskiwis.omegabot.command.Category;
import fr.leroideskiwis.omegabot.command.Command;
import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.Objects;
import java.io.IOException;
import java.util.Optional;


public class UrbanDictionaryCommand implements Command {

    @Override
    public SlashCommandData commandData() {
        return Commands.slash("definition", "Donne le nombre demandé de définitions de UrbanDictionary")
                .addOption(OptionType.STRING, "recherche", "Ce que vous voulez chercher", true)
                .addOption(OptionType.INTEGER, "définitions", "Le nombre de définitions que vous voulez voir", false);
    }

    @Override
    public void execute(OmegaUser user, SlashCommandInteraction event) {
        OptionMapping definitionsRaw = event.getOption("définitions");
        int definitions = Optional.ofNullable(definitionsRaw)
                .map(OptionMapping::getAsInt)
                .filter(def -> def > 0)
                .orElse(1);

        String searchTerm = Objects.requireNonNull(event.getOption("recherche"), "Search term cannot be null")
                .getAsString();

        try {
            JsonObject jsonResponse = fetchDefinition(searchTerm);
            JsonArray definitionsList = jsonResponse.getAsJsonArray("list");
            int definitionsToDisplay = Math.min(definitionsList.size(), definitions);

            if (definitionsToDisplay > 0) {
                StringBuilder definitionsText = new StringBuilder();

                for (int i = 0; i < definitionsToDisplay; i++) {
                    JsonObject definitionObj = definitionsList.get(i).getAsJsonObject();
                    String definition = cleanDefinition(definitionObj.get("definition").getAsString());
                    definitionsText.append("- ").append(definition).append("\n\n");
                }

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("définitions pour `" + searchTerm + "`")
                        .setColor(Color.BLUE)
                        .setDescription(definitionsText.toString())
                        .build()).queue();
            } else {
                event.reply("Aucune définition trouvée pour \"" + searchTerm + "\".").queue();
            }
        } catch (IOException e) {
            event.reply("La requête à l'API UrbanDictionary a échouée.").queue();
        }
    }

    private String cleanDefinition(String definition) {
        return definition
                // Remove braces found in links. The API doesn't provide the link URLs, so it is impossible to make these links work properly.
                // Replace double newlines by a newline and two spaces because discord markdown doesn't support line breaks in lists.
                .replace("[", "").replace("]", "")
                .replace("\r\n\r\n", "\n").replace("\n\n", "\n").replace("\n", "\n  ");
    }

    private JsonObject fetchDefinition(String searchTerm) throws IOException {
        String urbanDictionaryURL = "https://api.urbandictionary.com/v0/define?term=";
        String url = urbanDictionaryURL + searchTerm;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity!= null) {
                    String responseBody = EntityUtils.toString(entity, "UTF-8");
                    return new Gson().fromJson(responseBody, JsonObject.class);
                }
            }
        }
        // If the reply is null, return an empty definition list. execute() will do error handling.
        return new Gson().fromJson("{\"list\":[]}", JsonObject.class);
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

    @Override
    public Category category() {
        return Category.BOUTIQUE_FUN;
    }

    @Override
    public boolean isBlacklisted() {
        return false;
    }
}

