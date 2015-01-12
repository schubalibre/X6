import nodenet.ChannelDisabledException;
import nodenet.ChannelEmptyException;
import nodenet.ChannelFullException;
import nodenet.InputChannelVector;
import nodenet.NodeBehavior;
import nodenet.OutputChannelVector;


public class Verzoegerung implements NodeBehavior {

	private Object obj = null;
	// die Zeiger der Input Channel's und der Output Channel's
	private	int next = 0, last = 0;
	final int milsec = 2000;
	
	@Override
	public void transmitPacket(InputChannelVector inputChannels, OutputChannelVector outputChannels) {
		
		// die Channelanzahl (Bei jedem Durchlauf aktualisiert )
		int inputSize = inputChannels.size();
		int outputSize = outputChannels.size();

		// Wenn keine Channel da sing
		if(inputSize == 0 || outputSize == 0)return;

		// Zeiger meines Inputchannels - da modulo ist es egal ob er bei 0 anfängt
		next = (next+1) % inputSize;
		
		if(obj == null){
			try{
				obj =  inputChannels.elementAt(next).readObject(); 
			}
			catch(ChannelEmptyException exc) { return; } 
			catch(ChannelDisabledException exc2) { return; }
		}
		
		try {
			Thread.sleep(milsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return;
		}
		
		// Zeiger meines Outputchannels - da modulo ist es egal ob er bei 0 anfängt
		last = (last+1) % outputSize;
		
		try{
			outputChannels.elementAt(last).writeObject(obj);
			obj = null;
		}
		catch(ChannelFullException exc) { return; } 
		catch(ChannelDisabledException exc2) { return; }

	}

}
