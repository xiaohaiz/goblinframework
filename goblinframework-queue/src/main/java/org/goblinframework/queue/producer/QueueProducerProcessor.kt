package org.goblinframework.queue.producer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.util.GoblinField
import org.goblinframework.core.util.ReflectionUtils

@Singleton
class QueueProducerProcessor private constructor() : SpringContainerBeanPostProcessor {

    companion object {
        @JvmField val INSTANCE = QueueProducerProcessor()
    }

    override fun postProcessAfterInitialization(bean: Any?, beanName: String?): Any? {
        return bean?.run { tryQueueProducerInjection(bean) }
    }

    private fun tryQueueProducerInjection(bean: Any): Any {
        return ReflectionUtils.allFieldsIncludingAncestors(bean.javaClass, false, false)
                .map { GoblinField(it) }

    }
}