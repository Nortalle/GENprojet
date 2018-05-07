package Game;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Resources {

    private int scrum;
    private int eau;
    private int bois;
    private int chardon;
    private int petrol;
    private int fer;
    private int cuivre;
    private int acier;
    private int or;

    Resources(int [] ressources){
        scrum = ressources[0];
        eau = ressources[1];
        bois = ressources[2];
        chardon = ressources[3];
        petrol = ressources[4];
        fer = ressources[5];
        cuivre = ressources[6];
        acier = ressources[7];
        or = ressources[8];
    }

    String toJSON(){

        Gson moteurJson = new GsonBuilder().create();

        JsonObject ressources = new JsonObject();
        ressources.add("scrum", new JsonPrimitive(scrum));
        ressources.add("eau", new JsonPrimitive(eau));
        ressources.add("bois", new JsonPrimitive(bois));
        ressources.add("chardon", new JsonPrimitive(chardon));
        ressources.add("petrol", new JsonPrimitive(petrol));
        ressources.add("fer", new JsonPrimitive(fer));
        ressources.add("cuivre", new JsonPrimitive(cuivre));
        ressources.add("acier", new JsonPrimitive(acier));
        ressources.add("or", new JsonPrimitive(or));

        return moteurJson.toJson(ressources);
    }

    void fromJSON(String from){

        Gson moteurJson = new GsonBuilder().create();

        JsonObject ressources = moteurJson.fromJson(from, JsonObject.class);

        scrum = ressources.get("scrum").getAsInt();
        eau = ressources.get("eau").getAsInt();
        bois = ressources.get("bois").getAsInt();
        chardon = ressources.get("chardon").getAsInt();
        petrol = ressources.get("petrol").getAsInt();
        fer = ressources.get("fer").getAsInt();
        cuivre = ressources.get("cuivre").getAsInt();
        acier = ressources.get("acier").getAsInt();
        or = ressources.get("or").getAsInt();

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

    public int getChardon() {
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

}
