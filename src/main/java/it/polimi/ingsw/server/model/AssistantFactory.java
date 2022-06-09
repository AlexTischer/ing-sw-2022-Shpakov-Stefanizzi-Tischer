package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AssistantFactory {

    public AssistantDeck getAssistantDeck() {

        AssistantDeck deck = null;

        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/Assistants.json"));
            deck = gson.fromJson(reader, AssistantDeck.class);

        } catch (Exception ex) {
                ex.printStackTrace();
        }

        return deck;
    }
}

