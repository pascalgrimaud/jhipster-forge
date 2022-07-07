package tech.jhipster.lite.generator.server.springboot.database.mariadb.domain;

import static org.mockito.Mockito.when;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.docker.domain.DockerImage;
import tech.jhipster.lite.docker.domain.DockerImages;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
@ExtendWith(MockitoExtension.class)
class MariaDBModuleFactoryTest {

  @Mock
  private DockerImages dockerImages;

  @InjectMocks
  private MariaDBModuleFactory factory;

  @Test
  void shouldCreateModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture
      .propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("com.jhipster.test")
      .projectBaseName("myapp")
      .build();

    when(dockerImages.get("mariadb")).thenReturn(new DockerImage("mariadb", "0.0.0"));

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleOnProjectWithDefaultPom(module)
      .createFile("documentation/mariadb.md")
      .and()
      .createPrefixedFiles("src/main/java/com/jhipster/test/technical/infrastructure/secondary/mariadb", "DatabaseConfiguration.java")
      .createPrefixedFiles("src/main/docker", "mariadb.yml")
      .createFile("pom.xml")
      .containing("<groupId>org.springframework.boot</groupId>")
      .containing("<artifactId>spring-boot-starter-data-jpa</artifactId>")
      .containing("<groupId>org.mariadb.jdbc</groupId>")
      .containing("<artifactId>mariadb-java-client</artifactId>")
      .containing("<groupId>com.zaxxer</groupId>")
      .containing("<artifactId>HikariCP</artifactId>")
      .containing("<groupId>org.hibernate</groupId>")
      .containing("<artifactId>hibernate-core</artifactId>")
      .containing("<groupId>org.testcontainers</groupId>")
      .containing("<artifactId>mariadb</artifactId>")
      .and()
      .createFile("src/main/resources/config/application.properties")
      .containing("spring.datasource.url=jdbc:mariadb://localhost:3306/myapp")
      .containing("spring.datasource.username=root")
      .containing("spring.datasource.password=")
      .containing("spring.datasource.driver-class-name=org.mariadb.jdbc.Driver")
      .containing("spring.datasource.type=com.zaxxer.hikari.HikariDataSource")
      .containing("spring.datasource.hikari.poolName=Hikari")
      .containing("spring.datasource.hikari.auto-commit=false")
      .containing("spring.data.jpa.repositories.bootstrap-mode=deferred")
      .containing("spring.jpa.hibernate.ddl-auto=none")
      .containing("spring.jpa.hibernate.naming.implicit-strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy")
      .containing("spring.jpa.hibernate.naming.physical-strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy")
      .containing("spring.jpa.open-in-view=false")
      .containing("spring.jpa.properties.hibernate.cache.use_second_level_cache=false")
      .containing("spring.jpa.properties.hibernate.cache.use_query_cache=false")
      .containing("spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true")
      .containing("spring.jpa.properties.hibernate.generate_statistics=false")
      .containing("spring.jpa.properties.hibernate.id.new_generator_mappings=true")
      .containing("spring.jpa.properties.hibernate.jdbc.batch_size=25")
      .containing("spring.jpa.properties.hibernate.jdbc.time_zone=UTC")
      .containing("spring.jpa.properties.hibernate.order_inserts=true")
      .containing("spring.jpa.properties.hibernate.order_updates=true")
      .containing("spring.jpa.properties.hibernate.query.fail_on_pagination_over_collection_fetch=true")
      .containing("spring.jpa.properties.hibernate.query.in_clause_parameter_padding=true")
      .and()
      .createFile("src/test/resources/config/application.properties")
      .containing("spring.datasource.url=jdbc:tc:mariadb")
      .containing("spring.datasource.username=myapp")
      .containing("spring.datasource.password=")
      .containing("spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver")
      .containing("spring.datasource.hikari.maximum-pool-size=2");
  }
}
