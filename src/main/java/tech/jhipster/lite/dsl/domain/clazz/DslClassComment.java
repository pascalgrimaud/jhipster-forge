package tech.jhipster.lite.dsl.domain.clazz;

import tech.jhipster.lite.dsl.domain.ClassDslUtils;
import tech.jhipster.lite.error.domain.Assert;

public record DslClassComment(String comment) {
  public DslClassComment(String comment) {
    this.comment = cleanComment(comment);
  }

  private String cleanComment(String comment) {
    Assert.field("comment", comment).notBlank();
    return ClassDslUtils.cleanComment(comment);
  }
  public String get() {
    return comment();
  }
}
