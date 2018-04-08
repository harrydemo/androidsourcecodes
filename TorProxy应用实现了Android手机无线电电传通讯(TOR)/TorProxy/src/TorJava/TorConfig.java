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
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TorJava.Common.Parsing;
import TorJava.Common.StreamsAndHTTP;

/**
 * used for global configuration - stuff
 * 
 * @author Lexi
 * @author Michael Koellejan
 * @author Andriy Panchenko
 */
public class TorConfig {
    HashMap<String, HashMap<String, Object>> trustedServers;

    // functionality
    static String logFileName = null;
    static int cacheMaxAgeSeconds = 24 * 3600;
    static int startupDelaySeconds = 15;
    static int dirserver_port = 0; // for running an own directory server
    static int or_port = 0; // for running an own onion router
    String nickname = "TorJava"; // myself

    // Static final/utility variables
    public static final String TORJAVA_VERSION_STRING = "TorJava 0.1a";

    // QoS-parameters
    static int retriesConnect = 5;
    
    static int reconnectCircuit = 3;
    static int retriesStreamBuildup = 5;

    static int defaultIdleCircuits = 5;

    static int queueTimeoutCircuit = 40;
    static int queueTimeoutResolve = 20;
    static int queueTimeoutStreamBuildup = 10;

    static int circuitClosesOnFailures = 3;
    static int circuitsMaximumNumber = 15; 

    static float rankingTransferPerServerUpdate = 0.95f; // 0..1

    static boolean veryAggressiveStreamBuilding = false; // this is a truely asocial way of building streams!!

    // directory parameters
    static int intervalDirectoryV1Refresh = 30; // in minutes longer, since it updates the complete directory at once
    static int intervalDirectoryRefresh = 2;    // in minutes
    static int dirV2ReadMaxNumberOfDescriptorsFirstTime = 180;  // set to <=0 to read all
    static int dirV2ReadMaxNumberOfDescriptorsPerUpdate = 80;  // set to <=0 to read all
    static int dirV2ReadMaxNumberOfThreads = 3; 
    static int dirV2ReloadRetries = 3;   // per descriptor
    static int dirV2ReloadTimeout = 10;   // in seconds
    static int dirV2DescriptorsPerBatch = 1;
    static int dirV2NetworkStatusRequestTimeOut = 60000; // millisecond

    // QoS-parameter, see updateRanking in Circuit.java 
    static final int CIRCUIT_ESTABLISHMENT_TIME_IMPACT = 5;

    // Security parameters
    static int streamsPerCircuit = 50;
    static float rankingIndexEffect = 0.9f; // see Server.getRefinedRankingIndex

    // Path length
    static int route_min_length = 3;
    static int route_max_length = 3;
    
    // Don't establish any circuits until a certain part of 
    // the descriptors of running routers is present 
    static float min_percentage = 1;
    // Wait at most until this number of descriptors is known
    static int min_descriptors = route_min_length;
    
    // True if there shouldn't be two class C addresses on the route
    static boolean route_uniq_class_c = true;
    // True if there should be at most one router from one country 
    // (or block of countries) on the path
    static boolean route_uniq_country = false;
    // Allow a single node to be present in multiple circuits
    static int allow_node_multiple_circs = 3;

    static HashSet<String> avoidedCountries; 
    static HashSet<String> avoidedNodes;

    // Time intervals of gui updates in ms
    public static int guiUpdateIntervalMilliSeconds = 3000;
    public static boolean guiDisplayNodeNames = false;
    public static String guiCountryOfUser = "EU";

    // Filenames
    private static final String TOR_CONFIG_FILENAME = "torrc";
    private static final String TOR_CACHE_FILENAME = "cached-directory";
    static final String TOR_STABLE_DIR_FILENAME = "data/directory-stable";
    static final String TOR_GEOIPCITY_FILENAME = "data/GeoLiteCity.dat";

    // PROXY-Stuff
    // FIXME: is public (and in fact: complete class is public) only to be accessed from HTTPConnection
    public static Vector<String> setFilteredHeaders;
    public static Vector<String[]> setReplaceHeaders;

    public static int portWWWProxy = 8080;
    public static int portSocksProxy = 1080;

    private static String filename;

    /**
     * @param readFileName set to false to avoid any access to the lcoal file system
     */
    TorConfig(boolean readFileName) {
        if (readFileName) 
            init(getConfigDir() + TOR_CONFIG_FILENAME);
        else
            init(null);
    }

    TorConfig(String filename) {
        init(filename);
    }

    void reload() {
        if (filename == null) return;
        Logger.logGeneral(Logger.INFO,"TorConfig.reload: reloading config-file "+filename);
        init(filename);
    }

    private void init(String filename) {
        // init trusted dirctory servers
        trustedServers = new HashMap<String, HashMap<String, Object>>();
        // init set of avoided nodes, countries
        avoidedCountries = new HashSet<String>();
        avoidedNodes = new HashSet<String>();
        // init set of filtered HTTP-headers
        setFilteredHeaders = new Vector<String>();
        setReplaceHeaders = new Vector<String[]>();
        // read everything else from config
        readFromConfig(filename);
        // set filename, such that file can be reloaded
        TorConfig.filename = filename;
    }

    public void close() {
        writeToFile("/tmp/torrc.test");
    }

    /** should be called in case there are no trusted servers in the config-file given */
    private void addDefaultTrustedServers() {
      // Deprecated: V2-servers, only for fallback
      HashMap<String,Object> dirServer = new HashMap<String,Object>();

      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(2));
      dirServer.put("ip", "194.109.206.212");
      dirServer.put("port", new Integer(80));
      dirServer.put("fingerprint", "7E:A6:EA:D6:FD:83:08:3C:53:8F:44:03:8B:BF:A0:77:58:7D:D7:55");
      trustedServers.put("dizum", dirServer);

      // V3-servers
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "128.31.0.34");
      dirServer.put("port", new Integer(9031));
      dirServer.put("fingerprint", "FF:CB:46:DB:13:39:DA:84:67:4C:70:D7:CB:58:64:34:C4:37:04:41");
      trustedServers.put("moria1", dirServer);
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "128.31.0.34");
      dirServer.put("port", new Integer(9032));
      dirServer.put("fingerprint", "71:9B:E4:5D:E2:24:B6:07:C5:37:07:D0:E2:14:3E:2D:42:3E:74:CF");
      trustedServers.put("moria2", dirServer);
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "86.59.21.38");
      dirServer.put("port", new Integer(80));
      dirServer.put("fingerprint", "84:7B:1F:85:03:44:D7:87:64:91:A5:48:92:F9:04:93:4E:4E:B8:5D");
      trustedServers.put("tor26", dirServer);      
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "82.94.251.206");
      dirServer.put("port", new Integer(80));
      dirServer.put("fingerprint", "4A:0C:CD:2D:DC:79:95:08:3D:73:F5:D6:67:10:0C:8A:58:31:F1:6D");
      trustedServers.put("Tonga", dirServer);
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "216.224.124.114");
      dirServer.put("port", new Integer(9030));
      dirServer.put("fingerprint", "F3:97:03:8A:DC:51:33:61:35:E7:B8:0B:D9:9C:A3:84:43:60:29:2B");
      trustedServers.put("ides", dirServer);
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "80.190.246.100");
      dirServer.put("port", new Integer(80));
      dirServer.put("fingerprint", "68:33:3D:07:61:BC:F3:97:A5:87:A0:C0:B9:63:E4:A9:E9:9E:C4:D3");
      trustedServers.put("gabelmoo", dirServer);
      
      dirServer = new HashMap<String,Object>();
      dirServer.put("version", new Integer(3));
      dirServer.put("ip", "213.73.91.31");
      dirServer.put("port", new Integer(80));
      dirServer.put("fingerprint", "7B:E6:83:E6:5D:48:14:13:21:C5:ED:92:F0:75:C5:53:64:AC:71:23");
      trustedServers.put("dannenberg", dirServer);
    }

    private String replaceSpaceWithSpaceRegExp(String regexp) {
      return regexp.replaceAll(" ","\\\\s+");
    }
    private int parseInt(String config,String name,int myDefault) {
      int x = Integer.parseInt(Parsing.parseStringByRE(config,"^\\s*"+replaceSpaceWithSpaceRegExp(name)+"\\s+(\\d+)",Integer.toString(myDefault)));
      Logger.logGeneral(Logger.RAW_DATA,"TorConfig.parseInt: Parsed '"+name+"' as '"+x+"'");
      return x;
    }
    private String writeInt(String name,int value) {
      return name + " " + value + "\n";
    }
    /*private float parseFloat(String config,String name,float myDefault) {
      float x = Float.parseFloat(Parsing.parseStringByRE(config,"^\\s*"+replaceSpaceWithSpaceRegExp(name)+"\\s+([0-9.]+)",Float.toString(myDefault)));
      Logger.logGeneral(Logger.RAW_DATA,"TorConfig.parseFloat: Parsed '"+name+"' as '"+x+"'");
      return x;
    }*/
    private String writeFloat(String name,float value) {
      return name + " " + value + "\n";
    }
    private float parseFloat(String config,String name,float myDefault,float lower,float upper) {
      float x = Float.parseFloat(Parsing.parseStringByRE(config,"^\\s*"+replaceSpaceWithSpaceRegExp(name)+"\\s+([0-9.]+)",Float.toString(myDefault)));
      if (x<lower) x = lower;
      if (x>upper) x = upper;
      Logger.logGeneral(Logger.RAW_DATA,"TorConfig.parseFloat: Parsed '"+name+"' as '"+x+"'");
      return x;
    }
    private String parseString(String config,String name,String myDefault) {
      String x = Parsing.parseStringByRE(config,"^\\s*"+replaceSpaceWithSpaceRegExp(name)+"\\s+(\\S.*?)$",myDefault);
      Logger.logGeneral(Logger.RAW_DATA,"TorConfig.parseString: Parsed '"+name+"' as '"+x+"'");
      return x;
    }
    private String writeString(String name, String value) {
      return name + " " + value + "\n"; 
    }
    private boolean parseBoolean(String config,String name,boolean myDefault) {
      String mydef = "false";
      if (myDefault) mydef="true";
      String x = Parsing.parseStringByRE(config,"^\\s*"+replaceSpaceWithSpaceRegExp(name)+"\\s+(\\S.*?)$",mydef).trim();
      boolean ret = false;
      if (x.equals("1") || x.equalsIgnoreCase("true") || x.equalsIgnoreCase("yes")) ret = true;
      Logger.logGeneral(Logger.RAW_DATA,"TorConfig.parseBoolean: Parsed '"+name+"' as '"+ret+"'");
      return ret;
    }
    private String writeBoolean(String name, boolean value) {
      if (value == true) {
        return name + " " + "true" + "\n";
      } else {
        return name + " " + "false" + "\n";
      }
    }
    void readFromConfig(String filename) {
        try {
            String config="";
            if (filename != null) {
                DataInputStream sin = new DataInputStream(new FileInputStream(new File(filename)));
                //DataInputStream sin = new DataInputStream(ClassLoader.getSystemResourceAsStream(filename));
                config = StreamsAndHTTP.readAllFromStream(sin);
                Logger.logGeneral(Logger.RAW_DATA, "TorConfig.readFromConfig(): " + config);
            }
            //  Read variable config information here
            // logging-verbosity
            Logger.LOG_GENERAL = parseInt(config,"Log General",Logger.LOG_GENERAL);
            Logger.LOG_DIRECTORY = parseInt(config,"Log Directory",Logger.LOG_DIRECTORY);
            Logger.LOG_TLS = parseInt(config,"Log TLS",Logger.LOG_TLS);
            Logger.LOG_CIRCUIT = parseInt(config,"Log Circuit",Logger.LOG_CIRCUIT);
            Logger.LOG_STREAM = parseInt(config,"Log Stream",Logger.LOG_STREAM);
            Logger.LOG_CELL = parseInt(config,"Log Cell",Logger.LOG_CELL);
            Logger.LOG_CRYPTO = parseInt(config,"Log Crypto",Logger.LOG_CRYPTO);
            Logger.LOG_HIDDENSERVICE = parseInt(config,"Log HiddenService",Logger.LOG_HIDDENSERVICE);
            Logger.LOG_FILE_GENERAL = parseInt(config,"LogFile General",Logger.LOG_FILE_GENERAL);
            Logger.LOG_FILE_DIRECTORY = parseInt(config,"LogFile Directory",Logger.LOG_FILE_DIRECTORY);
            Logger.LOG_FILE_TLS = parseInt(config,"LogFile TLS",Logger.LOG_FILE_TLS);
            Logger.LOG_FILE_CIRCUIT = parseInt(config,"LogFile Circuit",Logger.LOG_FILE_CIRCUIT);
            Logger.LOG_FILE_STREAM = parseInt(config,"LogFile Stream",Logger.LOG_FILE_STREAM);
            Logger.LOG_FILE_CELL = parseInt(config,"LogFile Cell",Logger.LOG_FILE_CELL);
            Logger.LOG_FILE_CRYPTO = parseInt(config,"LogFile Crypto",Logger.LOG_FILE_CRYPTO);
            Logger.LOG_FILE_HIDDENSERVICE = parseInt(config,"LogFile HiddenService",Logger.LOG_FILE_HIDDENSERVICE);
            // You can still manually define V1 authorities
            Pattern p_trusted = Pattern.compile("^\\s*trusted\\s+(\\S+)\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(\\d+)\\s+([0-9a-f:]+)\\s*$",
                    Pattern.UNIX_LINES + Pattern.MULTILINE+Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
            Matcher m = p_trusted.matcher(config);
            while (m.find()) {
              HashMap<String,Object> dirServer = new HashMap<String,Object>();
              dirServer.put("version", new Integer(1));
              dirServer.put("ip", m.group(2));
              dirServer.put("port", new Integer(m.group(3)));
              dirServer.put("fingerprint", m.group(4));
              trustedServers.put(m.group(1), dirServer);
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.readFromConfig(): Parsed v1Dir '"+m.group(1)+"' with fingerprint '"+m.group(4)+"'");
            }
            // Parse trusted servers V2
            p_trusted = Pattern.compile("^\\s*trustedv2\\s+(\\S+)\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(\\d+)\\s+([0-9a-f:]+)\\s*$",
                    Pattern.UNIX_LINES + Pattern.MULTILINE+Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
            m = p_trusted.matcher(config);
            while (m.find()) {
              HashMap<String,Object> dirServer = new HashMap<String,Object>();
              dirServer.put("version", new Integer(2));
              dirServer.put("ip", m.group(2));
              dirServer.put("port", new Integer(m.group(3)));
              dirServer.put("fingerprint", m.group(4));
              trustedServers.put(m.group(1), dirServer);
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.readFromConfig: Parsed v2Dir '"+m.group(1)+"' with fingerprint '"+m.group(4)+"'");
            }
            // NEW: Parse trusted servers V3
            p_trusted = Pattern.compile("^\\s*trustedv3\\s+(\\S+)\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(\\d+)\\s+([0-9a-f:]+)\\s*$",
                    Pattern.UNIX_LINES + Pattern.MULTILINE+Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
            m = p_trusted.matcher(config);
            while (m.find()) {
              HashMap<String,Object> dirServer = new HashMap<String,Object>();
              dirServer.put("version", new Integer(3));
              dirServer.put("ip", m.group(2));
              dirServer.put("port", new Integer(m.group(3)));
              dirServer.put("fingerprint", m.group(4));
              trustedServers.put(m.group(1), dirServer);
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.readFromConfig: Parsed v3Dir '"+m.group(1)+"' with fingerprint '"+m.group(4)+"'");
            }
            // security parameters
            streamsPerCircuit= parseInt(config,"StreamsPerCircuit",streamsPerCircuit);
            rankingIndexEffect = parseFloat(config,"RankingIndexEffect",rankingIndexEffect,0,1);
            route_min_length = parseInt(config,"RouteMinLength",route_min_length);
            route_max_length = parseInt(config,"RouteMaxLength",route_max_length);            
            min_percentage = parseFloat(config,"MinPercentage",min_percentage,0,1);
            min_descriptors = parseInt(config,"MinDescriptors",min_descriptors);
            route_uniq_class_c = parseBoolean(config,"RouteUniqClassC",route_uniq_class_c);
            route_uniq_country = parseBoolean(config,"RouteUniqCountry",route_uniq_country);
            allow_node_multiple_circs = parseInt(config,"AllowNodeMultipleCircuits", allow_node_multiple_circs);
            // Avoid Countries
            Pattern p = Pattern.compile("^\\s*AvoidCountry\\s+(.*?)$", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.UNIX_LINES);
            m = p.matcher(config);
            while(m.find()) {
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.readConfig: will avoid country: "+m.group(1));
              avoidedCountries.add(m.group(1));
            }
            // Avoid Nodes
            p = Pattern.compile("^\\s*AvoidNode\\s+(.*?)$", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.UNIX_LINES);
            m = p.matcher(config);
            while(m.find()) {
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.readConfig: will avoid node: "+m.group(1));
              avoidedNodes.add(m.group(1));
            }
            // functionality 
            cacheMaxAgeSeconds = parseInt(config,"cacheMaxAgeSeconds",cacheMaxAgeSeconds);
            startupDelaySeconds = parseInt(config,"startupDelaySeconds",startupDelaySeconds);
            guiUpdateIntervalMilliSeconds = parseInt(config,"guiUpdateIntervalMilliSeconds",guiUpdateIntervalMilliSeconds);
            guiDisplayNodeNames = parseBoolean(config,"guiDisplayNodeNames",guiDisplayNodeNames);
            guiCountryOfUser = parseString(config,"guiCountryOfUser",guiCountryOfUser);

            dirserver_port = parseInt(config,"dirserverport",0);
            or_port = parseInt(config,"orport",0);
            logFileName = parseString(config,"Log filename",null);
            nickname = parseString(config,"nickname",nickname);
            portWWWProxy = parseInt(config,"portwwwproxy",portWWWProxy);
            portSocksProxy = parseInt(config,"portsocksproxy",portSocksProxy);
            // QoS parameters
            retriesConnect = parseInt(config,"RetriesConnect",retriesConnect);
            retriesStreamBuildup = parseInt(config,"RetriesStreamBuildup",retriesStreamBuildup);
            reconnectCircuit = parseInt(config,"ReconnectCircuit",reconnectCircuit);
            defaultIdleCircuits = parseInt(config,"DefaultIdleCircuits",defaultIdleCircuits);

            queueTimeoutCircuit = parseInt(config,"QueueTimeoutCircuit",queueTimeoutCircuit);
            queueTimeoutResolve = parseInt(config,"QueueTimeoutResolve",queueTimeoutResolve);
            queueTimeoutStreamBuildup = parseInt(config,"QueueTimeoutStreamBuildup",queueTimeoutStreamBuildup);

            rankingTransferPerServerUpdate = parseFloat(config,"RankingTransferPerServerUpdate",rankingTransferPerServerUpdate,0,1);

            circuitClosesOnFailures = parseInt(config,"CircuitClosesOnFailures",circuitClosesOnFailures);
            circuitsMaximumNumber = parseInt(config,"circuitsMaximumNumber",circuitsMaximumNumber);

            veryAggressiveStreamBuilding = parseBoolean(config,"veryAggressiveStreamBuilding",veryAggressiveStreamBuilding);
            // directory parameters
            intervalDirectoryV1Refresh = parseInt(config,"DirectoryV1Refresh",intervalDirectoryV1Refresh);
            intervalDirectoryRefresh   = parseInt(config,"DirectoryRefresh",intervalDirectoryRefresh);
            dirV2ReadMaxNumberOfDescriptorsFirstTime = parseInt(config,"MaxNumberOfDescriptorsFirstTime",dirV2ReadMaxNumberOfDescriptorsFirstTime);
            dirV2ReadMaxNumberOfDescriptorsPerUpdate = parseInt(config,"MaxNumberOfDescriptorsPerUpdate",dirV2ReadMaxNumberOfDescriptorsPerUpdate);
            dirV2ReloadRetries = parseInt(config,"dirV2ReloadRetries",dirV2ReloadRetries);
            dirV2ReloadTimeout = parseInt(config,"dirV2ReloadTimeout",dirV2ReloadTimeout);
            dirV2DescriptorsPerBatch  = parseInt(config,"dirV2DescriptorsPerBatch",dirV2DescriptorsPerBatch);
            // Filtering HTTP-headers
            p = Pattern.compile("^\\s*FilterHeader\\s+(.*?)$", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.UNIX_LINES);
            m = p.matcher(config);
            while(m.find()) {
              Logger.logGeneral(Logger.RAW_DATA,"TorConfig.readConfig: will filter '"+m.group(1)+"' HTTP-headers");
              setFilteredHeaders.add(m.group(1));
            }
            // Filtering HTTP-headers
            p = Pattern.compile("^\\s*ReplaceHeader\\s+(\\S+)\\s+(.*?)$", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.UNIX_LINES);
            m = p.matcher(config);
            while(m.find()) {
              Logger.logGeneral(Logger.RAW_DATA,"TorConfig.readConfig: will replace '"+m.group(1)+"' HTTP-headers with "+m.group(2));
              String[] set = new String[2];
              set[0] = m.group(1);
              set[1] = m.group(2);
              setReplaceHeaders.add(set);
            }
        } catch (IOException e) {
            Logger.logGeneral(Logger.WARNING,
                    "TorConfig.readFromConfig(): Warning: " + e.getMessage());
        }
        if (trustedServers.size()<1) addDefaultTrustedServers();
    }

  /** used to store some new values to a file */
  void writeToFile(String filename) {
      if (filename==null) return;
        try {
            StringBuffer config = new StringBuffer();

            Logger.logGeneral(Logger.VERBOSE, "TorConfig.writeToFile(): " + config);
            // Write variable config information here
            // logging-verbosity
            config.append(writeInt("Log General", Logger.LOG_GENERAL));
            config.append(writeInt("Log Directory", Logger.LOG_DIRECTORY));
            config.append(writeInt("Log TLS", Logger.LOG_TLS));
            config.append(writeInt("Log Circuit",Logger.LOG_CIRCUIT));
            config.append(writeInt("Log Stream",Logger.LOG_STREAM));
            config.append(writeInt("Log Cell",Logger.LOG_CELL));
            config.append(writeInt("Log Crypto",Logger.LOG_CRYPTO));
            config.append(writeInt("Log HiddenService",Logger.LOG_HIDDENSERVICE));
            config.append(writeInt("LogFile General",Logger.LOG_FILE_GENERAL));
            config.append(writeInt("LogFile Directory",Logger.LOG_FILE_DIRECTORY));
            config.append(writeInt("LogFile TLS",Logger.LOG_FILE_TLS));
            config.append(writeInt("LogFile Circuit",Logger.LOG_FILE_CIRCUIT));
            config.append(writeInt("LogFile Stream",Logger.LOG_FILE_STREAM));
            config.append(writeInt("LogFile Cell",Logger.LOG_FILE_CELL));
            config.append(writeInt("LogFile Crypto",Logger.LOG_FILE_CRYPTO));
            config.append(writeInt("LogFile HiddenService",Logger.LOG_FILE_HIDDENSERVICE));

            // trusted servers
            Iterator<String> trustedIterate = trustedServers.keySet().iterator(); 
            while (trustedIterate.hasNext()) {
              String dirServerName = (String)trustedIterate.next();
              HashMap<String, Object> dirServer = trustedServers.get(dirServerName);
              StringBuffer dirServerString = new StringBuffer();
              dirServerString.append(dirServerName + " ");
              dirServerString.append(dirServer.get("ip") + " ");
              dirServerString.append(dirServer.get("port") + " ");
              dirServerString.append(dirServer.get("fingerprint") + " ");
              config.append(writeString("trusted",dirServerString.toString()));
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.writeToFile: Wrote Dir '"+dirServerName+"' with fingerprint '"+dirServer.get("fingerprint")+"'");
            }

            // security parameters
            config.append(writeInt("StreamsPerCircuit",streamsPerCircuit));
            config.append(writeFloat("RankingIndexEffect",rankingIndexEffect));
            config.append(writeInt("RouteMinLength",route_min_length));
            config.append(writeInt("RouteMaxLength",route_max_length));
            config.append(writeFloat("MinPercentage",min_percentage));
            config.append(writeInt("MinDescriptors",min_descriptors));
            config.append(writeBoolean("RouteUniqClassC",route_uniq_class_c));
            config.append(writeBoolean("RouteUniqCountry",route_uniq_country));
            config.append(writeInt("AllowNodeMultipleCircuits", allow_node_multiple_circs));

             // Avoided countries
            Iterator<String> it = avoidedCountries.iterator();
            while (it.hasNext()) {
              String countryName = (String)it.next();
              config.append(writeString("AvoidCountry",countryName)); 
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.writeToFile: will avoid country "+countryName);
            }
             // Avoided nodes
            it = avoidedNodes.iterator();
            while (it.hasNext()) {
              String nodeName = (String)it.next();
              config.append(writeString("AvoidNode",nodeName)); 
              Logger.logGeneral(Logger.VERBOSE,"TorConfig.writeToFile: will avoid node "+nodeName);
            }
            // Functionality 
            config.append(writeInt("cacheMaxAgeSeconds",cacheMaxAgeSeconds));
            config.append(writeInt("startupDelaySeconds",startupDelaySeconds));
            config.append(writeInt("guiUpdateIntervalMilliSeconds",guiUpdateIntervalMilliSeconds));
            config.append(writeBoolean("guiDisplayNodeNames",guiDisplayNodeNames));
            config.append(writeString("guiCountryOfUser",guiCountryOfUser));
            config.append(writeInt("dirserverport",dirserver_port));
            config.append(writeInt("orport",or_port));
            config.append(writeString("Log filename",logFileName));
            config.append(writeString("nickname",nickname));
            config.append(writeInt("portwwwproxy",portWWWProxy));
            config.append(writeInt("portsocksproxy",portSocksProxy));

            // QoS parameters
            config.append(writeInt("RetriesConnect",retriesConnect));
            config.append(writeInt("RetriesStreamBuildup",retriesStreamBuildup));
            config.append(writeInt("ReconnectCircuit",reconnectCircuit));
            config.append(writeInt("DefaultIdleCircuits",defaultIdleCircuits));

            config.append(writeInt("QueueTimeoutCircuit",queueTimeoutCircuit));
            config.append(writeInt("QueueTimeoutResolve",queueTimeoutResolve));
            config.append(writeInt("QueueTimeoutStreamBuildup",queueTimeoutStreamBuildup));

            config.append(writeInt("CircuitClosesOnFailures",circuitClosesOnFailures));
            config.append(writeInt("circuitsMaximumNumber",circuitsMaximumNumber));
            
            config.append(writeBoolean("veryAggressiveStreamBuilding",veryAggressiveStreamBuilding));

            // FIXME: Check if this really works
            config.append(writeFloat("RankingTransferPerServerUpdate",rankingTransferPerServerUpdate));
            // directory parameters
            config.append(writeInt("DirectoryV1Refresh",intervalDirectoryV1Refresh));
            config.append(writeInt("DirectoryRefresh",intervalDirectoryRefresh));
            config.append(writeInt("MaxNumberOfDescriptorsFirstTime",dirV2ReadMaxNumberOfDescriptorsFirstTime));
            config.append(writeInt("MaxNumberOfDescriptorsPerUpdate",dirV2ReadMaxNumberOfDescriptorsPerUpdate));
            config.append(writeInt("dirV2ReloadRetries",dirV2ReloadRetries));
            config.append(writeInt("dirV2ReloadTimeout",dirV2ReloadTimeout));
            config.append(writeInt("dirV2DescriptorsPerBatch",dirV2DescriptorsPerBatch));

            // Filtering HTTP-headers
            Iterator<String> iterateHeaders = setFilteredHeaders.iterator();
            while (iterateHeaders.hasNext()) {
              String headerName = (String)iterateHeaders.next();
              config.append(writeString("FilterHeader",headerName)); 
              Logger.logGeneral(Logger.RAW_DATA,"TorConfig.writeToFile: will filter '"+headerName+"' HTTP-headers");
            }

            // Replace HTTP-headers
            Iterator<String[]> iterateReplace = setReplaceHeaders.iterator();
            while(iterateReplace.hasNext()) {
            	String[] set = iterateReplace.next();
                config.append(writeString("ReplaceHeader",set[0] + " " + set[1])); 
                Logger.logGeneral(Logger.RAW_DATA,"TorConfig.writeToFile: will filter '"+set[0]+"' HTTP-headers");            	
            }
            
            FileWriter writer = new FileWriter(new File(filename));
            writer.write(config.toString());
            writer.close();

        } catch (IOException e) {
            Logger.logGeneral(Logger.WARNING,
                    "TorConfig.writeToFile(): Warning: " + e.getMessage());
        }
 
    }

    static String getConfigDir() {
        //String os = operatingSystem();
        /*if (os.equals("Linux"))
            return System.getProperty("user.home")
                    + System.getProperty("file.separator") + ".TorJava"
                    + System.getProperty("file.separator");
        return System.getProperty("user.home")
                + System.getProperty("file.separator") + "TorJava"
                + System.getProperty("file.separator");*/
        return TorKeeper.getConfigPath() + System.getProperty("file.separator");
    }

    /** removed, since it is no more used */
    /*static String getTempDir() {
        String os = operatingSystem();
        if (os.equals("Linux"))
            return "/tmp/";
        return getConfigDir();
    }*/

    static String getCacheFilename() {
        return getConfigDir() + TOR_CACHE_FILENAME;
    }

    static String getLogFilename() {
        return logFileName;
    }

    static void setLogFilename(String name) {
        logFileName = name;
    }

    static String operatingSystem() {
        return System.getProperty("os.name");
    }
}

// vim: et
