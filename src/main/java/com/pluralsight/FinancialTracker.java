package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) throws IOException {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     *
     * @return
     */
    public static void loadTransactions(String FILE_NAME) {
        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.


        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(FILE_NAME));

            String line;
            while ((line = reader.readLine()) != null) {

                String[] newtransaction = line.split("\\|");

                LocalDate date = LocalDate.parse(newtransaction[0]);
                LocalTime time = LocalTime.parse(newtransaction[1]);
                String description = newtransaction[2];
                String vendor = newtransaction[3];
                double amount = Double.parseDouble(newtransaction[4]);

                transactions.add(new Transaction(date, time, description, vendor, amount));

            }
            reader.close();
        } catch (IOException e) {


        }

    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    private static void addDeposit(Scanner scanner) {

        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        try {

            System.out.println("Enter the date this formate (yyyy-MM-dd)");
            date = LocalDate.parse(scanner.nextLine(), DATE_FMT);
            System.out.println("Enter the time this formate (HH:mm:ss)");
            time = LocalTime.parse(scanner.nextLine(), TIME_FMT);
            System.out.println("Enter the description ");
            description = scanner.nextLine();
            System.out.println("enter the vendor name");
            vendor = scanner.nextLine();



        } catch (DateTimeException e) {
            System.out.println("Show me error");
        }

        try {
            System.out.println("Enter the deposit amount");
            amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("amount should be positive");
                return;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            String line = "";

            transactions.add(new Transaction(date, time, description, vendor, amount));
            writer.write(date+ "|" + time+ "|" + description+ "|" +vendor + "|"+ amount);

            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Run time error");



        }
        }


        // TODO


    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {
        // TODO

        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        try {
            System.out.println("Enter the date this formate (yyyy-MM-dd)");
            date = LocalDate.parse(scanner.nextLine(), DATE_FMT);

            System.out.println("Enter the time this formate (HH:mm:ss)");
            time = LocalTime.parse(scanner.nextLine(), TIME_FMT);
            System.out.println("Enter the description ");
            description = scanner.nextLine();
            System.out.println("enter the vendor name");
            vendor = scanner.nextLine();

        } catch (DateTimeException e) {
            System.out.println("Show me error");
        }

        try {
            System.out.println("Enter the payment amount");
            amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("amount should be positive");
                return;
            }
            amount = -amount;

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            String line = "";

            transactions.add(new Transaction(date, time, description, vendor, amount));
            writer.write(date+ "|" + time+ "|" + description+ "|" +vendor + "|"+ amount);

            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Run time error");
        }


    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() { /* TODO – print all transactions in column format */
        System.out.printf("all transactions\n");
        System.out.printf("date        | time     | description        | vendor       | amount\n");
        System.out.printf("--------------------------------------------------------------------\n");

        for (Transaction transaction : transactions) {
            System.out.println(transaction);

        }
    }

    private static void displayDeposits() { /* TODO – only amount > 0    */

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0){
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() { /* TODO – only amount < 0               */

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);

            }
        }
    }


    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    LocalDate start = LocalDate.now();
                    LocalDate end = LocalDate.now();
                    filterTransactionsByDate(start.withDayOfMonth(1), end);
                }

                case "2" -> {/* TODO – previous month report */ }
                case "3" -> {/* TODO – year-to-date report   */ }
                case "4" -> {/* TODO – previous year report  */ }
                case "5" -> {/* TODO – prompt for vendor then report */ }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range

        for (Transaction transaction : transactions) {
            if (transaction.getDate().isBefore(end) && transaction.getDate().isAfter(start)) {
                System.out.println(transaction);

            }

        }

    }
    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor

        boolean found = false;
        for (Transaction allVendor : transactions) {
            if (allVendor.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(allVendor.getVendor());
                found = true;
                if (found) {
                    System.out.println("No transactions found for vendor: " + vendor);

                }

            }

        }
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches

        System.out.println("Enter a start date of the month and date this formate (yyyy-MM-dd)");
        LocalDate startDateInput = (LocalDate.parse(scanner.nextLine(), DATE_FMT));

        LocalDate startDate = null;
        if (!startDate.isBlank()) {
            startDate = LocalDate.parse(startDate, DATE_FMT);

            System.out.println("Enter the time this formate (HH:mm:ss)");
            time = LocalTime.parse(scanner.nextLine(), TIME_FMT);
            System.out.println("Enter the description ");
            description = scanner.nextLine();
            System.out.println("enter the vendor name");
            vendor = scanner.nextLine();

        }
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        /* TODO – return LocalDate or null */
        return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
}
