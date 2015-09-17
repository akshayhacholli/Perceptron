/*************************************************************************
 * @author Akshay
 *         A class used to store train data from file such that it becomes easy for building neural network.
 *         Dependencies : Attribute.java
 **************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrainData
{
	List< Attribute > attribute_list;
	Attribute class_data;

	public TrainData ( )
	{
		attribute_list = new ArrayList< Attribute >();
		class_data = new Attribute( -1, "" );
	}

	public TrainData ( String fileName ) throws FileNotFoundException
	{
		this();
		readFile( fileName );
	}

	/**
	 * Reads the file passed as a parameter and then writes the content into the custom data
	 * structure
	 * 
	 * @param fileName - Train data file name (Either absolute path or relative)
	 * @throws FileNotFoundException
	 */
	private void readFile( String fileName ) throws FileNotFoundException
	{
		try
		{
			File train_file = new File( fileName );
			Scanner in;
			in = new Scanner( train_file );

			String[] tokens = in.nextLine().trim().split( "\\s+" ); // Parse the file
			this.attribute_list.add( new Attribute( 0, "x0" ) );

			int i = 1;
			while ( i <= tokens.length )
			{
				this.attribute_list.add( new Attribute( i, tokens[ i - 1 ] ) );
				i++;
			}

			while ( in.hasNextLine() )
			{
				tokens = in.nextLine().trim().split( "\\s+" );
				if ( tokens.length > 1 )
				{
					this.addTrainData( 0, 1 );

					for ( i = 1; i < tokens.length; i++ )
						this.addTrainData( i, Integer.parseInt( tokens[ i - 1 ] ) );

					// Last column would be the class attribute
					this.class_data.add_data( Integer.parseInt( tokens[ i - 1 ] ) );
				}
			}

			in.close();
		}
		catch ( FileNotFoundException e )
		{
			System.out.println( "Cannot find train file - " + fileName );
			throw e;
		}
	}

	/**
	 * Builds neural network by identifying weights.
	 * 
	 * @param learning_rate - Learning Rate
	 * @param iterations - Number of iterations to be run
	 * @return - Decision Tree
	 */
	protected void findWeights( double learning_rate, int iterations )
	{
		double sum, error, o, delta;
		int n = this.class_data.attribute_data.size();
		Attribute temp;

		for ( int i = 0; i < iterations; i++ )
		{
			sum = 0;
			for ( int j = 0; j < this.attribute_list.size(); j++ )
			{
				temp = this.attribute_list.get( j );
				sum += temp.attribute_data.get( i % n ) * temp.getWeigth();		//Sum of wi * xi
			}

			o = getSigmoidValue( sum );										//Get the value from sigmoid function
			error = this.class_data.attribute_data.get( i % n ) - o;		//Find out the error
			delta = learning_rate * error * o * (1.0 - o);					//Delta w

			for ( int j = 0; j < this.attribute_list.size(); j++ )
				this.attribute_list.get( j ).incrementWeigth( delta, i % n );		//Update weights
		}
	}
	
	/**
	 * Calculates accuracy of training data.
	 * 
	 * @param threshold - Threshold to classify output, e.x - If value greater than 0.5 classify out as 1 else 0
	 */

	protected void calculateAccuracyOfTrainFile( double threshold )
	{
		int result = 0, n = this.class_data.attribute_data.size();
		double sum, total = 0;
		Attribute temp;

		for ( int i = 0; i < n; i++ )
		{
			sum = 0;
			for ( int j = 0; j < this.attribute_list.size(); j++ )
			{
				temp = this.attribute_list.get( j );
				sum += temp.attribute_data.get( i ) * temp.getWeigth();
			}

			if ( sum < threshold )
				result = 0;
			else
				result = 1;

			if ( result == this.class_data.attribute_data.get( i ) )
				total++;
		}

		DecimalFormat df = new DecimalFormat( "#.##" );
		System.out.println( "Accuracy of training file ( " + n + " instance ) = " + df.format( (total * 100.00 / n) ) );
	}

	/**
	 * Calculates accuracy of test data using trained network.
	 * 
	 * @param threshold - Threshold to classify output, e.x - If value greater than 0.5 classify out as 1 else 0
	 * @param test_file_name - Test data file name (Either absolute path or relative)
	 * @throws FileNotFoundException
	 */
	
	protected void calculateAccuracyOfTestFile( double threshold, String test_file_name ) throws FileNotFoundException
	{
		try
		{
			File test_file = new File( test_file_name );
			Scanner in;
			String[] tokens;
			int total_tokens = 0, total_matched = 0, n, result;
			double sum;
			Attribute temp;

			in = new Scanner( test_file );
			in.nextLine();

			while ( in.hasNextLine() )
			{
				tokens = in.nextLine().trim().split( "\\s+" );
				n = tokens.length;
				if ( n > 1 )
				{
					temp = this.attribute_list.get( 0 );
					sum = temp.getWeigth();

					for ( int i = 1; i < n; i++ )
					{
						temp = this.attribute_list.get( i );
						sum += Integer.parseInt( tokens[ i - 1 ] ) * temp.getWeigth();
					}

					if ( sum < threshold )
						result = 0;
					else
						result = 1;

					if ( Integer.parseInt( tokens[ n - 1 ] ) == result )
						total_matched++;

					total_tokens++;
				}
			}

			DecimalFormat df = new DecimalFormat( "#.##" );
			System.out.println( "Accuracy of test data ( " + total_tokens + " instances ) = " + df.format( total_matched * 100.00 / total_tokens ) );

			in.close();
		}
		catch ( FileNotFoundException e )
		{
			System.out.println( "Cannot find test file - " + test_file_name );
			throw e;
		}
	}

	/**
	 * Sigmoid Function ( 1 / ( 1 + e ^ -x) )
	 * @param x - Input to sigmoid function
	 */
	private double getSigmoidValue( double x )
	{
		return (1 / (1 + Math.exp( -x )));
	}

	/**
	 * Adds training data column wise in their respective attribute.
	 * 
	 * @param i - column position of attribute within the file
	 * @param val - training data
	 */
	protected void addTrainData( int i, int val )
	{
		this.attribute_list.get( i ).add_data( val );
	}
}
