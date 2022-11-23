package com.sebsastian.service;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sebastiangetts
 */
class MyLiftTest {

    @Test
    void initial_floor_shall_be_0() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        assertEquals(0, elevator0.getCurrentFloor());
    }

    @Test
    void initial_target_floors_shall_be_empty() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        assertTrue(elevator0.getTargetFloors().isEmpty());
    }

    @Test
    void initial_floor_has_no_downward_call() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel1 = system.getFloorControlPanel(1);

        assertFalse(panel1.isDownPressed());
    }

    @Test
    void initial_floor_has_no_upward_call() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel1 = system.getFloorControlPanel(1);

        assertFalse(panel1.isUpPressed());
    }

    @Test
    void press_down_on_floor_0_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel0 = system.getFloorControlPanel(0);

        boolean success = panel0.pressDown();

        assertFalse(success);
    }

    @Test
    void press_down_on_floor_1_shall_return_true() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel1 = system.getFloorControlPanel(1);

        boolean success = panel1.pressDown();

        assertTrue(success);
    }

    @Test
    void check_downward_status_after_press_down_on_floor_1_shall_return_true() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel1 = system.getFloorControlPanel(1);

        panel1.pressDown();
        boolean pressed = panel1.isDownPressed();

        assertTrue(pressed);
    }

    @Test
    void press_down_2nd_time_on_floor_1_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel1 = system.getFloorControlPanel(1);

        panel1.pressDown();
        boolean success = panel1.pressDown();

        assertFalse(success);
    }

    @Test
    void press_up_on_floor_9_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel9 = system.getFloorControlPanel(9);

        boolean success = panel9.pressUp();

        assertFalse(success);
    }

    @Test
    void press_up_on_floor_8_shall_return_true() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel8 = system.getFloorControlPanel(8);

        boolean success = panel8.pressUp();

        assertTrue(success);
    }

    @Test
    void check_status_after_press_up_on_floor_8_shall_return_true() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel8 = system.getFloorControlPanel(8);

        panel8.pressUp();
        boolean pressed = panel8.isUpPressed();

        assertTrue(pressed);
    }

    @Test
    void press_up_2nd_time_on_floor_8_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.FloorControlPanel panel8 = system.getFloorControlPanel(8);

        panel8.pressUp();
        boolean success = panel8.pressUp();

        assertFalse(success);
    }

    @Test
    void add_negative_target_floor_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        boolean success = elevator0.addTargetFloor(-1);

        assertFalse(success);
    }

    @Test
    void add_target_floor_10_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        boolean success = elevator0.addTargetFloor(10);

        assertFalse(success);
    }

    @Test
    void add_target_floor_5_shall_return_true() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        boolean success = elevator0.addTargetFloor(5);

        assertTrue(success);
    }

    @Test
    void add_target_floor_5_when_elevator_on_floor_5_shall_return_false() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(5);
        elevator0.goToNext();
        boolean success = elevator0.addTargetFloor(5);

        assertFalse(success);
    }

    @Test
    void add_3_target_floors() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(3);
        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(3, targetFloors.size());
        assertTrue(targetFloors.contains(3));
        assertTrue(targetFloors.contains(5));
        assertTrue(targetFloors.contains(9));
    }

    @Test
    void proceed_1_time_after_add_3_target_floors() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(3);
        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(3, elevator0.getCurrentFloor());
        assertEquals(2, targetFloors.size());
        assertTrue(targetFloors.contains(5));
        assertTrue(targetFloors.contains(9));
    }

    @Test
    void proceed_2_time_after_add_3_target_floors() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(3);
        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);
        elevator0.goToNext();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(5, elevator0.getCurrentFloor());
        assertEquals(1, targetFloors.size());
        assertTrue(targetFloors.contains(9));
    }

    @Test
    void add_downward_target_on_upward_elevator() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);
        elevator0.goToNext();
        elevator0.addTargetFloor(3);

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(5, elevator0.getCurrentFloor());
        assertEquals(2, targetFloors.size());
        assertTrue(targetFloors.contains(3));
        assertTrue(targetFloors.contains(9));
    }

    @Test
    void proceed_downward_target_after_going_upward() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);
        elevator0.goToNext();
        elevator0.addTargetFloor(3);
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(9, elevator0.getCurrentFloor());
        assertEquals(1, targetFloors.size());
        assertTrue(targetFloors.contains(3));
    }

    @Test
    void finish_downward_target_after_going_upward() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);

        elevator0.addTargetFloor(5);
        elevator0.addTargetFloor(9);
        elevator0.goToNext();
        elevator0.addTargetFloor(3);
        elevator0.goToNext();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(3, elevator0.getCurrentFloor());
        assertEquals(0, targetFloors.size());
    }

    @Test
    void pickup_upward_call_from_5_with_elevator_on_floor_0() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        panel5.pressUp();
        elevator0.goToNext();

        assertEquals(5, elevator0.getCurrentFloor());
        assertFalse(panel5.isUpPressed());
    }

    @Test
    void pickup_downward_call_from_5_with_elevator_on_floor_7() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        elevator0.addTargetFloor(7);
        elevator0.goToNext();
        panel5.pressDown();
        elevator0.goToNext();

        assertEquals(5, elevator0.getCurrentFloor());
        assertFalse(panel5.isUpPressed());
    }

    @Test
    void pickup_upward_call_from_5_with_elevator_on_floor_0_going_to_7() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        elevator0.addTargetFloor(7);
        panel5.pressUp();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(5, elevator0.getCurrentFloor());
        assertEquals(1, targetFloors.size());
        assertTrue(targetFloors.contains(7));
        assertFalse(panel5.isUpPressed());
    }

    @Test
    void pickup_downward_call_from_5_with_elevator_on_floor_7_going_to_0() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        elevator0.addTargetFloor(7);
        elevator0.goToNext();
        elevator0.addTargetFloor(0);
        panel5.pressDown();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(5, elevator0.getCurrentFloor());
        assertEquals(1, targetFloors.size());
        assertTrue(targetFloors.contains(0));
        assertFalse(panel5.isDownPressed());
    }

    @Test
    void ignore_downward_call_from_5_with_elevator_on_floor_0_going_to_7() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        elevator0.addTargetFloor(7);
        panel5.pressDown();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(7, elevator0.getCurrentFloor());
        assertEquals(0, targetFloors.size());
        assertTrue(panel5.isDownPressed());
    }

    @Test
    void ignore_upward_call_from_5_with_elevator_on_floor_7_going_to_0() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 1);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);

        elevator0.addTargetFloor(7);
        elevator0.goToNext();
        elevator0.addTargetFloor(0);
        panel5.pressUp();
        elevator0.goToNext();

        Set<Integer> targetFloors = elevator0.getTargetFloors();
        assertEquals(0, elevator0.getCurrentFloor());
        assertEquals(0, targetFloors.size());
        assertTrue(panel5.isUpPressed());
    }

    @Test
    void pickup_1_upward_call_and_1_downward_call_with_2_idle_elevators_on_floor_0() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 2);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.Elevator elevator1 = system.getElevator(1);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);
        MyLift.FloorControlPanel panel7 = system.getFloorControlPanel(7);

        panel5.pressDown();
        panel7.pressUp();
        elevator0.goToNext();
        elevator1.goToNext();

        Set<Integer> currentFloors = new HashSet<>();
        currentFloors.add(elevator0.getCurrentFloor());
        currentFloors.add(elevator1.getCurrentFloor());
        assertTrue(currentFloors.contains(5));
        assertTrue(currentFloors.contains(7));
        assertFalse(panel5.isDownPressed());
        assertFalse(panel7.isUpPressed());
    }

    @Test
    void pickup_1_upward_call_and_1_downward_call_with_2_idle_elevators_on_floor_9() {
        MyLift.ElevatorSystem system = new MyLift.ElevatorSystem(10, 2);
        MyLift.Elevator elevator0 = system.getElevator(0);
        MyLift.Elevator elevator1 = system.getElevator(1);
        MyLift.FloorControlPanel panel5 = system.getFloorControlPanel(5);
        MyLift.FloorControlPanel panel7 = system.getFloorControlPanel(7);

        elevator0.addTargetFloor(9);
        elevator0.goToNext();
        elevator1.addTargetFloor(9);
        elevator1.goToNext();
        panel5.pressDown();
        panel7.pressUp();
        elevator0.goToNext();
        elevator1.goToNext();

        Set<Integer> currentFloors = new HashSet<>();
        currentFloors.add(elevator0.getCurrentFloor());
        currentFloors.add(elevator1.getCurrentFloor());
        assertTrue(currentFloors.contains(5));
        assertTrue(currentFloors.contains(7));
        assertFalse(panel5.isDownPressed());
        assertFalse(panel7.isUpPressed());
    }
}
