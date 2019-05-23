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

public class Command {
    
    public final static byte BOOTLOADER = 2;

    public static class Tux {

        private final static byte TUX = 0;
        
        public static class Eyes {
            public final static byte EYES_OPEN_CMD = 0x33;
            public final static byte EYES_CLOSE_CMD = 0x38;
            public final static byte EYES_BLINK_CMD = 0x40;
            public final static byte EYES_STOP_CMD = 0x32;

            public static byte[] open() {
                return new byte[] {TUX, EYES_OPEN_CMD};
            }

            public static byte[] close() {
                return new byte[] {TUX, EYES_CLOSE_CMD};
            }

            public static byte[] blink(byte times) {
                return new byte[] {TUX, EYES_BLINK_CMD, times };
            }

            public static byte[] stop() {
                return new byte[] {TUX, EYES_STOP_CMD};
            }
        }

        public static class Mouth {

            public final static byte MOUTH_OPEN_CMD = 0x34;
            public final static byte MOUTH_CLOSE_CMD = 0x35;
            public final static byte MOUTH_MOVE_CMD = 0x41;
            public final static byte MOUTH_STOP_CMD = 0x36;

            public static byte[] open() {
                return new byte[] {TUX, MOUTH_OPEN_CMD, 1};
            }

            public static byte[] close() {
                return new byte[] {TUX, MOUTH_CLOSE_CMD};
            }

            public static byte[] move(byte times) {
                return new byte[] {TUX, MOUTH_MOVE_CMD, times};
            }

            public static byte[] stop() {
                return new byte[] {TUX, MOUTH_STOP_CMD};
            }

        }

        public static class Flippers {
            public final static byte FLIPPERS_RAISE_CMD = 0x39;
            public final static byte FLIPPERS_LOWER_CMD = 0x3A;
            public final static byte FLIPPERS_WAVE_CMD = (byte) 0x80;
            public final static byte FLIPPERS_STOP_CMD = 0x30;

            public static byte[] raise() {
                return new byte[] {TUX, FLIPPERS_RAISE_CMD};
            }
            
            public static byte[] lower() {
                return new byte[] {TUX, FLIPPERS_LOWER_CMD};
            }
            public static byte[] wave(byte times, byte speed) {
                return new byte[] {TUX, FLIPPERS_WAVE_CMD, times, speed};
            }

            public static byte[] stop() {
                return new byte[] {TUX, FLIPPERS_STOP_CMD};
            }

        }

        public static class Led {
            private final static byte LED_FADE_SPEED_CMD = (byte) 0xD0;
            private final static byte LED_SET_CMD = (byte) 0xD1;
            private final static byte LED_PULSE_RANGE_CMD = (byte) 0xD2;
            private final static byte LED_PULSE_CMD = (byte) 0xD3;

            // TODO LED_FADE_SPEED_CMD

            // TODO LED_PULSE_RANGE_CMD

            public static byte[] pulse(byte parama, byte paramb) {
                return new byte[] {TUX, LED_PULSE_CMD, parama, paramb};
            }

            public static byte[] set(byte parama, byte paramb) {
                return new byte[] {TUX, LED_SET_CMD, parama, paramb};
            }

        }
    
        public static class Spin {
            private final static byte SPIN_LEFT_CMD = (byte) 0x83;
            private final static byte SPIN_RIGHT_CMD = (byte) 0x82;
            private final static byte SPIN_STOP_CMD = 0x37;
        }
        
    }

    public static class Fux {

        public final static byte FUX = 1;
        private final static byte USB_DONGLE_STATUS_CMD = 1;
        private final static byte USB_DONGLE_AUDIO_CMD = 2;
        private final static byte USB_DONGLE_VERSION_CMD = 6;
        private final static byte USB_DONGLE_CONNECTION_CMD = 0;

        public static class Connection {
            
            private final static byte USB_TUX_CONNECTION_DISCONNECT = 1;
            private final static byte USB_TUX_CONNECTION_CONNECT = 2;
            private final static byte USB_TUX_CONNECTION_ID_REQUEST = 3;
            private final static byte USB_TUX_CONNECTION_ID_LOOKUP = 4;
            private final static byte USB_TUX_CONNECTION_CHANGE_ID = 5;
            private final static byte USB_TUX_CONNECTION_WAKEUP = 6;
            private final static byte USB_TUX_CONNECTION_WIRELESS_CHANNEL = 7;

            public static byte[] connect() {
                return new byte[] {FUX, USB_DONGLE_CONNECTION_CMD, USB_TUX_CONNECTION_CONNECT};
            }

            public static byte[] disconnect() {
                return new byte[] {FUX, USB_DONGLE_CONNECTION_CMD, USB_TUX_CONNECTION_DISCONNECT, 0, 0};
            }

            public static byte[] idRequest() {
                return new byte[] {FUX, USB_DONGLE_CONNECTION_CMD, USB_TUX_CONNECTION_ID_REQUEST};
            }

            public static byte[] idLookup() {
                return new byte[] {FUX, USB_DONGLE_CONNECTION_CMD, USB_TUX_CONNECTION_ID_LOOKUP};
            }

            public static byte[] wakeup() {
                return new byte[] {FUX, USB_DONGLE_CONNECTION_CMD, USB_TUX_CONNECTION_WAKEUP};
            }


        }

    }
    
}