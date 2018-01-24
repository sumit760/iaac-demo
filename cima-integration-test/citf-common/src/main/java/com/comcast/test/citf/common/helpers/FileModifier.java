package com.comcast.test.citf.common.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * Utility class for file modification.
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 *
 */

@Service("fileModifier")
public class FileModifier {
	
	public static final int MAX_COUNT_UNLIMITED = -1;
	
	public int getNextExecutionSequence(){
		return execSequence++;
	}
	
	/**
	 * This is the type of expected modification, i.e. it should ADD when one need to add any 
	 * content in the file.
	 *
	 */
	public enum OperationType{
		ADD,
		REMOVE
	}
	
	/**
	 * This is the type of instruction.
	 * i.e. if one needs to modify the lines of files say first 30 matching lines with instruction1 
	 * and the next 20 with instruction2 and so on, then the instruction1 and instruction2 will be 
	 * serial with executionSequnce respectively 1 and 2. Also instruction1 and instruction2 should have
	 * maxChangecount as 30 and 20 respectively.
	 * <p>
	 * If the modification is needed for all the lines wherever match found, then the InstructionType 
	 * should be FREE.
	 */
	public enum InstructionType{
		SERIAL,
		FREE
	}
	
	/**
	 * Class to generate the modify instructions.
s	 */
	public class ModifyInstruction{
		
		private String value;
		private OperationType operationType;
		private InstructionType instructionType;
		private int maxChangecount;
		private int executionSequnce;
		
		//filters
		private String[] includeORFilter;
		private String[] excludeORFilter;
		
		private boolean executionOver = false;
		
		/**
		 * Constructor to initialize ModifyInstruction in case of sequential.
		 * 
		 * @param value
		 * 					For ADD operation, this value will be added at the end of matched line.
	     * 					For REMOVE operation, this value will be replace by blank space wherever
	     * 				 	found in the matched line.
		 * @param operationType
		 * 					Type of operation, i.e. add/remove.
		 * @param maxChangecount
		 * 					Maximum No of changes need to done for this instruction.
		 * @param includeORFilter
		 * 					This is a array of CSV Strings. Any of these CSV Strings must be matched 
		 * 					in the line to modify the line. And each of values under a CSV Strings 
		 * 					must have to be individually present in the line to qualify filtering condition.
		 * @param excludeORFilter
		 * 					This is a array of CSV Strings. Similar like includeORFilter, except these are 
		 * 					filter for rejecting any lines even if it has been qualified includeORFilter condition.
		 * @param executionSequnce
		 * 					Sequence No. in order to execute this instruction among the list of instructions, -1 
		 * 					indicates this should be treated separately.
		 */
		public ModifyInstruction(	String value, 
									OperationType operationType, 
									int maxChangecount,  
									String[] includeORFilter, 
									String[] excludeORFilter,
									int executionSequnce){
			this.value = value;
			this.operationType = operationType;
			this.instructionType = InstructionType.SERIAL;
			this.maxChangecount = maxChangecount;
			this.executionSequnce = executionSequnce;
			this.includeORFilter = includeORFilter!=null ? includeORFilter.clone() : null;
			this.excludeORFilter = excludeORFilter!=null ? excludeORFilter.clone() : null;
		}
		
		/**
		 * Overloaded constructor for ModifyInstruction in case of parallel.
		 * 
		 * @param value
		 * 					For ADD operation, this value will be added at the end of matched line.
	     * 					For REMOVE operation, this value will be replace by blank space wherever
	     * 				 	found in the matched line.
		 * @param operationType
		 * 					Type of operation, i.e. add/remove.
		 * @param maxChangecount
		 * 					Maximum No of changes need to done for this instruction.
		 * @param includeORFilter
		 * 					This is a array of CSV Strings. Any of these CSV Strings must be matched 
		 * 					in the line to modify the line. And each of values under a CSV Strings 
		 * 					must have to be individually present in the line to qualify filtering condition.
		 * @param excludeORFilter
		 * 					This is a array of CSV Strings. Similar like includeORFilter, except these are 
		 * 					filter for rejecting any lines even if it has been qualified includeORFilter condition.
		 */
		public ModifyInstruction(	String value, 
									OperationType operationType, 
									int maxChangecount, 
									String[] includeORFilter, 
									String[] excludeORFilter){
			this.value = value;
			this.operationType = operationType;
			this.instructionType = InstructionType.FREE;
			this.maxChangecount = maxChangecount;
			this.executionSequnce = -1;
			this.includeORFilter = includeORFilter!=null ? includeORFilter.clone() : null;
			this.excludeORFilter = excludeORFilter!=null ? excludeORFilter.clone() : null;
		}
		
		/**
		 * Gets the value.
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 * 
		 * @param value
		 * 			Value in the instruction
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Get operation type.
		 *  
		 * @return Operation type.
		 */
		public OperationType getOperationType() {
			return operationType;
		}

		/**
		 * Sets operation type.
		 * 
		 * @param operationType
		 * 			Type of operation
		 * 
		 * @see com.comcast.test.citf.common.helpers.FileModifier.OperationType
		 */
		public void setOperationType(OperationType operationType) {
			this.operationType = operationType;
		}

		/**
		 * Get Max change count value.
		 * 
		 * @return Max change count value.
		 */ 
		public int getMaxChangecount() {
			return maxChangecount;
		}

		/**
		 * Sets max change count value.
		 * 
		 * @param maxChangecount
		 * 			Maximum No. of changes
		 */
		public void setMaxChangecount(int maxChangecount) {
			this.maxChangecount = maxChangecount;
		}

		/**
		 * Get execution sequence.
		 * 
		 * @return Execution sequence.
		 */
		public int getExecutionSequnce() {
			return executionSequnce;
		}

		/**
		 * Sets execution sequence.
		 * 
		 * @param executionSequnce
		 * 			Sequence of execution
		 */
		public void setExecutionSequnce(int executionSequnce) {
			this.executionSequnce = executionSequnce;
		}

		/**
		 * Get includeORfilter.
		 * 
		 * @return includeORfilter.
		 */
		public String[] getIncludeORFilter() {
			return includeORFilter!=null ? includeORFilter.clone() : null;
		}

		/**
		 * Sets includeORfilter.
		 * 
		 * @param includeORFilter
		 * 			Array of include filter conditions
		 */
		public void setIncludeORFilter(String[] includeORFilter) {
			this.includeORFilter = includeORFilter!=null ? includeORFilter.clone() : null;
		}

		/**
		 * Get excludeORfilter.
		 * 
		 * @return excludeORfilter
		 */
		public String[] getExcludeORFilter() {
			return excludeORFilter!=null ? excludeORFilter.clone() : null;
		}

		/**
		 * Sets excludeORfilter.
		 * 
		 * @param excludeORFilter
		 * 			Array of exclude filter conditions
		 */
		public void setExcludeORFilter(String[] excludeORFilter) {
			this.excludeORFilter = excludeORFilter!=null ? excludeORFilter.clone() : null;
		}

		/**
		 * Checks if test execution is over or not.
		 * 
		 * @return true, if execution is over, false otherwise.
		 */
		public boolean isExecutionOver() {
			return executionOver;
		}

		/**
		 * Sets execution over flag.
		 * 
		 * @param executionOver 
		 * 			executionOver flag.
		 */
		public void setExecutionOver(boolean executionOver) {
			this.executionOver = executionOver;
		}

		/**
		 * Get instruction type.
		 * 
		 * @return instruction type.
		 */
		public InstructionType getInstructionType() {
			return instructionType;
		}

		/**
		 * Sets instruction type.
		 * 
		 * @param instructionType
		 * 			Type of instruction	@see com.comcast.test.citf.common.util.FileModifier.InstructionType
		 */
		public void setInstructionType(InstructionType instructionType) {
			this.instructionType = instructionType;
		}
		

		@Override
	    public int hashCode() {
	       return (this.value!=null?this.value.hashCode():0) + this.instructionType.hashCode() + this.operationType.hashCode()
	    		   + Arrays.hashCode(this.includeORFilter) + Arrays.hashCode(this.excludeORFilter);
	    }
	 
	    @Override
	    public boolean equals(Object obj) {
	       if(obj instanceof ModifyInstruction){
	    	   ModifyInstruction change = (ModifyInstruction)obj;
	    	   if(this.getValue().equals(change.getValue()) && this.getOperationType().equals(change.getOperationType()) 
	    			   && this.getInstructionType().equals(change.getInstructionType()) && this.executionSequnce==change.executionSequnce){
	    		   return true;
	    	   }
	       }
	       
	       return false;
	    }
	 
	    @Override
	    public String toString() {
	        return new MessageFormat("ModifyInstruction [Operation type={1}, Instruction type={2}, value={3}, maximum change count={4}, executionSequnce={5}, includeORFilter={6}, excludeORFilter={7} and execution is {8}")
	        		.format(new Object[] {this.operationType, this.instructionType, this.value, this.maxChangecount, this.executionSequnce, this.includeORFilter, this.excludeORFilter, this.executionOver?" over]":" not over]"});
	    }
		
	}
		
	
	/**
	 * Modifies the source file as per the modification instruction and 
	 * writes the modified file content to the destination file.
	 * 
	 * @param srcfileName
	 * 					 	Source file name.
	 * @param destfileName
	 * 						Destination file name.
	 * @param instructions
	 * 						The serial instruction should be given in a sorted 
	 * 						order in which the modifications need to take place.
	 * @param isDestinationTemporary
	 * 						It should be 'true' for not to change anything in the 
	 * 						source file and keeping the destination file as the 
	 * 						modified one and vice versa.
	 * @return true if operation succeeded, else false
	 */
	public boolean modifyFile(	String srcfileName, 
								String destfileName, 
								LinkedList<ModifyInstruction> instructions, 
								boolean isDestinationTemporary) throws IllegalStateException, IOException{
		boolean isSucceed = false;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		FileReader srcFileReader = null;
		Resource srcPR;
		LinkedList<ModifyInstruction> serialInstructions = null;
		LinkedList<ModifyInstruction> freeInstructions = null;
		
		LOGGER.info("Starting to modify {} file with instructions : {} and the {} file is {}", 
							srcfileName, instructions, (isDestinationTemporary?"temporary ":"destination"), destfileName);
		
		if (StringUtility.isStringEmpty(srcfileName) || StringUtility.isStringEmpty(destfileName) || instructions == null || instructions.size() == 0){
			throw new IllegalStateException(ICommonConstants.EXCEPTION_INVALID_INPUT);		
		}

		try {
			for(ModifyInstruction instruction: instructions){
				if(instruction!=null){
					if(InstructionType.SERIAL.equals(instruction.getInstructionType())){
						if(serialInstructions == null){
							serialInstructions = new LinkedList<FileModifier.ModifyInstruction>();
						}
						serialInstructions.add(instruction);
					}
					else {
						if(freeInstructions == null){
							freeInstructions = new LinkedList<FileModifier.ModifyInstruction>();
						}
						freeInstructions.add(instruction);
					}
				}
			}
			
			
			srcPR = new FileSystemResource(srcfileName);
			Path srcfilePath = Paths.get(srcPR.getURI());
			srcFileReader = new FileReader(srcPR.getFile());
			reader = new BufferedReader(srcFileReader);

			Path tempfilePath = Paths.get(destfileName);
			writer = Files.newBufferedWriter(tempfilePath,	StandardCharsets.UTF_8);

			String line;
			int serialInstructionIndex = 0;
			int freeInstructionIndex = 0;
			
			int serialChangeCount = 1;
			int freeChangeCount = 1;
			
			boolean hasInstructionToExecute = true;
			
			while ((line = reader.readLine()) != null) {
				String modLine = line;
				ModifyInstruction serialInstruction = null;
				ModifyInstruction freeInstruction = null;
				
				if(hasInstructionToExecute){
					if(serialInstructions!=null && serialInstructions.size()>serialInstructionIndex){
						serialInstruction = serialInstructions.get(serialInstructionIndex);
					}
					if(freeInstructions!=null && freeInstructions.size()>freeInstructionIndex){
						freeInstruction = freeInstructions.get(freeInstructionIndex);
					}
					if(serialInstruction == null && freeInstruction == null){
						hasInstructionToExecute = false;
					}
				}
						
				if(hasInstructionToExecute){
					ValidityType validity = isValid(serialInstruction, freeInstruction, line);
					
					if (!ValidityType.INVALID.equals(validity)) {
						OperationType operation;
						String value;
						
						//For Serial instruction
						if(ValidityType.SERIALLY_VALID.equals(validity)){
							operation = serialInstruction.getOperationType();
							value = serialInstruction.getValue();
							
							if (serialInstruction.getMaxChangecount() != MAX_COUNT_UNLIMITED && serialChangeCount >= serialInstruction.getMaxChangecount()) {
								serialInstruction.setExecutionOver(true);
								serialInstructionIndex++;
								serialChangeCount = 1;
							}
							else{
								serialChangeCount++;
							}
						}
						
						//For free instruction
						else{
							operation = freeInstruction.getOperationType();
							value = freeInstruction.getValue();
							
							if (freeInstruction.getMaxChangecount() != MAX_COUNT_UNLIMITED && freeChangeCount >= freeInstruction.getMaxChangecount()) {
								freeInstruction.setExecutionOver(true);
								freeInstructionIndex++;
								freeChangeCount = 1;
							}
							else {
								freeChangeCount++;
							}
						}
						
						modLine = (OperationType.REMOVE.equals(operation)) ? line.replaceAll(value, ICommonConstants.BLANK_STRING) : 
										StringUtility.appendStrings(line,	value);
						
						LOGGER.info("A line in {} has been modified from {} to {}", srcfileName, line, modLine);
						line = modLine;						
					}
				}
					
				writer.write(line, 0, line.length());
				writer.newLine();
			}
			
			reader.close();
			srcFileReader.close();
			writer.close();

			if(isDestinationTemporary) {
				Files.move(tempfilePath, srcfilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			isSucceed = true;
		} finally {
			try{
				if (reader != null){
					reader.close();
				}
				if (srcFileReader != null){
					srcFileReader.close();
				}
				if (writer != null){
					writer.close();
				}
			}catch(IOException e){
				LOGGER.error("Error occurred while cleaning up : ", e);
			}
		}
		
		LOGGER.info("Modification of {} file is {}", srcfileName, (isSucceed?"successfully completed.":"completed with error!"));
		return isSucceed;
	}
	
	
	
	
	/********************************* Private variables & methods **********************************************/
	
	private int execSequence = 0;
	
	private enum ValidityType{
		SERIALLY_VALID,
		FREELY_VALID,
		INVALID
	}
	
	/**
	 * Utility method to check whether instruction is valid
	 * 
	 * @param serialInstruction
	 * 			instance of ModifyInstruction (serial)
	 * @param freeInstruction
	 * 			instance of ModifyInstruction (free)
	 * @param line
	 * 			Line in file
	 * @return ValidityType	
	 * 
	 * @see com.comcast.test.citf.common.helpers.FileModifier.ValidityType
	 */
	private static ValidityType isValid(ModifyInstruction serialInstruction, ModifyInstruction freeInstruction, String line){
		ValidityType valid = ValidityType.INVALID;
		String[] includeORFilter;
		String[] excludeORFilter;
		
		if(freeInstruction!=null){
			includeORFilter = freeInstruction.getIncludeORFilter();
			excludeORFilter = freeInstruction.getExcludeORFilter();
			
			int inFltrRsult = filter(includeORFilter, line);
			int excldFltrResult = UNCHECKED;
			if(excludeORFilter!=null && inFltrRsult==CHECKED_VALID){
				excldFltrResult = filter(excludeORFilter, line);
			}
			if(inFltrRsult==CHECKED_VALID && (excldFltrResult==UNCHECKED || excldFltrResult==CHECKED_INVALID)) {
					valid = ValidityType.FREELY_VALID;			
			}
		}
		
		if(serialInstruction!=null && ValidityType.INVALID.equals(valid)){
			includeORFilter = serialInstruction.getIncludeORFilter();
			excludeORFilter = serialInstruction.getExcludeORFilter();
			
			int inFltrRsult = filter(includeORFilter, line);
			int excldFltrResult = UNCHECKED;
			if(excludeORFilter!=null && inFltrRsult==CHECKED_VALID) {
				excldFltrResult = filter(excludeORFilter, line);
			}
			if(inFltrRsult==CHECKED_VALID && (excldFltrResult==UNCHECKED || excldFltrResult==CHECKED_INVALID)) {
				valid = ValidityType.SERIALLY_VALID;		
			}
		}
		return valid;
	}
	
	/**
	 * Utility method to filter
	 * 
	 * @param filterList
	 * 			Array of filter strings
	 * @param line
	 * 			Line in file
	 * @return filter result 
	 */
	private static int filter(String[] filterList, String line){
		int result = UNCHECKED;
		boolean isValid = false;
		boolean includeFilterChecked = false;
		
		if(filterList!=null){
			for(String inFilter : filterList){
				if(inFilter.contains(ICommonConstants.COMMA)){
					Set<String> filterTokens = StringUtility.getTokensFromString(inFilter, ICommonConstants.COMMA);
					if(filterTokens!=null && filterTokens.size()>0){
						isValid = true;
						for(String filterToken : filterTokens){
							if(!line.contains(filterToken)){
								isValid = false;
								break;
							}
						}
							
						if(isValid){
							includeFilterChecked = true;
							break;
						}
					}
				}
				else{
					isValid = true;
					if(line.contains(inFilter)){
						includeFilterChecked = true;
						break;
					}								
					else {
						isValid = false;
					}
				}
				
			}
			
			if(!isValid) {
				includeFilterChecked = true;
			}
		}
		
		if(includeFilterChecked){
			result = isValid ? CHECKED_VALID : CHECKED_INVALID;
		}
		
		return result;
	}
	
	private static final int CHECKED_INVALID = 0;
	private static final int CHECKED_VALID = 1;
	private static final int UNCHECKED = 2;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileModifier.class);
}
