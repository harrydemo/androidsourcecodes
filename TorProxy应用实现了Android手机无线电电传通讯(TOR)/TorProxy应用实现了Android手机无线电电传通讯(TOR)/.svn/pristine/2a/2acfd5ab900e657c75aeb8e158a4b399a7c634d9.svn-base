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
import java.net.InetAddress;
import java.net.UnknownHostException;

import TorJava.Common.Encoding;
import TorJava.Common.TorException;

/**
 * the general form of a RELAY cell in the Tor Protocol. This class also calls
 * the crypto- functions in Node.java to decode an onion, if encrypted data is
 * received.
 * 
 * @author Lexi Pimenidis
 * @version unstable
 */

public class CellRelay extends Cell {
    public byte relay_command;

    // byte[] recognized; (ALWAYS ZERO)
    int streamID; // 16 bit unsigned integer

    byte[] digest;

    int length; // 16 bit unsigned integer

    public byte[] data;

    /** set to a value from 0 to outCircuit.route_established-1
     *  to address a special router in the chain, default is the last one
      */
    private int addressedRouterInCircuit=-1;

    static final byte CELL_RELAY_ESTABLISH_RENDEZVOUS = 33;

    static final byte CELL_RELAY_INTRODUCE1 = 34;

    static final byte CELL_RELAY_RENDEZVOUS2 = 37;

    static final byte CELL_RELAY_RENDEZVOUS_ESTABLISHED = 39;

    static final byte CELL_RELAY_COMMAND_INTRODUCE_ACK = 40;

    TCPStream s;

    static final int RELAY_BEGIN = 1;

    static final int RELAY_DATA = 2;

    public static final int RELAY_END = 3;

    static final int RELAY_CONNECTED = 4;

    static final int RELAY_SENDME = 5;

    static final int RELAY_EXTEND = 6;

    static final int RELAY_EXTENDED = 7;

    static final int RELAY_TRUNCATE = 8;

    static final int RELAY_TRUNCATED = 9;

    static final int RELAY_DROP = 10;

    static final int RELAY_RESOLVE = 11;

    static final int RELAY_RESOLVED = 12;

    static final int RELAY_ESTABLISH_INTRO = 32;

    static final int RELAY_ESTABLISH_RENDEVOUS = 33;

    static final int RELAY_INTRODUCE1 = 34;

    static final int RELAY_INTRODUCE2 = 35;

    static final int RELAY_RENDEVOUS1 = 36;

    static final int RELAY_RENDEVOUS2 = 37;

    static final int RELAY_INTRO_ESTABLISHED = 38;

    static final int RELAY_RENDEVOUS_ESTABLISHED = 39;

    static final int RELAY_COMMAND_INTRODUCE_ACK = 40; // FIX there are some
                                                        // constant declared
                                                        // twice

    static final int RELAY_COMMAND_SIZE = 1;

    static final int RELAY_RECOGNIZED_SIZE = 2;

    static final int RELAY_STREAMID_SIZE = 2;

    static final int RELAY_DIGEST_SIZE = 4;

    static final int RELAY_LENGTH_SIZE = 2;

    static final int RELAY_DATA_SIZE = 498;

    static final int RELAY_TOTAL_SIZE = CELL_PAYLOAD_SIZE;

    static final int RELAY_COMMAND_POS = 0;

    static final int RELAY_RECOGNIZED_POS = RELAY_COMMAND_POS
            + RELAY_COMMAND_SIZE;

    static final int RELAY_STREAMID_POS = RELAY_RECOGNIZED_POS
            + RELAY_RECOGNIZED_SIZE;

    static final int RELAY_DIGEST_POS = RELAY_STREAMID_POS
            + RELAY_STREAMID_SIZE;

    static final int RELAY_LENGTH_POS = RELAY_DIGEST_POS + RELAY_DIGEST_SIZE;

    static final int RELAY_DATA_POS = RELAY_LENGTH_POS + RELAY_LENGTH_SIZE;

    // DEBUG_START
    /** used for a nicer debugging output */
    static final String[] command_to_string = { "zero", "begin", "data", "end",
            "connected", "sendme", "extend", "extended", "truncate",
            "truncated", "drop", "resolv", "resolved" };

    static final String[] reason_to_string = { "none", "misc",
            "resolve failed", "connect refused", "exit policy", "destroy",
            "done", "timeout", "(unallocated - see spec)", "hibernating",
            "internal", "resource limit", "connection reset",
            "tor protocol violation" };

    // DEBUG_END

    /**
     * constructor. used for EXTEND-cells and SENDME-cells
     */
    CellRelay(Circuit c, int relay_command) {
        super(c, Cell.CELL_RELAY);
        this.relay_command = (byte) relay_command;
        reset();
    }

    /**
     * init cell. used by RELAY_BEGIN-cells
     */
    CellRelay(TCPStream s, int relay_command) {
        super(s.circ, Cell.CELL_RELAY);
        reset();
        this.s = s;
        this.streamID = s.ID;
        this.relay_command = (byte) relay_command;
    }

    /**
     * gets memory, resets values
     */
    void reset() {
        streamID = 0;
        digest = new byte[4];
        length = 0;
        data = new byte[498];
    }

    /**
     * init from received data
     */
    CellRelay(byte[] data) throws TorException {
        super(data);
        reset();
        init_from_data();
    }

    /**
     * init from main Cell-type
     */
    CellRelay(Circuit circ, Cell cell) throws TorException {
        super(cell.toByteArray()); // TODO: inefficient at max! - but currently working
        reset();
        this.outCircuit = circ;
        init_from_data();
    }

    /**
     * init cell from stream
     */
    CellRelay(InputStream in) throws IOException, TorException {
        super(in);
        reset();
        init_from_data();
    }

    boolean isTypeBegin() {
        return relay_command == RELAY_BEGIN;
    }

    boolean isTypeData() {
        return relay_command == RELAY_DATA;
    }

    boolean isTypeEnd() {
        return relay_command == RELAY_END;
    }

    boolean isTypeConnected() {
        return relay_command == RELAY_CONNECTED;
    }

    boolean isTypeSendme() {
        return relay_command == RELAY_SENDME;
    }

    boolean isTypeExtend() {
        return relay_command == RELAY_EXTEND;
    }

    boolean isTypeExtended() {
        return relay_command == RELAY_EXTENDED;
    }

    boolean isTypeTruncate() {
        return relay_command == RELAY_TRUNCATE;
    }

    boolean isTypeTruncated() {
        return relay_command == RELAY_TRUNCATED;
    }

    boolean isTypeDrop() {
        return relay_command == RELAY_DROP;
    }

    boolean isTypeResolve() {
        return relay_command == RELAY_RESOLVED;
    }

    boolean isTypeResolved() {
        return relay_command == RELAY_RESOLVED;
    }

    boolean isTypeEstablishedRendezvous() {
        return relay_command == CELL_RELAY_RENDEZVOUS_ESTABLISHED;
    }

    boolean isTypeIntroduceACK() {
        return relay_command == CELL_RELAY_COMMAND_INTRODUCE_ACK;
    }

    boolean isTypeRendezvous2() {
        return relay_command == CELL_RELAY_RENDEZVOUS2;
    }

    boolean isTypeIntroduce2() {
        return relay_command == RELAY_INTRODUCE2;
    }

    /**
     * decrypts an onion and checks digests and stuff. input is taken from the
     * parent class' payload.
     */
    void init_from_data() throws TorException {
        Logger.logCell(Logger.VERBOSE, "CellRelay.init_from_data() for "
                + outCircuit.route_established + " layers");
        // decrypt forwards, take keys from route
        int encrypting_router;
        boolean digest_verified = false;
        if (outCircuit.route_established == 0)
            Logger.logCell(Logger.WARNING,
                    "CellRelay.init_from_data() for zero layers on "
                            + outCircuit.print());
        for (encrypting_router = 0; encrypting_router <= outCircuit.route_established; ++encrypting_router) {
            // check if no decryption has lead to a recognized cell
            if (encrypting_router == outCircuit.route_established)
                throw new TorException(
                        "relay cell not recognized, possibly due to decryption errors?");
            // decrypt payload
            outCircuit.route[encrypting_router].sym_decrypt(payload);
            // if recognized and digest is correct, then stop decrypting
            if ((payload[CellRelay.RELAY_RECOGNIZED_POS] == 0)
                    && (payload[CellRelay.RELAY_RECOGNIZED_POS + 1] == 0)) {
                // check digest.
                System.arraycopy(payload, CellRelay.RELAY_DIGEST_POS, digest,
                        0, CellRelay.RELAY_DIGEST_SIZE); // save digest
                payload[CellRelay.RELAY_DIGEST_POS] = 0; // set to ZERO
                payload[CellRelay.RELAY_DIGEST_POS + 1] = 0;
                payload[CellRelay.RELAY_DIGEST_POS + 2] = 0;
                payload[CellRelay.RELAY_DIGEST_POS + 3] = 0;
                byte[] digest_calc = outCircuit.route[encrypting_router]
                        .calc_backward_digest(payload); // calc digest
                System
                        .arraycopy(digest, 0, payload,
                                CellRelay.RELAY_DIGEST_POS,
                                CellRelay.RELAY_DIGEST_SIZE); // restore
                                                                // digest
                if ((digest[0] == digest_calc[0]) && // check digest
                        (digest[1] == digest_calc[1])
                        && (digest[2] == digest_calc[2])
                        && (digest[3] == digest_calc[3])) {
                    Logger.logCell( Logger.VERBOSE,
                                    "CellRelay.init_from_data(): backward digest from "
                                            + outCircuit.route[encrypting_router].server.nickname
                                            + " is OK");
                    digest_verified = true;
                    break;
                }
                ;
            }
            ;
        }
        // check if digest verified
        if (!digest_verified) {
            Logger.logCell(Logger.WARNING,
                    "CellRelay.init_from_data(): Received "
                            + Encoding.toHexString(digest)
                            + " as backward digest but couldn't verify");
            throw new TorException("wrong digest");
        }
        ;
        // copy data from payload
        relay_command = payload[CellRelay.RELAY_COMMAND_POS];
        streamID = Encoding.byteArrayToInt(payload, CellRelay.RELAY_STREAMID_POS,
                CellRelay.RELAY_STREAMID_SIZE);
        length = Encoding.byteArrayToInt(payload, CellRelay.RELAY_LENGTH_POS,
                CellRelay.RELAY_LENGTH_SIZE);
        System.arraycopy(payload, CellRelay.RELAY_DATA_POS, data, 0,
                CellRelay.RELAY_DATA_SIZE);
        // DEBUG OUTPUT
        Logger.logCell(Logger.VERBOSE, "CellRelay.init_from_data(): "
                + print());
    }

    /** set to a value from 0 to outCircuit.route_established-1
     *  to address a special router in the chain, default is the last one
      */
    boolean setAddressedRouter(int router) {
      if ((router>-1)&&(router < outCircuit.route_established)) {
        addressedRouterInCircuit = router;
        return true;
      };
      return false;
    }

    /**
     * prepares the meta-data, such that the cell can be transmitted. encrypts
     * an onion.
     * 
     * @return the data ready for sending
     */
    byte[] toByteArray() {
        Logger.logCell(Logger.VERBOSE, "CellRelay.toByteArray() for "
                + outCircuit.route_established + " layers");
        // put everything in payload
        payload[CellRelay.RELAY_COMMAND_POS] = (byte) relay_command;
        System.arraycopy(Encoding.intToNByteArray(streamID,
                CellRelay.RELAY_STREAMID_SIZE), 0, payload,
                CellRelay.RELAY_STREAMID_POS, CellRelay.RELAY_STREAMID_SIZE);
        System.arraycopy(Encoding.intToNByteArray(length,
                CellRelay.RELAY_LENGTH_SIZE), 0, payload,
                CellRelay.RELAY_LENGTH_POS, CellRelay.RELAY_LENGTH_SIZE);
        System.arraycopy(data, 0, payload, CellRelay.RELAY_DATA_POS,
                CellRelay.RELAY_DATA_SIZE);
        // calc digest and insert it
        int i0 = outCircuit.route_established - 1;
        if (addressedRouterInCircuit>=0) i0=addressedRouterInCircuit;
        digest = outCircuit.route[i0].calc_forward_digest(payload);
        System.arraycopy(digest, 0, payload, CellRelay.RELAY_DIGEST_POS,
                CellRelay.RELAY_DIGEST_SIZE);
        // DEBUG OUTPUT
        Logger.logCell(Logger.RAW_DATA, "CellRelay.toByteArray(): " + print());
        // encrypt backwards, take keys from route
        for (int i = i0; i >= 0; --i)
            outCircuit.route[i].sym_encrypt(payload);
        // create the byte array to be send over TLS
        return super.toByteArray();
    }

    // DEBUG_START
    /**
     * for debugging and stuff
     */
    static String reasonForClosing(int reason) {
        if ((reason < 0) || (reason >= reason_to_string.length))
            return "[" + reason + "]";
        return reason_to_string[reason];
    }

    public String reasonForClosing() {
        return reasonForClosing(data[0]);
    }

    public String relayCommand() {
        return relayCommand(relay_command);
    }

    public static String relayCommand(int cmd) {
        if ((cmd < command_to_string.length) && (cmd >= 0))
            return command_to_string[cmd];
        else
            return "[" + cmd + "]";
    }

    /**
     * used for debugging
     */
    String print() {
        StringBuffer sb = new StringBuffer();

        // main header
        sb.append("Relay cell for circuit " + this.circID + "/stream "
                + streamID + " with command " + relayCommand() + ".\n");
        // is the cell not recognized?
        if (Encoding.byteArrayToInt(payload, 1, 2) != 0) {
            sb.append("  Recognized    "
                    + Encoding.toHexString(payload, 100, 1, 2) + "\n");
            sb.append("  DigestID      " + Encoding.toHexString(digest) + "\n");
        }
        ;
        // display connection
        if (isTypeBegin()) {
            byte[] host = new byte[length - 1];
            System.arraycopy(data, 0, host, 0, length - 1);
            sb.append("  Connecting to: " + new String(host) + "\n");
        } else
        // display reason for end, if given
        if (isTypeEnd()) {
            sb.append("  End reason: " + reasonForClosing() + "\n");
        } else
        // display connection
        if (isTypeConnected()) {
            byte[] ip = new byte[length];
            System.arraycopy(data, 0, ip, 0, length);
            try {
                sb.append("  Connected to: "
                        + InetAddress.getByAddress(ip).toString() + "\n");
            } catch (UnknownHostException e) {
            }
        } else
        // display data field, if there is data AND data is not encrypted
        if ((length > 0) && (relay_command != 6) && (relay_command != 7))
            sb.append("  Data (" + length + " bytes)\n"
                    + Encoding.toHexString(data, 100, 0, length) + "\n");

        return sb.toString();
    }
    // DEBUG_END
}
