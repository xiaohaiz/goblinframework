package org.goblinframework.dao.core.cql;

/**
 * Translator of {@link Criteria}.
 */
public interface CriteriaTranslator<T> {

  T translate(Criteria criteria);

}
