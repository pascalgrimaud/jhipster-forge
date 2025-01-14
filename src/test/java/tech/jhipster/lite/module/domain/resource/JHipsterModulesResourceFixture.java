package tech.jhipster.lite.module.domain.resource;

import static tech.jhipster.lite.module.domain.resource.JHipsterModulePropertyDefinition.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import tech.jhipster.lite.module.domain.JHipsterModuleFactory;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization.JHipsterModuleOrganizationBuilder;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleTags.JHipsterModuleTagsBuilder;

public final class JHipsterModulesResourceFixture {

  private JHipsterModulesResourceFixture() {}

  public static JHipsterModulesResources moduleResources() {
    return new JHipsterModulesResources(moduleResourcesCollection(), emptyHiddenModules());
  }

  public static Collection<JHipsterModuleResource> moduleResourcesCollection() {
    return List.of(
      defaultModuleResource(),
      defaultModuleResourceBuilder().slug("another-module").tags(new JHipsterModuleTagsBuilder().add("tag2").build()).build(),
      defaultModuleResourceBuilder()
        .slug("yet-another-module")
        .group("Another group")
        .operation("Another operation")
        .tags(new JHipsterModuleTagsBuilder().add("tag3").build())
        .build()
    );
  }

  public static Collection<JHipsterModuleResource> moduleNestedResourcesCollection() {
    return List.of(
      defaultModuleResource(),
      defaultModuleResourceBuilder().slug("module-a").build(),
      defaultModuleResourceBuilder().slug("module-b").moduleDependency("module-a").build(),
      defaultModuleResourceBuilder().slug("module-c").moduleDependency("module-b").build()
    );
  }

  public static JHipsterModuleResource defaultModuleResource() {
    return defaultModuleResourceBuilder().build();
  }

  public static JHipsterTestModuleResourceBuilder defaultModuleResourceBuilder() {
    return new JHipsterTestModuleResourceBuilder()
      .slug("slug")
      .operation("operation")
      .tags(JHipsterModuleTags.builder().add("tag1").build())
      .factory(properties -> null);
  }

  public static JHipsterHiddenModules emptyHiddenModules() {
    return new JHipsterHiddenModules(null, null);
  }

  public static JHipsterModulePropertiesDefinition propertiesDefinition() {
    return JHipsterModulePropertiesDefinition.builder()
      .addBasePackage()
      .addIndentation()
      .addProjectName()
      .addProjectBaseName()
      .add(optionalStringProperty("optionalString").build())
      .add(mandatoryIntegerProperty("mandatoryInteger").build())
      .add(mandatoryBooleanProperty("mandatoryBoolean").build())
      .add(optionalBooleanProperty("optionalBoolean").build())
      .build();
  }

  public static final class JHipsterTestModuleResourceBuilder {

    private String slug;
    private String group = "group";
    private String operation;
    private JHipsterModuleFactory factory;
    private JHipsterModuleTags tags;
    private String feature;
    private String rank;

    private final Collection<JHipsterModuleSlugFactory> moduleDependencies = new ArrayList<>();
    private final Collection<JHipsterFeatureSlugFactory> featureDependencies = new ArrayList<>();

    private JHipsterTestModuleResourceBuilder() {}

    public JHipsterTestModuleResourceBuilder slug(String slug) {
      this.slug = slug;

      return this;
    }

    public JHipsterTestModuleResourceBuilder group(String group) {
      this.group = group;

      return this;
    }

    public JHipsterTestModuleResourceBuilder operation(String operation) {
      this.operation = operation;

      return this;
    }

    public JHipsterTestModuleResourceBuilder factory(JHipsterModuleFactory factory) {
      this.factory = factory;

      return this;
    }

    public JHipsterTestModuleResourceBuilder feature(String feature) {
      this.feature = feature;

      return this;
    }

    public JHipsterTestModuleResourceBuilder moduleDependency(String module) {
      moduleDependencies.add(() -> module);

      return this;
    }

    public JHipsterTestModuleResourceBuilder featureDependency(String feature) {
      featureDependencies.add(() -> feature);

      return this;
    }

    public JHipsterTestModuleResourceBuilder tags(JHipsterModuleTags tags) {
      this.tags = tags;

      return this;
    }

    public JHipsterTestModuleResourceBuilder rank(String rank) {
      this.rank = rank;

      return this;
    }

    public JHipsterModuleResource build() {
      JHipsterModuleResource.JHipsterModuleResourceOptionalBuilder builder = JHipsterModuleResource.builder()
        .slug(() -> slug)
        .propertiesDefinition(propertiesDefinition())
        .apiDoc(group, operation)
        .organization(buildOrganization())
        .tags(tags);

      if (rank != null) {
        builder.rank(rank);
      }

      return builder.factory(factory);
    }

    private JHipsterModuleOrganization buildOrganization() {
      JHipsterModuleOrganizationBuilder builder = JHipsterModuleOrganization.builder().feature(() -> feature);

      moduleDependencies.forEach(builder::addDependency);
      featureDependencies.forEach(builder::addDependency);

      return builder.build();
    }
  }
}
