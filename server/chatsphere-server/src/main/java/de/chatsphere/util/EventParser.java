package de.chatsphere.util;

import de.chatsphere.api.shared.transfer.DefaultEvent;
import de.chatsphere.server.rxbus.Event;
import graphql.ExecutionResult;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The event parser converts an execution result made by GraphQL (simple the query result) to an
 * event POJO.
 */
public class EventParser {

  private static final Logger log = LoggerFactory.getLogger(EventParser.class);

  /**
   * Parse the execution result into event.
   *
   * <p>SAMPLE ExecutionResult object:
   * <pre>
   * <code>
   * {
   *    data={
   *      linkAdded={
   *        response={
   *          link={
   *            id=21,
   *            description=,
   *            __typename=Link
   *          },
   *          __typename=CreateLinkMutationResponse
   *        },
   *        sender=User98957,
   *        recipient=,
   *        __typename=Event
   *      }
   *    },
   *    errors=[],
   *    dataPresent=true,
   *    extensions=null
   * }
   * </code>
   * </pre>
   *
   * @param executionResult the execution result
   *
   * @return the event
   */
  public static Event parse(ExecutionResult executionResult) {
    Map<String, Object> result = Util.toMap(executionResult.getData());
    String operationName = result.keySet().iterator().next();
    Map<String, Object> data = Util.toMap(result.get(operationName));

    String sender = extractSender(data);
    List<String> recipients = extractRecipients(data);

    return new DefaultEvent(sender, recipients);
  }

  /**
   * Get the sender field of the data object.
   *
   * @param data the data object
   *
   * @return the sender field
   */
  private static String extractSender(Map<String, Object> data) {
    if (!Util.hasObject(data, "sender")) {
      return null;
    }

    return (String) data.get("sender");
  }

  /**
   * Get the recipient field of the data object.
   *
   * @param data the data object
   *
   * @return the recipient field
   */
  private static List<String> extractRecipients(Map<String, Object> data) {
    if (!Util.hasObject(data, "recipients")) {
      return null;
    }
    @SuppressWarnings("unchecked")
    List<String> recipients = (List<String>) data.get("recipients");
    ;
    return recipients;
  }
}
