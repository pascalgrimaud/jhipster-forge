Feature: Init

  Scenario: Should init from module
    When I apply "init" module to default project
      | packageName | tech.jhipster.chips |
      | baseName    | jhipster            |
      | endOfLine   | lf                  |
    Then I should have files in ""
      | .gitignore        |
      | .gitattributes    |
      | .editorconfig     |
      | package.json      |
      | README.md         |
      | .lintstagedrc.cjs |
      | .npmrc            |      
    And I should not have files in ""
      | .prettierignore |
      | .prettierrc     |
    And I should have files in ".husky"
      | pre-commit |
