// Copyright (C) 2019 Kowalski7cc
// 
// JTuxDriver is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// JTuxDriver is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with JTuxDriver. If not, see <http://www.gnu.org/licenses/>.

package com.kowalski7cc.jtuxdriver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

public class Tuxdroid {

    private AtomicReference<HidDevice> device;
    private Runnable connectedCallback = () -> {};
    private Runnable disconnectedCallback = () -> {};
    private Consumer<byte[]> eventListener = (b) -> {
    };
    private Thread readLoop = new Thread(() -> {
        byte[] data = new byte[USBDefines.PACKET_LENGTH];
        try {
            while (!Thread.interrupted()) {
                if (device.get() == null) {
                    synchronized (device) {
                        System.out.println("loop standby");
                        device.wait();
                    }
                }
                if (device.get().isOpen()){
                    try {
                        device.get().write(USBDefines.STATUS, USBDefines.PACKET_LENGTH, (byte) 0);
                        device.get().read(data, 1000);
                        eventListener.accept(data);
                    } catch(NullPointerException e) {
                        // Device disconnected
                        System.out.println("OWO");
                    }                   
                } else {
                    device.get().open();
                }
                Thread.sleep(150);
            }
        } catch (InterruptedException e) {
            System.out.println("loop interrupted");
        }
    });

    public Tuxdroid() {
        device = new AtomicReference<>();
    }

    public void start() {
        HidServices hidServices = HidManager.getHidServices();
        hidServices.addHidServicesListener(new HidServicesListener() {

            @Override
            public void hidFailure(HidServicesEvent event) {

            }

            @Override
            public void hidDeviceDetached(HidServicesEvent event) {
                if (event.getHidDevice().isVidPidSerial(USBDefines.VID, USBDefines.PID, null)) {
                    synchronized (device) {
                        device.set(null);
                        device.notifyAll();
                        disconnectedCallback.run();
                    }
                }
            }

            @Override
            public void hidDeviceAttached(HidServicesEvent event) {
                if (event.getHidDevice().isVidPidSerial(USBDefines.VID, USBDefines.PID, null)) {
                    synchronized (device) {
                        device.set(event.getHidDevice());
                        device.notifyAll();
                        connectedCallback.run();
                    }
                }
            }
        });

        // Scan for already attached dongle
        Optional.ofNullable(hidServices.getHidDevice(USBDefines.VID, USBDefines.PID, null))
                .ifPresent(found -> device.set(found));

        readLoop.start();
    }

    public void waitForDevice() {
        synchronized (device) {
            try {
                device.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public void waitForDevice(int millis) {
        synchronized (device) {
            try {
                device.wait(millis);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setEventListener(Consumer<byte[]> eventListener) {
        this.eventListener = eventListener;
    }

    public void writeRaw(byte[] bs) {
        if(device.get() != null)
            if(device.get().isOpen())
                device.get().write(bs, 64, (byte) 0);
    }

    public boolean openDevice() {
        return (device.get() != null) ? device.get().open() : false;
    }

    public boolean isDeviceOpen() {
        return (device.get() != null) ? device.get().isOpen() : false;
    }

    public void runWhenOpen(Runnable runnable) {
        new Thread(() -> {
            if(device.get() != null) {
                waitForDevice();
            }
            while (!isDeviceOpen());
            runnable.run();
        }).start();
    }

    public void stop() {
        readLoop.interrupt();
    }

    public void setDisconnectedCallback(Runnable callback) {
        this.disconnectedCallback = callback;
    }

    public void setConnectedCallback(Runnable callback) {
        this.connectedCallback = callback;
    }

}