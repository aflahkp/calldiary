package softfruit.solutions.calldiary.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CallObject extends RealmObject {
    @PrimaryKey
    private long time;
    public String notes;
    public String number;
    public boolean isDone;

    public void setId(long time){
        this.time = time;
    }
}
