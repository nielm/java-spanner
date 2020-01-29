/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/spanner/v1/result_set.proto

package com.google.spanner.v1;

public interface PartialResultSetOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.spanner.v1.PartialResultSet)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Metadata about the result set, such as row type information.
   * Only present in the first response.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetMetadata metadata = 1;</code>
   *
   * @return Whether the metadata field is set.
   */
  boolean hasMetadata();
  /**
   *
   *
   * <pre>
   * Metadata about the result set, such as row type information.
   * Only present in the first response.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetMetadata metadata = 1;</code>
   *
   * @return The metadata.
   */
  com.google.spanner.v1.ResultSetMetadata getMetadata();
  /**
   *
   *
   * <pre>
   * Metadata about the result set, such as row type information.
   * Only present in the first response.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetMetadata metadata = 1;</code>
   */
  com.google.spanner.v1.ResultSetMetadataOrBuilder getMetadataOrBuilder();

  /**
   *
   *
   * <pre>
   * A streamed result set consists of a stream of values, which might
   * be split into many `PartialResultSet` messages to accommodate
   * large rows and/or large values. Every N complete values defines a
   * row, where N is equal to the number of entries in
   * [metadata.row_type.fields][google.spanner.v1.StructType.fields].
   * Most values are encoded based on type as described
   * [here][google.spanner.v1.TypeCode].
   * It is possible that the last value in values is "chunked",
   * meaning that the rest of the value is sent in subsequent
   * `PartialResultSet`(s). This is denoted by the [chunked_value][google.spanner.v1.PartialResultSet.chunked_value]
   * field. Two or more chunked values can be merged to form a
   * complete value as follows:
   *   * `bool/number/null`: cannot be chunked
   *   * `string`: concatenate the strings
   *   * `list`: concatenate the lists. If the last element in a list is a
   *     `string`, `list`, or `object`, merge it with the first element in
   *     the next list by applying these rules recursively.
   *   * `object`: concatenate the (field name, field value) pairs. If a
   *     field name is duplicated, then apply these rules recursively
   *     to merge the field values.
   * Some examples of merging:
   *     # Strings are concatenated.
   *     "foo", "bar" =&gt; "foobar"
   *     # Lists of non-strings are concatenated.
   *     [2, 3], [4] =&gt; [2, 3, 4]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are strings.
   *     ["a", "b"], ["c", "d"] =&gt; ["a", "bc", "d"]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are lists. Recursively, the last and first elements
   *     # of the inner lists are merged because they are strings.
   *     ["a", ["b", "c"]], [["d"], "e"] =&gt; ["a", ["b", "cd"], "e"]
   *     # Non-overlapping object fields are combined.
   *     {"a": "1"}, {"b": "2"} =&gt; {"a": "1", "b": 2"}
   *     # Overlapping object fields are merged.
   *     {"a": "1"}, {"a": "2"} =&gt; {"a": "12"}
   *     # Examples of merging objects containing lists of strings.
   *     {"a": ["1"]}, {"a": ["2"]} =&gt; {"a": ["12"]}
   * For a more complete example, suppose a streaming SQL query is
   * yielding a result set whose rows contain a single string
   * field. The following `PartialResultSet`s might be yielded:
   *     {
   *       "metadata": { ... }
   *       "values": ["Hello", "W"]
   *       "chunked_value": true
   *       "resume_token": "Af65..."
   *     }
   *     {
   *       "values": ["orl"]
   *       "chunked_value": true
   *       "resume_token": "Bqp2..."
   *     }
   *     {
   *       "values": ["d"]
   *       "resume_token": "Zx1B..."
   *     }
   * This sequence of `PartialResultSet`s encodes two rows, one
   * containing the field value `"Hello"`, and a second containing the
   * field value `"World" = "W" + "orl" + "d"`.
   * </pre>
   *
   * <code>repeated .google.protobuf.Value values = 2;</code>
   */
  java.util.List<com.google.protobuf.Value> getValuesList();
  /**
   *
   *
   * <pre>
   * A streamed result set consists of a stream of values, which might
   * be split into many `PartialResultSet` messages to accommodate
   * large rows and/or large values. Every N complete values defines a
   * row, where N is equal to the number of entries in
   * [metadata.row_type.fields][google.spanner.v1.StructType.fields].
   * Most values are encoded based on type as described
   * [here][google.spanner.v1.TypeCode].
   * It is possible that the last value in values is "chunked",
   * meaning that the rest of the value is sent in subsequent
   * `PartialResultSet`(s). This is denoted by the [chunked_value][google.spanner.v1.PartialResultSet.chunked_value]
   * field. Two or more chunked values can be merged to form a
   * complete value as follows:
   *   * `bool/number/null`: cannot be chunked
   *   * `string`: concatenate the strings
   *   * `list`: concatenate the lists. If the last element in a list is a
   *     `string`, `list`, or `object`, merge it with the first element in
   *     the next list by applying these rules recursively.
   *   * `object`: concatenate the (field name, field value) pairs. If a
   *     field name is duplicated, then apply these rules recursively
   *     to merge the field values.
   * Some examples of merging:
   *     # Strings are concatenated.
   *     "foo", "bar" =&gt; "foobar"
   *     # Lists of non-strings are concatenated.
   *     [2, 3], [4] =&gt; [2, 3, 4]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are strings.
   *     ["a", "b"], ["c", "d"] =&gt; ["a", "bc", "d"]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are lists. Recursively, the last and first elements
   *     # of the inner lists are merged because they are strings.
   *     ["a", ["b", "c"]], [["d"], "e"] =&gt; ["a", ["b", "cd"], "e"]
   *     # Non-overlapping object fields are combined.
   *     {"a": "1"}, {"b": "2"} =&gt; {"a": "1", "b": 2"}
   *     # Overlapping object fields are merged.
   *     {"a": "1"}, {"a": "2"} =&gt; {"a": "12"}
   *     # Examples of merging objects containing lists of strings.
   *     {"a": ["1"]}, {"a": ["2"]} =&gt; {"a": ["12"]}
   * For a more complete example, suppose a streaming SQL query is
   * yielding a result set whose rows contain a single string
   * field. The following `PartialResultSet`s might be yielded:
   *     {
   *       "metadata": { ... }
   *       "values": ["Hello", "W"]
   *       "chunked_value": true
   *       "resume_token": "Af65..."
   *     }
   *     {
   *       "values": ["orl"]
   *       "chunked_value": true
   *       "resume_token": "Bqp2..."
   *     }
   *     {
   *       "values": ["d"]
   *       "resume_token": "Zx1B..."
   *     }
   * This sequence of `PartialResultSet`s encodes two rows, one
   * containing the field value `"Hello"`, and a second containing the
   * field value `"World" = "W" + "orl" + "d"`.
   * </pre>
   *
   * <code>repeated .google.protobuf.Value values = 2;</code>
   */
  com.google.protobuf.Value getValues(int index);
  /**
   *
   *
   * <pre>
   * A streamed result set consists of a stream of values, which might
   * be split into many `PartialResultSet` messages to accommodate
   * large rows and/or large values. Every N complete values defines a
   * row, where N is equal to the number of entries in
   * [metadata.row_type.fields][google.spanner.v1.StructType.fields].
   * Most values are encoded based on type as described
   * [here][google.spanner.v1.TypeCode].
   * It is possible that the last value in values is "chunked",
   * meaning that the rest of the value is sent in subsequent
   * `PartialResultSet`(s). This is denoted by the [chunked_value][google.spanner.v1.PartialResultSet.chunked_value]
   * field. Two or more chunked values can be merged to form a
   * complete value as follows:
   *   * `bool/number/null`: cannot be chunked
   *   * `string`: concatenate the strings
   *   * `list`: concatenate the lists. If the last element in a list is a
   *     `string`, `list`, or `object`, merge it with the first element in
   *     the next list by applying these rules recursively.
   *   * `object`: concatenate the (field name, field value) pairs. If a
   *     field name is duplicated, then apply these rules recursively
   *     to merge the field values.
   * Some examples of merging:
   *     # Strings are concatenated.
   *     "foo", "bar" =&gt; "foobar"
   *     # Lists of non-strings are concatenated.
   *     [2, 3], [4] =&gt; [2, 3, 4]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are strings.
   *     ["a", "b"], ["c", "d"] =&gt; ["a", "bc", "d"]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are lists. Recursively, the last and first elements
   *     # of the inner lists are merged because they are strings.
   *     ["a", ["b", "c"]], [["d"], "e"] =&gt; ["a", ["b", "cd"], "e"]
   *     # Non-overlapping object fields are combined.
   *     {"a": "1"}, {"b": "2"} =&gt; {"a": "1", "b": 2"}
   *     # Overlapping object fields are merged.
   *     {"a": "1"}, {"a": "2"} =&gt; {"a": "12"}
   *     # Examples of merging objects containing lists of strings.
   *     {"a": ["1"]}, {"a": ["2"]} =&gt; {"a": ["12"]}
   * For a more complete example, suppose a streaming SQL query is
   * yielding a result set whose rows contain a single string
   * field. The following `PartialResultSet`s might be yielded:
   *     {
   *       "metadata": { ... }
   *       "values": ["Hello", "W"]
   *       "chunked_value": true
   *       "resume_token": "Af65..."
   *     }
   *     {
   *       "values": ["orl"]
   *       "chunked_value": true
   *       "resume_token": "Bqp2..."
   *     }
   *     {
   *       "values": ["d"]
   *       "resume_token": "Zx1B..."
   *     }
   * This sequence of `PartialResultSet`s encodes two rows, one
   * containing the field value `"Hello"`, and a second containing the
   * field value `"World" = "W" + "orl" + "d"`.
   * </pre>
   *
   * <code>repeated .google.protobuf.Value values = 2;</code>
   */
  int getValuesCount();
  /**
   *
   *
   * <pre>
   * A streamed result set consists of a stream of values, which might
   * be split into many `PartialResultSet` messages to accommodate
   * large rows and/or large values. Every N complete values defines a
   * row, where N is equal to the number of entries in
   * [metadata.row_type.fields][google.spanner.v1.StructType.fields].
   * Most values are encoded based on type as described
   * [here][google.spanner.v1.TypeCode].
   * It is possible that the last value in values is "chunked",
   * meaning that the rest of the value is sent in subsequent
   * `PartialResultSet`(s). This is denoted by the [chunked_value][google.spanner.v1.PartialResultSet.chunked_value]
   * field. Two or more chunked values can be merged to form a
   * complete value as follows:
   *   * `bool/number/null`: cannot be chunked
   *   * `string`: concatenate the strings
   *   * `list`: concatenate the lists. If the last element in a list is a
   *     `string`, `list`, or `object`, merge it with the first element in
   *     the next list by applying these rules recursively.
   *   * `object`: concatenate the (field name, field value) pairs. If a
   *     field name is duplicated, then apply these rules recursively
   *     to merge the field values.
   * Some examples of merging:
   *     # Strings are concatenated.
   *     "foo", "bar" =&gt; "foobar"
   *     # Lists of non-strings are concatenated.
   *     [2, 3], [4] =&gt; [2, 3, 4]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are strings.
   *     ["a", "b"], ["c", "d"] =&gt; ["a", "bc", "d"]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are lists. Recursively, the last and first elements
   *     # of the inner lists are merged because they are strings.
   *     ["a", ["b", "c"]], [["d"], "e"] =&gt; ["a", ["b", "cd"], "e"]
   *     # Non-overlapping object fields are combined.
   *     {"a": "1"}, {"b": "2"} =&gt; {"a": "1", "b": 2"}
   *     # Overlapping object fields are merged.
   *     {"a": "1"}, {"a": "2"} =&gt; {"a": "12"}
   *     # Examples of merging objects containing lists of strings.
   *     {"a": ["1"]}, {"a": ["2"]} =&gt; {"a": ["12"]}
   * For a more complete example, suppose a streaming SQL query is
   * yielding a result set whose rows contain a single string
   * field. The following `PartialResultSet`s might be yielded:
   *     {
   *       "metadata": { ... }
   *       "values": ["Hello", "W"]
   *       "chunked_value": true
   *       "resume_token": "Af65..."
   *     }
   *     {
   *       "values": ["orl"]
   *       "chunked_value": true
   *       "resume_token": "Bqp2..."
   *     }
   *     {
   *       "values": ["d"]
   *       "resume_token": "Zx1B..."
   *     }
   * This sequence of `PartialResultSet`s encodes two rows, one
   * containing the field value `"Hello"`, and a second containing the
   * field value `"World" = "W" + "orl" + "d"`.
   * </pre>
   *
   * <code>repeated .google.protobuf.Value values = 2;</code>
   */
  java.util.List<? extends com.google.protobuf.ValueOrBuilder> getValuesOrBuilderList();
  /**
   *
   *
   * <pre>
   * A streamed result set consists of a stream of values, which might
   * be split into many `PartialResultSet` messages to accommodate
   * large rows and/or large values. Every N complete values defines a
   * row, where N is equal to the number of entries in
   * [metadata.row_type.fields][google.spanner.v1.StructType.fields].
   * Most values are encoded based on type as described
   * [here][google.spanner.v1.TypeCode].
   * It is possible that the last value in values is "chunked",
   * meaning that the rest of the value is sent in subsequent
   * `PartialResultSet`(s). This is denoted by the [chunked_value][google.spanner.v1.PartialResultSet.chunked_value]
   * field. Two or more chunked values can be merged to form a
   * complete value as follows:
   *   * `bool/number/null`: cannot be chunked
   *   * `string`: concatenate the strings
   *   * `list`: concatenate the lists. If the last element in a list is a
   *     `string`, `list`, or `object`, merge it with the first element in
   *     the next list by applying these rules recursively.
   *   * `object`: concatenate the (field name, field value) pairs. If a
   *     field name is duplicated, then apply these rules recursively
   *     to merge the field values.
   * Some examples of merging:
   *     # Strings are concatenated.
   *     "foo", "bar" =&gt; "foobar"
   *     # Lists of non-strings are concatenated.
   *     [2, 3], [4] =&gt; [2, 3, 4]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are strings.
   *     ["a", "b"], ["c", "d"] =&gt; ["a", "bc", "d"]
   *     # Lists are concatenated, but the last and first elements are merged
   *     # because they are lists. Recursively, the last and first elements
   *     # of the inner lists are merged because they are strings.
   *     ["a", ["b", "c"]], [["d"], "e"] =&gt; ["a", ["b", "cd"], "e"]
   *     # Non-overlapping object fields are combined.
   *     {"a": "1"}, {"b": "2"} =&gt; {"a": "1", "b": 2"}
   *     # Overlapping object fields are merged.
   *     {"a": "1"}, {"a": "2"} =&gt; {"a": "12"}
   *     # Examples of merging objects containing lists of strings.
   *     {"a": ["1"]}, {"a": ["2"]} =&gt; {"a": ["12"]}
   * For a more complete example, suppose a streaming SQL query is
   * yielding a result set whose rows contain a single string
   * field. The following `PartialResultSet`s might be yielded:
   *     {
   *       "metadata": { ... }
   *       "values": ["Hello", "W"]
   *       "chunked_value": true
   *       "resume_token": "Af65..."
   *     }
   *     {
   *       "values": ["orl"]
   *       "chunked_value": true
   *       "resume_token": "Bqp2..."
   *     }
   *     {
   *       "values": ["d"]
   *       "resume_token": "Zx1B..."
   *     }
   * This sequence of `PartialResultSet`s encodes two rows, one
   * containing the field value `"Hello"`, and a second containing the
   * field value `"World" = "W" + "orl" + "d"`.
   * </pre>
   *
   * <code>repeated .google.protobuf.Value values = 2;</code>
   */
  com.google.protobuf.ValueOrBuilder getValuesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * If true, then the final value in [values][google.spanner.v1.PartialResultSet.values] is chunked, and must
   * be combined with more values from subsequent `PartialResultSet`s
   * to obtain a complete field value.
   * </pre>
   *
   * <code>bool chunked_value = 3;</code>
   *
   * @return The chunkedValue.
   */
  boolean getChunkedValue();

  /**
   *
   *
   * <pre>
   * Streaming calls might be interrupted for a variety of reasons, such
   * as TCP connection loss. If this occurs, the stream of results can
   * be resumed by re-sending the original request and including
   * `resume_token`. Note that executing any other transaction in the
   * same session invalidates the token.
   * </pre>
   *
   * <code>bytes resume_token = 4;</code>
   *
   * @return The resumeToken.
   */
  com.google.protobuf.ByteString getResumeToken();

  /**
   *
   *
   * <pre>
   * Query plan and execution statistics for the statement that produced this
   * streaming result set. These can be requested by setting
   * [ExecuteSqlRequest.query_mode][google.spanner.v1.ExecuteSqlRequest.query_mode] and are sent
   * only once with the last response in the stream.
   * This field will also be present in the last response for DML
   * statements.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetStats stats = 5;</code>
   *
   * @return Whether the stats field is set.
   */
  boolean hasStats();
  /**
   *
   *
   * <pre>
   * Query plan and execution statistics for the statement that produced this
   * streaming result set. These can be requested by setting
   * [ExecuteSqlRequest.query_mode][google.spanner.v1.ExecuteSqlRequest.query_mode] and are sent
   * only once with the last response in the stream.
   * This field will also be present in the last response for DML
   * statements.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetStats stats = 5;</code>
   *
   * @return The stats.
   */
  com.google.spanner.v1.ResultSetStats getStats();
  /**
   *
   *
   * <pre>
   * Query plan and execution statistics for the statement that produced this
   * streaming result set. These can be requested by setting
   * [ExecuteSqlRequest.query_mode][google.spanner.v1.ExecuteSqlRequest.query_mode] and are sent
   * only once with the last response in the stream.
   * This field will also be present in the last response for DML
   * statements.
   * </pre>
   *
   * <code>.google.spanner.v1.ResultSetStats stats = 5;</code>
   */
  com.google.spanner.v1.ResultSetStatsOrBuilder getStatsOrBuilder();
}
