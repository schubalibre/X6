import nodenet.ChannelDisabledException;
import nodenet.ChannelEmptyException;
import nodenet.ChannelFullException;
import nodenet.InputChannelVector;
import nodenet.NodeBehavior;
import nodenet.OutputChannelVector;


public class Regulierung implements NodeBehavior {

	private Object obj = null;
	// die Zeiger der Input Channel's und der Output Channel's
	private	int next = -1, last = 0, count = 0;

	@Override
	public void transmitPacket(InputChannelVector inputChannels, OutputChannelVector outputChannels) {
		
		// die Channelanzahl (Bei jedem Durchlauf aktualisiert )
		int inputSize = inputChannels.size();
		int outputSize = outputChannels.size();

		// Wenn keine Channel da sing
		if(inputSize == 0 || outputSize == 0)return;

		// Zeiger meines Inputchannels - er wird einmal gesetzt, zufällig und dann beibehalten
		if(next <= 0) next = (int) (Math.random() * inputSize);
		
		if(obj == null){
			try{
				obj =  inputChannels.elementAt(next).readObject(); 
			}
			catch(ChannelEmptyException exc) { return; } 
			catch(ChannelDisabledException exc2) { return; }
		}
		
		// gibt das Paket aller drei Zyklen weiter.
		if(count % 3 == 0){
			System.out.println(count);
			// Zeiger meines Outputchannels - da modulo ist es egal ob er bei 0 anfängt
			last = (last+1) % outputSize;
			
			try{
				outputChannels.elementAt(last).writeObject(obj); 
				obj = null;
			}
			catch(ChannelFullException exc) { return; } 
			catch(ChannelDisabledException exc2) { return; }
		}
		
		count++;
		System.out.println(" ---------------- " + count);
	}

}
