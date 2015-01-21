import nodenet.ChannelDisabledException;
import nodenet.ChannelEmptyException;
import nodenet.ChannelFullException;
import nodenet.InputChannelVector;
import nodenet.NodeBehavior;
import nodenet.OutputChannelVector;


public class Regulierung implements NodeBehavior {

	private Object paket = null;
	private int aktuellePositionEingang = 0, aktuellePositionAusgang = 0, count = 0;
	@Override
	public void transmitPacket(InputChannelVector inputChannels, OutputChannelVector outputChannels) {

		int inputSize = inputChannels.size(); // gibt uns die Anzahl der Channels insgesamt wieder
		int outputSize = outputChannels.size();
		
		if(inputSize == 0 || outputSize == 0) return;// kontrolliert wieviele Kanäle im inputChannel sind 
		
		/*********************************************
		 *  InputChannel
		 ********************************************/
		
		/* 
		 * i ist der Zähler unsere Kanäle - Bei 5 Kanälen rufen wir inputChannels.elementAt(aktuellePositionEingang).readObject() 5 mal auf 
		 * 
		 * damit unser Paket nicht überschrieben wird konntrolieren wir, ob noch nichts in unserem paket steht 
		 *  
		 * */
		for(int i = 0; i < inputSize && paket == null;i++){
			
			try {
				
				paket = inputChannels.elementAt(aktuellePositionEingang).readObject();
				
			} catch (ChannelEmptyException e1) {
				System.out.println("Nix da zum Lesen");
				//e1.printStackTrace();
			} catch (ChannelDisabledException e1) {
				System.out.println("Input Kanal nicht aktiv");
				//e1.printStackTrace();
			}
			
			aktuellePositionEingang = (aktuellePositionEingang+1) % inputSize;
		}
		
		/*********************************************
		 *  OutputChannel
		 ********************************************/
		
		/* 
		 * i ist der Zähler unsere Kanäle - Bei 5 Kanälen rufen wir inputChannels.elementAt(aktuellePositionEingang).readObject() 5 mal auf 
		 * damit unser Paket nicht überschrieben wird konntrolieren wir, ob noch nichts in unserem paket steht
		 * 
		 * */

		for(int i = 0; i < outputSize && paket != null;i++){
			
			try {
				
				outputChannels.elementAt(aktuellePositionAusgang).writeObject(paket);
				paket = null;
				// bei jedem Erfolgreich übergebenen Paket setzen wir unseren Counter eins weiter
				count++;

			} catch (ChannelDisabledException e1) {
				System.out.println("Output Kanal nicht aktiv");
				//e1.printStackTrace();
			} catch (ChannelFullException e1){
				System.out.println("Kanal ist voll");
				//e1.printStackTrace();
			}
			// bei jedem Dritten versandtem Paket setzen wir unseren aktuellePositionAusgang eins weiter 
			//- somit erreichen wir, dass jeder Ausgang immer 3 Pakete versendet bevor er zum nächsten wechselt.
			if(count % 3 == 0){
				aktuellePositionAusgang = (aktuellePositionAusgang+1) % outputSize;
			}
		}
		
		
	}

}
