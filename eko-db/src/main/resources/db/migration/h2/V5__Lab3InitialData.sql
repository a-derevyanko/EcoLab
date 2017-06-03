INSERT INTO lab3data (
  id,
  user_id,
  start_date,
  save_date,
  version,
  tpp_output,
  number_of_units,
  city,
  steam_production_capacity,
  number_of_stacks,
  stacks_height,
  stacks_diameter,
  wind_direction,
  wind_speed,
  low_heat_value,
  fuel_consumer,
  carbon_in_fly_ash,
  sulphur_content,
  ash_content,
  water_content,
  ash_recycling_factor,
  flue_gas_nox_concentration,
  stack_exit_temperature,
  outside_air_temperature,
  excess_air_ratio,
  combustion_product_volume,
  water_vapor_volume,
  air_volume,
  no2_background_concentration,
  no_background_concentration,
  so2_background_concentration,
  ash_background_concentration,
  sulphur_oxides_fraction_associated_by_fly_ash,
  sulphur_oxides_fraction_associated_in_wet_dust_collector,
  sulphur_oxides_fraction_associated_in_desulphurization_system,
  desulphurization_system_running_time,
  boiler_running_time,
  ash_proportion_entrained_gases,
  solid_particles_propotion_collected_in_ash,
  temperature_coefficient,
  terrain_coefficient,
  harmful_substances_deposition_coefficient,
  no2_mac,
  no_mac,
  so2_mac,
  ash_mac,
  breakdown_wind_speed,
  completed
) VALUES (
  0,
  0,
  '2017-06-01 23:07:49',
  '2017-06-01 23:07:53',
  0,
  1200,
  4,
  'MOSCOW',
  1050,
  2,
  150,
  4.2,
  'NORTH_WEST',
  4,
  39.73,
  72.12535,
  0,
  1.4,
  0.1,
  3,
  0,
  250,
  140,
  17,
  1.4,
  11.27486556,
  1.447565,
  10.44784,
  0.05,
  0.008,
  0.1,
  0.2,
  0.02,
  0,
  0,
  0,
  6000,
  1.0,
  NULL,
  160,
  1,
  NULL,
  NULL,
  NULL,
  NULL,
  NULL,
  NULL,
  FALSE
);

INSERT INTO lab3variant (
id,
version,
tpp_output,
number_of_units,
fuel_type,
city,
steam_production_capacity,
number_of_stacks,
stacks_height,
wind_direction,
wind_speed,
low_heat_value,
fuel_consumer,
carbon_in_fly_ash,
sulphur_content,
ash_content,
water_content,
ash_recycling_factor,
flue_gas_nox_concentration,
stack_exit_temperature,
outside_air_temperature,
excess_air_ratio,
combustion_product_volume,
water_vapor_volume,
air_volume,
no2_background_concentration,
no_background_concentration,
so2_background_concentration,
ash_background_concentration,
solid_particles_propotion_collected_in_ash
) VALUES (
0,
0,
1200,
4,
'SULFUR_OIL',
'MOSCOW',
1050,
2,
150,
'NORTH_WEST',
4,
39.73,
72.12535,
0,
1.4,
0.1,
3,
0,
250,
140,
17,
1.4,
11.27486556,
1.447565,
10.44784,
0.05,
0.008,
0.1,
0.2,
0
);