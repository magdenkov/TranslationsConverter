import com.google.gson.Gson;
import org.json.JSONObject;

/**
 * Created by denis.magdenkov on 22.07.2014.
 */
public class FileJson {
    private StringBuilder json;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJson(StringBuilder json) {

        this.json = json;
    }

    public StringBuilder getJson() {

        return json;
    }

    @Override
    public String toString() {
        return "FileJson{" +
                "json=" + json.toString() +
                ", name='" + name + '\'' +
                '}';
    }

    public FileJson(String name,StringBuilder json) {
        this.name = name;
        this.json = json;
    }
}
