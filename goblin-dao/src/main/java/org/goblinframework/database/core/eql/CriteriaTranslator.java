package org.goblinframework.database.core.eql;

/**
 * Translator of {@link Criteria}.
 */
public interface CriteriaTranslator<T> {

  T translate(Criteria criteria);

}
