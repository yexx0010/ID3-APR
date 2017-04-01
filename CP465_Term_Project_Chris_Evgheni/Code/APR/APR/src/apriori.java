/**
 * @author 	Evgheni Naida
 * @version 2013-11-30
 * 			ID: 090305930
 * 			email: naid5930@mylaurier.ca
 *
 * 	Mines Association rules of the database 'transactions.txt'
 * based on config file 'config.txt' which contains # of attributes,  # of transactions
 * and min support in %. 
 * 	The output 'output.txt' is produced, containing association rules, their frequency
 * and their support.
 * 
 */





import java.io.*;
import java.text.DecimalFormat;
import java.util.*;



public class apriori {
    public static void main(String[] args) {
        AprioriAlgorithm apr = new AprioriAlgorithm();
        apr.Process();
    }
}
