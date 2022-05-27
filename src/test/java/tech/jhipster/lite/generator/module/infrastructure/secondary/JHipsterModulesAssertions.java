package tech.jhipster.lite.generator.module.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import tech.jhipster.lite.generator.module.domain.JHipsterModule;
import tech.jhipster.lite.generator.module.domain.JHipsterProjectFolder;

public final class JHipsterModulesAssertions {

  private JHipsterModulesAssertions() {}

  public static ModuleAsserter assertThatModule(JHipsterModule module) {
    return new ModuleAsserter(module);
  }

  public static class ModuleAsserter {

    private static final String SLASH = "/";

    private static final FileSystemJHipsterModulesRepository repository = new FileSystemJHipsterModulesRepository();

    private final JHipsterProjectFolder projectFolder;

    private ModuleAsserter(JHipsterModule module) {
      assertThat(module).as("Can't make assertions on a module without module").isNotNull();

      repository.apply(module);
      projectFolder = module.projectFolder();
    }

    public ModuleAsserter createJavaSources(String... files) {
      return createPrefixedFiles("src/main/java", files);
    }

    public ModuleAsserter createJavaTests(String... files) {
      return createPrefixedFiles("src/test/java", files);
    }

    public ModuleAsserter createPrefixedFiles(String prefix, String... files) {
      assertThat(files).as("Can't check null files for a module").isNotNull();

      String[] sourceFiles = Stream.of(files).map(file -> prefix + SLASH + file).toArray(String[]::new);

      return createFiles(sourceFiles);
    }

    public ModuleAsserter createFiles(String... files) {
      assertThat(files).as("Can't check null files for a module").isNotNull();

      SoftAssertions assertions = new SoftAssertions();
      Stream.of(files).map(file -> projectFolder.filePath(file)).forEach(assertFileExist(assertions));
      assertions.assertAll();

      return this;
    }

    private Consumer<Path> assertFileExist(SoftAssertions assertions) {
      return path -> assertions.assertThat(Files.exists(path)).as(fileNotFoundMessage(path, projectFolder)).isTrue();
    }

    public ModuleFileAsserter createFile(String file) {
      return new ModuleFileAsserter(this, file);
    }
  }

  public static class ModuleFileAsserter {

    private final ModuleAsserter moduleAsserter;
    private final String file;

    private ModuleFileAsserter(ModuleAsserter moduleAsserter, String file) {
      assertThat(file).as("Can't check file without file path").isNotBlank();

      this.moduleAsserter = moduleAsserter;
      this.file = file;
      assertFileExists();
    }

    private void assertFileExists() {
      Path path = moduleAsserter.projectFolder.filePath(file);

      assertThat(Files.exists(path)).as(fileNotFoundMessage(path, moduleAsserter.projectFolder)).isTrue();
    }

    public ModuleFileAsserter containing(String content) {
      assertThat(content).as("Can't check blank content").isNotBlank();

      try {
        Path path = moduleAsserter.projectFolder.filePath(file);

        assertThat(Files.readString(path)).as(() -> "Can't find " + content + " in " + path.toString()).contains(content);
      } catch (IOException e) {
        throw new AssertionError("Can't check file content: " + e.getMessage(), e);
      }

      return this;
    }

    public ModuleAsserter and() {
      return moduleAsserter;
    }
  }

  private static Supplier<String> fileNotFoundMessage(Path path, JHipsterProjectFolder projectFolder) {
    return () -> "Can't find file " + path + " in project folder, found " + projectFiles(projectFolder);
  }

  private static String projectFiles(JHipsterProjectFolder projectFolder) {
    try {
      return Files
        .walk(Paths.get(projectFolder.folder()))
        .filter(Files::isRegularFile)
        .map(Path::toString)
        .collect(Collectors.joining(", "));
    } catch (IOException e) {
      return "unreadable folder";
    }
  }
}
