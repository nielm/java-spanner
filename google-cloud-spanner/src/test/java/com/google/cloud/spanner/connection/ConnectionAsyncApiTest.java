/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.connection;

import static com.google.cloud.spanner.SpannerApiFutures.get;
import static com.google.common.truth.Truth.assertThat;

import com.google.api.core.ApiFuture;
import com.google.cloud.spanner.AsyncResultSet;
import com.google.cloud.spanner.AsyncResultSet.CallbackResponse;
import com.google.cloud.spanner.AsyncResultSet.ReadyCallback;
import com.google.cloud.spanner.ErrorCode;
import com.google.cloud.spanner.MockSpannerServiceImpl.SimulatedExecutionTime;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.SpannerApiFutures;
import com.google.cloud.spanner.SpannerException;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConnectionAsyncApiTest extends AbstractMockServerTest {
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();
  private static final Function<Connection, Void> AUTOCOMMIT =
      new Function<Connection, Void>() {
        @Override
        public Void apply(Connection input) {
          input.setAutocommit(true);
          return null;
        }
      };
  private static final Function<Connection, Void> READ_ONLY =
      new Function<Connection, Void>() {
        @Override
        public Void apply(Connection input) {
          input.setReadOnly(true);
          return null;
        }
      };
  private static final Function<Connection, Void> READ_WRITE =
      new Function<Connection, Void>() {
        @Override
        public Void apply(Connection input) {
          return null;
        }
      };

  @AfterClass
  public static void stopExecutor() {
    executor.shutdown();
  }

  @After
  public void reset() {
    mockSpanner.removeAllExecutionTimes();
  }

  @Test
  public void testExecuteQueryAsyncAutocommit() {
    testExecuteQueryAsync(AUTOCOMMIT);
  }

  @Test
  public void testExecuteQueryAsyncAutocommitIsNonBlocking() {
    testExecuteQueryAsyncIsNonBlocking(AUTOCOMMIT);
  }

  @Test
  public void testExecuteQueryAsStatementAsyncAutocommit() {
    testExecuteQueryAsync(AUTOCOMMIT, true);
  }

  @Test
  public void testExecuteQueryAutocommit() {
    testExecuteQuery(AUTOCOMMIT);
  }

  @Test
  public void testExecuteUpdateAsyncAutocommit() {
    testExecuteUpdateAsync(AUTOCOMMIT);
  }

  @Test
  public void testExecuteUpdateAsyncAutocommitIsNonBlocking() {
    testExecuteUpdateAsyncIsNonBlocking(AUTOCOMMIT);
  }

  @Test
  public void testExecuteUpdateAsStatementAsyncAutocommit() {
    testExecuteUpdateAsync(AUTOCOMMIT, true);
  }

  @Test
  public void testExecuteUpdateAutocommit() {
    testExecuteUpdate(AUTOCOMMIT);
  }

  @Test
  public void testExecuteBatchUpdateAsyncAutocommit() {
    testExecuteBatchUpdateAsync(AUTOCOMMIT);
  }

  @Test
  public void testExecuteBatchUpdateAsyncAutocommitIsNonBlocking() {
    testExecuteBatchUpdateAsyncIsNonBlocking(AUTOCOMMIT);
  }

  @Test
  public void testExecuteBatchUpdateAutocommit() {
    testExecuteBatchUpdate(AUTOCOMMIT);
  }

  @Test
  public void testWriteAsyncAutocommit() {
    testWriteAsync(AUTOCOMMIT);
  }

  @Test
  public void testWriteAutocommit() {
    testWrite(AUTOCOMMIT);
  }

  @Test
  public void testExecuteQueryAsyncReadOnly() {
    testExecuteQueryAsync(READ_ONLY);
  }

  @Test
  public void testExecuteQueryAsyncReadOnlyIsNonBlocking() {
    testExecuteQueryAsyncIsNonBlocking(READ_ONLY);
  }

  @Test
  public void testExecuteQueryAsStatementAsyncReadOnly() {
    testExecuteQueryAsync(READ_ONLY, true);
  }

  @Test
  public void testExecuteQueryReadOnly() {
    testExecuteQuery(READ_ONLY);
  }

  @Test
  public void testExecuteQueryAsyncReadWrite() {
    testExecuteQueryAsync(READ_WRITE);
  }

  @Test
  public void testExecuteQueryAsyncReadWriteIsNonBlocking() {
    testExecuteQueryAsyncIsNonBlocking(READ_WRITE);
  }

  @Test
  public void testExecuteQueryAsStatementAsyncReadWrite() {
    testExecuteQueryAsync(READ_WRITE, true);
  }

  @Test
  public void testExecuteQueryReadWrite() {
    testExecuteQuery(READ_WRITE);
  }

  @Test
  public void testExecuteUpdateAsyncReadWrite() {
    testExecuteUpdateAsync(READ_WRITE);
  }

  @Test
  public void testExecuteUpdateAsyncReadWriteIsNonBlocking() {
    testExecuteUpdateAsyncIsNonBlocking(READ_WRITE);
  }

  @Test
  public void testExecuteUpdateAsStatementAsyncReadWrite() {
    testExecuteUpdateAsync(READ_WRITE, true);
  }

  @Test
  public void testExecuteUpdateReadWrite() {
    testExecuteUpdate(READ_WRITE);
  }

  @Test
  public void testExecuteBatchUpdateAsyncReadWrite() {
    testExecuteBatchUpdateAsync(READ_WRITE);
  }

  @Test
  public void testExecuteBatchUpdateAsyncReadWriteIsNonBlocking() {
    testExecuteBatchUpdateAsyncIsNonBlocking(READ_WRITE);
  }

  @Test
  public void testExecuteBatchUpdateReadWrite() {
    testExecuteBatchUpdate(READ_WRITE);
  }

  @Test
  public void testBufferedWriteReadWrite() {
    testBufferedWrite(READ_WRITE);
  }

  private void testExecuteQueryAsync(Function<Connection, Void> connectionConfigurator) {
    testExecuteQueryAsync(connectionConfigurator, false);
  }

  private void testExecuteQueryAsync(
      Function<Connection, Void> connectionConfigurator, boolean executeAsStatement) {
    ApiFuture<Void> res;
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        final AtomicInteger rowCount = new AtomicInteger();
        final AtomicBoolean receivedTimeout = new AtomicBoolean();
        if (timeout) {
          mockSpanner.setExecuteStreamingSqlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try (AsyncResultSet rs =
            executeAsStatement
                ? connection.executeAsync(SELECT_RANDOM_STATEMENT).getResultSetAsync()
                : connection.executeQueryAsync(SELECT_RANDOM_STATEMENT)) {
          res =
              rs.setCallback(
                  executor,
                  new ReadyCallback() {
                    @Override
                    public CallbackResponse cursorReady(AsyncResultSet resultSet) {
                      try {
                        while (true) {
                          switch (resultSet.tryNext()) {
                            case OK:
                              rowCount.incrementAndGet();
                              break;
                            case DONE:
                              return CallbackResponse.DONE;
                            case NOT_READY:
                              return CallbackResponse.CONTINUE;
                          }
                        }
                      } catch (SpannerException e) {
                        receivedTimeout.set(e.getErrorCode() == ErrorCode.DEADLINE_EXCEEDED);
                        throw e;
                      }
                    }
                  });
        }
        try {
          SpannerApiFutures.get(res);
          assertThat(rowCount.get()).isEqualTo(RANDOM_RESULT_SET_ROW_COUNT);
          if (connection.isReadOnly() || !connection.isInTransaction()) {
            assertThat(connection.getReadTimestamp()).isNotNull();
          }
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          assertThat(timeout).isTrue();
          assertThat(receivedTimeout.get()).isTrue();
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testExecuteQuery(Function<Connection, Void> connectionConfigurator) {
    long rowCount = 0L;
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setExecuteStreamingSqlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try (ResultSet rs = connection.executeQuery(SELECT_RANDOM_STATEMENT)) {
          while (rs.next()) {
            rowCount++;
          }
          assertThat(rowCount).isEqualTo(RANDOM_RESULT_SET_ROW_COUNT);
          if (connection.isReadOnly() || !connection.isInTransaction()) {
            assertThat(connection.getReadTimestamp()).isNotNull();
          }
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testExecuteUpdateAsync(Function<Connection, Void> connectionConfigurator) {
    testExecuteUpdateAsync(connectionConfigurator, false);
  }

  private void testExecuteUpdateAsync(
      Function<Connection, Void> connectionConfigurator, boolean executeAsStatement) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setExecuteSqlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        ApiFuture<Long> updateCount =
            executeAsStatement
                ? connection.executeAsync(INSERT_STATEMENT).getUpdateCountAsync()
                : connection.executeUpdateAsync(INSERT_STATEMENT);
        try {
          assertThat(get(updateCount)).isEqualTo(1L);
          if (connection.isInTransaction()) {
            connection.commitAsync();
          }
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testExecuteUpdate(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setExecuteSqlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try {
          long updateCount = connection.executeUpdate(INSERT_STATEMENT);
          assertThat(updateCount).isEqualTo(1L);
          if (connection.isInTransaction()) {
            connection.commit();
          }
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testExecuteBatchUpdateAsync(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setExecuteBatchDmlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        ApiFuture<long[]> updateCounts =
            connection.executeBatchUpdateAsync(
                ImmutableList.of(INSERT_STATEMENT, INSERT_STATEMENT));
        try {
          assertThat(get(updateCounts)).asList().containsExactly(1L, 1L);
          if (connection.isInTransaction()) {
            connection.commitAsync();
          }
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testExecuteBatchUpdate(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setExecuteBatchDmlExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try {
          long[] updateCounts =
              connection.executeBatchUpdate(ImmutableList.of(INSERT_STATEMENT, INSERT_STATEMENT));
          assertThat(updateCounts).asList().containsExactly(1L, 1L);
          if (connection.isInTransaction()) {
            connection.commit();
          }
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          assertThat(timeout).isTrue();
          // Start a new transaction if a timeout occurred on a read/write transaction, as that will
          // invalidate that transaction.
          if (!connection.isReadOnly() && connection.isInTransaction()) {
            connection.clearStatementTimeout();
            connection.rollback();
          }
        }
      }
    }
  }

  private void testWriteAsync(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setCommitExecutionTime(SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        ApiFuture<Void> fut =
            connection.writeAsync(
                ImmutableList.of(
                    Mutation.newInsertBuilder("foo").build(),
                    Mutation.newInsertBuilder("bar").build()));
        try {
          assertThat(get(fut)).isNull();
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
        }
      }
    }
  }

  private void testWrite(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setCommitExecutionTime(SimulatedExecutionTime.ofMinimumAndRandomTime(10, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try {
          connection.write(
              ImmutableList.of(
                  Mutation.newInsertBuilder("foo").build(),
                  Mutation.newInsertBuilder("bar").build()));
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
        }
      }
    }
  }

  private void testBufferedWrite(Function<Connection, Void> connectionConfigurator) {
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      for (boolean timeout : new boolean[] {true, false}) {
        if (timeout) {
          mockSpanner.setCommitExecutionTime(
              SimulatedExecutionTime.ofMinimumAndRandomTime(1000, 0));
          connection.setStatementTimeout(1L, TimeUnit.NANOSECONDS);
        } else {
          mockSpanner.removeAllExecutionTimes();
          connection.clearStatementTimeout();
        }
        try {
          connection.bufferedWrite(
              ImmutableList.of(
                  Mutation.newInsertBuilder("foo").build(),
                  Mutation.newInsertBuilder("bar").build()));
          connection.commitAsync();
          assertThat(connection.getCommitTimestamp()).isNotNull();
          assertThat(timeout).isFalse();
        } catch (SpannerException e) {
          assertThat(timeout).isTrue();
          assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DEADLINE_EXCEEDED);
          connection.clearStatementTimeout();
          connection.rollbackAsync();
        }
      }
    }
  }

  private void testExecuteQueryAsyncIsNonBlocking(
      Function<Connection, Void> connectionConfigurator) {
    ApiFuture<Void> res;
    final AtomicInteger rowCount = new AtomicInteger();
    mockSpanner.freeze();
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      try (AsyncResultSet rs = connection.executeQueryAsync(SELECT_RANDOM_STATEMENT)) {
        res =
            rs.setCallback(
                executor,
                new ReadyCallback() {
                  @Override
                  public CallbackResponse cursorReady(AsyncResultSet resultSet) {
                    while (true) {
                      switch (resultSet.tryNext()) {
                        case OK:
                          rowCount.incrementAndGet();
                          break;
                        case DONE:
                          return CallbackResponse.DONE;
                        case NOT_READY:
                          return CallbackResponse.CONTINUE;
                      }
                    }
                  }
                });
        mockSpanner.unfreeze();
      }
      SpannerApiFutures.get(res);
      assertThat(rowCount.get()).isEqualTo(RANDOM_RESULT_SET_ROW_COUNT);
    }
  }

  private void testExecuteUpdateAsyncIsNonBlocking(
      Function<Connection, Void> connectionConfigurator) {
    mockSpanner.freeze();
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      ApiFuture<Long> updateCount = connection.executeUpdateAsync(INSERT_STATEMENT);
      if (connection.isInTransaction()) {
        connection.commitAsync();
      }
      mockSpanner.unfreeze();
      assertThat(get(updateCount)).isEqualTo(1L);
      assertThat(connection.getCommitTimestamp()).isNotNull();
    }
  }

  private void testExecuteBatchUpdateAsyncIsNonBlocking(
      Function<Connection, Void> connectionConfigurator) {
    mockSpanner.freeze();
    try (Connection connection = createConnection()) {
      connectionConfigurator.apply(connection);
      ApiFuture<long[]> updateCounts =
          connection.executeBatchUpdateAsync(ImmutableList.of(INSERT_STATEMENT, INSERT_STATEMENT));
      if (connection.isInTransaction()) {
        connection.commitAsync();
      }
      mockSpanner.unfreeze();
      assertThat(get(updateCounts)).asList().containsExactly(1L, 1L);
      assertThat(connection.getCommitTimestamp()).isNotNull();
    }
  }
}