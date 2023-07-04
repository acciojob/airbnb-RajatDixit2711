package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {
    Map<String, Hotel> hotelMap = new HashMap<>();
    Map<Integer, User> userMap = new HashMap<>();
    Map<String, Booking> bookMap = new HashMap<>();

    public String addHotel(Hotel hotel) {
        String name = hotel.getHotelName();
        if (name.equals(null) || hotel == null) {
            return "FAILURE";
        }
        for (String pname : hotelMap.keySet()) {
            if (pname.equals(name)) {
                return "FAILURE";
            }
        }
        hotelMap.put(name, hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        int adNumer = user.getaadharCardNo();
        userMap.put(adNumer, user);
        return adNumer;


    }

    public String getHotelWithMostFacilities() {
        int max = 0;
        List<String> l = new ArrayList<>();
        for (String name : hotelMap.keySet()) {
            Hotel hotel = hotelMap.get(name);
            List l1 = hotel.getFacilities();
            int num = l1.size();
            if (max < num) {
                max = num;
            }
        }
        for (String name : hotelMap.keySet()) {
            Hotel hotel = hotelMap.get(name);
            List l2 = hotel.getFacilities();
            if (l2.size() == max) {
                l.add(hotel.getHotelName());
            }
        }
        Collections.sort(l);
        if (max == 0) {
            return "";
        } else {
            return l.get(0);
        }

    }

    public int bookARoom(Booking booking) {
        String hotelName = booking.getHotelName();
        if (!hotelMap.containsKey(hotelName))return -1;
        if (hotelMap.get(hotelName).getAvailableRooms() >= booking.getNoOfRooms()) {
            Hotel hotel = hotelMap.get(hotelName);
            int totalRoomAvilable = hotel.getAvailableRooms();
            totalRoomAvilable -= booking.getNoOfRooms();
            hotel.setAvailableRooms(totalRoomAvilable);
            hotelMap.put(hotelName, hotel);
            String bookingId = UUID.randomUUID() + "";
            System.out.println(bookingId + "bookingId");
            int amountTobePaid = hotel.getPricePerNight() * booking.getNoOfRooms();
            bookMap.put(bookingId, booking);
           // userRent.put(bookingId, amountTobePaid);
            System.out.println(amountTobePaid + "Amount To Paid");
            return amountTobePaid;
        }
        return -1;
    }
    public int getBookings(Integer aadharCard) {
        int cnt = 0;
        for (String key : bookMap.keySet()) {
            if (aadharCard.equals(bookMap.get(key).getBookingAadharCard()))cnt++;
        }
        return cnt;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if (!hotelMap.containsKey(hotelName))return null;
        Hotel hotel = hotelMap.get(hotelName);
        List<Facility> facilities = hotel.getFacilities();
        for (int i = 0; i < newFacilities.size(); i++) {
            if (!facilities.contains(newFacilities.get(i)))facilities.add(newFacilities.get(i));
        }
        hotel.setFacilities(facilities);
        hotelMap.put(hotelName, hotel);
        return hotel;
    }
}
