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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.Security;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import TorJava.Common.Common;
import TorJava.Common.Encoding;
import TorJava.Common.StreamsAndHTTP;
import TorJava.Common.TorException;
import TorJava.Common.TorNoAnswerException;
import TorJava.Common.TorResolveFailedException;


/**
 * MAIN CLASS. keeps track of circuits, tls-connections and the status of
 * servers. Provides high level access to all needed functionality, i.e.
 * connecting to some remote service via Tor.
 * 
 * CG - 07/07/09 Removed GeoIP
 * 
 * @author Lexi Pimenidis
 * @author Tobias Koelsch
 * @author Vinh Pham
 * @author Andriy Panchenko
 * @author Michael Koellejan
 * @author Connell Gauld
 * @version $Date: 2008/04/16 16:34:55 $ $Revision: 1.2 $
 */

public class Tor {
    Directory dir;

    public FirstNodeHandler fnh;

    public static Logger log;


    TorBackgroundMgmt mgmt; // management-thread

    TorConfig config;

    DirectoryServer dirserver;

    PrivateKeyHandler privateKeyHandler;

    HashMap<String,HiddenServiceProperties> hiddenServices;

    HashMap<String,ServiceDescriptor> cachedServiceDescriptors;
    
    HashMap<String,Circuit> hiddenServiceCircuits;

    long startupPhaseWithoutConnects; // used to delay connects until Tor has some time to build up circuits and stuff

    Vector<TorEventHandler> eventHandler;
    
   /**
     * init Tor with all defaults
     * 
     * @exception IOException
     */
    public Tor(boolean fastStartup) throws IOException {
       log = new Logger();
       config = new TorConfig(true);
       init_phase1(false, fastStartup);
       initDirectory(false);
       init_phase3(fastStartup);
    }

    /**
     * start tor and try not to access any local files
     * 
     * @param noLocalFileSystemAccess set to true to avoid any access to the local filesystem
     * @exception IOException
     */
    public Tor(boolean noLocalFileSystemAccess, boolean fastStartup) throws IOException {
       log = new Logger(noLocalFileSystemAccess);
       config = new TorConfig(!noLocalFileSystemAccess);
       init_phase1(noLocalFileSystemAccess, fastStartup);
       initDirectory(noLocalFileSystemAccess);
       init_phase3(fastStartup);
    }

  /**
     * init Tor with all defaults
     * 
     * @exception IOException
     */
    public Tor(String configFile, boolean fastStartup) throws IOException {
       log = new Logger();
       config = new TorConfig(configFile);
       init_phase1(false, fastStartup);
       initDirectory(false);
       init_phase3(fastStartup);
    }


    public void reloadConfig() {
      if (config!=null) config.reload();
    }

    private void init_phase1(boolean noLocalFileSystemAccess, boolean fastStartup) 
      throws IOException
    {
      // install BC, if not already done
      if(Security.getProvider("BC")==null) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(),2);
      }
      
      // logger and config
      Logger.logGeneral(Logger.INFO,"TorJava is starting up. Please don't use this implementation for strong anonymity!");
      // create identity
      privateKeyHandler = new PrivateKeyHandler(fastStartup);
      // determine end of startup-Phase
      startupPhaseWithoutConnects = System.currentTimeMillis() + TorConfig.startupDelaySeconds * 1000L;
      // init event-handler
      eventHandler = new Vector<TorEventHandler>();
    }

    private void initDirectory(boolean noLocalFileSystemAccess) throws IOException {
        // what to do, if no local files shall be touched
        if (noLocalFileSystemAccess) {
            dir = new Directory(this,privateKeyHandler.getIdentity());
            return;
        }
      // load stable dir first
      // log.logDirectory(Logger.INFO,"Tor.initDirectory(): load stable dir");
      //dir = new Directory(this,privateKeyHandler.getIdentity(), config.TOR_STABLE_DIR_FILENAME, "");
      // load cache-file over stable-dir
      long age = System.currentTimeMillis() - TorConfig.cacheMaxAgeSeconds * 1000L;
      File cacheFile = new File(TorConfig.getCacheFilename());
      if (cacheFile.exists() && cacheFile.lastModified() >= age) {
        try{
          // TODO-FIXME-SECURITY-BUG-FIX: use fingerprint of dirServerKeys
          //dir.readDirectoryFromFile(config.getCacheFilename(), "");
          dir = new Directory(this,privateKeyHandler.getIdentity(), TorConfig.getCacheFilename(), "");
        }
        catch(Exception e) { 
          Logger.logDirectory(Logger.WARNING,"Tor.initDirectory(): "+e.getMessage());
        }
      } else {
        dir = new Directory(this,privateKeyHandler.getIdentity());
      }
      /* uneccessary 
      // check if in cache-file and/or stable-file have been some tor nodes
      if ((dir==null) || (dir.torServers==null) || (dir.torServers.size()<1)) 
        throw new IOException("no Tor nodes found");
      */
      // update from network in the background
      // -> will be done by TorBackgroundMgmt!
    }

    private void init_phase3(boolean fastStartup) throws IOException {
      // establish handler for TLS connections
      fnh = new FirstNodeHandler(this);
      // init thread to renew every now and then
      mgmt = new TorBackgroundMgmt(this,TorConfig.defaultIdleCircuits, fastStartup);
      // hidden services
      hiddenServices = new HashMap<String,HiddenServiceProperties>();
      cachedServiceDescriptors = new HashMap<String,ServiceDescriptor>();
      hiddenServiceCircuits = new HashMap<String, Circuit>();
      // directory service
      if (TorConfig.dirserver_port > 0)
        dirserver = new DirectoryServer(dir, TorConfig.dirserver_port);
    }

    /**
     * get a factory for TOR sockets.
     * 
     * @return default factory for TOR sockets.
     */
    public TorSocketFactory getSocketFactory() {
        if (TorSocketFactory.getDefault() == null)
            return new TorSocketFactory(this);
        else
            return (TorSocketFactory) TorSocketFactory.getDefault();
    }

    /**
     * makes a connection to a remote service
     * 
     * @param sp
     *            hostname, port to connect to and other stuff
     * @return some socket-thing
     */
    public TCPStream connect(TCPStreamProperties sp) throws IOException, TorResolveFailedException {
        //Tor.log.logGeneral(Logger.VERBOSE, "Tor: Trying to connect to " + sp.hostname);

        if (sp.hostname == null)
            throw new IOException("Tor: no hostname is provided");

        // check, if tor is still in startup-phase
        checkStartup();

        // check whether the address is hidden
        if (sp.hostname.endsWith(".onion"))
            return connectToHidden(sp);

        boolean resolveException = false;
        
        Circuit[] cs = fnh.provideSuitableCircuits(sp,false);
        if (TorConfig.veryAggressiveStreamBuilding) {

          for (int j = 0; j < cs.length; ++j) {
            // start N asynchronous stream building threads
            try{
              StreamThread[] streamThreads = new StreamThread[cs.length];
              for(int i=0;i<cs.length;++i) 
                streamThreads[i] = new StreamThread(cs[i],sp);
              // wait for the first stream to return
              int chosenStream = -1;
              int waitingCounter = TorConfig.queueTimeoutStreamBuildup * 1000 / 10;
              while((chosenStream < 0)&&(waitingCounter>=0)) {
                boolean atLeastOneAlive = false;
                for(int i=0;(i<cs.length)&&(chosenStream<0);++i) 
                  if (!streamThreads[i].isAlive()) {
                    if ((streamThreads[i].stream != null) && 
                        (streamThreads[i].stream.established)) {
                      chosenStream = i;
                    }
                  } else {
                    atLeastOneAlive = true;
                  }
                if (!atLeastOneAlive) break;
                Common.sleep(10);
                --waitingCounter;
              }
              // return one and close others
              if (chosenStream>=0) {
                TCPStream returnValue = streamThreads[chosenStream].stream;
                new ClosingThread(streamThreads,chosenStream);
                return returnValue;
              }
            }
            catch(Exception e) {
              Logger.logStream(Logger.WARNING,"Tor.connect(): " + e.getMessage() );
              return null;
            }
          }

        } else {
          // build serial N streams, stop if successful
          for (int i = 0; i < cs.length; ++i) {
            try {
              return new TCPStream(cs[i], sp);
            } catch (TorResolveFailedException e) {
            	Logger.logStream(Logger.WARNING, "Tor.connect: Resolve failed:" + e.getMessage());
            	resolveException = true;
            } catch (TorNoAnswerException e) {
              Logger.logStream(Logger.WARNING, "Tor.connect: Timeout on circuit:" + e.getMessage());
            } catch (TorException e) {
              Logger.logStream(Logger.WARNING, "Tor.connect: TorException trying to reuse existing circuit:" + e.getMessage());
            } catch(IOException e) {
              Logger.logStream(Logger.WARNING, "Tor.connect: IOException " + e.getMessage());
            }
          }
        }

        if (resolveException) throw new TorResolveFailedException();
        
        throw new IOException("Tor.connect: unable to connect to "
                + sp.hostname + ":" + sp.port + " after " + sp.connect_retries + " retries");
    }

   private Circuit establishIntroductionPoint(HiddenServiceProperties service,TCPStreamProperties sp_intro) 
   {
     Circuit circuit=null;
     for(int i=0;i<sp_intro.connect_retries;++i) {
         try {
             // use circuit
             circuit = fnh.provideSuitableNewCircuit(sp_intro);
             circuit.myHSProperties = service;
             Logger.logHiddenService(Logger.VERBOSE, "Tor.provideHiddenService: send relay_establish_intro-Cell over " + circuit.print());
             circuit.send_cell(new CellRelayEstablishIntro(circuit, service));
             circuit.queue.receiveRelayCell(CellRelay.RELAY_INTRO_ESTABLISHED);
             return circuit;
         } catch (Exception e) {
             Logger.logHiddenService(Logger.WARNING, "Tor.provideHiddenService: " + e.getMessage());
             //e.printStackTrace();
             if (circuit != null)
                 circuit.close(true);
         }
     }
     return null;
   }
    
    /**
     * ensures that a hidden service is running. If it isn't
     * already running, create it. If it is already running,
     * reassign the handler to the handler provided
     * @param service the service
     * @return the url of the service
     * @throws IOException
     * @throws TorException
     */
    public String provideHiddenService(HiddenServiceProperties service)
            throws IOException, TorException {
    	if (hiddenServices.containsKey(service.getName())) {
    		HiddenServiceProperties old = hiddenServices.get(service.getName());
    		old.reassignHandler(service.handler);
    		return service.getName();
    	}
    	else return setupHiddenService(service);
    }
    
    /**
     * initializes a new hidden service
     * 
     * @param service
     *            all data needed to init the thingy
     */
    public String setupHiddenService(HiddenServiceProperties service)
    		throws IOException, TorException{
        
    	if (service.handler == null)
            throw new TorException(
                    "Tor.provideHiddenService: need a handler for incoming connections");

        Logger.logHiddenService(Logger.INFO, "Tor.provideHiddenService: setting up hidden service " + service.getName());
        // check, if tor is still in startup-phase
        checkStartup();
        
        // Store the service
        String url = service.getName();
        hiddenServices.put(url, service);
        
        // Update hidden service status
        service.setStatus(HiddenServiceProperties.STATUS_STARTED, true);
        // establish choosen introduction points
        Iterator<IntroductionPoint> i = service.introPoints.iterator();
        while (i.hasNext()) {
        	IntroductionPoint ip = i.next();
            String ip_name = ip.getSrv().nickname;
            Logger.logHiddenService(Logger.INFO, "Tor.provideHiddenService: establish introduction point at " + ip_name);
             // create circuit properties to end the circuit at the
             // intro-point
             TCPStreamProperties sp_intro = new TCPStreamProperties();
             sp_intro.setCustomExitpoint(ip_name);
             establishIntroductionPoint(service,sp_intro);
        }
        // choose additional random introduction points, if none given
        while (service.introPoints.size() < service.minimum_number_of_intro_points) {
           TCPStreamProperties sp_intro = new TCPStreamProperties();
           sp_intro.connect_retries = 1;
           Circuit c = establishIntroductionPoint(service,sp_intro);
           if (c!=null) {
              String ip_name = c.route[c.route_established-1].server.nickname;
              Logger.logHiddenService(Logger.INFO, "Tor.provideHiddenService: establish introduction point at " + ip_name);
              service.addIntroPoint(ip_name,dir);
           };
        }
        // Update hidden service status
        service.setStatus(HiddenServiceProperties.STATUS_INTROPOINTS, true);
        // advertise introduction points
        int advertise_success = 0;
        Iterator<String> i2 = config.trustedServers.keySet().iterator();
        while (i2.hasNext()) {
            String nick = i2.next();
            HashMap<String, Object> trustedServer = config.trustedServers.get(nick);
            String address = (String) trustedServer.get("ip");
            int port = ((Integer) trustedServer.get("port")).intValue();
            int tries = 2;
            while (tries > 0) {
                Logger.logHiddenService(Logger.VERBOSE, "Tor.provideHiddenService: advertise service at " + nick + " (" + address + ":" + port + ")");
                TCPStreamProperties sp = new TCPStreamProperties(address, port);
                TCPStream stream = null;
                // advertise data for hidden service's rendevouz anonymously
                try {
                    stream = connect(sp);
                    // advertise service-descriptor with HTTP-POST
                    byte[] body = service.sd_v0.toByteArray();
                    String headerStr = "POST /tor/rendezvous/publish HTTP/1.0\r\n"
                        + "Content-Length: " + body.length + "\r\n"
                        + "\r\n";
                    byte[] header = headerStr.getBytes();
                    byte[] out = new byte[header.length + body.length];
                    System.arraycopy(header, 0, out, 0, header.length);
                    System.arraycopy(body, 0, out, header.length, body.length);
                    String answer = StreamsAndHTTP.HTTPBinaryRequest(
                            stream.getOutputStream(), stream.getInputStream(),
                             out);
                    // analyse answer
                    if (!answer.startsWith("HTTP/1.0 200 "))
                        throw new TorException( "Tor.provideHiddenService: no success posting service descriptor " + answer);
                    Logger.logHiddenService(Logger.INFO, "Successfully advertised at " + nick);
                    stream.close();
                    ++advertise_success;
                    if (advertise_success > 1) {
                        // Update hidden service status
                        service.setStatus(HiddenServiceProperties.STATUS_ADVERTISED, true);
                    }
                    break;
                } catch (Exception e) {
                    Logger.logHiddenService(Logger.WARNING,
                            "Tor.provideHiddenService: error advertising service at "
                                    + nick + " (" + address + ":" + port
                                    + ")\n" + e.getMessage());
                }
                if (--tries <= 0)
                    Logger.logHiddenService(Logger.WARNING,
                            "Tor.provideHiddenService: final error advertising service at "
                                    + nick + " (" + address + ":" + port + ")");
            }
        }
        // at least one advertisement?
        if (advertise_success < 1)
            throw new TorException(
                    "Tor.provideHiddenService: no successful advertisement");
        // success
        return url;
    }
    
    public int getHiddenServiceProvisionStatusPercent(String hiddenServiceUrl) throws IllegalArgumentException {
    	if (hiddenServices.containsKey(hiddenServiceUrl)) {
    		HiddenServiceProperties h = hiddenServices.get(hiddenServiceUrl);
    		return h.getStatusPercent();
    	} else throw new IllegalArgumentException("No such hidden service");
    }

    /**
     * makes a connection to a hidden service
     * 
     * @param sp
     *            hostname, port to connect to and other stuff
     * @return some socket-thing
     */
    private TCPStream connectToHidden(TCPStreamProperties spo)
            throws IOException {
        // check, if tor is still in startup-phase
        checkStartup();
        Circuit myRendezvousCirc = null;
        Server hiddenServer;
        Node hiddenNode = null;

        // String address, x, y;
        String z;
        // Iterator it, i, i2;
        byte[] cookie = new byte[20];
        // boolean notFound;
        int j;

        z = (String) Encoding.parseHiddenAddress(spo.hostname).get("z");

        // Do we already have a connection to this address?
        if (hiddenServiceCircuits.containsKey(z)) {
            // TODO assess suitability of this circuit
            System.out.println("Reusing existing circuit");
            TCPStreamProperties tcpProps = new TCPStreamProperties("", spo.port);
            try {
                return new TCPStream(hiddenServiceCircuits.get(z), tcpProps);
            } catch (TorNoAnswerException e) {
                // Create a new circuit instead
                e.printStackTrace();
            } catch (TorException e) {
                // Create a new circuit instead
                e.printStackTrace();
            }
        }

        // get a copy from the service descriptor (either local cache or
        // retrieve form network)
        ServiceDescriptor sd = (ServiceDescriptor) cachedServiceDescriptors
                .get(z);
        if (sd == null || (!sd.checkTimeStampValidity())) {
            sd = ServiceDescriptor.loadFromDirectory(z, this);
            cachedServiceDescriptors.put(z, sd); // cache it
        }

        boolean establishedRendezvous = false;
        j = 0; // attempts counted
        // spo.connect_retries try to establish rendezvous
        while ((j < spo.connect_retries) && (!establishedRendezvous)) {
            j++;
            try {
                myRendezvousCirc = fnh.provideSuitableNewCircuit(new TCPStreamProperties());
                String rendezvousName = myRendezvousCirc.route[myRendezvousCirc.route_established-1].server.nickname;

                Logger.logGeneral(Logger.INFO, "Tor.connectToHidden: establishing rendezvous point for " + z + " at "+ rendezvousName);
                Random rnd = new Random();
                rnd.nextBytes(cookie);

                myRendezvousCirc.send_cell(new CellRelayEstablishRendezvous( myRendezvousCirc, cookie));
                myRendezvousCirc.streamHistory.add(spo.hostname);

                // wait for answer
                CellRelay rendezvousACK = myRendezvousCirc.queue.receiveRelayCell(CellRelay.CELL_RELAY_RENDEZVOUS_ESTABLISHED);
                if (rendezvousACK.length > 0) {
                  throw new TorException(
                        "Tor.connectToHidden: Got NACK from RENDEZVOUS Point");
                }
                myRendezvousCirc.sd = sd;

                hiddenServer = new Server(this,sd.publicKey);
                hiddenNode = new Node(hiddenServer); // between HS and
                                                        // Rendezvous point

                establishedRendezvous = true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TorException e) {
                e.printStackTrace();
            }
        }

        if (!establishedRendezvous) {
            Logger.logGeneral(Logger.WARNING,
                    "Tor.connectToHidden: coudn't establishing rendezvous point for "
                            + z);
            throw new IOException(
                    "Tor.connectToHidden: coudn't establishing rendezvous point for "
                            + z);
        }

        Iterator<IntroductionPoint> it3 = sd.introPoints.iterator();
        String rendezvousName = myRendezvousCirc.route[myRendezvousCirc.route.length - 1].server.nickname;

        while (it3.hasNext()) {
            IntroductionPoint iPoint = (IntroductionPoint) it3.next();
            Logger.logGeneral(Logger.INFO,"Tor.connectToHidden: contacting introduction point " + iPoint.getNickname() + " for " + z);

            // introduce rendezvous to the node
            TCPStreamProperties spIntro = new TCPStreamProperties();

            spIntro.exitPolicyRequired = false;
//            Server sr = iPoint.getSrv();
            spIntro.setCustomExitpoint(iPoint.getNickname() );

            try {
                // make new circ where the last node is intro point
                Circuit myIntroCirc = new Circuit(this,fnh, dir, spIntro);

                // System.out.println(" LAST NODE IS... " +
                // myIntroCirc.route[myIntroCirc.route.length-1].server.name);

                // and CellIntro1 data encrypted with PK of Hidden Service, and _not_ of the introPoint
                myIntroCirc.send_cell(new CellRelayIntroduce1(myIntroCirc, cookie, sd, rendezvousName, hiddenNode));

                // wait for ack
                CellRelay introACK = myIntroCirc.queue.receiveRelayCell(CellRelay.CELL_RELAY_COMMAND_INTRODUCE_ACK);
                if (introACK.length > 0)
                    throw new TorException("Tor.connectToHidden: Got NACK from Introduction Point");
                // introduce ACK is received
                Logger.logGeneral(Logger.RAW_DATA, "Tor.connectToHidden: Got ACK from Intro Point");

                myIntroCirc.close(true);

                // wait for answer from the hidden service (RENDEZVOUS2)
                int oldTimeout = myRendezvousCirc.queue.timeout;
                if (oldTimeout < 120*1000) myRendezvousCirc.queue.timeout = 120*1000;
                CellRelay r2Relay = myRendezvousCirc.queue.receiveRelayCell(CellRelay.CELL_RELAY_RENDEZVOUS2);
                myRendezvousCirc.queue.timeout = oldTimeout;
                // finish diffie-hellman
                byte[] dh_gy = new byte[148];
                System.arraycopy(r2Relay.data, 0, dh_gy, 0, 148);
                hiddenNode.finish_dh(dh_gy);

                myRendezvousCirc.addNode(hiddenNode);

                Logger.logGeneral(Logger.INFO, "Tor.connectToHidden: succesfully established rendezvous with " + z);
                // address in begin cell set to "";
                TCPStreamProperties tcpProps = new TCPStreamProperties("", spo.port);

                if (hiddenServiceCircuits.containsKey(z)) hiddenServiceCircuits.remove(z);
                hiddenServiceCircuits.put(z, myRendezvousCirc);

                // connect
                return new TCPStream(myRendezvousCirc, tcpProps);
            } catch (TorException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // all intros failed...
        // perhaps there is something wrong with out cached service descriptor
        cachedServiceDescriptors.remove(z);
        throw new IOException(
                "Tor.connectToHidden: couldn't connect to an introduction point of "
                        + z);
    }

    /**
     * shut down everything
     * 
     * @param force
     *            set to true, if everything shall go fast. For graceful end,
     *            set to false
     */
    public void close(boolean force) {
        Logger.logGeneral(Logger.INFO,"TorJava ist closing down");
        // shutdown mgmt
        mgmt.close();
        // shut down connections
        fnh.close(force);
        // shutdown/save directory
        dir.close(TorConfig.getCacheFilename());
        // write config file 
        config.close();
        // close hidden services
        // TODO close hidden services, once they are implemented and work
        // kill logger
        Logger.logGeneral(Logger.INFO, "Tor.close(): CLOSED");
        log.close();
    }

    /** synonym for close(false); */
    public void close() {
        close(false);
    }

    /**
     * anonymously resolve a hostname.
     * 
     * @param host
     *            the hostname
     * @return the resolved hostname
     */
    public InetAddress resolve(String host) throws IOException {
        Object o = resolve_internal(host);
        if (o instanceof InetAddress)
            return (InetAddress) o;
        else
            return null;
    }

    /**
     * anonymously do a reverse look-up
     * 
     * @param addr
     *            the inet-address to be resolved
     * @return the hostname
     */
    public String resolve(InetAddress addr) throws IOException {
        // build address (works only for IPv4!)
        byte[] a = addr.getAddress();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; ++i) {
            sb.append((int) (a[3 - i]) & 0xff);
            sb.append('.');
        }
        ;
        sb.append("in-addr.arpa");
        // resolve address
        Object o = resolve_internal(sb.toString());
        if (o instanceof String)
            return (String) o;
        else
            return null;
    }

    /**
     * internal function to use the tor-resolve-functionality
     * 
     * @param query
     *            a hostname to be resolved, or for a reverse lookup:
     *            A.B.C.D.in-addr.arpa
     * @return either an InetAddress (normal query), or a String
     *         (reverse-DNS-lookup)
     */
    private Object resolve_internal(String query) throws IOException {
        try {
          // check, if tor is still in startup-phase
          checkStartup();
            // try to resolv query over all existing circuits
            // so iterate over all TLS-Connections
            Iterator<String> i = fnh.tls.keySet().iterator();
            while (i.hasNext()) {
                TLSConnection tls = fnh.tls.get(i.next());
                // and over all circuits in each TLS-Connection
                Iterator<Integer> i2 = tls.circuits.keySet().iterator();
                while (i2.hasNext()) {
                    Circuit circuit = (Circuit) tls.circuits.get(i2.next());
                    try {
                        if (circuit.established) {
                            // if an answer is given, we're satisfied
                            ResolveStream rs = new ResolveStream(circuit);
                            Object o = rs.resolve(query);
                            rs.close();
                            return o;
                        }
                        ;
                    } catch (Exception e) {
                        // in case of error, do nothing, but retry with the next
                        // circuit
                    }
                }
            }
            // if no circuit could give an answer (possibly there was no
            // established circuit?)
            // build a new circuit and ask this one to resolve the query
            ResolveStream rs = new ResolveStream(new Circuit(this,fnh, dir,
                    new TCPStreamProperties()));
            Object o = rs.resolve(query);
            rs.close();
            return o;
        } catch (TorException e) {
            throw new IOException("Error in Tor: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new IOException("Error in Tor: " + e.getMessage());
        }
    }

    public void registerEventHandler(TorEventHandler eh) {
      eventHandler.add(eh);
    }

    public boolean removeEventHandler(TorEventHandler eh) {
      return eventHandler.remove(eh);
    }

    void fireEvent(TorEvent event) {
      for(int i=0;i<eventHandler.size();++i) {
        try{
          TorEventHandler eh = (TorEventHandler) eventHandler.elementAt(i);
          eh.ping(event);
        }
        catch(Exception e) {
          Logger.logGeneral(Logger.INFO, "Tor.fireEvent(): "+e.getMessage());
        }
      }
    }
    
    private static boolean gaveMessage = false;
    private static boolean startUp = true;

    /** make sure that tor had some time to read the directory and build up soime circuits
     */
    private void checkStartup() {
      // start up is prooven to be over
      if (!startUp) return;
      // check if startup is over
      long now = System.currentTimeMillis();
      if (now >= startupPhaseWithoutConnects) {
        startUp = false;
        return;
      }
      // wait for startup to be over
      long sleep = startupPhaseWithoutConnects - System.currentTimeMillis();
      if(!gaveMessage) {
        gaveMessage=true;
        Logger.logGeneral(Logger.VERBOSE,"Tor.checkStartup(): TorJava is still in startup phase, sleeping for "+(sleep/1000L)+" seconds");
      }
      try { Thread.sleep(sleep); }
      catch(Exception e) {}
    }
    
    /**
     * returns a set of current established circuits
     * (only used by TorJava.Proxy.MainWindow to get a list of circuits to display)
     *
     */
    public HashSet<Circuit> getCurrentCircuits() {
        
        HashSet<Circuit> allCircs = new HashSet<Circuit>();
        Iterator<String> it = fnh.tls.keySet().iterator();
        while (it.hasNext()) {
          TLSConnection tls = (TLSConnection) fnh.tls.get(it.next());
          Iterator<Integer> i2 = tls.circuits.keySet().iterator();
          while (i2.hasNext()) {
              Circuit circuit = tls.circuits.get(i2.next());
              //if (circuit.established && (!circuit.closed)){
                allCircs.add(circuit);
              //}
          }
        }
        return allCircs;
    }

}

/** 
 * this class  is used to build a TCPStream in the background
 * @author Lexi
 */
class StreamThread extends Thread {
  TCPStream stream;
  Circuit cs;
  TCPStreamProperties sp;
  boolean finished = false;
  /** copy data to local variables and start background thread */
  StreamThread(Circuit cs,TCPStreamProperties sp) {
    this.cs = cs;
    this.sp = sp;
    this.finished = false;
    this.start();
  }
  /** build stream in background and return. possibly the stream is closed
   *  prematurely by another thread by having its queue closed */
  public void run() {
    try{
      this.stream = new TCPStream(cs,sp);
    }
    catch(Exception e) {
      if ((stream != null) && (stream.queue != null) && (!stream.queue.closed)) 
        Logger.logStream(Logger.WARNING,"Tor.StreamThread.run(): " + e.getMessage() );
      this.stream = null;
    }
    this.finished = true;
  }
}

/** this background thread closes all streams that have been build by 
 * StreamThreads but are not used any more<br>
 * FIXME: cache ready streams and possibly reuse them later on
 * @author Lexi
 */
class ClosingThread extends Thread {
  StreamThread[] threads;
  int chosenOne;
  ClosingThread(StreamThread[] threads,int chosenOne) {
    this.threads = threads;
    this.chosenOne = chosenOne;
    this.start();
  }
  public void run() {
    // loop and check when threads finish and then close the streams
    for(int i=0;i<threads.length;++i) 
      if(i != chosenOne ) {
        if (threads[i].stream != null) {
          try{ // finish the queue
            threads[i].stream.closed=true;
            threads[i].stream.queue.close();
          } catch(Exception e) { 
            Logger.logStream(Logger.WARNING,"Tor.ClosingThread.run(): " + e.getMessage() );
          }
          try{
            threads[i].stream.close();
          } catch(Exception e) { 
            Logger.logStream(Logger.WARNING,"Tor.ClosingThread.run(): " + e.getMessage() );
          }
        }
        try{
          threads[i].join();
        } catch(Exception e) { 
          Logger.logStream(Logger.WARNING,"Tor.ClosingThread.run(): " + e.getMessage() );
        }
      }
  }
}

// vim: et
