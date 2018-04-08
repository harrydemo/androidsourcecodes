package TorJava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Caches server descriptors into an external file.
 * @author cmg47
 *
 */
public class DescriptorCache {
	
	public static final String DESCRIPTOR_CACHE_FILENAME = "descriptor_cache.dat";
	private static final int SERVERS_BEFORE_SAVE = 30;
	private static final int NEW_SERVERS_PER_SAVE = 20;
	private static final int MAX_SERVERS_IN_CACHE = 110;
	
	ArrayList<String> cache = new ArrayList<String>();
	int descriptorsAtLastSave = 0;
	
	/**
	 * Return a list of the raw server descriptors in the cache file.
	 * @return
	 */
	public static String[] getCache() {
		ArrayList<String> descriptors = new ArrayList<String>();
		String filename = TorConfig.getConfigDir() + DESCRIPTOR_CACHE_FILENAME;
		FileInputStream inf = null;
		
		try {
			inf = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(inf);
			try {
				while(true) {
					String currentDescriptor = (String)in.readObject();
					descriptors.add(currentDescriptor);
				}
			} catch (Exception e) {
				// Finished
			}
			in.close();
		} catch (Exception e) {
			// Oh well
		} finally {
			if (inf != null) {
				try {
					inf.close();
				} catch (IOException e) {
					// Oh well
				}
			}
		}
		Logger.logDirectory(Logger.INFO, "Descriptor cache loaded. " + descriptors.size() + " entries.");
		String[] ret = new String[descriptors.size()];
		descriptors.toArray(ret);
		return ret;
	}
	
	/**
	 * Add a raw server descriptor to the cache
	 * @param descriptor the raw descriptor
	 */
	public synchronized void addToCache(String descriptor) {
		
		if (cache.size() > MAX_SERVERS_IN_CACHE) return;
		
		cache.add(descriptor);

		Logger.logDirectory(Logger.INFO, "Adding descriptor #" + cache.size() + " to cache.");
		
		if ((cache.size()>SERVERS_BEFORE_SAVE) && (cache.size() > (descriptorsAtLastSave + NEW_SERVERS_PER_SAVE))) {
			saveCache();
		}
	}
	
	/**
	 * Save the cache out to an external file
	 */
	private void saveCache() {
		descriptorsAtLastSave = cache.size();
		String filename = TorConfig.getConfigDir() + DESCRIPTOR_CACHE_FILENAME;
		FileOutputStream outf = null;
		try {
			outf = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(outf);
			for (int i=0; i<descriptorsAtLastSave; i++) {
				String descriptor = cache.get(i);
				out.writeObject(descriptor);
			}
			out.close();
			outf.close();
			Logger.logDirectory(Logger.INFO, "Descriptor cache saved. " + descriptorsAtLastSave + " entries.");
		} catch (Exception e) {
			// Oh well
		} finally {
			if (outf != null) {
				try {
					outf.close();
				} catch (IOException e) {
					// Nothing to do
				}
			}
		}
	}

}
