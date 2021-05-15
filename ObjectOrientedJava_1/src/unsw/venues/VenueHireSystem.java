/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {

    private List<Venue> venueList;

    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
        this.venueList = new ArrayList<Venue>();
    }

    /**
     * 
     * scans in commands from input and calls appropriate functions
     * 
     */
    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        case "cancel": 
            String id2 = json.getString("id");
            deleteReservation(id2);
            break;
        
        case "change":
            String id1 = json.getString("id");
            LocalDate start1 = LocalDate.parse(json.getString("start"));
            LocalDate end1 = LocalDate.parse(json.getString("end"));
            int small1 = json.getInt("small");
            int medium1 = json.getInt("medium");
            int large1 = json.getInt("large");

            JSONObject result1 = change(id1, start1, end1, small1, medium1, large1);
            
            System.out.println(result1.toString(2));
            break;

        case "list":
            String venue1 = json.getString("venue");
            printList(venue1);
            break;
        }

    }   

    /**
     * add a room to either a non-existant or an existing venue
     * @param venue name of the venue to which the room will be added 
     * @param room  name of the room
     * @param size  size of the room
     */
    private void addRoom(String venue, String room, String size) {
        int venueExists = 0;
        Room newRoom = new Room(room, size);
        //check to see if venue exists
        for (Venue v1 : venueList) {
            if (v1.getVenueName().equals(venue)) {
                venueExists = 1;
            }
        }
        //if doesn't exist, create venue & rooom
        if (venueExists == 0) {
            Venue newVenue = new Venue(venue);
            newVenue.setRoom(newRoom);
            venueList.add(newVenue);
        } else {
            //if does exist, add room
            for (Venue v2 : venueList) {
                if (v2.getVenueName().equals(venue)) {
                    v2.setRoom(newRoom);
                }
            }
        }
    }

    /**
     * delete a reservation
     * @param id id of the reservation
     */
    public void deleteReservation(String id) {
        //go through each venue
        for (Venue v1 : venueList) {
            //go through each room
            for (Room r1 : v1.getRoomList()) {
                //find reservation
                for (Reservation reservation1 : r1.getListReservation()) {
                    //if reservation found, delete it
                    if (id.equals(reservation1.getID())) {
                        r1.removeListReservation(reservation1);
                        break;
                    }
                }
            }
        }
    }

    /**
     * change the reservation
     * @param id id of the reservation to be changed
     * @param startDate new start date
     * @param endDate new enddate
     * @param small new request for small rooms
     * @param medium new request for medium rooms
     * @param large new request for large rooms
     * @return the JSON output
     */
    public JSONObject change(String id, LocalDate startDate, LocalDate endDate, int small, int medium, int large) {
        JSONObject result = new JSONObject();
        int flag = 0;
        //find the id
        for (Venue venue : venueList) {
            for (Room room : venue.getRoomList()) {
                for (Reservation reservation : room.getListReservation()) {
                    if (id.equals(reservation.getID())) {
                        //check to see if change is valid
                        //check to see if venue has enough rooms
                        if ((venue.numSmallRoom(startDate, endDate) >= small) && (venue.numMediumRoom(startDate, endDate) >= medium)
                            && venue.numLargeRoom(startDate, endDate) >= large) {
                            //delete original request
                            deleteReservation(id);
                            //create new request
                            JSONObject newRequest = request(id, startDate, endDate, small, medium, large);
                            if (newRequest.getString("status").equals("rejected")) {
                                result.put("status", "rejected");
                                return result;
                            } else {
                                result.put("status", "success");
                                result.put("venue", newRequest.getString("venue"));
                                result.put("rooms", newRequest.getJSONArray("rooms"));
                                return result;
                            }
                        }
                    }
                }
            }
        }
        //if change is not valid
        result.put("status", "rejected");
        return result;
    }

    /**
     * create a reservation request
     * @param id id of the request
     * @param start start date of the request
     * @param end end date of the request
     * @param small amount of small rooms for the request
     * @param medium amount of medium rooms for the request
     * @param large amount of large rooms for the request
     * @return the JSON output
     */
    public JSONObject request(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();
        int newSmall = small;
        int newMedium = medium;
        int newLarge = large;

        //check each venue for availability
        for (Venue v : venueList) {
            //if venue has enough rooms
            if ((newSmall <= v.numSmallRoom(start, end)) && (newMedium <= v.numMediumRoom(start, end))
                && (newLarge <= v.numLargeRoom(start, end))) {
                //create a reservation
                Reservation reservation = new Reservation(id, start, end, small, medium, large);
                int roomFound = 0;
                //iterate through rooms and add reservations
                for (Room room : v.getRoomList()) {
                    roomFound = 0;
                    //make sure room is available
                    if (room.checkRoomAvailable(start, end) == true) {

                        //if no more reservations to be made, exit loop
                        if ((newSmall == 0) && (newMedium == 0) && (newLarge == 0)) {
                            break;
                        }
                        if (room.getRoomSize().equals("small") && (newSmall > 0)) {
                            roomFound = 1;
                            newSmall--;
                        } else if (room.getRoomSize().equals("medium") && (newMedium > 0)) {
                            roomFound = 1;
                            newMedium--;
                        } else if (room.getRoomSize().equals("large") && (newLarge > 0)) {
                            roomFound = 1;
                            newLarge--;
                        } 

                        if (roomFound == 1) {
                            array.put(room.getRoomName());
                            room.setListReservation(reservation);
                        }
                    }
                }
                //print out result
                result.put("status", "success");
                result.put("venue", v.getVenueName());
                result.put("rooms", array);
                return result;
            }
        }
        
        //if venue not available
        result.put("status", "rejected");
        return result;
        
    }

    /**
     * print out a list of ordered reservations for a
     * specified venue
     * @param venueName venue that holds the rooms & reservations
     */
    public void printList (String venueName) {
        JSONArray list = new JSONArray();
        //iterate through venues to find venue
        for (Venue venue : venueList) {
            if (venue.getVenueName().equals(venueName)) {
                //for each room sort reservations
                for (Room room : venue.getRoomList()) {
                    //create JSON object for room
                    JSONObject listElement = new JSONObject();
                    JSONArray roomDetails = new JSONArray();
                    listElement.put("room", room.getRoomName());
                    //sort reservations
                    //code based from stack overflows
                    Collections.sort(room.getListReservation(), new Comparator<Reservation>() { 
                        public int compare (Reservation reservation1, Reservation reservation2) {
                            return reservation1.getStartDate().compareTo(reservation2.getEndDate());
                        }
                    });
                    //put all data types into a JSON object
                    for (Reservation r : room.getListReservation()) {
                        JSONObject reservationDetails = new JSONObject();
                        reservationDetails.put("id", r.getID());
                        reservationDetails.put("start", r.getStartDate());
                        reservationDetails.put("end", r.getEndDate());
                        roomDetails.put(reservationDetails);
                    } 
                    //add reservationDetails to overall list
                    listElement.put("reservations", roomDetails);
                    list.put(listElement);
                } 
            }    
        }
        System.out.println(list.toString(2));
    }
    

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
