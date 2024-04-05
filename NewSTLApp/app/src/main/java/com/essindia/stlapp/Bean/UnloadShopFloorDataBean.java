package com.essindia.stlapp.Bean;

public class UnloadShopFloorDataBean {

    public boolean isRouteValid() {
        return isRouteValid;
    }

    public void setRouteValid(boolean routeValid) {
        isRouteValid = routeValid;
    }

    public boolean isMachineValid() {
        return isMachineValid;
    }

    public void setMachineValid(boolean machineValid) {
        isMachineValid = machineValid;
    }

    public boolean isCoilValid() {
        return isCoilValid;
    }

    public void setCoilValid(boolean coilValid) {
        isCoilValid = coilValid;
    }

    public String getRoute_str() {
        return route_str;
    }

    public void setRoute_str(String route_str) {
        this.route_str = route_str;
    }

    public String getMachine_str() {
        return machine_str;
    }

    public void setMachine_str(String machine_str) {
        this.machine_str = machine_str;
    }

    public String getCoil_str() {
        return coil_str;
    }

    public void setCoil_str(String coil_str) {
        this.coil_str = coil_str;
    }

    boolean isRouteValid;
    boolean isMachineValid;
    boolean isCoilValid;

    String  route_str;
    String  machine_str;
    String  coil_str;
}
