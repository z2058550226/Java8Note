package com.suikajy.java8note.ex1_thread_review.part8_simulation;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Car {
    private final int id;
    private boolean engine = false,
            driveTrain = false, wheel = false;

    public Car(int id) {
        this.id = id;
    }

    public Car() {
        id = -1;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void addEngine() {
        engine = true;
    }

    public synchronized void addDriveTrain() {
        driveTrain = true;
    }

    public synchronized void addWheels() {
        wheel = true;
    }

    @Override
    public synchronized String toString() {
        return "Car " + id + " {" + " engine: " + engine
                + " driveTrain: " + driveTrain
                + " wheels: " + wheel + " ]";
    }
}

class CarQueue extends LinkedBlockingQueue<Car> {
}

class ChassisBuilder implements Runnable {

    private CarQueue carQueue;
    private int counter = 0;

    public ChassisBuilder(CarQueue carQueue) {
        this.carQueue = carQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(500);
                // Make chassis:
                Car c = new Car(counter++);
                System.out.println("ChassisBuilder created " + c);
                // Insert into queue
                carQueue.put(c);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted: ChassisBuilder");
        }
        System.out.println("ChassisBuilder off");
    }
}

public class CarBuilder {
}
