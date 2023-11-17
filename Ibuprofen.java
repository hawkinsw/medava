package edu.uc.cs3003.medava;

public class Ibuprofen extends Medicine {

    public Ibuprofen() {
        super("Ibuprofen");
    }

    @Override
    public MedicineSchedule getSchedule() {
        return MedicineSchedule.Uncontrolled;
    }

    @Override
    public double minimumTemperature() {
        return 30.0;
    }

    @Override
    public double maximumTemperature() {
        return 90.0;
    }
}