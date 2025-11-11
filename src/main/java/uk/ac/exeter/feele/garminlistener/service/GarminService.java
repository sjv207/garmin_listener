package uk.ac.exeter.feele.garminlistener.service;

public interface GarminService {

    /**
     * Import data from Garmin. It doesn't return anything as there is no need to confirm status to Garmin.
     * @param data
     */
    void importData(String data);
}
