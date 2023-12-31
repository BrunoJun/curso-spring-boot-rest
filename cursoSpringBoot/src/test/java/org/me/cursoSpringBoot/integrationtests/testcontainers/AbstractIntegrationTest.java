package org.me.cursoSpringBoot.integrationtests.testcontainers;

import org.me.cursoSpringBoot.CursoSpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.33");

        private static void startContainers(){

            Startables.deepStart(Stream.of(mySQLContainer.withStartupTimeoutSeconds(1000))).join();
        }

        private static Map<String, String> createConnectionConfiguration(){

            return Map.of(
                    "spring.datasource.url", mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username", "root",
                    "spring.datasource.password", mySQLContainer.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            startContainers();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
