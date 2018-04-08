package pl.gregorl.Grzmote.Network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import pl.gregorl.Grzmote.Common.ICommand;

public class CommandSender implements ICommandSender {

	public CommandSender(){
		
	}

	@Override
	public void Send(String address , ICommand command) throws UnknownHostException, IOException {
		
			(new SenderThread(address, 6969, command)).start();
	}
	
}

class SenderThread extends Thread
{
	private String address;
	private int port;
	private ICommand command;
	
	public SenderThread(String address, int port, ICommand command ){
		this.address = address;
		this.port = port;
		this.command = command;
	}
	
	public void run()
	{
		Socket s = null;
		ObjectOutputStream oos = null;
		try {
			s = new Socket(this.address,this.port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(this.command);
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
		finally{
			try {
				if(oos!=null)
				{
					oos.close();
				}
				if(s!=null){
					s.close();
				}
			} catch (IOException e) {
				
			}
		}
	}
	
}

