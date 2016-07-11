package connector.models;


/**
 * Created by hagen on 06/02/16.
 */

public class Internals {

    private String NAME;
    private String NR;
    private String STATE;
    private String TYPE;


    public Internals() {}

    public String getNAME() {
        return NAME;
    }

    public String getNR() {
        return NR;
    }

    public String getSTATE() {
        return STATE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setSTATE(String state) { STATE = state; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Internals internals = (Internals) o;

        if (NAME != null ? !NAME.equals(internals.NAME) : internals.NAME != null) return false;
        if (NR != null ? !NR.equals(internals.NR) : internals.NR != null) return false;
        if (STATE != null ? !STATE.equals(internals.STATE) : internals.STATE != null) return false;
        return TYPE != null ? TYPE.equals(internals.TYPE) : internals.TYPE == null;

    }

    @Override
    public int hashCode() {
        int result = NAME != null ? NAME.hashCode() : 0;
        result = 31 * result + (NR != null ? NR.hashCode() : 0);
        result = 31 * result + (STATE != null ? STATE.hashCode() : 0);
        result = 31 * result + (TYPE != null ? TYPE.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Internals{" +
                "NAME='" + NAME + '\'' +
                ", NR='" + NR + '\'' +
                ", STATE='" + STATE + '\'' +
                ", TYPE='" + TYPE + '\'' +
                '}';
    }
}
