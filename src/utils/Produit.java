package utils;

public class Produit {

    String id;
    String titre;

    public Produit(String id, String titre) {
        this.id = id;
        this.titre = titre;

    }
    public String getId (){
        return id;
    }
    public String getTitre() {
        return titre;
    }

}