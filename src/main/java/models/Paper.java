package models;

/**
 * Created by alexhao on 2017/3/6.
 */
public class Paper {
    private int ID;
    private String TITLE;
    private String ABSTRACT;
    private int PUB;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getABSTRACT() {
        return ABSTRACT;
    }

    public void setABSTRACT(String ABSTRACT) {
        this.ABSTRACT = ABSTRACT;
    }

    public int getPUB() {
        return PUB;
    }

    public void setPUB(int PUB) {
        this.PUB = PUB;
    }
}
