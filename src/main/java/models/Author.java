package models;

import com.sleepycat.persist.model.Entity;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;

/**
 * Created by alexhao on 2017/3/6.
 */

public class Author {
    private int ID;
    private String NAME;
    private String INFO;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getINFO() {
        return INFO;
    }

    public void setINFO(String INFO) {
        this.INFO = INFO;
    }
}
