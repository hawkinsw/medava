package edu.uc.cs3003.medava;

public class Thrombolytic extends Medicine {

    public Thrombolytic() {
        super("Thrombolytic");
    }

    @Override
    public double minimumTemperature() {
        System.out.println("Getting the minimum safe temperature for a Thrombolytic drug.");
        return 39.2;
    }

    @Override
    public double maximumTemperature() {
        System.out.println("Getting the maximum safe temperature for a Thrombolytic drug.");
        return 41.0;
    }

    @Override
    public MedicineSchedule getSchedule() {
      return MedicineSchedule.Uncontrolled;
    }
}
