package edu.uc.cs3003.medava;

public class HospitalRunner {
    public static void run() {
        Transporter priorityDispatch = new Transporter("Priority Dispatch", 40.0, 41.0);

        Pharmacy cvs = new Pharmacy("CVS at 7500 Beechmont Avenue");

        cvs.send(priorityDispatch);

        Hospital uc = new Hospital("World Famous University of Cincinnati Children's Hospital");

        uc.receive(priorityDispatch);
    }
}
