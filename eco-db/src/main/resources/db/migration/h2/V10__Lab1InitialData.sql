INSERT INTO LAB1DATA (ID, START_DATE, SAVE_DATE, VERSION,
                      STACKS_HEIGHT,
                      STACKS_DIAMETER, OUTSIDE_AIR_TEMPERATURE,
                      STEAM_PRODUCTION_CAPACITY, OXYGEN_CONCENTRATION_POINT,
                      FUEL_CONSUMER_NORMALIZED, STACK_EXIT_TEMPERATURE,
                      FLUE_GAS_NOX_CONCENTRATION,
                      EXCESS_AIR_RATIO, FLUE_GAS_NOX_CONCENTRATION_NC,
                      EXCESS_OF_NORMS, FLUE_GASES_RATE,
                      DRY_GASES_FLOW_RATE, MASS_EMISSIONS, FLUE_GASES_SPEED,
                      F, M, U, N, D,
                      HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT, TERRAIN_COEFFICIENT,
                      TEMPERATURE_COEFFICIENT,
                      DISTANCE_FROM_EMISSION_SOURCE, MAXIMUM_SURFACE_CONCENTRATION)
VALUES
  (0, '2018-02-02 08:40:13.064000000',
      '2018-02-02 09:14:37.797000000', NULL,
      45, 1.4, 20, 40, 6.8, 3300, 132,
                            155, 1.48,
                            163.86,
                            TRUE,
                            20.14,
                            12.31,
                            1.908,
                            13.09,
    1.06, 0.89, 2.4, 1, 13.94, 1, 1, 140, 627.3, 0.00847);

INSERT INTO LAB1_RANDOM_VARIANT (
  ID,
  VERSION,
  CITY,
  OUTSIDE_AIR_TEMPERATURE,
  STEAM_PRODUCTION_CAPACITY,
  OXYGEN_CONCENTRATION_POINT,
  STACK_EXIT_TEMPERATURE,
  FLUE_GAS_NOX_CONCENTRATION,
  FUEL_CONSUMER)
VALUES
  (0,
   0,
   'MOSCOW',
   10,
   30,
   5,
   130,
   180,
   2500);

insert into lab1team (id,user_id) values (
0, 0
);