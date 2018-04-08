package pl.gregorl.Grzmote;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import pl.gregorl.Grzmote.Common.Command;
import pl.gregorl.Grzmote.Common.ICommand;
import pl.gregorl.Grzmote.Network.CommandSender;
import pl.gregorl.Grzmote.Network.ICommandSender;

public class CommandSendingTabActivity extends BaseTabActivity implements
		ICommandSendingActivity {

	protected ICommandSender commandSender;
	
	public CommandSendingTabActivity()
	{
		super();
		commandSender = new CommandSender();
	}
	
	
	@Override
	public void sendCommand(ICommand command) throws UnknownHostException,
			IOException {
		this.commandSender.Send(Settings.getServerAddress(),command);

	}
	
	protected void adjustImageButton(ImageButton button,final String command) {
        button.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View arg0) {
					try {
						sendCommand(new Command(command));
					} catch (UnknownHostException e) {
						showAlertOnError(e);
					} catch (IOException e) {
						showAlertOnError(e);
					} catch(Exception exc){
						showAlertOnError(exc);
					}
			}
		});
	}
	
	
	private void showAlertOnError(Exception e){
		
		AlertDialog a = (new AlertDialog.Builder(getBaseContext())).create();
		a.setMessage("Connection problem: " + e.getLocalizedMessage());
		a.show();
		
	}
	

}
