package org.goblinframework.queue.module.config

import java.io.Serializable

class KafkaConfigMapper: Serializable {

    companion object {
        private const val serialVersionUID = 3618582962449266706L
    }

    /**
     * kafka instance name
     */
    var name: String? = null

    /**
     * server addresses
     */
    var servers: String? = null

    /**
     * producer config
     */
    var producer: ProducerConfigMapper? = null

    /**
     * consumer config
     */
    var consumer: ConsumerConfigMapper? = null

    class ProducerConfigMapper {

        /**
         * send message asynchronously or not
         */
        var asyncSend: Boolean? = null

        /**
         * The producer will attempt to batch records together into fewer requests whenever multiple records
         * are being sent to the same partition. This helps performance on both the client and the server.
         * This configuration controls the default batch size in bytes.
         * No attempt will be made to batch records larger than this size.
         * Requests sent to brokers will contain multiple batches, one for each partition with data available
         * to be sent.
         * A small batch size will make batching less common and may reduce throughput (a batch size of zero
         * will disable batching entirely). A very large batch size may use memory a bit more wastefully as
         * we will always allocate a buffer of the specified batch size in anticipation of additional records.
         */
        var batchSize: Int? = null

        /**
         * The number of acknowledgments the producer requires the leader to have received before considering
         * a request complete. This controls the durability of records that are sent. The following settings
         * are allowed:
         *
         *
         * acks=0 If set to zero then the producer will not wait for any acknowledgment from the server at all.
         * The record will be immediately added to the socket buffer and considered sent. No guarantee can be
         * made that the server has received the record in this case, and the retries configuration will not
         * take effect (as the client won't generally know of any failures). The offset given back for each
         * record will always be set to -1.
         *
         *
         * acks=1 This will mean the leader will write the record to its local log but will respond without
         * awaiting full acknowledgement from all followers. In this case should the leader fail immediately
         * after acknowledging the record but before the followers have replicated it then the record will be
         * lost.
         *
         *
         * acks=all This means the leader will wait for the full set of in-sync replicas to acknowledge the
         * record. This guarantees that the record will not be lost as long as at least one in-sync replica
         * remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting.
         */
        var acks: String? = null

        /**
         * The configuration controls the maximum amount of time the client will wait for the response of
         * a request. If the response is not received before the timeout elapses the client will resend
         * the request if necessary or fail the request if retries are exhausted.
         */
        var requestTimeout: Int? = null

        /**
         * The producer groups together any records that arrive in between request transmissions into a single
         * batched request. Normally this occurs only under load when records arrive faster than they can be
         * sent out. However in some circumstances the client may want to reduce the number of requests even
         * under moderate load. This setting accomplishes this by adding a small amount of artificial
         * delay—that is, rather than immediately sending out a record the producer will wait for up to the
         * given delay to allow other records to be sent so that the sends can be batched together.
         * This can be thought of as analogous to Nagle's algorithm in TCP. This setting gives the upper bound
         * on the delay for batching: once we get batch.size worth of records for a partition it will be sent
         * immediately regardless of this setting, however if we have fewer than this many bytes accumulated
         * for this partition we will 'linger' for the specified time waiting for more records to show up.
         * This setting defaults to 0 (i.e. no delay). Setting linger.ms=5, for example, would have the effect
         * of reducing the number of requests sent but would add up to 5ms of latency to records sent in the
         * absense of load.
         */
        var lingerMs: Long? = null

        /**
         * The maximum size of a request in bytes. This setting will limit the number of record batches the producer
         * will send in a single request to avoid sending huge requests. This is also effectively a cap on the maximum
         * record batch size. Note that the server has its own cap on record batch size which may be different from this.
         */
        var maxRequestSize: Int? = null
    }

    class ConsumerConfigMapper {
        /**
         * If true the consumer's offset will be periodically committed in the background.
         */
        var autoCommit: Boolean? = null

        /**
         * The frequency in milliseconds that the consumer offsets are auto-committed to Kafka if
         * autoCommit is set to true.
         */
        var autoCommitInterval: Int? = null

        /**
         * The maximum number of records returned in a single call to poll().
         */
        var maxPollRecords: Int? = null

        /**
         * The maximum delay between invocations of poll() when using consumer group management.
         * This places an upper bound on the amount of time that the consumer can be idle before fetching
         * more records. If poll() is not called before expiration of this timeout, then the consumer is
         * considered failed and the group will rebalance in order to reassign the partitions to another member.
         */
        var maxPollInterval: Int? = null

        /**
         * What to do when there is no initial offset in Kafka or if the current offset does not exist any more
         * on the server (e.g. because that data has been deleted):
         *
         *
         * earliest: automatically reset the offset to the earliest offset
         * latest: automatically reset the offset to the latest offset
         * none: throw exception to the consumer if no previous offset is found for the consumer's group
         * anything else: throw exception to the consumer.
         */
        var autoOffset: String? = null

        /**
         * The configuration controls the maximum amount of time the client will wait for the response of
         * a request. If the response is not received before the timeout elapses the client will resend
         * the request if necessary or fail the request if retries are exhausted.
         */
        var requestTimeout: Int? = null

        /**
         * The size of the TCP receive buffer (SO_RCVBUF) to use when reading data.
         * If the value is -1, the OS default will be used.
         */
        var bufferSize: Int? = null
    }
}