package sellerofair.Translator;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Request {

    private @Id @GeneratedValue Long id;
    private String time;
    private String IP;
    private String input;
    private ConnectionResult connectionResult;

    Request() {}

    Request(String time, String IP, Input input, ConnectionResult connectionResult) {

        this.time = time;
        this.IP = IP;
        this.input = input.toString();
        this.connectionResult = connectionResult;
    }

    public Long getId() {
        return this.id;
    }

    public String getTime() {
        return this.time;
    }

    public String getIP() {
        return this.IP;
    }

    public String getInput() {
        return this.input;
    }

    public ConnectionResult getConnectionResult() {
        return connectionResult;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setInput(Input input) { this.input = input.toString(); }

    public void setConnectionResult(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) &&
                Objects.equals(time, request.time) &&
                Objects.equals(IP, request.IP) &&
                Objects.equals(input, request.input) &&
                connectionResult == request.connectionResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, IP, input, connectionResult);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", IP='" + IP + '\'' +
                ", input='" + input + '\'' +
                ", connectionResult=" + connectionResult +
                '}';
    }
}