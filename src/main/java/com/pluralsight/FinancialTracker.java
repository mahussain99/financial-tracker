package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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

    public static void main(String[] args) {
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

    public static void loadTransactions(String FILE_NAME) {

        try {
            File newFile = new File(FILE_NAME);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
        } catch (IOException ex) {
            System.out.println("Show me run time error");
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] newTransaction = line.split("\\|");
                LocalDate date = LocalDate.parse(newTransaction[0], DATE_FMT);
                LocalTime time = LocalTime.parse(newTransaction[1], TIME_FMT);
                String description = newTransaction[2];
                String vendor = newTransaction[3];
                double amount = Double.parseDouble(newTransaction[4]);
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Show me run time error");
        }
    }

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
            System.out.println("Enter the vendor name");
            vendor = scanner.nextLine();


        } catch (DateTimeException e) {
            System.out.println("Show me error");
        }

        try {
            System.out.println("Enter the deposit amount");
            amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("Amount should be positive");
                return;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            String line = "";

            transactions.add(new Transaction(date, time, description, vendor, amount));
            writer.write(date.format(DATE_FMT) + "|" + time.format(TIME_FMT) + "|" + description + "|" + vendor + "|" + amount);

            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Run time error");

        }
    }

    private static void addPayment(Scanner scanner) {

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
            System.out.println("Enter the vendor name");
            vendor = scanner.nextLine();

        } catch (DateTimeException e) {
            System.out.println("Show me error");
        }

        try {
            System.out.println("Enter the payment amount");
            amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("Amount should be positive");
                return;
            }
            amount = -amount;

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            String line = "";

            transactions.add(new Transaction(date, time, description, vendor, amount));
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);

            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Run time error");
        }

    }

    private static void ledgerMenu(Scanner scanner) {
        transactions.sort(Comparator.comparing(Transaction::getDate, Comparator.reverseOrder()));
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

    private static void displayLedger() {


        try {
            System.out.println("All transactions");
            System.out.format("%-16s| %-12s | %-34s | %-26s | %s\n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("-------------------------------------------------------------------------------------------------------\n");

            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        } catch (Exception ex) {
            System.out.println("Show me error");
        }
    }

    private static void displayDeposits() {
        try {
            System.out.format("%-16s| %-12s | %-34s | %-26s | %s\n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("-------------------------------------------------------------------------------------------------------\n");

            for (Transaction transaction : transactions) {
                if (transaction.getAmount() > 0) {
                    System.out.println(transaction);
                }
            }
        } catch (Exception e) {
            System.out.println("Run tim error");
        }
    }

    private static void displayPayments() {
        System.out.format("%-16s| %-12s | %-34s | %-26s | %s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------\n");

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);

            }
        }
    }

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
                    System.out.println("Show us current month report " + start + " to " + end);
                }
                case "2" -> {/* TODO – previous month report */
                    LocalDate today = LocalDate.now();
                    LocalDate previousMonth = today.minusMonths(1);
                    LocalDate start = previousMonth.withDayOfMonth(1);
                    LocalDate end = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());
                    filterTransactionsByDate(start, end);

                }
                case "3" -> {/* TODO – year-to-date report   */
                    LocalDate today = LocalDate.now();
                    LocalDate start = today.withDayOfYear(1);
                    LocalDate end = today;
                    filterTransactionsByDate(start, end);
                    System.out.println("Showing transactions from " + start + " to " + end);

                }

                case "4" -> {/* TODO – previous year report  */
                    LocalDate today = LocalDate.now();
                    LocalDate previousYear = today.minusYears(1);
                    LocalDate start = previousYear.withDayOfYear(1);
                    LocalDate end = previousYear.withDayOfYear(previousYear.lengthOfYear());
                    filterTransactionsByDate(start, end);

                }
                case "5" -> {/* TODO – prompt for vendor then report */
                    System.out.println("Vendor name");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                }
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
        System.out.format("%-16s| %-12s | %-34s | %-26s | %s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------\n");

        for (Transaction transaction : transactions) {
            if (!transaction.getDate().isBefore(start) && !transaction.getDate().isAfter(end)) {
                System.out.println(transaction);


            }

        }

    }

    private static void filterTransactionsByVendor(String vendor) {
        System.out.format("%-16s| %-12s | %-34s | %-26s | %s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------\n");

        boolean found = false;
        for (Transaction allVendor : transactions) {
            if (allVendor.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(allVendor);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }

    private static void customSearch(Scanner scanner) {

        try {
            System.out.println("Enter the date any formate");
            String startDateInput = scanner.nextLine();
            LocalDate startDate = startDateInput.isBlank() ? null : LocalDate.parse(startDateInput, DATE_FMT);

            System.out.println("Enter any end date formate");
            String endDateInput = scanner.nextLine();
            LocalDate endNewDate = endDateInput.isBlank() ? null : LocalDate.parse(endDateInput, DATE_FMT);

            System.out.println("Enter the time any formate");
            String enterTime = scanner.nextLine();
            LocalTime newTime = enterTime.isBlank() ? null : LocalTime.parse(enterTime, TIME_FMT);

            System.out.println("Enter the anything about description ");
            String description = scanner.nextLine().toLowerCase();
            System.out.println("Enter the any vendor name");
            String vendor = scanner.nextLine().toLowerCase();

            System.out.println("Enter any amount");
            String amountInput = scanner.nextLine();

            Double exactAmount = null;
            if (!amountInput.isBlank())
                exactAmount = Double.parseDouble(amountInput);

            for (Transaction allFilterTransaction : transactions) {
                if (startDate != null && allFilterTransaction.getDate().isBefore(startDate))
                    continue;
                if (endNewDate != null && allFilterTransaction.getDate().isAfter(endNewDate))
                    continue;
                if (newTime != null && allFilterTransaction.getTime().isBefore(newTime))
                    continue;
                if (!description.isBlank() && !allFilterTransaction.getDescription().toLowerCase().contains(description.toLowerCase()))
                    continue;
                if (!vendor.isBlank() && !allFilterTransaction.getVendor().toLowerCase().contains(vendor.toLowerCase()))
                    continue;
                if (amountInput != null && allFilterTransaction.getAmount() != exactAmount)
                    continue;
                System.out.println(allFilterTransaction);
            }
        } catch (Exception ex) {
            System.out.println("Show me runtime error");
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
