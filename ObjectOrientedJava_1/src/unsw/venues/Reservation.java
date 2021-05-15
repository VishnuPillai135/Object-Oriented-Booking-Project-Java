package unsw.venues;

import java.time.LocalDate;
import java.lang.Object;

import java.util.ArrayList;
import java.util.List;

public class Reservation {

    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int smallRoomInt;
    private int mediumRoomInt;
    private int largeRoomInt;
    private ArrayList<Room> listRoom;

    /**
     * 
     * @param id id used for identification for reservation
     * @param startDate start date of reservation
     * @param endDate end date of reservation
     * @param smallRoomInt how many small rooms have been requested
     * @param mediumRoomInt how many medium rooms have been requested
     * @param largeRoomInt how many large rooms have been requested
     * @param listRoom rooms allocated to reservation
     * 
     */
    public Reservation (String id, LocalDate startDate, LocalDate endDate, int smallRoomInt, int mediumRoomInt, int largeRoomInt) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.smallRoomInt = smallRoomInt;
        this.mediumRoomInt = mediumRoomInt;
        this.largeRoomInt = largeRoomInt;
        listRoom = new ArrayList<Room>();
    }

    /**
     * 
     * getters and setters for Reservation class
     * 
     */
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getSmallRoomInt() {
        return smallRoomInt;
    }

    public void setSmallRooomInt(int smallRoomInt) {
        this.smallRoomInt = smallRoomInt;
    }

    public int getMediumRoomInt() {
        return mediumRoomInt;
    }

    public void setMediumRooomInt(int mediumRoomInt) {
        this.mediumRoomInt = mediumRoomInt;
    }

    public int getLargeRoomInt() {
        return largeRoomInt;
    }

    public void setLargeRooomInt(int largeRoomInt) {
        this.largeRoomInt = largeRoomInt;
    }

    public ArrayList<Room> getRoomList() {
		return listRoom;
    }
    
    public void setRoomList(Room room) {
        listRoom.add(room);
    }

}