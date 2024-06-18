package fr.leroideskiwis.omegabot.games.connect4;

import fr.leroideskiwis.omegabot.user.OmegaUser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardC4 {

    private final List<PlayerC4> players = new ArrayList<>();
    private PlayerC4 currentPlayer;
    private final List<ColumnC4> columns;
    private final Message message;

    public BoardC4(Message message, PlayerC4... players){
        this.message = message;
        this.players.addAll(List.of(players));
        this.currentPlayer = players[0];
        this.columns = new ArrayList<>();
        for(int i = 0; i < 7; i++) columns.add(new ColumnC4(String.valueOf(i)));
    }

    public void display(){
        StringBuilder builder = new StringBuilder();
        for(int i = 5; i >= 0; i--){
            for(ColumnC4 column : columns){
                Optional<String> piece = column.get(i);
                builder.append(piece.orElse(":black_circle:"));
            }
            builder.append("\n");
        }
        message.editMessage(builder.toString()).queue();
    }

    public boolean play(OmegaUser user, String columnIdentifier){
        if(!currentPlayer.isUser(user)) return false;
        Optional<ColumnC4> column = columns.stream().filter(c -> c.isIdentifier(columnIdentifier)).findFirst();

        if(column.isEmpty()) return false;
        if(column.get().isFull()) return false;
        column.get().addPiece(new PieceC4(currentPlayer));
        return true;
    }

}
