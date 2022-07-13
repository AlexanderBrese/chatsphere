package de.chatsphere.server.graphql;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The standard handling of data fetcher error involves placing a
 * {@link ModifiedExceptionWhileDataFetching} error into the error collection.
 */
public class ModifiedDataFetcherExceptionHandler implements DataFetcherExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(DataFetcherExceptionHandler.class);

  @Override
  public void accept(DataFetcherExceptionHandlerParameters handlerParameters) {
    Throwable exception = new Throwable(handlerParameters.getException().getMessage());
    SourceLocation sourceLocation = handlerParameters.getField().getSourceLocation();
    ExecutionPath path = handlerParameters.getPath();

    ModifiedExceptionWhileDataFetching error =
      new ModifiedExceptionWhileDataFetching(path, exception, sourceLocation);
    handlerParameters.getExecutionContext().addError(error);

    log.warn(error.getMessage(), handlerParameters.getException());
  }
}
