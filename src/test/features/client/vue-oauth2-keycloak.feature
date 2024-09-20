Feature: Vue OAuth2 Keycloak module

  Scenario: Should apply Vue OAuth2 Keycloak module
    When I apply modules to default project
      | init                |
      | prettier            |
      | vue-core            |
      | vue-oauth2-keycloak |
    Then I should have files in "src/main/webapp/app/auth/domain"
      | AuthRepository.ts |