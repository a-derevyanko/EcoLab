package org.ekolab.server.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLabStatistics {
  private int labNumber;
  private int tryCount;
  private int mark;
  private int pointCount;
  private LocalDateTime labDate;
  private LocalDateTime testDate;
}
