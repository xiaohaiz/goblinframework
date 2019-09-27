package org.goblinframework.database.core.eql;

public enum Operator {

  // ==========================================================================
  // Comparison Query Operators
  // ==========================================================================
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/eq/
   */
  $eq,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/gt/
   */
  $gt,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/gte/
   */
  $gte,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/lt/
   */
  $lt,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/lte/
   */
  $lte,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/ne/
   */
  $ne,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/in/
   */
  $in,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/nin/
   */
  $nin,
  /**
   * Field operator, works with specified field.
   * N/A for mongo
   */
  $like,

  // ==========================================================================
  // Logical Query Operators
  // ==========================================================================
  /**
   * Criteria operators, works with one or more criteria
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/or/
   */
  $or,
  /**
   * Criteria operators, works with one or more criteria
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/and/
   */
  $and,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/not/
   */
  $not,
  /**
   * Criteria operators, works with one or more criteria
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/nor/
   */
  $nor,

  // ==========================================================================
  // Element Query Operators
  // ==========================================================================
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/not/
   * For jdbc usage, the operator has difference semantic meaning
   * $exists(true) means IS NOT NULL
   * $exists(false) means IS NULL
   */
  $exists,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/type/
   * Double	                  1	“double”
   * String	                  2	“string”
   * Object	                  3	“object”
   * Array	                  4	“array”
   * Binary data	            5	“binData”
   * Undefined	              6	“undefined”	Deprecated.
   * ObjectId	                7	“objectId”
   * Boolean	                8	“bool”
   * Date	                    9	“date”
   * Null	                    10	“null”
   * Regular Expression	      11	“regex”
   * DBPointer	              12	“dbPointer”
   * JavaScript	              13	“javascript”
   * Symbol	                  14	“symbol”
   * JavaScript (with scope)	15	“javascriptWithScope”
   * 32-bit integer	          16	“int”
   * Timestamp	              17	“timestamp”
   * 64-bit integer	          18	“long”
   * Min key	                -1	“minKey”
   * Max key	                127	“maxKey”
   * N/A for jdbc
   */
  $type,

  // ==========================================================================
  // Evaluation Query Operators
  // ==========================================================================
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/mod/
   * N/A for jdbc
   */
  $mod,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/regex/
   * N/A for jdbc
   */
  $regex,

  // ==========================================================================
  // Query Operator Array
  // ==========================================================================
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/all/
   * N/A for jdbc
   */
  $all,

  /**
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/query/elemMatch/index.html
   * N/A for jdbc
   */
  $elemMatch,

  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/query/size/
   * N/A for jdbc
   */
  $size,

  // ==========================================================================
  // Field Update Operators
  // ==========================================================================
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/inc/
   */
  $inc,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/mul/
   */
  $mul,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/setOnInsert/
   * N/A for jdbc
   */
  $setOnInsert,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/set/
   */
  $set,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/unset/
   * For jdbc, $unset means SET FIELD NULL
   */
  $unset,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/min/
   * N/A for jdbc
   */
  $min,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/max/
   * N/A for jdbc
   */
  $max,
  /**
   * Field operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.org/manual/reference/operator/update/currentDate/
   * For mysql, set field=NOW()
   */
  $currentDate,


  // ==========================================================================
  // Array Update Operators
  // ==========================================================================

  /**
   * Array update operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/update/addToSet/
   */
  $addToSet,

  /**
   * Array update operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/update/pop/
   */
  $pop,

  /**
   * Array update operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/update/pullAll/
   */
  $pullAll,

  /**
   * Array update operator
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/update/pull/
   */
  $pull,

  @Deprecated
  $pushAll,

  /**
   * Array update operator, works with specified field.
   * For more information of mongo usage please refer to
   * https://docs.mongodb.com/manual/reference/operator/update/push/
   */
  $push,

  @Deprecated
  $isNull,
  @Deprecated
  $isNotNull;


}
