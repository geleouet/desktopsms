/**
 * 
 */
package fr.galize.desktopsms.comm;

import java.io.BufferedWriter;
import java.io.IOException;

final class RunnableImplementation implements Runnable {
		private final BufferedWriter _out;

		RunnableImplementation(BufferedWriter _out) {
			this._out = _out;
		}

		public void run() {
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			System.out.println("Send SMS");
			long l= System.currentTimeMillis();
			String id=Long.toString(l);
			//060-784-3903
			try {
				_out.write("SEND");
				_out.write(0);
				_out.write(id);
				_out.write(0);
				_out.write("0682887362");
//						_out.write("0607843903");
				_out.write(0);
				_out.write("Gros Bisous");
				_out.write(0);
				_out.write("OK");
				_out.flush();
				System.out.println("SMS Sent");
				_out.write("GET");
				_out.write(0);
				_out.flush();
				System.out.println("SEND GET");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}