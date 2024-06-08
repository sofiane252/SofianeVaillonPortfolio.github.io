import java.util.*;

/**
 * Created by zulupero on 24/09/2021.
 * Ayoub TAGUIA
 * Sofiane VAILLON
 * S1C2
 */

public class Jeton {
    static final Scanner input = new Scanner(System.in);
    public static String[] state;
    static final String VIDE = "___ ";
    static final int NCASES = 21;
    static final int NLIGNES = 6;
    static final String[] COULEURS = {"B", "R"};

    static boolean estOui(char reponse) {
        return "yYoO".indexOf(reponse) != -1;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean newDeal;
        int scoreBleus = 0;
        int scoreRouges = 0;

        do {
            System.out.println("Jouer contre une IA ? ");
            char reponse = input.next().charAt(0);
            boolean single = estOui(reponse);

            initJeu();
            afficheJeu();

            int numIA = Integer.parseInt(args[0]);

            int val = 1;
            String color = COULEURS[0];
            int pos;

            if (!single) {
                for(int i=0; i<NCASES-1; i++){
                    color = COULEURS[i%2];
                    System.out.println("C'est au joueur "+color+" de jouer !");
                    System.out.print("Entrez une position entre (0 et 20) : ");
                    do{
                        pos = Integer.parseInt(input.next());
                    } 
                    while (jouer(color, val, pos) == false);
                    if ((i % 2) == 1)
                        val++;
                    afficheJeu();
                }
            }
            else {
                for(int i=0; i<NCASES-1; i++){
                    color = COULEURS[i%2];
                    System.out.println("C'est au joueur "+color+" de jouer !");
                    do{
                        if(color == COULEURS[0]) {
                            System.out.print("Entrez une position entre (0 et 20) pour le jeton de valeur " + val + " : ");
                            pos = Integer.parseInt(input.next());
                        }
                        else {
                            pos = choixIA(numIA);
                            System.out.println("Le joueur rouge joue à la position : "+pos);
                        }
                    } 
                    while (jouer(color, val, pos) == false);
                    if ((i % 2) == 1)
                        val++;
                    System.out.println();
                    afficheJeu();
                }
            }

            System.out.println("Fin du jeu !");

            int sumR = sommeVoisins("R",-1);
            int sumB = sommeVoisins("B",-1);

            if ( sumB < sumR) {
                System.out.println("Les bleus gagnent par "+sumB+" à "+sumR);
                scoreBleus += 1;
            }
            else if (sumB == sumR)
                System.out.println("Égalité : "+sumB+" partout !");
            else {
                System.out.println("Les rouges gagnent par "+sumR+" à "+sumB);
                scoreRouges += 1;
            }

            System.out.println("Les rouges ont gagné "+ scoreRouges +" partie(s).");
            System.out.println("Les bleus ont gagné "+ scoreBleus +" partie(s).");

            System.out.println("Nouvelle partie ? ");
            reponse = input.next().charAt(0);
            newDeal = estOui(reponse);
        } while (newDeal);
        /*double ratio = Profiler.analyseInt(Jeton::iaRouge);
        double ratio2 = Profiler.analyseDouble(Jeton::iaRouge);
        double ratio3 = Profiler.method(Jeton::iaRouge);
        System.out.println(ratio+"---"+ratio2+"---"+ratio3);*/
        System.out.println("Bye Bye !");
        System.exit(0);

    }
    
    /**
     * Initialise le jeu avec un double/triple underscore à chaque case, signifiant 'case vide'
     */
    public static void initJeu() {
        state = new String[NCASES];
        for (int i=0 ; i<state.length ; i++)
            state[i] = VIDE;
    }

    /**
     * Affiche le plateau de jeu en mode texte
     */
    public static void afficheJeu(){
        int c = 0;
        int nb_espace = 10;
        for (int i=0 ; i<NLIGNES ; i++) {
            // met l'indice de la 1ère valeur de la ligne
            if (c > 9)
                System.out.print(c+":");
            else
                System.out.print(" "+c+":");
            
            // met les espaces
            for (int j = 0; j<nb_espace; j++)
                System.out.print(' ');
            if ((nb_espace % 2) == 1)
                nb_espace -= 1;
            nb_espace -= 2;

            // écris les differentes valeurs de state sous forme de pyramide
            switch(i){
                case 0: System.out.print(state[0]); 
                        c += i + 1; 
                        break;
                case 1: System.out.print(state[1] + state[2]); 
                        c += i + 1; 
                        break;
                case 2: System.out.print(state[3] + state[4] + state[5]); 
                        c += i + 1; 
                        break;
                case 3: System.out.print(state[6] + state[7] + state[8] + state[9]); 
                        c += i + 1; 
                        break;
                case 4: System.out.print(state[10] + state[11] + state[12] + state[13] + state[14]); 
                        c += i + 1; 
                        break;
                case 5: System.out.print(state[15] + state[16] + state[17] + state[18] + state[19] + state[20]); 
                        c += i + 1; 
                        break;
            }
            System.out.println();
        }
    }

    /**
     * Place un jeton sur le plateau, si possible.
     * @param couleur couleur du jeton : COULEURS[0] ou COULEURS[1]
     * @param val valeur faciale du jeton
     * @param pos position (indice) de l'emplacement où placer le jeton
     * @return true si le jeton a pu être posé, false sinon.
     */
    public static boolean jouer(String couleur, int val, int pos){
        if(val>10 || val<1){
            System.out.println("il y a un probleme sur la valeur");
            return false;
        }
        else if(pos>20 || pos<0){
            System.out.println("il y a un probleme sur la position");
            return false;
        }
        else if (state[pos] != VIDE) {
            System.out.println("la position choisi est deja prise");
            return false;
        }
        else if (val > 9) {
            state[pos] = couleur + String.valueOf(val) + " ";
            return true;
        }
        else {
            state[pos] = " " + couleur + String.valueOf(val) + " ";
            return true;
        }
    }

    /**
     * Retourne l'indice de la case débutant la ligne idLigne
     * @param idLigne indice de la ligne. La première ligne est la ligne #0.
     * @return l'indice de la case la plus à gauche de la ligne
     */
    public static int idDebutLigne(int idLigne){
        return (idLigne * (idLigne + 1)) /2;
    }

    /**
     * Retourne l'indice de la case terminant la ligne idLigne
     * @param idLigne indice de la ligne. La première ligne est la ligne #0.
     * @return l'indice de la case la plus à droite de la ligne
     */
    public static int idFinLigne(int idLigne){
        return idLigne + idDebutLigne(idLigne);
    }

    /** 
    * Donne l'indice de la ligne du Jeton
    * @param idJeton indice du jeton
    * @return la ligne du jeton  
    */
    public static int donneLigne(int idJeton){
        int idLigne = 0;
        if(idJeton == 0)
            idLigne=0;
        if(idJeton >= 1 && idJeton <=2)
            idLigne=1;
        if(idJeton >= 3 && idJeton <=5)
            idLigne=2;
        if(idJeton >= 6 && idJeton <=9)
            idLigne=3;
        if(idJeton >= 10 && idJeton <=14)
            idLigne=4;
        if(idJeton >= 15 && idJeton <=20)
            idLigne=5;
        return idLigne;
    }

    /**
     * Renvoie la position du jeton manquant
     * @return l'indice de la case non occupée
     */
    public static int getIdVide(){
        int caseVide = 0;
        int  cpt = 0;
        for (int i = 0; i<state.length; i++) {
            if (state[i] == VIDE) {
                caseVide = i;
                cpt++;
            }
        }
        if (cpt != 1)
            System.out.println("Probleme fonction getIdVide : compteur != 1");
        return caseVide;
    }

    /**
     * Extrait la valeur du jeton donné,
     * si la couleur correspond
     * @param col couleur du jeton recherché
     * @param idState indice du jeton
     * @return la valeur du jeton donné
     */
    public static int sommeBis(int idState, String col){
        char charac1, charac2, charac3;
        int sum = 0;
        String s;
        // on récupère les 3 caractères 
        charac1 = state[idState].charAt(0);
        charac2 = state[idState].charAt(1);
        charac3 = state[idState].charAt(2);
        if (charac1 == '_')
            return sum;
        // si le premier caractère est un espace la valeur ne peut pas être 10
        if (charac1 == ' ') {
            charac1 = state[idState].charAt(1);
            charac2 = state[idState].charAt(2);
            charac3 = state[idState].charAt(3);
        }
        s = Character.toString(charac1);
        if (s.equals(col)){
            // ici si le 3ème caractère est un 0 la valeur sera 10
            if (charac3 == '0')
                sum = 10;
            else
                sum = Character.getNumericValue(charac2);
        }
        return sum;
    }

    /**
     * fait la somme des poids des voisins de couleur col
     * (6 voisins au maximum)
     * @param col couleur des voisins considérés
     * @return somme des poids
     */
    public static int sommeVoisins(String col, int avecVide){
        int sum = 0;
        int idJetonManquant;
        if (avecVide == -1)
            idJetonManquant = getIdVide();
        else
            idJetonManquant = avecVide;
        int idLigneJeton = donneLigne(idJetonManquant);
        int indiceMax = 20;
        for(int indice=0 ; indice <= indiceMax; indice++){
            if(idJetonManquant == indice){
                // cas du coin supérieur : 0
                if(idLigneJeton == 0){
                    sum  = sommeBis(1, col);
                    sum += sommeBis(2, col);
                }
                // cas de la ligne inférieur
                else if(idLigneJeton == 5){
                    // cas du coin inférieur gauche : 15
                    if(idJetonManquant == 15){
                        sum  = sommeBis(idDebutLigne(idLigneJeton-1), col);
                        sum += sommeBis(idJetonManquant+1, col);
                    }
                    // cas du coin inférieur droit : 20
                    else if(idJetonManquant == 20){
                        sum  = sommeBis(idFinLigne(idLigneJeton-1), col);
                        sum += sommeBis(idJetonManquant-1, col);
                    }
                    // reste de la ligne inférieur
                    else{
                        sum  = sommeBis(idJetonManquant+1, col);
                        sum += sommeBis(idJetonManquant-1, col);
                        sum += sommeBis(idJetonManquant-6, col);
                        sum += sommeBis(idJetonManquant-5, col);
                    }
                }
                // indices de début de ligne, sauf les coins
                else if(idJetonManquant == idDebutLigne(idLigneJeton)){
                    sum  = sommeBis(idJetonManquant+1, col);
                    sum += sommeBis(idDebutLigne(idLigneJeton-1), col);
                    sum += sommeBis(idDebutLigne(idLigneJeton+1), col);
                    sum += sommeBis(idDebutLigne(idLigneJeton+1)+1, col);
                }
                // indices de fin de ligne, sauf les coins
                else if(idJetonManquant == idFinLigne(idLigneJeton)){
                    sum  = sommeBis(idJetonManquant-1, col);
                    sum += sommeBis(idFinLigne(idLigneJeton-1), col);
                    sum += sommeBis(idFinLigne(idLigneJeton+1), col);
                    sum += sommeBis(idFinLigne(idLigneJeton+1)-1, col);
                }
                // le reste des indices (centre / intérieur)
                else{
                    sum  = sommeBis(idJetonManquant-1, col);
                    sum += sommeBis(idJetonManquant+1, col);
                    sum += sommeBis(idJetonManquant-idLigneJeton, col);
                    sum += sommeBis(idJetonManquant-idLigneJeton-1, col);
                    sum += sommeBis(idJetonManquant+idLigneJeton+1, col);
                    sum += sommeBis(idJetonManquant+idLigneJeton+2, col);
                }
            }
        }
        return sum;
    }

    /**
     * Renvoie le prochain coup à jouer pour les rouges en fct de l'ia choisi
     * @param numIA : numéro de la fonction choisi
     * @return id de la case jouer par l'ia
     */
    public static int choixIA(int numIA) {
        int res = 0;
        switch(numIA) {
            case 1: res = iaRouge(); break;
            case 2: res = iaRouge3(); break;
            case 3: res = iaRouge2(); break;
            case 4: res = iaRouge1(); break;
        }
        return res;
    } 

    /**
     * Renvoie le prochain coup à jouer pour les rouges
     * Algo naïf = la première case dispo dans l'ordre des positions
     * @return id de la case
     */
    public static int iaRouge1(){  
        int caseJouer = 0;
        for (int i = 0; i<state.length; i++)
            if (state[i] == VIDE) {
                caseJouer = i;
                break;
            }
        return caseJouer;
    }


        /**
     * Renvoie le prochain coup à jouer pour les rouges
     * Algo naïf = case au hasard 
     * @return id de la case
     */
    public static int iaRouge2(){
        int caseJouer = 0;
        do {
            caseJouer = (int)(Math.random() * 21);
        } while(state[caseJouer] != VIDE);
        return caseJouer;
    }


    /**
     * Renvoie le prochain coup à jouer pour les rouges
     * pose les jetons en commançant par les diagonales
     * puis du haut vers le bas
     * @return id de la case
     */
    public static int iaRouge3(){
        int caseJouer = 0;
        int [] liste = {1,3,6,10,15,0,2,5,9,14,20,7,8,4,11,12,13,17,18,19,16};

        for(int i=0; i<liste.length; i++)
            if(state[liste[i]] == VIDE) {
                caseJouer = liste[i];
                break;
            }
        return caseJouer;
    }


    /**
     * Renvoie le prochain coup à jouer pour les rouges
     * fait la somme des voisins dans chaque case
     * pour pouvoir choisir la meilleur place
     * @return id de la case
     */
    public static int iaRouge(){
            int caseJouer = 0;
            int c = 0;
            //compte le nombre de cases vides
            for(int i=0; i<NCASES; i++)
                if (state[i] != VIDE)
                    c++;
            //joue les 3 premiers coups en random
            if (c < 6) 
                return iaRouge2();
            c = 100;
            for(int i=0; i<NCASES; i++) {
                if(state[i] == VIDE) {
                    int sommeR = sommeVoisins("R",i);
                    int sommeB = sommeVoisins("B",i);
                    // entre dans ce if seulement la première fois
                    if(c == 100) {
                        c = sommeR - sommeB;
                        caseJouer = i;
                    }
                    //change la valeur de l'ancien emplacement si cet emplacement a une valeur plus élevé
                    if(c < (sommeR - sommeB)) {
                        c = sommeR - sommeB;
                        caseJouer = i;
                    }
                }
            }
            return caseJouer;
        }
}
