package tech.jhipster.lite.merge.impl;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import tech.jhipster.lite.merge.Merge;

/**
 * Input to the merge process. Only three of the corners are input.
 * The fourth corner 'target' is output, and is NOT part of this class.
 * This diamond consists of the three incoming 'files'
 * The last file 'target' is not here, but will be generated by {@link Merge#merge()}
 *
 * @param base   how file was generated before this merge (if the generated existed before)
 * @param gen    how the new generated file looks like now.
 * @param custom how the file looks in the source tree (if exists)
 */
public record Diamond(
  /*
   * Starting leg in diamond merge. Both generated and custom are directly derived from this 'file'
   * 'base' is used on 'left' side in the compare process.
   */
  Body base,

  /*
   * Generated 'file' from generator.
   * The file is compared to 'base' in the first 'diamond' merge step {@link BodyBuilder#apply(List)}.
   * As the 'right' side of compare.
   */
  Body gen,
  /*
   * Custom file. Located in sources. In example 'src/main/resources/config/application.properties'
   * The file is compared to 'base' in the second 'diamond' merge step {@link BodyBuilder#apply(List)}.
   * As the 'right' side of compare.
   */
  Body custom
) {
  /**
   * @param base   how file was generated before this merge (if the generated existed before)
   * @param gen    how the new generated file looks like now.
   * @param custom how the file looks in the source tree (if exists)
   */
  public static Diamond of(@Nullable String base, @NotNull String gen, @Nullable String custom) {
    return new Diamond(Body.of(base), Body.of(gen), Body.of(custom));
  }
}
