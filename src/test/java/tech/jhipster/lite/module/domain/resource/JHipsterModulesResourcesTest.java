package tech.jhipster.lite.module.domain.resource;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.module.domain.resource.JHipsterModulesResourceFixture.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModuleSlug;
import tech.jhipster.lite.shared.error.domain.MissingMandatoryValueException;

@UnitTest
class JHipsterModulesResourcesTest {

  @Test
  void shouldNotGetModuleForUnknownSlug() {
    JHipsterModulesResources jHipsterModulesResources = new JHipsterModulesResources(
      List.of(defaultModuleResourceBuilder().slug("dummy").build()),
      emptyHiddenModules()
    );

    assertThatThrownBy(() -> jHipsterModulesResources.get(new JHipsterModuleSlug("dummy-2")))
      .isExactlyInstanceOf(UnknownSlugException.class)
      .hasMessageContaining("Module dummy-2 does not exist");
  }

  @Test
  void shouldNotBuildWithoutResources() {
    assertThatThrownBy(() -> new JHipsterModulesResources(List.of(), emptyHiddenModules()))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("modulesResources");
  }

  @Test
  void shouldNotBuildWithDuplicatedSlug() {
    JHipsterModuleResource resource = defaultModuleResourceBuilder().slug("dummy").build();

    assertThatThrownBy(() -> new JHipsterModulesResources(List.of(resource, resource), emptyHiddenModules())).isExactlyInstanceOf(
      DuplicatedSlugException.class
    );
  }

  @Test
  void shouldHaveMeaningfulToString() {
    var resource = defaultModuleResourceBuilder().slug("dummy").build();

    assertThat(resource.toString()).contains("JHipsterModuleResource[", "slug=dummy");
  }

  @Test
  void shouldBuildWithRankedResources() {
    var resource = defaultModuleResourceBuilder().rank("S").build();

    assertThat(resource.rank()).isEqualTo("S");
  }

  @Test
  void shouldBuildWithDefaultRankedResources() {
    var resource = defaultModuleResourceBuilder().build();

    assertThat(resource.rank()).isEqualTo("D");
  }
}
