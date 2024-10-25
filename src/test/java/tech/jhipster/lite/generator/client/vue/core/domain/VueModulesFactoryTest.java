package tech.jhipster.lite.generator.client.vue.core.domain;

import static org.mockito.Mockito.verify;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.ModuleFile;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.eslintConfigFile;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.lintStagedConfigFile;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.nodeDependency;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.nodeScript;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.packageJsonFile;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.tsConfigFile;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.vitestConfigFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.npm.NpmLazyInstaller;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
@ExtendWith(MockitoExtension.class)
class VueModulesFactoryTest {

  @InjectMocks
  private VueModulesFactory factory;

  @Mock
  private NpmLazyInstaller npmLazyInstaller;

  @Test
  void shouldCreateVueModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .projectBaseName("jhiTest")
      .basePackage("tech.jhipster.jhlitest")
      .build();

    JHipsterModule module = factory.buildVueModule(properties);

    //@formatter:off
    assertThatModuleWithFiles(module, packageJsonFile(), lintStagedConfigFile(), tsConfigFile(), vitestConfigFile(), eslintConfigFile())
      .hasFiles("documentation/vue.md")
      .hasFile("package.json")
        .notContaining(nodeDependency("@tsconfig/recommended"))
        .containing(nodeDependency("vue"))
        .containing(nodeDependency("@vitejs/plugin-vue"))
        .containing(nodeDependency("@vue/test-utils"))
        .containing(nodeDependency("@vue/tsconfig"))
        .containing(nodeDependency("eslint-plugin-vue"))
        .containing(nodeDependency("jsdom"))
        .containing(nodeDependency("vite"))
        .containing(nodeDependency("vue-tsc"))
        .containing(nodeDependency("@types/sinon"))
        .containing(nodeDependency("sinon"))
        .containing(nodeDependency("axios"))
        .containing(nodeDependency("vue-router"))
        .containing(nodeDependency("piqure"))
        .containing(nodeScript("build", "npm-run-all build:*"))
        .containing(nodeScript("build:tsc", "vue-tsc -p tsconfig.build.json --noEmit"))
        .containing(nodeScript("build:vite", "vite build --emptyOutDir"))
        .containing(nodeScript("dev", "npm-run-all --parallel dev:*"))
        .containing(nodeScript("dev:vite", "vite"))
        .containing(nodeScript("watch:tsc", "npm run build:tsc -- --watch"))
        .containing(nodeScript("preview", "vite preview"))
        .containing(nodeScript("start", "vite"))
        .and()
      .hasFile(".lintstagedrc.cjs")
        .containing(
          """
            module.exports = {
              '{src/**/,}*.{ts,vue}': ['eslint --fix', 'prettier --write'],
              '*.{md,json,yml,html,css,scss,java,xml,feature}': ['prettier --write'],
            };
            """
        )
      .and()
      .hasPrefixedFiles("", "eslint.config.js", "tsconfig.build.json", "vite.config.ts")
      .hasFile("tsconfig.json")
        .matchingSavedSnapshot()
        .and()
      .hasFile("vitest.config.ts")
        .matchingSavedSnapshot()
        .and()
      .hasFile("eslint.config.js")
        .matchingSavedSnapshot()
      .and()
      .hasFiles("src/main/webapp/app/shared/http/infrastructure/secondary/AxiosHttp.ts")
      .hasFiles("src/main/webapp/index.html")
      .hasPrefixedFiles("src/main/webapp/app", "env.d.ts", "AppVue.vue", "injections.ts", "router.ts", "main.ts")
      .hasPrefixedFiles("src/main/webapp/app/home","infrastructure/primary/HomepageVue.vue", "application/HomeRouter.ts")
      .hasPrefixedFiles("src/main/webapp/content/images", "JHipster-Lite-neon-green.png", "VueLogo.png")
      .hasFiles("src/test/webapp/unit/Dummy.spec.ts")
      .hasFiles("src/test/webapp/unit/shared/http/infrastructure/secondary/AxiosHttp.spec.ts")
      .hasFiles("src/test/webapp/unit/shared/http/infrastructure/secondary/AxiosStub.ts")
      .hasFiles("src/test/webapp/unit/router/infrastructure/primary/HomeRouter.spec.ts");
    //@formatter:on
    verify(npmLazyInstaller).runInstallIn(properties.projectFolder());
  }

  @Test
  void shouldCreatePiniaModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("tech.jhipster.jhlitest")
      .build();

    JHipsterModule module = factory.buildPiniaModule(properties);

    assertThatModuleWithFiles(
      module,
      packageJsonFile(),
      new ModuleFile("src/test/resources/projects/vue/main.ts.template", "src/main/webapp/app/main.ts")
    )
      .hasFile("package.json")
      .containing(nodeDependency("pinia"))
      .containing(nodeDependency("pinia-plugin-persistedstate"))
      .containing(nodeDependency("@pinia/testing"))
      .and()
      .hasFile("src/main/webapp/app/main.ts")
      .containing("import { createPinia } from 'pinia';")
      .containing("import piniaPersist from 'pinia-plugin-persistedstate';")
      .containing(
        """
        const pinia = createPinia();
        pinia.use(piniaPersist);
        app.use(pinia);
        """
      );
  }
}
