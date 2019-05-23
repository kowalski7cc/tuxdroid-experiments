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

import java.util.Arrays;
import java.util.Optional;

public enum Descriptor {

	FRAME_HEADER_PORTS((byte) 0xC0), FRAME_HEADER_SENSORS1((byte) 0xC1), FRAME_HEADER_LIGHT((byte) 0xC2), FRAME_HEADER_POSITION1((byte) 0xC3),
	FRAME_HEADER_POSITION2((byte) 0xC4), FRAME_HEADER_IR((byte) 0xC5), FRAME_HEADER_ID((byte) 0xC6), FRAME_HEADER_BATTERY((byte) 0xC7),
	FRAME_HEADER_VERSION((byte) 0xC8), FRAME_HEADER_REVISION((byte) 0xC9), FRAME_HEADER_AUTHOR((byte) 0xCA), FRAME_HEADER_SOUND_VAR((byte) 0xCB),
	FRAME_HEADER_AUDIO((byte) 0xCC), FRAME_HEADER_FLASH_PRO((byte) 0xCD), FRAME_HEADER_LED((byte) 0xCE), FRAME_HEADER_PONG((byte) 0xFF);

	private byte descriptor;

	private Descriptor(byte descriptor) {
		this.descriptor = descriptor;
	}

	public static Optional<Descriptor> getFromID(byte descriptor) {
		return Arrays.asList(values()).stream().filter(x -> x.descriptor == descriptor).findFirst();
	}

	public int getDescriptor() {
		return descriptor;
	}

	public String toString() {
		return this.name().split("_")[2];
	}

}