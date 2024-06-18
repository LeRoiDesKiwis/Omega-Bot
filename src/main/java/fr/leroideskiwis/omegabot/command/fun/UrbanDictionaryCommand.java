package fr.leroideskiwis.omegabot.command.fun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.util.Objects;
import java.io.IOException;


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
        int definitions = (definitionsRaw == null || Objects.requireNonNull(definitionsRaw).getAsInt() <= 0) ? 1 : definitionsRaw.getAsInt();

        String searchTerm = Objects.requireNonNull(event.getOption("recherche")).getAsString();

        try {
            JsonObject jsonResponse = fetchDefinition(searchTerm);

            JsonArray definitionsList = jsonResponse.getAsJsonArray("list");
            int definitionsToDisplay = java.lang.Math.min(definitionsList.size(), definitions);

            if (definitionsToDisplay > 0) {
                StringBuilder definitionsText = new StringBuilder();
                for (int i = 0; i < definitionsToDisplay; i++) {
                    String definition =  definitionsList
                            .get(i).getAsJsonObject()
                            .get("definition").getAsString()
                            .replace("[", "").replace("]", "");

                    definitionsText.append((i + 1)).append(": ").append(definition).append("\n");
                }
                event.reply(definitionsText.toString()).queue();
            } else {
                event.reply("Aucune définition trouvée pour \"" + searchTerm + "\".").queue();
            }
        } catch (java.io.IOException e) {
            event.reply("La requête à l'API UrbanDictionary a échouée.").queue();
        }
    }

    private JsonObject fetchDefinition(String searchTerm) throws IOException {
        String url = "http://api.urbandictionary.com/v0/define?term=" + searchTerm;

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
        throw new RuntimeException("Failed to fetch definition");
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

