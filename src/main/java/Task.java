import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task implements Serializable {
    private String title;
    private RGA content;

    @JsonCreator
    public Task(@JsonProperty("title") String title,
                @JsonProperty("description") RGA description) {
        this.title = title;
        this.content = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RGA getContent() {
        return content;
    }
}