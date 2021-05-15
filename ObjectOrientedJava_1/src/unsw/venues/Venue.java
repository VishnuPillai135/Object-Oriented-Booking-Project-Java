package unsw.venues;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Venue {

        private String venueName;
        private ArrayList<Room> roomList;
        
        /**
         * 
         * @param venueName name of venue
         * @param roomList list of rooms that are in each venue
         * 
         */
        public Venue(String venueName) {
            this.venueName = venueName;
            roomList = new ArrayList<Room>();
        }

        /**
         * 
         * getters and setters for Venue class
         * 
         */
        public String getVenueName() {
            return venueName;
        }

        public void setVenueName(String venueName) {
            this.venueName = venueName;
        }
        
        public ArrayList<Room> getRoomList() {
            return roomList;
        }

        public void setRoom(Room room) {
            roomList.add(room);
        }

        /**
         * 
         * @param start start date for reservation
         * @param end end date for reservation
         * @return returns the amount of small rooms avaialble
         */
        public int numSmallRoom(LocalDate start, LocalDate end) {
            int count = 0;
            for (Room r : this.roomList) {
                if (r.getRoomSize().equals("small")) {
                    //checks to see if its available
                    if (r.checkRoomAvailable(start, end)) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * 
         * @param start start date for reservation
         * @param end end date for reservation
         * @return returns the amount of medium rooms avaialble
         */
        public int numMediumRoom(LocalDate start, LocalDate end) {
            int count = 0;
            for (Room r : this.roomList) {
                if (r.getRoomSize().equals("medium")) {
                    //checks to see if its available
                    if (r.checkRoomAvailable(start, end)) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * 
         * @param start start date for reservation
         * @param end end date for reservation
         * @return returns the amount of large rooms avaialble
         */
        public int numLargeRoom(LocalDate start, LocalDate end) {
            int count = 0;
            for (Room r : this.roomList) {
                if (r.getRoomSize().equals("large")) {
                    //checks to see if its available
                    if (r.checkRoomAvailable(start, end)) {
                        count++;
                    }
                }
            }
            return count;
        }
}