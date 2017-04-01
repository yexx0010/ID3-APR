/**
 * @author 	Evgheni Naida
 * @version 2013-11-30
 * 			ID: 090305930
 * 			email: naid5930@mylaurier.ca
 *
 * 	AprioriAlgorithm class. Mines Association rules of the database 'transactions.txt'
 * based on config file 'config.txt' which contains # of attributes,  # of transactions
 * and min support in %. 
 * 	The output 'output.txt' is produced, containing association rules, their frequency
 * and their support.
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Generates Apriori itemsets
 */
class AprioriAlgorithm{
	
	//---------------I/O files
    String configFile="config.txt"; 		//configuration file
    String transactionsFile="transactions.txt"; 	//transaction file
    String outputFile="output.txt";			//output file
	
    
    
    int numItems; 							//#of columns/items
    int numTransactions; 					//number of transactions
    double minSup; 							//minimum support
    
    Vector<String> candidates=new Vector<String>(); //the current candidates
    
    String oneVal[]; 						//value of columns treated as TRUE, array
    String itemSeparator = ","; 			//default item separator

    
    
    //--------------------Generates Apriori Itemsets
    public void Process(){
    	int itemsetNumber=0; 				//the current itemset
        int z = 0;							//frequency itemset counter
        getConfig();
        System.err.println("Apriori has started!!!!!!\n");
        

        do{										//loop until complete
            itemsetNumber++;					//increment the itemset
            generateCandidates(itemsetNumber);	//generate candidates

            z = calculateFrequentItemsets(itemsetNumber);//determine frequent itemsets
            if(candidates.size()!=0){
                System.out.println(itemsetNumber + "-item itemsets: " + (z-1));//print the count of itemsets
            }
        
        }while(candidates.size()>1);//if <=1 frequent items, then end
        System.out.println("\r\nDone! \r\nFor details check the output file called:   " + outputFile);
    }

    

    
    
    /**
     * Gets Configutaion information from config.txt, allows changes in
     * separator, and TRUE column values
     */
    private void getConfig()
    {
        FileWriter fw;
        BufferedWriter file_out;

        String input="";

        //Separator change
        System.out.println("\nPress 'C' to change the item separator(default: ','), any other key to continue.  ");
        input=getInput();
        if(input.compareToIgnoreCase("c")==0){
            System.out.print("Enter the separating character for items (return for '"+itemSeparator+"'): ");
            input=getInput();
            if(input.compareToIgnoreCase("")!=0)
                itemSeparator=input;
        }

        try{
        	 //--------------------Read the config file
             FileInputStream file_in = new FileInputStream(configFile); 	//get config file
             BufferedReader data_in = new BufferedReader(new InputStreamReader(file_in)); //read the data
             numItems=Integer.valueOf(data_in.readLine()).intValue();		//1st line, # of colmns
             numTransactions=Integer.valueOf(data_in.readLine()).intValue();//2nd line, # of tnscn
             minSup=(Double.valueOf(data_in.readLine()).doubleValue());		//3rd line, min supp
             minSup/=100.0; 												//min supp to decimal
             
             
            //---------------------Change the TRUE value of each column 
            oneVal = new String[numItems];
            System.out.print("Press 'Y' to change what row value is recognized as TRUE (default: 'y'):");
            if(getInput().compareToIgnoreCase("y")==0){
                for(int i=0; i<oneVal.length; i++){
                    System.out.print("Enter value of column #" + (i+1) + ": ");
                    oneVal[i] = getInput();
                }
            }
            else for(int i=0; i<oneVal.length; i++) oneVal[i]="y"; //default is 'y'
            
            
            
            //---------------------Generating Output file
            fw= new FileWriter(outputFile);
            file_out = new BufferedWriter(fw);
            //create the header of the output file, containing important info about config
            file_out.write("Number of Transactions: " + numTransactions + "\r\n");
            file_out.write("Number of Items/Columns: " + numItems + "\r\n");
            file_out.write("Min Support: " + minSup + "\r\n");
            file_out.write("-----------------------------------------------------------\r\n");
            file_out.write("Column values considered as True:\r\n");
            for(int i=0; i<oneVal.length; i++){
                file_out.write("\tColumn" + (i+1) + ": " + oneVal[i] + "\r\n");
            }
            
            file_out.write("\r\n");
            file_out.close();
        }
        

        catch(IOException e){ System.out.println(e); }
    }

    
    
    
    /**
     * Generates all possible candidates for the n-th itemsets,
     * candidates are stored in the candidates vector
     * 
     * @param: int n, current itemset
     */
    private void generateCandidates(int set){
        String string1, string2; 			//strings that will be used for comparisons
        StringTokenizer st1, st2; 			//string tokenizers
        Vector<String> tmpCand = 
        		new Vector<String>(); 		//temp candidate vector
        

        if(set == 1){//first set case
            for(int i = 1; i <= numItems; i++){ tmpCand.add(Integer.toString(i));
            }
        }
        else if(set == 2){ //second set case
            for(int i = 0; i < candidates.size(); i++){
                st1 = new StringTokenizer(candidates.get(i));
                string1 = st1.nextToken();
                for(int j = i+1; j < candidates.size(); j++){
                    st2 = new StringTokenizer(candidates.elementAt(j));
                    string2 = st2.nextToken();
                    tmpCand.add(string1 + " " + string2);
                }
            }
        }
        else{//all other set cases
            for(int i=0; i<candidates.size(); i++){
                for(int j=i+1; j<candidates.size(); j++){
                    string1 = new String();
                    string2 = new String();
                    st1 = new StringTokenizer(candidates.get(i));
                    st2 = new StringTokenizer(candidates.get(j));

                    for(int s = 0; s < set-2; s++){
                        string1 = string1 + " " + st1.nextToken();
                        string2 = string2 + " " + st2.nextToken();
                    }
                    if(string2.compareToIgnoreCase(string1)==0)//if same n-2 tokens, add
                        tmpCand.add((string1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
                }
            }
        }
        
        candidates.clear(); 					 //del old cand
        candidates = new Vector<String>(tmpCand);//store new
        tmpCand.clear();
    }

    
    

    
    
    /**
     * Identify frequency in the itemsets, based on min supp
     * 
     * @param: int n, itemset to evaluate
     * @return: int z, count of frequent itemsets, that meet minSup condition
     */
    private int calculateFrequentItemsets(int n){
        Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the 
        							//current itemset, with proper support
        FileInputStream file_in; 							//file input stream
        BufferedReader data_in; 							//data input stream
        BufferedWriter file_out;							//output file
        FileWriter fw;										//file writer obj
        StringTokenizer stCandidate, stTransaction; 		//tokenizer
        boolean itemFound; 									//true, if transaction matches itemset
        boolean transAttributes[] = new boolean[numItems]; 	//array, holding attribs of transaction after delim
        int count[] = new int[candidates.size()]; 			//count intemFinds
        int z = 1;											//frequency itemset counter

        
        try{
                fw= new FileWriter(outputFile, true); 
                file_out = new BufferedWriter(fw);
                
                file_in = new FileInputStream(transactionsFile);
                data_in = new BufferedReader(new InputStreamReader(file_in));

                
                //-----------Count the number of occurences
                for(int i=0; i<numTransactions; i++){ //iterate each transaction, store in array
                	stTransaction = new StringTokenizer(data_in.readLine(), itemSeparator); //read transaction 
                    for(int j=0; j<numItems; j++){
                    	transAttributes[j]=(stTransaction.nextToken().compareToIgnoreCase(oneVal[j])==0);
                    }
                    for(int c=0; c<candidates.size(); c++){ //check each candidate
                        itemFound = false;
                        stCandidate = new StringTokenizer(candidates.get(c));//see what items
                        while(stCandidate.hasMoreTokens()){//check if item is in transaction
                            itemFound = (transAttributes[Integer.valueOf(stCandidate.nextToken())-1]);
                            if(!itemFound) break;
                        }
                        if(itemFound) count[c]++; //count if found
                    }

                }
                
                
                //--------------------Write to File all the candidates with proper support
                file_out.write("---------------Frequency " + n + "-itemsets ---------------\r\n" );
                for(int i=0; i<candidates.size(); i++){
                	double support = count[i]/(double)numTransactions;
                	if(support>=minSup){//each candidate with >min supp
                        frequentCandidates.add(candidates.get(i)); //add to vector
                        DecimalFormat format = new DecimalFormat("0.000"); //formatting
                        file_out.write("frequency-itemset " + z + ": [" + candidates.get(i) 
                        		+ "]" + "\t\t#ofT: " + count[i] + "\t\tsup: " + format.format(support) + "\r\n" );
                        z++;
                    }
                }
                file_out.write("\r\n");
                file_out.close();
        }
       
        catch(IOException e) { System.out.println(e); } //Catch I/O error
       
        candidates.clear();//clear old candidates and store new candidates
        candidates = new Vector<String>(frequentCandidates);
        frequentCandidates.clear();
        return z;
    }
    
    
    
    /**
  	 * Gets user input from System.in
     * @return: String, user input
     */
    public static String getInput(){
        String input="";
        BufferedReader inpt = new BufferedReader(new InputStreamReader(System.in));
        
        try{ input = inpt.readLine();} //error handling
        catch (Exception e){ System.out.println(e);}
        return input;
    }
    
    
    
}