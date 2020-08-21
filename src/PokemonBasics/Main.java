package PokemonBasics;
import Graphics.Window;
import java.util.Calendar;
import java.util.Date;


public class Main {
    static Pokemon[] party = new PokemonBasics.Pokemon[6];
    static Item[] itemBag = new Item[950];
    //[box][spot in box]
    static Pokemon[][] storageSystem = new Pokemon[36][30];

    public static void switchPokemon(int original, int newSlot){
        Pokemon temp = party[original];
        party[original] = party[newSlot];
        party[newSlot] = temp;
    }

    public static void addItemToBag(Item item){
        boolean added = false;
        for(int i=0; i<300; i++){
            if (itemBag[i].id == item.id){
                itemBag[i].add(1);
                added = true;
            }else if (itemBag[i] == null) {
                itemBag[i] = item;
                added = true;
            }
        }
        if (!added) {
            System.out.println("Your Item Bag is full! Discard something to continue.");
        }
    }

    public static void addToPC(int partySlot){
        for (int i = 0; i<36; i++){
            for (int j = 0; j<30; j++){
                if (storageSystem[i][j] == null){
                    storageSystem[i][j] = party[partySlot];
                }
            }
        }
    }

    public static void addToPC(int partySlot, int box, int pcSlot) {
    }


    public static void addToParty(Pokemon pokemon){
        boolean added = false;
        for(int i=0; i<6; i++){
            if (party[i] == null) {
                party[i] = pokemon;
                added = true;
            }
        }
        if (!added) {
            System.out.println("You're already travelling with 6 pokemon! Which one would you like to send to the PC?");
            //addtopc(partyslot)
        }
    }

    //day = 600-1959
    //night = 2000-559
    public static int timeOfDay(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        return (hour*100)+minutes;
    }

    public static void battle(Trainer opponent){
            Pokemon currentMon = party[0];
            Pokemon currentOpponentMon = opponent.party[0];

            //main turn loop
            while (opponent.canBattle()){

            }
    }

    public static void battle(Pokemon opponent){

        //main turn loop
        while (!opponent.isDead()){

        }
    }

    public static void main(String[] args){
        System.out.println(timeOfDay());
        new Window().run();
    }



}
