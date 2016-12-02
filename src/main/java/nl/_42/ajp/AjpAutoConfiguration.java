package nl._42.ajp;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.RemoteIpValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.websocket.server.ServerContainer;


@Configuration
@ConditionalOnClass({ Servlet.class, ServerContainer.class })
@ConditionalOnWebApplication
@AutoConfigureBefore(EmbeddedServletContainerAutoConfiguration.class)
public class AjpAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjpAutoConfiguration.class);

    public static final String DEPEND_ON_BEAN = "ajpConnector";

    @Configuration
    @ConditionalOnClass(name = "org.apache.tomcat.websocket.server.WsSci", value = Tomcat.class)
    @EnableConfigurationProperties(AjpProperties.class)
    static class AjpConfiguration {

        private final AjpProperties properties;

        public AjpConfiguration(AjpProperties properties) {
            this.properties = properties;
        }

        @Bean
        @ConditionalOnMissingBean(name = DEPEND_ON_BEAN)
        public EmbeddedServletContainerFactory ajpConnector() {
            LOGGER.info(">>> Creating AJP Servlet Container factory, with port: " + properties.getAjpPort() + " | Remote auth: " + properties.getRemoteAuthentication());
            TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
            tomcat.addAdditionalTomcatConnectors(createConnector());
            tomcat.addContextValves(createRemoteIpValves());
            return tomcat;
        }

        private RemoteIpValve createRemoteIpValves() {
            RemoteIpValve remoteIpValve = new RemoteIpValve();
            remoteIpValve.setRemoteIpHeader("x-forwarded-for");
            remoteIpValve.setProtocolHeader("x-forwarded-proto");
            return remoteIpValve;
        }

        private Connector createConnector() {
            Connector connector = new Connector("AJP/1.3");
            connector.setPort(properties.getAjpPort());
            connector.setScheme("ajp");
            return connector;
        }

    }
}

