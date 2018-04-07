/**
 * 
 */
package weibo4andriod;

import java.util.ArrayList;
import java.util.List;

import weibo4andriod.http.Response;
import weibo4andriod.org.json.JSONArray;
import weibo4andriod.org.json.JSONException;
import weibo4andriod.org.json.JSONObject;

/**
 * @author hezhou
 *
 */
public class Count implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9076424494907778181L;

	private long id;
	
	private long comments;
	
	private long rt;
	
	private long dm;
	
	private long mentions;
	
	private long followers;
	
	public Count(JSONObject json)throws WeiboException, JSONException{
    	id = json.getLong("id");
    	comments = json.getLong("comments");
    	rt = json.getLong("rt");
    	dm = json.getLong("dm");
    	mentions = json.getLong("mentions");
    	followers = json.getLong("followers");
    }
	
	static List<Count> constructCounts(Response res) throws WeiboException {
	   	 try {
	            JSONArray list = res.asJSONArray();
	            int size = list.length();
	            List<Count> counts = new ArrayList<Count>(size);
	            for (int i = 0; i < size; i++) {
	            	counts.add(new Count(list.getJSONObject(i)));
	            }
	            return counts;
	        } catch (JSONException jsone) {
	            throw new WeiboException(jsone);
	        } catch (WeiboException te) {
	            throw te;
	        }
	   }
	
	@Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof Count && ((Count) obj).id == this.id;
    }

    public long getComments() {
		return comments;
	}

	public long getRt() {
		return rt;
	}

	public long getDm() {
		return dm;
	}

	public long getMentions() {
		return mentions;
	}

	public long getFollowers() {
		return followers;
	}

	@Override
    public String toString() {
        return "Count{ id=" + id +
                ", comments=" + comments +
                ", rt=" + rt + 
                ", dm=" + dm + 
                ", mentions=" + mentions + 
                ", followers=" + followers + 
                '}';
    }
}
