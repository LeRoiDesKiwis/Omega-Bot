package fr.leroideskiwis.omegabot.games.connect4;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardC4 {

    private final List<PlayerC4> players = new ArrayList<>();
    private PlayerC4 currentPlayer;
    private final List<ColumnC4> columns;
    private final Message message;
    private boolean finished = false;
    private String error = "";

    public BoardC4(Message message, PlayerC4... players){
        this.message = message;
        this.players.addAll(List.of(players));
        this.currentPlayer = players[0];
        this.columns = new ArrayList<>();
        for(int i = 0; i < 7; i++) columns.add(new ColumnC4(String.valueOf(i+1)));
    }

    public void display(){
        StringBuilder builder = new StringBuilder("Au tour de %s (%s)\n".formatted(currentPlayer.getAsMention(), currentPlayer.toString()));
        for(int i = 0; i < 7; i++) {
            builder.append(columns.get(i));
        }
        builder.append("\n");
        for(int i = 5; i >= 0; i--){
            for(ColumnC4 column : columns){
                Optional<PieceC4> piece = column.get(i);
                builder.append(piece.map(PieceC4::toString).orElse(":black_circle:"));
            }
            builder.append("\n");
        }
        builder.append(error);
        message.editMessage(builder.toString()).queue();
    }

    public boolean play(String columnIdentifier){
        if(play1(columnIdentifier)) return true;
        error = "Erreur: tu as fait une erreur >:(\n";
        return false;
    }

    private boolean play1(String columnIdentifier){
        error = "";
        Optional<ColumnC4> column = columns.stream().filter(c -> c.isIdentifier(columnIdentifier)).findFirst();

        if(column.isEmpty()) return false;
        if(column.get().isFull()) return false;
        column.get().addPiece(new PieceC4(currentPlayer));
        Optional<PlayerC4> player = checkWin();
        if(player.isPresent()){
            finished = true;
            error = "Le joueur %s a gagné !".formatted(player.get().getAsMention());
            display();
        }
        return true;
    }

    public boolean isFinished(){
        return finished;
    }

    public boolean isApplicable(MessageReceivedEvent event){
        return message.getChannelIdLong() == event.getChannel().getIdLong() && currentPlayer.isMember(event.getMember());
    }

    public void nextPlayer(){
        this.currentPlayer = currentPlayer.equals(players.get(0)) ? players.get(1) : players.get(0);
    }

    public void forceEnd() {
        finished = true;
        error = "La partie a expirée !";
        display();
    }

    public Optional<PieceC4> getPiece(int i, int j){
        return columns.get(i).get(j);
    }

    public Optional<PlayerC4> checkWin(){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 8; j++){
                TokenSolver solver = new TokenSolver(this, i, j);
                Optional<PlayerC4> player = solver.solve();
                if(player.isPresent()) return player;
            }
        }
        return Optional.empty();
    }
}
