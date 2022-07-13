package de.chatsphere.api.shared.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Reportable {

  Logger logger = LoggerFactory.getLogger(Reportable.class);
  String INTERNAL_ERROR = "An internal error occurred.";
}
