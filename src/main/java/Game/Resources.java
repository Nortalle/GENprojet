package Game;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Class which represents all the resources of a player
 *
 * @author Vincent Guidoux
 */
public class Resources {
    private static final int SCRUM_ID = 0;
    private static final int EAU_ID = 1;
    private static final int BOIS_ID = 2;
    private static final int CHARBON_ID = 3;
    private static final int PETROL_ID = 4;
    private static final int FER_ID = 5;
    private static final int CUIVRE_ID = 6;
    private static final int ACIER_ID = 7;
    private static final int OR_ID = 8;

    private int scrum;//unité monétaire du jeu
    private int eau;
    private int bois;
    private int chardon;
    private int petrol;
    private int fer;
    private int cuivre;
    private int acier;
    private int or;

    /**
     * constructor which init all the resources with the given value
     *
     * @param value : value to initialize all the resources
     */
    public Resources(int value){
        setAll(value);
    }

    /**
     * constructor which init all the resources with the given tab
     *
     * @param resources tab of resources (Scrum, Eau, Bois, Charbon, Petrol, Fer, Cuivre, Acier, Or); -1 means unknown
     */
    public Resources(int [] resources){
        scrum = resources[0];
        eau = resources[1];
        bois = resources[2];
        chardon = resources[3];
        petrol = resources[4];
        fer = resources[5];
        cuivre = resources[6];
        acier = resources[7];
        or = resources[8];
    }

    /**
     * constructor which init all the resources with the given json
     *
     * @param json : json of the resources
     */
    public Resources(String json){
        fromJSON(json);
    }

    /**
     *
     * @return the resources Jsonified
     */
    public String toJSON(){
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject resources = new JsonObject();
        resources.add("scrum", new JsonPrimitive(scrum));
        resources.add("eau", new JsonPrimitive(eau));
        resources.add("bois", new JsonPrimitive(bois));
        resources.add("chardon", new JsonPrimitive(chardon));
        resources.add("petrol", new JsonPrimitive(petrol));
        resources.add("fer", new JsonPrimitive(fer));
        resources.add("cuivre", new JsonPrimitive(cuivre));
        resources.add("acier", new JsonPrimitive(acier));
        resources.add("or", new JsonPrimitive(or));

        return jsonEngine.toJson(resources);
    }

    /**
     * Sets all the resources to zero
     */
    public void init(){

        setAll(0);
    }

    /**
     * Sets the object with the given Json
     *
     * @param from : Json to sets the object
     */
    public void fromJSON(String from){
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject resources = jsonEngine.fromJson(from, JsonObject.class);

        scrum = resources.get("scrum").getAsInt();
        eau = resources.get("eau").getAsInt();
        bois = resources.get("bois").getAsInt();
        chardon = resources.get("chardon").getAsInt();
        petrol = resources.get("petrol").getAsInt();
        fer = resources.get("fer").getAsInt();
        cuivre = resources.get("cuivre").getAsInt();
        acier = resources.get("acier").getAsInt();
        or = resources.get("or").getAsInt();
    }


    public int getScrum() {
        return scrum;
    }

    public void setScrum(int scrum) {
        this.scrum = scrum;
    }

    public int getEau() {
        return eau;
    }

    public void setEau(int eau) {
        this.eau = eau;
    }

    public int getBois() {
        return bois;
    }

    public void setBois(int bois) {
        this.bois = bois;
    }

    public int getCharbon() {
        return chardon;
    }

    public void setChardon(int chardon) {
        this.chardon = chardon;
    }

    public int getPetrol() {
        return petrol;
    }

    public void setPetrol(int petrol) {
        this.petrol = petrol;
    }

    public int getFer() {
        return fer;
    }

    public void setFer(int fer) {
        this.fer = fer;
    }

    public int getCuivre() {
        return cuivre;
    }

    public void setCuivre(int cuivre) {
        this.cuivre = cuivre;
    }

    public int getAcier() {
        return acier;
    }

    public void setAcier(int acier) {
        this.acier = acier;
    }

    public int getOr() {
        return or;
    }

    public void setOr(int or) {
        this.or = or;
    }

    /**
     * Sets all the resources to the given value
     * @param all : value to set all the ressources
     */
    public void setAll(int all){
        scrum = all;
        eau = all;
        bois = all;
        chardon = all;
        petrol = all;
        fer = all;
        cuivre = all;
        acier = all;
        or = all;
    }

    public int[] toArray() {
        int array[] = {scrum, eau, bois, chardon, petrol, fer, cuivre, acier, or};
        return array;
    }
}
