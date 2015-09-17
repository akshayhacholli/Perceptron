/**
 * This class is used to represent the attributes from the train file.
 * It contains all the information for the attribute such as Name, Values, position,training data values
 * 
 * @author Akshay
 */


import java.util.ArrayList;

public class Attribute
{
	int index;
	String name;
	Double weigth;
	ArrayList< Integer > attribute_data;

	public Attribute ( int index, String name )
	{
		this.index = index;
		this.name = name;
		this.attribute_data = new ArrayList< Integer >();
		this.weigth = 0.0;			//Initialize weight to zero
	}

	protected void add_data( int val )
	{
		this.attribute_data.add( val );
	}

	public Double getWeigth()
	{
		return weigth;
	}

	/**
	 * Updates the weight value
	 * @param delta - value by which weight has to be updated
	 * @param index - index of train value to be multiplied with
	 */
	public void incrementWeigth( double delta, int index )
	{
		this.weigth += delta * this.attribute_data.get( index );
	}
}
