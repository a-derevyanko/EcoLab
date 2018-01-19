INSERT INTO LAB1DATA (
  ID,
  USER_ID,
  START_DATE,
  SAVE_DATE,
  VERSION,
  COMPLETED,
  STACKS_HEIGHT,
  STACKS_DIAMETER,
  OUTSIDE_AIR_TEMPERATURE,
  STEAM_PRODUCTION_CAPACITY,
  OXYGEN_CONCENTRATION_POINT,
  FUEL_CONSUMER_NORMALIZED,
  STACK_EXIT_TEMPERATURE,
  FLUE_GAS_NOX_CONCENTRATION,
  EXCESS_AIR_RATIO,
  FLUE_GAS_NOX_CONCENTRATION_NC,
  EXCESS_OF_NORMS,
  FLUE_GASES_RATE, DRY_GASES_FLOW_RATE,
  MASS_EMISSIONS,
  FLUE_GASES_SPEED,
  F,
  M,
  U,
  N,
  D,
  HARMFUL_SUBSTANCES_DEPOSITION_COEFFICIENT,
  TERRAIN_COEFFICIENT,
  TEMPERATURE_COEFFICIENT,
  DISTANCE_FROM_EMISSION_SOURCE,
  MAXIMUM_SURFACE_CONCENTRATION)
VALUES
  (0,
    0,
    '2017-11-22 23:21:48.164000000',
    '2017-11-22 23:58:31.662000000',
    NULL,
    FALSE,
    40,
    1,
    10,
    30,
    5,
    2500,
    130,
    180,
    1.31,
    168.4,
    TRUE,
    15.18,
    8.18,
    1.47,
    19.34,
    1.95,
    0.81,
    2.32,
    1,
    14.39,
    1,
    1,
    140,
    575.6,
    10.748);

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