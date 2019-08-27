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

import java.io.Console;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * New
 */
public class New {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("New ready");
        Tuxdroid tuxdroid = new Tuxdroid();
        tuxdroid.setOnConnected(() -> {
            System.out.println("TuxDroid Connected");
            greet(tuxdroid);
        });
        tuxdroid.setOnDisconnected(() -> {
            System.out.println("TuxDroid Connected");
        });
        tuxdroid.start();
        // tuxdroid.setEventListener(x -> System.out.println(arrayToString(x)));
        greet(tuxdroid);

        List<Map.Entry<String, Method>> data = new LinkedList<>();
        for (Class<?> c : Command.class.getClasses()) {
            for (Class<?> c1 : c.getClasses()) {
                for (Method m : c1.getMethods()) {
                    data.add(Map.entry(
                            "Tuxdroid" + "-" + c.getSimpleName() + "-" + c1.getSimpleName() + "-" + m.getName(), m));
                }
            }
        }
        IntStream.range(0, data.size()).forEach(i -> System.out.println(i + ") " + data.get(i).getKey()));

        try (Scanner sc = new Scanner(System.in)) {
            data.get(sc.nextInt()).getValue().invoke(New.class, null);
        } catch (Exception e) {

        }

    }

    public static void greet(Tuxdroid tuxdroid) {
        System.out.println("Hello");
        tuxdroid.writeRaw(Command.Tux.Eyes.blink((byte) 2));
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