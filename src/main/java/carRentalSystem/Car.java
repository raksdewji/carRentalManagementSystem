package carRentalSystem;

/**
 * Class to save all the car details
 * This class contains all the calculations and details of the car
 * It extends Vehicle so it has all the features of a vehicle
 */
public class Car extends Vehicle {
    private double rentRate = 78;
    private double lateFee;
    private int seats = 0;

    public Car(String VehicleId, int Year, String Make, String Model, int status, VehicleType vehicleType) {
        super(VehicleId, Year, Make, Model, status, vehicleType);
        seats = vehicleType.getCarSeats();
        if (seats == 7)
            rentRate = 113;
    }

    /**
     * Used to add either cars or vans to the list
     *
     * @param endDate,startDate accepts start date and end date
     * @return lateFee the late fee
     */
    public double getLateFee(DateTime endDate, DateTime startDate) {

        if (DateTime.diffDays(endDate, startDate) > 0)
            this.lateFee = 1.25 * this.rentRate * DateTime.diffDays(endDate, startDate);
        else
            this.lateFee = 0.0;
        return this.lateFee;
    }

    /**
     * Used to add either cars or vans to the list
     *
     * @param returnDate accepts the date as ro when it has to be returned
     * @return Returns true if returned else false with appropriate messages
     */
    public boolean returnVehicle(DateTime returnDate) {
        String vehicleType;
        if (this.Vehicle_id.contains("C_"))
            vehicleType = "car";
        else
            vehicleType = "van";

        if (this.vehicleStatus != 0) {
            DateTime estDate = this.records.get(this.getLastElementIndex()).getEstimatedReturnDate();
            DateTime rentDate = this.records.get(this.getLastElementIndex()).getRentDate();
            if (vehicleType.equals("car") && DateTime.diffDays(returnDate, estDate) < 0 && DateTime.diffDays(returnDate, rentDate) < this.vehicleType.canBeRentedForMinimumDays(rentDate, vehicleType)) {
                return false;
            } else {
                this.records.get(this.getLastElementIndex()).setData(returnDate, this.rentRate * DateTime.diffDays(returnDate, rentDate), this.getLateFee(returnDate, estDate));
                this.vehicleStatus = 0;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * sets the vehicle status to available after maintenance
     *
     * @return boolean Returns true if returned else false
     */
    public boolean completeMaintenance() {
        if (super.vehicleStatus != 2)
            return false;
        this.vehicleStatus = 0;
        return true;
    }


    /**
     * Method used to get details of car with their rental history
     * Prints the rental record of car
     */
    public String getDetails() {
        String details = super.getDetails();
        if (records.isEmpty()) {
            details += "\n RENTAL RECORD:\tempty";
        } else {
            details += "\n RENTAL RECORD\n";
            int count = 0;
            for (int index = 0; index < records.size(); index++)
                count++;
            for (int index = count - 1; index >= 0; index--) {
                details += this.records.get(index).getDetails() + "                     \n";
                for (int innerIndex = 0; innerIndex < 10; innerIndex++)
                    details += "-";
                details += "                     \n";
            }
        }
        return details;
    }

}