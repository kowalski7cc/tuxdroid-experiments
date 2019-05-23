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

/**
 * New
 */
public class New {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("New ready");
        Tuxdroid tuxdroid = new Tuxdroid();
        tuxdroid.setConnectedCallback(() -> {
            tuxdroid.runWhenOpen(() -> {
                System.out.println("Hello");
                tuxdroid.writeRaw(new byte[] { 0, 0x41, 2, 0, 0 });
            });
        });
        tuxdroid.start();
        tuxdroid.setEventListener(x -> System.out.println(arrayToString(x)));
        tuxdroid.writeRaw(Command.Fux.Connection.disconnect());
        Thread.sleep(1000);

        tuxdroid.stop();
    }

    public static String arrayToString(byte[] data) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < data.length; i++) {
            if (i > 3 && i % 4 == 0)
                result.append(Descriptor.getFromID(data[i]).map(Descriptor::toString).orElse("NULL"));
            else if (i == 0)
                result.append("Frame: " + String.format("%02x", data[i]));
            else if (i == 1)
                result.append("RF Connected: " + (data[i] == 1 ? "True" : "False"));
            else if (i == 3)
                result.append("Desc. count : " + data[i]);
            else
                result.append(String.format("%02x", data[i]));
            if (i + 1 < data.length) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}