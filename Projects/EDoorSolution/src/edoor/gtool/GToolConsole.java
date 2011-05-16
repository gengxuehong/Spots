package edoor.gtool;

public class GToolConsole implements edoor.Command.IConsole
{
	
	@Override
	public void print(String fmt, Object... params) {
		String output = String.format(fmt, params);	
		System.out.print(output);
	}


}
