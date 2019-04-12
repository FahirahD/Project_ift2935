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

    public ResultSet querybis(String query) throws SQLException {
        if (isConnected()) {

            ResultSet resultSet = null;

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        }
        return null;
    }

    /**
     * Pour se connecter dans l'application
     **/
    public boolean isValidLogin(String userType,String courriel, String password) {

        ResultSet results = query("SELECT courriel, mot_de_passe FROM "+userType.toLowerCase()+" WHERE courriel = '" +courriel+ "' AND mot_de_passe = '" + password + "'");

        return hasResults(results);
    }
/**
 ////////////////////////////////////////////////// Methodes pour l'onglet Acheteur //////////////////////////////////////////////////
 */

    /**
     *	Requete retournant l'histoirque d'achat d'un acheteur
     *
     * //@param username, le nom d'usager
     * ->param userType, le type d'usager
     *
     * @return la liste des precedentes transactions si il y a lieu
     */

    public ResultSet getProductCategorie(String nomCategorie,String courriel){

        ResultSet results = query("select id_prod,titre from produit where categorie = '" +nomCategorie+"' and etat_prod_vente='Affiche' and id_prod NOT in (select id_prod from negociation where courriel_acheteur = '"+courriel+"' and (etat_offre is null or etat_offre = 'accepte'));");

        return results;
    }
    public ResultSet getProductAffiche(String courriel){

        ResultSet results = query("select id_prod,titre from produit where etat_prod_vente='Affiche' and id_prod NOT in (select id_prod from negociation where courriel_acheteur = '"+courriel+"' and (etat_offre is null or etat_offre = 'accepte'));");

        return results;
    }
    public ResultSet getProductCategorieNego(String nomCategorie,String courriel){

        ResultSet results = query("select id_prod,titre from produit where categorie = '" +nomCategorie+"' and id_prod in (select id_prod from negociation where courriel_acheteur = '"+courriel+"');");

        return results;
    }
    public ResultSet getProductAfficheNego(String courriel){

        ResultSet results = query("select id_prod,titre from produit where id_prod in (select id_prod from negociation where courriel_acheteur = '"+courriel+"');");

        return results;
    }
    public ResultSet getProduitAfficheInfo(String idProduit) {

        ResultSet results = query("select nomBoutique,titre,etat,description_produit,numRue,nomRue,codePost,ville from produit natural join ficheProduit where id_prod = '" + idProduit + "' ;");

        return results;
    }
    public ResultSet getPrixInfoNego(String idProduit,String courriel) {

        ResultSet results = query("select prix_offert,etat_offre from negociation where  id_prod = '" + idProduit + "' and courriel_acheteur = '"+courriel+"'");

        return results;
    }
    public ResultSet negocier(String idProduit,String courriel,String prix) {

        ResultSet results = query(" select negocier('" + idProduit + "','" + courriel + "','" + prix + "');");

        return results;
    }

    /**
     // Methodes pour l'onglet Annonceur ////////////////////////////////////////////////////////////////////////////////////
     */
    public ResultSet getBoutique(String courriel) {

        ResultSet results = query("SELECT nomBoutique from vendeur WHERE courriel = '" + courriel + "';");

        return results;
    }
    public ResultSet getProduitVendeur(String nomBoutique) {

        ResultSet results = query("SELECT id_prod,titre,etat_prod_vente from produit WHERE nomBoutique = '" + nomBoutique + "';");

        return results;
    }

    public ResultSet getProduitVendeur2(String idProduit) {

        ResultSet results = query("select categorie,titre,etat,etat_prod_vente,prixEnVente,description_produit,numRue,nomRue,codePost,ville from produit natural join ficheProduit where id_prod = '" + idProduit + "';");

        return results;
    }
    public ResultSet ajouterProduitVendeur(String idBoutique,String categorie,String titre , String prix_souhaite, String etat , String description , String noRue,String nomRue,String codePost , String ville) throws SQLException {

        ResultSet results = querybis(" select ajoutProduit('" + idBoutique + "','" + categorie + "','" + titre + "','" + prix_souhaite + "','" + etat + "','" + description + "','" + noRue + "','" + nomRue + "','" + codePost + "','" + ville + "');");
        return results;
    }

    public ResultSet getCategories() {

        ResultSet results = query("SELECT nom from categories;");

        return results;
    }

    /**
     *
     // Methodes pour l'onglet Expert ////////////////////////////////////////////////////////////////////////////////////
     */


    public ResultSet getProduitToEstimate() {

        ResultSet result = query("select id_prod,titre,prix_souhaite from estimation natural join produit where avis is null;");

        return result;

    }

    public void estimer(String id_prod,String prix) {

        ResultSet result = query("select estimer('"+id_prod+"', '"+prix+"', 'fahirah@udem.com');");
    }


    /**

     --////////////////////////////////////////////////// Requetes de recherche simples //////////////////////////////////////////////////


     * Methode retournant tous les produits en vente de la boutique specifee
     * @return la liste de tous les produits en ventes liste par la boutique specifiee
     */
    public ResultSet requetteSimple1() {

        ResultSet result = query("select *\n" +
                "    from produit\n" +
                "    where nomBoutique in (select nomBoutique \n" +
                "\t\t\t\t\t\t\t\tfrom vendeur\n" +
                "                                        where courriel = 'nec.mauris@Quisquevarius.org');");

        return result;

    }



    /**
     * Methode retournant tous les produits en achetes de l'acheteur specife
     * @return la liste de tous les produits achetes par l'acheteur specifie
     */
    public ResultSet requetteSimple2() {

        ResultSet result = query("select *\n" +
                "    from produit\n" +
                "    where id_prod in (select id_prod \n" +
                "\t\t\t\t\t\t\tfrom vente\n" +
                "                                    where courriel_acheteur ='tortor@aliquamerosturpis.net');");

        return result;

    }


    /**
     * Methode retournant tous les produits en estimé par l'expert specife
     * @return la liste de tous les produits evalues par l'expert specifie
     */
    public ResultSet requetteSimple3() {

        ResultSet result = query("select *\n" +
                "    from produit\n" +
                "    where id_prod in (select id_prod \n" +
                "\t\t\t\t\t\t\tfrom estime_accepte\n" +
                "                                    where courriel_expert ='nunc.id.enim@condimentumDonec.co.uk');");

        return result;

    }


    /**
     * Methode retournant tous les produits vendus sur le site
     * @return la liste de tous les produits en vente
     */
    public ResultSet requetteSimple4() {

        ResultSet result = query("select *\n" +
                "    from produit\n" +
                "    where etat_prod_vente = 'Vente';");

        return result;

    }


    /**
     * Methode retournant tous les produits en pre-vente sur le site
     * @return la liste de tous les produits en pre-vent
     */
    public ResultSet requetteSimple5() {

        ResultSet result = query(" select *\n" +
                "    from produit\n" +
                "    where etat_prod_vente = 'Estimation';");

        return result;

    }




    /**
     * Methode retournant tous les produits en vente sur le site
     * @return la liste des produits en vente
     */
    public ResultSet requetteSimple6() {

        ResultSet result = query("select *\n" +
                "    from produit\n" +
                "    where etat_prod_vente = 'Affiche';");

        return result;

    }

    /**
     * Methode retournant tous les produits
     * @return la liste des produits de la categorie specifiee et dont le prix est inferieur a 10
     */
    public ResultSet requetteSimple7() {

        ResultSet result = query("Select *\n" +
                "    from produit\n" +
                "    where categorie='Outils' AND prixEnVente<10;");

        return result;

    }

    /**
     * Methode retournant l'historique des ventes d'un vendeur specifie
     * @return la liste de tous les produits vendus par le vendeur specifie
     */
    public ResultSet requetteSimple8() {

        ResultSet result = query("SELECT id_prod,categorie,titre, courriel_acheteur, num_transaction, prix_vente\n" +
                "    FROM produit natural join (SELECT id_prod, courriel_acheteur, prix_vente,num_transaction FROM vente) AS temp\n" +
                "    WHERE nomBoutique in (select nomBoutique from vendeur where courriel='dolor@necmollisvitae.edu');");

        return result;

    }


    /**
     --    ////////////////////////////////////////////////// Requetes de recherche complexes //////////////////////////////////////////////////


     * Methode retournant le Nom, prenom, courriel de l’acheteur dont le produit achete proviens de la ville ('Florenceville') et de la catégorie ('Telephones')
     *
     * @return la liste des acheteurs correspondant a ces criteres
     */
    public ResultSet requetteComplexe1() {

        ResultSet result = query("with r1 as( select * from ficheproduit natural join produit),\n" +
                "    r2 as (select id_prod from r1 where ville='Florenceville' and categorie='Téléphones' and etat_prod_vente='Vente'),\n" +
                "    r3 as (select courriel_acheteur from r2 natural join vente),\n" +
                "    r4 as (select distinct nom, prenom, courriel from r3 inner join acheteur on r3.courriel_acheteur=acheteur.courriel)\n" +
                "    select * from r4;");

        return result;

    }

    /**
     * Methode retournant le courriel de tous les experts ayant estime les produits de l’utilisateur'placerat@lacinia.co.uk'
     * @return la liste de tous les experts ayant estime tous les produits de l'utilisateur'placerat@lacinia.co.uk'
     */
    public ResultSet requetteComplexe2() {

        ResultSet result = query(" with r1 as (select id_prod from vendeur natural join produit where courriel='placerat@lacinia.co.uk'),\n" +
                "                r2 as (select courriel_expert from r1 natural join estime_accepte),\n" +
                "                r3 as (select distinct courriel,nom,prenom from r2 inner join expert on courriel_expert=courriel)\n" +
                "        select nom,prenom,courriel from r3;");

        return result;

    }


    /**
     *  Methode retournant les 5 plus grands vendeurs sur la plateforme (par nb de ventes ventes) de la ville'Florenceville'
     *  de la derniere annee (a partir de la date actuelle)
     * @return la liste des 5 plus grands vendeurs de la plateforme
     */
    public ResultSet requetteComplexe3() {

        ResultSet result = query(" with r1 as ( select * from ficheproduit natural join produit),\n" +
                "                r2 as (select id_prod,nomboutique from r1 where ville='Florenceville'),\n" +
                "                r3 as (select r2.nomboutique,id_prod from r2 natural join vente where date_vente >= (CURRENT_TIMESTAMP - INTERVAL'12 months')),\n" +
                "        r4 as (select r3.nomboutique,id_prod from r3 inner join vendeur on r3.nomboutique = vendeur.nomboutique),\n" +
                "        r5 as ( select nomBoutique, count(id_prod) as nb_prod_vendus from r4 group by nomBoutique order by nb_prod_vendus desc)\n" +
                "        select * from r5 limit 5;");

        return result;

    }


    /**
     * Methode retournant le courriel du vendeur ou tous les produits qui ont ete vendus se trouvaient a Florenceville
     * @return la liste de tous les courriels des vendeurs ayant vendus un produit se trouvant a Florenceville
     */
    public ResultSet requetteComplexe4() {

        ResultSet result = query("select courriel\n" +
                "        from vendeur\n" +
                "        where nomBoutique in(select nomBoutique\n" +
                "                from produit as p natural join (select id_prod, prix_vente from vente) as v\n" +
                "                where v.prix_vente < p.prixEnVente and p.id_prod in (select id_prod from ficheProduit where ville ='Florenceville'));");

        return result;

    }

    /**
     * Methode retournant l'historique des estimations de tous les produits evalues et leur decision, avec leur prix de vente si il y a lieu
     */
    public ResultSet requetteComplexe5() {

        ResultSet result = query("select id_prod,categorie,titre, prix_estime as estime_rejete,estime_accepte,prix_vente\n" +
                "        from estimation natural join (select id_prod,prix_estime as estime_accepte from estime_accepte) as a\n" +
                "        natural join (select id_prod,prix_vente from vente) as v\n" +
                "        natural join (select id_prod,categorie,titre from produit) as p;");

        return result;

    }









    private boolean connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PW);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isConnected();
    }


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