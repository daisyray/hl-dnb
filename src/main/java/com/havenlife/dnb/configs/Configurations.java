package com.havenlife.dnb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.ConditionalTemplateConfigurationFactory;
import freemarker.cache.FileExtensionMatcher;
import freemarker.cache.FirstMatchTemplateConfigurationFactory;
import freemarker.cache.NullCacheStorage;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateConfiguration;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configurations {
    @Autowired
    private ObjectMapper jackson;
    @Value("${release.version}")
    private String releaseVersion;
    @Value("${is-development}")
    private boolean isDev;

    private static String APP_RELEASE_VERSION = "APP_RELEASE_VERSION";
    private static String JACKSON = "JACKSON";
    private static String UTILS = "UTILS";
    @Bean
    public Configuration configureFreemarker() {
        try {
            var configs = new Configuration(Configuration.VERSION_2_3_31);

            configs.setAutoImports(Map.of("layout", "/layout/index.ftl"));
            configs.setAllSharedVariables(
                new SimpleHash(
                    Map.of(
                            APP_RELEASE_VERSION, releaseVersion,
                            JACKSON, jackson
                    ),
                    configs.getObjectWrapper()
                )
            );
            var templateConfiguration = new TemplateConfiguration();
            templateConfiguration.setOutputFormat(HTMLOutputFormat.INSTANCE);
            configs.setTemplateConfigurations(
                new FirstMatchTemplateConfigurationFactory(
                    new ConditionalTemplateConfigurationFactory(new FileExtensionMatcher("ftl"), templateConfiguration)
                )
            );
            if (isDev) {
                configs.setCacheStorage(new NullCacheStorage());
                configs.setTemplateUpdateDelayMilliseconds(0);
                configs.setDirectoryForTemplateLoading(Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/static").toFile());
            } else {
                configs.setClassForTemplateLoading(this.getClass(), "/static");
            }
            return configs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
