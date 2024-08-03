package fr.leroideskiwis.omegabot.games.connect4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColumnC4 {

    private List<PieceC4> pieces = new ArrayList<>();
    private final String identifier;

    public ColumnC4(String identifier){
        this.identifier = identifier;
    }

    public void addPiece(PieceC4 piece){
        pieces.add(piece);
    }

    public boolean isFull(){
        return pieces.size() >= 6;
    }

    public Optional<PieceC4> get(int i){
        return pieces.size() > i ? Optional.of(pieces.get(i)) : Optional.empty();
    }

    public boolean isIdentifier(String identifier){
        return this.identifier.equals(identifier);
    }

    public String toString(){
        return ":number_%s:".formatted(identifier);
    }
}
