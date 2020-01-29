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
// source: google/spanner/v1/mutation.proto

package com.google.spanner.v1;

public interface MutationOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.spanner.v1.Mutation)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Insert new rows in a table. If any of the rows already exist,
   * the write or transaction fails with error `ALREADY_EXISTS`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert = 1;</code>
   *
   * @return Whether the insert field is set.
   */
  boolean hasInsert();
  /**
   *
   *
   * <pre>
   * Insert new rows in a table. If any of the rows already exist,
   * the write or transaction fails with error `ALREADY_EXISTS`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert = 1;</code>
   *
   * @return The insert.
   */
  com.google.spanner.v1.Mutation.Write getInsert();
  /**
   *
   *
   * <pre>
   * Insert new rows in a table. If any of the rows already exist,
   * the write or transaction fails with error `ALREADY_EXISTS`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert = 1;</code>
   */
  com.google.spanner.v1.Mutation.WriteOrBuilder getInsertOrBuilder();

  /**
   *
   *
   * <pre>
   * Update existing rows in a table. If any of the rows does not
   * already exist, the transaction fails with error `NOT_FOUND`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write update = 2;</code>
   *
   * @return Whether the update field is set.
   */
  boolean hasUpdate();
  /**
   *
   *
   * <pre>
   * Update existing rows in a table. If any of the rows does not
   * already exist, the transaction fails with error `NOT_FOUND`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write update = 2;</code>
   *
   * @return The update.
   */
  com.google.spanner.v1.Mutation.Write getUpdate();
  /**
   *
   *
   * <pre>
   * Update existing rows in a table. If any of the rows does not
   * already exist, the transaction fails with error `NOT_FOUND`.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write update = 2;</code>
   */
  com.google.spanner.v1.Mutation.WriteOrBuilder getUpdateOrBuilder();

  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, then
   * its column values are overwritten with the ones provided. Any
   * column values not explicitly written are preserved.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert_or_update = 3;</code>
   *
   * @return Whether the insertOrUpdate field is set.
   */
  boolean hasInsertOrUpdate();
  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, then
   * its column values are overwritten with the ones provided. Any
   * column values not explicitly written are preserved.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert_or_update = 3;</code>
   *
   * @return The insertOrUpdate.
   */
  com.google.spanner.v1.Mutation.Write getInsertOrUpdate();
  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, then
   * its column values are overwritten with the ones provided. Any
   * column values not explicitly written are preserved.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write insert_or_update = 3;</code>
   */
  com.google.spanner.v1.Mutation.WriteOrBuilder getInsertOrUpdateOrBuilder();

  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, it is
   * deleted, and the column values provided are inserted
   * instead. Unlike [insert_or_update][google.spanner.v1.Mutation.insert_or_update], this means any values not
   * explicitly written become `NULL`.
   * In an interleaved table, if you create the child table with the
   * `ON DELETE CASCADE` annotation, then replacing a parent row
   * also deletes the child rows. Otherwise, you must delete the
   * child rows before you replace the parent row.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write replace = 4;</code>
   *
   * @return Whether the replace field is set.
   */
  boolean hasReplace();
  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, it is
   * deleted, and the column values provided are inserted
   * instead. Unlike [insert_or_update][google.spanner.v1.Mutation.insert_or_update], this means any values not
   * explicitly written become `NULL`.
   * In an interleaved table, if you create the child table with the
   * `ON DELETE CASCADE` annotation, then replacing a parent row
   * also deletes the child rows. Otherwise, you must delete the
   * child rows before you replace the parent row.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write replace = 4;</code>
   *
   * @return The replace.
   */
  com.google.spanner.v1.Mutation.Write getReplace();
  /**
   *
   *
   * <pre>
   * Like [insert][google.spanner.v1.Mutation.insert], except that if the row already exists, it is
   * deleted, and the column values provided are inserted
   * instead. Unlike [insert_or_update][google.spanner.v1.Mutation.insert_or_update], this means any values not
   * explicitly written become `NULL`.
   * In an interleaved table, if you create the child table with the
   * `ON DELETE CASCADE` annotation, then replacing a parent row
   * also deletes the child rows. Otherwise, you must delete the
   * child rows before you replace the parent row.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Write replace = 4;</code>
   */
  com.google.spanner.v1.Mutation.WriteOrBuilder getReplaceOrBuilder();

  /**
   *
   *
   * <pre>
   * Delete rows from a table. Succeeds whether or not the named
   * rows were present.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Delete delete = 5;</code>
   *
   * @return Whether the delete field is set.
   */
  boolean hasDelete();
  /**
   *
   *
   * <pre>
   * Delete rows from a table. Succeeds whether or not the named
   * rows were present.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Delete delete = 5;</code>
   *
   * @return The delete.
   */
  com.google.spanner.v1.Mutation.Delete getDelete();
  /**
   *
   *
   * <pre>
   * Delete rows from a table. Succeeds whether or not the named
   * rows were present.
   * </pre>
   *
   * <code>.google.spanner.v1.Mutation.Delete delete = 5;</code>
   */
  com.google.spanner.v1.Mutation.DeleteOrBuilder getDeleteOrBuilder();

  public com.google.spanner.v1.Mutation.OperationCase getOperationCase();
}
