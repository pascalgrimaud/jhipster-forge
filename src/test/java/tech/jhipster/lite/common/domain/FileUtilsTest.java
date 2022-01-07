package tech.jhipster.lite.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static tech.jhipster.lite.TestUtils.assertFileNotExist;
import static tech.jhipster.lite.common.domain.FileUtils.*;
import static tech.jhipster.lite.common.domain.WordUtils.LF;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.error.domain.GeneratorException;
import tech.jhipster.lite.error.domain.MissingMandatoryValueException;

@UnitTest
class FileUtilsTest {

  @Nested
  class ExistsTest {

    @Test
    void shouldExists() {
      String tmp = FileUtils.tmpDir();

      assertTrue(FileUtils.exists(tmp));
    }

    @Test
    void shouldNotExistsForNull() {
      assertThatThrownBy(() -> FileUtils.exists(null))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("path");
    }

    @Test
    void shouldNotExistsForBlank() {
      assertThatThrownBy(() -> FileUtils.exists(null))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("path");
    }
  }

  @Nested
  class CreateFolderTest {

    @Test
    void shouldNotCreateFolderForNull() {
      assertThatThrownBy(() -> FileUtils.createFolder(null))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("path");
    }

    @Test
    void shouldNotCreateFolderForBlank() {
      assertThatThrownBy(() -> FileUtils.createFolder(null))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("path");
    }

    @Test
    void shouldCreateFolderOnlyOnceTime() {
      String path = FileUtils.tmpDirForTest();

      assertFileNotExist(path);

      assertThatCode(() -> FileUtils.createFolder(path)).doesNotThrowAnyException();
      assertTrue(FileUtils.exists(path));

      assertThatCode(() -> FileUtils.createFolder(path)).doesNotThrowAnyException();
      assertTrue(FileUtils.exists(path));
    }

    @Test
    void shouldNotCreateFolderWhenItsAFile() throws Exception {
      String path = FileUtils.tmpDirForTest();
      String destinationFile = getPath(path, "chips");

      assertFalse(FileUtils.exists(path));
      assertThatCode(() -> FileUtils.createFolder(path)).doesNotThrowAnyException();
      assertTrue(FileUtils.exists(path));
      Files.createFile(FileUtils.getPathOf(destinationFile));

      assertThatThrownBy(() -> FileUtils.createFolder(destinationFile)).isInstanceOf(IOException.class);
    }
  }

  @Nested
  class GetPathTest {

    @Test
    void shouldGetPath() {
      String result = getPath("chips", "beer");

      assertThat(result).isEqualTo("chips/beer");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void shouldGetPathForLinux() {
      String result = getPath("/home/chips/beer");

      assertThat(result).isEqualTo(File.separator + "home" + File.separator + "chips" + File.separator + "beer");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldGetPathForWindows() {
      String result = getPath("C:\\chips\\beer");

      assertThat(result).isEqualTo("C:/chips/beer");
    }

    @Test
    void shouldGetPathOf() {
      Path result = FileUtils.getPathOf("chips", "beer");

      assertThat(result).isEqualTo(Path.of("chips" + File.separator + "beer"));
    }

    @Test
    void shouldGetInputStream() {
      InputStream in = FileUtils.getInputStream("generator/mustache/README.txt");

      assertThat(in).isNotNull();
    }

    @Test
    void shouldNotGetInputStream() {
      assertThatThrownBy(() -> FileUtils.getInputStream("generator/mustache/chips.txt")).isExactlyInstanceOf(GeneratorException.class);
    }
  }

  @Nested
  class DetectEndOfLineTest {

    @Test
    void shouldDetectLf() throws Exception {
      Path tempFile = Files.createTempFile("detect-lf", null);
      Files.writeString(tempFile, "my little file \nwith lf");

      assertThat(detectEndOfLine(tempFile.toString())).hasValue(LF);
    }

    @Test
    void shouldDetectCrLf() throws Exception {
      Path tempFile = Files.createTempFile("detect-crlf", null);
      Files.writeString(tempFile, "my little file \r\nwith crlf");

      assertThat(detectEndOfLine(tempFile.toString())).hasValue(WordUtils.CRLF);
    }

    @Test
    void shouldNotDetectEndOfLine() throws Exception {
      Path tempFile = Files.createTempFile("detect-none", null);
      Files.writeString(tempFile, "my little file without new line");

      assertThat(detectEndOfLine(tempFile.toString())).isEmpty();
    }
  }

  @Nested
  class ReadTest {

    @Test
    void shouldRead() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/readme-short.md");

      String result = FileUtils.read(filename);

      String lineSeparator = detectEndOfLine(filename).orElse("\0");
      String expectedResult = new StringBuilder()
        .append("this is a short readme")
        .append(lineSeparator)
        .append("used for unit tests")
        .append(lineSeparator)
        .append("powered by JHipster \uD83E\uDD13")
        .append(lineSeparator)
        .toString();
      assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldNotReadWhenFileNotExist() {
      String filename = getPath("src/test/resources/generator/utils/unknown.md");

      assertThatThrownBy(() -> FileUtils.read(filename)).isExactlyInstanceOf(NoSuchFileException.class);
    }
  }

  @Nested
  class GetLineTest {

    @Test
    void shouldGetLine() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/readme-short.md");

      assertThat(FileUtils.getLine(filename, "used for unit tests")).isEqualTo(2);
      assertThat(FileUtils.getLine(filename, "JHipster")).isEqualTo(3);
    }

    @Test
    void shouldNotGetLineAsCaseSensitive() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/readme-short.md");

      assertThat(FileUtils.getLine(filename, "jhipster")).isEqualTo(-1);
    }

    @Test
    void shouldNotGetLineForAnotherText() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/readme-short.md");

      assertThat(FileUtils.getLine(filename, "beer")).isEqualTo(-1);
    }

    @Test
    void shouldNotGetLineWhenFileNotExist() {
      String filename = getPath("src/test/resources/generator/utils/unknown.md");

      assertThatThrownBy(() -> FileUtils.getLine(filename, "beer")).isInstanceOf(IOException.class);
    }
  }

  @Nested
  class ContainsInLineTest {

    @Test
    void shouldContainsInLine() {
      String filename = getPath("src", "test", "resources", "generator", "utils", "example-readme.md");

      assertTrue(FileUtils.containsInLine(filename, "Before you can build this project"));
    }

    @Test
    void shouldNotContainsInLine() {
      String filename = getPath("src", "test", "resources", "generator", "utils", "example-readme.md");

      assertFalse(FileUtils.containsInLine(filename, "apero with beers"));
    }

    @Test
    void shouldNotContainsInLineWhenFilenameNotExist() {
      String filename = getPath("src", "test", "resources", "generator", "utils", "unknown.md");

      assertFalse(FileUtils.containsInLine(filename, "apero with beers"));
    }
  }

  @Nested
  class ContainsLinesTest {

    @Test
    void shouldContainsLinesSingle() {
      String filename = getPath("src/test/resources/generator/buildtool/maven/pom.test.xml");
      List<String> lines = List.of("<dependency>");

      assertTrue(FileUtils.containsLines(filename, lines));
    }

    @Test
    void shouldContainsLines() {
      String filename = getPath("src/test/resources/generator/buildtool/maven/pom.test.xml");
      List<String> lines = List.of(
        "<dependency>",
        "<groupId>org.junit.jupiter</groupId>",
        "<artifactId>junit-jupiter-engine</artifactId>",
        "<version>${junit-jupiter.version}</version>",
        "<scope>test</scope>",
        "</dependency>"
      );

      assertTrue(FileUtils.containsLines(filename, lines));
    }

    @Test
    void shouldNotContainsLines() {
      String filename = getPath("src/test/resources/generator/buildtool/maven/pom.test.xml");
      List<String> lines = List.of(
        "<dependency>",
        "<groupId>org.junit.jupiter</groupId>",
        "<artifactId>junit-jupiter-engine</artifactId>",
        "<version>${junit-jupiter.version}</version>",
        "<scope>WRONG_SCOPE</scope>",
        "</dependency>"
      );

      assertFalse(FileUtils.containsLines(filename, lines));
    }

    @Test
    void shouldNotContainsLinesWhenFileNotExist() {
      String filename = getPath("chips.txt");
      List<String> lines = List.of("chips");

      assertFalse(FileUtils.containsLines(filename, lines));
    }

    @Test
    void shouldNotContainsLinesWithNullFilename() {
      List<String> list = List.of();
      assertThatThrownBy(() -> FileUtils.containsLines(null, list))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("filename");
    }

    @Test
    void shouldNotContainsLinesWithBlankFilename() {
      List<String> list = List.of();
      assertThatThrownBy(() -> FileUtils.containsLines(" ", list))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("filename");
    }

    @Test
    void shouldNotContainsLinesWithEmptyLines() {
      String filename = getPath("src/test/resources/generator/buildtool/maven/pom.test.xml");
      List<String> list = List.of();
      assertThatThrownBy(() -> FileUtils.containsLines(filename, list))
        .isExactlyInstanceOf(MissingMandatoryValueException.class)
        .hasMessageContaining("lines");
    }
  }

  @Nested
  class ReplaceTest {

    @Test
    void shouldReplaceInFile() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/readme-short.md");

      String result = FileUtils.replaceInFile(filename, "powered by JHipster \uD83E\uDD13", "Hello JHipster Lite");

      String lineSeparator = detectEndOfLine(filename).orElse("\0");
      String expectedResult = new StringBuilder()
        .append("this is a short readme")
        .append(lineSeparator)
        .append("used for unit tests")
        .append(lineSeparator)
        .append("Hello JHipster Lite")
        .append(lineSeparator)
        .toString();
      assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldNotReplaceInFileWhenFileNotExist() {
      String filename = getPath("src/test/resources/generator/utils/unknown.md");

      assertThatThrownBy(() -> FileUtils.replaceInFile(filename, "powered by JHipster", "Hello JHipster Lite"))
        .isInstanceOf(IOException.class);
    }
  }

  @Nested
  class ContainsRegexpTest {

    @Test
    void shouldContainOneLineTextOneLineRegexp() {
      String text = "Hello JHipster Lite";

      assertTrue(FileUtils.containsRegexp(text, "JH.*ite"));
    }

    @Test
    void shouldNotContainOneLineTextOneLineRegexp() {
      String text = "Hello JHipster Lite";

      assertFalse(FileUtils.containsRegexp(text, "jh.*ite"));
    }

    @Test
    void shouldContainMultiLineTextOneLineRegexp() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();

      assertTrue(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_DOTALL + "short.*for unit"));
    }

    @Test
    void shouldNotContainMultiLineTextOneLineRegexp() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();

      assertFalse(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_DOTALL + "JHipster.*for unit"));
    }

    @Test
    void shouldContainMultiLineTextMultiLineRegexp() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();
      String regexp = new StringBuilder().append("used for unit tests").append(LF).append("Hello J.* Lite").toString();

      assertTrue(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_DOTALL + regexp));
    }

    @Test
    void shouldNotContainMultiLineTextMultiLineRegexp() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();
      String regexp = new StringBuilder().append("used for unit tests").append(LF).append("Hello W.* Lite").toString();

      assertFalse(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_DOTALL + regexp));
    }

    @Test
    void shouldContainMultiLineTextMultiLineText() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();
      String searchText = new StringBuilder().append("used for unit tests").append(LF).append("Hello JHipster Lite").append(LF).toString();

      assertTrue(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_MULTILINE + searchText));
    }

    @Test
    void shouldNotContainMultiLineTextMultiLineText() {
      String text = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("used for unit tests")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();
      String searchText = new StringBuilder()
        .append("this is a short readme")
        .append(LF)
        .append("Hello JHipster Lite")
        .append(LF)
        .toString();

      assertFalse(FileUtils.containsRegexp(text, FileUtils.REGEXP_PREFIX_MULTILINE + searchText));
    }
  }

  @Nested
  class CountsRegexpTest {

    @Test
    void shouldCountManyItemOneLineRegexp() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/example-readme.md");

      assertEquals(9, FileUtils.countsRegexp(filename, "jhi.?ster"));
    }

    @Test
    void shouldCountNoItemOneLineRegexp() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/example-readme.md");

      assertEquals(0, FileUtils.countsRegexp(filename, "Hel.* world"));
    }

    @Test
    void shouldCountManyItemMultiLineRegexp() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/example-readme.md");

      String lineSeparator = detectEndOfLine(filename).orElse("\0");
      String regexp = new StringBuilder().append("```").append(lineSeparator).append("./mv.?w clean").toString();

      assertEquals(2, FileUtils.countsRegexp(filename, FileUtils.REGEXP_PREFIX_DOTALL + regexp));
    }

    @Test
    void shouldCountNoItemMultiLineRegexp() throws Exception {
      String filename = getPath("src/test/resources/generator/utils/example-readme.md");

      String lineSeparator = detectEndOfLine(filename).orElse("\0");
      String regexp = new StringBuilder().append("```").append(lineSeparator).append("np.? ci").toString();

      assertEquals(0, FileUtils.countsRegexp(filename, FileUtils.REGEXP_PREFIX_DOTALL + regexp));
    }
  }

  @Nested
  class FileSystemTest {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldReturnPosixFalseForWindows() {
      assertFalse(isPosix());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void shouldReturnPosixTrueForNonWindows() {
      assertTrue(isPosix());
    }
  }
}
