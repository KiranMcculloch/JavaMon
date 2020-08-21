package PokemonBasics;

public class Trainer {
    final Pokemon[] party;
    final String name;

    public Trainer(String name, Pokemon[] party){
        this.party = party;
        this.name = name;
    }

    public boolean canBattle(){
        int x = 0;
        for(int i = 0; i < 6; i++){
            if (this.party[i].isDead()){
                x+=1;
            }
        }
        if (x>=6){ return false; } else{ return true; }
    }
}
