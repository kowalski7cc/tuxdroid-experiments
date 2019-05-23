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
 * TuxHardware
 */
public class TuxHardware {

    private Ports ports;
    private Sensors1 sensors1;
    private Light light;
    private Position1 position1;
    private Position2 position2;
    private Ir ir;
    private Id id;
    private Battery battery;
    private Version version;
    private Revision revision;
    private Author author;
    private Audio audio;
    private Sound_var sound_var;
    private Flash_prog lash_prog;
    private Led led;
    private Pong pong;

    public TuxHardware() {
        ports = new Ports();
        sensors1 = new Sensors1();
    }
    public class Ports
    {
        byte portb;
        byte portc;
        byte portd;
    }

    public class Sensors1 {
    
        
    }

    public class Light {
    
        
    }

    public class Position1 {
    
        
    }

    public class Position2 {
    
        
    }

    public class Ir {
    
        
    }

    public class Id {
    
        
    }

    public class Battery {
    
        
    }

    public class Version {
    
        
    }

    public class Revision {
    
        
    }

    public class Author {
    
        
    }

    public class Audio {
    
        
    }

    public class Sound_var {
    
        
    }

    public class Flash_prog {
    
        
    }

    public class Led {
    
        
    }

    public class Pong {
    
        
    }

}