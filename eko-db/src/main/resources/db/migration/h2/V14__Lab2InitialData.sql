INSERT INTO LAB2DATA (ID, USER_ID, START_DATE, SAVE_DATE, VERSION,
                             BAROMETRIC_PRESSURE,
                             INDOORS_TEMPERATURE, ROOM_SIZE,
                             QUANTITY_OF_SINGLE_TYPE_EQUIPMENT,
                             HEMISPHERE_RADIUS,
                             AVERAGE_SOUND_PRESSURE,
                             CORRECTION_FACTOR, SOUND_PRESSURE_MEASURING_SURFACE,
                             HEMISPHERE_SURFACE, MEASURING_FACTOR,
                             SOUND_POWER_LEVEL, ROOM_CONSTANT_1000,
                             FREQUENCY_COEFFICIENT, ROOM_CONSTANT, REFLECTED_SOUND_POWER)
VALUES
  (0, 0, '2018-02-15 09:00:19.548000000', '2018-02-15 09:17:45.825000000', NULL, 755, 25, 180, 1, 4, (100, 100,
                                                                                                           100, 100,
                                                                                                           100, 100,
                                                                                                           100, 100,
                                                                                                           100),
                                                                                                           -0.067244,
   99.93, 100.53096491487338, 20.02, 119.95, 9, 1.4, 12.6, 114.95);

INSERT INTO LAB2_RANDOM_VARIANT (ID, VERSION, NAME, BAROMETRIC_PRESSURE,
                                        INDOORS_TEMPERATURE, QUANTITY_OF_SINGLE_TYPE_EQUIPMENT,
                                        AVERAGE_SOUND_PRESSURE, ESTIMATED_GEOMETRIC_MEAN_FREQUENCY)
VALUES (0, NULL, 'SUPPLING_PUMP', 795, 20, 1, (98.2, 100.8, 99.4, 117.8, 117.2, 116.4, 110.8, 95, 95.8), 1000);