package org.ekolab.server.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserProfile {
  private List<UserLabStatistics> statistics;
  private UserInfo userInfo;
  private Set<Integer> allowedLabs;
  private StudentInfo studentInfo;
  private byte[] picture;
  private double averageMark;
  private double averagePointCount;
}
