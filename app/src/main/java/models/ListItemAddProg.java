package models;

/**
 * Created by Sudip on 5/4/2018.
 */

public class ListItemAddProg {
    String name;
    int logo;
    public void setData(String name, int logo) {
        this.name = name;
        this.logo = logo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getLogo() {
        return logo;
    }
    public void setLogo(int logo) {
        this.logo = logo;
    }
}
