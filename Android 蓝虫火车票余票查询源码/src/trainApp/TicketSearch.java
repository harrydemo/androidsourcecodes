package trainApp;

import java.util.ArrayList;
import java.util.List;

import trainApp.http.HttpClient;
import trainApp.http.PostDataProvider;
import trainApp.http.ticket.TicketHttp;

public class TicketSearch
{

	public TicketSearch(String url)
	{
		this.postDataProvider = PostDataProvider.getInstance();
		this.postUrl = url;
	}

	private PostDataProvider	postDataProvider	= null;
	private String				month				= "";
	private String				day					= "";
	private String				startStation		= "";
	private String				arriveStation		= "";
	private String				trainCode			= "";
	private String				rFlag				= "";

	private String				name_cbkall			= "";
	private List<String>		trainFlags			= null;
	private String				postUrl				= "";

	public void setMonth(String month)
	{
		this.month = month;
	}

	public void setDay(String day)
	{
		this.day = day;
	}

	public void setStartStation(String startStation)
	{
		this.startStation = startStation;
	}

	public void setArriveStation(String arriveStation)
	{
		this.arriveStation = arriveStation;
	}

	public void setTrainCode(String trainCode)
	{
		this.trainCode = trainCode;
	}

	public void setRFlag(String rFlag)
	{
		this.rFlag = rFlag;
	}

	public void setName_CheckAll(String name_cbkall)
	{
		this.name_cbkall = name_cbkall;
	}

	public void setTrainFlag(List<String> trainFlags)
	{
		this.trainFlags = new ArrayList<String>();
		this.trainFlags = trainFlags;
	}

	public String post(String lx_value)
	{
		String msg = "";
		if (this.postDataProvider == null) return "";

		try
		{
			this.postDataProvider.setPostData(this.month, this.day,
					this.startStation, this.arriveStation, this.trainCode,
					this.rFlag);

			if (this.name_cbkall == PostDataProvider.NAME_CKBALL_VALUE)
			{
				this.postDataProvider.addPostData();
			}
			else
			{
				for (int i = 0; i < trainFlags.size(); i++)
				{
					String flag = trainFlags.get(i);
					if (flag == "DC")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGDC, "DC");
					}
					else if (flag == "Z")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGZ, "Z");
					}
					else if (flag == "T")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGT, "T");
					}
					else if (flag == "K")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGK, "K");
					}
					else if (flag == "PK")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGPK, "PK");
					}
					else if (flag == "PKE")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGPKE, "PKE");
					}
					else if (flag == "LK")
					{
						this.postDataProvider.addPostData(
								PostDataProvider.TFLAGLK, "LK");
					}
				}

			}

			//
			this.postDataProvider.addLXPostData(lx_value);

			msg = TicketHttp.post(this.postUrl,
					this.postDataProvider.getPostData());
			if(msg.equals("")){
				msg = HttpClient.post(this.postUrl, this.postDataProvider.getPostData());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return msg;
	}
}
