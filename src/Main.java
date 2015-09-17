/*************************************************************************
 * @author Akshay Hacholli
 * 
 *         A programs which trains a single layer perceptron with logistic regression using gradient descent algorithm.
 * 
 *         Dependencies : TrainData.java
 *         Compilation : javac directory_path\*.java
 * 
 *         Input : train file path, test file path, learning rate, number of iterations data as command line arguments
 *         Output : Accuracy of training data and test data
 *         Execution: java Main "path_of_train_file" "path_of_test_file" learning_rate number_of_iterations
 *
 *         Example
 *         D:\>java Main "Data/Train.dat" "Data/Test.dat" 0.05 2000
 *         Accuracy of training file ( 15 instance ) = 93.33
 *         Accuracy of test data ( 14 instances ) = 85.71
 *************************************************************************/

/**
 * This class takes train file as an argument and process it to build train neural network, then this network is tested for accuracy
 * on train file and test file.
 */

import java.io.FileNotFoundException;

public class Main
{
	public static void main( String[] args )
	{
		try
		{
			final double threshold = 0.5;
			// We need four parameters train file, test file, learning rate and number of iterations as input
			if ( args.length < 4 )
			{
				System.out.println( "Please input train file, test file, learning rate, number of iterations as command line arguments." );
				return;
			}

			TrainData train_data = new TrainData( args[ 0 ] ); // This is a custom class which stores the train data from file.
			train_data.findWeights( Double.parseDouble( args[ 2 ] ), Integer.parseInt( args[ 3 ] ) );	//Finds the weights for the corresponding learning rate and iterations
			
			train_data.calculateAccuracyOfTrainFile( threshold );
			train_data.calculateAccuracyOfTestFile( threshold, args[ 1 ] );
		}
		catch ( FileNotFoundException e )
		{
			// Do nothing already output is printed
		}
	}
}
