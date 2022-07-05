package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {

    private Double pricePerHour;
    private Double pricePerDay;

    private TaxService taxService;

    public RentalService(){
    }

    public RentalService(Double pricePerHour, Double pricePerDay, BrazilTaxService taxService) {
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
        this.taxService = taxService;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void processInvoice(CarRental carRental) {
        long t1 = carRental.getStart().getTime(); // .getTime() pega o tempo em milliseconds
        long t2 = carRental.getFinish().getTime();
        double hours = (double) (t2 - t1) / 1000 / 60 / 60; // (milliseconds) / seconds / minutes / hours / ...

        double basicPayment = 0.0;

        if (hours <= 12) {
            basicPayment = getPricePerHour() * Math.ceil(hours);
        }
        else {
            basicPayment = getPricePerDay() * Math.ceil(hours / 24); // hours / 24 -> days
        }

        double tax = taxService.tax(basicPayment);

        carRental.setInvoice(new Invoice(basicPayment, tax));
    }
}
