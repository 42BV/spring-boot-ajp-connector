package nl._42.ajp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tomcat.ajp", ignoreUnknownFields = false)
public class AjpProperties {

    private Integer ajpPort;
    private Boolean remoteAuthentication;
    private Boolean enabled;

    public Integer getAjpPort() {
        return ajpPort;
    }

    public void setAjpPort(Integer ajpPort) {
        this.ajpPort = ajpPort;
    }

    public Boolean getRemoteAuthentication() {
        return remoteAuthentication;
    }

    public void setRemoteAuthentication(Boolean remoteAuthentication) {
        this.remoteAuthentication = remoteAuthentication;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
