package tech.jhipster.lite.generator.client.react.core.application;

import static tech.jhipster.lite.TestUtils.*;
import static tech.jhipster.lite.common.domain.FileUtils.getPath;
import static tech.jhipster.lite.common.domain.WordUtils.DQ;
import static tech.jhipster.lite.generator.project.domain.Constants.PACKAGE_JSON;

import tech.jhipster.lite.generator.project.domain.Project;

public class ReactAssert {

  private ReactAssert() {}

  public static void assertDependencies(Project project) {
    assertFileContent(project, PACKAGE_JSON, "@testing-library/jest-dom" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@testing-library/react" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@testing-library/user-event" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@types/jest" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@types/node" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@types/react" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "@types/react-dom" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "react" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "react-dom" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "react-scripts" + DQ + ": ");
    assertFileContent(project, PACKAGE_JSON, "typescript" + DQ + ": ");
  }

  public static void assertScripts(Project project) {
    assertFileContent(project, PACKAGE_JSON, "start");
    assertFileContent(project, PACKAGE_JSON, "build");
    assertFileContent(project, PACKAGE_JSON, "test");
  }

  public static void assertConfigFiles(Project project) {
    assertFileExist(project, "tsconfig.json");
  }

  public static void assertReactFiles(Project project) {
    String pathPublic = "public";
    String pathSrc = "src";
    String pathApp = "src/main/webapp/app";
    String pathConfig = "src/main/webapp/config";
    String pathWebapp = "src/main/webapp";
    String primaryApp = "src/main/webapp/app/common/primary/app";
    String primaryTestApp = "src/test/javascript/spec/common/primary/app";

    assertFileExist(project, getPath(pathPublic, "index.html"));
    assertFileExist(project, getPath(pathWebapp, "index.css"));
    assertFileExist(project, getPath(pathSrc, "index.tsx"));
    assertFileExist(project, getPath(pathWebapp, "react-app-env.d.ts"));
    assertFileExist(project, getPath(primaryApp, "App.css"));
    assertFileExist(project, getPath(primaryApp, "App.tsx"));
    assertFileExist(project, getPath(primaryTestApp, "App.test.tsx"));
    assertFileExist(project, getPath(pathConfig, "setupTests.ts"));
  }
}
