package edu.uc.cs3003.medava;

public abstract class Medicine implements Shippable {
    private String mMedicineName;

    public Medicine(String medicineName) {
        this.mMedicineName = medicineName;
    }

    public String getMedicineName() {
        return mMedicineName;
    }

    public double minimumTemperature() {
        return 0.0;
    }

    public double maximumTemperature() {
        return 100.0;
    }

    public boolean isTemperatureRangeAcceptable(Double lowTemperature, Double highTemperature) {
        return (minimumTemperature() <= lowTemperature && highTemperature <= maximumTemperature());
    }
    public abstract MedicineSchedule getSchedule();

}

