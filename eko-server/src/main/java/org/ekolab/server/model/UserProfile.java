package org.ekolab.server.model;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
  private List<UserLabStatistics> statistics;
  private UserInfo userInfo;
  private StudentInfo studentInfo;
  private byte[] picture;
  private double averageMark;
  private double averagePointCount;
}
