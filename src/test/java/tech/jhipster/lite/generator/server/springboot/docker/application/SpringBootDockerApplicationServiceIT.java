package tech.jhipster.lite.generator.server.springboot.docker.application;

import static tech.jhipster.lite.TestUtils.assertFileContent;
import static tech.jhipster.lite.TestUtils.assertFileExist;
import static tech.jhipster.lite.TestUtils.tmpProject;
import static tech.jhipster.lite.generator.tools.domain.Constants.POM_XML;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.jhipster.lite.IntegrationTest;
import tech.jhipster.lite.common.domain.WordUtils;
import tech.jhipster.lite.generator.buildtool.maven.application.MavenApplicationService;
import tech.jhipster.lite.generator.project.application.ProjectApplicationService;
import tech.jhipster.lite.generator.tools.domain.Project;

@IntegrationTest
class SpringBootDockerApplicationServiceIT {

  @Autowired
  SpringBootDockerApplicationService springBootDockerApplicationService;

  @Autowired
  ProjectApplicationService projectApplicationService;

  @Autowired
  MavenApplicationService mavenApplicationService;

  @Test
  void shouldAddJib() {
    Project project = tmpProject();
    projectApplicationService.init(project);
    mavenApplicationService.addPomXml(project);

    springBootDockerApplicationService.addJib(project);

    assertFileExist(project, "src/main/docker/jib/entrypoint.sh");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.version>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.version>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.image>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.image>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.architecture>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.architecture>");

    assertFileContent(project, POM_XML, mavenJibPlugin(project));
  }

  @Test
  void shouldAddJibFiles() {
    Project project = tmpProject();
    projectApplicationService.init(project);
    mavenApplicationService.addPomXml(project);

    springBootDockerApplicationService.addJibFiles(project);

    assertFileExist(project, "src/main/docker/jib/entrypoint.sh");

    assertFileContent(
      project,
      "src/main/docker/jib/entrypoint.sh",
      project.getPackageName().get() + "." + WordUtils.upperFirst(project.getBaseName().get()) + "App"
    );
  }

  @Test
  void shouldAddJibPlugin() {
    Project project = tmpProject();
    projectApplicationService.init(project);
    mavenApplicationService.addPomXml(project);

    springBootDockerApplicationService.addJibPlugin(project);

    assertFileContent(project, POM_XML, "<jib-maven-plugin.version>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.version>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.image>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.image>");

    assertFileContent(project, POM_XML, "<jib-maven-plugin.architecture>");
    assertFileContent(project, POM_XML, "</jib-maven-plugin.architecture>");

    assertFileContent(project, POM_XML, mavenJibPlugin(project));
  }

  @Test
  void shouldAddDockerfile() {
    Project project = tmpProject();
    projectApplicationService.init(project);

    springBootDockerApplicationService.addDockerfile(project);

    assertFileExist(project, "Dockerfile");
    assertFileContent(project, "Dockerfile", "EXPOSE 8080");
  }

  @Test
  void shouldAddDockerfileWithDifferentPort() {
    Project project = tmpProject();
    project.addConfig("serverPort", 7419);
    projectApplicationService.init(project);

    springBootDockerApplicationService.addDockerfile(project);

    assertFileExist(project, "Dockerfile");
    assertFileContent(project, "Dockerfile", "EXPOSE 7419");
  }

  private List<String> mavenJibPlugin(Project project) {
    return List.of(
      "<plugin>",
      "<groupId>com.google.cloud.tools</groupId>",
      "<artifactId>jib-maven-plugin</artifactId>",
      "<version>${jib-maven-plugin.version}</version>",
      "<configuration>",
      "<from>",
      "<image>${jib-maven-plugin.image}</image>",
      "<platforms>",
      "<platform>",
      "<architecture>${jib-maven-plugin.architecture}</architecture>",
      "<os>linux</os>",
      "</platform>",
      "</platforms>",
      "</from>",
      "<to>",
      "<image>" + project.getBaseName().orElse("jhipster") + ":latest</image>",
      "</to>",
      "<container>",
      "<entrypoint>",
      "<shell>bash</shell>",
      "<option>-c</option>",
      "<arg>/entrypoint.sh</arg>",
      "</entrypoint>",
      "<ports>",
      "<port>" + project.getServerPort() + "</port>",
      "</ports>",
      "<environment>",
      "<SPRING_OUTPUT_ANSI_ENABLED>ALWAYS</SPRING_OUTPUT_ANSI_ENABLED>",
      "<JHIPSTER_SLEEP>0</JHIPSTER_SLEEP>",
      "</environment>",
      "<creationTime>USE_CURRENT_TIMESTAMP</creationTime>",
      "<user>1000</user>",
      "</container>",
      "<extraDirectories>",
      "<paths>src/main/docker/jib</paths>",
      "<permissions>",
      "<permission>",
      "<file>/entrypoint.sh</file>",
      "<mode>755</mode>",
      "</permission>",
      "</permissions>",
      "</extraDirectories>",
      "</configuration>",
      "</plugin>"
    );
  }
}
