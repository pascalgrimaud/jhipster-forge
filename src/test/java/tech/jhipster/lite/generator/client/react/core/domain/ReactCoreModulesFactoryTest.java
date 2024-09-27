package tech.jhipster.lite.generator.client.react.core.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;

@UnitTest
class ReactCoreModulesFactoryTest {

  private static final ReactCoreModulesFactory factory = new ReactCoreModulesFactory();

  @Test
  void shouldBuildModuleWithStyle() {
    JHipsterModule module = factory.buildModule(
      JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).projectBaseName("jhipster").build()
    );

    assertThatModuleWithFiles(module, packageJsonFile(), lintStagedConfigFile(), eslintConfigFile(), tsConfigFile(), vitestConfigFile())
      .hasFile("package.json")
      .notContaining(nodeDependency("@tsconfig/recommended"))
      .containing(nodeDependency("@testing-library/dom"))
      .containing(nodeDependency("@testing-library/react"))
      .containing(nodeDependency("@types/node"))
      .containing(nodeDependency("@types/react"))
      .containing(nodeDependency("@types/react-dom"))
      .containing(nodeDependency("@tsconfig/vite-react"))
      .containing(nodeDependency("@vitejs/plugin-react"))
      .containing(nodeDependency("eslint-plugin-react"))
      .containing(nodeDependency("jsdom"))
      .containing(nodeDependency("ts-node"))
      .containing(nodeDependency("vite"))
      .containing(nodeDependency("react"))
      .containing(nodeDependency("react-dom"))
      .containing(nodeScript("dev", "npm-run-all --parallel dev:*"))
      .containing(nodeScript("dev:vite", "vite"))
      .containing(nodeScript("build", "npm-run-all build:*"))
      .containing(nodeScript("build:tsc", "tsc"))
      .containing(nodeScript("build:vite", "vite build --emptyOutDir"))
      .containing(nodeScript("preview", "vite preview"))
      .containing(nodeScript("start", "vite"))
      .and()
      .hasFile(".lintstagedrc.cjs")
      .containing(
        """
        module.exports = {
          '{src/**/,}*.{ts,tsx}': ['eslint --fix', 'prettier --write'],
          '*.{md,json,yml,html,css,scss,java,xml,feature}': ['prettier --write'],
        };
        """
      )
      .and()
      .hasFile("eslint.config.js")
      .matchingSavedSnapshot()
      .and()
      .hasFile("tsconfig.json")
      .matchingSavedSnapshot()
      .and()
      .hasFile("vitest.config.ts")
      .matchingSavedSnapshot()
      .and()
      .hasFiles("vite.config.ts")
      .hasFiles("src/main/webapp/index.html")
      .hasPrefixedFiles("src/main/webapp/app", "index.css", "index.tsx", "vite-env.d.ts")
      .hasFiles("src/test/webapp/unit/common/primary/app/App.spec.tsx")
      .hasFile("src/main/webapp/app/common/primary/app/App.tsx")
      .containing("import './App.css';")
      .and()
      .hasFiles("src/main/webapp/app/common/primary/app/App.css")
      .hasPrefixedFiles("src/main/webapp/assets", "ReactLogo.png", "JHipster-Lite-neon-blue.png");
  }

  @Test
  void shouldViteConfigBeUpdatedWhenServerPortPropertyNotDefault() {
    JHipsterModule module = factory.buildModule(
      JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).projectBaseName("jhipster").put("serverPort", 8081).build()
    );

    assertThatModuleWithFiles(module, packageJsonFile(), eslintConfigFile(), tsConfigFile(), vitestConfigFile())
      .hasFile("vite.config.ts")
      .containing("localhost:8081")
      .notContaining("localhost:8080");
  }

  @Test
  void shouldViteConfigBeDefaultWhenServerPortPropertyMissing() {
    JHipsterModule module = factory.buildModule(
      JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest()).projectBaseName("jhipster").build()
    );

    assertThatModuleWithFiles(module, packageJsonFile(), eslintConfigFile(), tsConfigFile(), vitestConfigFile())
      .hasFile("vite.config.ts")
      .containing("localhost:8080");
  }

  private String nodeScript(String key, String value) {
    return "\"" + key + "\": \"" + value + "\"";
  }
}
