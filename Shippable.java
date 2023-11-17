package edu.uc.cs3003.medava;

public interface Shippable {
  public MedicineSchedule getSchedule();
  public String getMedicineName();
  public boolean isTemperatureRangeAcceptable(Double lowTemperature, Double highTemperature);
}