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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import TorJava.Common.Encoding;
import TorJava.Common.Encryption;
import TorJava.Common.Parsing;
import TorJava.Common.TorException;


/**
 * a compound data structure that keeps track of the static informations we have
 * about a single Tor server.
 * 
 * CG - 07/07/09 Removed GeoIP
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @author Michael Koellejan
 * @author Connell Gauld
 * @version unstable
 */
public class Server {
		Tor tor;
	
    // The raw router descriptor which has been handed to us. 
    // In the normal case we just return this stored descriptor.
    String routerDescriptor;

    // Information extracted from the Router descriptor.
    public String nickname; 

    String hostname; // ip or hostname
    public InetAddress address; // the resolved hostname
    public String countryCode; // country code where it is located

    int orPort;
    int socksPort;
    int dirPort;

    int bandwidthAvg;
    int bandwidthBurst;
    int bandwidthObserved;
 
    String platform;
    Date published;

    byte[] fingerprint;

    int uptime;
    
    RSAPublicKeyStructure onionKey;
    RSAPrivateKeyStructure onionKeyPrivate;
    
    RSAPublicKeyStructure signingKey;
    RSAPrivateKeyStructure signingKeyPrivate;
  
    ExitPolicy[] exitpolicy;

    byte[] router_signature;
    
    String contact;

    HashSet<String> family = new HashSet<String>();
    
//  FIXME: read-history, write-history not implemented
    static final String PUBLISHED_ITEM_SIMPLEDATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final int MAX_EXITPOLICY_ITEMS = 300;
    private SimpleDateFormat dateFormat;

    // Additional information for V2-Directories
    Date lastUpdate;
    boolean dirv2_authority = false;
    boolean dirv2_exit = false;  // v1 trusted
    boolean dirv2_fast = false;
    boolean dirv2_guard = false;
    boolean dirv2_named = false;
    boolean dirv2_stable = false;
    boolean dirv2_running = false; // v1 alive
    boolean dirv2_valid = false;
    boolean dirv2_v2dir = false;

    // Additional internal information
    // removed - are replaced with dirv2_* stuff
    //boolean alive = true;
    //boolean trusted = false;

    // TorJava Server-Ranking data
    float rankingIndex;
    static final int highBandwidth = 2097152; // see updateServerRanking()
    static final float alpha = 0.6f; // see updateServerRanking()
    //static final float rankingIndexEffect = 0.5f; // see getRefinedRankingIndex
    static final float punishmentFactor = 0.75f; // coefficient to decrease
                                                    // server ranking if the                                                  // server fails to respo                                                 // in ti
    /**
     * compound data structure for storing exit policies
     */
    class ExitPolicy {
        boolean accept; // if false: reject
        long ip;
        long netmask;
        int lo_port;
        int hi_port;
    }
    
    /**
     * takes a router descriptor as string
     * 
     * @param routerDescriptor
     *            a router descriptor to initialize the object from
     */
    Server(Tor tor,String routerDescriptor) throws TorException {
				if (tor==null) throw new TorException("Server.<init>: tor is null");
				this.tor = tor;
        init();
        update(routerDescriptor);
        this.countryCode = "?";

    }

    /**
     * Special constructor for hidden service: Faked server in connectToHidden().
     * @param pk
     * @throws TorException
     */
    Server(Tor tor,RSAPublicKeyStructure pk) throws TorException {
				if (tor==null) throw new TorException("Server.<init>: tor is null");
				this.tor = tor;
        init();
        onionKey = pk;
        this.countryCode = "?";
    }

    /**
     * takes input data and initializes the server object with it. A router
     * descriptor and a signature will be automatically generated.
     */
    Server(Tor tor,String varNickname, InetAddress varAddress, int varOrPort, int varSocksPort,
            int varDirPort, int varBandwidthAvg, int varBandwidthBurst,
            int varBandwidthObserved, byte[] varfingerprint, int varInitialUptime,
            RSAPublicKeyStructure varOnionKey, RSAPrivateKeyStructure varOnionKeyPrivate, 
            RSAPublicKeyStructure varSigningKey, RSAPrivateKeyStructure varSigningKeyPrivate,
            ExitPolicy[] varExitpolicy, String varContact, HashSet<String> varFamily) 
			throws TorException
		{
				if (tor==null) throw new TorException("Server.<init>: tor is null");
        
        // Set member variables.
				this.tor = tor;
        this.nickname = varNickname;
        this.address = varAddress;
        this.hostname = varAddress.getHostAddress();
        
        this.orPort = varOrPort;
        this.socksPort = varSocksPort;
        this.dirPort = varDirPort;
        
        this.bandwidthAvg = varBandwidthAvg;
        this.bandwidthBurst = varBandwidthBurst;
        this.bandwidthObserved = varBandwidthObserved;

        this.platform = TorConfig.TORJAVA_VERSION_STRING + " on " + TorConfig.operatingSystem();
       
        this.published = new Date(System.currentTimeMillis());
        this.fingerprint = new byte[varfingerprint.length];
        System.arraycopy(varfingerprint, 0, this.fingerprint, 0,
                varfingerprint.length);

        this.uptime = varInitialUptime;
        
        this.onionKey = varOnionKey;
        this.onionKeyPrivate = varOnionKeyPrivate;
        this.signingKey = varSigningKey;
        this.signingKeyPrivate = varSigningKeyPrivate;

        this.exitpolicy = varExitpolicy;

        this.contact = varContact;
        
        this.family = varFamily;
        
        // Render router descriptor
        this.routerDescriptor = renderRouterDescriptor();
        this.countryCode = "?";

    }

    /** Constructor-indepentent initialization **/
    private void init() {
        dateFormat = new SimpleDateFormat(PUBLISHED_ITEM_SIMPLEDATE_FORMAT);
        // unknown/new
        rankingIndex = -1;
    }

    /**
     * updates the object from a router descriptor
     * 
     * @param routerDescriptor
     *            string encoded router descriptor
     */
    void update(String routerDescriptor) throws TorException {
        parseRouterDescriptor(routerDescriptor);
        updateServerRanking();
    }

    /**
     *  wrapper from server-flags of dir-spec v1 to dir-spec v2
     */
    void updateServerStatus(boolean alive,boolean trusted) {
      dirv2_running = alive;
      dirv2_exit = trusted;
      dirv2_guard = trusted;
      dirv2_valid = trusted;
    }
    
    /**
     * Update this server's status
     * 
     * @param flags string containing flags
     */
    void updateServerStatus(String flags) {
      if (flags.indexOf("Running")>=0)     dirv2_running = true; 
      if (flags.indexOf("Exit")>=0)           dirv2_exit = true; 
      if (flags.indexOf("Authority")>=0) dirv2_authority = true;
      if (flags.indexOf("Fast")>=0)           dirv2_fast = true;
      if (flags.indexOf("Guard")>=0)         dirv2_guard = true;
      if (flags.indexOf("Stable")>=0)       dirv2_stable = true;
      if (flags.indexOf("Named")>=0)         dirv2_named = true;
      if (flags.indexOf("V2Dir")>=0)         dirv2_v2dir = true;
      if (flags.indexOf("Valid")>=0)         dirv2_valid = true;
    }

    /**
     * @return the regular expression that can be evaluated by the
     *         initialisation function
     */
    static String regularExpression() {
        return "(router (\\w+) \\S+ \\d+ \\d+.*?END SIGNATURE-----\n)";
    }

    /**
     * This function parses the exit policy items from the router descriptor.
     * 
     * @param routerDescriptor
     *            a router descriptor with exit policy items.
     * @return the complete exit policy
     */
    private ExitPolicy[] parseExitPolicy(String routerDescriptor) {
        ArrayList<ExitPolicy> epList = new ArrayList<ExitPolicy>(30);
        ExitPolicy ep;

        Pattern p = Pattern.compile("^(accept|reject) (.*?):(.*?)$",
                Pattern.DOTALL + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE
                        + Pattern.UNIX_LINES);
        Matcher m = p.matcher(routerDescriptor);

        // extract all exit policies from description
        int nr = 0;
        while (m.find() && (nr < MAX_EXITPOLICY_ITEMS)) {
            ep = new ExitPolicy();
            ep.accept = m.group(1).equals("accept");
            // parse network
            String network = m.group(2);
            ep.ip = 0;
            ep.netmask = 0;
            if (!network.equals("*")) {
                int slash = network.indexOf("/");
                if (slash >= 0) {
                    ep.ip = Encoding.dottedNotationToBinary(network.substring(0,
                            slash));
										String netmask = network.substring(slash + 1);
										if (netmask.indexOf(".")>-1)
											ep.netmask = Encoding.dottedNotationToBinary(netmask);
										else ep.netmask = (((0xffffffffL << (32-(Integer.parseInt(netmask))))) & 0xffffffffL);
                } else {
                    ep.ip = Encoding.dottedNotationToBinary(network);
                    ep.netmask = 0xffffffff;
                }
                ;
            }
            ;
            ep.ip = ep.ip & ep.netmask;
            // parse port range
            if (m.group(3).equals("*")) {
                ep.lo_port = 0;
                ep.hi_port = 65535;
            } else {
                int dash = m.group(3).indexOf("-");
                if (dash > 0) {
                    ep.lo_port = Integer
                            .parseInt(m.group(3).substring(0, dash));
                    ep.hi_port = Integer.parseInt(m.group(3)
                            .substring(dash + 1));
                } else {
                    ep.lo_port = Integer.parseInt(m.group(3));
                    ep.hi_port = ep.lo_port;
                }
                ;
            }
            ;
            ++nr;
            epList.add(ep);
        }

        return (ExitPolicy[]) (epList.toArray(new ExitPolicy[1]));
    }

    /**
     * extracts all relevant information from the router discriptor and saves it
     * in the member variables.
     * 
     * @param rd
     *            string encoded router descriptor
     */
    private void parseRouterDescriptor(String rd)
            throws TorException {
        this.routerDescriptor = rd;

        // Router item: nickname, hostname, onion-router-port, socks-port, dir-port
        Pattern p = Pattern.compile(
                "^router (\\w+) (\\S+) (\\d+) (\\d+) (\\d+)", Pattern.DOTALL
                        + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE
                        + Pattern.UNIX_LINES);
        Matcher m = p.matcher(rd);
        m.find();

        this.nickname = m.group(1);

        this.hostname = m.group(2);
        this.orPort = Integer.parseInt(m.group(3));
        this.socksPort = Integer.parseInt(m.group(4));
        this.dirPort = Integer.parseInt(m.group(5));

        // secondary information
        platform = Parsing.parseStringByRE(rd, "^platform (.*?)$", "unknown");
        published = dateFormat.parse(Parsing.parseStringByRE(rd, "^published (.*?)$", ""), (new ParsePosition(0)));
        uptime = Integer.parseInt(Parsing.parseStringByRE(rd, "^uptime (\\d+)", "0"));
        fingerprint = Encoding.parseHex(Parsing.parseStringByRE(rd, "^opt fingerprint (.*?)$", ""));
        contact = Parsing.parseStringByRE(rd, "^contact (.*?)$", "");

        // make that IF description is from a trusted server, that fingerprint is correct
				if (tor.config.trustedServers.containsKey(nickname)) {
					String fingerprintFromConfig = (String) (tor.config.trustedServers.get(nickname)).get("fingerprint");
					if (!Encoding.toHexString(fingerprint).equalsIgnoreCase(fingerprintFromConfig))
						throw new TorException("Server " + nickname + " is trusted, but fingerprint check failed");
				}

        // bandwith
        p = Pattern.compile("^bandwidth (\\d+) (\\d+) (\\d+)?", Pattern.DOTALL
                + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE
                + Pattern.UNIX_LINES);
        m = p.matcher(rd);
        if (m.find()) {
            bandwidthAvg = Integer.parseInt(m.group(1));
            bandwidthBurst = Integer.parseInt(m.group(2));
            bandwidthObserved = Integer.parseInt(m.group(3));
        }
        ;

        // onion key
        String stringOnionKey = Parsing.parseStringByRE(rd,
                "^onion-key\n(.*?END RSA PUBLIC KEY......)", "");
        onionKey = Encryption.extractRSAKey(stringOnionKey);

        // signing key
        String stringSigningKey = Parsing.parseStringByRE(rd,
                "^signing-key\n(.*?END RSA PUBLIC KEY-----\n)", "");
        signingKey = Encryption.extractRSAKey(stringSigningKey);

        SHA1Digest sha1 = new SHA1Digest();

        // verify signing-key against fingerprint
        try {
            RSAPublicKeyStructure signingKey_asn = new RSAPublicKeyStructure(
                    signingKey.getModulus(), signingKey.getPublicExponent());
            byte[] pkcs = Encryption
                    .getPKCS1EncodingFromRSAPublicKey(signingKey_asn);
            byte[] key_hash = new byte[20];
            sha1.update(pkcs, 0, pkcs.length);
            sha1.doFinal(key_hash, 0);
            if (!Encoding.arraysEqual(key_hash, fingerprint))
                throw new TorException("Server " + nickname
                        + " doesn't verify signature vs fingerprint");
        } catch (Exception e) {
            throw new TorException("Server " + nickname
                    + " doesn't verify signature vs fingerprint");
        }

        // parse family
        String stringFamily = Parsing.parseStringByRE(rd, "^family (.*?)$", "");
        if (stringFamily == "")
            stringFamily = Parsing.parseStringByRE(rd, "^opt family (.*?)$", "");
        Pattern p_family = Pattern.compile("(\\S+)");
        Matcher m_family = p_family.matcher(stringFamily);
        while (m_family.find()) {
            String host = m_family.group(1);
            family.add(host);

        }

        // check the validity of the signature    
        router_signature = Encoding
                .parseBase64(Parsing
                        .parseStringByRE(
                                rd,
                                "^router-signature\n-----BEGIN SIGNATURE-----(.*?)-----END SIGNATURE-----",
                                ""));
        byte[] sha1_input = (Parsing.parseStringByRE(rd,
                "^(router .*?router-signature\n)", "")).getBytes();
        if (!Encryption.verifySignature(router_signature, signingKey, sha1_input)) {
            Logger.logCrypto(Logger.ERROR,
                    "Server -> router-signature check failed for " + nickname);
            throw new TorException("Server " + nickname
                    + ": description signature verification failed");
        }

        // exit policy
        exitpolicy = parseExitPolicy(rd);
        // usually in directory the hostname is already set to the IP
        // so, following resolve just converts it to the InetAddress
        try {
            address = InetAddress.getByName(hostname); 
        } catch (UnknownHostException e) {
            throw new TorException("Server.ParseRouterDescriptor: Unresolvable hostname " + hostname);
        }
    }

    /**
     * converts exit policy objects back into an item
     * 
     * @param ep
     *            an array of exit-policy objects.
     * @return an exit policy item.
     * 
     */
    private String renderExitPolicy(ExitPolicy[] ep) {
        StringBuffer raw_policy = new StringBuffer();

        for (int i = 0; i < ep.length; i++) {
            if (ep[i].accept)
                raw_policy.append("accept ");
            else
                raw_policy.append("reject ");

            if (ep[i].netmask == 0 && ep[i].ip == 0) {
                raw_policy.append("*");
            } else {
                if (ep[i].netmask == 0xffffffff) {
                    raw_policy.append(Encoding.binaryToDottedNotation(ep[i].ip));
                } else {
                    raw_policy.append(Encoding.binaryToDottedNotation(ep[i].ip));
                    raw_policy.append("/"
														+ Encoding.netmaskToInt(ep[i].netmask));
//                            + Encoding.binaryToDottedNotation(ep[i].netmask));  // deprecated
                }
            }

            raw_policy.append(":");

            if (ep[i].lo_port == 0 && ep[i].hi_port == 65535) {
                raw_policy.append("*");
            } else {
                if (ep[i].lo_port == ep[i].hi_port) {
                    raw_policy.append(ep[i].lo_port);
                } else {
                    raw_policy.append(ep[i].lo_port + "-" + ep[i].hi_port);
                }
            }

            raw_policy.append("\n");
        }

        return raw_policy.toString();
    }

    /**
         * renders a router descriptor from member variables
         * 
         * @return router descriptor in extensible information format
         */
        String renderRouterDescriptor() {
            StringBuffer rawServer = new StringBuffer();
    
            rawServer.append("router " + nickname + " " + address.getHostAddress() + " " + orPort + " " + socksPort + " " + dirPort + "\n");
            rawServer.append("platform " + platform + "\n");
            
            rawServer.append("published " + dateFormat.format(published) + "\n");
            rawServer.append("opt fingerprint " + Parsing.renderFingerprint(fingerprint, true) + "\n");
            if (uptime != 0)
                rawServer.append("uptime " + uptime + "\n");
            rawServer.append("bandwidth " + bandwidthAvg + " " + bandwidthBurst + " " + bandwidthObserved + "\n");
    
            rawServer.append("onion-key\n" + Encryption.getPEMStringFromRSAPublicKey(onionKey) + "\n");
    
            rawServer.append("signing-key\n" + Encryption.getPEMStringFromRSAPublicKey(signingKey) + "\n");
     
            String stringFamily = "";
            Iterator<String> familyIterator = family.iterator();
            while(familyIterator.hasNext()) {
                stringFamily += " " + familyIterator.next();
            }
           
            rawServer.append("opt family" + stringFamily + "\n");
    
            if (contact != "")
                rawServer.append("contact " + contact + "\n");
    
            rawServer.append(renderExitPolicy(exitpolicy));
    
    //      sign data
            rawServer.append("router-signature\n");
     
            rawServer.append("directory-signature " + tor.config.nickname + "\n");
            byte[] data = rawServer.toString().getBytes();
            rawServer.append(Encryption.binarySignatureToPEM(Encryption.signData(data,
                    new RSAKeyParameters(true, signingKeyPrivate.getModulus(),
                            signingKeyPrivate.getPrivateExponent()))));
            
            return rawServer.toString();
        }

    /**
     * updates the server ranking index
     * 
     * Is supposed to be between 0 (undesirable) and 1 (very desirable). Two
     * variables are taken as input:
     * <ul>
     * <li> the uptime
     * <li> the bandwidth
     * <li> if available: the previous ranking
     * </ul>
     */
    private void updateServerRanking() {
        float rankingFromDirectory = (Math.min(1, uptime / 86400) + Math.min(1,
                (bandwidthAvg * alpha + bandwidthObserved * (1 - alpha))
                        / highBandwidth)) / 2; // 86400 is uptime of 24h
        // build over-all ranking from old value (if available) and new 
        if (rankingIndex<0) {
            rankingIndex = rankingFromDirectory;
        } else {
            rankingIndex = rankingFromDirectory *(1-TorConfig.rankingTransferPerServerUpdate)  + 
                           rankingIndex         *   TorConfig.rankingTransferPerServerUpdate;
        }
        Logger.logDirectory(Logger.VERBOSE,"Server.updateServerRanking: "+nickname+" is ranked "+rankingIndex);
    }

    /**
     * returns ranking index taking into account user preference
     * 
     * @param p
     *            user preference (importance) of considering ranking index
     *            <ul>
     *            <li> 0 select hosts completely randomly
     *            <li> 1 select hosts with good uptime/bandwidth with higher
     *            prob.
     *            </ul>
     */
    float getRefinedRankingIndex(float p) {
        // align all ranking values to 0.5, if the user wants to choose his
        // servers
        // from a uniform probability distribution
        return (rankingIndex * p + TorConfig.rankingIndexEffect * (1 - p));
    }

    /**
     * decreases ranking_index by the punishment_factor
     */
    void punishRanking() {
        rankingIndex *= punishmentFactor;
    }

    /**
     * can be used to query the exit policies wether this server would allow
     * outgoing connections to the host and port as given in the parameters.
     * <b>IMPORTANT:</b> this routing must be able to work, even if <i>addr</i>
     * is not given!
     * 
     * @param addr
     *            the host that someone wants to connect to
     * @param port
     *            the port that is to be connected to
     * @return a boolean value wether the conenction would be allowed
     */
    boolean exitPolicyAccepts(InetAddress addr, int port) { // used by
                                                            // create_new_route
        long ip;
        if (addr != null) { // set IP as given
            byte[] temp1 = addr.getAddress();
            long[] temp = new long[4];
            for (int i = 0; i < 4; ++i) {
                temp[i] = temp1[i];
                if (temp[i] < 0)
                    temp[i] = 256 + temp[i];
            }
            ;
            ip = ((temp[0] << 24) | (temp[1] << 16) | (temp[2] << 8) | temp[3]);
        } else {
            // HACK: if no IP and port is given, always return true
            if (port == 0)
                return true;
            // HACK: if no IP is given, use only exits that allow ALL ip-ranges
            // this should possibly be replaced by some other way of checking it
            ip = 0xffffffffL;
        }
        ;
    
        for (int i = 0; i < exitpolicy.length; ++i) {
            if ((exitpolicy[i].lo_port <= port)
                    && (exitpolicy[i].hi_port >= port)
                    && (exitpolicy[i].ip == (ip & exitpolicy[i].netmask))) {
                return exitpolicy[i].accept;
            }
            ;
        }
        ;
        return false;
    }

    /**
     * @return can this server be used as a directory-server?
     */
    boolean isDirServer() {
        return (dirPort > 0);
    }

    // DEBUG_START
    /**
     * used for debugging purposes
     * 
     * @param b
     *            an array t be printed in hex
     */
    private String print_array(byte[] b) {
        String hex = "0123456789abcdef";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            int x = b[i];
            if (x < 0)
                x = 256 + x; // why are there no unsigned bytes in java?
            sb.append(hex.substring(x >> 4, (x >> 4) + 1));
            sb.append(hex.substring(x % 16, (x % 16) + 1));
            sb.append(" ");
        }
        ;
        return sb.toString();
    }

    /**
     * used for debugging purposes
     */
    String print() {
        StringBuffer sb = new StringBuffer();
        sb.append("---- " + nickname + " (" + contact + ")\n");
        sb.append("hostname:" + hostname + "\n");
        sb.append("or port:" + orPort + "\n");
        sb.append("socks port:" + socksPort + "\n");
        sb.append("dirserver port:" + dirPort + "\n");
        sb.append("platform:" + platform + "\n");
        sb.append("published:" + published + "\n");
        sb.append("uptime:" + uptime + "\n");
        sb.append("bandwidth: " + bandwidthAvg + " " + bandwidthBurst
                + " " + bandwidthObserved + "\n");
        sb.append("fingerprint:" + print_array(fingerprint) + "\n");
        sb.append("onion key:" + onionKey + "\n");
        sb.append("signing key:" + signingKey + "\n");
        sb.append("signature:" + print_array(router_signature) + "\n");
        sb.append("exit policies:" + "\n");
        for (int i = 0; i < exitpolicy.length; ++i)
            sb.append("  " + exitpolicy[i].accept + " "
                    + Encoding.toHex(exitpolicy[i].ip) + "/"
                    + Encoding.toHex(exitpolicy[i].netmask) + ":"
                    + exitpolicy[i].lo_port + "-" + exitpolicy[i].hi_port
                    + "\n");
        return sb.toString();
    }
    // DEBUG_END
}
