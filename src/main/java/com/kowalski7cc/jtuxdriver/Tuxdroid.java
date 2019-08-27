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
    private Runnable onConnected = () -> {};
    private Runnable onDisconnected = () -> {};

    private Consumer<byte[]> eventListener = (b) -> {
    };
    private Thread readLoop = new Thread(() -> {
        byte[] data = new byte[USBDefines.PACKET_LENGTH];
        try {
            while (!Thread.interrupted()) {
                if (device.get() == null) {
                    synchronized (device) {
                        device.wait();
                        
                    }
                    device.get().open();
                }
                if (device.get().isOpen()){
                    try {
                        device.get().write(USBDefines.STATUS, USBDefines.PACKET_LENGTH, (byte) 0);
                        device.get().read(data, 1000);
                        eventListener.accept(data);
                        synchronized(device) {
                            device.notifyAll();
                        }
                    } catch(NullPointerException e) {
                    }                   
                }
                Thread.sleep(300);
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
                        onDisconnected.run();
                    }
                }
            }

            @Override
            public void hidDeviceAttached(HidServicesEvent event) {
                if (event.getHidDevice().isVidPidSerial(USBDefines.VID, USBDefines.PID, null)) {
                    synchronized (device) {
                        device.set(event.getHidDevice());
                        device.notifyAll();
                        onConnected.run();
                    }
                }
            }
        });

        // Scan for already attached dongle
        Optional.ofNullable(hidServices.getHidDevice(USBDefines.VID, USBDefines.PID, null))
                .ifPresent(found -> device.set(found));

        readLoop.start();
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

    public void stop() {
        readLoop.interrupt();
    }

    public void setOnDisconnected(Runnable callback) {
        this.onDisconnected = callback;
    }

    public void setOnConnected(Runnable callback) {
        this.onConnected = callback;
    }

}