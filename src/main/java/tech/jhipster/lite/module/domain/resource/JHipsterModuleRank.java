package tech.jhipster.lite.module.domain.resource;

import tech.jhipster.lite.shared.error.domain.Assert;

public record JHipsterModuleRank(String rank) {
  public JHipsterModuleRank {
    Assert.notNull("rank", rank);
  }
}
