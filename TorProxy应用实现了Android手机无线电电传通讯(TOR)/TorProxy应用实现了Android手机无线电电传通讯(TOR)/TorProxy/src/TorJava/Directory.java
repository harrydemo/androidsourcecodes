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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.KeyPair;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.provider.JCERSAPrivateKey;
import org.bouncycastle.jce.provider.JCERSAPublicKey;

import TorJava.Common.Common;
import TorJava.Common.Encoding;
import TorJava.Common.Encryption;
import TorJava.Common.Parsing;
import TorJava.Common.StreamsAndHTTP;
import TorJava.Common.TorException;

/**
 * This class maintains a list of the currently known Tor routers. It has
 * the capability to update stats and find routes that fit to certain criteria.
 * 
 * @author Lexi Pimenidis
 * @author Tobias Koelsch
 * @author Andriy Panchenko
 * @author Michael Koellejan
 * @author Johannes Renner
 * @author Connell Gauld
 */
class Directory {
    Tor tor;
    
    public static final String CONSENSUS_CACHE_FILENAME = "networkstatus_cached.dat";
  
    // List of known Tor servers
    ConcurrentHashMap<String, Server> torServers;
    HashMap<String, String> identityDigestToNickname;
    HashMap<String, String> nicknameToDigestDescriptor;
    HashMap<String, String> nicknameToIdentityDigest;
    ArrayList<NetworkStatusDescription> latestDirCaches;


    // HashMap that has class C address as key, and a HashSet with nicks
    // of Nodes that have IP-Address of that class
    HashMap<String, HashSet<String>> addressNeighbours;

    // HashMap where keys are CountryCodes, and values are HashSets with nicks
    // of Nodes having an IP-address in the specific country
    HashMap<String, HashSet<String>> countryNeighbours;

    // HashSet excluded by config nodes
    HashSet<String> excludedNodesByConfig;

    Random rnd;

    static final String PUBLISHED_ITEM_SIMPLEDATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // Number of retries to find a route on one recursive stap before falling
    // back and changing previous node
    static final int RETRIES_ON_RECURSIVE_ROUTE_BUILD = 10; 

    // time intervals to poll descriptor fetchers for result
    static final int FETCH_THREAD_QUERY_TIME = 2000; // 2 second
    
    SimpleDateFormat dateFormat;
    Date published;

    JCERSAPrivateKey signingKeyPrivate;
    JCERSAPublicKey signingKey;

    ArrayList<String> recommendedSoftware;

    boolean updateRunning = false;
    int updateCounter = 0;
    int nodeCounter = 0;
    
    boolean stop = false;

    /**
     * Initialize directory from the network
     */
    public Directory(Tor tor, KeyPair dirServerKeys) {
        init(tor, dirServerKeys);
        // is done from background mgmt .. but should be done here a first time
        // NOTE FROM LEXI: refreshListOfServers MUST NOT be called from here, 
        // but instead MUST be called from background mgmt
        //refreshListOfServers();
    }

    /**
     * Initialize directory from a file
     * @param filename
     *            the file that stores informations about the directory
     */
    public Directory(Tor tor, KeyPair dirServerKeys, String filename, String fingerprint) {
        init(tor, dirServerKeys);
        readDirectoryFromFile(filename, fingerprint);
    }
    
    /** Initialize the directory object **/
    private void init(Tor tor, KeyPair dirServerKeys) {
      this.tor = tor;        
      if (dirServerKeys != null) {
        try {
          signingKey = (org.bouncycastle.jce.provider.JCERSAPublicKey)(dirServerKeys.getPublic());
          signingKeyPrivate = (org.bouncycastle.jce.provider.JCERSAPrivateKey)(dirServerKeys.getPrivate());
        } catch(ClassCastException e) {
          System.err.println("TorJava.Directory.init: caught infamous ClassCastException. No solution known.");
          e.printStackTrace();
        }
      }
      torServers = new ConcurrentHashMap<String,Server>();
      identityDigestToNickname = new HashMap<String, String>();
      nicknameToDigestDescriptor = new HashMap<String, String>();
      latestDirCaches = new ArrayList<NetworkStatusDescription>();
      nicknameToIdentityDigest = new HashMap<String, String>();
      
      addressNeighbours = new HashMap<String,HashSet<String>>();
      countryNeighbours = new HashMap<String,HashSet<String>>();
      rnd = new Random();
      dateFormat = new SimpleDateFormat(PUBLISHED_ITEM_SIMPLEDATE_FORMAT);
      recommendedSoftware = new ArrayList<String>();
      excludedNodesByConfig = new HashSet<String>();
      excludedNodesByConfig.addAll(TorConfig.avoidedNodes);
    }

    /**
     * Render the directory in the extensible information format. used to write
     * it to disk and also used by the directory-server
     * 
     * @return directory in extensible information format.
     */
    public String renderDirectory() throws IOException {
        StringBuffer rawDir = new StringBuffer();
    
        rawDir.append("signed-directory\n");

        published = new Date(System.currentTimeMillis());
        rawDir.append("published " + dateFormat.format(published) + "\n");
    
        StringBuffer tmpRecommendedSoftware = new StringBuffer();
        ListIterator<String> rsIterator = recommendedSoftware.listIterator();
        while (rsIterator.hasNext()) {
            tmpRecommendedSoftware.append(rsIterator.next() + ",");
        }
    
        rawDir.append("recommended-software "
                + tmpRecommendedSoftware.toString() + "\n");
        rawDir.append("router-status " + renderRouterStatus() + "\n");
        rawDir.append("opt ranking-index " + renderRankingIndex() + "\n");
    
        rawDir.append("dir-signing-key\n"
                + Encryption.getPEMStringFromRSAPublicKey(
                        Encryption.getRSAPublicKeyStructureFromJCERSAPublicKey(
                                signingKey)) + "\n");
    
        Iterator<String> serverNames = (torServers.keySet()).iterator();
    
        while (serverNames.hasNext()) {
            Server s = (Server) torServers.get((String) (serverNames.next()));
            rawDir.append(s.routerDescriptor);
            rawDir.append("\n");
        }
    
        // sign data
        rawDir.append("directory-signature " + tor.config.nickname + "\n");
        byte[] data = rawDir.toString().getBytes();
        rawDir.append(Encryption.binarySignatureToPEM(Encryption.signData(data,
                new RSAKeyParameters(true, signingKeyPrivate.getModulus(),
                        signingKeyPrivate.getPrivateExponent()))));
    
        return rawDir.toString();
    }

    /**
     * Render an array of servers with their ranking indices
     */
    private String renderRankingIndex() {
        StringBuffer rawIndex = new StringBuffer();
        // Iterate over all server nicknames
        Iterator<String> serverNames = (torServers.keySet()).iterator();
        while (serverNames.hasNext()) {
            // Instantiate the Server-objects
            Server s = (Server)torServers.get(serverNames.next());
            rawIndex.append(s.nickname);
            rawIndex.append("=" + s.rankingIndex + " ");
        }    
        return rawIndex.toString();
    }

    /**
     * Assemble the router-status
     */
    private String renderRouterStatus() {
        StringBuffer rawStatus = new StringBuffer();
        // Iterate over all server nicknames
        Iterator<String> serverNames = (torServers.keySet()).iterator();
        while (serverNames.hasNext()) {
            Server s = (Server)torServers.get(serverNames.next());
            //if (s.alive == false)
            if (s.dirv2_running == false) rawStatus.append("!");
            //if (s.trusted == true)
            if (s.dirv2_exit == true) rawStatus.append(s.nickname + "=");
            rawStatus.append("$" + Parsing.renderFingerprint(s.fingerprint, false) + " ");
        }
        return rawStatus.toString();
    }

    private void addToNeighbours(Server s){
        HashSet<String> neighbours;
        String ipClassCString = Parsing.parseStringByRE(s.address.getHostAddress(), "(.*)\\.", "");
        
        //add it to the addressNeighbours
        if (addressNeighbours.containsKey(ipClassCString))
            neighbours =  addressNeighbours.get(ipClassCString);
        else
            neighbours = new HashSet<String>();
        neighbours.add(s.nickname);
         addressNeighbours.put(ipClassCString, neighbours);

        //add it to the country neighbours
        if (countryNeighbours.containsKey(s.countryCode))
            neighbours =  countryNeighbours.get(s.countryCode);
        else
            neighbours = new HashSet<String>();
        neighbours.add(s.nickname);
        countryNeighbours.put(s.countryCode, neighbours);
    }

    private Server receivedServerDescriptor(String name, String description) {
      Server s = null;
      // Create new entry if not existing
      if (!torServers.containsKey(name)) {
        try {
          s = new Server(tor,description);
          torServers.put(name, s);
          addToNeighbours(s);
          if (TorConfig.avoidedCountries.contains(s.countryCode))
             excludedNodesByConfig.add(s.nickname);
        } catch (TorException e) {
          Logger.logDirectory(Logger.WARNING, "Directory.receivedServerDescriptor: " + e.getMessage());
        }
      } else {
        // Server exists
        s = ((Server) torServers.get(name));
        try {
          s.update(description);
        } catch (TorException e) {
          Logger.logDirectory(Logger.WARNING, "Directory.receivedServerDescriptor: " + e.getMessage());
        }
      }
      return s;
    }
    
    /**
     * Write the directory to a file
     * 
     * @param file
     *            the filename
     */
    void writeDirectoryToFile(String file) {
      try {
        Logger.logDirectory(Logger.INFO, "Directory.writeDirectoryToFile: writing directory to "+file);
        File dir = new File(file);
        if (dir.exists()) {
          dir.delete();
          dir.createNewFile();
        }   
        PrintWriter writer = new PrintWriter(new FileOutputStream(dir));
        writer.write(renderDirectory());
        writer.close();
      } catch (IOException e) {
        Logger.logDirectory(Logger.WARNING, "Directory.writeDirectoryToFile: Error: "+e.getMessage());
      }
    }

    /**
     * Read the directory out of a file
     * 
     * @param file
     *            the filename
     */
    void readDirectoryFromFile(String file,String fingerprint) {
      try {
        // first try to load real file (outside jar)
        parseDirServerV1(StreamsAndHTTP.readAllFromStream(new DataInputStream( new FileInputStream(new File(file)))),fingerprint);
      } catch(Exception e) {
         Logger.logDirectory(Logger.VERBOSE, "Directory.readDirectoryFromFile: Warning: " + e.getMessage());
        try {
          // if that does not work, check within JAR
          parseDirServerV1(StreamsAndHTTP.readAllFromStream(new DataInputStream(ClassLoader.getSystemResourceAsStream(file))),fingerprint);
        } catch (Exception e2) {
          Logger.logDirectory(Logger.WARNING, "Directory.readDirectoryFromFile: Warning: " + e2.getMessage());
        }
      }
    }

    /**
     * Poll some known servers, is triggered by TorBackgroundMgmt
     * and directly after starting.<br>
     * TODO : Test if things do not break if suddenly servers disappear from the directory that are currently being used<br>
     * TODO : Test if servers DO disappear from the directory
     *
     * @return 0 = no update, 1 = v1 update, 2 = v2 update, 3 = v3 update
     */
    public int refreshListOfServers(boolean fastStartup) {
      // Check if there's already an update running
      if (updateRunning) {
        Logger.logDirectory(Logger.INFO,"Directory.refreshListOfServers: update already running...");
        return 0;
      };
      updateRunning = true; // yes, I know... race condition... anyway unlikely      
      
      // Sort the known directory authorities for their protocol version
      // TODO: Move this into the constructor, since it only needs to be done once?
      HashMap<String, HashMap<String, Object>> v1Servers = new HashMap<String, HashMap<String, Object>>();
      HashMap<String, HashMap<String, Object>> v2Servers = new HashMap<String, HashMap<String, Object>>(); 
      HashMap<String, HashMap<String, Object>> v3Servers = new HashMap<String, HashMap<String, Object>>();
      Iterator<String> i = tor.config.trustedServers.keySet().iterator();
      while (i.hasNext()) {
        String nick = i.next();
        HashMap<String, Object> s = tor.config.trustedServers.get(nick);
        try {
          int version = ((Integer) s.get("version")).intValue();
          if (version == 1) v1Servers.put(nick, s);
          else if (version == 2) v2Servers.put(nick, s);
          else if (version == 3) v3Servers.put(nick, s);
        } catch(Exception e) {
          Logger.logDirectory(Logger.WARNING,"Directory.refreshListOfServers: "+e.getMessage());
        }
      }
      // NEW: Try V3 first
      if (v3Servers.size()>0) {
        try {
          updateNetworkStatusV3(v3Servers, fastStartup);
          if (stop) return 0;
          // Finish, if some nodes were found
          if (torServers.size()>0) {
            updateRunning = false;
            return 3;
          }
        } catch(Exception e) {
          Logger.logDirectory(Logger.ERROR, "Directory.refreshListOfServers: "+e.getMessage());
        }
      } else Logger.logDirectory(Logger.WARNING,"Directory.refreshListOfServers: No trusted V3 directory authorities known");
      // Try V2
      Logger.logDirectory(Logger.ERROR, "Directory.refreshListOfServers: Dir protocol V3 was unable to find any tor nodes, fallback to V2");
      if (v2Servers.size()>0) {
        try {
          updateNetworkStatusV2(v2Servers);
          // Finish, if some nodes were found
          if (torServers.size()>0) {
            updateRunning = false;
            return 2;
          }
        } catch(Exception e) {
          Logger.logDirectory(Logger.ERROR,"Directory.refreshListOfServers: "+e.getMessage());
        }
      } else Logger.logDirectory(Logger.WARNING,"Directory.refreshListOfServers: No trusted V2 directory authorities known");
      // Finally try V1
      Logger.logDirectory(Logger.ERROR,"Directory.refreshListOfServers: Dir protocol V2 was unable to find any tor nodes, fallback to V1");
      if (v1Servers.size()>0) {
        // Poll known V1-servers until success
        i = v1Servers.keySet().iterator();
        while (i.hasNext()) {
          try {
            HashMap<String, Object> s = v1Servers.get(i.next());
            String ip = (String)s.get("ip");
            int port = ((Integer)s.get("port")).intValue();
            Logger.logDirectory(Logger.INFO, "Directory.refreshListOfServers: Polling v1-server "+i.next()+" ("+ip+":"+port+")");
            pollDirserverV1(ip, port, (String)s.get("fingerprint"));
            // Finish, if some nodes were found
            if (torServers.size()>0) {
              updateRunning = false;
              return 1;
            }                  
          } catch(Exception e) {
            Logger.logDirectory(Logger.ERROR,"Directory.refreshListOfServers: "+i.next()+" (v1) > "+e.getMessage());
          } 
        }
      }
      if (torServers.size()>0) 
        Logger.logDirectory(Logger.WARNING, "Directory.refreshListOfServers: No directory information available");
      updateRunning = false;
      return 0;
    }
        
    /**
     * Get a V3 network-status consensus, parse it and initiate downloads of missing descriptors
     * 
     * @param v3Servers
     * @throws TorException
     */
    private void updateNetworkStatusV3(HashMap<String, HashMap<String, Object>> v3Servers, boolean fastStartup) throws TorException {
      // The list of servers to be updated is stored in this HashMap
      HashMap<String, NetworkStatusDescription> updateServer = new HashMap<String,NetworkStatusDescription>();
      HashSet<String> dirCaches = new HashSet<String>();
      ++updateCounter;
      // Setup a list containg all nicknames
      List<String> nicks = new ArrayList<String>();
      Iterator<String> i = v3Servers.keySet().iterator();
      while (i.hasNext()) nicks.add(i.next());
      // Choose one randomly
      while (nicks.size()>0) {
        int index = rnd.nextInt(nicks.size());
        String nick = nicks.get(index);
        Logger.logDirectory(Logger.INFO, "Directory.updateNetworkStatusV3: Randomly chosen authority "+nick);
        try {
          HashMap<String,Object> s = v3Servers.get(nick);
          Logger.logDirectory(Logger.INFO, "Directory.updateNetworkStatusV3: Fetching consensus document from trusted server "+nick);
          
          boolean usingCached = false;
          String consensus = null;
          if (fastStartup) {
        	  StringBuilder b = new StringBuilder();
        	  FileReader fin = null;
        	  try {
	        	  fin = new FileReader(TorConfig.getConfigDir() + CONSENSUS_CACHE_FILENAME);
	        	  int read = 0;
	        	  char[] buffer = new char[1024];
	        	  while(true) {
	        		  read = fin.read(buffer);
	        		  if (read == -1) break;
	        		  b.append(buffer, 0, read);
	        	  }
	        	  consensus = b.toString();
	        	  usingCached = true;
	        	  Logger.logDirectory(Logger.INFO, "Using cached consensus document");
        	  } catch (Exception e) { // Do our best but don't worry
        		  consensus = null;
        	  } finally {
        		  if (fin != null) fin.close();
        	  }
          }
        	  
          if (consensus == null) {
              // Download network status from server
        	  consensus = StreamsAndHTTP.HTTPRequest((String)s.get("ip"), ((Integer)s.get("port")).intValue(),
                                "GET /tor/status-vote/current/consensus HTTP/1.1\r\nHost: "+nick+"\r\n\r\n", 
                                TorConfig.dirV2NetworkStatusRequestTimeOut);
          }
          if (consensus==null) throw new TorException("null answer from "+nick);
          if (consensus.length()<1) throw new TorException("no answer from "+nick);
          if (consensus.indexOf(" 200")<0) throw new TorException("http-error from "+nick);
          // Save a cache
          if (!usingCached) {
	          FileWriter fout = null;
	          try {
	        	  File f = new File(TorConfig.getConfigDir() + CONSENSUS_CACHE_FILENAME);
	        	  f.createNewFile();
	        	  fout = new FileWriter(f);
	        	  fout.write(consensus);
	        	  Logger.logDirectory(Logger.INFO, "Saved consensus cache");
	          } catch (Exception e) {
	        	  // We tried...
	        	  Logger.logDirectory(Logger.VERBOSE, "Unable to save cache");
	          } finally {
	        	  if (fout != null) fout.close();
	          }
          }
          // Parse the document
          if (stop) return;
          
          parseDirV3NetworkStatus(consensus, Encoding.parseHex((String)s.get("fingerprint")), updateServer, dirCaches);

          Logger.logDirectory(Logger.INFO, "Directory.updateNetworkStatusV3: Parsed network status, starting to fetch descriptors ..");
          TorKeeper.setComplete(TorKeeper.CONSENSUS_PARSED, true);
          break;
        } catch(Exception e) {
          Logger.logDirectory(Logger.WARNING, "Directory.updateNetworkStatusV3: " + e.getMessage());
          nicks.remove(index);
        }
      }
      if (stop) return;
      fetchDescriptors(updateServer, dirCaches, fastStartup);
	  //Debug.stopMethodTracing();
    }
    
    /**
     * Parse a directory protocol V3 network-status consensus document
     */
    private void parseDirV3NetworkStatus(String consensus, byte[] fingerprint, HashMap<String, NetworkStatusDescription> updateServer, HashSet<String> dirCaches) 
       throws TorException, ParseException {
      // Check the version
      String version = Parsing.parseStringByRE(consensus, "^network-status-version (\\d+)", "");
      if (!version.equals("3")) throw new TorException("wrong network status version");
      // Parse valid-until
      Pattern p_valid = Pattern.compile("^valid-until (\\S+) (\\S+)", Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
      Matcher m_valid = p_valid.matcher(consensus);
      if (m_valid.find()) {
        Date validUntil = dateFormat.parse(m_valid.group(1)+" "+m_valid.group(2));
        Logger.logDirectory(Logger.INFO,"Directory.parseDirV3NetworkStatus: Consensus document is valid until "+validUntil);   
      }
      
      // TODO: Check signature here 
      // Extract the signed data
      //byte[] signedData = Parsing.parseStringByRE(consensus,"^(network-status-version.*directory-signature )", "").getBytes();
      //Logger.logDirectory(Logger.INFO, "Directory.parseDirV3NetworkStatus: Extracted the signed data");
      //System.out.println(signedData);      
      // Parse signatures
      //Pattern p_signature = Pattern.compile("^directory-signature (\\S+) (\\S+)\\s*\n-----BEGIN SIGNATURE-----\n(.*?)-----END SIGNATURE",
      //        Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
      //Matcher m_sig = p_signature.matcher(consensus);
      //while (m_sig.find()) {
      //    byte[] signature = Encoding.parseBase64(m_sig.group());
      //    Logger.logDirectory(Logger.INFO, "Directory.parseDirV3NetworkStatus: Found signature: "+signature);
      //    byte[] identityDigest = Encoding.parseHex(m_sig.group(1));
      //    Logger.logDirectory(Logger.INFO, "Directory.parseDirV3NetworkStatus: Extracted identityDigest: "+identityDigest);
      //    byte[] signingKeyDigest = Encoding.parseHex(m_sig.group(2));
      //    Logger.logDirectory(Logger.INFO, "Directory.parseDirV3NetworkStatus: Extracted signingKeyDigest: "+signingKeyDigest);
      //}
      // Verify signature
      //if (signature.length < 1) throw new TorException("No signature found in network status");
      //else if (!Encryption.verifySignature(signature, signingKeyDigest, signedData))
      //  throw new TorException("Directory signature verification failed");
      
      
      // Parse the single routers
      Pattern p_router = Pattern.compile("^r (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\d+) (\\d+)\\s*\ns ([a-z0-9 ]+)?", 
              Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
      Matcher m = p_router.matcher(consensus);      
      // Loop to extract all routers
      int counter = 0;

      while (m.find()) {
        String nick = m.group(1);
        NetworkStatusDescription sinfo = new NetworkStatusDescription();
        sinfo.nick = nick;
        sinfo.identity_key = Encoding.parseBase64(m.group(2));
        sinfo.digest_descriptor = Encoding.parseBase64(m.group(3));
        //sinfo.last_publication = dateFormat.parse(m.group(4)+" "+m.group(5));
        //sinfo.last_publication = Encoding.parseDate(m.group(4)+" "+m.group(5));
        sinfo.last_publication_str = m.group(4)+" "+m.group(5);
        sinfo.ip = m.group(6);
        sinfo.or_port = Integer.parseInt(m.group(7));
        sinfo.dir_port = Integer.parseInt(m.group(8));
        sinfo.flags = m.group(9);
        //Logger.logDirectory(Logger.RAW_DATA,"Directory.parseDirV3NetworkStatus: Flags of server "+nick+": "+sinfo.flags);
        // FIXME: What happens if two servers are using the same nick ??
        if (!updateServer.containsKey(nick)) {
        	String identityDigest = Encoding.toHexStringNoColon(sinfo.identity_key);
        	String digestDescriptor = Encoding.toHexStringNoColon(sinfo.digest_descriptor);
        	identityDigestToNickname.put(identityDigest, nick);
        	nicknameToDigestDescriptor.put(nick, digestDescriptor);
        	nicknameToIdentityDigest.put(nick, identityDigest);
        	
        	// Careful, this log is a performance drain
          //Logger.logDirectory(Logger.RAW_DATA,"Directory.parseDirV3NetworkStatus: Found server "
           //       +nick+" (digest="+Encoding.toHexString(sinfo.digest_descriptor)+")");
          updateServer.put(nick, sinfo);
          // Count running routers that we are about to learn
          if (sinfo.flags.indexOf("Running")>0) ++counter;
        } else {
          Date storedEntry = ((NetworkStatusDescription)updateServer.get(nick)).getLastPublication();
          if (sinfo.getLastPublication().compareTo(storedEntry)<0) updateServer.put(nick,sinfo);
        }
        // Update the list of directory caches
        if ((sinfo.dir_port>0) && (sinfo.flags.indexOf("Running")>0) && (sinfo.flags.indexOf("V2Dir")>0) && (!dirCaches.contains(nick))) {
          Logger.logDirectory(Logger.RAW_DATA,"Directory.parseDirV3NetworkStatus: Found dir cache: "+nick);
          dirCaches.add(nick);
        }
        // If this server is among torServers, update its status
        Server s = (Server) torServers.get(nick);
        if (s != null) s.updateServerStatus(sinfo.flags);
        
      }
      nodeCounter = counter;
    }    
        
    /**
     * Take a list of NetworkStatusDescriptions and process a number of those to update the list of tor nodes.
     * this includes: batching downloads, downloading full descriptors, parsing and adding them to the list of tor nodes.
     */
    private void updateNetworkStatusV2(HashMap<String, HashMap<String, Object>> v2Servers) throws TorException {
      // store list of servers to be updated in this HashMap
      HashMap<String, NetworkStatusDescription> updateServer = new HashMap<String,NetworkStatusDescription>();
      HashSet<String> dirCaches = new HashSet<String>();
      ++updateCounter;

      // fetch network status from all v2Dirservers
      Iterator<String> i = v2Servers.keySet().iterator();
      Vector<NetworkStatusFetcher> networkStatusFetchers = new Vector<NetworkStatusFetcher>();
      Logger.logDirectory(Logger.VERBOSE,"Directory.updateNetworkStatusV2: start threading");
      while (i.hasNext()) {
        // start all threads
        String nick = (String) i.next();
        HashMap<String, Object> s = v2Servers.get(nick);
        Logger.logDirectory(Logger.INFO,"Directory.updateNetworkStatusV2: getting network status from trusted server "+nick);
        networkStatusFetchers.add(new NetworkStatusFetcher(nick,(String) s.get("ip"),((Integer) s.get("port")).intValue(),
                                                           "GET /tor/status/authority HTTP/1.1\r\nHost: "+nick+"\r\n\r\n",
                                                           Encoding.parseHex((String)s.get("fingerprint"))));
      }
      // join all threads and parse their outputs
      Logger.logDirectory(Logger.VERBOSE,"Directory.updateNetworkStatusV2: collect threads");
      boolean all_finished = false;
      while(!all_finished) {
        all_finished = true;
        for(int j=0;j<networkStatusFetchers.size();++j) {
          NetworkStatusFetcher nsf = null;
          try{
            nsf = (NetworkStatusFetcher)networkStatusFetchers.elementAt(j);
            if (nsf.finished) {
              if (!nsf.extracted) {
                nsf.extracted = true;
                nsf.join();
                if (nsf.exception != null) {
                    Logger.logDirectory(Logger.WARNING,"Directory.updateNetworkStatusV2: stored exception at " + nsf.nick);
                    throw(nsf.exception);
                }
                parseDirV2NetworkStatus(nsf.result, nsf.fingerprint, updateServer,dirCaches);
              }
            } else {
              all_finished = false;
            }
          }
          catch(Exception e) {
            Logger.logDirectory(Logger.WARNING,"Directory.updateNetworkStatusV2: at " + nsf.nick +" > "+ e.getMessage());
            //StackTraceElement[] se = e.getStackTrace();
            //for(int ij=0;ij<se.length;++ij) Logger.logDirectory(Logger.WARNING,"Directory.updateNetworkStatusV2: "+se[ij].toString());
          }
        }
        if (!all_finished) Common.sleep(1);
      }
      Logger.logDirectory(Logger.VERBOSE,"Directory.updateNetworkStatusV2: finished threading");
      fetchDescriptors(updateServer, dirCaches, false);
      // get list of servers to be updated      (DEPRECATED: serial version)
      /*Iterator i = v2Servers.keySet().iterator();
      while (i.hasNext()) {
        try{
          String nick = (String) i.next();
          HashMap s = (HashMap) v2Servers.get(nick);
          Logger.logDirectory(Logger.INFO,"Directory.updateNetworkStatusV2: getting network status from trusted server "+nick);
          // load network status from server
          String networkStatus = StreamsAndHTTP.HTTPRequest((String) s.get("ip"),((Integer) s.get("port")).intValue(),
                              "GET /tor/status/authority HTTP/1.0\r\nHost: "+nick+"\r\n\r\n", TorConfig.dirV2NetworkStatusRequestTimeOut);
          if (networkStatus==null) throw new TorException("null answer from "+nick);
          if (networkStatus.length()<1) throw new TorException("no answer from "+nick);
          if (networkStatus.indexOf(" 200")<0) throw new TorException("http-error from "+nick);
          parseDirV2NetworkStatus(networkStatus,Encoding.parseHex((String)s.get("fingerprint")),updateServer,dirCaches);
        } catch(Exception e) {
           Logger.logDirectory(Logger.WARNING,"Directory.updateNetworkStatusV2: " + e.getMessage());
        }
      }*/
    }

    /**
     * Parse a network status document into a list of torNodes
     */
    private void parseDirV2NetworkStatus(String rawDir,byte[] fingerprint, HashMap<String,NetworkStatusDescription> updateServer, HashSet<String> dirCaches) 
      throws TorException,ParseException
    {
      // check version
      String version = Parsing.parseStringByRE(rawDir,"^network-status-version (\\d+)","");
      if (!version.equals("2")) throw new TorException("wrong network status version");
      // check fingerprint as given in file
      byte[] fingerprintFromServer = Encoding.parseHex(Parsing.parseStringByRE(rawDir,"^fingerprint (\\S+)",""));
      if (!Encoding.arraysEqual(fingerprint,fingerprintFromServer))
        throw new TorException("Directory.parseDirV2NetworkStatus: fingerprint as given in file differs from config");
      // extract dir-signing-key
      RSAPublicKeyStructure dirSigningKey = Encryption.extractRSAKey(Parsing.parseStringByRE(rawDir,"^dir-signing-key\n(.*?END RSA PUBLIC KEY-----)$", ""));
      if (dirSigningKey == null) throw new TorException("Directory.parseDirV2NetworkStatus: couldn't extract dir-signing-key");
      // check if signature belongs to key with given fingerprint
      byte[] pubKeyHash = Encryption.getHash(Encryption.getPKCS1EncodingFromRSAPublicKey(dirSigningKey));
      if (!Encoding.arraysEqual(pubKeyHash,fingerprint)) 
        throw new TorException("Directory.parseDirV2NetworkStatus: public key does not match fingerprint "+
                               "(given "+Encoding.toHexString(pubKeyHash)+" vs expected "+Encoding.toHexString(fingerprint)+")");
      // extract signed data
      byte[] signed_data = Parsing.parseStringByRE(rawDir,"^(network-status.*directory-signature .*?\n)", "").getBytes();
      byte[] directory_signature = Encoding.parseBase64(Parsing.parseStringByRE(
                    rawDir, "^directory-signature .*?\n-----BEGIN SIGNATURE-----\n(.*?)-----END SIGNATURE", ""));
      if (directory_signature.length < 1)
          throw new TorException("no signature found in directory");
      if (!Encryption.verifySignature(directory_signature, dirSigningKey, signed_data))
          throw new TorException("Directory signature verification failed");
 
      // parse out parts of single routers
      Pattern p_router = Pattern.compile("^r (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\d+) (\\d+)\\s*\ns ([a-z0-9 ]+)?", 
              Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
      Matcher m = p_router.matcher(rawDir);
      // loop to extract all routers
      int counter = 0;
      while (m.find()) {
          String nick = m.group(1);
          //System.out.println("parsed info for server "+nick+" ("+ip+") = "+flags);
          NetworkStatusDescription sinfo = new NetworkStatusDescription();
          sinfo.nick = nick;
          sinfo.identity_key = Encoding.parseBase64(m.group(2));
          sinfo.digest_descriptor = Encoding.parseBase64(m.group(3));
          sinfo.last_publication_str = m.group(4)+" "+m.group(5);
          sinfo.ip = m.group(6);
          sinfo.or_port = Integer.parseInt(m.group(7));
          sinfo.dir_port = Integer.parseInt(m.group(8));
          sinfo.flags = m.group(9);
          // check what to do
          if (!updateServer.containsKey(nick)) {
            Logger.logDirectory(Logger.RAW_DATA,"Directory.parseDirV2NetworkStatus: Found server "+nick+" (digest="+Encoding.toHexString(sinfo.digest_descriptor)+")");
            updateServer.put(nick,sinfo);
            // Count those running routers that we are about to learn
            if (sinfo.flags.indexOf("Running")>0) ++counter;
          } else {
            Date storedEntry = ((NetworkStatusDescription)updateServer.get(nick)).getLastPublication();
            if (sinfo.getLastPublication().compareTo(storedEntry)<0) updateServer.put(nick,sinfo);
          }
          // update list of dircaches
          if ( (sinfo.dir_port>0) && (sinfo.flags.indexOf("Running")>0) && (sinfo.flags.indexOf("V2Dir")>0) && (!dirCaches.contains(nick)) )  {
            Logger.logDirectory(Logger.RAW_DATA,"Directory.parseDirV2NetworkStatus: Found dir cache: "+nick);
            dirCaches.add(nick);
          }
          // if server is among torServers, update its status
          Server s = (Server) torServers.get(nick);
          if (s != null) s.updateServerStatus(sinfo.flags);                  
      }
      nodeCounter = counter;
    }    
    
    /**
     * Poll a single directory server (V1) and update information in
     * tor_servers as appropriate
     *
     * @param host
     *            the address of the server
     * @param port
     *            the dir-port of the server
     * @exception IOException
     * @deprecated
     */
    private void pollDirserverV1(String host, int port,String fingerprint) throws IOException {
      try {
        // Connect to dir server
        String dir = StreamsAndHTTP.HTTPRequest(host, port,"GET / HTTP/1.0\r\nHost: "+host+"\r\n\r\n");
        // Parse data
        parseDirServerV1(dir, fingerprint);
      } catch (TorException e) {
        Logger.logDirectory(Logger.WARNING, "Directory.pollDirserverV1(): Error: " + e.getMessage());
        e.printStackTrace();
      }
    }

    /**
     * very central routine for parsing a directory file :-))
     * <ul>
     * <li>checks signature
     * <li>parse recommended software
     * <li>parse router status
     * <li>extract single router descriptions
     * <li>parse server ranking
     * <li>adjust family-sets
     * </ul>
     * 
     * @param rawDir
     *            the directory as given in file or retrieved from directory server
     * @deprecated
     */
    private void parseDirServerV1(String rawDir,String fingerprint) throws TorException {
        if (rawDir.length() < 1) throw new TorException("Directory.parseDirServer: can't parse empty directory");
        // check directory signature ONLY if fingerprint is given. 
        // otherwise this doesn't make sense at all
        if ( (fingerprint !=null) && (fingerprint.length()>0)) {
          // extract public key
          RSAPublicKeyStructure dirSigningKey = Encryption.extractRSAKey(Parsing.parseStringByRE(rawDir,"^dir-signing-key\n(.*?END RSA PUBLIC KEY-----)$", ""));
          if (dirSigningKey == null)
              throw new TorException("Directory.parseDirServer: couldn't extract dir-signing-key");
          // check if signature belongs to key with given fingerprint
          byte[] pubKeyHash = new byte[20];
          pubKeyHash = Encryption.getHash(Encryption.getPKCS1EncodingFromRSAPublicKey(dirSigningKey));
          byte[] compare_to_fingerprint = Encoding.parseHex(fingerprint);
          if (!Encoding.arraysEqual(pubKeyHash,compare_to_fingerprint)) 
            throw new TorException("Directory.parseDirServer: public key does not match fingerprint "+
                                   "(given "+Encoding.toHexString(pubKeyHash)+" vs expected "+Encoding.toHexString(compare_to_fingerprint)+")");
          // extract signed data
          byte[] signed_data = Parsing.parseStringByRE(rawDir,"^(signed-directory.*?directory-signature .*?\n)", "").getBytes();
          byte[] directory_signature = Encoding.parseBase64(Parsing.parseStringByRE(
                        rawDir, "^directory-signature .*?\n-----BEGIN SIGNATURE-----\n(.*?)-----END SIGNATURE", ""));
          if (directory_signature.length < 1)
              throw new TorException("no signature found in directory");
          // verify signature
          if (!Encryption.verifySignature(directory_signature, dirSigningKey,signed_data))
              throw new TorException("Directory signature verification failed");
        }
    
        // parse recommended software
        String stringRecommendedSoftware = Parsing.parseStringByRE(rawDir,"^recommended-software (.*?)$", "");
        Pattern pRecSoft = Pattern.compile("(.*?),", Pattern.UNIX_LINES + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
        Matcher mRecSoft = pRecSoft.matcher(stringRecommendedSoftware);
        while (mRecSoft.find()) {
            recommendedSoftware.add(mRecSoft.group(1));
        }
    
        // Parse other directory items.
        published = dateFormat.parse(Parsing.parseStringByRE(rawDir, "^published (.*?)$", ""), (new ParsePosition(0)));
        String rRankingIndex = Parsing.parseStringByRE(rawDir, "^opt ranking index (.*?)$", "");
    
        // parse router status
        String rStatus = " " + Parsing.parseStringByRE(rawDir, "^router-status (.*?)$", "");
        // add the space to make sure each name has either a leading ' ' or '!'
    
        // parse out parts of single routers
        Pattern p_router = Pattern.compile(Server.regularExpression(),
                Pattern.UNIX_LINES + Pattern.MULTILINE
                        + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
        Matcher m = p_router.matcher(rawDir);
        // loop to extract all routers
        while (m.find()) {
            String description = m.group(1);
            String name = m.group(2);
    
            Server s = receivedServerDescriptor(name,description);

            // set 'alive' and 'trusted'
            boolean alive = false;
            boolean trusted = false;
            int ind = rStatus.indexOf(name + "=");

            if (ind > -1) {
                char preceedingChar = rStatus.charAt(ind - 1);
                if (preceedingChar == ' ') {
                    alive = true;
                    trusted = true;
                } else if (preceedingChar == '!')
                    trusted = true;
            } else if (rStatus.indexOf(" $" + Encoding.toHexString(s.fingerprint) + " ") > -1)
                alive = true;

            s.updateServerStatus(alive,trusted);
        }
        // parse server ranking index
        Pattern p_rank = Pattern.compile("(.*?)=(.*?) ", Pattern.UNIX_LINES
                + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
        Matcher m_rank = p_rank.matcher(rRankingIndex);
    
        while (m_rank.find()) {
            Server s = (Server) torServers.get((String) (m_rank.group(1)));
            try {
                s.rankingIndex = Float.parseFloat(m_rank.group(2));
            } catch (NumberFormatException nf_e) {
                Logger.logDirectory(Logger.WARNING, "Ranking index:"
                        + nf_e.getMessage());
            }
        }
        // convert family in the way, that it only contains _names_ of alive nodes
        adjustFamilySets();
    }
    
    
    ArrayList<DescriptorFetcher> fetchers = new ArrayList<DescriptorFetcher>();
    
    /**
     * Trigger downloading of missing descriptors from directory caches
     */
    private void fetchDescriptors(HashMap<String, NetworkStatusDescription> updateServer, HashSet<String> dirCaches, boolean fastStartup) 
       throws TorException {
    	
    	if (fastStartup) {
    		String[] descriptors = DescriptorCache.getCache();
    		if (descriptors.length > 30) { 
	    		for (int i=0; i<descriptors.length; i++) {
	    			Pattern p_router = Pattern.compile(Server.regularExpression(), Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
	                Matcher m = p_router.matcher(descriptors[i]);
	                // loop to extract all routers
	                while (m.find()) {
	                  String description = m.group(1);
	                  String name = m.group(2);
	                  Server serv = receivedServerDescriptor(name,description);
	                  if (serv!=null) {
	                      NetworkStatusDescription nsd = (NetworkStatusDescription) updateServer.get(name);
	                      if (nsd != null) serv.updateServerStatus(nsd.flags);
	                  }
	                }
	    		}
	    		Logger.logDirectory(Logger.INFO, "Fast staring using " + descriptors.length + " descriptors.");
	    		return;
    		}
    	}
      DescriptorCache d = new DescriptorCache();
      // Check if there are servers to be updated
      if (updateServer.size()==0) throw new TorException("No servers to be updated");
      // Check if there are some directory caches
      if (dirCaches.size()==0) throw new TorException("No directory caches found");
      // Set number of updates to be fetched
      int maxNewEntries = TorConfig.dirV2ReadMaxNumberOfDescriptorsPerUpdate;
      if (updateCounter==1) maxNewEntries = TorConfig.dirV2ReadMaxNumberOfDescriptorsFirstTime;
      // update servers in updateServers from dirCaches 
      // here: sort requests and pick descriptors from 'good' servers (whatever that may be) first
      Vector<NetworkStatusDescription> sortedServers = new Vector<NetworkStatusDescription>();
      Iterator<String> i = updateServer.keySet().iterator();
      int count = 0;
      //while (i.hasNext()) {
      while((count<400)&&(i.hasNext())) {
    	  count++;
        String nick = i.next();
        NetworkStatusDescription s = (NetworkStatusDescription) updateServer.get(nick);
        // straight insertion technique
        int insertPosition = 0;
        for(;insertPosition<sortedServers.size();++insertPosition) {
          NetworkStatusDescription other = (NetworkStatusDescription) sortedServers.elementAt(insertPosition);
          if (s.isBetterThan(other)) break;
        }
        // worse than last entry. append, iff not enough entries in current list
        if(insertPosition<maxNewEntries) sortedServers.add(insertPosition,s);
        if ((count % 100)==0) Logger.logDirectory(Logger.INFO, "At " + count);
      }
      // finally get the updates
      Iterator<String> iDirCaches = dirCaches.iterator();
      int numberOfThreads = Math.min(TorConfig.dirV2ReadMaxNumberOfThreads, maxNewEntries / TorConfig.dirV2DescriptorsPerBatch);
      int runningThreads = numberOfThreads;
      DescriptorFetcher[] fetchThreads = new DescriptorFetcher[numberOfThreads];
      String cacheNick = null;
      for (int j=0; j<numberOfThreads; ++j) {
        fetchThreads[j] = new DescriptorFetcher();
        fetchers.add(fetchThreads[j]);
        // set dirServers
        if (!iDirCaches.hasNext()) iDirCaches = dirCaches.iterator();  
        cacheNick = iDirCaches.next();
        NetworkStatusDescription loadFrom = (NetworkStatusDescription) updateServer.get(cacheNick);
        latestDirCaches.add(loadFrom);
        Logger.logDirectory(Logger.VERBOSE,"Instructing FetchDescriptorThread to use directory cache: "+cacheNick);
        fetchThreads[j].loadFrom = loadFrom;
      }
      int lastRequestedDescriptor = 0;
      // run until all descriptors got chance to be fetched
      while(runningThreads > 0) {
    	  if (stop) return;
        // go through all threads, assign job if they idle, collect results if they are done
        for (int j=0; j<numberOfThreads; ++j) {
          // collect results
          if (fetchThreads[j].resolved) {
            Logger.logDirectory(Logger.VERBOSE,"Got descriptor for: "+fetchThreads[j].nicks);
            
            // Cache descriptor
             d.addToCache(fetchThreads[j].descriptor);
            
            Pattern p_router = Pattern.compile(Server.regularExpression(), Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
            Matcher m = p_router.matcher(fetchThreads[j].descriptor);
            // loop to extract all routers
            while (m.find()) {
              String description = m.group(1);
              String name = m.group(2);
              Server serv = receivedServerDescriptor(name,description);
              // successful parsing? => no more reloading retries
              if (serv!=null) {
                fetchThreads[j].reloadRetries=0;
                NetworkStatusDescription nsd = (NetworkStatusDescription) updateServer.get(name);
                serv.updateServerStatus(nsd.flags);
              }
            }
            fetchThreads[j].resolved = false;
            fetchThreads[j].idle = true;
          }
          // retry, if failed
          if (fetchThreads[j].failed) 
            if (fetchThreads[j].reloadRetries < TorConfig.dirV2ReloadRetries) {
            Logger.logDirectory(Logger.VERBOSE, "Failed getting descriptor for: "+fetchThreads[j].nicks+" from "+fetchThreads[j].loadFrom.nick);
            fetchThreads[j].reloadRetries++;  
            // choose next dir-cache in a round-robin fashion
            if (!iDirCaches.hasNext()) iDirCaches = dirCaches.iterator();  
            cacheNick = (String)iDirCaches.next();
            NetworkStatusDescription loadFrom = (NetworkStatusDescription) updateServer.get(cacheNick);
            Logger.logDirectory(Logger.VERBOSE, "Instructing FetchDescriptorThread to load from "+cacheNick+" descriptors for "+fetchThreads[j].nicks);
            fetchThreads[j].loadFrom = loadFrom;
            fetchThreads[j].failed = false;
            synchronized(fetchThreads[j]) {fetchThreads[j].notify();}
          } else {
            Logger.logDirectory(Logger.VERBOSE, "All attempts failed to fetch descriptor for "+fetchThreads[j].nicks);
            fetchThreads[j].failed = false;
            fetchThreads[j].idle = true;
          }
          // assign jobs, if idle
          if (fetchThreads[j].idle){
            StringBuffer nodesDigests = new StringBuffer();
            StringBuffer nicks = new StringBuffer();
            if (lastRequestedDescriptor < sortedServers.size()) {
              int k = lastRequestedDescriptor + TorConfig.dirV2DescriptorsPerBatch;
              for (;(lastRequestedDescriptor<k)&&(lastRequestedDescriptor<sortedServers.size()); ++lastRequestedDescriptor) {
                NetworkStatusDescription s = (NetworkStatusDescription) sortedServers.elementAt(lastRequestedDescriptor);
                nodesDigests.append(Encoding.toHexStringNoColon(s.digest_descriptor)); 
                nicks.append(" " + s.nick);
              }
              Logger.logDirectory(Logger.VERBOSE, "Instructing FetchDescriptorThread to load from "+fetchThreads[j].loadFrom.nick+" descriptors for "+nicks);
              fetchThreads[j].reloadRetries = 0;
              fetchThreads[j].nicks = nicks;
              fetchThreads[j].nodesDigests = nodesDigests;
              fetchThreads[j].idle = false;
              synchronized(fetchThreads[j]) {fetchThreads[j].notify();}
            } else {
              fetchThreads[j].stopped = true;
              runningThreads--;
            }
          }            
        }
        // sleep is good
        Common.sleep(FETCH_THREAD_QUERY_TIME);
      }
      // terminate all fetch-threads
      for (int j=0; j<numberOfThreads; ++j) {
        try {
          fetchThreads[j].stopped = true; // make sure they don't run
          fetchThreads[j].join(); 
        } catch (InterruptedException e) {}
      }        
    }
    
    /**
     * checks wether the given circuit is compatible to the given restrictions
     * 
     * @param circ
     *            a circuit
     * @param sp
     *            the requirements to the route
     * @param forHiddenService
     * @return the boolean result
     */
    boolean isCompatible(Circuit circ, TCPStreamProperties sp,
            boolean forHiddenService) throws TorException {
        Server[] route_copy = new Server[circ.route.length];
        for (int i = 0; i < circ.route.length; ++i)
            route_copy[i] = circ.route[i].server;
        if (forHiddenService) {
            // it is not allowed to use circuits that have already been used for
            // smth else
            if (circ.streamHistory == null)
                if ((circ.streams == null)
                        || ((circ.streams.size() == 1) && (circ.sd != null) && (sp.hostname
                                .indexOf(circ.sd.getURL()) > -1)))
                    return isCompatible(route_copy, sp, forHiddenService);
        } else {
            // check for exit policies of last node
            if ((circ.sd == null) && (circ.myHSProperties == null))
                return isCompatible(route_copy, sp, forHiddenService);
        }
        return false;
    }

    /**
     * Check whether the given route is compatible to the given restrictions
     * 
     * @param route
     *            a list of servers that form the route
     * @param sp
     *            the requirements to the route
     * @param forHiddenService
     *            set to TRUE to disregard exitPolicies
     * @return the boolean result
     */
    boolean isCompatible(Server[] route, TCPStreamProperties sp, boolean forHiddenService) throws TorException {
        // check for null values
        if (route == null)
            throw new TorException("received NULL-route");
        if (sp == null)
            throw new TorException("received NULL-sp");
        if (route[route.length - 1] == null)
            throw new TorException("route contains NULL at position "
                    + (route.length - 1));
        // empty route is always wrong
        if (route.length < 1)
            return false;
        // route is too short
        if (route.length < sp.getMinRouteLength())
            return false;
        // route is too long
        if (route.length > sp.getMaxRouteLength())
            return false;

        // check compliance with sp.route
        String[] proposed_route = sp.getProposedRoute();
        if (proposed_route != null) {
            for(int i=0 ; (i<proposed_route.length)&&(i<route.length)  ;++i)
                if (proposed_route[i]!=null)
                    if (! route[i].nickname.equalsIgnoreCase(proposed_route[i]))
                        return false;
        }
        
        if ((!forHiddenService) && (sp.exitPolicyRequired)) {
            // check for exit policies of last node
            return route[route.length - 1].exitPolicyAccepts(sp.addr, sp.port);
        } else
            return true;
    }

    /**
     * Exclude related nodes: family, class C and country (if specified in TorConfig)
     *
     * @param s node that should be excluded with all its relations
     * @return set of excluded node names
     */
    HashSet<String> excludeRelatedNodes(Server s){

            HashSet<String> excluded_server_names = new HashSet<String>();
            HashSet<String> myAddressNeighbours, myCountryNeighbours;
            
            if(TorConfig.route_uniq_class_c){
                       myAddressNeighbours = getAddressNeighbours(s.address.getHostAddress());
                       if (myAddressNeighbours != null) excluded_server_names.addAll(myAddressNeighbours);
            } else
                   excluded_server_names.add(s.nickname);    
             // excluse all country insider, if desired
            if(TorConfig.route_uniq_country){
                   myCountryNeighbours = (HashSet<String>) countryNeighbours.get(s.countryCode);
                   if (myCountryNeighbours != null)
                     excluded_server_names.addAll(myCountryNeighbours);
            }
            // exclude its family as well
            excluded_server_names.addAll(s.family);
  
            return excluded_server_names;
    }

    /**
     * returns a route through the network as specified in
     * 
     * @see TCPStreamProperties
     * 
     * @param sp
     *            tcp stream properties
     * @return a list of servers
     */
    Server[] createNewRoute(TCPStreamProperties sp) throws TorException {
        // are servers available?
        if (torServers.size() < 1)
            throw new TorException("directory is empty");

        // use length of route proposed by TCPStreamProperties
        int minRouteLength = sp.getMinRouteLength();
        int len;

        // random value between min and max route length
        len = minRouteLength
                + rnd.nextInt(sp.getMaxRouteLength() - minRouteLength + 1);

        // choose random servers to form route
        Server[] route = new Server[len];

        HashSet<String> excluded_server_names = new HashSet<String>();
        // take care, that none of the specified proposed servers is selected
        // before in route
        String[] proposed_route = sp.getProposedRoute();
        if (proposed_route != null)
            for (int j = 0; j < proposed_route.length; ++j)
                if (proposed_route[j] != null) {

                    Server s = (Server) torServers.get(proposed_route[j]);
                    if (s != null) 
                        excluded_server_names.addAll(excludeRelatedNodes(s));
                }

        return createNewRoute(sp, proposed_route, excluded_server_names, route, len-1, -1);
    }

   /**
     * returns a route through the network as specified in
     * 
     * @see TCPStreamProperties
     * 
     * @param sp tcp stream properties
     * @param propoused_route   array of routers that were proposed by tcp
     *                           stream properties
     * @param excluded_server_names selfexplained
     * @param route current route array
     * @param i index in array route up to which the route has to be built
     * @return a list of servers
     */
    synchronized private Server[] createNewRoute(TCPStreamProperties sp, String[] proposed_route, HashSet<String> excluded_server_names, Server[] route, int i, int maxIterations)
         throws TorException{

        float p = sp.getRankingInfluenceIndex();
        HashSet<String> previous_excluded_server_names = new HashSet<String>();

             Integer allowedCircuitsWithNode;

             Iterator<String> serverNameIt = (torServers.keySet()).iterator();
             while (serverNameIt.hasNext()) {
                String serverName = serverNameIt.next();
                Server s = (Server) torServers.get(serverName);
                allowedCircuitsWithNode = (Integer) FirstNodeHandler.currentlyUsedNodes.get(s.nickname);
                // exit server must be trusted
                if ((allowedCircuitsWithNode != null ) && (allowedCircuitsWithNode.intValue() > TorConfig.allow_node_multiple_circs))
                   excluded_server_names.add(s.nickname);
               }


            if ((proposed_route != null) && (i < proposed_route.length) && (proposed_route[i] != null)) {
                // choose proposed server
                route[i] = (Server) torServers.get(proposed_route[i]);
                if (route[i] == null)
                    throw new TorException("couldn't find server " + proposed_route[i] + " for position " + i);
            } else {

                if (i == route.length-1) { // the last router has to accept exit
                    // policy

                    serverNameIt = (torServers.keySet()).iterator();
                    HashSet<String> suitable_server_names = new HashSet<String>(); // suitable
                    // servers

                    while (serverNameIt.hasNext()) {
                        String serverName = serverNameIt.next();
                        Server s = (Server) torServers.get(serverName);
                        // exit server must be trusted
                        if (s.exitPolicyAccepts(sp.addr, sp.port)
                                && (sp.allowUntrustedExit || s.dirv2_exit))
                            suitable_server_names.add(serverName);
                    }

                    HashSet<String> x = new HashSet<String>(torServers.keySet());
                    x.removeAll(suitable_server_names);
                    x.addAll(excluded_server_names);
                    // now select one of them

                    route[i] = selectRandomNode(torServers, x, p);

                } else if ((i == 0) && (!sp.allowNonGuardEntry)){ // entry node must be guard
                        
                    serverNameIt = (torServers.keySet()).iterator();
                    HashSet<String> suitable_server_names = new HashSet<String>(); // suitable
                    // servers

                    while (serverNameIt.hasNext()) {
                        String serverName = (String) serverNameIt.next();
                        Server s = (Server) torServers.get(serverName);
                        // entry server must be guard
                        if (s.dirv2_guard)
                            suitable_server_names.add(serverName);
                    }

                    HashSet<String> x = new HashSet<String>(torServers.keySet());
                    x.removeAll(suitable_server_names);
                    x.addAll(excluded_server_names);
                    // now select one of them
                    route[i] = selectRandomNode(torServers, x, p);

                }
                else route[i] = selectRandomNode(torServers, excluded_server_names, p);

                if (route[i] == null)
                      return null;
                previous_excluded_server_names.addAll(excluded_server_names);
                excluded_server_names.addAll(excludeRelatedNodes(route[i]));

                int numberOfNodeOccurances;
                allowedCircuitsWithNode = (Integer) FirstNodeHandler.currentlyUsedNodes.get(route[i].nickname);
                if (allowedCircuitsWithNode != null)
                  numberOfNodeOccurances = allowedCircuitsWithNode.intValue() + 1;
                else
                  numberOfNodeOccurances = 0;
                FirstNodeHandler.currentlyUsedNodes.put(route[i].nickname, numberOfNodeOccurances);
            }

        if (i>0){ 
                Server[] a_route = createNewRoute(sp, proposed_route, excluded_server_names, route, i-1, -1);
                if (a_route == null) {
                      
                      previous_excluded_server_names.add(route[i-1].nickname);
                      if (maxIterations > -1)
                              maxIterations = Math.min (maxIterations, RETRIES_ON_RECURSIVE_ROUTE_BUILD) - 1;
                      else maxIterations = RETRIES_ON_RECURSIVE_ROUTE_BUILD - 1;
                      
                      if (maxIterations < 0) return null;
                      route = createNewRoute(sp, proposed_route, previous_excluded_server_names, route, i, maxIterations); 
                      
                } else route = a_route;
        }
         
                
        return route;

        // sanity check - leave away, since this is a new route anyway
        /*if (!isCompatible(route, sp, false))
            throw new TorException(
                    "Directory.createNewRoute(): Route is not compatible with TCPStreamProperties..");
        */
    }

    /** TODO: add doc: when is this used? */
    Server selectRandomNode(float p) {
        return selectRandomNode(torServers, new HashSet<String>(), p);
    }

    /** TODO: add doc: when is this used? */
    private Server selectRandomNode(ConcurrentHashMap<String, Server> tor_servers, HashSet<String> excluded_server_names, float p) {
      float rankingSum = 0;
      Server myServer;
      excluded_server_names.addAll(excludedNodesByConfig);
      // At first, calculate sum of the rankings
      Iterator<Server> it = tor_servers.values().iterator();
      while (it.hasNext()) {
        myServer = it.next();
        if ((!excluded_server_names.contains(myServer.nickname)) && myServer.dirv2_running)
           rankingSum += myServer.getRefinedRankingIndex(p);
      }
      // generate a random float between 0 and rankingSum
      float serverRandom = rnd.nextFloat() * rankingSum;
      // select the server
      it = tor_servers.values().iterator();
      while (it.hasNext()) {
        myServer = it.next();
        if ((!excluded_server_names.contains(myServer.nickname)) && myServer.dirv2_running) {
          serverRandom -= myServer.getRefinedRankingIndex(p);
          if (serverRandom <= 0) return myServer;
        }
      }
      Logger.logDirectory(Logger.INFO, "Returning null from selectRandomNode size " + tor_servers.size());
      return null;
    }

    /**
     * restores circuit from the failed node route[failedNode]
     * 
     * @param sp
     *            tcp stream properties
     * @param route
     *            existing route
     * @param failedNode
     *            index of node in route, that failed
     * @return a route
     */
    Server[] restoreCircuit(TCPStreamProperties sp, Server[] route,
            int failedNode) throws TorException {

        // used to build the custom route up to the failed node
        String[] customRoute = new String[route.length];

        // if TCPStreamProperties are NA, create a new one
        if (sp == null)
            sp = new TCPStreamProperties();

        // customize sp, so that createNewRoute could be used to do the job

        sp.setMinRouteLength(route.length); // make sure we build circuit of the same length
        sp.setMaxRouteLength(route.length); // it used to be

        sp.setRankingInfluenceIndex(1.0f); // make sure now to select with
        // higher prob. reliable servers

        route[failedNode].punishRanking(); // decreasing ranking of the failed one

        // reuse hosts that are required due to TCPStreamProperties
        if (sp.route!=null)
          for( int i=0; (i<sp.route.length)&&(i<customRoute.length) ; ++i)
             customRoute[i] = sp.route[i];
        // reuse hosts that were reported to be working
        for (int i = 0; i < failedNode; ++i)
            customRoute[i] = new String(route[i].nickname);

        sp.setCustomRoute(customRoute);

        try {
            route = createNewRoute(sp);

        } catch (TorException te) {
            Logger.logDirectory(Logger.WARNING,
                    "Directory.restoreCircuit: failed");
        }

        return route;

    }

    /**
     * returns the server with a specific nickname
     * 
     */
    Server getByName(String nickname) {
        // Do we already have the descriptor
        if (torServers.containsKey(nickname)) return torServers.get(nickname);
        
        // No? Try to get from directory cache then
        
        // Get the digest descriptor
        String digest = null;
        if (nicknameToDigestDescriptor.containsKey(nickname)) digest = nicknameToDigestDescriptor.get(nickname);
        else return null;
        
         
        DescriptorFetcher f = new DescriptorFetcher();
        f.nodesDigests = new StringBuffer();
        f.nodesDigests.append(digest);
        f.nicks = new StringBuffer();
        f.nicks.append(" " + nickname);
        int currentDirCache = 0;
        f.loadFrom = latestDirCaches.get(currentDirCache);
        f.idle = false;
        synchronized(f) {f.notify();}
        while (true) {
                if (f.resolved) {
                        f.stopped = true;
                        Logger.logDirectory(Logger.VERBOSE,"Got descriptor for: "+f.nicks);
                        Pattern p_router = Pattern.compile(Server.regularExpression(), Pattern.UNIX_LINES + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
                        Matcher m = p_router.matcher(f.descriptor);
                        // loop to extract all routers
                        while (m.find()) {
                          String description = m.group(1);
                          String name = m.group(2);
                          Server serv = receivedServerDescriptor(name,description);
                          // successful parsing? => no more reloading retries
                          if (serv!=null) {
                            torServers.put(serv.nickname, serv);
                          }
                          return serv;
                        }
                } else if (f.failed){
                        currentDirCache++;
                        if ((currentDirCache < 20)&&(latestDirCaches.size() > currentDirCache)) {
                                f.loadFrom = latestDirCaches.get(currentDirCache);
                                f.failed = false;
                                synchronized(f) {f.notify();}
                        } else {
                                f.stopped = true;
                                return null;
                        }
                }
                synchronized(f) {f.notify();}
                Common.sleep(2000); // 2 seconds
        }
    }


    /**
     * returns the server with the specific identity digest
     * @param identityDigest string of hex of the identity digest. May begin with $
     * @return a tor server
     */
    String getNicknameByIdentityDigest(String identityDigest) {
        if (identityDigest.startsWith("$")) identityDigest = identityDigest.substring(1);
        return identityDigestToNickname.get(identityDigest.toLowerCase());
    }
    
    /**
     * returns the identity digest of the server with the specified nickname 
     * @param nickname the server
     * @return the identity digest as a string of hex (without preceeding '$')
     */
    String getIdentityDigestByNickname(String nickname) {
    	return nicknameToIdentityDigest.get(nickname);
    }

    /**
     * convert server family sets in the way that they only contain _names_ of
     * alive nodes
     * 
     */
    private void adjustFamilySets() {

        Server s, s2;
        boolean notFound;
        String serverEntry;
        HashSet<String> familySet;
        Iterator<String> familyIt;
        Iterator<Server> sIt;
        Iterator<Server> serverIt = torServers.values().iterator();

        while (serverIt.hasNext()) {

            s = serverIt.next();

            familySet = new HashSet<String>(s.family);
            s.family.clear();

            familyIt = familySet.iterator();

            while (familyIt.hasNext()) {

                serverEntry = familyIt.next();

                switch (serverEntry.charAt(0)) {

                case '!':
                    break; // currently not alive
                case '$': // digest, has to be resolved

                    // TODO use second HashMap not to search each time
                    notFound = true;
                    sIt = torServers.values().iterator();
                    while (sIt.hasNext() && notFound) {

                        s2 = (Server) sIt.next();
                        if ((serverEntry.substring(1)).equalsIgnoreCase(Encoding
                                .toHexString(s2.fingerprint))) {
                            s.family.add(s2.nickname);
                            notFound = false;
                        }
                    }
                    break;
                default: // should be name
                    s.family.add(serverEntry);
                }
            }
        }
    }

    /**
     * Return the set of neighbors by address of the specific IP in the dotted notation
     */
    private HashSet<String> getAddressNeighbours(String address){
          String ipClassCString = Parsing.parseStringByRE(address, "(.*)\\.", "");
          HashSet<String> neighbours = (HashSet<String>)  addressNeighbours.get(ipClassCString);
          return neighbours;
    }

    /**
     * should be called when TorJava is closing
     */
    void close(String dirFile) {
    	stop = true;
        writeDirectoryToFile(dirFile);
        for (int i=0; i<fetchers.size(); i++) {
        	DescriptorFetcher f = fetchers.get(i);
        	f.close();
        }
    }

    /**
     * for debugging purposes
     */
    void print() {
      Iterator<Server> i = (torServers.values()).iterator();
      while (i.hasNext()) {
        Server s = i.next();
        Logger.logDirectory(Logger.VERBOSE, s.print());
      }
    }

    // used to store server descriptors from a dir-spec v2 network status document
    class NetworkStatusDescription implements Serializable {
      /**
		 * 
		 */
		private static final long serialVersionUID = 4503028273477804837L;
	String nick;
      byte[] identity_key;
      byte[] digest_descriptor;
      private Date last_publication_date = null;
      String last_publication_str;
      String ip;
      int or_port,dir_port;
      String flags;

      public Date getLastPublication() {
    	  if (last_publication_date == null) {
    		  last_publication_date = Encoding.parseDate(last_publication_str);
    	  }
    	  return last_publication_date;
      }
      
      /**
       * we have to judge from the server's flags which of the both should be
       * downloaded rather than the other. MAYBE one or both of them are already in 
       * the HasMap this.torServers, but we can't rely on that.<br>
       * The flags are stored in the member variable "flags" and are currently:<br>
       * <tt>Authority, Exit, Fast, Guard, Named, Stable, Running, Valid, V2Dir</tt>
       *
       * @param other the other descriptor, to which we compare this descriptor
       * @return true, if this one is better to download
       */
      boolean isBetterThan(NetworkStatusDescription other) {
        // do a fixed prioritizing: Running, Authority, Exit, Guard, Fast, Stable, Valid
        if ( (flags.indexOf("Running")>=0)   && (other.flags.indexOf("Running")<0) )   return true;
        if ( (other.flags.indexOf("Running")>=0)   && (flags.indexOf("Running")<0) )   return false;
        if ( (flags.indexOf("Authority")>=0) && (other.flags.indexOf("Authority")<0) ) return true;
        if ( (other.flags.indexOf("Authority")>=0) && (flags.indexOf("Authority")<0) ) return false;
        if ( (flags.indexOf("Exit")>=0)      && (other.flags.indexOf("Exit")<0) )      return true;
        if ( (other.flags.indexOf("Exit")>=0)      && (flags.indexOf("Exit")<0) )      return false;
        if ( (flags.indexOf("Guard")>=0)     && (other.flags.indexOf("Guard")<0) )     return true;
        if ( (other.flags.indexOf("Guard")>=0)     && (flags.indexOf("Guard")<0) )     return false;
        if ( (flags.indexOf("Fast")>=0)      && (other.flags.indexOf("Fast")<0) )      return true;
        if ( (other.flags.indexOf("Fast")>=0)      && (flags.indexOf("Fast")<0) )      return false;
        if ( (flags.indexOf("Stable")>=0)    && (other.flags.indexOf("Stable")<0) )    return true;
        if ( (other.flags.indexOf("Stable")>=0)    && (flags.indexOf("Stable")<0) )    return false;
        if ( (flags.indexOf("Valid")>=0)     && (other.flags.indexOf("Valid")<0) )     return true;
        if ( (other.flags.indexOf("Valid")>=0)     && (flags.indexOf("Valid")<0) )     return false;
        // finally - all (important) flags seem to be equal..
        // download the one, that is fresher?
        getLastPublication();
        Date other_last_publication = other.getLastPublication();
        if ( last_publication_date.compareTo(other_last_publication)<0 ) return true;
        if ( last_publication_date.compareTo(other_last_publication)>0 ) return false;
        // choose by random
        if (rnd!=null) return rnd.nextBoolean();
        // say no, because experience tells that dir-servers tend to list important stuff first
        return false;
      }
    }
}

/**
 * This class is used to fetch the tor network-status from each server in parallel
 */
class NetworkStatusFetcher extends Thread {
  String nick;
  String ip;
  int port;
  String url;
  byte[] fingerprint;

  String result=null;
  boolean finished = false;
  boolean extracted = false;
  Exception exception=null;
  
  NetworkStatusFetcher(String nick,String ip,int port,String url,byte[] fingerprint) {
    this.nick = nick;
    this.ip = ip;
    this.port = port;
    this.url = url;
    this.fingerprint = fingerprint;
    this.setName("NetworkStatusFetcher");
    this.start();
  }
  
  public void run() {
    try{
      result = StreamsAndHTTP.HTTPRequest(this.ip, this.port, this.url, TorConfig.dirV2NetworkStatusRequestTimeOut);
      if (result==null) throw new TorException("null answer from "+nick);
      if (result.length()<1) throw new TorException("no answer from "+nick);
      if (result.indexOf(" 200")<0) throw new TorException("http-error from "+nick);
    }
    catch(Exception e) {
      exception = e;
    }
    finished = true;
  }
}

/**
 * Descriptor-Fetcher Class. Implements a separate thread
 * and fetches the descriptor for the given node
 */
class DescriptorFetcher extends Thread {
  boolean stopped = false;
  boolean resolved = false;
  boolean failed = false;
  boolean idle = true;
  int reloadRetries = 0;
  String descriptor;
  StringBuffer nodesDigests, nicks;
  Directory.NetworkStatusDescription loadFrom;
  

  DescriptorFetcher() {
	this.setName("DescriptorFetcher");
    this.start();
  }

  /**
   * keep up to date with the directory informations
   */
  private void fetch_descriptor() {
    // Download descriptor
    StringBuffer strHTTPGet = new StringBuffer("GET /tor/server/d/");
    strHTTPGet.append(nodesDigests);
    strHTTPGet.append(" HTTP/1.1\r\nHost: "+loadFrom.nick+"\r\n\r\n");
    try {
      descriptor = StreamsAndHTTP.HTTPRequest(loadFrom.ip, loadFrom.dir_port, strHTTPGet.toString(), TorConfig.dirV2ReloadTimeout * 1000);
      int iHTTP200pos = descriptor.indexOf(" 200");
      if ((iHTTP200pos>=0) && (iHTTP200pos <= 15)) {
        resolved = true;
      } else {
        failed = true;
        descriptor = Parsing.parseStringByRE(descriptor,"^(HTTP.*?)\n",descriptor);
        throw new TorException("response '"+descriptor+"' asking for descriptors of " + nicks);
      }
    } catch(Exception e) {
      Logger.logDirectory(Logger.VERBOSE, "Directory.DirectoryFetcherThread: Loading node descriptions from "+loadFrom.nick+": "+e.getMessage());
      failed = true;
    }
  }

  public void run() {
    // run until killed
	  this.setPriority(3); // Lower priority for fetcher thread
    while (!stopped) {
      try {
        synchronized (this){ this.wait(); }
        if ((!idle) && (!resolved) && (!failed)) 
          fetch_descriptor();
         // wait
        //  sleep(Directory.FETCH_THREAD_IDLE_TIME);
      } catch (Exception e) {
      }
    }
  }
  
  public void close() {
	  stopped = true;
	  this.interrupt();
  }
};
// vim: et
