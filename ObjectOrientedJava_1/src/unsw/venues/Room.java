package unsw.venues;

import java.time.LocalDate;

import java.util.List;
import java.util.ArrayList;

public class Room {
    private String roomName;
    private String roomSize;
    private ArrayList<Reservation> listReservation;

    /**
     * 
     * @param roomName name of room
     * @param roomSize size of room, either small, medium or large
     * @param listReservation list of reservations for room
     */
    public Room (String roomName, String roomSize) {
        this.roomName = roomName;
        this.roomSize = roomSize;
        listReservation = new ArrayList<Reservation>();
    }

    /**
     * 
     * getters and setters for Room class
     * 
     */

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public ArrayList<Reservation> getListReservation() {
		return listReservation;
    }

    public void setListReservation(Reservation reservation) {
		listReservation.add(reservation);
    }

    public void removeListReservation(Reservation reservation) {
        listReservation.remove(reservation);
    }

    /**
     * 
     * @param start start date of reservation
     * @param end enddate of reservation
     * @return either the room is available for reservation or its oocupied
     */
    public boolean checkRoomAvailable(LocalDate start, LocalDate end) {
        //go through each reservation already booked for the room
        //if true, then room is available
        //if false, then room is not available
        for (Reservation r : this.listReservation) {
            if ((start.compareTo(r.getStartDate()) >= 0) && (start.compareTo(r.getEndDate()) <= 0)) {
                return false;
            }
            if ((end.compareTo(r.getStartDate()) >= 0) && (end.compareTo(r.getEndDate()) <= 0)) {
                return false;
            }
            if ((start.compareTo(r.getStartDate()) <= 0) && (end.compareTo(r.getEndDate()) >= 0)) {
                return false;
            }
        }
        return true;
    }
    
}