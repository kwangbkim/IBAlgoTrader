import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.ib.client.tws.DataMiner;

public class UpdatePrices {

	private DataMiner MyMiner = new DataMiner();
	
	public static void main(String[] args)
	{
		UpdatePrices a = new UpdatePrices();
		
		a.GetDataFromIB();
		
		// update world stock price correlations
		Runtime r = Runtime.getRuntime();
		try {
			Process child = r.exec("/bin/sh");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(child.getOutputStream()));
			
			bw.write("cd \"/Users/Kwang/Investment Management/Statistics/R\"" + "\n");
			bw.flush();
			
			bw.write("./RunVTCorrelations.sh" + "\n");
			bw.flush();
			
			bw.write("exit" + "\n");
			bw.flush();
			
			try {
				child.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("failed to run correlation calculation");
			System.out.println(e.getMessage());
		}
	}
	
	private void GetDataFromIB()
	{
		MyMiner.updateData();
		MyMiner.disconnect();
	}
}
