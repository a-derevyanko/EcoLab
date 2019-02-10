package org.ecolab.server.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserProfile {
  private List<UserLabStatistics> statistics;
  private UserInfo userInfo;
  private Set<Integer> allowedLabs;
  private Set<Integer> allowedDefence;
  private StudentInfo studentInfo;
  private byte[] picture;
  private int averageMark;
  private int averagePointCount;
}
