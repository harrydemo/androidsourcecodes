/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava;

import java.io.IOException;
import java.io.InputStream;

import TorJava.Common.Encoding;

/**
 * most general form of the cells in the Tor protocol. Should not be used on its
 * own.
 * 
 * @author Lexi Pimenidis
 * @author Vinh Pham
 * @version unstable
 */

public class Cell {
    int circID;

    public byte command;

    byte[] payload;

    static final String[] type_to_string = { "padding", "create", "created",
            "relay", "destroy", "create-fast", "created-fast" };

    static final int CELL_PADDING = 0; /* Padding */

    static final int CELL_CREATE = 1; /* Create a circuit */

    static final int CELL_CREATED = 2; /* Acknowledge create */

    public static final int CELL_RELAY = 3; /* End-to-end data */

    static final int CELL_DESTROY = 4; /* Stop using a circuit */

    static final int CELL_CREATE_FAST = 5; /* Create a circuit, no PK */

    static final int CELL_CREATED_FAST = 6; /* Circuit created, no PK */

    static final int CELL_TOTAL_SIZE = 512;

    static final int CELL_CIRCID_SIZE = 2;

    static final int CELL_COMMAND_SIZE = 1;

    static final int CELL_PAYLOAD_SIZE = 509;

    static final int CELL_CIRCID_POS = 0;

    static final int CELL_COMMAND_POS = CELL_CIRCID_POS + CELL_CIRCID_SIZE;

    static final int CELL_PAYLOAD_POS = CELL_COMMAND_POS + CELL_COMMAND_SIZE;

    /* Circuit for sending data or circuit that needs to be created */
    Circuit outCircuit;

    /**
     * init cell for sending.
     * 
     * @param outCircuit
     *            a circuit that is used to send some data on, or that needs ot
     *            be created
     * @param command
     *            the purpose of this cell
     */
    Cell(Circuit outCircuit, int command) {
        // payload is all zeros because java does this for us.
        this.payload = new byte[Cell.CELL_PAYLOAD_SIZE];

        this.circID = outCircuit.ID;
        this.command = (Encoding.intToNByteArray(command, 1))[0];
        this.outCircuit = outCircuit;
    }

    /**
     * init cell from data<br>
     * Attention: this.outCircuit is not set!
     * 
     * @param data
     *            a raw cell. 512 bytes long.
     */
    Cell(byte[] data) throws NullPointerException {
        init_from_data(data);
    }

    /**
     * init cell from stream Attention: this.outCircuit is not set!
     * 
     * @param in
     *            the input stream from a TLS-line to read the data from
     */
    Cell(InputStream in) throws IOException {
        if (in == null)
            throw new IOException("null as input stream given");
        byte[] data = new byte[Cell.CELL_TOTAL_SIZE];
        int filled = 0;
        while (filled < data.length) {
            int n = in.read(data, filled, data.length - filled);
            if(n<0) throw new IOException("Cell.<init>: reached EOF");
            filled += n;
            Thread.yield();
        };
        init_from_data(data);
    }

    /**
     * this function decodes an incoming cell from the raw bytes into the given
     * data structures of the Cell-Class.
     * 
     * @param data
     *            a raw cell. 512 bytes long.
     */
    private void init_from_data(byte[] data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("no data given");
        this.payload = new byte[Cell.CELL_PAYLOAD_SIZE];

        this.circID = Encoding.byteArrayToInt(data, Cell.CELL_CIRCID_POS,
                Cell.CELL_CIRCID_SIZE);
        this.command = data[Cell.CELL_COMMAND_POS];
        System.arraycopy(data, Cell.CELL_PAYLOAD_POS, this.payload, 0,
                payload.length);

        // ** DEBUG-START
        Logger.logCell(Logger.RAW_DATA, "Cell.init_from_data: "+print("Received "));
        // ** DEBUG-END
    }

    /** is this a padding cell? */
    boolean isTypePadding() {
        return this.command == CELL_PADDING;
    }

    /** is this a created cell? */
    boolean isTypeCreated() {
        return this.command == CELL_CREATED;
    }

    /** is this a relay cell? */
    boolean isTypeRelay() {
        return this.command == CELL_RELAY;
    }

    /** is this a destroy cell? */
    boolean isTypeDestroy() {
        return this.command == CELL_DESTROY;
    }

    /**
     * @deprecated other function from the package should access the circID
     *             directly
     */
    int getCircID() {
        return circID;
    }

    /**
     * concat all data to a single byte-array. This function is used to finally
     * transmit the cell over a line.
     */
    byte[] toByteArray() {
        byte[] buff = new byte[Cell.CELL_TOTAL_SIZE];
        // ** DEBUG-START
        Logger.logCell(Logger.RAW_DATA, "Cell.toByteArray(): "+print("Sending "));
        // ** DEBUG-END
        System.arraycopy(Encoding.intToNByteArray(this.circID,
                Cell.CELL_CIRCID_SIZE), 0, buff, Cell.CELL_CIRCID_POS,
                Cell.CELL_CIRCID_SIZE);

        buff[Cell.CELL_COMMAND_POS] = this.command;
        System.arraycopy(this.payload, 0, buff, CELL_PAYLOAD_POS,
                this.payload.length);

        return buff;
    }

    // ** DEBUG-START
    /** wrapper for print(String descr) */
    String print() {
        return print("");
    }

    public static String type(int t) {
        if ((t >= 0) && (t < type_to_string.length))
            return type_to_string[t];
        else
            return "[" + t + "]";
    }

    public String type() {
        return type(command);
    }

    /** used for debugging output */
    String print(String descr) {
        return descr + "cell for circuit " + getCircID() + " with command "
                + type() + ". Payload:\n" + Encoding.toHexString(payload, 100);
    }
    // ** DEBUG-END
}
