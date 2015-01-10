import nodenet.ChannelDisabledException;
import nodenet.ChannelEmptyException;
import nodenet.ChannelFullException;
import nodenet.InputChannelVector;
import nodenet.NodeBehavior;
import nodenet.OutputChannelVector;


public class Zufall implements NodeBehavior {

	private Object obj = null;
	// die Zeiger der Input Channel's und der Output Channel's
	private	int next = 0, last = 0;
	
	@Override
	public void transmitPacket(InputChannelVector inputChannels, OutputChannelVector outputChannels) {
		
		// die Channelanzahl (Bei jedem Durchlauf aktualisiert )
		int inputSize = inputChannels.size();
		int outputSize = outputChannels.size();

		// Wenn keine Channel da sing
		if(inputSize == 0 || outputSize == 0)return;

		// Zeiger meines Inputchannels per Zufall berechnet
		next = (int) (Math.random() * inputSize);
		
		try{
			this.obj =  inputChannels.elementAt(next).readObject(); 
		}
		catch(ChannelEmptyException exc) { return; } 
		catch(ChannelDisabledException exc2) { return; }

		// Zeiger meines Outputchannels per Zufall berechnet
		last = (int) (Math.random() * outputSize);
		
		try{
			outputChannels.elementAt(last).writeObject(this.obj); 
		}
		catch(ChannelFullException exc) { return; } 
		catch(ChannelDisabledException exc2) { return; }

	}

}
