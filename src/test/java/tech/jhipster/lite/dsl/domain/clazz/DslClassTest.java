package tech.jhipster.lite.dsl.domain.clazz;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.dsl.DslParser;
import tech.jhipster.lite.dsl.domain.DslContext;
import tech.jhipster.lite.dsl.infrastructure.secondary.AntlrUtils;
import tech.jhipster.lite.error.domain.MissingMandatoryValueException;
import tech.jhipster.lite.error.domain.StringWithWitespacesException;

@UnitTest
class DslClassTest {

  @Test
  void shouldThrowExceptionByDefault() {
    DslClass.DslClassBuilder builder = DslClass.dslClassBuilder();

    assertThrows(MissingMandatoryValueException.class, builder::build, "MissingMandatoryValueException was expected");
  }

  @Test
  void shouldHaveAValidDslClassIfName() {
    DslClass.DslClassBuilder builder = DslClass.dslClassBuilder();
    DslClass dslClass = builder.name(new DslClassName("test")).build();
    assertEquals("Test", dslClass.getName().get());
    assertEquals("class", dslClass.getType().key());
    assertNotNull(dslClass.getAnnotations());
    assertNotNull(dslClass.getFields());
    assertTrue(dslClass.getComment().isEmpty());
    assertTrue(dslClass.getPackage().get().isEmpty());
  }

  @Test
  void shouldNotAssertNull() {
    DslClass.DslClassBuilder builder = DslClass.dslClassBuilder();
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.name(null);
      },
      "MissingMandatoryValueException was expected"
    );
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.type(null);
      },
      "MissingMandatoryValueException was expected"
    );
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.addField(null);
      },
      "MissingMandatoryValueException was expected"
    );
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.addAnnotation(null);
      },
      "MissingMandatoryValueException was expected"
    );
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.comment(null);
      },
      "MissingMandatoryValueException was expected"
    );
    assertThrows(
      MissingMandatoryValueException.class,
      () -> {
        builder.definePackage(null);
      },
      "MissingMandatoryValueException was expected"
    );
  }
}
