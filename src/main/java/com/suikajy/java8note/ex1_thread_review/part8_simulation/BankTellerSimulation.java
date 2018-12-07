package com.suikajy.java8note.ex1_thread_review.part8_simulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 银行出纳员仿真（就是模拟银行出纳员处理排队中的顾客的demo，翻译不接地气）
 *
 * 这里PriorityQueue是一个优先级队列，它的元素会按照元素实现的compareTo方法来排序
 */

// Read-only objects don't require synchronization
class BankCustomer {
    private final int serviceTime;

    public BankCustomer(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        return "[" + serviceTime + "]";
    }
}

// Teach the customer line to display itself
class CustomerLine extends ArrayBlockingQueue<BankCustomer> {

    public CustomerLine(int maxLineSize) {
        super(maxLineSize);
    }

    @Override
    public String toString() {
        if (this.size() == 0) {
            return "[Empty]";
        }
        StringBuilder result = new StringBuilder();
        for (BankCustomer customer : this) {
            result.append(customer);
        }
        return result.toString();
    }
}

// Randomly add customers to a queue:
class CustomerGenerator implements Runnable {

    private CustomerLine customers;
    private static Random rand = new Random(47);

    public CustomerGenerator(CustomerLine customers) {
        this.customers = customers;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
                customers.put(new BankCustomer(rand.nextInt(1000)));
            }
        } catch (InterruptedException e) {
            System.out.println("CustomerGenerator interrupted");
        }
        System.out.println("CustomerGenerator terminating");
    }
}

class Teller implements Runnable, Comparable<Teller> {

    private static int counter = 0;
    private final int id = counter++;
    // Customers served during this shift:
    private int customersServed = 0;
    private CustomerLine customers;
    private boolean servingCustomerLine = true;

    public Teller(CustomerLine customers) {
        this.customers = customers;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                BankCustomer customer = customers.take();
                TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
                synchronized (this) {
                    customersServed++;
                    while (!servingCustomerLine) wait();
                }
            }
        } catch (InterruptedException e) {
            System.out.println(this + "interrupted");
        }
        System.out.println(this + "terminating");
    }

    public synchronized void doSomethingElse() {
        customersServed = 0;
        servingCustomerLine = false;
    }

    public synchronized void serveCustomerLine() {
        assert !servingCustomerLine : "already serving: " + this;
        servingCustomerLine = true;
        notifyAll();
    }

    @Override
    public String toString() {
        return "Teller " + id + " ";
    }

    public String shortString() {
        return "T" + id;
    }

    // Used by priority queue:
    @Override
    public synchronized int compareTo(Teller other) {
        return Integer.compare(customersServed, other.customersServed);
    }
}

class TellerManager implements Runnable {
    private ExecutorService exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<>();
    private int adjustmentPeriod;
    private static Random rand = new Random(47);

    public TellerManager(ExecutorService exec, CustomerLine customers, int adjustmentPeriod) {
        this.exec = exec;
        this.customers = customers;
        this.adjustmentPeriod = adjustmentPeriod;
        // Start with a single teller:
        Teller teller = new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
    }

    public void adjustTellerNumber() {
        // This is actually a control system. By adjusting
        // the numbers, you can reveal stability issues in
        // the control mechanism.
        // If line is too long. add another teller:
        if (customers.size() / workingTellers.size() > 2) {
            // If tellers are on break or doing
            // another job, bring one back:
            if (tellersDoingOtherThings.size() > 0) {
                Teller teller = tellersDoingOtherThings.remove();
                teller.serveCustomerLine();
                workingTellers.offer(teller);
                return;
            }
            // Else create (hire) a new teller
            Teller teller = new Teller(customers);
            exec.execute(teller);
            workingTellers.offer(teller);
            return;
        }
        // If line is short enough, remove a teller;
        if (workingTellers.size() > 1 &&
                customers.size() / workingTellers.size() < 2) {
            reassignOneTeller();
        }
        // If there is no line, we only need one teller:
        if (customers.size() == 0)
            while (workingTellers.size() > 1)
                reassignOneTeller();
    }

    // Give a teller a different job or a break:
    private void reassignOneTeller() {
        Teller teller = workingTellers.poll();
        teller.doSomethingElse();
        tellersDoingOtherThings.offer(teller);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber();
                System.out.print(customers + " { ");
                for (Teller teller : workingTellers) {
                    System.out.print(teller.shortString() + " ");
                }
                System.out.println(" } ");
            }
        } catch (InterruptedException e) {
            System.out.println(this + "interrupted");
        }
        System.out.println(this + "terminating");
    }

    @Override
    public String toString() {
        return "TellerManager ";
    }
}

public class BankTellerSimulation {

    static final int MAX_LINE_SIZE = 50;
    static final int ADJUSTMENT_PERIOD = 1000;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        // If line is too long, customers will leave:
        CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customers));
        // Manager will add and remove tellers as necessary
        exec.execute(new TellerManager(exec, customers, ADJUSTMENT_PERIOD));
        TimeUnit.MILLISECONDS.sleep(5000);
//        System.out.println("Press 'Enter' to quit");
        exec.shutdownNow();
    }
}
