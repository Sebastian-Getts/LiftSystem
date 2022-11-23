package com.sebsastian.service;

import java.util.*;

/**
 * @author sebastiangetts
 */
public final class MyLift {

    /**
     * DO NOT CHANGE THIS CLASS!
     */
    static final class ElevatorSystem {

        private final List<FloorControlPanel> floorControlPanels;
        private final List<Elevator> elevators;

        ElevatorSystem(int totalFloors, int totalElevators) {
            CallService callService = new CallService(totalFloors);
            this.floorControlPanels = new ArrayList<>(totalFloors);
            this.elevators = new ArrayList<>(totalElevators);
            for (int f = 0; f < totalFloors; f++) {
                floorControlPanels.add(new FloorControlPanel(f, callService));
            }
            for (int e = 0; e < totalElevators; e++) {
                elevators.add(new Elevator(totalFloors, callService));
            }
        }

        FloorControlPanel getFloorControlPanel(int floor) {
            return floorControlPanels.get(floor);
        }

        Elevator getElevator(int elevatorIndex) {
            return elevators.get(elevatorIndex);
        }
    }

    /**
     * A vertical elevator that travels up and down.
     */
    static final class Elevator {

        private final int totalFloor;
        private final CallService callService;

        private int currentFloor = 0;

        Elevator(int totalFloor, CallService callService) {
            this.totalFloor = totalFloor;
            this.callService = callService;
        }

        int getCurrentFloor() {
            return currentFloor;
        }

        /**
         * 当前所有的目标楼层（所有电梯的目标是公共的）
         *
         * @return target floors set
         */
        Set<Integer> getTargetFloors() {
            return new HashSet<>(callService.deque);
        }

        boolean addTargetFloor(int targetFloor) {
            if (targetFloor == currentFloor) {
                return false;
            }
            return callService.addTargetFloor(targetFloor);
        }

        void goToNext() {
            currentFloor = callService.handleNext(currentFloor);
        }
    }

    /**
     * A user interface on each floor to call elevators.
     * 1. 记下当前楼层的上下记录，等该楼层一到，清除记录。
     * 2. 看能否插队
     */
    static final class FloorControlPanel {

        // current floor
        private final int floor;
        private final CallService callService;

        FloorControlPanel(int floor, CallService callService) {
            this.floor = floor;
            this.callService = callService;
        }

        boolean pressUp() {
            // if total floor is 10, it means there are 9 floor...
            return callService.handlePressUp(floor);
        }

        boolean pressDown() {
            return callService.handlePressDown(floor);
        }

        boolean isUpPressed() {
            return callService.upSet.contains(floor);
        }

        boolean isDownPressed() {
            return callService.downSet.contains(floor);
        }
    }

    /**
     * A service to register calls and coordinate call handling.
     */
    static final class CallService {

        private final int totalFloors;

        private final Deque<Integer> deque;

        private final TreeSet<Integer> upSet;

        private final TreeSet<Integer> downSet;

        CallService(int totalFloors) {
            this.totalFloors = totalFloors;
            deque = new ArrayDeque<>();
            upSet = new TreeSet<>();
            downSet = new TreeSet<>();
        }

        boolean handlePressUp(int floor) {
            if (floor >= totalFloors - 1) {
                return false;
            }
            if (upSet.contains(floor)) {
                return false;
            }
            upSet.add(floor);

            return true;
        }

        boolean handlePressDown(int floor) {
            if (floor <= 0) {
                return false;
            }
            if (downSet.contains(floor)) {
                return false;
            }
            downSet.add(floor);

            return true;
        }

        /**
         * 处理target楼层队列或上下指令集合
         *
         * @param currentFloor 当前楼层
         * @return 即将到达的楼层
         */
        int handleNext(int currentFloor) {
            if (deque.isEmpty() && (upSet.size() > 0 || downSet.size() > 0)) {
                return handlePress(currentFloor);
            }
            if (deque.isEmpty()) {
                return currentFloor;
            }
            // 先处理press的，看有无可以插队的
            int next = deque.peekFirst();
            // 当前楼层 小于 下一站，表示电梯上行
            if (currentFloor - next < 0 && upSet.size() > 0) {
                Integer bigger = upSet.lower(next);
                if (bigger == null) {
                    deque.pollFirst();
                    upSet.remove(next);
                    downSet.remove(next);
                    return next;
                }
                if (bigger > currentFloor) {
                    // 可以插队的
                    upSet.remove(bigger);
                    return bigger;
                }
            }
            // 电梯下行
            if (currentFloor - next > 0 && downSet.size() > 0) {
                Integer bigger = downSet.higher(next);
                if (bigger == null) {
                    deque.pollFirst();
                    upSet.remove(next);
                    downSet.remove(next);
                    return next;
                }
                if (bigger < currentFloor) {
                    // 可以插队的
                    downSet.remove(bigger);
                    return bigger;
                }
            }
            deque.pollFirst();
            upSet.remove(next);
            downSet.remove(next);
            return next;
        }

        boolean addTargetFloor(int floor) {
            if (floor < 0 || floor > totalFloors - 1) {
                return false;
            }
            deque.offerLast(floor);

            return true;
        }

        /**
         * 处理按键，target楼层队列为空时，贪心找距离当前楼层最近的。
         * @param currentFloor 当前楼层
         * @return 即将到达的楼层
         */
        int handlePress(int currentFloor) {
            // 当只需要处理上下按键时，找距离当前楼层最近的过去
            Integer upHigher, upLower, downHigher, downLower;
            int target;
            if (upSet.isEmpty() && downSet.isEmpty()) {
                return currentFloor;
            } else if (upSet.isEmpty()) {
                downHigher = downSet.higher(currentFloor);
                downLower = downSet.lower(currentFloor);
                if (downHigher != null && downLower != null) {
                    target = Math.abs(downHigher - currentFloor) < Math.abs(downLower - currentFloor) ? downHigher : downLower;
                } else if (downHigher != null) {
                    target = downHigher;
                } else {
                    //noinspection ConstantConditions
                    target = downLower;
                }
            } else {
                upHigher = upSet.higher(currentFloor);
                upLower = upSet.lower(currentFloor);
                if (upHigher != null && upLower != null) {
                    target = Math.abs(upHigher - currentFloor) < Math.abs(upLower - currentFloor) ? upHigher : upLower;
                } else if (upHigher != null) {
                    target = upHigher;
                } else {
                    //noinspection ConstantConditions
                    target = upLower;
                }
            }
            upSet.remove(target);
            downSet.remove(target);

            return target;
        }
    }

}
