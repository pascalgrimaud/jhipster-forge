package tech.jhipster.lite.generator.client.vue.core.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.JHipsterModuleBuilder;
import static tech.jhipster.lite.module.domain.JHipsterModule.documentationTitle;
import static tech.jhipster.lite.module.domain.JHipsterModule.from;
import static tech.jhipster.lite.module.domain.JHipsterModule.lineAfterRegex;
import static tech.jhipster.lite.module.domain.JHipsterModule.lineBeforeText;
import static tech.jhipster.lite.module.domain.JHipsterModule.moduleBuilder;
import static tech.jhipster.lite.module.domain.JHipsterModule.packageName;
import static tech.jhipster.lite.module.domain.JHipsterModule.path;
import static tech.jhipster.lite.module.domain.JHipsterModule.preCommitCommands;
import static tech.jhipster.lite.module.domain.JHipsterModule.scriptCommand;
import static tech.jhipster.lite.module.domain.JHipsterModule.scriptKey;
import static tech.jhipster.lite.module.domain.JHipsterModule.stagedFilesFilter;
import static tech.jhipster.lite.module.domain.JHipsterModule.text;
import static tech.jhipster.lite.module.domain.JHipsterModule.to;
import static tech.jhipster.lite.module.domain.npm.JHLiteNpmVersionSource.COMMON;
import static tech.jhipster.lite.module.domain.npm.JHLiteNpmVersionSource.VUE;

import java.util.function.Consumer;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.file.JHipsterDestination;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.npm.NpmLazyInstaller;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.module.domain.replacement.MandatoryReplacer;
import tech.jhipster.lite.shared.error.domain.Assert;

public class VueModulesFactory {

  private static final JHipsterSource SOURCE = from("client/vue");
  private static final JHipsterSource APP_SOURCE = from("client/vue/webapp/app");
  private static final JHipsterSource PIQURE_SOURCE = from("client/common/piqure");
  private static final JHipsterSource DOCUMENTATION_SOURCE = SOURCE.append("documentation");
  private static final JHipsterSource IMAGE_SOURCE = SOURCE.append("webapp/content/images");

  private static final JHipsterDestination MAIN_DESTINATION = to("src/main/webapp/app");
  private static final JHipsterDestination TEST_DESTINATION = to("src/test/webapp");

  private static final String IMPORT_NEEDLE = "// jhipster-needle-main-ts-import";
  private static final String PROVIDER_NEEDLE = "// jhipster-needle-main-ts-provider";
  private static final String PINIA_IMPORTS =
    """
    import { createPinia } from 'pinia';
    import piniaPersist from 'pinia-plugin-persistedstate';
    """;
  private static final String PINIA_PROVIDER =
    """
    const pinia = createPinia();
    pinia.use(piniaPersist);
    app.use(pinia);
    """;

  private final NpmLazyInstaller npmLazyInstaller;

  public VueModulesFactory(NpmLazyInstaller npmLazyInstaller) {
    this.npmLazyInstaller = npmLazyInstaller;
  }

  public JHipsterModule buildVueModule(JHipsterModuleProperties properties) {
    //@formatter:off
    return moduleBuilder(properties)
      .preCommitActions(stagedFilesFilter("{src/**/,}*.{ts,vue}"), preCommitCommands("eslint --fix", "prettier --write"))
      .documentation(documentationTitle("Vue"), DOCUMENTATION_SOURCE.file("vue.md"))
      .packageJson()
        .removeDevDependency(packageName("@tsconfig/recommended"))
        .addDependency(packageName("vue"), VUE)
        .addDependency(packageName("axios"), VUE)
        .addDependency(packageName("vue-router"), VUE)
        .addDependency(packageName("piqure"), COMMON)
        .addDevDependency(packageName("@vitejs/plugin-vue"), VUE)
        .addDevDependency(packageName("@vue/test-utils"), VUE)
        .addDevDependency(packageName("@vue/tsconfig"), VUE)
        .addDevDependency(packageName("eslint-plugin-vue"), VUE)
        .addDevDependency(packageName("jsdom"), COMMON)
        .addDevDependency(packageName("vite"), COMMON)
        .addDevDependency(packageName("vue-tsc"), VUE)
        .addDevDependency(packageName("@types/sinon"), VUE)
        .addDevDependency(packageName("sinon"), VUE)
        .addScript(scriptKey("build"), scriptCommand("npm-run-all build:*"))
        .addScript(scriptKey("build:tsc"), scriptCommand("vue-tsc -p tsconfig.build.json --noEmit"))
        .addScript(scriptKey("build:vite"), scriptCommand("vite build --emptyOutDir"))
        .addScript(scriptKey("dev"), scriptCommand("npm-run-all --parallel dev:*"))
        .addScript(scriptKey("dev:vite"), scriptCommand("vite"))
        .addScript(scriptKey("preview"), scriptCommand("vite preview"))
        .addScript(scriptKey("start"), scriptCommand("vite"))
        .addScript(scriptKey("watch:tsc"), scriptCommand("npm run build:tsc -- --watch"))
        .and()
      .postActions()
        .add(context -> npmLazyInstaller.runInstallIn(context.projectFolder()))
        .and()
      .files()
        .add(SOURCE.file("tsconfig.build.json"), to("tsconfig.build.json"))
        .batch(SOURCE, to("."))
          .addTemplate("vite.config.ts")
          .and()
        .add(SOURCE.template("webapp/index.html"), to("src/main/webapp/index.html"))
        .batch(APP_SOURCE, MAIN_DESTINATION)
          .addTemplate("env.d.ts")
          .addTemplate("main.ts")
          .addTemplate("AppVue.vue")
          .addTemplate("router.ts")
          .and()
        .batch(PIQURE_SOURCE, MAIN_DESTINATION)
          .addTemplate("injections.ts")
        .and()
        .batch(APP_SOURCE.append("home"), MAIN_DESTINATION.append("home"))
          .addTemplate("application/HomeRouter.ts")
          .addTemplate("infrastructure/primary/HomepageVue.vue")
          .and()
        .add(APP_SOURCE.template("shared/http/infrastructure/secondary/AxiosHttp.ts.mustache"), MAIN_DESTINATION.append("shared/http/infrastructure/secondary/AxiosHttp.ts"))
        .batch(IMAGE_SOURCE, to("src/main/webapp/content/images"))
          .addFile("JHipster-Lite-neon-green.png")
          .addFile("VueLogo.png")
          .and()
        .add(SOURCE.template("webapp/app/router.ts"), MAIN_DESTINATION.append("router.ts"))
        .add(SOURCE.template("Dummy.spec.ts"), to("src/test/webapp/unit/Dummy.spec.ts"))
        .add(APP_SOURCE.template("test/webapp/unit/shared/http/infrastructure/secondary/AxiosHttp.spec.ts.mustache"), TEST_DESTINATION.append("unit/shared/http/infrastructure/secondary/AxiosHttp.spec.ts"))
        .add(APP_SOURCE.template("test/webapp/unit/shared/http/infrastructure/secondary/AxiosStub.ts.mustache"), TEST_DESTINATION.append("unit/shared/http/infrastructure/secondary/AxiosStub.ts"))
        .add(APP_SOURCE.template("test/webapp/unit/router/infrastructure/primary/HomeRouter.spec.ts.mustache"), TEST_DESTINATION.append("unit/router/infrastructure/primary/HomeRouter.spec.ts"))
        .and()
      .apply(patchEslintConfig(properties))
      .apply(patchTsConfig(properties))
      .apply(patchVitestConfig(properties))
      .build();
    //@formatter:on
  }

  private Consumer<JHipsterModuleBuilder> patchEslintConfig(JHipsterModuleProperties properties) {
    String vuePluginConfig =
      """
      \t...vue.configs['flat/recommended'],
      \t{
      \t\tfiles: ['**/*.vue'],
      \t\tlanguageOptions: {
      \t\t\tparserOptions: { parser: '@typescript-eslint/parser' },
      \t\t\tglobals: { ...globals.browser },
      \t\t},
      \t},\
      """.replace("\t", properties.indentation().spaces());
    //@formatter:off
    return moduleBuilder -> moduleBuilder
      .mandatoryReplacements()
        .in(path("eslint.config.js"))
          .add(lineAfterRegex("from '@eslint/js'"), "import vue from 'eslint-plugin-vue';")
          .add(lineAfterRegex("...typescript.configs.recommended"), vuePluginConfig)
          .add(text("files: ['src/*/webapp/**/*.ts']"), "files: ['src/*/webapp/**/*.vue', 'src/*/webapp/**/*.ts']")
          .add(eslintTypescriptVueRule("'vue/html-self-closing': 'off',", properties.indentation()))
          .add(eslintTypescriptVueRule("'@typescript-eslint/no-explicit-any': 'off',", properties.indentation()))
          .add(eslintTypescriptVueRule("'@typescript-eslint/no-empty-object-type': 'off',", properties.indentation()))
          .and()
        .and();
    //@formatter:on
  }

  private static MandatoryReplacer eslintTypescriptVueRule(String rule, Indentation indentation) {
    return new MandatoryReplacer(lineAfterRegex("quotes: \\['error', 'single'"), indentation.times(3) + rule);
  }

  private Consumer<JHipsterModuleBuilder> patchTsConfig(JHipsterModuleProperties properties) {
    //@formatter:off
    return moduleBuilder -> moduleBuilder
      .mandatoryReplacements()
        .in(path("tsconfig.json"))
          .add(text("@tsconfig/recommended/tsconfig.json"), "@vue/tsconfig/tsconfig.dom.json")
          .add(tsConfigCompilerOption("sourceMap", true, properties.indentation()))
          .add(tsConfigCompilerOption("allowJs", true, properties.indentation()))
          .add(text("\"types\": ["), "\"types\": [\"vite/client\", ")
          .and()
        .and();
    //@formatter:on
  }

  private static MandatoryReplacer tsConfigCompilerOption(String optionName, boolean optionValue, Indentation indentation) {
    String compilerOption = indentation.times(2) + "\"%s\": %s,".formatted(optionName, optionValue);
    return new MandatoryReplacer(lineAfterRegex("\"compilerOptions\":"), compilerOption);
  }

  private Consumer<JHipsterModuleBuilder> patchVitestConfig(JHipsterModuleProperties properties) {
    //@formatter:off
    return moduleBuilder -> moduleBuilder
      .mandatoryReplacements()
        .in(path("vitest.config.ts"))
          .add(lineAfterRegex("from 'vitest/config';"), "import vue from '@vitejs/plugin-vue';")
          .add(text("plugins: ["), "plugins: [vue(), ")
          .add(text("environment: 'node',"), "environment: 'jsdom',")
          .add(vitestCoverageExclusion("src/main/webapp/**/*.component.ts"))
          .add(vitestCoverageExclusion("src/main/webapp/app/router.ts"))
          .add(vitestCoverageExclusion("src/main/webapp/app/injections.ts"))
          .add(vitestCoverageExclusion("src/main/webapp/app/main.ts"))
        .and();
    //@formatter:on
  }

  private static MandatoryReplacer vitestCoverageExclusion(String filePattern) {
    return new MandatoryReplacer(
      text("(configDefaults.coverage.exclude as string[])"),
      "(configDefaults.coverage.exclude as string[])" + ", '" + filePattern + "'"
    );
  }

  public JHipsterModule buildPiniaModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .packageJson()
        .addDependency(packageName("pinia"), VUE)
        .addDependency(packageName("pinia-plugin-persistedstate"), VUE)
        .addDevDependency(packageName("@pinia/testing"), VUE)
        .and()
      .mandatoryReplacements()
        .in(path("src/main/webapp/app/main.ts"))
          .add(lineBeforeText(IMPORT_NEEDLE), PINIA_IMPORTS)
          .add(lineBeforeText(PROVIDER_NEEDLE), PINIA_PROVIDER)
          .and()
        .and()
      .build();
    //@formatter:on
  }
}
