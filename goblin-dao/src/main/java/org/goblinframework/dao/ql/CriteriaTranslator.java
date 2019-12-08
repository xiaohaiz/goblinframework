package org.goblinframework.dao.ql;

/**
 * Translator of {@link Criteria}.
 */
public interface CriteriaTranslator<T> {

  T translate(Criteria criteria);

}
