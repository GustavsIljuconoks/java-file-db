// 241RDB246 Gustavs Iļjučonoks 9.grupa

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


class Utils {

    static String VEHICLE_BUS = "BUS";
    static String VEHICLE_TRAIN = "TRAIN";
    static String VEHICLE_PLANE = "PLANE";
    static String VEHICLE_BOAT = "BOAT";

    static String formatCity(String city) {
        String[] words = city.trim().toLowerCase().split("\\s+");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            formatted.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)).append(" ");
        }
        return formatted.toString().trim();
    }

    static boolean validateDate(String date) {
        Pattern pattern = Pattern.compile("^(\\d{2})/(\\d{2})/(\\d{4})$");
        Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) return false;

        int day = parseInt(matcher.group(1));
        int month = parseInt(matcher.group(2));

        return day >= 1 && day <= 31 && month >= 1 && month <= 12;
    }

    static boolean validateVehicle(String vehicle) {
        return vehicle.equals(VEHICLE_BUS) || vehicle.equals(VEHICLE_TRAIN) ||
                vehicle.equals(VEHICLE_PLANE) || vehicle.equals(VEHICLE_BOAT);
    }
}

final class Travel {
    int id;
    String city;
    String date;
    int days;
    double price;
    String vehicle;

    public Travel(int id, String city, String date, int days, double price, String vehicle) {
        this.id = id;
        this.city = city;
        this.date = date;
        this.days = days;
        this.price = price;
        this.vehicle = vehicle;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}

public final class Main {
    static Scanner sc = new Scanner(System.in);
    static List<Travel> travelDatabase = new ArrayList<>();

    public static void main(String[] args) {
        loadDatabase("src/lab13/db.csv");
        String command;

        loop: while (true) {
            command = sc.nextLine().trim();

            if (command.startsWith("add")) {
                handleAdd(command);
            } else if (command.startsWith("edit")) {
                handleEdit(command);
            } else if (command.startsWith("delete")) {
                handleDelete(command);
            } else if (command.startsWith("list")) {
                handleList(command);
            } else if (command.startsWith("sort")) {
                // TODO: Implement sorting
            } else if (command.startsWith("find")) {
                // TODO: Implement searching
            } else if (command.startsWith("avg")) {
                // TODO: Implement average
            } else if (command.startsWith("exit")) {
                break loop;
            } else {
                System.out.println("Invalid command");
            }
        }
        sc.close();
    }

    static void handleAdd(String command)
    {
        String[] parts = command.split(" ", 2);
        if (parts.length < 2) {
            System.out.println("wrong format");
            return;
        }

        String[] fields = parts[1].split(";");
        if (fields.length != 6) {
            System.out.println("wrong field count");
            return;
        }

        String idStr = fields[0];
        String city = Utils.formatCity(fields[1]);
        String date = fields[2];
        String dayCountStr = fields[3];
        String priceStr = fields[4];
        String vehicle = fields[5].toUpperCase();

        if (idStr.length() != 3) {
            System.out.println("wrong id");
            return;
        }

        int id;
        try {
            id = parseInt(idStr);
        } catch (NumberFormatException e) {
            System.out.println("wrong id");
            return;
        }

        for (Travel travel : travelDatabase) {
            if (travel.id == id) {
                System.out.println("wrong id");
                return;
            }
        }

        if (!Utils.validateDate(date)) {
            System.out.println("wrong date");
            return;
        }

        int days;
        try {
            days = parseInt(dayCountStr);
        } catch (NumberFormatException e) {
            System.out.println("wrong day count");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            System.out.println("wrong price");
            return;
        }

        if (!Utils.validateVehicle(vehicle)) {
            System.out.println("wrong vehicle");
            return;
        }

        travelDatabase.add(new Travel(id, city, date, days, price, vehicle));
        System.out.println("added");
    }

    // TODO: Implement editing
    public static void handleEdit(String command) { }

    // TODO: Implement delete
    public static void handleDelete(String command)
    {

    }

    public static void handleList(String command)
    {
        System.out.println("\n------------------------------------------------------------");
        System.out.printf("%-4s%-21s%-11s%6s%10s%8s", "Id", "City", "Date", "Days", "Price", "Vehicle");
        System.out.println("\n------------------------------------------------------------");

        for (Travel travel : travelDatabase) {
            System.out.printf("%-4d%-21s%-11s%6d%10.2f%8s\n", travel.id, travel.city, travel.date, travel.days, travel.price, travel.vehicle);
        }

        System.out.println("\n------------------------------------------------------------");
    }

    public static void loadDatabase(String fileName)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                int id = parseInt(fields[0]);
                String city = fields[1];
                String date = fields[2];
                int days = parseInt(fields[3]);
                double price = Double.parseDouble(fields[4]);
                String vehicle = fields[5];
                travelDatabase.add(new Travel(id, city, date, days, price, vehicle));
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}
