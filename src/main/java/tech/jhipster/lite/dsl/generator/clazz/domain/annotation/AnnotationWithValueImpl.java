package tech.jhipster.lite.dsl.generator.clazz.domain.annotation;

import java.util.Optional;
import tech.jhipster.lite.dsl.common.domain.clazz.ClassImport;
import tech.jhipster.lite.error.domain.Assert;

public record AnnotationWithValueImpl(String name, Optional<String> value, Optional<ClassImport> import_) implements Annotation {
  public AnnotationWithValueImpl {
    Assert.field("name", name).noWhitespace();
    Assert.notNull("value", name);
    Assert.notNull("import_", import_);
    value.ifPresent(s -> Assert.field("value", s).notBlank().noWhitespace());
    import_.ifPresent(s -> Assert.field("import_", s.get()).notBlank().noWhitespace());
  }
}
