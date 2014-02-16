import javax.persistence.*;
import java.net.Proxy;

/**
 * Created by jkkjl on 12/17/13.
 */
@Entity
@Table(name = "PROXYUNIT")
public class ProxyUnit {
    @Id
    @GeneratedValue
    private Long id;
    private Proxy proxy;
    @Column(name = "host")
    private String host;
    @Column(name = "port")
    private int port;
    @Column(name = "isAvaliable")
    boolean isAvaliable;

    public ProxyUnit(boolean isAvaliable, String host, int port) {
        this.isAvaliable = isAvaliable;
        this.port = port;
        this.host = host;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public ProxyUnit() {
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void banUser() {
    }
}
