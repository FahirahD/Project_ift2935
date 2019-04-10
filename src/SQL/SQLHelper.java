package SQL;
import java.sql.*;


/**
 * @author Fahirrah DIARRA
 * @author Mehran ASADI
 * @author Julien KIANG
 * @author Lenny SIEMENI
 *
 * Classe SQLHelper.java, gere la connection et toutes les requetes a la base de donnees
 */
public class SQLHelper {

    // Identifiants de connexion
    private final static String URL = "jdbc:postgresql://postgres.iro.umontreal.ca:5432/asadimeh?currentSchema=projet";
    private final static String USER = "asadimeh_app";
    private final static String PW = "expresso1234";

    // Objet vehiculant la connexion
    private Connection connection;

    // Constructor
    public SQLHelper() {
        connect();
    }

    // Retourne le statut de la connection
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Methode generique qui communique avec la base de donnees
     */
    public ResultSet query(String query) {
        if (isConnected()) {

            ResultSet resultSet = null;

            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                return resultSet;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Pour se connecter dans l'application
     * Vérifie si la combinaison de username et password est bien dans la base de donnees
     *
     * @param userType, le type d'usager (vendeur, acheteur, expert)
     * @param username, l'identifiant de l'usager
     * @param password, le mot de passe
     *
     * @return si la requete trouve ou nom l'usager dans la table correspondante (vide ou non)
     */
    public boolean isValidLogin(String userType,String username, String password) {

        ResultSet results = query("SELECT courriel, mot_de_passe FROM "+userType.toLowerCase()+" WHERE courriel = '" + username + "' AND mot_de_passe = '" + password + "'");

        return hasResults(results);
    }

    ////////////////////////////////////////////////// Methodes pour l'onglet Acheteur //////////////////////////////////////////////////


    /**
     *	Requete retournant l'histoirque d'achat d'un acheteur
     *
     * @param username, le nom d'usager
     * ->param userType, le type d'usager
     *
     * @return la liste des precedentes transactions si il y a lieu
     */
    public ResultSet getBuyerHistory(String username) {
        ResultSet results = query("SELECT nom_produit, prix_propose, annonceur_username, telephone, adresse_facturation FROM Produit INNER JOIN Usager ON annonceur_username = Usager.username INNER JOIN Annonceur ON annonceur_username = Annonceur.username INNER JOIN Offre ON Produit.id = Offre.id WHERE usernameAch='" + username + "' AND etat='vendu'");

        return results;
    }

    /**
     *  Requete retournant l'historique d'achat d'un acheteur
     */
    public ResultSet getBuyerOffers(String username){

        ResultSet results = query("SELECT nom_produit, prix_propose, annonceur_username, telephone, adresse_facturation FROM Produit INNER JOIN Offre ON Produit.id = Offre.id INNER JOIN Usager ON annonceur_username = Usager.username INNER JOIN Annonceur ON annonceur_username = Annonceur.username WHERE usernameAch='" + username + "' AND etat='dispo' ");

        return results;
    }

    /**
     *	Requete retournant tous les objets en vente parmis tous les magasins
     *
     * @return la liste de tous les objets en vente si il y a lieu
     *
     *
     */
    public ResultSet getAvailableItems() {

        ResultSet results = query("SELECT nom_produit, prix_souhaite, annonceur_username FROM Produit WHERE ((etat='dispo') AND (date_exp > CURDATE())) ;");

        return results;
    }

    /**
     *	Recherche de produits à acheter selon un prix inférieur à celui entré
     */
    public ResultSet getItemsCheaperThan(int price) {

        ResultSet results = query("SELECT nom_produit, prix_souhaite, annonceur_username FROM Produit WHERE ((prix_souhaite < " + price + ") AND (etat='dispo') AND (date_exp > CURDATE()));");

        return results;
    }

    /**
     *	Recherche de produits à acheter selon la catégorie choisie
     */
    public ResultSet getItemsByCategory(String category) {

        ResultSet results = query("SELECT nom_produit, prix_souhaite, annonceur_username FROM Produit WHERE ( (nom_categorie ='" + category + "') AND (etat='dispo') AND (date_exp > CURDATE()))");

        return results;
    }

    /**
     *  Recherche de produits selon la catégorie et le prix
     */
    public ResultSet getItemsByCategoryAndPrice(String category, int price){

        ResultSet results = query("SELECT nom_produit, prix_souhaite, annonceur_username FROM Produit "
                + "WHERE ((prix_souhaite < " + price + ") AND (nom_categorie ='" + category + "') "
                + "AND (etat='dispo') AND (date_exp > CURDATE()));");

        return results;
    }

    /**
     *  Requetes retournant toutes les categories d'objets existantes
     *
     * @return la liste des castegories existantes
     */
    public ResultSet getCategoryNames(){

        ResultSet results = query("SELECT nom_categorie FROM Categorie;");

        return results;
    }


    // Methodes pour l'onglet Annonceur ////////////////////////////////////////////////////////////////////////////////////

    /**
     *	Recherche des produits non vendus par l'usager
     */

    public ResultSet getBoutique(String courriel) {

        ResultSet results = query("SELECT nomBoutique from vendeur WHERE courriel = '" + courriel + "';");

        return results;
    }
    public ResultSet getProduitVendeur(String nomBoutique) {

        ResultSet results = query("SELECT id_prod,titre,etat_prod_vente from produit WHERE nomBoutique = '" + nomBoutique + "';");

        return results;
    }

    public ResultSet getUnsoldItems(String seller) {

        ResultSet results = query("SELECT nom_produit, prix_souhaite FROM Produit WHERE ((annonceur_username='" + seller + "') AND (etat='dispo'));");

        return results;
    }

    /**
     *	Recherche des produits non vendus par l'usager dont l'offre est expirée
     */
    public ResultSet getExpiredItems(String seller) {

        ResultSet results = query("SELECT nom_produit, prix_souhaite FROM Produit WHERE ((annonceur_username='" + seller + "') AND (etat='dispo') AND (date_exp < CURDATE()))");

        return results;
    }

    /**
     *	Historique des produits vendus par l'annonceur
     */
    public ResultSet getSellerHistory(String username) {

        ResultSet results = query("SELECT nom_produit, prix_offre, acheteur_username, telephone, adresse_livraison FROM Produit INNER JOIN (SELECT id, usernameAch AS acheteur_username, MAX(prix_propose) AS prix_offre FROM Offre GROUP BY id) AS Temp ON Produit.id = Temp.id INNER JOIN Usager ON acheteur_username = Usager.username INNER JOIN Acheteur ON acheteur_username = Acheteur.username WHERE ((annonceur_username='" + username + "') AND (etat='vendu')) \n");

        return results;
    }

    /**
     *	Historique des offres sur un produit de l`annonceur
     */
    public ResultSet getProductOffers(String seller, String product) {

        ResultSet results = query("SELECT usernameAch,prix_propose FROM Offre JOIN Produit ON Produit.id = Offre.id WHERE ((annonceur_username='" + seller + "') AND (nom_produit='" + product + "'))");

        return results;
    }

    // Methodes pour l'onglet Expert ////////////////////////////////////////////////////////////////////////////////////

    /**
     *	Recherche des produits dans la catégorie de l'expert dont il n'y a pas encore de prix estimé
     */
    public ResultSet getUnevaluatedProducts(String expert) {

        ResultSet results = query("SELECT nom_produit, prix_souhaite, annonceur_username, telephone, adresse_facturation FROM Produit INNER JOIN Usager ON annonceur_username = Usager.username INNER JOIN Annonceur ON annonceur_username = Annonceur.username WHERE (nom_categorie = ANY(SELECT nom_categorie FROM Expertise WHERE usernameExp = '" + expert + "') AND prix_estime='0')");

        return results;
    }

    /**
     *	Historique des produits estimés par l'expert et qui sont vendus
     */
    public ResultSet getExpertHistory(String username) {

        ResultSet results = query("SELECT nom_produit, prix_estime, prix_vendu, annonceur_username, telephone, adresse_facturation FROM Produit INNER JOIN (SELECT id, usernameAch AS acheteur_username, MAX(prix_propose) AS prix_vendu FROM Offre GROUP BY id) AS Temp ON Produit.id = Temp.id INNER JOIN Usager ON annonceur_username = Usager.username INNER JOIN Annonceur ON annonceur_username = Annonceur.username WHERE (etat='vendu' AND expert_username='" + username + "')");

        return results;
    }

    /**
     *	Query utilse a l'application
     */




    // Private helping methods /////////////////////////////////////////////////////////////////////////////////

    // Attempt connection to database
    private boolean connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PW);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isConnected();
    }

    // Test if results have been returned
    private boolean hasResults (ResultSet results) {

        if( results != null) {
            try {
                if (results.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}