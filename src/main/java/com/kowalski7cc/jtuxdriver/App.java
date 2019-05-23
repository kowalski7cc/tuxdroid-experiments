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

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

public class App {

    private static AtomicReference<HidDevice> tuxdroid;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Ready");
        tuxdroid = new AtomicReference<>();
        HidServices hidServices = HidManager.getHidServices();
        hidServices.addHidServicesListener(new HidServicesListener() {

            @Override
            public void hidFailure(HidServicesEvent event) {
            }

            @Override
            public void hidDeviceDetached(HidServicesEvent event) {
                var dev = event.getHidDevice();
                System.out.println(dev);
                if (dev.getProduct().equals(USBDefines.PNAME)) {
                    synchronized (tuxdroid) {
                        System.out.println("USB attached");
                        tuxdroid.set(null);
                        tuxdroid.notifyAll();
                    }
                }
            }

            @Override
            public void hidDeviceAttached(HidServicesEvent event) {
                var dev = event.getHidDevice();
                System.out.println(dev);
                if (dev.getProduct().equals(USBDefines.PNAME)) {
                    synchronized (tuxdroid) {
                        System.out.println("USB attached");
                        tuxdroid.set(dev);
                        tuxdroid.notifyAll();
                    }
                }
            }
        });

        // Find tuxdroid already attached
        Optional.ofNullable(hidServices.getHidDevice(USBDefines.VID, USBDefines.PID, null))
                .ifPresent(device -> tuxdroid.set(device));

        while (tuxdroid.get() == null) {
            waitForDevice();
        }

        System.out.println("Tuxdroid Connected");

        while (tuxdroid.get().isOpen() != true);

        new Thread(() -> {
            while (tuxdroid.get() != null && tuxdroid.get().isOpen()) {
                byte[] data = new byte[64];
                tuxdroid.get().write(new byte[] { 0x01, 0x01, 0x00, 0x00 }, 64, (byte) 0);
                tuxdroid.get().read(data, 500);
                System.out.println(Byte.toString(data[0]) + ":" + Byte.toString(data[1]) + ":" + Byte.toString(data[2])
                        + ":" + Byte.toString(data[3]) + ":" + Byte.toString(data[4]));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

        byte[] send = new byte[64];
        send[0] = 0x00;
        send[1] = 0x40;
        send[2] = 0x02;
        send[3] = 0x00;
        send[4] = 0x00;

        tuxdroid.get().write(send, 64, (byte) 0);

        // send[0] = 0x01;
        // send[1] = 0x01;
        // send[2] = 0x00;
        // send[3] = 0x00;
        // send[4] = 0x00;
        // tuxdroid.get().write(send, 64, (byte) 0);
        // Thread.sleep(300);
        // tuxdroid.get().write(send, 64, (byte) 0);
        // Thread.sleep(300);
        // tuxdroid.get().write(send, 64, (byte) 0);
        // Thread.sleep(300);


        
        waitForDevice();

        if(tuxdroid.get()==null)
            System.out.println("Tuxdroid disconnected");

    }

    public static void waitForDevice() {
        synchronized (tuxdroid) {
            try {
                tuxdroid.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
